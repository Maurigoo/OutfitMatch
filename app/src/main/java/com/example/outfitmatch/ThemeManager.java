package com.example.outfitmatch;

import android.content.Context;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {
    public static void applyTheme(Context context) {
        boolean isDarkMode = SettingsManager.isDarkModeEnabled(context);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}
