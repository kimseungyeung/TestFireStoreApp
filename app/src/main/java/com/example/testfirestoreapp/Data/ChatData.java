package com.example.testfirestoreapp.Data;

public class ChatData {
    private String userName;
    private String message;
    private String email;
    private String date;
    private String Imagename;
    public ChatData() { }

    public ChatData(String userName, String message,String mail,String da,String image) {
        this.userName = userName;
        this.message = message;
        this.email =mail;
        this.date=da;
        this.Imagename=image;

    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setImagename(String imagename) {
        Imagename = imagename;
    }

    public String getImagename() {
        return Imagename;
    }
}