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

/**
 * Created by shwangshwing on 10/9/17.
 */

public class CurrentSsdfYearProvider implements ProvidersInterfaces.ICurrentSsdfYearProvider {
    private final DataInterfaces.ICurrentSsdfYearData currentSsdfYearData;

    private String currentSsdfYearOverriden;
    private boolean overrideYear;
    private String lastEmittedSsdfYear;

    Observable<String> currentSsdfYearObservable;

    private final List<ObservableEmitter<String>> emitters;
    Disposable ssdfYearDataSubscr;

    public CurrentSsdfYearProvider(DataInterfaces.ICurrentSsdfYearData currentSsdfYearData) {
        this.currentSsdfYearData = currentSsdfYearData;

        this.currentSsdfYearOverriden = null;
        this.overrideYear = false;

        emitters = new ArrayList<>();
    }

    @Override
    public synchronized Observable<String> getCurrentSsdfYear() {
        if (this.currentSsdfYearObservable == null) {
            this.currentSsdfYearObservable = Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> e) throws Exception {
                    List<ObservableEmitter<String>> disposedEmitters = new ArrayList<>();
                    for (ObservableEmitter<String> emitter : emitters) {
                        if (emitter.isDisposed()) {
                            disposedEmitters.add(emitter);
                        }
                    }

                    emitters.removeAll(disposedEmitters);

                    emitters.add(e);
                    if (lastEmittedSsdfYear == null) {
                        setupProperSsdfYear();
                    } else {
                        e.onNext(lastEmittedSsdfYear);
                    }
                }
            });
        }

        return this.currentSsdfYearObservable;
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

    private synchronized void setupProperSsdfYear()
    {
        if (!this.overrideYear) {
            if (ssdfYearDataSubscr == null) {
                ssdfYearDataSubscr = this.currentSsdfYearData.getCurrentSsdfYear()
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                for (ObservableEmitter<String> emitter : emitters) {
                                    emitter.onNext(s);
                                    lastEmittedSsdfYear = s;
                                }
                            }
                        });
            }
        } else {
            if (ssdfYearDataSubscr != null) {
                ssdfYearDataSubscr.dispose();
                ssdfYearDataSubscr = null;
            }

            for (ObservableEmitter<String> emitter : emitters) {
                emitter.onNext(this.currentSsdfYearOverriden);
                lastEmittedSsdfYear = this.currentSsdfYearOverriden;
            }
        }

    }
}
