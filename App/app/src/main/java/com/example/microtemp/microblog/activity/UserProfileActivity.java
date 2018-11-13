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
import com.example.microtemp.microblog.ui.ListItemMicroblog;
import com.example.microtemp.microblog.ui.MicroblogRecyclerViewAdapter;
import com.example.microtemp.microblog.api.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;


    private List<ListItemMicroblog> microblogList;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        session = new SessionManager(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.microblog_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        microblogList = new ArrayList<>();

        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();

        String name = user.get(SessionManager.KEY_NAME);
        String email = user.get(SessionManager.KEY_EMAIL);

        TextView txtViewResponeId = (TextView) findViewById(R.id.responseID);
        TextView txtViewResponeUsername = (TextView) findViewById(R.id.responseUsername);
        Intent intent = getIntent();
        String data = intent.getStringExtra("response");
        String[] response = data.split(":");
       /* txtViewResponeId.setText(response[0]);
        txtViewResponeUsername.setText(response[1]);*/
        txtViewResponeId.setText(name);
        txtViewResponeUsername.setText(email);


        URL url = null;
        try {
            url = new URL("http://212.191.92.88:51020/getmymicroblogs/15");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    JSONObject obj = new JSONObject(line.substring(0, line.length()));
                    String author = obj.getString("author");
                    String title = obj.getString("name");
                    microblogList.add(new ListItemMicroblog(author, title, " "));

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new MicroblogRecyclerViewAdapter(microblogList, this);
        recyclerView.setAdapter(adapter);

        //Dodanie Obiektów do listy adaptera

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
                session.logoutUser();
                finish();
                return false;
            }
        });



    }


}
