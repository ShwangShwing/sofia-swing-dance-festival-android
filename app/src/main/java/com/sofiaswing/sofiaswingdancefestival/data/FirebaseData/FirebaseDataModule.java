package com.sofiaswing.sofiaswingdancefestival.data.FirebaseData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/9/17.
 */

@Module
public class FirebaseDataModule {
    public FirebaseDataModule() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);

        // Enable current year collection precaching
        final DataInterfaces.ICurrentSsdfYearData currentSsdfYearData = new CurrentSsdfYearData();
        currentSsdfYearData.getCurrentSsdfYear()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentSsdfYear -> {
                    DatabaseReference curentYearRef = FirebaseDatabase.getInstance().getReference(currentSsdfYear);
                    curentYearRef.keepSynced(true);
                });
    }

    @Provides
    DataInterfaces.ICurrentSsdfYearData provideCurrentSsdfYearData()
    {
        return new CurrentSsdfYearData();
    }

    @Provides
    DataInterfaces.INewsArticlesData provideNewsArticlesFirebaseData(
            ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
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

    @Provides
    DataInterfaces.IClassLevelsData provideClassLevelsFirebaseData(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        return new ClassLevelsFirebaseData(currentSsdfYearProvider);
    }

    @Provides
    DataInterfaces.IEventsData provideFirebaseEventsData(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        return new EventsFirebaseData(currentSsdfYearProvider);
    }

    @Provides
    DataInterfaces.IPartiesData provideFirebasePartiesData(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        return new PartiesFirebaseData(currentSsdfYearProvider);
    }

    @Provides
    DataInterfaces.ISsdfYearsData provideSsdfYearsData() {
        return new SsdfYearsFirebaseData();
    }
}
