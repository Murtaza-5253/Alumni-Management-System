package com.igc.studentalumni.Messages;

// Message.java
public class Message {

    private int UserId; // message body
    private String Message; // data of the user that sent this message
    private String sentAt;
    private String Name;
    //@Exclude
    //private boolean belongsToCurrentUser;

    public Message(int UserId,String Message,String sentAt,String Name)
    {
        this.UserId = UserId;
        this.Message = Message;
        this.sentAt  =sentAt;
        this.Name = Name;
    }

    public int getUsersId() {
        return UserId;
    }

    public String getMessage() {
        return Message;
    }

    public String getSentAt() {
        return sentAt;
    }

    public String getName() {
        return Name;
    }
}

