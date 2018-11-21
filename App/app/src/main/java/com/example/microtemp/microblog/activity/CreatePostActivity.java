package com.example.microtemp.microblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.api.RetrofitClient;
import com.example.microtemp.microblog.api.SessionManager;
import com.example.microtemp.microblog.model.Post;
import com.example.microtemp.microblog.model.User;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends AppCompatActivity {


    private EditText title, content, tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);


    }

    public void CreatePost(View view) {
        title = findViewById(R.id.edit_post_name);
        content = findViewById(R.id.edit_post_content);
        tags = findViewById(R.id.edit_tag);

        final String title = this.title.getText().toString().trim();
        final String content = this.content.getText().toString().trim();
        final String tags = this.tags.getText().toString().trim();
        User user = SessionManager.getInstance(this).getUser();
        Intent intent = getIntent();
        final Integer id = intent.getIntExtra("id_microblog",0);
        JsonObject jsonPost = new JsonObject();
        jsonPost.addProperty("author_id", user.getId());
        jsonPost.addProperty("title", title);
        jsonPost.addProperty("content", content);
        jsonPost.addProperty("blog_id", id);
        jsonPost.addProperty("tags", tags);

        Log.d("JSON BODY", jsonPost.toString());

        addPost(jsonPost);
    }

    public void addPost(JsonObject jsonPost) {

        retrofit2.Call<JsonObject> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .createPost(jsonPost);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Post created", Toast.LENGTH_LONG).show();
                    Log.d("GOOD", Integer.toString(response.code()));
                } else {
                    Toast.makeText(getApplicationContext(), Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                    Log.d("ERROR", Integer.toString(response.code()));
                }


            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Post created", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(CreatePostActivity.this, Post.class);
                        startActivity(intent);
                    }
                }, 1000);
            }
        });


    }


}
