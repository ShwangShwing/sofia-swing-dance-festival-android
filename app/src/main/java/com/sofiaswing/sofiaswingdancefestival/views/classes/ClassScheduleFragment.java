package com.sofiaswing.sofiaswingdancefestival.views.classes;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.models.ClassModel;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ClassScheduleFragment extends Fragment {
    private ClassesInterfaces.IPresenter presenter;
    private String classLevel;
    private ArrayAdapter<ClassModel> classScheduleAdapter;

    public ClassScheduleFragment() {
        // Required empty public constructor
    }

    public static ClassScheduleFragment newInstance(ClassesInterfaces.IPresenter presenter, String classLevel) {
        ClassScheduleFragment fragment = new ClassScheduleFragment();

        fragment.presenter = presenter;
        fragment.classLevel = classLevel;

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter.getClassByLevel(classLevel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ClassModel>>() {
                    @Override
                    public void accept(List<ClassModel> classModels) throws Exception {
                        classScheduleAdapter.clear();
                        classScheduleAdapter.addAll(classModels);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_class_schedule, container, false);

        ListView lvClasses = root.findViewById(R.id.lvClassSchedule);
        this.classScheduleAdapter = new ClassScheduleAdapter(root.getContext(), android.R.layout.simple_list_item_1);
        lvClasses.setAdapter(this.classScheduleAdapter);

        return root;
    }

    private class ClassScheduleAdapter extends ArrayAdapter<ClassModel> {

        public ClassScheduleAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View classRow = convertView;

            if (classRow == null) {
                LayoutInflater inflater;
                inflater = LayoutInflater.from(this.getContext());
                classRow = inflater.inflate(R.layout.layout_class_row, null);
            }

            ClassModel classItem = getItem(position);

            DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.getDefault());

            ((TextView) classRow.findViewById(R.id.tvTime))
                    .setText(String.format("%s - %s",
                            dateFormatter.format(classItem.getStartTime()),
                            dateFormatter.format(classItem.getEndTime())));

            String instructorsNames = "";
            for (InstructorModel instructor: classItem.getInstructors()) {
                if (instructorsNames != "") {
                    instructorsNames += ", ";
                }

                instructorsNames += instructor.getName();
            }

            ((TextView) classRow.findViewById(R.id.tvInstructors))
                    .setText(instructorsNames);

            ((TextView) classRow.findViewById(R.id.tvVenue))
                    .setText(classItem.getVenue().getName());

            return classRow;
        }
    }
}
