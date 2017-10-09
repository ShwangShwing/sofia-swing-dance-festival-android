package com.sofiaswing.sofiaswingdancefestival.views.newsArticle;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;
import com.sofiaswing.sofiaswingdancefestival.views.news.NewsArticleViewModel;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsArticleView extends Fragment
    implements NewsArticleInterfaces.IView {

    private NewsArticleInterfaces.IPresenter presenter;
    private ProvidersInterfaces.IImageProvider imageProvider;

    public NewsArticleView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_news_article_view, container, false);

        this.setRetainInstance(true);

        this.presenter.start();

        return root;
    }

    @Override
    public void setPresenter(NewsArticleInterfaces.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setImageProvider(ProvidersInterfaces.IImageProvider imageProvider) {
        this.imageProvider = imageProvider;
    }

    @Override
    public void setNewsArticle(NewsArticleViewModel newsArticle) {
        DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.getDefault());
        ((TextView) this.getActivity().findViewById(R.id.tvNewsArticleDate))
                .setText(dateFormatter.format(newsArticle.getPostedOn()));
        ((TextView) this.getActivity().findViewById(R.id.tvNewsArticleText))
                .setText(newsArticle.getText());

        final ImageView image = this.getActivity().findViewById(R.id.ivNewsArticleImage);
        image.setImageResource(R.drawable.newsarticleplaceholderimage);
        image.setAlpha(0.5f);

        final ProgressBar progressBar = this.getActivity().findViewById(R.id.pbNewsArticleImageLoading);
        progressBar.setVisibility(View.VISIBLE);

        imageProvider.getImageFromUrl(newsArticle.getImageUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        image.setImageBitmap(bitmap);
                        image.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // Image not found. Fail silently
                        Log.d("IMAGE_DOWNLOAD_FAIL",
                                String.format("Image downloading has filed: %s, %s",
                                        throwable.getClass(),
                                        throwable.getMessage()));
                        image.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
