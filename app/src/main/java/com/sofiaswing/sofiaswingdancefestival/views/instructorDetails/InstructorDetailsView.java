package com.sofiaswing.sofiaswingdancefestival.views.instructorDetails;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstructorDetailsView extends Fragment
    implements InstructorDetailsInterfaces.IView {
    private InstructorDetailsInterfaces.IPresenter presenter;

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
    public void onResume() {
        super.onResume();

        this.presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();

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

        Picasso.with(getContext())
                .load(Uri.parse(instructor.getImageUrl()))
                .placeholder(R.drawable.newsarticleplaceholderimage)
                .error(R.drawable.newsarticleplaceholderimage)
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        image.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        image.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
