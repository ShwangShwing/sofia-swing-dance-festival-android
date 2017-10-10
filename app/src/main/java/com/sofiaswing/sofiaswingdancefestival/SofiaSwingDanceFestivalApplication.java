package com.sofiaswing.sofiaswingdancefestival;

import android.app.Application;

import com.sofiaswing.sofiaswingdancefestival.data.FirebaseData.FirebaseDataModule;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersModule;
import com.sofiaswing.sofiaswingdancefestival.views.instructorDetails.InstructorDetailsActivity;
import com.sofiaswing.sofiaswingdancefestival.views.instructorDetails.InstructorDetailsModule;
import com.sofiaswing.sofiaswingdancefestival.views.instructors.InstructorsActivity;
import com.sofiaswing.sofiaswingdancefestival.views.instructors.InstructorsModule;
import com.sofiaswing.sofiaswingdancefestival.views.news.NewsActivity;
import com.sofiaswing.sofiaswingdancefestival.views.news.NewsModule;
import com.sofiaswing.sofiaswingdancefestival.views.newsArticle.NewsArticleActivity;
import com.sofiaswing.sofiaswingdancefestival.views.newsArticle.NewsArticleModule;
import com.sofiaswing.sofiaswingdancefestival.views.venues.VenuesActivity;
import com.sofiaswing.sofiaswingdancefestival.views.venues.VenuesModule;

import dagger.Component;

/**
 * Created by shwangshwing on 10/8/17.
 */

public class SofiaSwingDanceFestivalApplication extends Application {
    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        this.component = DaggerSofiaSwingDanceFestivalApplication_ApplicationComponent
                .builder()
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    @Component(modules = {
            NewsModule.class,
            NewsArticleModule.class,
            InstructorsModule.class,
            InstructorDetailsModule.class,
            VenuesModule.class,
            FirebaseDataModule.class,
            ProvidersModule.class
    })
    public interface ApplicationComponent {
        void inject(NewsActivity newsActivity);
        void inject(NewsArticleActivity newsArticleActivity);
        void inject(InstructorsActivity instructorsActivity);
        void inject(InstructorDetailsActivity instructorDetailsActivity);
        void inject(VenuesActivity venuesActivity);
    }
}
