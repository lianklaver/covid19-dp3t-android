package org.dpppt.android.app.selectlanguage.adapters;

import org.dpppt.android.app.selectlanguage.model.Language;

public class LanguageUiModel {

    private boolean isSelected = false;
    private String label;
    private Language language;

    public LanguageUiModel(Language language, String label) {
        this.language = language;
        this.label = label;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Language getLanguage() {
        return language;
    }
}
