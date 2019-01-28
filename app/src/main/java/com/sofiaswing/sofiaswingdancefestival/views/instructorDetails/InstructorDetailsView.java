package com.sofiaswing.sofiaswingdancefestival.views.instructorDetails;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstructorDetailsView extends Fragment
    implements InstructorDetailsInterfaces.IView {
    private InstructorDetailsInterfaces.IPresenter presenter;

    private CompositeDisposable subscriptions;

    @Inject
    public ProvidersInterfaces.INetworkImageLoader netImageLoader;

    public InstructorDetailsView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.subscriptions = new CompositeDisposable();
        this.inject();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_instructor_details_view, container, false);

        this.setRetainInstance(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        this.presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.subscriptions.clear();
        this.presenter.stop();
    }

    @Override
    public void setPresenter(InstructorDetailsInterfaces.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setInstructor(InstructorModel instructor) {
        ((TextView) this.getActivity().findViewById(R.id.tvInstructorName))
                .setText(instructor.getName());

        String instructorTypeString = instructor.getType();
        if (instructor.getType().equals("main")) {
            instructorTypeString = getString(R.string.instructor_type_main);
        }
        else if (instructor.getType().equals("taster")) {
            instructorTypeString = getString(R.string.instructor_type_taster);
        }

        ((TextView) this.getActivity().findViewById(R.id.tvInstructorType))
                .setText(instructorTypeString);

        ((TextView) this.getActivity().findViewById(R.id.tvInstructorDescription))
                    .setText(instructor.getDescription());

        final ImageView image = this.getActivity().findViewById(R.id.ivInstructorImage);
        image.setAlpha(0.5f);

        final ProgressBar progressBar = this.getActivity().findViewById(R.id.pbInstructorImageLoading);
        progressBar.setVisibility(View.VISIBLE);

        netImageLoader.getImage(instructor.getImageUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        subscriptions.add(d);
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        image.setImageBitmap(bitmap);
                        image.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        image.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getActivity().getApplication())
                .getComponent()
                .inject(this);
    }
}
