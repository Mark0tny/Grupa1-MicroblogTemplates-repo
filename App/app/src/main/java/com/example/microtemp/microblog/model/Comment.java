package com.example.microtemp.microblog.model;

import java.util.Date;

public class Comment {
    private int id;
    private int idPost;
    private String content;
    private Date timeCreated;
    private int author;

    public Comment(int id, int idPost, String content, Date timeCrreated, int author) {
        this.id = id;
        this.idPost = idPost;
        this.content = content;
        this.timeCreated = timeCrreated;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCrrated(Date timeCrreated) {
        this.timeCreated = timeCrreated;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }
}
