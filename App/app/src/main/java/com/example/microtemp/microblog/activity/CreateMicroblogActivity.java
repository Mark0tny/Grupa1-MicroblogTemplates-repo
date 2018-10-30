package com.example.microtemp.microblog.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.microtemp.microblog.R;

public class CreateMicroblogActivity extends AppCompatActivity {


    private EditText title, tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_microblog);

        title = findViewById(R.id.edit_microblog_name);
        tags = findViewById(R.id.edit_tag);
    }
}
