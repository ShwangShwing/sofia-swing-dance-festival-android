package com.sofiaswing.sofiaswingdancefestival;

import android.app.Application;
import android.content.Context;

import com.sofiaswing.sofiaswingdancefestival.data.FirebaseData.FirebaseDataModule;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersModule;
import com.sofiaswing.sofiaswingdancefestival.ui.UiModule;
import com.sofiaswing.sofiaswingdancefestival.utils.EventSubscriptionAlarmReceiver;
import com.sofiaswing.sofiaswingdancefestival.views.about.AboutModule;
import com.sofiaswing.sofiaswingdancefestival.views.about.AboutView;
import com.sofiaswing.sofiaswingdancefestival.views.classes.ClassScheduleFragment;
import com.sofiaswing.sofiaswingdancefestival.views.classes.ClassesModule;
import com.sofiaswing.sofiaswingdancefestival.views.classes.ClassesView;
import com.sofiaswing.sofiaswingdancefestival.views.contactUs.ContactUsModule;
import com.sofiaswing.sofiaswingdancefestival.views.contactUs.ContactUsView;
import com.sofiaswing.sofiaswingdancefestival.views.instructorDetails.InstructorDetailsActivity;
import com.sofiaswing.sofiaswingdancefestival.views.instructorDetails.InstructorDetailsModule;
import com.sofiaswing.sofiaswingdancefestival.views.instructors.InstructorsModule;
import com.sofiaswing.sofiaswingdancefestival.views.instructors.InstructorsView;
import com.sofiaswing.sofiaswingdancefestival.views.main.MainActivity;
import com.sofiaswing.sofiaswingdancefestival.views.map.MapsActivity;
import com.sofiaswing.sofiaswingdancefestival.views.map.MapsModule;
import com.sofiaswing.sofiaswingdancefestival.views.myEvents.MyEventsModule;
import com.sofiaswing.sofiaswingdancefestival.views.myEvents.MyEventsView;
import com.sofiaswing.sofiaswingdancefestival.views.news.NewsModule;
import com.sofiaswing.sofiaswingdancefestival.views.news.NewsView;
import com.sofiaswing.sofiaswingdancefestival.views.newsArticle.NewsArticleActivity;
import com.sofiaswing.sofiaswingdancefestival.views.newsArticle.NewsArticleModule;
import com.sofiaswing.sofiaswingdancefestival.views.parties.PartiesModule;
import com.sofiaswing.sofiaswingdancefestival.views.parties.PartiesView;
import com.sofiaswing.sofiaswingdancefestival.views.schedule.ScheduleModule;
import com.sofiaswing.sofiaswingdancefestival.views.schedule.ScheduleView;
import com.sofiaswing.sofiaswingdancefestival.views.settings.SettingsModule;
import com.sofiaswing.sofiaswingdancefestival.views.settings.SettingsView;
import com.sofiaswing.sofiaswingdancefestival.views.venues.VenuesModule;
import com.sofiaswing.sofiaswingdancefestival.views.venues.VenuesView;

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
            MyEventsModule.class,
            ScheduleModule.class,
            VenuesModule.class,
            ContactUsModule.class,
            SettingsModule.class,
            AboutModule.class,
            UiModule.class,
            FirebaseDataModule.class,
            ProvidersModule.class,
            MapsModule.class
    })
    public interface ApplicationComponent {
        void inject(MainActivity mainActivity);
        void inject(NewsView newsView);
        void inject(NewsArticleActivity newsArticleActivity);
        void inject(InstructorsView instructorsView);
        void inject(InstructorDetailsActivity instructorDetailsActivity);
        void inject(VenuesView venuesView);
        void inject(ClassesView classesView);
        void inject(EventSubscriptionAlarmReceiver eventSubscriptionAlarmReceiver);
        void inject(PartiesView partiesView);
        void inject(MyEventsView myEventsView);
        void inject(ScheduleView shceduleView);
        void inject(AboutView aboutView);
        void inject(ContactUsView contactUsView);
        void inject(SettingsView settingsView);
        void inject(MapsActivity mapsActivity);
        void inject(ClassScheduleFragment classScheduleFragment);
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
