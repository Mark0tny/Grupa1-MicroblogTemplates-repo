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
import com.example.microtemp.microblog.ui.ListItemMicroblog;
import com.example.microtemp.microblog.ui.MicroblogRecyclerViewAdapter;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItemMicroblog> microblogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        User user = SessionManager.getInstance(this).getUser();

        recyclerView = (RecyclerView) findViewById(R.id.microblog_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        microblogList = new ArrayList<>();

        JsonObject jsonMicroblog = new JsonObject();
        jsonMicroblog.addProperty("id", user.getId());
        Log.d("JSON BODY", jsonMicroblog.toString());

        retrofit2.Call<JsonObject> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .getMicroblogs(jsonMicroblog);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonReader reader1;
                StringBuilder sb = new StringBuilder();

                if (response.isSuccessful()) {
                    try {
                        reader1 = new JsonReader(new InputStreamReader(response.raw().body().byteStream()));
                        reader1.setLenient(true);
                        String line;
                        while ((line = reader1.nextString()) != null) {
                            sb.append(line);
                            Log.d("JSON LINE", line);
                            JSONObject jsonObject = null;
                            jsonObject = new JSONObject(sb.toString());
                            String author = jsonObject.getString("author").replace('"', ' ');
                            String title = jsonObject.getString("name").replace('"', ' ');
                            String privacy = jsonObject.getString("private").replace('"', ' ');
                            //String tags = jsonObject.getString("tags").replace('"', ' ').replace(",","#");
                            microblogList.add(new ListItemMicroblog(author, title, privacy));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });


        TextView txtViewResponeUsername = (TextView) findViewById(R.id.responseUsername);
        txtViewResponeUsername.setText("Witaj " + user.getEmail());

        adapter = new MicroblogRecyclerViewAdapter(microblogList, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, CreateMicroblogActivity.class);
                startActivity(intent);
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finish();
                return false;
            }
        });
    }

}
