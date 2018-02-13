package com.sofiaswing.sofiaswingdancefestival.views.instructors;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;
import com.sofiaswing.sofiaswingdancefestival.views.instructorDetails.InstructorDetailsActivity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstructorsView extends Fragment implements InstructorsInterfaces.IView {
    @Inject
    public InstructorsInterfaces.IPresenter presenter;
    @Inject
    public ProvidersInterfaces.IImageProvider imageProvider;

    private ArrayAdapter<InstructorModel> instructorsAdapter;
    private CompositeDisposable subscriptions;

    public static InstructorsView newInstance() {
        InstructorsView fragment = new InstructorsView();
        return fragment;
    }

    public InstructorsView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject();
        presenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.subscriptions = new CompositeDisposable();

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_instructors_view, container, false);
        this.setRetainInstance(true);

        ListView lvInstructors = root.findViewById(R.id.lvInstructors);
        this.instructorsAdapter = new InstructorsAdapter(root.getContext(), android.R.layout.simple_list_item_1);
        lvInstructors.setAdapter(this.instructorsAdapter);
        lvInstructors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.selectInstructor(position);
            }
        });

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
        this.subscriptions.clear();
    }

    @Override
    public void setInstructors(List<InstructorModel> instructors) {
        this.instructorsAdapter.clear();
        this.instructorsAdapter.addAll(instructors);
    }

    @Override
    public void navigateToInstructor(String instructorId) {
        Intent intent = new Intent(this.getContext(), InstructorDetailsActivity.class);
        intent.putExtra(InstructorDetailsActivity.INSTRUCTOR_ID_KEY, instructorId);
        this.startActivity(intent);
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getActivity().getApplication())
                .getComponent()
                .inject(this);
    }

    private class InstructorsAdapter extends ArrayAdapter<InstructorModel>  {
        public InstructorsAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View instructorRow = convertView;

            // TODO: Remove the row below when the image loading race condition is fixed
            instructorRow = null;
            if (instructorRow == null) {
                LayoutInflater inflater;
                inflater = LayoutInflater.from(this.getContext());
                instructorRow = inflater.inflate(R.layout.layout_instructor_row, null);
            }

            InstructorModel instructor = this.getItem(position);

            ((TextView) instructorRow.findViewById(R.id.tvInstructorName))
                    .setText(instructor.getName());

            String instructorTypeString = instructor.getType();
            if (instructor.getType().equals("main")) {
                instructorTypeString = getString(R.string.instructor_type_main);
            }
            else if (instructor.getType().equals("taster")) {
                instructorTypeString = getString(R.string.instructor_type_taster);
            }

            ((TextView) instructorRow.findViewById(R.id.tvInstructorType))
                    .setText(instructorTypeString);

//            ((TextView) instructorRow.findViewById(R.id.tvInstructorDescription))
//                    .setText(instructor.getDescription());

            final ImageView image = instructorRow.findViewById(R.id.ivInstructorImage);
            image.setImageResource(R.drawable.newsarticleplaceholderimage);
            image.setAlpha(0.5f);

            final ProgressBar progressBar = instructorRow.findViewById(R.id.pbInstructorImageLoading);
            progressBar.setVisibility(View.VISIBLE);

            subscriptions.add(
                    imageProvider.getImageFromUrl(instructor.getImageUrl())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Bitmap>() {
                        @Override
                        public void accept(Bitmap bitmap) throws Exception {
                            //TODO: need to unsubscribe if views are reused then remove
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
                    })
            );

            return instructorRow;
        }
    }
}
