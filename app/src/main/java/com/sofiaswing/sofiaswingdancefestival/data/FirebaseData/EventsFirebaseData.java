package com.sofiaswing.sofiaswingdancefestival.data.FirebaseData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.ClassModel;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
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
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/11/17.
 */

public class EventsFirebaseData implements DataInterfaces.IEventsData {
    private final ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider;
    final private CurrentSsdfYearFirebaseDatabaseReferenceProvider ssdfYearFbDbRefProvider;

    public EventsFirebaseData(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        this.currentSsdfYearProvider = currentSsdfYearProvider;
        this.ssdfYearFbDbRefProvider = new CurrentSsdfYearFirebaseDatabaseReferenceProvider(this.currentSsdfYearProvider);
    }

    @Override
    public Observable<List<ClassModel>> getClassesByLevel(final String level) {
        String eventTypeFullString = String.format("class_%s", level);

        return this.getClass(eventTypeFullString, level);
    }

    @Override
    public Observable<List<ClassModel>> getTasterClasses() {
        return this.getClass("taster_class", "");
    }

    private Observable<List<ClassModel>> getClass(final String typeFullString, final String level) {
        Observable<List<ClassModel>> observable = Observable.create(new ObservableOnSubscribe<List<ClassModel>>() {
            private DatabaseReference activeClassesDbRef = null;
            private ValueEventListener activeValueEventListener = null;

            @Override
            public void subscribe(final ObservableEmitter<List<ClassModel>> e) throws Exception {
                ssdfYearFbDbRefProvider.getDatabaseReference("events")
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<DatabaseReference>() {
                            @Override
                            public void accept(DatabaseReference databaseReference) throws Exception {
                                if (activeClassesDbRef != null && activeValueEventListener != null)
                                {
                                    activeClassesDbRef.removeEventListener(activeValueEventListener);
                                }

                                activeClassesDbRef = databaseReference;
                                activeValueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();

                                        List<ClassModel> classes = new ArrayList<ClassModel>();

                                        while (i.hasNext()) {
                                            DataSnapshot classSnapshot = i.next();

                                            String id = "";
                                            Date startTime = null;
                                            Date endTime = null;
                                            String levelName = "";
                                            String name = "";
                                            VenueModel venue = null;
                                            List<InstructorModel> instructors = new ArrayList();

                                            try {
                                                int rootUrlLength = dataSnapshot.getRef().getRoot().toString().length();
                                                id = dataSnapshot.getRef().toString().substring(rootUrlLength + 1);
                                                startTime = new Date(Long.parseLong(classSnapshot.child("start").getValue().toString()) * 1000);
                                                endTime = new Date(Long.parseLong(classSnapshot.child("end").getValue().toString()) * 1000);
                                                levelName =
                                                        classSnapshot.child("levelName").exists() ?
                                                                classSnapshot.child("levelName").getValue().toString()
                                                                : "";
                                                name = classSnapshot.child("name").getValue().toString();

                                                DataSnapshot venueSnapshot = classSnapshot.child("venue").getChildren().iterator().next();
                                                venue = new VenueModel(venueSnapshot.getKey(),
                                                        venueSnapshot.child("name").getValue().toString(),
                                                        venueSnapshot.child("address").getValue().toString(),
                                                        null);
                                                DataSnapshot instructorsSnapshot = classSnapshot.child("instructors");
                                                Iterator<DataSnapshot> instructorsIterator = instructorsSnapshot.getChildren().iterator();
                                                while(instructorsIterator.hasNext()) {
                                                    DataSnapshot instructorSnapshot = instructorsIterator.next();

                                                    instructors.add(new InstructorModel(
                                                            instructorSnapshot.getKey(),
                                                            instructorSnapshot.child("name").getValue().toString(),
                                                            instructorSnapshot.child("imageUrl").getValue().toString()
                                                    ));
                                                }
                                            }
                                            catch (Exception e) {

                                            }

                                            ClassModel newClass = new ClassModel(
                                                    id,
                                                    startTime,
                                                    endTime,
                                                    level,
                                                    levelName,
                                                    name,
                                                    venue,
                                                    instructors
                                            );

                                            classes.add(newClass);
                                        }

                                        Collections.sort(classes, new Comparator<ClassModel>() {
                                            @Override
                                            public int compare(ClassModel o1, ClassModel o2) {
                                                if (o1.getStartTime() == null) {
                                                    return -1;
                                                }
                                                else if (o2.getStartTime() == null) {
                                                    return 1;
                                                }

                                                return (int)(o1.getStartTime().getTime() - o2.getStartTime().getTime());
                                            }
                                        });

                                        e.onNext(classes);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                };

                                activeClassesDbRef
                                        .orderByChild("type")
                                        .equalTo(typeFullString)
                                        .addValueEventListener(activeValueEventListener);
                            }
                        });
            }
        });

        return observable;
    }

}