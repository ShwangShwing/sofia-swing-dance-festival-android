package com.sofiaswing.sofiaswingdancefestival.data.FirebaseData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.ClassModel;
import com.sofiaswing.sofiaswingdancefestival.models.EventModel;
import com.sofiaswing.sofiaswingdancefestival.models.PartyModel;
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
    public Observable<List<PartyModel>> getParties() {
        Observable<List<PartyModel>> observable = Observable.create(new ObservableOnSubscribe<List<PartyModel>>() {
            private DatabaseReference activePartiesDbRef = null;
            private ValueEventListener activeValueEventListener = null;


            @Override
            public void subscribe(@NonNull final ObservableEmitter<List<PartyModel>> e) throws Exception {
                ssdfYearFbDbRefProvider.getDatabaseReference("events")
                        .subscribeOn(Schedulers.io())
                        .subscribe(databaseReference -> {
                            if (activePartiesDbRef != null && activeValueEventListener != null)
                            {
                                activePartiesDbRef.removeEventListener(activeValueEventListener);
                            }

                            activePartiesDbRef = databaseReference;

                            activeValueEventListener = new ValueEventListener() {
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
                                        String venueId = "";

                                        try {
                                            int rootUrlLength = partySnapshot.getRef().getRoot().toString().length();
                                            id = partySnapshot.getRef().toString().substring(rootUrlLength + 1);
                                            startTime = new Date(Long.parseLong(partySnapshot.child("start").getValue().toString()) * 1000);
                                            endTime = new Date(Long.parseLong(partySnapshot.child("end").getValue().toString()) * 1000);
                                            name = partySnapshot.child("name").getValue().toString();

                                            venueId = partySnapshot.child("venueId").getValue().toString();
                                        }
                                        catch (Exception e1) {

                                        }

                                        PartyModel newParty = new PartyModel(
                                                id,
                                                startTime,
                                                endTime,
                                                name,
                                                venueId
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
                            };

                            activePartiesDbRef
                                    .orderByChild("type")
                                    .equalTo("party")
                                    .addValueEventListener(activeValueEventListener);
                        });
            }
        });

        return observable;
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

    @Override
    public Observable<List<EventModel>> getEvents(List<String> filterEventIds) {
        Observable<List<EventModel>> observable = Observable.create(new ObservableOnSubscribe<List<EventModel>>() {
            private DatabaseReference activeEventsDbRef = null;
            private ValueEventListener activeValueEventListener = null;


            @Override
            public void subscribe(@NonNull final ObservableEmitter<List<EventModel>> e) throws Exception {
                ssdfYearFbDbRefProvider.getDatabaseReference("events")
                        .subscribeOn(Schedulers.io())
                        .subscribe(databaseReference -> {
                            if (activeEventsDbRef != null && activeValueEventListener != null)
                            {
                                activeEventsDbRef.removeEventListener(activeValueEventListener);
                            }

                            activeEventsDbRef = databaseReference;

                            activeValueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();

                                    List<EventModel> events = new ArrayList<EventModel>();

                                    while (i.hasNext()) {
                                        DataSnapshot eventSnapshot = i.next();

                                        String id = "";
                                        Date startTime = null;
                                        Date endTime = null;
                                        String name = "";
                                        String venueId = "";
                                        String eventType = "";

                                        try {
                                            int rootUrlLength = eventSnapshot.getRef().getRoot().toString().length();
                                            id = eventSnapshot.getRef().toString().substring(rootUrlLength + 1);

                                            if (filterEventIds == null || filterEventIds.contains(id)) {
                                                startTime = new Date(Long.parseLong(eventSnapshot.child("start").getValue().toString()) * 1000);
                                                endTime = new Date(Long.parseLong(eventSnapshot.child("end").getValue().toString()) * 1000);
                                                name = eventSnapshot.child("name").getValue().toString();
                                                eventType = eventSnapshot.child("type").getValue().toString();

                                                venueId = eventSnapshot.child("venueId").getValue().toString();

                                                EventModel newEvent = new EventModel(
                                                        id,
                                                        startTime,
                                                        endTime,
                                                        name,
                                                        venueId,
                                                        eventType
                                                );

                                                events.add(newEvent);
                                            }
                                        }
                                        catch (Exception e1) {

                                        }
                                    }

                                    Collections.sort(events, (o1, o2) -> {
                                        if (o1.getStartTime() == null) {
                                            return -1;
                                        }
                                        else if (o2.getStartTime() == null) {
                                            return 1;
                                        }

                                        return (int)(o1.getStartTime().getTime() - o2.getStartTime().getTime());
                                    });

                                    e.onNext(events);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };

                            activeEventsDbRef
                                    .orderByChild("type")
                                    .addValueEventListener(activeValueEventListener);
                        });
            }
        });

        return observable;
    }

    private Observable<List<ClassModel>> getClass(final String typeFullString, final String level) {
        Observable<List<ClassModel>> observable = Observable.create(new ObservableOnSubscribe<List<ClassModel>>() {
            private DatabaseReference activeClassesDbRef = null;
            private ValueEventListener activeValueEventListener = null;

            @Override
            public void subscribe(final ObservableEmitter<List<ClassModel>> e) throws Exception {
                ssdfYearFbDbRefProvider.getDatabaseReference("events")
                        .subscribeOn(Schedulers.io())
                        .subscribe(databaseReference -> {
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
                                        String name = "";
                                        String venueId = "";
                                        List<String> instructorIds = new ArrayList();

                                        try {
                                            int rootUrlLength = classSnapshot.getRef().getRoot().toString().length();
                                            id = classSnapshot.getRef().toString().substring(rootUrlLength + 1);
                                            startTime = new Date(Long.parseLong(classSnapshot.child("start").getValue().toString()) * 1000);
                                            endTime = new Date(Long.parseLong(classSnapshot.child("end").getValue().toString()) * 1000);
                                            name = classSnapshot.child("name").getValue().toString();
                                            if (classSnapshot.child("venueId").exists())
                                            {
                                                venueId = classSnapshot.child("venueId").getValue().toString();
                                            }

                                            DataSnapshot instructorsSnapshot = classSnapshot.child("instructorIds");
                                            Iterator<DataSnapshot> instructorsIterator = instructorsSnapshot.getChildren().iterator();
                                            while(instructorsIterator.hasNext()) {
                                                DataSnapshot instructorSnapshot = instructorsIterator.next();
                                                instructorIds.add(instructorSnapshot.getValue().toString());
                                            }
                                        }
                                        catch (Exception e1) {

                                        }

                                        ClassModel newClass = new ClassModel(
                                                id,
                                                startTime,
                                                endTime,
                                                level,
                                                name,
                                                venueId,
                                                instructorIds
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
                        });
            }
        });

        return observable;
    }

}
