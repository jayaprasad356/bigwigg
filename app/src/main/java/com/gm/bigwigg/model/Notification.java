package com.gm.bigwigg.model;

public class Notification {
    String id,user_id,notify_user_id,notify_post_id,title,type,date_created;
    public Notification(){

    }

    public Notification(String id, String user_id, String notify_user_id, String notify_post_id, String title, String type, String date_created) {
        this.id = id;
        this.user_id = user_id;
        this.notify_user_id = notify_user_id;
        this.notify_post_id = notify_post_id;
        this.title = title;
        this.type = type;
        this.date_created = date_created;
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

    public String getNotify_user_id() {
        return notify_user_id;
    }

    public void setNotify_user_id(String notify_user_id) {
        this.notify_user_id = notify_user_id;
    }

    public String getNotify_post_id() {
        return notify_post_id;
    }

    public void setNotify_post_id(String notify_post_id) {
        this.notify_post_id = notify_post_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
