package com.sofiaswing.sofiaswingdancefestival.views.news;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.models.NewsArticleModel;
import com.sofiaswing.sofiaswingdancefestival.views.newsArticle.NewsArticleActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsView extends Fragment implements NewsInterfaces.IView {
    private ArrayAdapter<NewsArticleModel> lvNewsAdapter;

    private CompositeDisposable subscriptions;

    @Inject
    public NewsInterfaces.IPresenter presenter;

    public static Fragment newInstance() {
        Fragment fragment = new NewsView();
        return fragment;
    }

    public NewsView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject();
        presenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.subscriptions = new CompositeDisposable();

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_news_view, container, false);

        this.setRetainInstance(true);

        ListView lvNewsArticles = root.findViewById(R.id.lvNewsArticles);
        this.lvNewsAdapter = new NewsArticlesAdapter(root.getContext(), android.R.layout.simple_list_item_1);
        lvNewsArticles.setAdapter(this.lvNewsAdapter);
        lvNewsArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.selectNewsArticle(position);
            }
        });

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
        subscriptions.clear();
    }

    @Override
    public void setNews(List<NewsArticleModel> newsArticles) {
        this.lvNewsAdapter.clear();
        this.lvNewsAdapter.addAll(newsArticles);
    }

    @Override
    public void navigateToArticle(String articleId) {
        Intent intent = new Intent(this.getContext(), NewsArticleActivity.class);
        intent.putExtra(NewsArticleActivity.ARTICLE_ID_KEY, articleId);
        startActivity(intent);
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getActivity().getApplication())
                .getComponent()
                .inject(this);
    }

    private class NewsArticlesAdapter extends ArrayAdapter<NewsArticleModel> {

        public NewsArticlesAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
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

            NewsArticleModel article = this.getItem(position);

            DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.getDefault());
            ((TextView) newsArticleRow.findViewById(R.id.tvNewsArticleDate))
                    .setText(dateFormatter.format(article.getPostedOn()));
            ((TextView) newsArticleRow.findViewById(R.id.tvNewsArticleText))
                    .setText(article.getText());

            final ImageView image = newsArticleRow.findViewById(R.id.ivNewsArticleImage);
            image.setAlpha(0.5f);

            final ProgressBar progressBar = newsArticleRow.findViewById(R.id.pbNewsArticleImageLoading);
            progressBar.setVisibility(View.VISIBLE);

            Picasso.with(getContext())
                    .load(Uri.parse(article.getImageUrl()))
                    .placeholder(R.drawable.sofia_swing_logo)
                    .error(R.drawable.sofia_swing_logo)
                    .into(image, new Callback() {
                        @Override
                        public void onSuccess() {
                            image.setAlpha(1f);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            image.setAlpha(1f);
                            progressBar.setVisibility(View.GONE);
                        }
                    });

            return newsArticleRow;
        }
    }
}
