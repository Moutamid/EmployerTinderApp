package com.moutimid.tinder.Admin.Model;
// Question.java
// Question.java
public class Question {
    private String text;
    private String key; // Key for Firebase database

    public Question() {
        // Default constructor required for Firebase
    }

    public Question(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
