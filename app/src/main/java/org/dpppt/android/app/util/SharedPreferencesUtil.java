package org.dpppt.android.app.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    private static final String PREFS_COVID = "PREFS_COVID";
    private static final String PREF_KEY_ONBOARDING_COMPLETED = "PREF_KEY_ONBOARDING_COMPLETED";
    private static final String PREF_KEY_PREFERRED_LANGUAGE = "PREF_KEY_PREFERRED_LANGUAGE";

    public static boolean getOnBoardingCompleted(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_COVID, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREF_KEY_ONBOARDING_COMPLETED, false);
    }

    public static void setOnBoardingCompleted(Context context, boolean completed) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_COVID, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(PREF_KEY_ONBOARDING_COMPLETED, completed).apply();
    }

    public static int getPreferredLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_COVID, Context.MODE_PRIVATE);
        return preferences.getInt(PREF_KEY_PREFERRED_LANGUAGE, -1);
    }

    public static void setPreferredLanguage(Context context, int languageId) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_COVID, Context.MODE_PRIVATE);
        preferences.edit().putInt(PREF_KEY_PREFERRED_LANGUAGE, languageId).apply();
    }
}
