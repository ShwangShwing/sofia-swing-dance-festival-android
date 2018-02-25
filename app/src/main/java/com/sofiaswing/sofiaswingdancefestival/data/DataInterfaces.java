package com.sofiaswing.sofiaswingdancefestival.data;

import com.sofiaswing.sofiaswingdancefestival.models.ClassLevelModel;
import com.sofiaswing.sofiaswingdancefestival.models.ClassModel;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.models.NewsArticleModel;
import com.sofiaswing.sofiaswingdancefestival.models.PartyModel;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class DataInterfaces {
    public interface INewsArticlesData {
        Observable<List<NewsArticleModel>> getAll();
        Observable<NewsArticleModel> getById(String id);
    }

    public interface IInstructorsData {
        Observable<List<InstructorModel>> getAll();
        Observable<InstructorModel> getById(String id);
    }

    public interface IVenuesData {
        Observable<List<VenueModel>> getAll();
    }

    public interface IClassLevelsData {
        Observable<List<ClassLevelModel>> getAll();
    }

    public interface IEventsData {
        Observable<List<ClassModel>> getClassesByLevel(String level);
        Observable<List<ClassModel>> getTasterClasses();
    }

    public interface IPartiesData {
        Observable<List<PartyModel>> getParties();
    }

    public interface ICurrentSsdfYearData {
        Observable<String> getCurrentSsdfYear();
    }

    public interface ISsdfYearsData {
        Observable<List<String>> getSsdfYears();
    }
}
