package com.sofiaswing.sofiaswingdancefestival.views.news;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsView extends Fragment
        implements NewsInterfaces.IView {
    public ProvidersInterfaces.IImageProvider imageProvider;

    private NewsInterfaces.IPresenter presenter;

    private ArrayAdapter<NewsArticleViewModel> lvNewsAdapter;

    public NewsView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_news_view, container, false);

        ListView lvNewsArticles = root.findViewById(R.id.lvNewsArticles);
        this.lvNewsAdapter = new NewsArticlesAdapter(root.getContext(), android.R.layout.simple_list_item_1);
        lvNewsArticles.setAdapter(this.lvNewsAdapter);

        this.presenter.start();

        return root;
    }

    @Override
    public void setPresenter(NewsInterfaces.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setImageProvider(ProvidersInterfaces.IImageProvider imageProvider) {
        this.imageProvider = imageProvider;
    }

    @Override
    public void setNews(List<NewsArticleViewModel> newsArticles) {
        this.lvNewsAdapter.clear();
        this.lvNewsAdapter.addAll(newsArticles);
    }

    @Override
    public void navigateToArticle(String articleId) {

    }

    private class NewsArticlesAdapter extends ArrayAdapter<NewsArticleViewModel> {

        public NewsArticlesAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
        }

        public NewsArticlesAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View newsArticleRow = convertView;

            // TODO: Remove the row below when the image loading race condition is fixed
            newsArticleRow = null;
            if (newsArticleRow == null) {
                LayoutInflater inflater;
                inflater = LayoutInflater.from(this.getContext());
                newsArticleRow = inflater.inflate(R.layout.layout_news_article_row, null);
            }

            NewsArticleViewModel article = this.getItem(position);


            DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.getDefault());
            ((TextView) newsArticleRow.findViewById(R.id.tvNewsArticleDate))
                    .setText(dateFormatter.format(article.getPostedOn()));
            ((TextView) newsArticleRow.findViewById(R.id.tvNewsArticleText))
                    .setText(article.getText());

            final ImageView image = newsArticleRow.findViewById(R.id.ivNewsArticleImage);
            image.setImageResource(R.drawable.newsarticleplaceholderimage);
            image.setAlpha(0.5f);

            final ProgressBar progressBar = newsArticleRow.findViewById(R.id.pbNewsArticleImageLoading);
            progressBar.setVisibility(View.VISIBLE);

            imageProvider.getImageFromUrl(article.getImageUrl())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Bitmap>() {
                        @Override
                        public void accept(Bitmap bitmap) throws Exception {
                            //TODO: need to unsubscribe if views are reused then remove
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

            return newsArticleRow;
        }
    }
}
