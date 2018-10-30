package com.example.microtemp.microblog.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.model.Post;

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

        Intent intent = new Intent(CreatePostActivity.this, Post.class);
        startActivity(intent);
    }

    public void CreateMicroblog(View view) {
        
        Intent intent = new Intent(CreatePostActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }
}
