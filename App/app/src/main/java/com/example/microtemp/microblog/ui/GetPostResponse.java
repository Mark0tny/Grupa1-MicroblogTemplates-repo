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

    @SerializedName("author")
    @Expose
    private Integer author;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("id_post")
    @Expose
    private Integer idPost;
    @SerializedName("title")
    @Expose
    private String title;
}
