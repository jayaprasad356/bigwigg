package com.example.bigwigg.model;

public class Explore {
    String id,name,email,role,description,profile,rating_count;
    public Explore(){

    }

    public Explore(String id, String name, String email, String role, String description, String profile, String rating_count) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.description = description;
        this.profile = profile;
        this.rating_count = rating_count;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getRating_count() {
        return rating_count;
    }

    public void setRating_count(String rating_count) {
        this.rating_count = rating_count;
    }
}
