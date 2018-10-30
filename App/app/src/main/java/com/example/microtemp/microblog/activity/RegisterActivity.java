package com.example.microtemp.microblog.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.example.microtemp.microblog.R;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {


    private EditText username, email, password;
    private Button registerBtn, registerBackBtn;
    private ProgressBar progressBar;
    private static String Url_Reg = "http://212.191.92.88:51020";

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
                try {
                    Registration();

                    hideKeyboardFrom(getApplicationContext(), v);
                    Snackbar snackbar = Snackbar.make(v, "Rejestracja powiodła się", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }, 2000);


                } catch (AuthFailureError authFailureError) {
                    authFailureError.printStackTrace();
                }
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

    private void Registration() throws AuthFailureError {

        final String email = this.email.getText().toString().trim();
        final String nick = this.username.getText().toString().trim();
        final String password = this.password.getText().toString().trim();

        if (TextUtils.isEmpty(nick)) {
            username.setError("Please enter username");
            username.requestFocus();
            return ;
        }
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


        StringBuilder request = new StringBuilder(Url_Reg);
        request.append("/email/" + email + "/nick/" + nick + "/haslo/" + password);
        try {
            URL url = new URL(request.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.flush();
            os.close();


            Log.d("RESPONSE", con.getResponseMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
