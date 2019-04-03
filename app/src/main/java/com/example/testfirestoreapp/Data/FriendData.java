package com.example.testfirestoreapp.Data;

public class FriendData {
    private String friendname;
    private String friendemail;
    public FriendData(String fname,String fmail){
        this.friendname =fname;
        this.friendemail = fmail;

    }

    public String getFriendemail() {
        return friendemail;
    }

    public String getFriendname() {
        return friendname;
    }

    public void setFriendemail(String friendemail) {
        this.friendemail = friendemail;
    }

    public void setFriendname(String friendname) {
        this.friendname = friendname;
    }
}
