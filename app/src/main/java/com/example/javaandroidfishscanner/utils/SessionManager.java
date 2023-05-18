package com.example.javaandroidfishscanner.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    // Key for storing the "is_logged_in" value in the shared preferences
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "user_session";

    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserSession(String username, String email) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public void setUserEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public HashMap<String, String> getUserSession() {
        HashMap<String, String> userData = new HashMap<>();
        userData.put(KEY_USERNAME, sharedPreferences.getString(KEY_USERNAME, null));
        userData.put(KEY_EMAIL, sharedPreferences.getString(KEY_EMAIL, null));
        return userData;
    }

    public void clearUserSession() {
        editor.clear();
        editor.apply();
    }
}