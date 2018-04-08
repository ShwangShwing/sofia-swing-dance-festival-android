package com.sofiaswing.sofiaswingdancefestival.views.schedule;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.models.EventModel;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleView extends Fragment implements ScheduleInterfaces.IView {
    private final int VENUE_WIDTH = 500;
    private final int HEADER_HEIGHT = 200;
    private final int EVENT_HEIGHT = 200;
    private final float DIP_PER_MINUTE = 10;
    private final int HEADER_TIME_INTERVAL_MINUTES = 30;

    @Inject
    public ScheduleInterfaces.IPresenter presenter;
    private int previousScreenOrientation;
    private RelativeLayout schedule;
    private long minScheduleTimestampMs;
    private long maxScheduleTimestampMs;

    public static ScheduleView newInstance() {
        ScheduleView fragment = new ScheduleView();
        return fragment;
    }

    public ScheduleView() {
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
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_schedule_view, container, false);

        this.setRetainInstance(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.schedule = this.getView().findViewById(R.id.rl_shedule_container);
        this.previousScreenOrientation = this.getActivity().getRequestedOrientation();
        this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.getActivity().setRequestedOrientation(this.previousScreenOrientation);
        this.presenter.stop();
    }

    @Override
    public void setSchedule(List<VenueModel> venues, List<EventModel> events, Map<String, String> classLevelStrings) {
        schedule.removeAllViews();
        Map<String, Integer> venueRowMap = new HashMap<>();
        int curVenueRow = 0;
        for (VenueModel venue : venues) {
            this.putVenueRowInSchedule(curVenueRow, venue);
            venueRowMap.put(venue.getId(), curVenueRow);
            curVenueRow++;
        }

        this.calculateScheduleConstraints(events);
        this.populateHeader();
        for (EventModel event : events) {
            Integer venueRowIndex = venueRowMap.get(event.getVenueId());
            if (venueRowIndex != null) {
                this.putEventInSchedule(venueRowIndex, event, classLevelStrings);
            }
            else {
                // Something wrong... no such venue row
            }
        }
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getActivity().getApplication())
                .getComponent()
                .inject(this);
    }

    private void calculateScheduleConstraints(List<EventModel> events) {
        this.minScheduleTimestampMs = Long.MAX_VALUE;
        this.maxScheduleTimestampMs = 0;

        for (EventModel event : events) {
            this.minScheduleTimestampMs = Math.min(this.minScheduleTimestampMs, event.getStartTime().getTime());
            this.maxScheduleTimestampMs = Math.max(this.maxScheduleTimestampMs, event.getEndTime().getTime());
        }
    }

    private void populateHeader() {
        // populate dates
        final int MILISECONDS_IN_A_MINUTE = 60 * 1000;
        final long SECONDS_IN_A_DAY = 60 * 60 * 24;
        final long MILISECONDS_IN_A_DAY = 1000 * SECONDS_IN_A_DAY;
        for (long curDayStartTimestamp = this.minScheduleTimestampMs;
             curDayStartTimestamp < this.maxScheduleTimestampMs;) {
            View dayView = getLayoutInflater().inflate(R.layout.layout_schedule_date, null);
            TextView tvScheduleView = dayView.findViewById(R.id.tvScheduleDate);
            DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
            String dateAsString = dateFormatter.format(curDayStartTimestamp);
            tvScheduleView.setText(dateAsString);

            int width = Math.min(
                    (int)(SECONDS_IN_A_DAY / 60 * DIP_PER_MINUTE),
                    (int)((this.maxScheduleTimestampMs - curDayStartTimestamp) / MILISECONDS_IN_A_MINUTE * DIP_PER_MINUTE));
            this.addViewToSchedule(
                    VENUE_WIDTH +
                            (int)((curDayStartTimestamp - this.minScheduleTimestampMs) / MILISECONDS_IN_A_MINUTE * DIP_PER_MINUTE),
                    0,
                    width,
                    HEADER_HEIGHT / 2,
                    dayView);

            curDayStartTimestamp += MILISECONDS_IN_A_DAY;
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(curDayStartTimestamp);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            curDayStartTimestamp = cal.getTimeInMillis();
        }

        // populate times
        for (long curDayStartTimestamp = this.minScheduleTimestampMs;
             curDayStartTimestamp < this.maxScheduleTimestampMs;
             curDayStartTimestamp += HEADER_TIME_INTERVAL_MINUTES * MILISECONDS_IN_A_MINUTE) {
            View timeView = getLayoutInflater().inflate(R.layout.layout_schedule_time, null);
            TextView tvScheduleTime = timeView.findViewById(R.id.tvScheduleTime);
            DateFormat dateFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
            String timeAsString = dateFormatter.format(curDayStartTimestamp);
            tvScheduleTime.setText(timeAsString);

            this.addViewToSchedule(
                    VENUE_WIDTH +
                            (int)((curDayStartTimestamp - this.minScheduleTimestampMs) / MILISECONDS_IN_A_MINUTE * DIP_PER_MINUTE),
                    HEADER_HEIGHT / 2,
                    (int)(HEADER_TIME_INTERVAL_MINUTES * DIP_PER_MINUTE),
                    HEADER_HEIGHT / 2,
                    timeView);
        }
    }

    private void putVenueRowInSchedule(int venueRowIndex, VenueModel venue) {
        View venueView = getLayoutInflater().inflate(R.layout.layout_schedule_venue, null);
        TextView tvVenueName = venueView.findViewById(R.id.tvScheduleVenueName);
        tvVenueName.setText(venue.getName());

        this.addViewToSchedule(
                0,
                HEADER_HEIGHT + venueRowIndex * EVENT_HEIGHT,
                VENUE_WIDTH,
                EVENT_HEIGHT,
                venueView);
    }

    private void putEventInSchedule(int venueRowIndex, EventModel event, Map<String, String> classLevelStrings) {
        View eventView = getLayoutInflater().inflate(R.layout.layout_schedule_event, null);

        DateFormat dateFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());

        ((TextView) eventView.findViewById(R.id.tvTime))
                .setText(String.format("%s - %s",
                        dateFormatter.format(event.getStartTime()),
                        dateFormatter.format(event.getEndTime())));

        ((TextView) eventView.findViewById(R.id.tvName))
                .setText(event.getName());

        String eventTypeStr = "Error! Unknown event type!";
        String eventType = event.getEventType();
        if (eventType.equals("taster_class")) {
            eventTypeStr = getString(R.string.taster_class);
        }
        else if (eventType.startsWith("class_")) {
            String classLevel = classLevelStrings.get(eventType.substring("class_".length()));
            if (classLevel == null) {
                classLevel = "";
            }

            eventTypeStr = String.format("%s %s", getString(R.string.dance_class), classLevel);
        }
        else if (eventType.equals("party")) {
            eventTypeStr = getString(R.string.party);
        }

        ((TextView) eventView.findViewById(R.id.tvEventType))
                .setText(eventTypeStr);


        int eventLengthInMinutes = (int)(event.getEndTime().getTime() - event.getStartTime().getTime()) / (60 * 1000);
        int eventStartRelativeToFirstInMinutes = (int)(event.getStartTime().getTime() - this.minScheduleTimestampMs) / (60 * 1000);
        int cellLeftMargin = VENUE_WIDTH + (int)(eventStartRelativeToFirstInMinutes * DIP_PER_MINUTE);
        int cellWidth = (int)(DIP_PER_MINUTE * eventLengthInMinutes);
        int cellHeight = EVENT_HEIGHT;

        this.addViewToSchedule(
                cellLeftMargin,
                HEADER_HEIGHT + EVENT_HEIGHT * venueRowIndex,
                cellWidth,
                cellHeight,
                eventView);
    }

    private void addViewToSchedule(int leftMargin, int topMargin, int width, int height, View newView) {
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(width, height);
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        schedule.addView(newView, params);
    }
}
