package com.example.microtemp.microblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.api.SessionManager;
import com.example.microtemp.microblog.model.User;
import com.example.microtemp.microblog.ui.ListItemMicroblog;
import com.example.microtemp.microblog.ui.MicroblogRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

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

        adapter = new MicroblogRecyclerViewAdapter(microblogList, this);
        recyclerView.setAdapter(adapter);

        TextView txtViewResponeUsername = (TextView) findViewById(R.id.responseUsername);
        txtViewResponeUsername.setText("Witaj " + user.getEmail());


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
