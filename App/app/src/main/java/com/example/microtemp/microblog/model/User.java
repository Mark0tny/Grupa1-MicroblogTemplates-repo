package com.example.microtemp.microblog.model;

import java.util.List;

public class User {

    private int id;
    private String username;
    private  String password;
    private List<Microblog> microblogList;
    private  List<Tag> tagList;
    private List<Post> postList;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(int id, String username, String password, List<Microblog> microblogList, List<Tag> tagList, List<Post> postList) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.microblogList = microblogList;
        this.tagList = tagList;
        this.postList = postList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Microblog> getMicroblogList() {
        return microblogList;
    }

    public void setMicroblogList(List<Microblog> microblogList) {
        this.microblogList = microblogList;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", microblogList=" + microblogList +
                ", tagList=" + tagList +
                ", postList=" + postList +
                '}';
    }
}
