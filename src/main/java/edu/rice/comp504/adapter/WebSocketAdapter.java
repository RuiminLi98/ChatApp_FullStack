package edu.rice.comp504.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import edu.rice.comp504.model.MessageDB;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.RoomDB;
import edu.rice.comp504.model.UserDB;
import edu.rice.comp504.model.chatroom.ChatRoom;
import edu.rice.comp504.model.chatroom.GroupChat;
import edu.rice.comp504.model.chatroom.UserChat;
import edu.rice.comp504.model.message.CompositeMessage;
import edu.rice.comp504.model.message.Message;
import edu.rice.comp504.model.message.NullMessage;
import edu.rice.comp504.model.message.TextMessage;
import edu.rice.comp504.model.notification.InviteNotification;
import edu.rice.comp504.model.notification.Notification;
import edu.rice.comp504.model.notification.NotificationFac;
import edu.rice.comp504.model.notification.SimpleNotification;
import edu.rice.comp504.model.user.NullUser;
import edu.rice.comp504.model.user.RegisteredUser;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import com.google.gson.JsonObject;

import java.util.*;

/**
 * Create a web socket for the server.
 */
@WebSocket
public class WebSocketAdapter {

    /**
     * Open user's session.
     *
     * @param session The user whose session is opened.
     */
    @OnWebSocketConnect
    public void onConnect(Session session) {
        //String username = "User" + UserDB.genNextUserId();
        //UserDB.addSessionUser(session, username);
    }

    /**
     * Close the user's session.
     *
     * @param session The use whose session is closed.
     */
    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {

        UserDB.removeUser(session);
    }

    /**
     * Send a message.
     *
     * @param session The session user sending the message.
     * @param message The message to be sent.
     */
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println(message);
        JsonParser parser = new JsonParser();
        JsonObject jo = (JsonObject) parser.parse(message);
        //System.out.println(jo.get("username").getAsString());
        //MsgToClientSender.broadcastMessage(UserDB.getUser(session), message);

        switch (jo.get("action").getAsString()) {
            case "login":
                mapSessionUser(session, jo.get("username").getAsString());
                break;

            case "send":
                String sender = jo.get("username").getAsString();
                String room = jo.get("roomName").getAsString();
                String body = jo.get("message").getAsString();

                System.out.println(sender + " " + room + " " + body);

                // check hate speech
                String text = new String(body);
                if (text.equalsIgnoreCase("fuck")) {
                    Map<String, Integer> hateSpeechCount = UserDB.getHateSpeechCount();
                    hateSpeechCount.put(sender, hateSpeechCount.getOrDefault(sender, 0) + 1);
                    if (hateSpeechCount.get(sender) == 1) {
                        //send WarningNotification
                        Notification notification = new NotificationFac().make("warn", "", sender, room);
                        User hateSpeaker = UserDB.getUsers().get(sender);
                        hateSpeaker.addNotification(notification);
                    } else if (hateSpeechCount.get(sender) == 2) {
                        //mute by all rooms
                        for (ChatRoom chatRoom : RoomDB.make().getRooms().values()) {
                            if (chatRoom.getType().equals("groupchat")) {
                                ((GroupChat) chatRoom).addToMuteList(sender);
                                Notification notification = new NotificationFac().make("mute", "", sender, room);
                                User hateSpeaker = UserDB.getUsers().get(sender);
                                hateSpeaker.addNotification(notification);
                            }
                        }
                    }
                }

                Message messageObj = createMessage(sender, room, body);
                // check if muted or the input parameters are incorrect
                if (messageObj instanceof NullMessage) {
                    break;
                } else { // broadcast to every one in the room
                    MsgToClientSender.broadcastMessage(UserDB.getUser(session), room, messageObj);
                }
                break;

            case "updateMessage":
                // TODO: update message function here, broadcast message list, History is also here
                String updateRoom = jo.get("roomName").getAsString();
                List<Message> messageList = MessageDB.make().getMessageMap().get(updateRoom);
                MsgToClientSender.updateMessages(session, updateRoom, messageList);
                break;

            case "notification":
                // TODO: update the notifications of a given user
                String username = jo.get("username").getAsString();
                User user = UserDB.getUsers().get(username);
                List<Notification> notificationList = user.getNotificationsList();
                MsgToClientSender.sendNotificationList(username, notificationList);
                break;

            case "invite":
                String inviteTarget = jo.get("userGetInvite").getAsString();
                String inviteSource = jo.get("userSendInvite").getAsString();
                // TODO: invite function here, need notification
                String roomName = jo.get("roomName").getAsString();
                RegisteredUser temp = ((RegisteredUser) UserDB.getUsers().get(inviteTarget));
                NotificationFac fac = new NotificationFac();
                InviteNotification invite = (InviteNotification) fac.make("invite", inviteSource, inviteTarget,
                        roomName);
                temp.addNotification(invite);
                MsgToClientSender.sendInviteNotification(roomName, invite, inviteTarget);
                System.out.println("apater invite");
                break;

            case "mute":
                String userMuted = jo.get("userMute").getAsString();
                String roomName2 = jo.get("roomName").getAsString();
                ChatRoom chatRoom = RoomDB.getOnly().getRooms().get(roomName2);
                ((GroupChat) chatRoom).addToMuteList(userMuted);
                //broadcast
                MsgToClientSender.broadcastMuteMessage(userMuted, roomName2);
                //send a notification to the person who got muted
                User mutedUser = UserDB.getUsers().get(userMuted);
                mutedUser.addNotification(new NotificationFac().make("mute", "", userMuted, roomName2));
                break;

            case "kick":
                String userKicked = jo.get("userKick").getAsString();
                String kickRoomName = jo.get("roomName").getAsString();
                ChatRoom kickChatRoom = RoomDB.getOnly().getRooms().get(kickRoomName);
                User kickedUser = UserDB.getUsers().get(userKicked);

                for (String kickRoomUserN : ((GroupChat) kickChatRoom).getUserList()) {
                    User kickRoomUser = UserDB.getUsers().get(kickRoomUserN);
                    kickRoomUser.addNotification(new NotificationFac().make("kick", "", userKicked, kickRoomName));
                }
                // kick the user from the room, decrease the number of members, remove the room from user's room list
                MsgToClientSender.broadcastKickMessage(userKicked, kickRoomName);
                ((GroupChat) kickChatRoom).kickUser(userKicked);
                //broadcast
                //send a notification to the person who got kicked
                break;

            case "block":
                String userBlock = jo.get("userBlock").getAsString();
                String userBlocked = jo.get("userBlocked").getAsString();
                roomName = jo.get("roomName").getAsString();
                GroupChat blockRoom = (GroupChat) RoomDB.make().getRooms().get(roomName);
                blockRoom.addToBlockList(userBlock, userBlocked);
                MsgToClientSender.setBlockResult(roomName, true, userBlock);

                break;

            case "getBlockUsers":
                String userCurrent = jo.get("userSendInvite").getAsString();
                String roomCurrent = jo.get("roomName").getAsString();
                user = UserDB.getUsers().get(userCurrent);
                GroupChat roomNow = ((GroupChat) RoomDB.make().getRooms().get(roomCurrent));
                List<String> userList2 = roomNow.getUserList();
                userList2.remove(userCurrent);
                List<String> userBlockedByCurr = new ArrayList<>();
                if (roomNow.getBlockMap().containsKey(userCurrent)) {
                    userBlockedByCurr = roomNow.getBlockMap().get(userCurrent);
                }
                List<String> difference = new ArrayList<>();
                boolean flag = false;
                for (String x : userList2) {
                    for (String j : userBlockedByCurr) {
                        if (x.equals(j)) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        difference.add(x);
                    }
                    flag = false;
                }
                MsgToClientSender.sendBlockList(roomCurrent, difference, userCurrent);
                break;

            case "getInviteUsers":
                String roomNameCurr = jo.get("roomName").getAsString();
                String userNameCurr = jo.get("userSendInvite").getAsString();
                GroupChat roomCurr2 = ((GroupChat) RoomDB.make().getRooms().get(roomNameCurr));
                List<User> res = new ArrayList<>();
                List<User> userAll = new ArrayList<>();
                for (Map.Entry<String, User> x : (UserDB.getUsers()).entrySet()) {
                    userAll.add(x.getValue());
                }
                List<String> userListName = roomCurr2.getUserList();
                Set<String> roomUserSet = new HashSet<>();
                for (String x : userListName) {
                    roomUserSet.add(x);
                }
                for (int i = 0; i < userAll.size(); i++) {
                    if (roomUserSet.contains(userAll.get(i).getUsername())) {
                        continue;
                    }
                    res.add(userAll.get(i));
                }
                MsgToClientSender.sendInviteList(roomNameCurr, res, userNameCurr);
                break;

            case "acceptInvite":
                String senderT = jo.get("userGetInvite").getAsString();  //
                String receiverT = jo.get("userSendInvite").getAsString();  //
                String roomName3 = jo.get("roomName").getAsString();   //
                RegisteredUser temp3 = ((RegisteredUser) UserDB.getUsers().get(receiverT));
                GroupChat acceptJoinedRoom = (GroupChat) RoomDB.make().getRooms().get(roomName3);
                acceptJoinedRoom.addToUserList(senderT);
                //temp3.getRoomList().add(acceptJoinedRoom);
                RegisteredUser temp4 = ((RegisteredUser) UserDB.getUsers().get(senderT));
                temp4.getRoomList().add(acceptJoinedRoom);
                ArrayList<Notification> noti = temp4.getNotificationsList();
                for (Notification x : noti) {
                    if (x.getType().equals("invite") && x.getSender().equals(receiverT)) {
                        noti.remove(x);
                        break;
                    }
                }
                int num = acceptJoinedRoom.getCurNumUser();
                acceptJoinedRoom.setCurNumUser(num + 1);
                NotificationFac fac3 = new NotificationFac();
                Notification n = fac3.make("inviteAccept", senderT, receiverT, senderT + "accept to join" + roomName3);
                temp3.addNotification(n);
                MsgToClientSender.sendAcceptInviteNotification("", n, receiverT);
                break;

            case "rejectInvite":
                String senderT2 = jo.get("userGetInvite").getAsString();   //
                String receiverT2 = jo.get("userSendInvite").getAsString();   //
                String roomName4 = jo.get("roomName").getAsString();  //
                RegisteredUser temp2 = ((RegisteredUser) UserDB.getUsers().get(receiverT2));
                NotificationFac fac2 = new NotificationFac();
                Notification n1 = fac2.make("inviteReject", receiverT2, senderT2, senderT2 + "reject you to join" + roomName4);
                temp2.getNotificationsList().add(n1);
                MsgToClientSender.sendRejectInviteNotification("", n1, receiverT2);
                break;

            case "leave":
                String roomLeft = jo.get("roomName").getAsString();
                String userLeft = jo.get("username").getAsString();
                // TODO: leave function here, need notification or message?
                RegisteredUser userLeftCurr = ((RegisteredUser) UserDB.getUsers().get(userLeft));
                ChatRoom roomLeftCurr = RoomDB.make().getRooms().get(roomLeft);
                if (roomLeftCurr.getType().equals("groupchat")) {
                    //groupchat+admin+size != 1 ===cannot leave
                    if (((GroupChat) roomLeftCurr).getOwner().equals(userLeft) && ((GroupChat) roomLeftCurr).getCurNumUser() != 1) {
                        MsgToClientSender.setLeaveResult(roomLeft, false, userLeft);
                    } else {
                        //leave meesage before left to all users in the group
                        Message leaveMessageObj = createMessage(((GroupChat) roomLeftCurr).getOwner(), roomLeft, userLeft + " leave the room by himself!");
                        MsgToClientSender.broadcastMessage(roomLeftCurr.getRoomName(), roomLeft, leaveMessageObj);
                        userLeftCurr.removeAChatRoom(roomLeftCurr);
                        if (((GroupChat) roomLeftCurr).getOwner().equals(userLeft)) {
                            RoomDB.make().getRooms().remove(roomLeft);
                        } else {
                            int currUserNum = ((GroupChat) roomLeftCurr).getCurNumUser() - 1;
                            ((GroupChat) roomLeftCurr).setCurNumUser(currUserNum);
                            ((GroupChat) roomLeftCurr).getUserList().remove(userLeft);
                            ((GroupChat) roomLeftCurr).getMuteList().remove(userLeft);
                            Map<String, List<String>> map = ((GroupChat) roomLeftCurr).getBlockMap();
                            if (map.containsKey(userLeft)) {
                                map.remove(userLeft);
                            }
                            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                                List<String> list = entry.getValue();
                                list.remove(userLeft);
                            }
                        }
                        MsgToClientSender.setLeaveResult(roomLeft, true, userLeft);
                    }
                } else if (roomLeftCurr.getType().equals("userchat")) {
                    User user1 = UserDB.getUsers().get(((UserChat) roomLeftCurr).getUser1());
                    User user2 = UserDB.getUsers().get(((UserChat) roomLeftCurr).getUser2());

                    user1.getRoomList().remove(roomLeftCurr);
                    user2.getRoomList().remove(roomLeftCurr);

                    RoomDB.make().getRooms().remove(roomLeft);
                    NotificationFac fac5 = new NotificationFac();

                    SimpleNotification simple = (SimpleNotification) fac5.make("simple", userLeft, user1.getUsername(),
                            userLeft + " leave room " + roomLeft);
                    SimpleNotification simple2 = (SimpleNotification) fac5.make("simple", userLeft, user2.getUsername(),
                            userLeft + " leave room " + roomLeft);

                    user1.addNotification(simple);
                    user2.addNotification(simple2);

                    MsgToClientSender.setLeaveResult(roomLeft, true, userLeft);

                    MsgToClientSender.sendSimpleNotification(simple, user1.getUsername());
                    MsgToClientSender.sendSimpleNotification(simple2, user2.getUsername());
                }
                break;

            case "leaveAll":
                String userLeft2 = jo.get("username").getAsString();
                ArrayList<ChatRoom> leaveAllRooms = UserDB.getUsers().get(userLeft2).getRoomList();
                ArrayList<ChatRoom> leaveAllRoomsTemp = new ArrayList<>();
                RegisteredUser userLeftCurr2 = ((RegisteredUser) UserDB.getUsers().get(userLeft2));
                for (ChatRoom x : leaveAllRooms) {
                    leaveAllRoomsTemp.add(x);
                }
                for (ChatRoom x : leaveAllRoomsTemp) {
                    if (x.getType().equals("groupchat")) {
                        //groupchat+admin+size != 1 ===cannot leave
                        if (((GroupChat) x).getOwner().equals(userLeft2) && ((GroupChat) x).getCurNumUser() != 1) {
                            ;
                        } else {
                            //leave meesage before left to all users in the group
                            MsgToClientSender.broadcastMessage(x.getRoomName(), x.getRoomName(), TextMessage.make("",
                                    ((GroupChat) x).getOwner(), userLeft2 + " leave the room by himself", "", "", 12));
                            userLeftCurr2.removeAChatRoom(x);
                            if (((GroupChat) x).getOwner().equals(userLeft2)) {
                                RoomDB.make().getRooms().remove(x.getRoomName());
                            } else {
                                int currUserNum = ((GroupChat) x).getCurNumUser() - 1;
                                ((GroupChat) x).setCurNumUser(currUserNum);
                                ((GroupChat) x).getUserList().remove(userLeft2);
                                ((GroupChat) x).getMuteList().remove(userLeft2);
                                Map<String, List<String>> map = ((GroupChat) x).getBlockMap();
                                if (map.containsKey(userLeft2)) {
                                    map.remove(userLeft2);
                                }
                                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                                    List<String> list = entry.getValue();
                                    list.remove(userLeft2);
                                }
                            }
                        }
                    } else if (x.getType().equals("userchat")) {
                        User user1 = UserDB.getUsers().get(((UserChat) x).getUser1());
                        User user2 = UserDB.getUsers().get(((UserChat) x).getUser2());

                        user1.getRoomList().remove(x);
                        user2.getRoomList().remove(x);

                        RoomDB.make().getRooms().remove(x.getRoomName());
                        NotificationFac fac6 = new NotificationFac();

                        SimpleNotification simple = (SimpleNotification) fac6.make("simple", userLeft2, user1.getUsername(),
                                userLeft2 + " leave room " + x.getRoomName());
                        SimpleNotification simple2 = (SimpleNotification) fac6.make("simple", userLeft2, user2.getUsername(),
                                userLeft2 + " leave room " + x.getRoomName());

                        user1.addNotification(simple);
                        user2.addNotification(simple2);
                        MsgToClientSender.sendSimpleNotification(simple, user1.getUsername());
                        MsgToClientSender.sendSimpleNotification(simple2, user2.getUsername());
                    }
                }
                MsgToClientSender.setLeaveAllResult(true, userLeft2);
                break;

            case "edit":
                //need to check if the user is the sender of the message
                String editUser = jo.get("username").getAsString();
                String editRoomName = jo.get("roomName").getAsString();
                String editTimestamp = jo.get("timestamp").getAsString();
                String newText = jo.get("newText").getAsString();
                List<Message> messages = MessageDB.make().getMessageMap().get(editRoomName);
                Iterator<Message> iterator = messages.iterator();
                int idx = 0;
                while (iterator.hasNext()) {
                    Message m = iterator.next();
                    if (m.getTimestamp().equals(editTimestamp) && m.getSendUser().equals(editUser)) {
                        CompositeMessage newMessage = new CompositeMessage("auto", editUser);
                        newMessage.addMultipleChildFromString(newText);
                        messages.set(idx, newMessage);
                    }
                    idx++;
                }
                break;

            case "recall":
                //need to check if the user is the sender of the message
                String recallUser = jo.get("username").getAsString();
                String recallRoomName = jo.get("roomName").getAsString();
                String recallTimestamp = jo.get("timestamp").getAsString();
                idx = 0;
                List<Message> recallMessages = MessageDB.make().getMessageMap().get(recallRoomName);
                iterator = recallMessages.iterator();
                while (iterator.hasNext()) {
                    Message m = iterator.next();
                    if (m.getTimestamp().equals(recallTimestamp) && m.getSendUser().equals(recallUser)) {
                        iterator.remove();
                    }
                    idx++;
                }
                break;

            case "delete":
                //need to check if the user is admin
                String deleteUser = jo.get("username").getAsString();
                String deleteRoomName = jo.get("roomName").getAsString();
                String deleteTimestamp = jo.get("timestamp").getAsString();
                //check if the user is admin
                List<String> admins = ((GroupChat) RoomDB.getOnly().getRooms().get(deleteRoomName)).getAdminList();
                if (!admins.contains(deleteUser)) {
                    break;
                }
                //
                idx = 0;
                List<Message> deleteMessages = MessageDB.make().getMessageMap().get(deleteRoomName);
                iterator = deleteMessages.iterator();
                while (iterator.hasNext()) {
                    Message m = iterator.next();
                    if (m.getTimestamp().equals(deleteTimestamp) && m.getSendUser().equals(deleteUser)) {
                        iterator.remove();
                    }
                    idx++;
                }
                break;


            default:
                System.out.println("Not valid action!");
                break;
        }

    }

    /**
     * Map websocket session with current username when logging in.
     *
     * @param session  websocket session
     * @param username username
     */
    public void mapSessionUser(Session session, String username) {
        UserDB.addSessionUser(session, username);
    }


    public void registerUser(String username, String school, int age, String interestsTemp, String password) {
        UserDB.addUser(username, school, age, interestsTemp, password);
    }

    /**
     * Login.
     *
     * @param username user
     * @param password password
     * @return user
     */
    public User logInUser(String username, String password) {
        Map<String, User> usersTemp = UserDB.getUsers();
        if (usersTemp.containsKey(username)) {
            if (usersTemp.get(username).getPassword().equals(password)) {
                return usersTemp.get(username);
            }
        }
        return new NullUser("", "", "", 0, "");
    }


    /**
     * Get room list of a user.
     */
    public List<ChatRoom> getUserRoomList(String userName) {
        return (UserDB.getUsers().get(userName)).getRoomList();
    }

    /**
     * Get users in same rooms.
     */
    public List<User> getKnownUser(String userName) {

        List<User> users = new ArrayList<>();
        Set<String> set = new HashSet<>();
        RegisteredUser user = (RegisteredUser) UserDB.getUsers().get(userName);

        for (ChatRoom room : user.getRoomList()) {
            if (room.getType().equals("groupchat")) {
                List<String> list = ((GroupChat) room).getUserList();
                for (String tmpUser : list) {
                    if (RoomDB.make().getRooms().containsKey(tmpUser + "," + userName)
                            || RoomDB.make().getRooms().containsKey(userName + "," + tmpUser)) {
                        continue;
                    }
                    set.add(tmpUser);
                }
            }
        }

        set.remove(userName);

        for (String str : set) {
            users.add(UserDB.getUsers().get(str));
        }

        return users;
    }

    /**
     * Create a room, add this room to it's owner's roomList.
     */
    public boolean createGroupChat(int userLimit, String roomName, String interest,
                                   String ownerUsername, boolean isPublic, String roomPassword) {
        if (RoomDB.make().addGroupRoom(userLimit, roomName, interest, ownerUsername, isPublic, roomPassword)) {
            Map<String, ChatRoom> rooms = RoomDB.make().getRooms();
            GroupChat room = (GroupChat) rooms.get(roomName);
            User user = UserDB.getUsers().get(ownerUsername);
            user.addAChatRoom(room);
            return true;
        }
        return false;
    }

    /**
     * Create a user chat, add this room to it's owner's roomList.
     */
    public boolean createUserChat(String userName, String friendName) {

        if (RoomDB.make().addUserChat(userName, friendName)) {
            Map<String, ChatRoom> rooms = RoomDB.make().getRooms();
            UserChat room = (UserChat) rooms.get(userName + "," + friendName);
            User user = UserDB.getUsers().get(userName);
            User friend = UserDB.getUsers().get(friendName);
            user.addAChatRoom(room);
            friend.addAChatRoom(room);
            return true;
        }
        return false;
    }

    /**
     * Return the list of user/admin/owner of the chosen room.
     */
    public List<String> showAllUsersInside(String roomname) {
        List<String> res = new ArrayList<>();
        if (RoomDB.make().getRooms().get(roomname).getType().equals("userchat")) {
            res.add(((UserChat) RoomDB.make().getRooms().get(roomname)).getUser1());
            res.add(((UserChat) RoomDB.make().getRooms().get(roomname)).getUser2());
            return res;
        }
        Set<String> userSet = new HashSet<>();
        List<String> users = ((GroupChat) RoomDB.make().getRooms().get(roomname)).getUserList();
        String owner = ((GroupChat) RoomDB.make().getRooms().get(roomname)).getOwner();
        List<String> admin = ((GroupChat) RoomDB.make().getRooms().get(roomname)).getAdminList();
        for (String x : users) {
            userSet.add(x);
        }
//        List <String> res = new ArrayList<>();
        res.add("Owner: " + owner + "(Admin)");
        userSet.remove(owner);
        for (String x : admin) {
            res.add("Admin: " + x);
            userSet.remove(x);
        }
        for (String x : userSet) {
            res.add(x);
        }
        return res;
    }

    /**
     * Adapter's create message function.
     *
     * @param sender sender's username
     * @param room   room's name
     * @param body   message body text
     * @return a message
     */
    public Message createMessage(String sender, String room, String body) {
        //MUTE : check if the sender is muted in the given room
        //BTW, BLOCK will be checked in /updateMessage
        ChatRoom chatRoom = RoomDB.make().getRooms().get(room);
        if (chatRoom.getType().equals("groupchat")) { //check mute
            List<String> mutedUsers = ((GroupChat) chatRoom).getMuteList();
            if (mutedUsers.contains(sender)) {
                //send mute notification
                Notification notification = new NotificationFac().make("mute", "", sender, room);
                User user = UserDB.getUsers().get(sender);
                user.addNotification(notification);
                return NullMessage.make();
                // return MessageDB.make().addMessage(sender, room, body, "null");

            }
        }
        return MessageDB.make().addMessage(sender, room, body, "composite");
    }


}
