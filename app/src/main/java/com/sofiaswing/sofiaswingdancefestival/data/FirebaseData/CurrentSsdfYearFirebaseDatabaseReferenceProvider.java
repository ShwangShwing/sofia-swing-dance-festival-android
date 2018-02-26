package com.sofiaswing.sofiaswingdancefestival.data.FirebaseData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 2/4/18.
 */

public class CurrentSsdfYearFirebaseDatabaseReferenceProvider {
    private ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider;

    public CurrentSsdfYearFirebaseDatabaseReferenceProvider(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        this.currentSsdfYearProvider = currentSsdfYearProvider;
    }

    Observable<DatabaseReference> getDatabaseReference(final String collectionName) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        Observable<DatabaseReference> observable = this.currentSsdfYearProvider.getCurrentSsdfYear()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(currentSsdfYear -> {
                    String collectionPath = String.format("%s/%s", currentSsdfYear, collectionName);
                    DatabaseReference databaseRef = database.getReference(collectionPath);
                    return databaseRef;
                });
        return observable;
    }
}
