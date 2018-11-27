package com.example.microtemp.microblog.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@EqualsAndHashCode
public class Post {

    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("id_microblog")
    @Expose
    private Integer idMicroblog;
    @SerializedName("id_post")
    @Expose
    private Integer idPost;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("time_created")
    @Expose
    private String timeCreated;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("views")
    @Expose
    private Integer views;

}
