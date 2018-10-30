package com.example.microtemp.microblog.model;

import java.util.List;

public class Microblog {
    private int id;
    private String name;
    private int author;
    private List<Tag> tagList;
    private List<Post> postList;
    private List<User> followers;
    private boolean privacy;

    public Microblog(int id, String name, int author, List<Tag> tagList, boolean privacy) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.tagList = tagList;
        this.privacy = privacy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
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

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public boolean isPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }
}
