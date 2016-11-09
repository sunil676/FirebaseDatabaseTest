package com.sunil.firebasedatabasetest.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sunil on 11/7/16.
 */

public class SharedPreferenceUtils {

    private static SharedPreferenceUtils sInstance;

    private SharedPreferences mPrefs;

    public static SharedPreferenceUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferenceUtils(context);
        }
        return sInstance;
    }

    private SharedPreferenceUtils(Context context) {
        mPrefs = context.getSharedPreferences("FirebasePref", Context.MODE_PRIVATE);
    }


    public String getUUID() {
        return mPrefs.getString("UUID",null);
    }

    public void setUUID(String uuid) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("UUID", uuid);
        editor.apply();
    }

    public String getEmail() {
        return mPrefs.getString("Email",null);
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("Email", email);
        editor.apply();
    }

}
