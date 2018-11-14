package com.example.microtemp.microblog.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.api.RetrofitClient;
import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    private EditText username, email, password;
    private Button registerBtn, registerBackBtn;
    private ProgressBar progressBar;
    boolean regSucces = false;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        progressBar = findViewById(R.id.register_progress);
        username = findViewById(R.id.nickname_register);
        email = findViewById(R.id.email_register);
        password = findViewById(R.id.password_register);
        registerBtn = findViewById(R.id.send_request_button);
        registerBackBtn = findViewById(R.id.register_back_button);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registration();
                hideKeyboardFrom(getApplicationContext(), v);


            }
        });


        registerBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                email.setText("");
                password.setText("");
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void Registration() {

        progressBar.setVisibility(View.VISIBLE);

        final String email = this.email.getText().toString().trim();
        final String nick = this.username.getText().toString().trim();
        final String password = this.password.getText().toString().trim();

        JsonObject jsonUser = new JsonObject();
        jsonUser.addProperty("username", nick);
        jsonUser.addProperty("email", email);
        jsonUser.addProperty("password", password);
        Log.d("JSON BODY", jsonUser.toString());

        if (TextUtils.isEmpty(nick)) {
            username.setError("Please enter username");
            username.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            this.email.setError("Please enter email");
            this.email.requestFocus();
            return;
        }
        if (!validateEmail(email)) {
            this.email.setError("Please enter valid email");
            this.email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            this.password.setError("Please enter password");
            this.password.requestFocus();
            return;
        }

        retrofit2.Call<JsonObject> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .createUser(jsonUser);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Toast.makeText(getApplicationContext(), "Registration SUCCESS", Toast.LENGTH_LONG).show();
                        regSucces = true;
                        nextActivity();
                    }
                } else if (response.code() == 400) {
                    Toast.makeText(getApplicationContext(), "User Already Exists", Toast.LENGTH_LONG).show();
                    regSucces = false;
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("FAILURE 400", t.getMessage());
            }
        });
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public void nextActivity(){
        if(regSucces) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }, 2000);
        }
    }

}
