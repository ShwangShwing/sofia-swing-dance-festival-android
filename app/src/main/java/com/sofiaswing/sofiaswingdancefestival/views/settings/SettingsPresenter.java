package com.sofiaswing.sofiaswingdancefestival.views.settings;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/12/17.
 */

public class SettingsPresenter implements SettingsInterfaces.IPresenter {
    private SettingsInterfaces.IView view;
    private final ProvidersInterfaces.ISettingsProvider settingsProvider;
    private final ProvidersInterfaces.IVolatileSettingsProvider volatileSettingsProvider;
    private final DataInterfaces.ISsdfYearsData ssdfYearsData;
    private final ProvidersInterfaces.ICurrentTimeProvider currentTimeProvider;
    private Disposable ssdfYearsObs;

    public SettingsPresenter(ProvidersInterfaces.ISettingsProvider settingsProvider,
                             ProvidersInterfaces.IVolatileSettingsProvider volatileSettingsProvider,
                             DataInterfaces.ISsdfYearsData ssdfYearsData, ProvidersInterfaces.ICurrentTimeProvider currentTimeProvider) {
        this.settingsProvider = settingsProvider;
        this.volatileSettingsProvider = volatileSettingsProvider;
        this.ssdfYearsData = ssdfYearsData;
        this.currentTimeProvider = currentTimeProvider;
    }

    @Override
    public void setView(SettingsInterfaces.IView view) {
        this.view = view;
    }

    @Override
    public void start() {
        this.view.setEventNotificationTimeSelection(settingsProvider.getEventsNotificationAdvanceTimeSeconds());
        this.updateHackerPanelAndIndicator();
        this.updateCustomYearFields();
        this.updateOverrideTimeSettings();
    }

    @Override
    public void stop() {
        if (this.ssdfYearsObs != null) {
            ssdfYearsObs.dispose();
        }
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

    @Override
    public void createTestNotification(String id, String name, long startTime, long notifyTime) {
        if (startTime == 0) startTime = notifyTime;
        if (notifyTime == 0) notifyTime = startTime;

        this.settingsProvider.subscribeForEvent(
                id,
                name,
                startTime,
                notifyTime);
    }

    @Override
    public void notifyCurrentTime() {
        long currentTime = this.currentTimeProvider.getCurrentTimeMs();
        this.view.notifyCurrentTimeMs(currentTime);
    }

    @Override
    public void setTimeOverride(boolean override, boolean freezeOverridenTime, long overridenTime) {
        if (override) {
            this.volatileSettingsProvider.setOverrideCurrentTime(override, freezeOverridenTime, overridenTime);
        }
        else {
            this.volatileSettingsProvider.setOverrideCurrentTime(false, false, 0);
        }

        this.updateOverrideTimeSettings();
        this.notifyCurrentTime();
    }

    private void updateHackerPanelAndIndicator() {
        if (this.volatileSettingsProvider.isHackerModeEnabled()) {
            if (this.ssdfYearsObs == null) {
                this.ssdfYearsObs = this.ssdfYearsData.getSsdfYears()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(years -> this.view.setSsdfYears(years));
            }

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

    private void updateOverrideTimeSettings() {
        if (this.volatileSettingsProvider.isCurrentTimeOverriden()) {
            boolean isTimeFrozen = this.volatileSettingsProvider.isCurrentOverridenTimeFrozen();
            this.view.setNotifyOverrideTimeState(true, isTimeFrozen);
        }
        else {
            this.view.setNotifyOverrideTimeState(false, false);
        }
    }
}
