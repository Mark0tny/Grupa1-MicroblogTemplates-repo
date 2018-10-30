package com.example.microtemp.microblog.activity;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.ui.ListItemMicroblog;

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

        recyclerView = (RecyclerView) findViewById(R.id.microblog_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        microblogList = new ArrayList<>();

        //Dodanie Obiektów do listy adaptera

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        TextView txtViewRespone = (TextView) findViewById(R.id.response);
        Intent intent = getIntent();
        String data = intent.getStringExtra("response");
        txtViewRespone.setText(data);

    }

}
