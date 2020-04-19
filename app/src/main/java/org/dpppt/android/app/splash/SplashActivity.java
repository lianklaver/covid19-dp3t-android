package org.dpppt.android.app.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.dpppt.android.app.R;
import org.dpppt.android.app.main.MainActivity;
import org.dpppt.android.app.onboarding.OnboardingActivity;
import org.dpppt.android.app.selectlanguage.SelectLanguageActivity;
import org.dpppt.android.app.util.SharedPreferencesUtil;

public class SplashActivity extends AppCompatActivity {

    private static final int REQ_SELECT_LANGUAGE = 1;
    private static final int REQ_ONBOARDING = 2;
    private static final int DELAY = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (savedInstanceState == null) {
                    boolean onboardingCompleted = SharedPreferencesUtil.getOnBoardingCompleted(SplashActivity.this);
                    if (onboardingCompleted) {
                        goToMain();
                    } else {
                        startActivityForResult(new Intent(SplashActivity.this, SelectLanguageActivity.class), REQ_SELECT_LANGUAGE);
                    }
                }
            }
        }, DELAY);
    }

    public void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_SELECT_LANGUAGE) {
            if (resultCode == RESULT_OK) {
                startActivityForResult(new Intent(SplashActivity.this, OnboardingActivity.class), REQ_ONBOARDING);
            } else {
                finish();
            }
        } else if (requestCode == REQ_ONBOARDING) {
            if (resultCode == RESULT_OK) {
                SharedPreferencesUtil.setOnBoardingCompleted(this, true);
                goToMain();
            } else {
                finish();
            }
        }
    }

}
