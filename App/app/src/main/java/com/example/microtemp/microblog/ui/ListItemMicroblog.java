package com.example.microtemp.microblog.ui;

public class ListItemMicroblog {


    private String author;
    private String title;
    private String tags;

    public ListItemMicroblog(String author, String title, String tags) {
        this.author = author;
        this.title = title;
        this.tags = tags;
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
}
