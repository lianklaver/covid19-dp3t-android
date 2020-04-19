package org.dpppt.android.app.selectlanguage.model;


import androidx.annotation.StringRes;

import org.dpppt.android.app.R;

public enum Language {
    NEDERLANDS(0, R.string.language_selection_nederlands),
    ENGLISH(1, R.string.language_selection_english);

    private final int id;
    private final int stringResource;

    Language(int id, @StringRes int stringResource) {
        this.id = id;
        this.stringResource = stringResource;
    }

    public static Language find(int id) {
        for (Language language : values()) {
            if (language.id == id) {
                return language;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public int getStringResource() {
        return stringResource;
    }
}