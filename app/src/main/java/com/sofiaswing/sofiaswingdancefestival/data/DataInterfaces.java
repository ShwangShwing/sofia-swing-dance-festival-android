package com.sofiaswing.sofiaswingdancefestival.data;

import com.sofiaswing.sofiaswingdancefestival.models.ClassLevelModel;
import com.sofiaswing.sofiaswingdancefestival.models.ClassModel;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.models.NewsArticleModel;
import com.sofiaswing.sofiaswingdancefestival.models.PartyModel;
import com.sofiaswing.sofiaswingdancefestival.models.EventModel;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class DataInterfaces {
    // This is a workaround for firebase connection that likes to stop working
    // sometimes when switching networks.
    public interface IBrokenDbConnectionFixer {
        void fixBrokenDbConnection();
    }

    public interface INewsArticlesData {
        default Observable<List<NewsArticleModel>> getAll() {
            return this.getAll(false);
        }
        Observable<List<NewsArticleModel>> getAll(boolean includeUnpublished);
        Observable<NewsArticleModel> getById(String id);
    }

    public interface IInstructorsData {
        Observable<List<InstructorModel>> getAll();
        Observable<InstructorModel> getById(String id);
    }

    public interface IVenuesData {
        Observable<List<VenueModel>> getAll();
        Observable<VenueModel> getById(String id);
    }

    public interface IClassLevelsData {
        Observable<List<ClassLevelModel>> getAll();
    }

    public interface IEventsData {
        Observable<List<PartyModel>> getParties();
        Observable<List<ClassModel>> getClassesByLevel(String level);
        Observable<List<EventModel>> getEvents(List<String> eventIds);
    }

    public interface ICurrentSsdfYearData {
        Observable<String> getCurrentSsdfYear();
    }

    public interface ISsdfYearsData {
        Observable<List<String>> getSsdfYears();
    }
}
