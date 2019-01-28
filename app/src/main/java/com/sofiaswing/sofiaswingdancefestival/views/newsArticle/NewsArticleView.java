package com.sofiaswing.sofiaswingdancefestival.views.newsArticle;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.models.NewsArticleModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsArticleView extends Fragment
    implements NewsArticleInterfaces.IView {

    @Inject
    public ProvidersInterfaces.INetworkImageLoader netImageLoader;

    private NewsArticleInterfaces.IPresenter presenter;

    private CompositeDisposable subscriptions;

    public NewsArticleView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.subscriptions = new CompositeDisposable();
        this.inject();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_news_article_view, container, false);

        this.setRetainInstance(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        this.presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        this.presenter.stop();
        this.subscriptions.clear();
    }

    @Override
    public void setPresenter(NewsArticleInterfaces.IPresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void setNewsArticle(NewsArticleModel newsArticle) {
        DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.getDefault());
        dateFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Sofia"));
        ((TextView) this.getActivity().findViewById(R.id.tvNewsArticleDate))
                .setText(dateFormatter.format(newsArticle.getPostedOn()));
        ((TextView) this.getActivity().findViewById(R.id.tvNewsArticleText))
                .setText(newsArticle.getText());

        final ImageView image = this.getActivity().findViewById(R.id.ivNewsArticleImage);
        image.setAlpha(0.5f);

        final ProgressBar progressBar = this.getActivity().findViewById(R.id.pbNewsArticleImageLoading);
        progressBar.setVisibility(View.VISIBLE);


        netImageLoader.getImage(newsArticle.getImageUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        subscriptions.add(d);
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        image.setImageBitmap(bitmap);
                        image.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        image.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getActivity().getApplication())
                .getComponent()
                .inject(this);
    }
}
