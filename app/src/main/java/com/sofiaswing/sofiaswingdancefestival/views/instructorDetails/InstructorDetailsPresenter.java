package com.sofiaswing.sofiaswingdancefestival.views.instructorDetails;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class InstructorDetailsPresenter implements InstructorDetailsInterfaces.IPresenter {
    private final InstructorDetailsInterfaces.IView view;
    private final DataInterfaces.IInstructorsData instructorsData;
    private String instructorId;

    private final CompositeDisposable subscriptions;

    public InstructorDetailsPresenter(
            InstructorDetailsInterfaces.IView view,
            DataInterfaces.IInstructorsData instructorsData) {
        this.view = view;
        this.instructorsData = instructorsData;

        this.view.setPresenter(this);

        this.subscriptions = new CompositeDisposable();
    }

    @Override
    public InstructorDetailsInterfaces.IView getView() {
        return this.view;
    }

    @Override
    public void start() {
        this.subscriptions.add(
                this.instructorsData.getById(this.instructorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(instructor -> view.setInstructor(instructor))
        );
    }

    @Override
    public void stop() {
        this.subscriptions.clear();
    }

    @Override
    public void setInstructorId(String id) {
        this.instructorId = id;
    }
}
