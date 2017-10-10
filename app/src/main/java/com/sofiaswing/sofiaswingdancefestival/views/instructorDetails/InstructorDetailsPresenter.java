package com.sofiaswing.sofiaswingdancefestival.views.instructorDetails;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;
import com.sofiaswing.sofiaswingdancefestival.views.instructors.InstructorViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class InstructorDetailsPresenter implements InstructorDetailsInterfaces.IPresenter {
    private final InstructorDetailsInterfaces.IView view;
    private final DataInterfaces.IInstructorsData instructorsData;
    private String instructorId;

    public InstructorDetailsPresenter(
            InstructorDetailsInterfaces.IView view,
            DataInterfaces.IInstructorsData instructorsData,
            ProvidersInterfaces.IImageProvider imageProvider) {
        this.view = view;
        this.instructorsData = instructorsData;

        this.view.setPresenter(this);
        this.view.setImageProvider(imageProvider);
    }

    @Override
    public InstructorDetailsInterfaces.IView getView() {
        return this.view;
    }

    @Override
    public void start() {
        this.instructorsData.getById(this.instructorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<InstructorModel>() {
                    @Override
                    public void accept(InstructorModel instructor) throws Exception {
                        InstructorViewModel instructorView = new InstructorViewModel(
                                instructor.getName(),
                                instructor.getImageUrl(),
                                instructor.getType(),
                                instructor.getDescription()
                        );

                        view.setInstructor(instructorView);
                    }
                });
    }

    @Override
    public void setInstructorId(String id) {
        this.instructorId = id;
    }
}
