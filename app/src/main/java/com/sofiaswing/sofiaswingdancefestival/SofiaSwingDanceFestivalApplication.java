package com.sofiaswing.sofiaswingdancefestival;

import android.app.Application;
import android.content.Context;

import com.sofiaswing.sofiaswingdancefestival.data.FirebaseData.FirebaseDataModule;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersModule;
import com.sofiaswing.sofiaswingdancefestival.utils.EventSubscriptionAlarmReceiver;
import com.sofiaswing.sofiaswingdancefestival.views.about.AboutActivity;
import com.sofiaswing.sofiaswingdancefestival.views.about.AboutModule;
import com.sofiaswing.sofiaswingdancefestival.views.classes.ClassesActivity;
import com.sofiaswing.sofiaswingdancefestival.views.classes.ClassesModule;
import com.sofiaswing.sofiaswingdancefestival.views.contactUs.ContactUsActivity;
import com.sofiaswing.sofiaswingdancefestival.views.contactUs.ContactUsModule;
import com.sofiaswing.sofiaswingdancefestival.views.instructorDetails.InstructorDetailsActivity;
import com.sofiaswing.sofiaswingdancefestival.views.instructorDetails.InstructorDetailsModule;
import com.sofiaswing.sofiaswingdancefestival.views.instructors.InstructorsActivity;
import com.sofiaswing.sofiaswingdancefestival.views.instructors.InstructorsModule;
import com.sofiaswing.sofiaswingdancefestival.views.news.NewsActivity;
import com.sofiaswing.sofiaswingdancefestival.views.news.NewsModule;
import com.sofiaswing.sofiaswingdancefestival.views.newsArticle.NewsArticleActivity;
import com.sofiaswing.sofiaswingdancefestival.views.newsArticle.NewsArticleModule;
import com.sofiaswing.sofiaswingdancefestival.views.parties.PartiesActivity;
import com.sofiaswing.sofiaswingdancefestival.views.parties.PartiesModule;
import com.sofiaswing.sofiaswingdancefestival.views.venues.VenuesActivity;
import com.sofiaswing.sofiaswingdancefestival.views.venues.VenuesModule;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

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
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    @Component(modules = {
            ApplicationModule.class,
            NewsModule.class,
            NewsArticleModule.class,
            InstructorsModule.class,
            InstructorDetailsModule.class,
            ClassesModule.class,
            PartiesModule.class,
            VenuesModule.class,
            ContactUsModule.class,
            AboutModule.class,
            FirebaseDataModule.class,
            ProvidersModule.class
    })
    public interface ApplicationComponent {
        void inject(NewsActivity newsActivity);
        void inject(NewsArticleActivity newsArticleActivity);
        void inject(InstructorsActivity instructorsActivity);
        void inject(InstructorDetailsActivity instructorDetailsActivity);
        void inject(VenuesActivity venuesActivity);
        void inject(ClassesActivity classesActivity);
        void inject(EventSubscriptionAlarmReceiver eventSubscriptionAlarmReceiver);
        void inject(PartiesActivity partiesActivity);
        void inject(AboutActivity aboutActivity);

        void inject(ContactUsActivity contactUsActivity);
    }
}

@Module
class ApplicationModule {
    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context provideContext() {
        return this.context;
    }
}
