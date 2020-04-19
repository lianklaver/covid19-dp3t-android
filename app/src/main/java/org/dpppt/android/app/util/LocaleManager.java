package org.dpppt.android.app.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import org.dpppt.android.app.selectlanguage.model.Language;

import java.util.Locale;

public class LocaleManager {

    public static void setAppLocale(Context context) {
        Language language = Language.find(SharedPreferencesUtil.getPreferredLanguage(context));
        if (language == null) {
            language = Language.ENGLISH;
        }

        Locale locale = getLocale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, dm);
    }

    private static Locale getLocale(Language language) {
        switch (language) {
            case NEDERLANDS:
                return new Locale("nl");
            case ENGLISH:
                return Locale.ENGLISH;
        }
        return Locale.ENGLISH;
    }
}

