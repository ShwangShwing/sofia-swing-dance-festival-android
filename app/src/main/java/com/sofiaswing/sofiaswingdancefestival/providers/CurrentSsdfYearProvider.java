package com.sofiaswing.sofiaswingdancefestival.providers;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class CurrentSsdfYearProvider implements ProvidersInterfaces.ICurrentSsdfYearProvider {
    private final DataInterfaces.ICurrentSsdfYearData currentSsdfYearData;
    private final ProvidersInterfaces.ISettingsProvider settingsProvider;

    private final BehaviorSubject<String> yearSubject;
    private Disposable ssdfYearDataSubscr;
    private final Observer<Pair<Boolean, String>> ssdfYearObs;
    private final LiveData<Pair<Boolean, String>> ssdfYearLiveData;

    public CurrentSsdfYearProvider(DataInterfaces.ICurrentSsdfYearData currentSsdfYearData, ProvidersInterfaces.ISettingsProvider settingsProvider) {
        this.currentSsdfYearData = currentSsdfYearData;
        this.settingsProvider = settingsProvider;

        this.yearSubject = BehaviorSubject.create();

        this.ssdfYearObs = booleanStringPair -> {
            final boolean isYearFromDb = booleanStringPair.first;
            final String customSsdfYear = booleanStringPair.second;

            if (isYearFromDb) {
                if (ssdfYearDataSubscr == null) {
                    ssdfYearDataSubscr = currentSsdfYearData.getCurrentSsdfYear()
                            .subscribe(s -> yearSubject.onNext(s));
                }
            } else {
                if (ssdfYearDataSubscr != null) {
                    ssdfYearDataSubscr.dispose();
                    ssdfYearDataSubscr = null;
                }

                yearSubject.onNext(customSsdfYear);
            }
        };

        ssdfYearLiveData = this.settingsProvider.obsCurrentSsdfYear();
        this.ssdfYearLiveData.observeForever(this.ssdfYearObs);
    }

    @Override
    public void finalize() {
        if (ssdfYearDataSubscr != null) {
            ssdfYearDataSubscr.dispose();
            ssdfYearDataSubscr = null;
        }
        this.ssdfYearLiveData.removeObserver(this.ssdfYearObs);
    }

    @Override
    public synchronized Observable<String> getCurrentSsdfYear() {
        return this.yearSubject;
    }
}
