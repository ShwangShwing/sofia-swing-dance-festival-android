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

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class NewsArticlesFirebaseData implements DataInterfaces.INewsArticlesData {
    private ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider;
    private List<NewsArticleModel> newsArticles;

    @Inject
    public NewsArticlesFirebaseData(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        this.currentSsdfYearProvider = currentSsdfYearProvider;
        this.newsArticles = new ArrayList<>();
    }

    @Override
    public Observable<List<NewsArticleModel>> getAll() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        String collectionPath = String.format("%s/newsArticles", this.currentSsdfYearProvider.getCurrentSsdfYear());
        final DatabaseReference newsArticlesRef = database.getReference(collectionPath);

        Observable<List<NewsArticleModel>> observable = Observable.create(new ObservableOnSubscribe<List<NewsArticleModel>>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<List<NewsArticleModel>> e) throws Exception {
                newsArticlesRef.orderByKey().addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
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
                });
            }
        });

        return observable;
    }

    @Override
    public Observable<NewsArticleModel> getById(String id) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        String collectionPath = String.format("%s/newsArticles/%s",
                this.currentSsdfYearProvider.getCurrentSsdfYear(),
                id);
        final DatabaseReference newsArticlesRef = database.getReference(collectionPath);

        Observable<NewsArticleModel> observable = Observable.create(new ObservableOnSubscribe<NewsArticleModel>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<NewsArticleModel> e) throws Exception {
                newsArticlesRef.addValueEventListener(new ValueEventListener() {
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
                });
            }
        });

        return observable;
    }
}
