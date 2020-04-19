package org.dpppt.android.app.selectlanguage;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.dpppt.android.app.R;
import org.dpppt.android.app.selectlanguage.adapters.LanguageUiModel;
import org.dpppt.android.app.selectlanguage.adapters.LanguagesAdapter;
import org.dpppt.android.app.selectlanguage.model.Language;
import org.dpppt.android.app.util.SharedPreferencesUtil;
import org.dpppt.android.app.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectLanguageActivity extends AppCompatActivity {

    private Button button;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<LanguageUiModel> languageUiModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.NextStepTheme_Splash);
        setContentView(R.layout.activity_select_language);
        setupStatusBar();
        setupAdapter();
        setupButton();
    }

    private void setupStatusBar() {
        UiUtils.setStatusBarColor(this, R.color.orange_main);
    }

    private void setupAdapter() {
        recyclerView = findViewById(R.id.languages_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        populateSupportedLanguages();
        mAdapter = new LanguagesAdapter(languageUiModels, languageUiModelModel -> {
            for (LanguageUiModel model : languageUiModels) {
                model.setSelected(false);
            }
            languageUiModelModel.setSelected(true);
            mAdapter.notifyDataSetChanged();

            button.setEnabled(true);
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void populateSupportedLanguages() {
        languageUiModels = new ArrayList<>();
        languageUiModels.add(new LanguageUiModel(Language.NEDERLANDS, getString(Language.NEDERLANDS.getStringResource())));
        languageUiModels.add(new LanguageUiModel(Language.ENGLISH, getString(Language.ENGLISH.getStringResource())));
    }

    private void setupButton() {
        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            setAppLanguage();
            setResult(Activity.RESULT_OK);
            finish();
        });
    }

    private void setAppLanguage() {
        LanguageUiModel selectedLanguageUiModel = null;
        for (LanguageUiModel model : languageUiModels) {
            if (model.isSelected()) {
                selectedLanguageUiModel = model;
                break;
            }
        }

        if (selectedLanguageUiModel != null) {
            SharedPreferencesUtil.setPreferredLanguage(this, selectedLanguageUiModel.getLanguage().getId());
        }
    }
}
