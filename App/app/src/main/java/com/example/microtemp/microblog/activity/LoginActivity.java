package com.example.microtemp.microblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.api.LoginResponse;
import com.example.microtemp.microblog.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.microtemp.microblog.activity.RegisterActivity.validateEmail;

public class LoginActivity extends AppCompatActivity {

    private static String LOG_TAG = "LoginActivity";
    public Button loginBtn, registerBtn;
    public EditText email, password;


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

        loginBtn = findViewById(R.id.sign_in_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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

        Call<LoginResponse> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .login(email, password);
        Toast.makeText(getApplicationContext(), call.request().toString(), Toast.LENGTH_LONG).show();

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response != null) {
                    LoginResponse loginResponse = response.body();
                    Log.d("RESPONSE CODE", Integer.toString(response.code()));
                   //Toast.makeText(getApplicationContext(), loginResponse.getMsg(), Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }


}
