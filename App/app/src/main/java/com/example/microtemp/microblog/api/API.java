package com.example.microtemp.microblog.api;

import com.example.microtemp.microblog.ui.ListItemMicroblog;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface API {

    @POST("/createuser")
    Call<JsonObject> createUser(
            @Body JsonObject jsonUser
    );

    @POST("/login")
    Call<JsonObject> login(
            @Body JsonObject jsonUser
    );

    @POST("/createmicroblog")
    Call<JsonObject> createmicroblog(
            @Body JsonObject jsonMicrolog
    );

    @POST("/getmymicroblogs")
    Call<JsonObject> getMicroblogs(
            @Body JsonObject jsonMicrolog
    );

}
