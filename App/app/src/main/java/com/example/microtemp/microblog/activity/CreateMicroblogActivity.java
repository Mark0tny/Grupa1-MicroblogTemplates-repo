package com.example.microtemp.microblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.model.Post;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateMicroblogActivity extends AppCompatActivity {


    private EditText title, tags;
    private Spinner spinner;
    private Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_microblog);

        title = findViewById(R.id.edit_microblog_name);
        tags = findViewById(R.id.edit_tag);
        create = findViewById(R.id.create_microblog);
        spinner = findViewById(R.id.privacy_spinner);

        ArrayAdapter<String> privacyAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.privacy));
        privacyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(privacyAdapter);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createMicroBlog();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void createMicroBlog() throws IOException {

        final String title = this.title.getText().toString().trim();
        final String tags = this.tags.getText().toString().trim();
        StringBuilder request = new StringBuilder("http://212.191.92.88:51020");
        request.append("/create/microblog/" + title + "/author/" + 15 + "/private/" + "public");
        URL obj = new URL(request.toString());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        try {
            con.setRequestMethod("POST");
            int responseCode = con.getResponseCode();
            Log.d("ResponseCode: ", Integer.toString(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {

                String response = con.getResponseMessage();

                Toast.makeText(getApplicationContext(), "Blog Created!", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CreateMicroblog(View view) {
        Intent intent = new Intent(CreateMicroblogActivity.this, Post.class);
        startActivity(intent);
    }

}
