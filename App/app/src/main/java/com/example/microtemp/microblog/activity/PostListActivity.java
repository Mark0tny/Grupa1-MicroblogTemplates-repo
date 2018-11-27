package com.example.microtemp.microblog.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.api.RetrofitClient;
import com.example.microtemp.microblog.api.SessionManager;
import com.example.microtemp.microblog.model.User;
import com.example.microtemp.microblog.api.GetPostResponse;
import com.example.microtemp.microblog.ui.MicroblogRecyclerViewAdapter;
import com.example.microtemp.microblog.ui.PostRecyclerViewAdapter;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostListActivity extends AppCompatActivity {

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;
    List<GetPostResponse> postList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final Integer id = intent.getIntExtra("id", 0);
        final String data = intent.getStringExtra("name");
        String microBlog = data;
        JsonObject jsonPost = new JsonObject();
        jsonPost.addProperty("id", id);
        loadPost(jsonPost);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostListActivity.this, CreatePostActivity.class);
                intent.putExtra("id_microblog", id);
                startActivity(intent);
            }
        });
        TextView txtViewResponeUsername = findViewById(R.id.responseMicroblogName);
        txtViewResponeUsername.setText(microBlog.replaceAll("\"", ""));
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

    private void deletePost(JsonObject jsonPost) {
        retrofit2.Call<JsonObject> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .deletePost(jsonPost);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(getApplicationContext(), "Post deleted", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
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

        recyclerView.addOnItemTouchListener(
                new MicroblogRecyclerViewAdapter.
                        RecyclerItemClickListener(this, recyclerView, new MicroblogRecyclerViewAdapter.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(PostListActivity.this, PostActivity.class);
                        intent.putExtra("id_post", postList.get(position).getIdPost());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, final int position) {
                      final AlertDialog dialog = new AlertDialog.Builder(PostListActivity.this)
                              .setMessage("Are you sure?")
                              .setPositiveButton("Yes", null)
                              .setNegativeButton("No", null)
                              .show();
                        Button negativBtn = dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE);
                        negativBtn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        Button positivBtn = dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                        positivBtn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        positivBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                User user = SessionManager.getInstance(PostListActivity.this).getUser();
                                JsonObject jsonPost = new JsonObject();
                                jsonPost.addProperty("userid", user.getId());
                                jsonPost.addProperty("postid", postList.get(position).getIdPost());
                                deletePost(jsonPost);
                                dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onShowPress(View view, int position) {

                    }

                }));
    }



}
