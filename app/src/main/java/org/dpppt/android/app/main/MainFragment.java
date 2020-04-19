/*
 * Created by Ubique Innovation AG
 * https://www.ubique.ch
 * Copyright (c) 2020. All rights reserved.
 */

package org.dpppt.android.app.main;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.dpppt.android.app.R;
import org.dpppt.android.app.contacts.ContactsFragment;
import org.dpppt.android.app.main.model.AppState;
import org.dpppt.android.app.main.views.HeaderView;
import org.dpppt.android.app.notifications.NotificationsFragment;
import org.dpppt.android.app.trigger.TriggerFragment;
import org.dpppt.android.app.util.TracingStatusHelper;
import org.dpppt.android.app.util.UiUtils;
import org.dpppt.android.sdk.TracingStatus;

import java.util.List;

public class MainFragment extends Fragment {

    private TracingViewModel tracingViewModel;
    private HeaderView headerView;
    private View headerViewBackground;
    private int statusBarColor = R.color.status_bar_grey;

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracingViewModel = new ViewModelProvider(requireActivity()).get(TracingViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupHeader(view);
        setupCards(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        tracingViewModel.invalidateTracingStatus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        headerView.stopArcAnimation();
    }

    private void setupHeader(View view) {
        headerView = view.findViewById(R.id.main_header_container);
        headerViewBackground = view.findViewById(R.id.main_header_bg);
        tracingViewModel.getAppStateLiveData()
                .observe(getViewLifecycleOwner(), appState -> {
                    setHeaderViewState(appState);
                });
    }

    private void setHeaderViewState(AppState appState) {
        headerView.setState(appState);

        int backgroundDrawable = R.drawable.bg_gradient_gray;
        switch (appState) {
            case TRACING:
                statusBarColor = R.color.status_bar_green;
                backgroundDrawable = R.drawable.bg_gradient_green;
                break;
            case ERROR:
            case EXPOSED_ERROR:
                statusBarColor = R.color.status_bar_grey;
                backgroundDrawable = R.drawable.bg_gradient_gray;
                break;
            case EXPOSED:
                statusBarColor = R.color.status_bar_red;
                backgroundDrawable = R.drawable.bg_gradient_red;
                break;
        }
        headerViewBackground.setBackgroundResource(backgroundDrawable);
        UiUtils.setStatusBarColor(getActivity(), statusBarColor);
    }

    private void setupCards(View view) {
        view.findViewById(R.id.card_contacts).setOnClickListener(
                v -> {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment_container, ContactsFragment.newInstance())
                            .addToBackStack(ContactsFragment.class.getCanonicalName())
                            .commit();
                    UiUtils.setStatusBarColor(getActivity(), R.color.white);
                });
        View contactStatusView = view.findViewById(R.id.contacts_status);
        tracingViewModel.getTracingEnabledLiveData().observe(getViewLifecycleOwner(),
                isTracing -> {
                    List<TracingStatus.ErrorState> errors = tracingViewModel.getErrorsLiveData().getValue();
                    TracingStatusHelper.State state = errors.size() > 0 || !isTracing ? TracingStatusHelper.State.WARNING :
                            TracingStatusHelper.State.OK;
                    int titleRes = state == TracingStatusHelper.State.OK ? R.string.tracing_active_title
                            : R.string.tracing_error_title;
                    int textRes = state == TracingStatusHelper.State.OK ? R.string.tracing_active_text
                            : R.string.tracing_error_text;
                    TracingStatusHelper.updateStatusView(contactStatusView, state, titleRes, textRes);
                });

        view.findViewById(R.id.card_notifications).setOnClickListener(
                v -> {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment_container, NotificationsFragment.newInstance())
                            .addToBackStack(NotificationsFragment.class.getCanonicalName())
                            .commit();
                    UiUtils.setStatusBarColor(getActivity(), R.color.white);
                });
        View notificationStatusBubble = view.findViewById(R.id.notifications_status_bubble);
        View notificationStatusView = notificationStatusBubble.findViewById(R.id.notification_status);

        View buttonInform = view.findViewById(R.id.main_button_inform);
        buttonInform.setOnClickListener(
                v -> {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment_container, TriggerFragment.newInstance())
                            .addToBackStack(TriggerFragment.class.getCanonicalName())
                            .commit();
                    UiUtils.setStatusBarColor(getActivity(), R.color.white);
                });

        tracingViewModel.getSelfOrContactExposedLiveData().observe(getViewLifecycleOwner(),
                selfOrContactExposed -> {
                    boolean isExposed = selfOrContactExposed.first || selfOrContactExposed.second;
                    buttonInform.setVisibility(!selfOrContactExposed.first ? View.VISIBLE : View.GONE);
                    TracingStatusHelper.State state =
                            !(isExposed) ? TracingStatusHelper.State.OK
                                    : TracingStatusHelper.State.INFO;
                    int title = isExposed ? (selfOrContactExposed.first ? R.string.meldungen_infected_title
                            : R.string.meldungen_meldung_title)
                            : R.string.meldungen_no_meldungen_title;
                    int text = isExposed ? (selfOrContactExposed.first ? R.string.meldungen_infected_text :
                            R.string.meldungen_meldung_text)
                            : R.string.meldungen_no_meldungen_text;

                    TracingStatusHelper.updateStatusView(notificationStatusView, state, title, text);
                });
    }

}
