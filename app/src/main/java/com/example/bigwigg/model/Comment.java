package com.example.bigwigg.model;

public class Comment {
    String id,name,message,profile;
    public Comment(){

    }

    public Comment(String id, String name, String message, String profile) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
