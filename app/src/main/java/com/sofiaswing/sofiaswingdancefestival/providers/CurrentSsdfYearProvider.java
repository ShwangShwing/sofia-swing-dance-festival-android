package com.sofiaswing.sofiaswingdancefestival.providers;

import android.util.Log;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class CurrentSsdfYearProvider implements ProvidersInterfaces.ICurrentSsdfYearProvider {
    private final DataInterfaces.ICurrentSsdfYearData currentSsdfYearData;

    private String currentSsdfYearOverriden;
    private boolean overrideYear;

    private final BehaviorSubject<String> yearSubject;
    Disposable ssdfYearDataSubscr;

    public CurrentSsdfYearProvider(DataInterfaces.ICurrentSsdfYearData currentSsdfYearData) {
        this.currentSsdfYearData = currentSsdfYearData;

        this.currentSsdfYearOverriden = null;
        this.overrideYear = false;

        this.yearSubject = BehaviorSubject.create();
        this.setupProperSsdfYear();
    }

    @Override
    public synchronized Observable<String> getCurrentSsdfYear() {
        return this.yearSubject;
    }

    @Override
    public synchronized void setCurrentSsdfYearFromData() {
        if (this.overrideYear) {
            this.overrideYear = false;
        }

        this.setupProperSsdfYear();
    }

    @Override
    public synchronized void setCurrentSsdfYear(String currentSsdfYear) {
        this.overrideYear = true;
        this.currentSsdfYearOverriden = currentSsdfYear;

        this.setupProperSsdfYear();
    }

    private synchronized void setupProperSsdfYear() {
        if (!this.overrideYear) {
            if (ssdfYearDataSubscr == null) {
                ssdfYearDataSubscr = this.currentSsdfYearData.getCurrentSsdfYear()
                        .subscribe(s -> yearSubject.onNext(s));
            }
        } else {
            if (ssdfYearDataSubscr != null) {
                ssdfYearDataSubscr.dispose();
                ssdfYearDataSubscr = null;
            }

            yearSubject.onNext(this.currentSsdfYearOverriden);
        }

    }
}
