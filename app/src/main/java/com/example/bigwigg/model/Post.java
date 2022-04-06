package com.example.bigwigg.model;

public class Post {
    String id,user_id,name,profile,caption,image;
    public Post(){

    }

    public Post(String id, String user_id, String name, String profile, String caption, String image) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.profile = profile;
        this.caption = caption;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
