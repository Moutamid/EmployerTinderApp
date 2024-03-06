package com.moutimid.tinder;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private String senderId;
    private String receiverId;
    private String message;
    private boolean starred;

    public Message() {
        // Default constructor required for Firebase
    }

    public Message(String senderId, String receiverId, String message, boolean starred) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.starred = starred;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("senderId", senderId);
        result.put("receiverId", receiverId);
        result.put("message", message);
        result.put("starred", starred);
        return result;
    }
}
