package com.example.microtemp.microblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.api.RetrofitClient;
import com.example.microtemp.microblog.api.SessionManager;
import com.example.microtemp.microblog.model.User;
import com.example.microtemp.microblog.api.GetPostResponse;
import com.example.microtemp.microblog.ui.PostRecyclerViewAdapter;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity {

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;
    List<GetPostResponse> postList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final Integer id = intent.getIntExtra("id", 0);
        final String data = intent.getStringExtra("name");

        User user = SessionManager.getInstance(this).getUser();
        JsonObject jsonPost = new JsonObject();
        jsonPost.addProperty("id", id);
        loadPost(jsonPost);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this, CreatePostActivity.class);
                intent.putExtra("id_microblog", id);
                startActivity(intent);
            }
        });
        TextView txtViewResponeUsername = findViewById(R.id.responseMicroblogName);
        txtViewResponeUsername.setText(data);
    }

    public void loadPost(JsonObject jsonPost) {

        retrofit2.Call<List<GetPostResponse>> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .getPosts(jsonPost);

        call.enqueue(new Callback<List<GetPostResponse>>() {
            @Override
            public void onResponse(Call<List<GetPostResponse>> call, Response<List<GetPostResponse>> response) {
                if (response.isSuccessful()) {
                    postList = response.body();
                    recyclerViewInit(postList);
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetPostResponse>> call, Throwable t) {
                Log.d("ERROR", t.toString());
            }
        });
    }

    public void recyclerViewInit(final List<GetPostResponse> postList) {

        recyclerView = findViewById(R.id.post_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new PostRecyclerViewAdapter(postList, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

}
