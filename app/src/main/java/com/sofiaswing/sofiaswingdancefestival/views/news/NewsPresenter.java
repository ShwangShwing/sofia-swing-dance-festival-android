package com.sofiaswing.sofiaswingdancefestival.views.news;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.NewsArticleModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class NewsPresenter implements NewsInterfaces.IPresenter {
    public final NewsInterfaces.IView view;
    private final DataInterfaces.INewsArticlesData newsArticlesData;
    private List<NewsArticleModel> newsArticles;

    public NewsPresenter(NewsInterfaces.IView view,
                         ProvidersInterfaces.IImageProvider imageProvider,
                         DataInterfaces.INewsArticlesData newsArticlesData) {
        this.view = view;
        this.newsArticlesData = newsArticlesData;
        view.setPresenter(this);
        view.setImageProvider(imageProvider);
        this.newsArticles = new ArrayList<>();
    }

    @Override
    public NewsInterfaces.IView getView() {
        return this.view;
    }

    @Override
    public void start() {
        this.newsArticlesData.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<List<NewsArticleModel>, List<NewsArticleViewModel>>() {
                    @Override
                    public List<NewsArticleViewModel> apply(@NonNull List<NewsArticleModel> newsArticles) throws Exception {
                        newsArticles = new ArrayList<NewsArticleModel>(newsArticles);
                        List<NewsArticleViewModel> newsArticlesForView = new ArrayList<NewsArticleViewModel>();
                        for (NewsArticleModel newsArticle : newsArticles) {
                            NewsArticleViewModel newsArticleForView =
                                    new NewsArticleViewModel(
                                            newsArticle.getPostedOn(),
                                            newsArticle.getImageUrl(),
                                            newsArticle.getText());
                            newsArticlesForView.add(newsArticleForView);
                        }

                        Collections.reverse(newsArticlesForView);
                        return newsArticlesForView;
                    }
                })
                .subscribe(new Consumer<List<NewsArticleViewModel>>() {
                    @Override
                    public void accept(List<NewsArticleViewModel> newsArticleViewModels) throws Exception {
                        getView().setNews(newsArticleViewModels);
                    }
                });
    }

    @Override
    public void selectNewsArticle(int index) {

    }
}
