package com.example.microtemp.microblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.api.RetrofitClient;
import com.example.microtemp.microblog.model.User;
import com.example.microtemp.microblog.ui.FollowersRecyclerViewAdapter;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersActivity extends AppCompatActivity {

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;
    private List<User> userList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        Intent intent = getIntent();
        final Integer id = intent.getIntExtra("id_blog", 0);
        JsonObject jsonGetFollowers = new JsonObject();
        jsonGetFollowers.addProperty("id", id);
        loadFollowers(jsonGetFollowers);
    }


    public void loadFollowers(JsonObject jsonFollowersList) {
        retrofit2.Call<List<User>> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .getfollowers(jsonFollowersList);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    userList = response.body();
                    recyclerViewInit(userList);
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });

    }

    public void recyclerViewInit(final List<User> usersList) {
        recyclerView = findViewById(R.id.followers_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new FollowersRecyclerViewAdapter(usersList, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}
