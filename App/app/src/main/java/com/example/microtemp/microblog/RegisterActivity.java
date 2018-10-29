package com.example.microtemp.microblog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    private EditText username, email, password;
    private Button registerBtn, registerBackBtn;
    private ProgressBar progressBar;
    private static String Url_Reg = "http://212.191.92.88:51020";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        progressBar.setVisibility(View.VISIBLE);
        registerBtn.setVisibility(View.GONE);

        final String email = this.email.getText().toString().trim();
        final String nick = this.username.getText().toString().trim();
        final String password = this.password.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url_Reg,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(RegisterActivity.this, "Rejestracja powiodła się", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Rejestracja nie powiodła się " + e.toString(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            registerBtn.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new LinkedHashMap<>();
                params.put("email", email);
                params.put("nick", nick);
                params.put("password", password);
                String result = params.toString();
                Log.d("REGISTER TEST", result);
                Log.d("REGISTER TEST", result);
                Log.d("REGISTER TEST", result);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        Toast.makeText(this, stringRequest.toString(),Toast.LENGTH_SHORT).show();
        Log.d("REQUEST TEST", stringRequest.toString());
        Log.d("REQUEST TEST URL", stringRequest.getUrl());
        Log.d("REQUEST TEST BODY C", stringRequest.getBodyContentType());
        Log.d("REQUEST TEST BODY", stringRequest.getBody().toString());

    }
}
