package com.ss_technology.mushinprojectandroidapp.Container;

public class Chat_Message_Container {
    private String id,message,sender_msg,receiver_msg,sender_id;

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_msg() {
        return sender_msg;
    }

    public void setSender_msg(String sender_msg) {
        this.sender_msg = sender_msg;
    }

    public String getReceiver_msg() {
        return receiver_msg;
    }

    public void setReceiver_msg(String receiver_msg) {
        this.receiver_msg = receiver_msg;
    }
}
