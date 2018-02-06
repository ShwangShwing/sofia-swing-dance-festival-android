package com.sofiaswing.sofiaswingdancefestival.views.settings;

import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;
import com.sofiaswing.sofiaswingdancefestival.ui.UiInterfaces;

/**
 * Created by shwangshwing on 10/12/17.
 */

public class SettingsPresenter implements SettingsInterfaces.IPresenter {
    private final SettingsInterfaces.IView view;
    private final ProvidersInterfaces.ISettingsProvider settingsProvider;
    private final ProvidersInterfaces.IVolatileSettingsProvider volatileSettingsProvider;

    public SettingsPresenter(SettingsInterfaces.IView view,
                             ProvidersInterfaces.ISettingsProvider settingsProvider,
                             ProvidersInterfaces.IVolatileSettingsProvider volatileSettingsProvider,
                             UiInterfaces.IPopupCreator popupCreator
    ) {
        this.view = view;
        this.settingsProvider = settingsProvider;
        this.volatileSettingsProvider = volatileSettingsProvider;
        this.view.setPresenter(this);
        this.view.setPopupCreator(popupCreator);
    }

    @Override
    public SettingsInterfaces.IView getView() {
        return this.view;
    }

    @Override
    public void start() {
        this.view.setEventNotificationTimeSelection(settingsProvider.getEventsNotificationAdvanceTimeSeconds());
        this.updateHackerPanelAndIndicator();
        this.updateCustomYearFields();
    }

    @Override
    public void setEventsNotificationAdvanceTimeSeconds(long seconds) {
        settingsProvider.setEventsNotificationAdvanceTimeSeconds(seconds);
        settingsProvider.setupAllNotificationAlarms(); // to reacalculate the alarm times
    }

    @Override
    public void enableHackerMode() {
        this.volatileSettingsProvider.enableHackerMode();
        this.updateHackerPanelAndIndicator();
        this.view.notifyHackerModeEnabled();
    }

    @Override
    public void setYearFromDatabase() {
        this.volatileSettingsProvider.setCurrentSsdfYearFromData();
        this.updateCustomYearFields();
    }

    @Override
    public void setCustomYear(String customYear) {
        this.volatileSettingsProvider.setCurrentSsdfYear(customYear);
        this.updateCustomYearFields();
    }

    private void updateHackerPanelAndIndicator() {
        if (this.volatileSettingsProvider.isHackerModeEnabled()) {
            this.view.showHackerModeEnabledIndicator();
            this.view.showHackerPanel();
        }
        else {
            this.view.hideHackerModeEnabledIndicator();
            this.view.hideHackerModeEnabledIndicator();
        }
    }

    private void updateCustomYearFields() {
        if (this.volatileSettingsProvider.isYearFromDatabase()) {
            this.view.setYearFromDatabase(true);
            this.view.setCustomYear("");
        }
        else {
            this.view.setYearFromDatabase(false);
            this.view.setCustomYear(this.volatileSettingsProvider.getCurrentCustomSsdfYear());
        }
    }
}