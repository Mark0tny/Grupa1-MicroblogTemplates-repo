package com.example.microtemp.microblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.api.RetrofitClient;
import com.example.microtemp.microblog.api.SessionManager;
import com.example.microtemp.microblog.model.User;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        ArrayAdapter<String> privacyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.privacy));
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(CreateMicroblogActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                    }
                }, 1000);
            }
        });
    }

    private void createMicroBlog() throws IOException {
        User user = SessionManager.getInstance(this).getUser();
        final String title = this.title.getText().toString().trim();
        final String tags = this.tags.getText().toString().trim();
        final String privacy = this.spinner.getSelectedItem().toString().toLowerCase().trim();

        JsonObject jsonMicroblog = new JsonObject();
        jsonMicroblog.addProperty("title", title);
        jsonMicroblog.addProperty("id", user.getId());
        jsonMicroblog.addProperty("tags", tags);
        jsonMicroblog.addProperty("private", privacy);
        Log.d("JSON BODY", jsonMicroblog.toString());

        retrofit2.Call<JsonObject> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .createmicroblog(jsonMicroblog);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Microblog created", Toast.LENGTH_LONG).show();
                    Log.d("GOOD", Integer.toString(response.code()));
                } else {
                    Toast.makeText(getApplicationContext(), Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                    Log.d("ERROR", Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("ERROR",t.getMessage());
            }
        });
    }


}
