package com.sofiaswing.sofiaswingdancefestival.views.settings;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

/**
 * Created by shwangshwing on 10/12/17.
 */

public class SettingsPresenter implements SettingsInterfaces.IPresenter {
    private final SettingsInterfaces.IView view;
    private final ProvidersInterfaces.ISettingsProvider settingsProvider;

    public SettingsPresenter(SettingsInterfaces.IView view, ProvidersInterfaces.ISettingsProvider settingsProvider) {
        this.view = view;
        this.settingsProvider = settingsProvider;
        this.view.setPresenter(this);
    }

    @Override
    public SettingsInterfaces.IView getView() {
        return this.view;
    }

    @Override
    public void start() {
        this.view.setEventNotificationTimeSelection(settingsProvider.getEventsNotificationAdvanceTimeSeconds());
    }

    @Override
    public void setEventsNotificationAdvanceTimeSeconds(long seconds) {
        settingsProvider.setEventsNotificationAdvanceTimeSeconds(seconds);
        settingsProvider.setupAllNotificationAlarms(); // to reacalculate the alarm times
        Toast.makeText(((Fragment)view).getContext(), String.format("Setting to %d seconds", seconds), Toast.LENGTH_SHORT).show();
    }
}
