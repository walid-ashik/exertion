package com.appkwan.exertion.feature.home.fragments;

public class Post {

    public String group;
    public String query_group;
    public String location;
    public String query_location;
    public String post;
    public String user_id;

    public String postId;

    public Post() {
    }

    public String getQuery_group() {
        return query_group;
    }

    public void setQuery_group(String query_group) {
        this.query_group = query_group;
    }

    public String getQuery_location() {
        return query_location;
    }

    public void setQuery_location(String query_location) {
        this.query_location = query_location;
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
