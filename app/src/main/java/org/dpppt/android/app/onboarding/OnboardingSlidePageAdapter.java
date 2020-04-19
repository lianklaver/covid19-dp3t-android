/*
 * Created by Ubique Innovation AG
 * https://www.ubique.ch
 * Copyright (c) 2020. All rights reserved.
 */

package org.dpppt.android.app.onboarding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.dpppt.android.app.R;

public class OnboardingSlidePageAdapter extends FragmentStateAdapter {

    public static final int SCREEN_INDEX_PERMISSIONS = 2;

    public OnboardingSlidePageAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return OnboardingContentFragment.newInstance(
                        R.string.onboarding_step_one_title,
                        R.string.onboarding_step_one_message,
                        R.drawable.ill_on_boarding_1,
                        R.drawable.ill_grey_oval
                );
            case 1:
                return OnboardingContentFragment.newInstance(
                        R.string.onboarding_step_two_title,
                        R.string.onboarding_step_two_message,
                        R.drawable.ill_on_boarding_2,
                        R.drawable.ill_green_oval
                );
            case SCREEN_INDEX_PERMISSIONS:
                return OnboardingPermissionFragment.newInstance();
        }
        throw new IllegalArgumentException("There is no fragment for view pager position " + position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
