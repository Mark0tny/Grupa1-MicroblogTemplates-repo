package com.example.microtemp.microblog.ui;

import android.media.Image;

public class ListItemPost {

    private String author;
    private String title;
    private String tags;

    private int views;
    private String imageUrl;

    public ListItemPost(String author, String title, String tags, int views, String imageUrl) {
        this.author = author;
        this.title = title;
        this.tags = tags;
        this.views = views;
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
