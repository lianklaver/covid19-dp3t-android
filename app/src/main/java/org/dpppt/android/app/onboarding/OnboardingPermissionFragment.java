/*
 * Created by Ubique Innovation AG
 * https://www.ubique.ch
 * Copyright (c) 2020. All rights reserved.
 */

package org.dpppt.android.app.onboarding;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.dpppt.android.app.R;
import org.dpppt.android.app.util.DeviceFeatureHelper;
import org.dpppt.android.app.util.InfoDialog;

public class OnboardingPermissionFragment extends Fragment {

    private static final int REQUEST_CODE_ASK_PERMISSION_FINE_LOCATION = 123;

    private View tracingButton;
    private ImageView tracingIcon;
    private View batteryOptimizationButton;
    private ImageView batteryOptimizationIcon;
    private View bluetoothButton;
    private ImageView bluetoothIcon;

    private BroadcastReceiver bluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                updateButtonStatus();
            }
        }
    };

    public OnboardingPermissionFragment() {
        super(R.layout.fragment_onboarding_permission);
    }

    public static OnboardingPermissionFragment newInstance() {
        return new OnboardingPermissionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        requireActivity().registerReceiver(bluetoothStateReceiver, filter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tracingButton = view.findViewById(R.id.tracing_button);
        tracingButton.setOnClickListener(v -> {
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permissions, REQUEST_CODE_ASK_PERMISSION_FINE_LOCATION);
        });
        tracingIcon = view.findViewById(R.id.tracing_icon);

        View tracingInfoButton = view.findViewById(R.id.tracing_info);
        tracingInfoButton.setOnClickListener(v -> {
            InfoDialog.newInstance(R.string.onboarding_android_location_permission_info)
                    .show(getChildFragmentManager(), InfoDialog.class.getCanonicalName());
        });

        batteryOptimizationButton = view.findViewById(R.id.battery_optimization_button);
        batteryOptimizationButton.setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                    Uri.parse("package:" + requireContext().getPackageName())));
        });
        batteryOptimizationIcon = view.findViewById(R.id.battery_optimization_icon);

        View batteryInfoButton = view.findViewById(R.id.battery_optimization_info);
        batteryInfoButton.setOnClickListener(v -> {
            InfoDialog.newInstance(R.string.onboarding_android_battery_saving_info)
                    .show(getChildFragmentManager(), InfoDialog.class.getCanonicalName());
        });

        bluetoothButton = view.findViewById(R.id.bluetooth_button);
        bluetoothButton.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), getString(R.string.activate_bluetooth_button) + " ...", Toast.LENGTH_SHORT).show();
            BluetoothAdapter.getDefaultAdapter().enable();
        });
        bluetoothIcon = view.findViewById(R.id.bluetooth_icon);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateButtonStatus();
    }

    private void updateButtonStatus() {
        boolean locationPermissionGranted = DeviceFeatureHelper.isLocationPermissionGranted(requireContext());
        if (locationPermissionGranted) {
            tracingIcon.setImageResource(R.drawable.ic_green_check);
        } else {
            tracingIcon.setImageResource(R.drawable.ic_push_notifications);
        }

        boolean batteryOptDeactivated = DeviceFeatureHelper.isBatteryOptimizationDeactivated(requireContext());
        if (batteryOptDeactivated) {
            batteryOptimizationIcon.setImageResource(R.drawable.ic_green_check);
        } else {
            batteryOptimizationIcon.setImageResource(R.drawable.ic_push_notifications);
        }

        boolean bluetoothEnabled = DeviceFeatureHelper.isBluetoothEnabled();
        if (bluetoothEnabled) {
            bluetoothIcon.setImageResource(R.drawable.ic_green_check);
        } else {
            bluetoothIcon.setImageResource(R.drawable.ic_bluetooth);
        }

        ((OnboardingActivity) requireActivity()).updateStepButton();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(bluetoothStateReceiver);
    }

}