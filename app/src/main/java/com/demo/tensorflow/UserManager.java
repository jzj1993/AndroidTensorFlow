package com.demo.tensorflow;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class UserManager {

    public static boolean login(Context context, String user, String password) {
        if (TextUtils.isEmpty(user)) {
            return false;
        }
        SharedPreferences sp = sp(context);
        String pwd = sp.getString(user, null);
        return pwd != null && pwd.equals(password);
    }

    public static void signUp(Context context, String user, String password) {
        SharedPreferences sp = sp(context);
        sp.edit().putString(user, password).apply();
    }

    private static SharedPreferences sp(Context context) {
        return context.getSharedPreferences("account", Context.MODE_PRIVATE);
    }
}
