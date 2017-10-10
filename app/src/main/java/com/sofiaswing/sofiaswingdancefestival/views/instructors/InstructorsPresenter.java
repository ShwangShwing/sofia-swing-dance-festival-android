package com.sofiaswing.sofiaswingdancefestival.views.instructors;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class InstructorsPresenter implements InstructorsInterfaces.IPresenter{
    private final InstructorsInterfaces.IView view;
    private final DataInterfaces.IInstructorsData instructorsData;
    private List<InstructorModel> instructors;

    public InstructorsPresenter(InstructorsInterfaces.IView view,
                                ProvidersInterfaces.IImageProvider imageProvider,
                                DataInterfaces.IInstructorsData instructorsData) {
        this.view = view;
        this.instructorsData = instructorsData;

        this.view.setPresenter(this);
        this.view.setImageProvider(imageProvider);

        this.instructors = new ArrayList<>();
    }

    @Override
    public InstructorsInterfaces.IView getView() {
        return this.view;
    }

    @Override
    public void start() {
        this.instructorsData.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<InstructorModel>>() {
                    @Override
                    public void accept(List<InstructorModel> incomingInstructors) throws Exception {
                        instructors = new ArrayList(incomingInstructors);

                        ArrayList<InstructorViewModel> viewInstructors = new ArrayList<InstructorViewModel>();

                        for (InstructorModel instructor : instructors) {
                            InstructorViewModel viewInstructor = new InstructorViewModel(
                                    instructor.getName(),
                                    instructor.getImageUrl(),
                                    instructor.getType()
                            );
                            viewInstructors.add(viewInstructor);
                        }

                        view.setInstructors(viewInstructors);
                    }
                });
    }

    @Override
    public void selectInstructor(int index) {
        this.view.navigateToInstructor(instructors.get(index).getId());
    }
}
