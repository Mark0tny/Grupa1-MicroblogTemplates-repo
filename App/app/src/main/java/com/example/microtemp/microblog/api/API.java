package com.example.microtemp.microblog.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {

    @FormUrlEncoded
    @POST("/createuser")
    Call<RetrofitResponse> createUser (
      @Field("email") String email,
      @Field("username") String username,
      @Field("password") String password
    );


    @FormUrlEncoded
    @POST("/login")
    Call<LoginResponse> login (
            @Field("email") String email,
            @Field("password") String password
    );
}
