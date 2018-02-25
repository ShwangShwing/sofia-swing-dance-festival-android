package com.sofiaswing.sofiaswingdancefestival.data.FirebaseData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.PartyModel;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by shwangshwing on 10/11/17.
 */

public class PartiesFirebaseData implements DataInterfaces.IPartiesData {
    private final ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider;

    public PartiesFirebaseData(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        this.currentSsdfYearProvider = currentSsdfYearProvider;
    }

    @Override
    public Observable<List<PartyModel>> getParties() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        String collectionPath = String.format("dev2017/events", this.currentSsdfYearProvider.getCurrentSsdfYear());
        final DatabaseReference partiesRef = database.getReference(collectionPath);
        final Query partiesQuery = partiesRef
                .orderByChild("type")
                .equalTo("party");

        Observable<List<PartyModel>> observable = Observable.create(new ObservableOnSubscribe<List<PartyModel>>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<List<PartyModel>> e) throws Exception {
                partiesQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();

                        List<PartyModel> parties = new ArrayList<PartyModel>();

                        while (i.hasNext()) {
                            DataSnapshot partySnapshot = i.next();

                            String id = "";
                            Date startTime = null;
                            Date endTime = null;
                            String name = "";
                            VenueModel venue = null;

                            try {
                                id = partySnapshot.getKey();
                                startTime = new Date(Long.parseLong(partySnapshot.child("start").getValue().toString()) * 1000);
                                endTime = new Date(Long.parseLong(partySnapshot.child("end").getValue().toString()) * 1000);
                                name = partySnapshot.child("name").getValue().toString();

                                DataSnapshot venueSnapshot = partySnapshot.child("venue").getChildren().iterator().next();
                                venue = new VenueModel(venueSnapshot.getKey(),
                                        venueSnapshot.child("name").getValue().toString(),
                                        venueSnapshot.child("address").getValue().toString(),
                                        null);
                            }
                            catch (Exception e) {

                            }

                            PartyModel newParty = new PartyModel(
                                    id,
                                    startTime,
                                    endTime,
                                    name,
                                    venue
                            );

                            parties.add(newParty);
                        }

                        Collections.sort(parties, (o1, o2) -> {
                            if (o1.getStartTime() == null) {
                                return -1;
                            }
                            else if (o2.getStartTime() == null) {
                                return 1;
                            }

                            return (int)(o1.getStartTime().getTime() - o2.getStartTime().getTime());
                        });

                        e.onNext(parties);
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
