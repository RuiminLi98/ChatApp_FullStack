const getSendMsgRequest = (username, roomName, message) => ({
    username: username,
    //timeStamp: new Date().getTime(),
    roomName: roomName,
    message: message,
    action: "send"
});

const getLoginRequest = (username) => ({
    username: username,
    //timeStamp: new Date().getTime(),
    action: "login"
});

const getInviteRequest = (username, roomName, otherName) => ({
    userSendInvite: username,
    roomName: roomName,
    userGetInvite: otherName,
    //timeStamp: new Date().getTime(),
    action: "invite"
});

const acceptInviteRequest = (username, roomName, otherName) => ({
    userSendInvite: username,
    roomName: roomName,
    userGetInvite: otherName,
    //timeStamp: new Date().getTime(),
    action: "acceptInvite"
});

const rejectInviteRequest = (username, roomName, otherName) => ({
    userSendInvite: username,
    roomName: roomName,
    userGetInvite: otherName,
    //timeStamp: new Date().getTime(),
    action: "rejectInvite"
});

const getInviteUsersRequest = (username, roomName) => ({
    userSendInvite: username,
    roomName: roomName,
    //timeStamp: new Date().getTime(),
    action: "getInviteUsers"
});

const getBlockUsersRequest = (username, roomName) => ({
    userSendInvite: username,
    roomName: roomName,
    //timeStamp: new Date().getTime(),
    action: "getBlockUsers"
});

const getMuteRequest = (roomName, otherName) => ({
    roomName: roomName,
    userMute: otherName,
    //timeStamp: new Date().getTime(),
    action: "mute"
});

const getKickRequest = (roomName, otherName) => ({
    roomName: roomName,
    userKick: otherName,
    //timeStamp: new Date().getTime(),
    action: "kick"
});

// do not do this
const getBlockRequest= (roomName, otherName,blockedName) => ({
    roomName: roomName,
    userBlock: otherName,
    userBlocked: blockedName,
    //timeStamp: new Date().getTime(),
    action: "block"
});

const getLeaveRequest = (username, roomName) => ({
    username: username,
    roomName: roomName,
    //timeStamp: new Date().getTime(),
    action: "leave"
});

const getLeaveAllRequest = (username) => ({
    username: username,
    //timeStamp: new Date().getTime(),
    action: "leaveAll"
});

const getUpdateMsg = (roomName) => ({
    roomName: roomName,
    //timeStamp: new Date().getTime(),
    action: "updateMessage"
});

const getLogoutRequest = (username) => ({
    username: username,
    //timeStamp: new Date().getTime(),
    action: "logout"
});

// do not use
const getRecallUpdateRequest = (msgId, roomName, newContext) => ({
    type: newContext ? "editMsg" : "recallMsg",
    timeStamp: new Date().getTime(),
    newContext,
    roomName,
    msgId
});

// do not use
const getDeleteRequest = (msgId, roomName) => ({
    type: "deleteMsg",
    roomName,
    msgId,
    timeStamp: new Date().getTime(),
});

const getNotificationRequest = (username) => ({
    username: username,
    action: "notification"
});

const getHistoryRequest = (roomName) => ({
    roomName: roomName,
    action: "updateMessage"
});

const requests = {
    getSendMsgRequest,
    getLoginRequest,
    getInviteRequest,
    getMuteRequest,
    getKickRequest,
    getBlockRequest,
    getLeaveRequest,
    getUpdateMsg,
    getLogoutRequest,
    getDeleteRequest,
    getRecallUpdateRequest,
    getInviteUsersRequest,
    getBlockUsersRequest,
    getNotificationRequest,
    getHistoryRequest,
    acceptInviteRequest,
    rejectInviteRequest,
    getLeaveAllRequest
}

export {requests};