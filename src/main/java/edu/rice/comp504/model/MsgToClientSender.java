package edu.rice.comp504.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.rice.comp504.model.chatroom.ChatRoom;

import edu.rice.comp504.model.chatroom.GroupChat;
import edu.rice.comp504.model.chatroom.UserChat;
import edu.rice.comp504.model.message.Message;
import edu.rice.comp504.model.notification.InviteNotification;
import edu.rice.comp504.model.notification.Notification;
import edu.rice.comp504.model.notification.SimpleNotification;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static j2html.TagCreator.p;

/**
 * Send messages to the client.
 */
public class MsgToClientSender {

    /**
     * Broadcast message to all users.
     * @param sender  The message sender.
     * @param message The message.
     */
    public static void broadcastMessage(String sender, String room, Message message) {
        if (message.getType().equals("null")) {
            return;
        }
        ChatRoom chatRoom = RoomDB.make().getRooms().get(room);
        if (chatRoom.getType().equals("groupchat")) {
            //check if the user is in the given room
            List<String> userList = ((GroupChat)chatRoom).getUserList();
            UserDB.getSessions().forEach(session -> {
                String currUser = UserDB.getUserBySession(session);
                if (!userList.contains(currUser)) {
                    return;
                }
                //check block list
                List<String> blockList = ((GroupChat) chatRoom).getBlockMap().getOrDefault(currUser, new ArrayList<>());
                if (blockList.contains(sender)) {
                    return;
                }
                //send
                sendJsonObject(room, message, sender, session);
            });
        } else { // userchat
            UserDB.getSessions().forEach(session -> {
                String currUser = UserDB.getUserBySession(session);
                if (!currUser.equals(((UserChat)chatRoom).getUser1()) && !currUser.equals(((UserChat)chatRoom).getUser2())) {
                    return;
                }
                sendJsonObject(room, message, sender, session);
            });
        }
    }

    /**
     * Send.
     * @param roomName room
     * @param list list
     * @param userName user
     */
    public static void sendInviteList(String roomName, List<User> list, String userName) {
        JsonObject jo = new JsonObject();
        jo.addProperty("message", new Gson().toJsonTree(list).toString());
        jo.addProperty("room", roomName);
        jo.addProperty("username", userName);
        jo.addProperty("action", "getInviteUsers");
        UserDB.getSessions().forEach(session -> {
            String currUser = UserDB.getUserBySession(session);
            if (currUser.equals(userName)) {
                try {
                    session.getRemote().sendString(String.valueOf(jo));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Send block.
     * @param roomName room
     * @param list list
     * @param userName user
     */
    public static void sendBlockList(String roomName, List<String> list, String userName) {
        JsonObject jo = new JsonObject();
        jo.addProperty("message", new Gson().toJsonTree(list).toString());
        jo.addProperty("room", roomName);
        jo.addProperty("username", userName);
        jo.addProperty("action", "getBlockUsers");
        UserDB.getSessions().forEach(session -> {
            String currUser = UserDB.getUserBySession(session);
            if (currUser.equals(userName)) {
                try {
                    session.getRemote().sendString(String.valueOf(jo));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Invite.
     * @param roomName room
     * @param invite invite
     * @param userName user
     */
    public static void sendInviteNotification(String roomName, InviteNotification invite, String userName) {
        JsonObject jo = new JsonObject();
        jo.addProperty("message", new Gson().toJsonTree(invite).toString());
        jo.addProperty("room", roomName);
        jo.addProperty("username", userName);
        jo.addProperty("action", "invite");
        UserDB.getSessions().forEach(session -> {
            String currUser = UserDB.getUserBySession(session);
            if (currUser.equals(userName)) {
                try {
                    session.getRemote().sendString(String.valueOf(jo));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Accept.
     * @param roomName room
     * @param accept accept
     * @param userName user
     */
    public static void sendAcceptInviteNotification(String roomName, Notification accept, String userName) {
        JsonObject jo = new JsonObject();
        jo.addProperty("message", new Gson().toJsonTree(accept).toString());
        jo.addProperty("room", roomName);
        jo.addProperty("username", userName);
        jo.addProperty("action", "acceptInvite");
        UserDB.getSessions().forEach(session -> {
            String currUser = UserDB.getUserBySession(session);
            if (currUser.equals(userName)) {
                try {
                    session.getRemote().sendString(String.valueOf(jo));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Reject.
     * @param roomName room
     * @param accept accept
     * @param userName user
     */
    public static void sendRejectInviteNotification(String roomName, Notification accept, String userName) {
        JsonObject jo = new JsonObject();
        jo.addProperty("message", new Gson().toJsonTree(accept).toString());
        jo.addProperty("room", roomName);
        jo.addProperty("username", userName);
        jo.addProperty("action", "rejectInvite");
        UserDB.getSessions().forEach(session -> {
            String currUser = UserDB.getUserBySession(session);
            if (currUser.equals(userName)) {
                try {
                    session.getRemote().sendString(String.valueOf(jo));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Send simple.
     * @param notification notification
     * @param userName user
     */
    public static void sendSimpleNotification(SimpleNotification notification, String userName) {
        JsonObject jo = new JsonObject();
        jo.addProperty("message", new Gson().toJsonTree(notification).toString());
        jo.addProperty("username", userName);
        jo.addProperty("action", "simpleNotification");
        UserDB.getSessions().forEach(session -> {
            String currUser = UserDB.getUserBySession(session);
            if (currUser.equals(userName)) {
                try {
                    session.getRemote().sendString(String.valueOf(jo));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Leave.
     * @param roomName room
     * @param res res
     * @param userName user
     */
    public static void setLeaveResult(String roomName, boolean res, String userName) {
        JsonObject jo = new JsonObject();
        jo.addProperty("message", new Gson().toJsonTree(res).toString());
        jo.addProperty("room", roomName);
        jo.addProperty("username", userName);
        jo.addProperty("action", "leave");
        UserDB.getSessions().forEach(session -> {
            String currUser = UserDB.getUserBySession(session);
            if (currUser.equals(userName)) {
                try {
                    session.getRemote().sendString(String.valueOf(jo));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Set leave.
     * @param res res
     * @param userName username
     */
    public static void setLeaveAllResult(boolean res, String userName) {
        JsonObject jo = new JsonObject();
        jo.addProperty("message", new Gson().toJsonTree(res).toString());
        jo.addProperty("username", userName);
        jo.addProperty("action", "leaveAll");
        UserDB.getSessions().forEach(session -> {
            String currUser = UserDB.getUserBySession(session);
            if (currUser.equals(userName)) {
                try {
                    session.getRemote().sendString(String.valueOf(jo));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Sent block.
     * @param roomName room
     * @param res res
     * @param userName username
     */
    public static void setBlockResult(String roomName, boolean res, String userName) {
        JsonObject jo = new JsonObject();
        jo.addProperty("message", new Gson().toJsonTree(res).toString());
        jo.addProperty("room", roomName);
        jo.addProperty("username", userName);
        jo.addProperty("action", "block");
        UserDB.getSessions().forEach(session -> {
            String currUser = UserDB.getUserBySession(session);
            if (currUser.equals(userName)) {
                try {
                    session.getRemote().sendString(String.valueOf(jo));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    private static void sendJsonObject(String room, Message message, String sender, Session session) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("username", sender);
            jo.addProperty("message", new Gson().toJsonTree(message).toString());
            jo.addProperty("room", room);
            jo.addProperty("action", "send");
            session.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*UserDB.getSessions().forEach(session -> {
            String curUser = UserDB.getUserBySession(session);
            try {
                JsonObject jo = new JsonObject();
                // TODO: use .addProperty(key, value) add a JSON object property that has a key "userMessage"
                //  and a j2html paragraph value

                jo.addProperty("message", new Gson().toJsonTree(message).toString());
                jo.addProperty("room", p(room).render());

                session.getRemote().sendString(String.valueOf(jo));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/
    }

    /**
     * Send.
     * @param username user
     * @param notificationList list
     */
    public static void sendNotificationList(String username, List<Notification> notificationList) {
        try {
            UserDB.getSessions().forEach(session -> {
                String currUser = UserDB.getUserBySession(session);
                if (!currUser.equals(username)) {
                    return;
                }
                JsonObject jo = new JsonObject();
                jo.addProperty("username",username);
                jo.addProperty("notificationList",new Gson().toJsonTree(notificationList).toString());
                jo.addProperty("action","notification");
                try {
                    session.getRemote().sendString(String.valueOf(jo));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mute.
     * @param userMuted muted
     * @param roomName room
     */
    public static void broadcastMuteMessage(String userMuted, String roomName) {
        try {
            ChatRoom chatRoom = RoomDB.make().getRooms().get(roomName);
            List<String> userList = ((GroupChat)chatRoom).getUserList();
            UserDB.getSessions().forEach(session -> {
                String currUser = UserDB.getUserBySession(session);
                if (!userList.contains(currUser)) {
                    return;
                }
                JsonObject jo = new JsonObject();
                jo.addProperty("userMuted", userMuted);
                jo.addProperty("roomName", roomName);
                jo.addProperty("action", "mute");
                try {
                    session.getRemote().sendString(String.valueOf(jo));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update.
     * @param session session
     * @param updateRoom update
     * @param messageList list
     */
    public static void updateMessages(Session session, String updateRoom, List<Message> messageList) {
        JsonObject returnJO = new JsonObject();
        returnJO.addProperty("roomName", updateRoom);
        returnJO.addProperty("messages", new Gson().toJsonTree(messageList).toString());
        returnJO.addProperty("action", "updateMessage");
        try {
            session.getRemote().sendString(String.valueOf(returnJO));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Broadcast.
     * @param userKicked kicked
     * @param kickRoomName room
     */
    public static void broadcastKickMessage(String userKicked, String kickRoomName) {
        try {
            ChatRoom chatRoom = RoomDB.make().getRooms().get(kickRoomName);
            List<String> userList = ((GroupChat)chatRoom).getUserList();
            UserDB.getSessions().forEach(session -> {
                String currUser = UserDB.getUserBySession(session);
                if (!userList.contains(currUser)) {
                    return;
                }
                JsonObject jo = new JsonObject();
                jo.addProperty("userKicked", userKicked);
                jo.addProperty("roomName", kickRoomName);
                jo.addProperty("action", "kick");
                try {
                    session.getRemote().sendString(String.valueOf(jo));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
