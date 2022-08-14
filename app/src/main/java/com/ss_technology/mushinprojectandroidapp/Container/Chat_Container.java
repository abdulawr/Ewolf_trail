package com.ss_technology.mushinprojectandroidapp.Container;

import java.io.Serializable;

public class Chat_Container implements Serializable {
    private String chat_id;
    private String sender_id;
    private String receiver_id;
    private String name;
    private String mobile;
    private String image;
    private String sender_msg;
    private String receiver_msg;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
