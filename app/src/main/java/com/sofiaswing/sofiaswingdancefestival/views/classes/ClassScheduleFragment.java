package com.sofiaswing.sofiaswingdancefestival.views.classes;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.inject.Inject;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;


public class ClassScheduleFragment extends Fragment implements ClassesInterfaces.IClassesLevelView {
    private static final String CLASS_LEVEL_KEY = "class_level";
    @Inject
    public ClassesInterfaces.IClassesLevelPresenter presenter;
    @Inject
    public ProvidersInterfaces.ICurrentTimeProvider currentTimeProvider;
    private String classLevel;
    private SectionedRecyclerViewAdapter classScheduleAdapter;

    public ClassScheduleFragment() {
        // Required empty public constructor
    }

    public static ClassScheduleFragment newInstance(String classLevel) {
        ClassScheduleFragment fragment = new ClassScheduleFragment();
        Bundle args = new Bundle();
        args.putString(CLASS_LEVEL_KEY, classLevel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject();
        presenter.setView(this);

        this.classLevel = getArguments().getString(CLASS_LEVEL_KEY);
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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_class_schedule, container, false);

        setRetainInstance(true);

        RecyclerView lvClasses = root.findViewById(R.id.rvClassSchedule);
        this.classScheduleAdapter = new SectionedRecyclerViewAdapter();
        lvClasses.setLayoutManager(new LinearLayoutManager(getContext()));
        lvClasses.setAdapter(this.classScheduleAdapter);

        return root;
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getActivity().getApplication())
                .getComponent()
                .inject(this);
    }

    @Override
    public String getClassLevel() {
        return this.classLevel;
    }

    @Override
    public void setClasses(List<ClassPresenterModel> classes) {
        // The key is the unix timestamp in milliseconds of the start of the day
        // This will automatically sort days in the map in ascending order.
        SortedMap<Long, Pair<String, List<ClassPresenterModel>>> sectionsMap = new TreeMap<>();
        for (ClassPresenterModel classModel : classes) {
            String dateFmtString = android.text.format.DateFormat.getBestDateTimePattern(
                    Locale.getDefault(),
                    "yyyy-MMMM-dd EEEE");
            DateFormat fmt = new SimpleDateFormat(dateFmtString);
            fmt.setTimeZone(TimeZone.getTimeZone("Europe/Sofia"));
            String startDate = fmt.format(classModel.getClassModel().getStartTime());
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Sofia"));
            cal.setTimeInMillis(classModel.getClassModel().getStartTime().getTime());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Long startDateAsTimestampMs = cal.getTimeInMillis();
            if (sectionsMap.containsKey(startDateAsTimestampMs)) {
                sectionsMap.get(startDateAsTimestampMs).second.add(classModel);
            } else {
                List<ClassPresenterModel> newList = new ArrayList<>();
                newList.add(classModel);
                sectionsMap.put(startDateAsTimestampMs, new Pair(startDate, newList));
            }
        }
        this.classScheduleAdapter.removeAllSections();
        for (Map.Entry<Long, Pair<String, List<ClassPresenterModel>>> entry : sectionsMap.entrySet()) {
            this.classScheduleAdapter.addSection(
                    new ExpandableDaySection(
                            entry.getValue().first,
                            entry.getValue().second));
        }
        classScheduleAdapter.notifyDataSetChanged();
    }


    private class ExpandableDaySection extends StatelessSection {

        String title;
        List<ClassPresenterModel> list;
        boolean expanded = true;

        ExpandableDaySection(String title, List<ClassPresenterModel> list) {
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.layout_class_row)
                    .headerResourceId(R.layout.day_header_row)
                    .build());

            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return expanded ? list.size() : 0;
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            ClassPresenterModel classPresenterModel = list.get(position);

            if (classPresenterModel.getClassModel().getEndTime().getTime() <=
                    currentTimeProvider.getCurrentTimeMs()) {
                itemHolder.llEventContainer.setBackgroundResource(R.color.pastEventBackground);
            }
            else {
                itemHolder.llEventContainer.setBackgroundResource(R.color.pendingEventBackground);
            }

            DateFormat dateTimeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
            dateTimeFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Sofia"));

            itemHolder.tvName.setText(classPresenterModel.getClassModel().getName());
            itemHolder.tvTime.setText(String.format("%s - %s",
                    dateTimeFormatter.format(classPresenterModel.getClassModel().getStartTime()),
                    dateTimeFormatter.format(classPresenterModel.getClassModel().getEndTime())));

            String instructorsNames = "";
            for (InstructorModel instructor : classPresenterModel.getInstructors()) {
                if (instructorsNames != "") {
                    instructorsNames += ", ";
                }
                instructorsNames += instructor.getName();
            }
            itemHolder.tvInstructors.setText(instructorsNames);
            itemHolder.tvVenue.setText(classPresenterModel.getVenue().getName());
            itemHolder.tvIsSubscribed.setVisibility(classPresenterModel.isSubscribed() ? View.VISIBLE : View.GONE);


            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (classPresenterModel.isSubscribed()) {
                        classPresenterModel.setSubscribed(false);
                        presenter.unsubscribeFromEvent(classPresenterModel.getClassModel().getId());
                    } else {
                        classPresenterModel.setSubscribed(true);
                        presenter.subscribeForEvent(classPresenterModel.getClassModel().getId(),
                                classPresenterModel.getClassModel().getName(),
                                (int) (classPresenterModel.getClassModel().getStartTime().getTime() / 1000));
                    }
                    classScheduleAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.tvTitle.setText(title);

            headerHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expanded = !expanded;
//          headerHolder.imgArrow.setImageResource(
//                  expanded ? R.drawable.ic_keyboard_arrow_up_black_18dp : R.drawable.ic_keyboard_arrow_down_black_18dp
//          );
                    classScheduleAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final TextView tvTitle;
//    private final ImageView imgArrow;

        HeaderViewHolder(View view) {
            super(view);

            rootView = view;
            tvTitle = view.findViewById(R.id.tvDayHeader);
//      imgArrow = (ImageView) view.findViewById(R.id.imgArrow);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final LinearLayout llEventContainer;
        private final TextView tvName;
        private final TextView tvTime;
        private final TextView tvVenue;
        private final TextView tvInstructors;
        private final TextView tvIsSubscribed;

        ItemViewHolder(View view) {
            super(view);
            rootView = view;
            llEventContainer = view.findViewById(R.id.ll_event_container);
            tvName = view.findViewById(R.id.tvName);
            tvTime = view.findViewById(R.id.tvTime);
            tvVenue = view.findViewById(R.id.tvVenue);
            tvInstructors = view.findViewById(R.id.tvInstructors);
            tvIsSubscribed = view.findViewById(R.id.tvIsSubscribed);
        }
    }
}
