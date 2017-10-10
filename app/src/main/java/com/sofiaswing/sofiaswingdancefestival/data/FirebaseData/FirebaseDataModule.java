package com.sofiaswing.sofiaswingdancefestival.data.FirebaseData;

import com.google.firebase.database.FirebaseDatabase;
import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/9/17.
 */

@Module
public class FirebaseDataModule {
    public FirebaseDataModule() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
    }

    @Provides
    DataInterfaces.INewsArticlesData provideNewsArticlesFirebaseData(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        return new NewsArticlesFirebaseData(currentSsdfYearProvider);
    }

    @Provides
    DataInterfaces.IInstructorsData provideInstructorsFirebaseData(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        return new InstructorsFirebaseData(currentSsdfYearProvider);
    }

    @Provides
    DataInterfaces.IVenuesData provideVenuesFirebaseData(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider)
    {
        return new VenuesFirebaseData(currentSsdfYearProvider);
    }
}
