package com.sofiaswing.sofiaswingdancefestival.views.classes;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.ClassModel;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class ClassesPresenter implements ClassesInterfaces.IPresenter {
    private ClassesInterfaces.IView view;
    private final DataInterfaces.IClassLevelsData classLevelsData;
    private final CompositeDisposable disposables;

    public ClassesPresenter(DataInterfaces.IClassLevelsData classLevelsData) {
        this.classLevelsData = classLevelsData;
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void setView(ClassesInterfaces.IView view) {
        this.view = view;
    }

    @Override
    public void start() {
        Disposable subscription = this.classLevelsData.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(classLevels -> view.setClassesTabs(classLevels));

        this.disposables.add(subscription);
    }

    @Override
    public void stop() {
        this.disposables.clear();
    }
}
