package com.example.microtemp.microblog.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.microtemp.microblog.api.RetrofitResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    private EditText username, email, password;
    private Button registerBtn, registerBackBtn;
    private ProgressBar progressBar;
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
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                        }
//                    }, 2000);


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

        final String email = this.email.getText().toString().trim();
        final String nick = this.username.getText().toString().trim();
        final String password = this.password.getText().toString().trim();

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

        retrofit2.Call<RetrofitResponse> call = RetrofitClient
                .getmInstance()
                .getAPI()
                .createUser(email, nick, password);

       call.enqueue(new Callback<RetrofitResponse>() {
           @Override
           public void onResponse(Call<RetrofitResponse> call, Response<RetrofitResponse> response) {

               if(response.code() == 200){
                   RetrofitResponse retrofitResponse = response.body();
                   Toast.makeText(getApplicationContext(),retrofitResponse.getMsg(),Toast.LENGTH_LONG).show();
               }else if(response.code()== 400){
                   Toast.makeText(getApplicationContext(),"User already exist",Toast.LENGTH_LONG).show();
               }
           }

           @Override
           public void onFailure(Call<RetrofitResponse> call, Throwable t) {

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
}
