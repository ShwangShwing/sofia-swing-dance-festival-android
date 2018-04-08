package com.sofiaswing.sofiaswingdancefestival.data.FirebaseData;

import android.location.Location;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class VenuesFirebaseData implements DataInterfaces.IVenuesData {
    private final ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider;
    private final CurrentSsdfYearFirebaseDatabaseReferenceProvider ssdfYearFbDbRefProvider;

    public VenuesFirebaseData(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        this.currentSsdfYearProvider = currentSsdfYearProvider;
        this.ssdfYearFbDbRefProvider = new CurrentSsdfYearFirebaseDatabaseReferenceProvider(this.currentSsdfYearProvider);
    }

    @Override
    public Observable<List<VenueModel>> getAll() {
        Observable<List<VenueModel>> observable = Observable.create(new ObservableOnSubscribe<List<VenueModel>>() {
            private DatabaseReference activeVenuesDbRef = null;
            private ChildEventListener activeChildEventListener = null;

            @Override
            public void subscribe(final ObservableEmitter<List<VenueModel>> e) throws Exception {
                ssdfYearFbDbRefProvider.getDatabaseReference("venues")
                        .subscribeOn(Schedulers.io())
                        .subscribe(databaseReference -> {
                            if (activeVenuesDbRef != null && activeChildEventListener != null)
                            {
                                activeVenuesDbRef.removeEventListener(activeChildEventListener);
                            }

                            activeVenuesDbRef = databaseReference;
                            activeChildEventListener = new ChildEventListener() {
                                private List<VenueModel> venues = new ArrayList<VenueModel>();

                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    venues.add(getVenueModelFromDataSnapshot(dataSnapshot));

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
                            };

                            activeVenuesDbRef.orderByChild("position").addChildEventListener(activeChildEventListener);
                        });
            }
        });

        return observable;
    }

    @Override
    public Observable<VenueModel> getById(final String venueId) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference venueRef = database.getReference(venueId);

        Observable<VenueModel> observable = Observable.create(e -> venueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                VenueModel venue = getVenueModelFromDataSnapshot(dataSnapshot);
                e.onNext(venue);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }));

        return observable;
    }

    private VenueModel getVenueModelFromDataSnapshot(final DataSnapshot dataSnapshot) {
        String venueId = FirebaseHelpers.getNodePathFromSnapshot(dataSnapshot);

        DataSnapshot venueNameSnapshot = dataSnapshot.child("name");
        DataSnapshot addressSnapshot = dataSnapshot.child("address");
        DataSnapshot imageUrlSnapshot = dataSnapshot.child("imageUrl");
        DataSnapshot youTubeUrlSnapshot = dataSnapshot.child("youtubeUrl");
        DataSnapshot latitudeSnapshot = dataSnapshot.child("latitude");
        DataSnapshot longitudeSnapshot = dataSnapshot.child("longitude");

        Location location = null;
        try {
            double latitude = Double.parseDouble(latitudeSnapshot.getValue().toString());
            double longitude = Double.parseDouble(longitudeSnapshot.getValue().toString());

            location = new Location(String.format("%f, %f", latitude, longitude));
            location.setLatitude(latitude);
            location.setLongitude(longitude);
        } catch (NumberFormatException ex) {
            location = null;
        } catch (NullPointerException ex) {
            location = null;
        }

        VenueModel venue = new VenueModel(
                venueId,
                venueNameSnapshot.exists() ? venueNameSnapshot.getValue().toString()
                        : "No name in the database!",
                addressSnapshot.exists() ? addressSnapshot.getValue().toString()
                        : "",
                imageUrlSnapshot.exists() ? imageUrlSnapshot.getValue().toString()
                        : "https://sofiaswing.com/wp-content/uploads/2017/11/SSDF2017-logo-6.png",
                youTubeUrlSnapshot.exists() ? youTubeUrlSnapshot.getValue().toString()
                        : "",
                location
        );

        return venue;
    }
}
