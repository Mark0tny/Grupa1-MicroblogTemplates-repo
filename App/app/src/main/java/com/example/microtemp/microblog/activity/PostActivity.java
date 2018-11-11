package com.example.microtemp.microblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.microtemp.microblog.R;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(PostActivity.this, CreatePostActivity.class);
                startActivity(intent);
            }
        });

        TextView txtViewResponeId = (TextView) findViewById(R.id.responseID);
        TextView txtViewResponeUsername = (TextView) findViewById(R.id.responseUsername);
        Intent intent = getIntent();
        String data = intent.getStringExtra("response");
        String[] response = data.split(":");
        txtViewResponeId.setText(response[0]);
        txtViewResponeUsername.setText(response[1]);
    }

}
