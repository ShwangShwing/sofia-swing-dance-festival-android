package com.sofiaswing.sofiaswingdancefestival.data.FirebaseData;

import android.location.Location;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class VenuesFirebaseData implements DataInterfaces.IVenuesData {
    private final ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider;

    public VenuesFirebaseData(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        this.currentSsdfYearProvider = currentSsdfYearProvider;
    }

    @Override
    public Observable<List<VenueModel>> getAll() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        String collectionPath = String.format("%s/venues", this.currentSsdfYearProvider.getCurrentSsdfYear());
        final DatabaseReference venuesRef = database.getReference(collectionPath);

        Observable<List<VenueModel>> observable = Observable.create(new ObservableOnSubscribe<List<VenueModel>>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<List<VenueModel>> e) throws Exception {
                venuesRef.orderByKey().addChildEventListener(new ChildEventListener() {
                    private List<VenueModel> venues = new ArrayList<VenueModel>();

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String venueKey = dataSnapshot.getKey();
                        DataSnapshot venueNameSnapshot = dataSnapshot.child("name");
                        DataSnapshot addressSnapshot = dataSnapshot.child("address");
                        DataSnapshot latitudeSnapshot = dataSnapshot.child("latitude");
                        DataSnapshot longitudeSnapshot = dataSnapshot.child("longitude");

                        Location location = null;
                        try {
                            double latitude = Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                            double longitude = Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());

                            location = new Location(String.format("%f, %f", latitude, longitude));
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                        } catch (NumberFormatException ex) {
                            location = null;
                        } catch (NullPointerException ex) {
                            location = null;
                        }

                        VenueModel venue = new VenueModel(
                                venueKey,
                                venueNameSnapshot.exists() ? venueNameSnapshot.getValue().toString()
                                        : "No name in the database!",
                                addressSnapshot.exists() ? addressSnapshot.getValue().toString()
                                        : "",
                                location
                        );

                        venues.add(venue);

                        e.onNext(venues);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        return observable;
    }
}
