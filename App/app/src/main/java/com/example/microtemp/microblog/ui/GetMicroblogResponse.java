package com.example.microtemp.microblog.ui;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class GetMicroblogResponse {

    @SerializedName("author")
    @Expose
    private Integer author;
    @SerializedName("id_microblog")
    @Expose
    private Integer idMicroblog;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("private")
    @Expose
    private String privacy;
    @SerializedName("tags")
    @Expose
    private String tags;
}
