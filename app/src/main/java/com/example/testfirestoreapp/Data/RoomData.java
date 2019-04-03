package com.example.testfirestoreapp.Data;

import java.util.ArrayList;

public class RoomData {
    private String RoomName;
    private String RoomText;
    private ArrayList<String> EmailList;
    private ArrayList<ChatData> messagelist;
    public RoomData() {
    }

    public RoomData(ArrayList<String> emaillist, String roomName, String roomtext,ArrayList<ChatData>mlist) {
        this.RoomName = roomName;
        this.RoomText = roomtext;
        this.EmailList = emaillist;
        this.messagelist =mlist;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        this.RoomName = roomName;
    }

    public String getRoomText() {
        return RoomText;
    }

    public void setRoomText(String roomtext) {
        this.RoomText = roomtext;
    }

    public ArrayList<String> getEmailList() {
        return EmailList;
    }

    public void setEmailList(ArrayList<String> emaillist) {
        this.EmailList = emaillist;
    }
    public void addEmailList(String email){this.EmailList.add(email);}

    public void setMessagelist(ArrayList<ChatData> mlist) {
        this.messagelist = mlist;
    }
    public ArrayList<ChatData> getmessagelist(){
        return this.messagelist;
    }
    public void addmessagelist(ChatData mesage){this.messagelist.add(mesage);}
}