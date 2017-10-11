package com.sofiaswing.sofiaswingdancefestival.data.FirebaseData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.ClassLevelModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class ClassLevelsFirebaseData implements DataInterfaces.IClassLevelsData {
    private final ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider;


    public ClassLevelsFirebaseData(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        this.currentSsdfYearProvider = currentSsdfYearProvider;
    }

    @Override
    public Observable<List<ClassLevelModel>> getAll() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        String collectionPath = String.format("%s/classLevels", this.currentSsdfYearProvider.getCurrentSsdfYear());
        final DatabaseReference classLevelsRef = database.getReference(collectionPath);

        Observable<List<ClassLevelModel>> observable = Observable.create(new ObservableOnSubscribe<List<ClassLevelModel>>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<List<ClassLevelModel>> e) throws Exception {
                classLevelsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<ClassLevelModel> classLevels = new ArrayList<ClassLevelModel>();

                        Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                        while (i.hasNext()) {
                            DataSnapshot classLevelSnapshot = i.next();

                            String id = classLevelSnapshot.getKey();
                            int position = 0;
                            try {
                                position = Integer.parseInt(classLevelSnapshot.child("position").getValue().toString());
                            }
                            catch (Exception e) {

                            }

                            String name = "";
                            try {
                                name = classLevelSnapshot.child("name").getValue().toString();
                            }
                            catch (Exception e) {

                            }

                            classLevels.add(new ClassLevelModel(id, name, position));
                        }

                        Collections.sort(classLevels);

                        e.onNext(classLevels);
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
