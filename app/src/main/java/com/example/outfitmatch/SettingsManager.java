package com.example.outfitmatch;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String SETTINGS_PREF = "settings";
    private static final String DARK_MODE_KEY = "is_dark_mode";

    // Método para guardar la preferencia del tema
    public static void saveThemePreference(Context context, boolean isDarkMode) {
        SharedPreferences preferences = context.getSharedPreferences(SETTINGS_PREF, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(DARK_MODE_KEY, isDarkMode).apply();
    }

    // Método para recuperar la preferencia del tema
    public static boolean isDarkModeEnabled(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SETTINGS_PREF, Context.MODE_PRIVATE);
        return preferences.getBoolean(DARK_MODE_KEY, false); // Por defecto, modo claro
    }
}
