package com.sofiaswing.sofiaswingdancefestival.data;

import com.sofiaswing.sofiaswingdancefestival.models.NewsArticleModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class DataInterfaces {
    public interface INewsArticlesData {
        Observable<List<NewsArticleModel>> getAll();
        Observable<NewsArticleModel> getById(String id);
    }
}
