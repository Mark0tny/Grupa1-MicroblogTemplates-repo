package com.example.microtemp.microblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.microtemp.microblog.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private static String LOG_TAG = "LoginActivity";
    public Button loginBtn, registerBtn;
    public EditText email, password;
    private static String Url_Reg = "http://212.191.92.88:51020";

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
                } catch (IOException e) {
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

    private void Login() throws IOException {

        final String email = this.email.getText().toString().trim();
        final String password = this.password.getText().toString().trim();
        StringBuilder request = new StringBuilder(Url_Reg);
        request.append("/email/" + email + "/haslo/" + password);
        URL obj = new URL(request.toString());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        try{
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            Log.d("ResponseCode: ",Integer.toString(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Intent i = new Intent(this, UserProfileActivity.class);
                i.putExtra("response", response.toString());
                startActivity(i);
                Log.d("LOGIN RESPONSE",response.toString());


            } else {
                Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
