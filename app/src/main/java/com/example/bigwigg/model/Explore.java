package com.example.bigwigg.model;

public class Explore {
    String id,profile;
    public Explore(){

    }
    public Explore(String id, String profile) {
        this.id = id;
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
