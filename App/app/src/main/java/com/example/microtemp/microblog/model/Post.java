package com.example.microtemp.microblog.model;

import java.util.Date;
import java.util.List;

public class Post {
    private int id;
    private int author;
    private String title;
    private Date timeCreated;
    private String content;
    private Byte[] picture;
    private List<Comment> commentList;
    private int views;
    private List<Tag> tagList;
    private int idMicroblog;

    public Post(int id, int author, String title, Date timeCreated, String content, Byte[] picture) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.timeCreated = timeCreated;
        this.content = content;
        this.picture = picture;
    }

    public Post(int id, int author, String title, Date timeCreated) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.timeCreated = timeCreated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Byte[] getPicture() {
        return picture;
    }

    public void setPicture(Byte[] picture) {
        this.picture = picture;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public int getIdMicroblog() {
        return idMicroblog;
    }

    public void setIdMicroblog(int idMicroblog) {
        this.idMicroblog = idMicroblog;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", author=" + author +
                ", title='" + title + '\'' +
                ", timeCreated=" + timeCreated +
                ", content='" + content + '\'' +
                '}';
    }
}
