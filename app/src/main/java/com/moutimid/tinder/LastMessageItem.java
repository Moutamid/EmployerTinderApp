package com.moutimid.tinder;

// LastMessageItem.java
public class LastMessageItem {

    private String lastMessage;
    private long timestamp; // Assuming timestamp is a long value

    public LastMessageItem() {
        // Default constructor required for Firebase
    }

    public LastMessageItem(String lastMessage, long timestamp) {
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
