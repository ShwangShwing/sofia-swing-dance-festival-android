package com.sofiaswing.sofiaswingdancefestival;

import android.app.Application;

import com.sofiaswing.sofiaswingdancefestival.views.news.NewsActivity;

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

    @Component
    public interface ApplicationComponent {
        void inject(NewsActivity newsActivity);
    }
}
