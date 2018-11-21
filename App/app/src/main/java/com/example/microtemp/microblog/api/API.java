package com.example.microtemp.microblog.api;
import com.example.microtemp.microblog.ui.GetMicroblogResponse;
import com.example.microtemp.microblog.ui.GetPostResponse;
import com.google.gson.JsonObject;
import java.util.List;
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
    Call<List<GetMicroblogResponse>> getMicroblogs(
            @Body JsonObject jsonMicrolog
    );

    @POST("/addpost")
    Call<JsonObject> createPost(
            @Body JsonObject jsonPost
    );

    @POST("/getposts")
    Call<List<GetPostResponse>> getPosts(
            @Body JsonObject jsonPost
    );

    @POST("/addcomment")
    Call<JsonObject> createComment(
            @Body JsonObject jsonComment
    );


}
