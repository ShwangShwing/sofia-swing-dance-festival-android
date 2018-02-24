package com.sofiaswing.sofiaswingdancefestival.views.map;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ivo on 16.02.18.
 */

public class MapsPresenter implements MapsInterfaces.IPresenter {
  private final DataInterfaces.IVenuesData venuesData;
  private final CompositeDisposable subscriptions = new CompositeDisposable();
  private MapsInterfaces.IView view;

  public MapsPresenter(DataInterfaces.IVenuesData venuesData) {
    this.venuesData = venuesData;
  }

  @Override
  public void setView(MapsInterfaces.IView view) {
    this.view = view;
  }

  @Override
  public void start() {
    subscriptions.add(this.venuesData.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(venueModels -> view.setVenues(venueModels)));
  }

  @Override
  public void stop() {
    this.subscriptions.clear();
  }
}
