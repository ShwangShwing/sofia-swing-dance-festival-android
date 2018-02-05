package com.sofiaswing.sofiaswingdancefestival.providers;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;

import io.reactivex.Observable;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class CurrentSsdfYearProvider implements ProvidersInterfaces.ICurrentSsdfYearProvider {
    private final DataInterfaces.ICurrentSsdfYearData currentSsdfYearData;

    private final String currentSsdfYearFromData;
    private String currentSsdfYearOverriden;
    private boolean overrideYear;

    public CurrentSsdfYearProvider(DataInterfaces.ICurrentSsdfYearData currentSsdfYearData) {
        this.currentSsdfYearData = currentSsdfYearData;

        this.currentSsdfYearFromData = "dev2017";
        this.currentSsdfYearOverriden = null;
        this.overrideYear = false;
    }

    @Override
    public Observable<String> getCurrentSsdfYear() {
//        if (this.overrideYear) {
//            return this.currentSsdfYearOverriden;
//        }
//        else {
//            return this.currentSsdfYearFromData;
//        }
        return this.currentSsdfYearData.getCurrentSsdfYear();
    }
}
