package com.example.yeon1213.famidge;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtility {

    public static boolean checkAutoLogin(Context context) {
        SharedPreferences login = context.getSharedPreferences("Login", Context.MODE_PRIVATE);

        return login.getBoolean("auto", false);
    }

    public static String getID(Context context) {
        SharedPreferences login = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        String loginID = login.getString("ID", null);

        return loginID.equals(null) ? null : loginID;
    }

    public static String getPassword(Context context) {
        SharedPreferences login = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        String password = login.getString("Password", null);

        return password.equals(null) ? null : password;
    }

}
