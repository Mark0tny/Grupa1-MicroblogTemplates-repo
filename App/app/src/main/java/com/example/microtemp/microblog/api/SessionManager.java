package com.example.microtemp.microblog.api;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.microtemp.microblog.model.User;

public class SessionManager {

    private static SessionManager pref;
    private Context context;
    int PRIVATE_MODE = 0;

    public static final String PREF_NAME = "UserPref";
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";

    private SessionManager(Context context) {
        this.context = context;
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (pref == null) {
            pref = new SessionManager(context);
        }
        return pref;
    }

    public void saveUser(User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("id", user.getId());
        editor.putString("email", user.getEmail());
        editor.putString("username", user.getUsername());

        editor.apply();

    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getInt("id", -1) != -1;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("username", null),
                sharedPreferences.getString("password", null), null, null, null
        );
    }

    //logout
    public void clear() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
