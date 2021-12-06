'use strict';
import {requests} from './requests.js';
let intervalID = -1;//id of current interval
let webSocket;
let pingId;
let lastSelectedMem;
//const webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chatapp");
const UserList = ["Owner", "Admin1", "Admin2", "Member1", "Member2"];
const RoomList = [{roomName: "COMP504", type: "public", limit: 200, cur: 180},
    {roomName: "Amazon", type: "public", limit: 150, cur: 70},
    {roomName: "Google", type: "public", limit: 100, cur: 50},
    {roomName: "Group D", type: "private", limit: 100, cur: 50},
    {roomName: "Amazon", type: "public", limit: 150, cur: 70}];
const notiList = [{sender: "qwe", receiver: "asd", roomName: "zxc"}];
const emojiLib = [
    "😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣", "😊", "😇", "🙂", "🙃", "😉", "😌",
    "😍", "🥰", "😘", "😗", "😙", "😚", "😋", "😛", "😝", "😜", "🤪", "🤨", "🧐", "🤓", "😎",
];


/**
 * Entry point into chat room
 */
window.onload = function () {

    setUsername();
    //loadRoomUser();
    hideRoomInfo();
    updateRoomList();
    clearInterval(intervalID);
    intervalID = setInterval(updateRoomList, 3000);
    let member_btn = $(".member_btn");
    $("#search_btn").click(doSearch);
    member_btn.focus(doSomething);
    $("#join_room").click(joinRooms);
    $("#inputArea").keydown(onKeyPress);
    $("#chatStart").click(createUserChat);
    $(".members").on('click', 'button', function (){
        lastSelectedMem = $(this).text();
    });
    $("#btn-join").click(getAllRooms);
    $("#btn-chat").click(getAllUsers);
    $("#btn-clear").click(sendMessage);
    $("#btn-invite").click(getInviteUsers);
    $("#inviteReq").click(inviteIntoRoom);
    $("#btn-mute").click(mute);
    $("#btn-kick").click(kick);
    $("#btn-block").click(getBlockUsers);
    $("#blockReq").click(blockInRoom);
    $("#btn-history").click(showRoomHistory);
    $("#btn-leave").click(leaveRoom);
    $("#btn-leaveAll").click(leaveAll);
    //$("#notificationInfo").click(getNotification);
    $("#notificationArea").on('click', '.invite_ac', acceptInvite);
    $("#notificationArea").on('click', '.invite_rj', rejectInvite);
    $("#btn-logout").click(doLogOut);
    $(".rooms").on('click', 'button', function (){
        hideRoomInfo();
        showRoomInfo();
        $("#chat-content").empty();
        $("#roomName").text($(this).text());
        $("#roomName").val($(this).text().replace(/[\r\n]/g,"").replace(/[ ]/g,""));
        $.post('/roomInfo', {roomName: $("#roomName").text().replace(/[\r\n]/g,"").replace(/[ ]/g,"")}, function (data){
            console.log(data);
            data = JSON.parse(data);
            console.log(data.interest);
            if(data.interest === "") $("#roomDescription").text("This is about everything");
            else $("#roomDescription").text("This is about " + data.interest);
            $("#limitRoomNum").text(data.userLimit);
            if(data.type === "userchat") {
                $("#curRoomNum").text(2);
                /*$("#btn-kick").removeAttr("disabled");
                $("#btn-mute").removeAttr("disabled");
                $("#btn-block").removeAttr("disabled");*/
            }
            else{
                console.log(data.owner, $("#user_name").val());
                $("#curRoomNum").text(data.curNumUser);
                if(data.owner === $("#user_name").val()) {
                    $("#btn-kick").removeAttr("disabled");
                    $("#btn-mute").removeAttr("disabled");
                    $("#btn-block").removeAttr("disabled");
                    $("#btn-invite").removeAttr("disabled");
                }
            }
        })
        console.log($("#roomName").text().replace(/[\r\n]/g,"").replace(/[ ]/g,""));
        loadRoomUser();

    });
    $(document).on("click", "#btn_createRoomSave", createGroupChat);
    $(document).on("click", "#btn_createRoomCancel", clearCreateForm);

    //$('#createModal').on('show.bs.modal', clearCreateForm);
    $("#select-emoji").change(insertEmoji);
    for (let i = 0; i < emojiLib.length; i++) {
        addOption($("#select-emoji"), emojiLib[i], emojiLib[i], "emoji-" + emojiLib[i]);
    }
};
/**
 * leavebutton
 */
function leaveRoom() {
    webSocket.send(JSON.stringify(requests.getLeaveRequest(
        $("#user_name").val(),
        $("#roomName").text().replace(/[\r\n]/g,"").replace(/[ ]/g,"")
    )));
}

/**
 * leaveAllButton
 */
function leaveAll(){
    webSocket.send(JSON.stringify(requests.getLeaveAllRequest(
        $("#user_name").val()
    )));
}
/**
 * mute button
 */
function mute(){
    console.log(lastSelectedMem);
    console.log(lastSelectedMem.indexOf($("#user_name").val()));
    if(lastSelectedMem !== "" && lastSelectedMem.indexOf($("#user_name").val()) === -1){
        console.log(1);
        webSocket.send(JSON.stringify(requests.getMuteRequest(
            $("#roomName").text().replace(/[\r\n]/g,"").replace(/[ ]/g,""),
            lastSelectedMem
        )));
    }
}
function kick(){
    if(lastSelectedMem !== "" && lastSelectedMem.indexOf($("#user_name").val()) === -1){
        webSocket.send(JSON.stringify(requests.getKickRequest(
            $("#roomName").text().replace(/[\r\n]/g,"").replace(/[ ]/g,""),
            lastSelectedMem
        )));
    }
}
/**
 * invite button get users
 */
function inviteIntoRoom(){
    //console.log($("#curRoomNum").text(),$("#limitRoomNum").text())
    if($("#curRoomNum").text() !== $("#limitRoomNum").text()) {
        /*$.post("/invite", {sender: $("#user_name").val(), receiver: $("#inviteTo").text(), roomName:$("#roomName").text().replace(/[\r\n]/g,"").replace(/[ ]/g,"")}, function (){
            $("#inviteModal").hide();
        })*/
        console.log(1);
        $("#inviteModal").modal('hide');
        webSocket.send(JSON.stringify(requests.getInviteRequest(
            $("#user_name").val(),
            $("#roomName").text().replace(/[\r\n]/g,"").replace(/[ ]/g,""),
            $("#inviteTo").text()
        )));
    }
}
function getInviteUsers(){
    $("#roomNameInInvite").text($("#roomName").text().replace(/[\r\n]/g,"").replace(/[ ]/g,""));
    webSocket.send(JSON.stringify(requests.getInviteUsersRequest(
        $("#user_name").val(),
        $("#roomName").text().replace(/[\r\n]/g,"").replace(/[ ]/g,"")
    )));
    /*$.post("/invite/getUsers", {roomName: $("#roomName").text().replace(/[\r\n]/g,"").replace(/[ ]/g,"")}, function (data){
        console.log(data);// list of room info about name, type, limit, num
        //data = UserList;

        let inviteTable = $("#inviteTable");
        inviteTable.empty();
        let html = "";
        for(let i = 0; i < data.length; i++){
            html += "<tr><th scope=\"row\"><input type=\"radio\" name=\"invite\"></th><td>" +
                data[i].username + "</td></tr>";
        }
        inviteTable.append(html);
        $("input:radio[name='invite']").change(function (){
            let opt = $("input:radio[name='invite']:checked").parent("th").next("td").text();
            $("#inviteTo").text(opt);
            $("#inviteReq").removeAttr("disabled");
        });
    })*/
}
function getBlockUsers(){
    $("#roomNameInBlock").text($("#roomName").text().replace(/[\r\n]/g,"").replace(/[ ]/g,""));
    webSocket.send(JSON.stringify(requests.getBlockUsersRequest(
        $("#user_name").val(),
        $("#roomName").text().replace(/[\r\n]/g,"").replace(/[ ]/g,"")
    )));
}
function blockInRoom(){
    webSocket.send(JSON.stringify(requests.getBlockRequest(
        $("#roomName").text().replace(/[\r\n]/g,"").replace(/[ ]/g,""),
        $("#user_name").val(),
        $("#blockIn").text()
    )));
}
function showRoomHistory(){
    webSocket.send(JSON.stringify(requests.getHistoryRequest(
        $("#roomName").text().replace(/[\r\n]/g,"").replace(/[ ]/g,"")
    )));
}
/**
 * Get Notifications.
 */
function getNotification() {
    webSocket.send(JSON.stringify(requests.getNotificationRequest(
        $("#user_name").val()
    )));
}

/**
 * Render messages.
 */
function renderMsg(data, message) {
    console.log($("#user_name").val());
    if (!data/*data.msgType === 'JoinRoom' || data.msgType === 'LeaveRoom' ||
        data.msgType === 'RecallMsg' || data.msgType === 'DeleteMsg'*/) {
        //$("#messageList").append(`<option class="msg-sys-info" id=${data.msgId} value=${data.from}>${"System Info: " + data.msg} </option>`);
        console.log("abc");
    } else if (data.username === $("#user_name").val()) {
        console.log("me");
        let context = message.body;
        let timestamp = message.timestamp;
        let username = message.sendUser;
        $("#chat-content").append("<li class=\"clearfix\">" +
            "<div class=\"message-data text-right\">" +
        "<span class=\"message-data-time\">" + username + ", " + timestamp + " Status: Readed "+"</span>" +
        "</div>" +
        "<div class=\"message other-message float-right\">" +
            "<button type = \"button\" style = \"outline: none\" class = \"text_btn outline-none\"" +
            "data-container = \"body\" data-toggle = \"popover\" data-trigger = \"hover focus\"" +
            "data-placement = \"top\" >" + context + "</button>" +
            "</div>" +
        "</li>");
    } else {
        console.log("other");
        let context = message.body;
        let timestamp = message.timestamp;
        let username = message.sendUser;
        $("#chat-content").append("<li class=\"clearfix\">" +
            "<div class=\"message-data\">" +
            "<span class=\"message-data-time\">" + username + ", " + timestamp + "</span>" +
            "</div>" +
            "<div class=\"message my-message\">" +
            "<button type = \"button\" style = \"outline: none\" class = \"text_btn outline-none\"" +
        "data-container = \"body\" data-toggle = \"popover\" data-trigger = \"hover focus\"" +
        "data-placement = \"top\" >" + context + "</button>" +
            "</div>" +
            "</li>"
        );
    }
    $('[data-toggle="popover"]').popover({
        html: true,
        sanitize: false,
        content: '<button type="button" id="button-image" class="popover_btn btn-primary">recall</button>' +
            '<button type="button" id="button-image" class="popover_btn btn-danger">delete</button>'
    });
}

/**
 * Handle the Responses
 */
function responseHandler(message) {
    const data = JSON.parse(message.data);
    console.log(data);
    const msgType = data.action;
    console.info("data received " + msgType);
    switch (msgType) {
        case 'send':
            console.log(data.username);
            console.log(data.room);
            let message = JSON.parse(data.message);
            console.log("childMsg: " , message);
            console.log(message.childrenMessage[0]);
            console.log(message.childrenMessage[0].body);
            console.log("room title = " + $("#roomName").val());
            if (data.room === $("#roomName").val()) {
                renderMsg(data, message.childrenMessage[0]);
                getNotification();
            }
            break;
        case 'invite':
            console.log(2);
            console.log("reach invite");
            getNotification();
            loadRoomUser();
            //$("#inviteModal").hide();
            break;
        case 'getInviteUsers':
            console.log(data);
            let msg = JSON.parse(data.message);
            let inviteTable = $("#inviteTable");
            inviteTable.empty();
            let html = "";
            for(let i = 0; i < msg.length; i++){
                console.log(msg[i]);
                html += "<tr><th scope=\"row\"><input type=\"radio\" name=\"invite\"></th><td>" +
                    msg[i].username + "</td></tr>";
            }
            inviteTable.append(html);
            $("input:radio[name='invite']").change(function (){
                let opt = $("input:radio[name='invite']:checked").parent("th").next("td").text();
                $("#inviteTo").text(opt);
                $("#inviteReq").removeAttr("disabled");
            });
            break;
        case 'getBlockUsers':
            let blockMsg = JSON.parse(data.message);
            let blockTable = $("#blockTable");
            blockTable.empty();
            let blockHtml = "";
            for(let i = 0; i < blockMsg.length; i++){
                console.log(blockMsg[i]);
                blockHtml += "<tr><th scope=\"row\"><input type=\"radio\" name=\"block\"></th><td>" +
                    blockMsg[i] + "</td></tr>";
            }
            blockTable.append(blockHtml);
            $("input:radio[name='block']").change(function (){
                let opt = $("input:radio[name='block']:checked").parent("th").next("td").text();
                $("#blockIn").text(opt);
                $("#blockReq").removeAttr("disabled");
            });
            break;
        case 'leave':
            console.log("leave", data);
            if (data.message === "true"){
                updateRoomList();
                $(".members .member_btn").remove();
                $("#welcome").show();
                $("#chat-content").empty();
                $("#roomDescription").text("Description");
                hideRoomInfo();
                $("#leaveInfo").text("Leave Success");
                loadRoomUser();
            }
            else {
                $("#leaveInfo").text("Leave Fail");
            }
            //console.log("leave")
            updateRoomList();
            break;
        case 'leaveAll':
            updateRoomList();
            $(".members .member_btn").remove();
            $("#welcome").show();
            $("#chat-content").empty();
            $("#roomDescription").text("Description");
            hideRoomInfo();
            break;
        case 'mute':
            console.log("mute success");
            loadRoomUser();
            getNotification();
            break;
        case 'kick':
            console.log("kick success");
            loadRoomUser();
            getNotification();
            break;
        case 'notification':
            console.log(data.notificationList);
            let me = JSON.parse(data.notificationList);
            console.log(me);
            if(me.length === 0){
                renderNotification(me.length);
            }
            for (let i = 0; i < me.length; i++) {
                console.log("current notification");
                renderNotification(me[i]);
            }
            break;
        case 'updateMessage':
            console.log(data);
            let historyMsg = JSON.parse(data.messages);
            console.log(historyMsg);
            let history = $("#historyList");
            history.empty();
            let historyHtml = "";
            for(let i = 0; i < historyMsg.length; i++){
                console.log(historyMsg[i]);
                historyHtml += "<li>(" +  historyMsg[i].timestamp + ") " +  historyMsg[i].sendUser + ": " + historyMsg[i].childrenMessage[0].body + "</li>";
            }
            history.append(historyHtml);
            break;
        case 'acceptInvite':
            getNotification();
            console.log("accept invite");
            break;
        case 'rejectInvite':
            getNotification();
            console.log("reject invite");
            break;
        default:
            console.info("Missing type: " + msgType);
    }
}

function renderNotification(notification) {
    console.log("begin notification rendering");
    $("#notificationArea").empty();
    console.log(notification.type);
    if (!notification) {
        console.log("current notification is empty");
    } else if (notification.hasButton) {
        console.log("this notification has button");
        let senderUser = notification.sender;
        let info = notification.info;
        let type = notification.type;
        $("#notificationArea").append("<div class=\"row w-100 mb-1\">" +
            "<div class=\"col-6\"><span class=\"inviteSender\">" + senderUser + " " +
            "</span>" + type + " you to <span class=\"inviteRoomName\">" + info + "</span></div>" +
            "<button type=\"button\" class=\"btn btn-outline-success btn-sm col-1 p-0 mr-2 invite_ac\" style=\"border: none\">accept</button>" +
            "<button type=\"button\" class=\"btn btn-outline-danger btn-sm col-1 p-0 ml-2 invite_rj\" style=\"border: none\">reject</button>" +
            "</div>");
    } else {
        // This notification does not have button
        console.log("this notification does not have button");
        let senderUser = notification.sender;
        let info = notification.info;
        let type = notification.type;
        $("#notificationArea").append("<div class=\"row w-100 mb-1\">" +
            "<div class=\"col-6\"><span class=\"inviteSender\">" + senderUser + " " +
            "</span>" + type + " you to <span class=\"inviteRoomName\">" + info + "</span></div>" +
            "</div>");
    }
}

function sendM() {
    let tempUsername = $("#user_name").val();
    let tempRoomName = $("#roomName").text().replace(/[\r\n]/g,"").replace(/[ ]/g,"");
    let tempInputArea = $("#inputArea").val();
    if (tempUsername !== "" && tempRoomName !== "" && tempInputArea !== "") {
        webSocket.send(JSON.stringify(requests.getSendMsgRequest(
            tempUsername,
            tempRoomName,
            tempInputArea
        )));
    }
    $("#inputArea").val("");
}

function hideRoomInfo(){
    $("#roomInfo").hide();
    $("#inputArea").attr("disabled", "true");
    $("#btn-block").attr("disabled", "true");
    $("#btn-kick").attr("disabled", "true");
    $("#btn-mute").attr("disabled", "true");
    $("#btn-invite").attr("disabled", "true");
    $("#btn-history").attr("disabled", "true");
    $("#btn-leave").attr("disabled", "true");
}

function showRoomInfo(){
    $("#welcome").hide();
    $("#roomInfo").show();
    $("#inputArea").removeAttr("disabled");
    $("#btn-history").removeAttr("disabled");
    $("#btn-leave").removeAttr("disabled");
}

/**
 * A function to add options to a list.
 * @param list target HTML list
 * @param value source value
 * @param inner target value
 * @param id option id
 */
function addOption(list, value, inner, id) {
    list.append(`<option id=${id} value=${value}>${inner} </option>`);
}
/**
 * insert emoji
 */
function insertEmoji() {
    const selected = $("#select-emoji option:selected");
    const input = $("#inputArea");
    input.val(input.val() + selected.text());
}
/**
 * click inner chat button to begin chat
 * */
function createUserChat() {
    $.post("/create/userchat", {username: $("#user_name").val(), chatName: $("#chatWith").text()}, function (data) {
        console.log(data);
        if (data === true) {
            updateRoomList();
            $("#chatModal").modal('hide');
        }
        else {
            //notify
        }
    }, "json")
}
/**
 * Update the room list of the current user once per 3 seconds.
 */
function updateRoomList() {
    $.get("/room/update", {username: $("#user_name").val()}, function (data) {
        //console.log(data);
        //data = RoomList;
        // clear the room list first
        let rooms = $(".rooms");
        rooms.empty();
        // update the room list from the data
        let html = "";
        for(let i = 0; i< data.length; i++){
            html += "<span class=\"text\">\n" +
                "                <button class=\"room_btn btn-outline-primary p-0 m-0 u_btn\">\n" +
                data[i].roomName +
                "                </button>\n" +
                "            </span><br>";
        }
        rooms.append(html);
    }, "json");
}
/**
 * click invite notification accept
 * */
function acceptInvite() {
    console.log($(this).siblings("div").children(".inviteSender").text(), $(this).siblings("div").children(".inviteRoomName").text());
    webSocket.send(JSON.stringify(requests.acceptInviteRequest(
        $(this).siblings("div").children(".inviteSender").text().replace(/[\r\n]/g,"").replace(/[ ]/g,""),
        $(this).siblings("div").children(".inviteRoomName").text(),
        $("#user_name").val()
    )));
    console.log("1");
    getNotification();
    /*$.post("/notification/invite/accept", {receiver: $("#user_name").val(),
        sender: $(this).siblings("div").children(".inviteSender").text(),
        roomName: $(this).siblings("div").children(".inviteRoomName").text(),type: true}, function (data) {
        getNotification();
        updateRoomList();
    }, "json")*/
}
/**
 * click invite notification reject
 */
function rejectInvite() {
    console.log($(this).siblings("div").children(".inviteSender").text(), $(this).siblings("div").children(".inviteRoomName").text());
    webSocket.send(JSON.stringify(requests.rejectInviteRequest(
        $(this).siblings("div").children(".inviteSender").text().replace(/[\r\n]/g,"").replace(/[ ]/g,""),
        $(this).siblings("div").children(".inviteRoomName").text(),
        $("#user_name").val()
    )));
    getNotification();
    /*$.post("/notification/invite/reject", {receiver: $("#user_name").val(),
        sender: $(this).siblings("div").children(".inviteSender").text(),
        roomName: $(this).siblings("div").children(".inviteRoomName").text(), type: false}, function (data) {
        getNotification();
        updateRoomList();
    }, "json")*/
}
/**
 * outside notification button
 * */
/*function getNotification() {
    $.post("/user/notification", {username: $("#user_name").val()}, function (data) {
        let notifications = $("#notificationBody");
        //clear
        //get

    }, "json")
}*/
/**
 * inside join button
 * */
function joinRooms(){
    $.post("/join/group", {username: $("#user_name").val(), roomName: $("#roomSelection").text()}, function (data) {
        console.log(data);
        updateRoomList();
        $("#joinModal").modal('hide');
        if (data === true) {
            updateRoomList();
            $("#joinModal").modal('hide');
        }
        else {
            //notify
        }
    }, "json")
}

/**
 * outside chat button
 * */
function getAllUsers(){
    $.post("/chat/getUsers", {username: $("#user_name").val()}, function (data) {

        console.log(data);// list of room info about name, type, limit, num
        //data = UserList;

        let userTable = $("#userTable");
        userTable.empty();
        $("#chatWith").text("");
        let html = "";
        for(let i = 0; i < data.length; i++){
            html += "<tr><th scope=\"row\"><input type=\"radio\" name=\"chat\"></th><td>" +
                data[i].username + "</td></tr>";
        }
        userTable.append(html);
        $("input:radio[name='chat']").change(function (){
            let opt = $("input:radio[name='chat']:checked").parent("th").next("td").text();
            console.log(opt);
            $("#chatWith").text(opt);
            $("#chatStart").removeAttr("disabled");
        });
    }, "json");
}

/**
 * outside join button
 * */
function getAllRooms(){
    $.post("/join/getRooms", {username: $("#user_name").val()}, function (data) {

        console.log(data);// list of room info about name, type, limit, num
        //data = RoomList;
        let roomTable = $("#roomTable");
        $("#roomSelection").text("");
        roomTable.empty();
        let html = "";
        for(let i = 0; i < data.length; i++){
            html += "<tr><th scope=\"row\"><input type=\"radio\" name=\"join\"></th><td>" +
                data[i].roomName + "</td><td>" +(data[i].isPublic === true?"public": "private") +
                "</td><td>"+data[i].curNumUser+"/"+data[i].userLimit+"</td></tr>";
        }
        roomTable.append(html);
        $("input:radio[name='join']").change(function (){
            let opt = $("input:radio[name='join']:checked").parent("th").next("td").text();
            $("#roomSelection").text(opt);
            $("#join_room").removeAttr("disabled");
        });
    }, "json");
}

function loadRoomUser() {
    $.get("/room/members", {username: $("#user_name").val(), roomname: $("#roomName").text().replace(/[\r\n]/g,"").replace(/[ ]/g,"")}, function (data){
        console.log(JSON.parse(data));
        data = JSON.parse(data);
        console.log(data[0])
        $(".members .member_btn").remove();
        let html = '';
        for(let i = 0; i < data.length; i++){
            html += "<button type='radio' name='member' class=\"member_btn btn-outline-primary\" id='" + data[i] +"'>" + data[i] + "</button>";
        }
        $(".members").append(html);
    })

}

function doSearch() {
    let sh_input = $("#search_input").val();
    console.log(sh_input);
    if(sh_input === ""){
        for(let i = 0; i < UserList.length; i++){
            $("#" + UserList[i]).show();
        }
    }
    else{
        for(let i = 0; i < UserList.length; i++){
            if(UserList[i].indexOf(sh_input) >= 0) $("#" + UserList[i]).show();
            else $("#" + UserList[i]).hide();
        }
    }
}

function doSomething(){
    let username = $("#user_name").text().replace(/[\r\n]/g,"").replace(/[ ]/g,"");
    console.log(username);
    if(username === "Owner" || username === "Admin1" || username === "Admin2"){

        let btn = $(this).text();
        if(btn === "Member1" || btn === "Member2"){
            $('#btn-kick').removeAttr("disabled");
            $('#btn-mute').removeAttr("disabled");
        }
    }
}

function ping() {
    webSocket.send("ping");
}



/**
 * Set up websocket.
 */
async function setUpWebSocket() {
    webSocket = new WebSocket("wss://" + location.hostname + ":" + location.port + "/chatapp");
    //"wss://" + location.hostname + ":" + location.port + "/chatapp"
    await new Promise((resolve) => {
        webSocket.onopen = (e) => {
            resolve(e.data);
            //pingId = setInterval(, 29000);
        }
    })
    /*webSocket.onopen = () => {
        //pingId = setInterval(, 29000);
    }*/
    webSocket.onmessage = (msg) => responseHandler(msg);
    webSocket.onclose = () => {
        setTimeout(setUpWebSocket, 2000);
    }
    webSocket.onerror = () => {
        setTimeout(setUpWebSocket, 2000);
    }
    webSocket.send(JSON.stringify(requests.getLoginRequest(
        $("#user_name").val(),
    )));
}

/**
 * Set the username from the landing page.
 */
function setUsername() {
    if ($("#user_name").val() == "") {
        console.log("set the username successfully");
        $("#user_name").text(localStorage.getItem("username"));
        $("#user_name").val(localStorage.getItem("username"));
        $.post("/userInfo", {username: $("#user_name").val()},function (data) {
            data = JSON.parse(data);
            $("#age").text(data.age);
            $("#school").text(data.school);
            let newText = "";
            for (let i = 0; i < data.interests.length; i++) {
                if(i === data.interests.length - 1) newText += data.interests[i];
                else newText += data.interests[i] + ", ";
            }
            $("#interests").text(newText);
        });
    } else {
        console.log("do not reset the username");
    }
    setUpWebSocket();
}

/**
 * Clear the create form.
 */
function clearCreateForm() {
    document.getElementById("new_room_name").value = "";
    document.getElementById("interest").value = "";
    document.getElementById("maxUser").value = 1;
    document.getElementById("password").value = "";
    document.getElementById("isPublic").checked = true;
    document.getElementById("new_room_nameAlert").innerText = "";
    document.getElementById("interestAlert").innerText = "";
    document.getElementById("passwordAlert").innerText = "";

}

/**
 * Create a new group chat room.
 */
function createGroupChat() {
    checkCreateRoom();
    if(checkCreateRoomComplete()) {
        $.post("/create/groupchat", {
            username: $("#user_name").val(),
            roomName: $("#new_room_name").val(),
            interest: $("#interest").val(), // only one interest for each room
            maxUser: $("#maxUser").val(),
            isPublic: $("#isPublic").is(':checked'),
            password: $("#password").val()
        }, function (data) {
            console.log(data);
            updateRoomList();
            if (data === true) { // the size of the roomList increases
                console.log(data);
                console.log("create room success");
                clearCreateForm();
                $("#createModal").modal('hide');
            } else {
                document.getElementById("new_room_nameAlert").innerText = "Exist room name, create room fail";
                console.log("create room fail");
            }
        }, "json");
    }
}

/**
 * Logout the account, and redirect to the landing page.
 */
function doLogOut() {
    $.get("/logout", {username: $("#user_name").val()}, function (data) {
        // this username is the name on the top-left corner of main.html
        // in this case, controller can do something according to this username
        console.log(data);
        // it should be always true, just keep it for testing the controller
        if (data === true) {
            window.location.href = "index.html"
        }
    }, "json");
}
/**
 * Send a message to the server.
 */
function sendMessage() {
    sendM();
    /*$.post("/sendMessage", {
    let message = $("#inputArea").val();
    for(let i = 0; i < emojiLib.length; i++) {
        message = message.replace(emojiLib[i],":sep:" + "/" + emojiLib[i]);
    }
    $.post("/sendMessage", {
        username: $("#user_name").val(),
        roomName:document.getElementById("title").innerText,
        message:message
    }, function (data) {
        if(data === true) {
            console.log("ready to send");
            $('#inputArea').val('').focus();
        } else {
            console.log("will not be sent");
        }
        /!*
        if (msg !== "") {
            webSocket.send(msg);
            $("#message").val("");
        }
        *!/
    }, "json");*/
}
/**
 * Press Enter button, then send the message.
 */
function onKeyPress() {
    let event = window.event;
    event = event || window.event;
    if (event.which === 13) {
        event.preventDefault();
        event.returnValue = false;
        console.log("onKeyPress");
        sendMessage();
    }
}

/**
 * Room name should not be empty when creating a chat room.
 */
function validateCreateRoomName() {
    var a = document.getElementById("new_room_name");
    var a2 = document.getElementById("new_room_nameAlert");
    if (a.value.trim() === "") {
        a2.innerText = "   !";
        return false;
    }
    else {
        a2.innerText = ": )";
    }
    return true;
}

/**
 * The interest for a chat room could be empty or words seperated by comma.
 */
function validateCreateInterest() {
    var a = document.getElementById("interest");
    var a2 = document.getElementById("interestAlert");
    if (a.value.trim() === "") {
        a2.innerText = "No interest is OK";
    }
    else if(a.validity.patternMismatch && a.value.trim() !== "") {
        a2.innerText = "Interest should be a word";
    }
    else {
        a2.innerText = ": )";
    }
    return true;
}

/**
 * A public chat room do not need a password but a private chat room needs.
 */
function validateCreatePassword() {
    var a = document.getElementById("password");
    var a2 = document.getElementById("passwordAlert");
    //console.log(typeof $("#isPublic").is(':checked')); //--boolean
    if($("#isPublic").is(':checked') === false) {
        console.log("a.value" + a.value);
        if(a.value.trim() === "") {
            a2.innerText = "!";
            return false;
        }
        else{
            a2.innerText = ": )";
            return true;
        }
    }
    else{
        a2.innerText = ": )";
        return true;
    }
}

/**
 * A function to go through all checks on the create room page.
 */
function checkCreateRoom() {
    validateCreateRoomName();
    validateCreateInterest();
    validateCreatePassword();
}

function checkCreateRoomComplete() {
    if($("#new_room_name").val().trim() === "") {
        return false;
    } else {
        if($("#isPublic").is(':checked') === true) {
            return true;
        } else {
            if($("#password").val().trim() === "") {
                return false;
            } else {
                return true;
            }
        }
    }
}
/**
 * Enter a chatRoom
 */
/*
function tempFunc() {
    document.getElementById("roomName").innerText = "Room 1"
}

function tempFunc2() {
    document.getElementById("roomName").innerText = "Room 2"
}

function tempFunc3() {
    document.getElementById("roomName").innerText = "Room 3"
}

function tempFunc4() {
    document.getElementById("roomName").innerText = "User 1"
}

function tempFunc5() {
    document.getElementById("roomName").innerText = "User 2"
}*/
