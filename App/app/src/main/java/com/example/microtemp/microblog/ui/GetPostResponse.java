package com.example.microtemp.microblog.ui;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class GetPostResponse {

    @SerializedName("count")
    @Expose
    private Integer count;
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