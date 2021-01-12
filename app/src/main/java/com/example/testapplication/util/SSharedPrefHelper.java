package com.example.testapplication.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.testapplication.GlobalApplication;

public class SSharedPrefHelper {
    public static final String TAG = SSharedPrefHelper.class.getSimpleName();
    // SharedPreference Key
    public static final String PREF_COMMON_KEY = "AppPref";

    public static void setSharedData(String Key, String content) {
        try {
            SharedPreferences pref = getContext().getSharedPreferences(PREF_COMMON_KEY, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(Key, content);
            editor.commit();
        } catch (Exception e) {
            L.w(TAG, e.getMessage());
        }
    }

    public static String getSharedData(String Key) {
        return getSharedData(getContext(), Key, "");
    }

    public static String getSharedData(String Key, String defVal) {
        return getSharedData(getContext(), Key, defVal);
    }

    public static String getSharedData(Context context, String Key) {
        return getSharedData(context, Key, "");
    }

    public static String getSharedData(Context context, String Key, String defVal) {
        String content = defVal;
        try {
            SharedPreferences pref = context.getSharedPreferences(PREF_COMMON_KEY, Activity.MODE_PRIVATE);
            content = pref.getString(Key, null);
            if (content == null) {
                content = defVal;
            }
        } catch (Exception e) {
            L.w(TAG, e.getMessage());
        }
        return content;
    }

    public static void setSharedDataBoolean(String key, boolean value) {
        String bool = value ? "1" : "0";
        setSharedData(key, bool);
    }

    public static Boolean getSharedDataBoolean(String key) {
        String strBool = getSharedData(key);
        boolean bool = ("1".equals(strBool));
        return bool;
    }

    public static Boolean getSharedDataBoolean(String key, boolean defaultValue) {
        String strBool = getSharedData(key);
        boolean bool = Util.isEmpty(strBool) ? defaultValue : ("1".equals(strBool));
        return bool;
    }

    private static Context getContext() {
        Context context = GlobalApplication.getInstance().getApplicationContext();
        return context;
    }

}
