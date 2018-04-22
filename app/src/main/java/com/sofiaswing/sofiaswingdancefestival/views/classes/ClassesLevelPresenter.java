package com.sofiaswing.sofiaswingdancefestival.views.classes;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.ClassModel;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ClassesLevelPresenter implements ClassesInterfaces.IClassesLevelPresenter{

  private ClassesInterfaces.IClassesLevelView view;
  private CompositeDisposable subscriptions = new CompositeDisposable();

  private final DataInterfaces.IVenuesData venuesData;
  private final DataInterfaces.IEventsData eventsData;
  private final DataInterfaces.IInstructorsData instructorsData;
  private final ProvidersInterfaces.ISettingsProvider settingsProvider;


  public ClassesLevelPresenter(DataInterfaces.IVenuesData venuesData,
                               DataInterfaces.IEventsData eventsData,
                               DataInterfaces.IInstructorsData instructorsData,
                               ProvidersInterfaces.ISettingsProvider settingsProvider) {
    this.venuesData = venuesData;
    this.eventsData = eventsData;
    this.instructorsData = instructorsData;
    this.settingsProvider = settingsProvider;
  }

  @Override
  public void setView(ClassesInterfaces.IClassesLevelView view) {
    this.view = view;
  }

  @Override
  public void start() {
    Observable<List<ClassModel>> observable;
    if (!this.view.isTaster()) {
      observable = eventsData.getClassesByLevel(view.getClassLevel());
    } else {
      observable = eventsData.getTasterClasses();
    }
    this.subscriptions.add(observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::augmentClassesList));
  }

  @Override
  public void stop() {
    this.subscriptions.clear();
  }

  @Override
  public void subscribeForEvent(String eventId, String eventName, int startTimestamp) {
    this.settingsProvider.subscribeForEvent(eventId, eventName, startTimestamp, 0);
  }

  @Override
  public void unsubscribeFromEvent(String eventId) {
    this.settingsProvider.unsubscribeFromEvent(eventId);
  }

  private void augmentClassesList(List<ClassModel> classes) {
    subscriptions.add(Observable.fromIterable(classes).flatMap(
            classModel -> {
              List<Observable<InstructorModel>> instructors = new ArrayList<>();
              for (String id : classModel.getInstructorIds()) {
                instructors.add(instructorsData.getById(id));
              }
              Observable<List<InstructorModel>> instructorsAll = Observable.merge(instructors).toList().toObservable();
              Observable<VenueModel> venue = venuesData.getById(classModel.getVenueId());
              return Observable.zip(instructorsAll, venue, (instructorModels, venueModel) ->
                      new ClassPresenterModel(classModel, instructorModels, venueModel, settingsProvider.isSubscribedForEvent(classModel.getId())));
            })
            .toList()
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(transformedClasses -> view.setClasses(transformedClasses)));
  }
}
