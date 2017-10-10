package com.sofiaswing.sofiaswingdancefestival.views.instructorDetails;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;
import com.sofiaswing.sofiaswingdancefestival.views.instructors.InstructorViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstructorDetailsView extends Fragment
    implements InstructorDetailsInterfaces.IView {
    private InstructorDetailsInterfaces.IPresenter presenter;
    private ProvidersInterfaces.IImageProvider imageProvider;

    public InstructorDetailsView() {
        // Required empty public constructor
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
    public void onStart() {
        super.onStart();

        this.presenter.start();
    }

    @Override
    public void setPresenter(InstructorDetailsInterfaces.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setImageProvider(ProvidersInterfaces.IImageProvider imageProvider) {
        this.imageProvider = imageProvider;
    }

    @Override
    public void setInstructor(InstructorViewModel instructor) {
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
        image.setImageResource(R.drawable.newsarticleplaceholderimage);
        image.setAlpha(0.5f);

        final ProgressBar progressBar = this.getActivity().findViewById(R.id.pbInstructorImageLoading);
        progressBar.setVisibility(View.VISIBLE);

        imageProvider.getImageFromUrl(instructor.getImageUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        image.setImageBitmap(bitmap);
                        image.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // Image not found. Fail silently
                        Log.d("IMAGE_DOWNLOAD_FAIL",
                                String.format("Image downloading has filed: %s, %s",
                                        throwable.getClass(),
                                        throwable.getMessage()));
                        image.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
