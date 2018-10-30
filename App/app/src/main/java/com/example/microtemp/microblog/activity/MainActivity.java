package com.example.microtemp.microblog.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.microtemp.microblog.R;

public class MainActivity extends AppCompatActivity {

    TextView txtViewRespone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtViewRespone = (TextView) findViewById(R.id.response);
        Intent intent = getIntent();
        String data = intent.getStringExtra("response");
        txtViewRespone.setText(data);


    }
}
