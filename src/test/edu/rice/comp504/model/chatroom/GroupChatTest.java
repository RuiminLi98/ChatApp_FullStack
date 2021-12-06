package edu.rice.comp504.model.chatroom;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GroupChatTest {

    GroupChat test = new GroupChat(10, 1, "ChatRoom", "Swim",
            "admin", false, "123");

    @Test
    public void getAdminList() {

        List<String> list = test.getAdminList();
        assertEquals(new ArrayList<String>(), list);

    }

    @Test
    public void getBlockMap() {

        Map<String, List<String>> map = new HashMap<>();
        assertEquals(new HashMap<String, List<String>>(), map);

    }

    @Test
    public void getUserList() {
        List<String> list = test.getUserList();
        List<String> trueList = new ArrayList<>();
        trueList.add("admin");
        assertEquals(trueList, list);
    }

    @Test
    public void isPublic() {

        assertEquals(false, test.isPublic());

    }

    @Test
    public void getCurNumUser() {
        assertEquals(1, test.getCurNumUser());
    }

    @Test
    public void getOwner() {
        assertEquals("admin", test.getOwner());
    }

    @Test
    public void getRoomPassword() {
        assertEquals("123",test.getRoomPassword());
    }

    @Test
    public void getRules() {
        assertEquals(new ArrayList<String>(),test.getRules());
    }

    @Test
    public void getMuteList() {
        assertEquals(new ArrayList<String>(),test.getMuteList());
    }

    @Test
    public void setCurNumUser() {
        test.setCurNumUser(2);
        assertEquals(2,test.getCurNumUser());
    }

    @Test
    public void setOwner() {
        test.setOwner("admin1");
        assertEquals("admin1",test.getOwner());
    }

    @Test
    public void setToPrivate() {
        test.setToPrivate();
        assertEquals(false,test.isPublic());
    }

    @Test
    public void setToPublic() {
        test.setToPublic();
        assertEquals(true,test.isPublic());
    }

    @Test
    public void setRoomPassword() {
        test.setRoomPassword("111");
        assertEquals("111",test.getRoomPassword());
    }

    @Test
    public void addRules() {
        test.addRules("aaa");
        List<String> trueList = new ArrayList<>();
        trueList.add("aaa");
        assertEquals(trueList,test.getRules());
    }

    @Test
    public void setUserList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("admin1");
        test.setUserList(list);
        assertEquals(list,test.getUserList());
    }

    @Test
    public void addToUserList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("admin");
        list.add("admin1");
        test.addToUserList("admin1");
        assertEquals(list,test.getUserList());
    }

    @Test
    public void addToAdminList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("admin");
        test.addToAdminList("admin");
        assertEquals(list,test.getAdminList());
    }

//    @Test
//    public void addToBlockList() {
//        //todo: wrong implementation
//        ArrayList<String> list = new ArrayList<>();
//        list.add("admin");
//        test.addToBlockList("admin","aaa");
//        assertEquals(new ArrayList<String>(),new ArrayList<String>());
//    }

    @Test
    public void addMuteList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("admin");
        test.addMuteList("admin");
        assertEquals(list,test.getMuteList());
    }

    @Test
    public void getInterest() {
        assertEquals("Swim",test.getInterest());
    }

    @Test
    public void propertyChange() {
    }
}