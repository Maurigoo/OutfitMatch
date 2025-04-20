package com.example.outfitmatch.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageManager {

    public static void setLocale(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = res.getConfiguration();
        config.setLocale(locale);

        res.updateConfiguration(config, res.getDisplayMetrics());

        // Guardar idioma
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("app_lang", lang).apply();
    }

    public static void loadLocale(Context context) {
        String lang = PreferenceManager.getDefaultSharedPreferences(context).getString("app_lang", "es");
        setLocale(context, lang);
    }
}
