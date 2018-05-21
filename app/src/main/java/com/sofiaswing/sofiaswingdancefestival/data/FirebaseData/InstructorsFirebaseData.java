package com.sofiaswing.sofiaswingdancefestival.data.FirebaseData;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class InstructorsFirebaseData implements DataInterfaces.IInstructorsData {
    final private ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider;
    final private CurrentSsdfYearFirebaseDatabaseReferenceProvider ssdfYearFbDbRefProvider;

    public InstructorsFirebaseData(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        this.currentSsdfYearProvider = currentSsdfYearProvider;
        this.ssdfYearFbDbRefProvider = new CurrentSsdfYearFirebaseDatabaseReferenceProvider(this.currentSsdfYearProvider);
    }

    @Override
    public Observable<List<InstructorModel>> getAll() {
        Observable<List<InstructorModel>> observable = Observable.create(new ObservableOnSubscribe<List<InstructorModel>>() {
            private DatabaseReference activeInstructorsDbRef = null;
            private ChildEventListener activeChildEventListener = null;

            @Override
            public void subscribe(final ObservableEmitter<List<InstructorModel>> e) throws Exception {
                ssdfYearFbDbRefProvider.getDatabaseReference("instructors")
                        .subscribeOn(Schedulers.io())
                        .subscribe(databaseReference -> {
                            if (activeInstructorsDbRef != null && activeChildEventListener != null) {
                                activeInstructorsDbRef.removeEventListener(activeChildEventListener);
                            }

                            activeInstructorsDbRef = databaseReference;
                            activeChildEventListener = new ChildEventListener() {
                                final List<InstructorModel> instructors = new ArrayList<InstructorModel>();

                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    String instructorPath = FirebaseHelpers.getNodePathFromSnapshot(dataSnapshot);
                                    DataSnapshot nameSnapshot = dataSnapshot.child("name");
                                    DataSnapshot imageUrlSnapshot = dataSnapshot.child("imageUrl");
                                    DataSnapshot typeSnapshot = dataSnapshot.child("type");
                                    DataSnapshot descriptionSnapshot = dataSnapshot.child("description");

                                    InstructorModel instructor = new InstructorModel(
                                            instructorPath,
                                            nameSnapshot.exists() ? nameSnapshot.getValue().toString()
                                                    : "No name! Problem with the database!",
                                            imageUrlSnapshot.exists() ? imageUrlSnapshot.getValue().toString()
                                                    : "No image url! Problem with the database!",
                                            typeSnapshot.exists() ? typeSnapshot.getValue().toString()
                                                    : "No instructor type! Problem with the database!",
                                            descriptionSnapshot.exists() ? descriptionSnapshot.getValue().toString()
                                                    : "No description! Problem with the database!"
                                    );

                                    instructors.add(instructor);
                                    e.onNext(instructors);
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

                            activeInstructorsDbRef.orderByChild("position").addChildEventListener(activeChildEventListener);
                        });
            }
        });

        return observable;
    }

    @Override
    public Observable<InstructorModel> getById(String id) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference instructorsRef = database.getReference(id);

        Observable<InstructorModel> observable = Observable.create(new ObservableOnSubscribe<InstructorModel>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<InstructorModel> e) throws Exception {
                instructorsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String articleKey = dataSnapshot.getKey();
                        DataSnapshot nameSnapshot = dataSnapshot.child("name");
                        DataSnapshot imageUrlSnapshot = dataSnapshot.child("imageUrl");
                        DataSnapshot typeSnapshot = dataSnapshot.child("type");
                        DataSnapshot descriptionSnapshot = dataSnapshot.child("description");

                        InstructorModel instructor = new InstructorModel(
                                articleKey,
                                nameSnapshot.exists() ? nameSnapshot.getValue().toString()
                                        : "No name! Problem with the database!",
                                imageUrlSnapshot.exists() ? imageUrlSnapshot.getValue().toString()
                                        : "No image url! Problem with the database!",
                                typeSnapshot.exists() ? typeSnapshot.getValue().toString()
                                        : "No instructor type! Problem with the database!",
                                descriptionSnapshot.exists() ? descriptionSnapshot.getValue().toString()
                                        : "No description! Problem with the database!"
                        );

                        e.onNext(instructor);
                        e.onComplete();
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
