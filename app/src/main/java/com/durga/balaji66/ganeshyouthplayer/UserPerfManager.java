package com.durga.balaji66.ganeshyouthplayer;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPerfManager {
   private Context mContext;

    UserPerfManager(Context context) {
        this.mContext = context;
    }

    public void saveLoginDetails(String email, String password) {
        SharedPreferences preferences = mContext.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Phone", email);
        editor.putString("Password", password);
        editor.apply();
    }

    public String getMobile() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Phone", "");
    }

    public boolean isUserLogOut() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        boolean isEmailEmpty = sharedPreferences.getString("Phone", "").isEmpty();
        boolean isPasswordEmpty = sharedPreferences.getString("Password", "").isEmpty();
        return isEmailEmpty || isPasswordEmpty;
    }

    public void clear() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("Phone");
        editor.remove("Password");
        editor.apply();
    }


}
