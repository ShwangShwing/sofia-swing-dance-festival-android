package com.sofiaswing.sofiaswingdancefestival.providers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class CurrentSsdfYearProvider implements ProvidersInterfaces.ICurrentSsdfYearProvider {
    private final String currentSsdfYearFromData;
    private String currentSsdfYearOverriden;
    private boolean overrideYear;

    public CurrentSsdfYearProvider() {
        this.currentSsdfYearFromData = "dev2017";
        this.currentSsdfYearOverriden = null;
        this.overrideYear = false;
    }

    @Override
    public String getCurrentSsdfYear() {
        if (this.overrideYear) {
            return this.currentSsdfYearOverriden;
        }
        else {
            return this.currentSsdfYearFromData;
        }
    }
}
