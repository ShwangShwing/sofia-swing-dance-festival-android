package com.sofiaswing.sofiaswingdancefestival.views.newsArticle;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;

import javax.inject.Inject;

public class NewsArticleActivity extends AppCompatActivity {
    public static final String ARTICLE_ID_KEY = "article_id";

    @Inject
    NewsArticleInterfaces.IPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);

        this.inject();

        Intent intent = this.getIntent();
        String newsArticleId = intent.getStringExtra(ARTICLE_ID_KEY);

        this.presenter.setNewsArticleId(newsArticleId);

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragment, (Fragment) this.presenter.getView())
                .commit();
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getApplication())
                .getComponent().inject(this);
    }
}
