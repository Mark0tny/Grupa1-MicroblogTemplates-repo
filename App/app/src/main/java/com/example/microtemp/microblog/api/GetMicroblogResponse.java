package com.example.microtemp.microblog.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class GetMicroblogResponse {

    @SerializedName("id_microblog")
    @Expose
    private Integer idMicroblog;
    @SerializedName("id_user")
    @Expose
    private Integer idUser;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("time_created")
    @Expose
    private String timeCreated;
    @SerializedName("username")
    @Expose
    private String username;
}
