package com.example.microtemp.microblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.api.RetrofitClient;
import com.example.microtemp.microblog.api.SessionManager;
import com.example.microtemp.microblog.model.User;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.microtemp.microblog.activity.RegisterActivity.validateEmail;

public class LoginActivity extends AppCompatActivity {

    private static String LOG_TAG = "LoginActivity";
    public Button loginBtn, registerBtn;
    public EditText email, password;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.login_progress);

        loginBtn = findViewById(R.id.sign_in_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    Login();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        registerBtn = findViewById(R.id.register_button);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Login() throws Exception {

        final String email = this.email.getText().toString().trim();
        final String password = this.password.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            this.email.setError("Please enter email");
            this.email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            this.password.setError("Please enter password");
            this.password.requestFocus();
            return;
        }

        JsonObject jsonUser = new JsonObject();
        jsonUser.addProperty("login", email);
        jsonUser.addProperty("password", password);
        Log.d("JSON BODY", jsonUser.toString());

        retrofit2.Call<JsonObject> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .login(jsonUser);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Log.d("Response", response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            String id_user = jsonObject.getString("id_user").replace('"', ' ');
                            String username = jsonObject.getString("username").replace('"', ' ');
                            User user = new User();
                            user.setId(Integer.parseInt(id_user));
                            user.setUsername(username);
                            user.setPassword(password);
                            SessionManager.getInstance(LoginActivity.this).saveUser(user);
                            Log.d("PREFERENCES VALUE", user.getUsername() + "ID: " + user.getId());
                            nextActivity();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else if (response.code() == 400) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "User Don't Exists", Toast.LENGTH_LONG).show();
                        Log.d("400", "User Don't Exists");
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SessionManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void nextActivity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 1000);
    }


}
