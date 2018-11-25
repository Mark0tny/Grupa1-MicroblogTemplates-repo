package com.example.microtemp.microblog.api;
import com.example.microtemp.microblog.model.Comment;
import com.example.microtemp.microblog.model.User;
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

    @POST("/getcomments")
    Call<List<Comment>> getComments(
            @Body JsonObject jsonComment
    );

    @POST("/upvote")
    Call<JsonObject> likePost(
            @Body JsonObject jsonLike
    );

    @POST("/follow")
    Call<JsonObject> followBlog(
            @Body JsonObject jsonFollow
    );

    @POST("/followed")
    Call<JsonObject> getfollowedblogs(
            @Body JsonObject jsonFollow
    );

    @POST("/followers")
    Call<List<User>> getfollowers(
            @Body JsonObject jsonUsers
    );





}
