package com.sofiaswing.sofiaswingdancefestival.views.instructors;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class InstructorsPresenter implements InstructorsInterfaces.IPresenter{
    private InstructorsInterfaces.IView view;
    private final DataInterfaces.IInstructorsData instructorsData;
    private List<InstructorModel> instructors;

    private final CompositeDisposable subscriptions;

    public InstructorsPresenter(DataInterfaces.IInstructorsData instructorsData) {
        this.instructorsData = instructorsData;

        this.instructors = new ArrayList<>();
        this.subscriptions = new CompositeDisposable();
    }

    @Override
    public void setView(InstructorsInterfaces.IView view) {
        this.view = view;
    }

    @Override
    public void start() {
        this.subscriptions.add(
                this.instructorsData.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(incomingInstructors -> {
                    instructors = new ArrayList(incomingInstructors);

                    view.setInstructors(instructors);
                })
        );
    }

    @Override
    public void stop() {
        this.subscriptions.clear();
    }

    @Override
    public void selectInstructor(int index) {
        this.view.navigateToInstructor(instructors.get(index).getDescription());
    }
}
