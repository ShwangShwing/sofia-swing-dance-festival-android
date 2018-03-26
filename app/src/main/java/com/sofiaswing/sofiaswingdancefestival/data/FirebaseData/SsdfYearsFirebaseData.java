package com.sofiaswing.sofiaswingdancefestival.data.FirebaseData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by shwangshwing on 2/25/18.
 */

public class SsdfYearsFirebaseData implements DataInterfaces.ISsdfYearsData {
    @Override
    public Observable<List<String>>getSsdfYears() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference ssdfYears = database.getReference();

        final Query ssdfYearsQuery = ssdfYears
                .orderByKey();

        Observable<List<String>> ssdfYearsObservable = Observable.create(e -> ssdfYearsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> years = new ArrayList<String>();
                Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()) {
                    DataSnapshot yearSnapshot = i.next();
                    String recordKey = yearSnapshot.getKey();
                    if (recordKey.equals("activeSsdfYear")) {
                        continue;
                    }

                    years.add(recordKey);
                }

                e.onNext(years);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }));

        return ssdfYearsObservable;
    }
}
