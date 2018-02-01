package com.sofiaswing.sofiaswingdancefestival.views.classes;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.models.ClassModel;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.utils.EventSubscriptionAlarmReceiver;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ClassScheduleFragment extends Fragment {
    private ClassesInterfaces.IPresenter presenter;
    private String classLevel;
    private ArrayAdapter<ClassModel> classScheduleAdapter;
    private List<Boolean> subscribedClasses;
    private boolean isTaster;

    public ClassScheduleFragment() {
        // Required empty public constructor
    }

    public static ClassScheduleFragment newInstance(
            ClassesInterfaces.IPresenter presenter,
            String classLevel,
            boolean isTaster) {
        ClassScheduleFragment fragment = new ClassScheduleFragment();

        fragment.presenter = presenter;
        fragment.classLevel = classLevel;
        fragment.isTaster = isTaster;

        fragment.subscribedClasses = new ArrayList<>();

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!this.isTaster) {
            presenter.getClassesByLevel(classLevel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<ClassModel>>() {
                        @Override
                        public void accept(List<ClassModel> classes) throws Exception {
                            classScheduleAdapter.clear();
                            subscribedClasses.clear();
                            for (int i = 0; i < classes.size(); i++) {
                                subscribedClasses.add(i,
                                        presenter.isSubscribedForEvent(classes.get(i).getId()));
                            }

                            classScheduleAdapter.addAll(classes);
                        }
                    });
        }
        else {
            presenter.getTasterClasses()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<ClassModel>>() {
                        @Override
                        public void accept(List<ClassModel> classes) throws Exception {
                            classScheduleAdapter.clear();
                            subscribedClasses.clear();
                            for (int i = 0; i < classes.size(); i++) {
                                subscribedClasses.add(i,
                                        presenter.isSubscribedForEvent(classes.get(i).getId()));
                            }

                            classScheduleAdapter.addAll(classes);
                        }
                    });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_class_schedule, container, false);

        ListView lvClasses = root.findViewById(R.id.lvClassSchedule);
        this.classScheduleAdapter = new ClassScheduleAdapter(root.getContext(), android.R.layout.simple_list_item_1);
        lvClasses.setAdapter(this.classScheduleAdapter);
        lvClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (subscribedClasses.get(position)) {
                    subscribedClasses.set(position, false);
                    view.findViewById(R.id.tvIsSubscribed).setVisibility(View.GONE);
                    presenter.unsubscribeFromEvent(classScheduleAdapter.getItem(position).getId());
                }
                else {
                    subscribedClasses.set(position, true);
                    view.findViewById(R.id.tvIsSubscribed).setVisibility(View.VISIBLE);
                    presenter.subscribeForEvent(
                            classScheduleAdapter.getItem(position).getId(),
                            classScheduleAdapter.getItem(position).getName(),
                            (int)(classScheduleAdapter.getItem(position).getStartTime().getTime() / 1000));

                }
            }
        });

        return root;
    }

    private class ClassScheduleAdapter extends ArrayAdapter<ClassModel> {
        private ArrayList<String> itemDates;

        public ClassScheduleAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
            itemDates = new ArrayList<>();
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

            {
                // Fill date separator
                DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());

                if (this.itemDates.size() <= position) {
                    this.itemDates.add(dateFormatter.format(classItem.getStartTime()));
                }
                else {
                    this.itemDates.set(position, dateFormatter.format(classItem.getStartTime()));
                }


                TextView tvDateSeparator = classRow.findViewById(R.id.tvDateSeparator);
                if (position == 0 || !this.itemDates.get(position).equals(this.itemDates.get(position - 1))) {
                    tvDateSeparator.setVisibility(View.VISIBLE);
                    tvDateSeparator.setText(String.format("%s", this.itemDates.get(position)));
                }
                else {
                    tvDateSeparator.setVisibility(View.GONE);
                }
            }

            {
                DateFormat dateTimeFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.getDefault());

                ((TextView) classRow.findViewById(R.id.tvTime))
                        .setText(String.format("%s - %s",
                                dateTimeFormatter.format(classItem.getStartTime()),
                                dateTimeFormatter.format(classItem.getEndTime())));

                ((TextView) classRow.findViewById(R.id.tvName))
                        .setText(classItem.getName());
            }

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

            if (subscribedClasses.get(position)) {
                subscribedClasses.set(position, true);
                classRow.findViewById(R.id.tvIsSubscribed).setVisibility(View.VISIBLE);
            }
            else {
                subscribedClasses.set(position, false);
                classRow.findViewById(R.id.tvIsSubscribed).setVisibility(View.GONE);
            }

            return classRow;
        }
    }
}
