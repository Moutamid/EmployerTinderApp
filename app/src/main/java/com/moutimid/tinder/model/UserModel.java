package com.moutimid.tinder.model;


import java.io.Serializable;

public class UserModel implements Serializable {
    public String uid;
    public String email;
    public String user_name;
    public String name;
    public String phone;
    public String modalities;
    public String time_in_fields;
    public String population;
    public String bio;
    public String resume;
    public String profile_img;
    public String pdfUrl;
    public String type;
    public String selectedText;

    public UserModel() {
    }

    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModalities() {
        return modalities;
    }

    public void setModalities(String modalities) {
        this.modalities = modalities;
    }

    public String getTime_in_fields() {
        return time_in_fields;
    }

    public void setTime_in_fields(String time_in_fields) {
        this.time_in_fields = time_in_fields;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }
}