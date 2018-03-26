package com.sofiaswing.sofiaswingdancefestival.data.FirebaseData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.NewsArticleModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class NewsArticlesFirebaseData implements DataInterfaces.INewsArticlesData {
    final private ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider;
    final private CurrentSsdfYearFirebaseDatabaseReferenceProvider ssdfYearFbDbRefProvider;

    @Inject
    public NewsArticlesFirebaseData(
            ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        this.currentSsdfYearProvider = currentSsdfYearProvider;
        this.ssdfYearFbDbRefProvider = new CurrentSsdfYearFirebaseDatabaseReferenceProvider(this.currentSsdfYearProvider);
    }

    @Override
    public Observable<List<NewsArticleModel>> getAll(final boolean includeUnpublished) {
        Observable<List<NewsArticleModel>> observable = Observable.create(new ObservableOnSubscribe<List<NewsArticleModel>>() {
            private DatabaseReference activeArticlesDbRef = null;
            private ChildEventListener activeChildEventListener = null;
            @Override
            public void subscribe(@NonNull final ObservableEmitter<List<NewsArticleModel>> e) throws Exception {
                ssdfYearFbDbRefProvider.getDatabaseReference("newsArticles")
                    .subscribeOn(Schedulers.io())
                    .subscribe(databaseReference -> {
                        if (activeArticlesDbRef != null && activeChildEventListener != null)
                        {
                            activeArticlesDbRef.removeEventListener(activeChildEventListener);
                        }

                        activeArticlesDbRef = databaseReference;
                        activeChildEventListener = new ChildEventListener() {
                            private List<NewsArticleModel> newsArticles = new ArrayList<NewsArticleModel>();

                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (!includeUnpublished) {
                                    if (!dataSnapshot.hasChild("isPublished")) {
                                        return;
                                    }

                                    if (!(Boolean.parseBoolean(dataSnapshot.child("isPublished").getValue().toString()))) {
                                        return;
                                    }
                                }

                                String articlePath = FirebaseHelpers.getNodePathFromSnapshot(dataSnapshot);

                                DataSnapshot postedOnSnapshot = dataSnapshot.child("postedOn");
                                DataSnapshot imageUrlSnapshot = dataSnapshot.child("imageUrl");
                                DataSnapshot textSnapshot = dataSnapshot.child("text");

                                NewsArticleModel article = new NewsArticleModel(
                                        articlePath,
                                        postedOnSnapshot.exists() ?
                                                new Date(Long.parseLong(postedOnSnapshot.getValue().toString()) * 1000)
                                                : null,
                                        imageUrlSnapshot.exists() ?
                                                imageUrlSnapshot.getValue().toString()
                                                : "",
                                        textSnapshot.exists() ?
                                                textSnapshot.getValue().toString()
                                                : "No text. Problem with the database!"
                                );

                                newsArticles.add(article);
                                e.onNext(newsArticles);
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

                        activeArticlesDbRef.orderByKey().addChildEventListener(activeChildEventListener);
                    });

            }
        });

        return observable;
    }

    @Override
    public Observable<NewsArticleModel> getById(String id) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference newsArticlesRef = database.getReference(id);

        Observable<NewsArticleModel> observable = Observable.create(e -> newsArticlesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String articleKey = dataSnapshot.getKey();
                DataSnapshot postedOnSnapshot = dataSnapshot.child("postedOn");
                DataSnapshot imageUrlSnapshot = dataSnapshot.child("imageUrl");
                DataSnapshot textSnapshot = dataSnapshot.child("text");

                NewsArticleModel article = new NewsArticleModel(
                        articleKey,
                        postedOnSnapshot.exists() ?
                                new Date(Long.parseLong(postedOnSnapshot.getValue().toString()) * 1000)
                                : null,
                        imageUrlSnapshot.exists() ?
                                imageUrlSnapshot.getValue().toString()
                                : "",
                        textSnapshot.exists() ?
                                textSnapshot.getValue().toString()
                                : "No text. Problem with the database!");
                e.onNext(article);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }));

        return observable;
    }
}
