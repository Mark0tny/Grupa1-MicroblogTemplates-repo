package com.example.microtemp.microblog.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.api.RetrofitClient;
import com.example.microtemp.microblog.api.SessionManager;
import com.example.microtemp.microblog.model.Comment;
import com.example.microtemp.microblog.model.User;
import com.example.microtemp.microblog.ui.CommentsRecyclerViewAdapter;
import com.example.microtemp.microblog.ui.GetPostResponse;
import com.example.microtemp.microblog.ui.PostRecyclerViewAdapter;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;
    private Button buttonAddComment;
    private EditText commentContent;
    private List<Comment> commentList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        buttonAddComment = findViewById(R.id.comment_post_btn);
        commentContent = findViewById(R.id.comment_field);
        loadComments(initGetComments());



        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(commentContent.getText().toString().trim())){
                    addComment(initAddComent(),initGetComments());
                }else{
                    commentContent.setError("Field can not be empty");
                    commentContent.requestFocus();
                    return;

                    }
            }
        });
    }

    private void addComment(JsonObject jsonAddComment,JsonObject jsonCommentList) {
        retrofit2.Call<JsonObject> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .createComment(jsonAddComment);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Toast.makeText(getApplicationContext(), "Comment added", Toast.LENGTH_LONG).show();
                Log.d("GOOD", Integer.toString(response.code()));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
        loadComments(jsonCommentList);
    }
    public void loadComments(JsonObject jsonCommentList){
        retrofit2.Call<List<Comment>> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .getComments(jsonCommentList);

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    commentList = response.body();
                    recyclerViewInit(commentList);
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });


    }



    public void recyclerViewInit(final List<Comment> commentList) {

        recyclerView = findViewById(R.id.comment_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new CommentsRecyclerViewAdapter(commentList, getApplicationContext());
        recyclerView.setAdapter(adapter);

    }

    public JsonObject initAddComent(){
        Intent intent = getIntent();
        final Integer id = intent.getIntExtra("id_post",0);
        User user = SessionManager.getInstance(this).getUser();
        JsonObject jsonAddComment = new JsonObject();
        jsonAddComment.addProperty("post_id",id);
        jsonAddComment.addProperty("content",commentContent.getText().toString().trim());
        jsonAddComment.addProperty("author",user.getId());
        return  jsonAddComment;


    }
    public JsonObject initGetComments(){
        Intent intent = getIntent();
        final Integer id = intent.getIntExtra("id_post",0);
        JsonObject jsonCommentList = new JsonObject();
        jsonCommentList.addProperty("id",id);
        return jsonCommentList;
    }

}
