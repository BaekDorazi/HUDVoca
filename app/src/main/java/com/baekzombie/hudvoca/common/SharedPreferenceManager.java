package com.baekzombie.hudvoca.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceManager {
    /**
     * SharedPreference name
     */
    private static final String PreferenceKey = "ALABS_MOBILE_CHIEF";

    /**
     * 로그인 사용자 FCM Token
     */
    public static final String PREF_FCM_TOKEN = "PREF_FCM_TOKEN";

    public static SharedPreferences get(Context context) {
        return context.getSharedPreferences(PreferenceKey, Context.MODE_PRIVATE);
    }

    public static boolean putString(Context context, String key, String value) {
        SharedPreferences preference = get(context);
        if (preference != null) {
            Editor editor = preference.edit();
            editor.putString(key, value);
            return editor.commit();
        } else {
            return false;
        }
    }

    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences preference = get(context);
        if (preference != null) {
            Editor editor = preference.edit();
            editor.putInt(key, value);
            return editor.commit();
        } else {
            return false;
        }
    }

    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences preference = get(context);
        if (preference != null) {
            Editor editor = preference.edit();
            editor.putBoolean(key, value);
            return editor.commit();
        } else {
            return false;
        }
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences preference = get(context);
        return preference.getString(key, defaultValue);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences preference = get(context);
        return preference.getInt(key, defaultValue);
    }

    public static boolean getBoolean(Context context, String key) {
        return get(context).getBoolean(key, false);
    }

    public static boolean isExistPreference(Context context, String key) {
        SharedPreferences preference = get(context);
        if (preference.contains(key)) {
            return true;
        }

        return false;
    }
}