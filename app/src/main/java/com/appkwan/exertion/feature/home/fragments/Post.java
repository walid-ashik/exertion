package com.appkwan.exertion.feature.home.fragments;

public class Post {

    private String group;
    private String location;
    private String post;
    private String user_id;

    public Post() {
    }

    public Post(String group, String location, String post, String user_id) {
        this.group = group;
        this.location = location;
        this.post = post;
        this.user_id = user_id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
