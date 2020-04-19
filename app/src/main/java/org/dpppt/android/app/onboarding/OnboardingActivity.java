/*
 * Created by Ubique Innovation AG
 * https://www.ubique.ch
 * Copyright (c) 2020. All rights reserved.
 */

package org.dpppt.android.app.onboarding;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.dpppt.android.app.R;
import org.dpppt.android.app.util.LocaleManager;
import org.dpppt.android.sdk.DP3T;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleManager.setAppLocale(this);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.pager);
        pagerAdapter = new OnboardingSlidePageAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateStepButton();
            }
        });

        TabLayout tabLayout = findViewById(R.id.onboarding_tab_dots);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.select()).attach();

        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < pagerAdapter.getItemCount() - 1) {
                viewPager.setCurrentItem(currentItem + 1, true);
            } else {
                DP3T.start(this);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    public void updateStepButton() {
        button.setEnabled(checkPermissionsReady()
                || viewPager.getCurrentItem() < OnboardingSlidePageAdapter.SCREEN_INDEX_PERMISSIONS);
    }

    public boolean checkPermissionsReady() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean bluetoothEnabled = bluetoothAdapter != null && bluetoothAdapter.isEnabled();
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean batteryOptDeact = powerManager.isIgnoringBatteryOptimizations(this.getPackageName());
        boolean locationGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
        return bluetoothEnabled && batteryOptDeact && locationGranted;
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

}
