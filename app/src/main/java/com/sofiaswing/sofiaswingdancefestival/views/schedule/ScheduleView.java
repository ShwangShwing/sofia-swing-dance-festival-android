package com.sofiaswing.sofiaswingdancefestival.views.schedule;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;
import com.sofiaswing.sofiaswingdancefestival.utils.DimensionUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleView extends Fragment implements ScheduleInterfaces.IView {
    private final int VENUE_WIDTH = 95;
    private final int HEADER_HEIGHT = 55;
    private final int EVENT_HEIGHT = 95;
    private final float DIP_PER_MINUTE = 3.5f;
    private final int HEADER_TIME_INTERVAL_MINUTES = 30;

    @Inject
    public ScheduleInterfaces.IPresenter presenter;
    @Inject
    public ProvidersInterfaces.ICurrentTimeProvider currentTimeProvider;
    private RelativeLayout schedule;
    private long minScheduleTimestampMs;
    private long maxScheduleTimestampMs;
    private View verticalLine = null;
    private boolean isRunning = false;
    private boolean isFirstScheduleLoading = true;
    // every event must change color when it expires. The key is the timestamp in ms when it should
    // expire and the contents is the layout itself that should change color
    private SortedMap<Long, List<LinearLayout>> eventContainerWithExpireTime;

    private int EVENT_HEIGHT_PX;
    private int VENUE_WIDTH_PX;
    private int HEADER_HEIGHT_PX;
    private int DIP_PER_MINUTE_PX;

    public static ScheduleView newInstance() {
        ScheduleView fragment = new ScheduleView();
        fragment.eventContainerWithExpireTime = new TreeMap<>();
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

        EVENT_HEIGHT_PX = DimensionUtils.dipToPixels(getContext(), EVENT_HEIGHT);
        VENUE_WIDTH_PX = DimensionUtils.dipToPixels(getContext(), VENUE_WIDTH);
        HEADER_HEIGHT_PX = DimensionUtils.dipToPixels(getContext(), HEADER_HEIGHT);
        DIP_PER_MINUTE_PX = DimensionUtils.dipToPixels(getContext(), DIP_PER_MINUTE);
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
        this.isRunning = true;
        this.schedule = this.getView().findViewById(R.id.rl_shedule_container);
        this.presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.isRunning = false;
        this.presenter.stop();
    }

    @Override
    public void setSchedule(List<VenueModel> venues, List<ScheduleEventViewModel> events, Map<String, String> classLevelStrings) {
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
        this.eventContainerWithExpireTime.clear();
        for (ScheduleEventViewModel event : events) {
            Integer venueRowIndex = venueRowMap.get(event.getVenueId());
            if (venueRowIndex != null) {
                this.putEventInSchedule(venueRowIndex, event, classLevelStrings);
            } else {
                // Something wrong... no such venue row
            }
        }

        this.putVerticalTimelineAndMarkPassedEvents(this.isFirstScheduleLoading);
        this.isFirstScheduleLoading = false;
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getActivity().getApplication())
                .getComponent()
                .inject(this);
    }

    private void calculateScheduleConstraints(List<ScheduleEventViewModel> events) {
        this.minScheduleTimestampMs = Long.MAX_VALUE;
        this.maxScheduleTimestampMs = 0;

        for (ScheduleEventViewModel event : events) {
            this.minScheduleTimestampMs = Math.min(this.minScheduleTimestampMs, event.getStartTime().getTime());
            this.maxScheduleTimestampMs = Math.max(this.maxScheduleTimestampMs, event.getEndTime().getTime());
        }

        // Set the schedule to start and end on a round hour
        this.minScheduleTimestampMs -= this.minScheduleTimestampMs % (60 * 60 * 1000);
        this.maxScheduleTimestampMs -= this.maxScheduleTimestampMs % (60 * 60 * 1000);
    }

    private void populateHeader() {
        // populate dates
        final int MILISECONDS_IN_A_MINUTE = 60 * 1000;
        final long SECONDS_IN_A_DAY = 60 * 60 * 24;
        final long MILISECONDS_IN_A_DAY = 1000 * SECONDS_IN_A_DAY;
        for (long curDayStartTimestamp = this.minScheduleTimestampMs;
             curDayStartTimestamp < this.maxScheduleTimestampMs; ) {
            View dayView = getLayoutInflater().inflate(R.layout.layout_schedule_date, null);
            TextView tvScheduleView = dayView.findViewById(R.id.tvScheduleDate);
            String dateFmtString = android.text.format.DateFormat.getBestDateTimePattern(
                    Locale.getDefault(),
                    "yyyy-MM-dd EEEE");
            DateFormat dateFormatter = new SimpleDateFormat(dateFmtString);
            dateFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Sofia"));
            String dateAsString = dateFormatter.format(curDayStartTimestamp);
            tvScheduleView.setText(dateAsString);

            int width = Math.min(
                    (int) (SECONDS_IN_A_DAY / 60 * DIP_PER_MINUTE_PX),
                    (int) ((this.maxScheduleTimestampMs - curDayStartTimestamp) / MILISECONDS_IN_A_MINUTE * DIP_PER_MINUTE_PX));
            this.addViewToSchedule(
                    (int) ((curDayStartTimestamp - this.minScheduleTimestampMs) / MILISECONDS_IN_A_MINUTE * DIP_PER_MINUTE_PX),
                    0,
                    width,
                    HEADER_HEIGHT_PX / 2,
                    dayView);

            curDayStartTimestamp += MILISECONDS_IN_A_DAY;
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Sofia"));
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
            dateFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Sofia"));
            String timeAsString = dateFormatter.format(curDayStartTimestamp);
            tvScheduleTime.setText(timeAsString);

            this.addViewToSchedule(
                    (int) ((curDayStartTimestamp - this.minScheduleTimestampMs) / MILISECONDS_IN_A_MINUTE * DIP_PER_MINUTE_PX),
                    HEADER_HEIGHT_PX / 2,
                    (int) (HEADER_TIME_INTERVAL_MINUTES * DIP_PER_MINUTE_PX),
                    HEADER_HEIGHT_PX / 2,
                    timeView);
        }
    }

    private void putVenueRowInSchedule(int venueRowIndex, VenueModel venue) {
        View venueView = getLayoutInflater().inflate(R.layout.layout_schedule_venue, null);
        TextView tvVenueName = venueView.findViewById(R.id.tvScheduleVenueName);
        tvVenueName.setText(venue.getName());

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(VENUE_WIDTH_PX, EVENT_HEIGHT_PX);
        params.leftMargin = 0;
        params.topMargin = HEADER_HEIGHT_PX + venueRowIndex * EVENT_HEIGHT_PX;

        RelativeLayout lvVenuesContainer = this.getView().findViewById(R.id.rl_venues_container);
        lvVenuesContainer.addView(venueView, params);
    }

    private void putEventInSchedule(int venueRowIndex, ScheduleEventViewModel event, Map<String, String> classLevelStrings) {
        View eventView = getLayoutInflater().inflate(R.layout.layout_schedule_event, null);

        DateFormat dateFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        dateFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Sofia"));

        LinearLayout llEventContainer = eventView.findViewById(R.id.ll_event_container);
        long eventEndTimestampMs = event.getEndTime().getTime();
        if (this.currentTimeProvider.getCurrentTimeMs() >= eventEndTimestampMs) {
            llEventContainer.setBackgroundResource(R.color.pastEventBackground);
        } else {
            if (!this.eventContainerWithExpireTime.containsKey(eventEndTimestampMs)) {
                this.eventContainerWithExpireTime.put(
                        eventEndTimestampMs,
                        new ArrayList<>());
            }
            this.eventContainerWithExpireTime.get(eventEndTimestampMs).add(llEventContainer);
        }

        ((TextView) eventView.findViewById(R.id.tvTime))
                .setText(String.format("%s - %s",
                        dateFormatter.format(event.getStartTime()),
                        dateFormatter.format(event.getEndTime())));

        ((TextView) eventView.findViewById(R.id.tvName))
                .setText(event.getName());

        String eventTypeStr = getString(R.string.misc_event);
        String eventType = event.getEventType();
        if (eventType.startsWith("class_")) {
            String classLevel = classLevelStrings.get(eventType.substring("class_".length()));
            if (classLevel == null) {
                classLevel = "";
            }

            eventTypeStr = String.format("%s %s", getString(R.string.dance_class), classLevel);
        } else if (eventType.equals("party")) {
            eventTypeStr = getString(R.string.party);
        }

        ((TextView) eventView.findViewById(R.id.tvEventType))
                .setText(eventTypeStr);

        View notifyView = eventView.findViewById(R.id.ivIsSubscribed);
        View dontNotifyView = eventView.findViewById(R.id.ivIsNotSubscribed);
        if (event.isSubscribed()) {
            notifyView.setVisibility(View.VISIBLE);
            dontNotifyView.setVisibility(View.GONE);
        }
        else {
            // Currently you cannot subscribe from this screen, so let's keep the dontnotify view also invisible
            notifyView.setVisibility(View.GONE);
            dontNotifyView.setVisibility(View.GONE);
        }

        int eventLengthInMinutes = (int) (event.getEndTime().getTime() - event.getStartTime().getTime()) / (60 * 1000);
        int eventStartRelativeToFirstInMinutes = (int) (event.getStartTime().getTime() - this.minScheduleTimestampMs) / (60 * 1000);
        int cellLeftMargin = eventStartRelativeToFirstInMinutes * DIP_PER_MINUTE_PX;
        int cellWidth = DIP_PER_MINUTE_PX * eventLengthInMinutes;
        int cellHeight = EVENT_HEIGHT_PX;

        this.addViewToSchedule(
                cellLeftMargin,
                HEADER_HEIGHT_PX + EVENT_HEIGHT_PX * venueRowIndex,
                cellWidth,
                cellHeight,
                eventView);
    }

    private void putVerticalTimelineAndMarkPassedEvents(boolean scrollToTime) {
        if (!this.isRunning) {
            return;
        }

        if (this.verticalLine != null) {
            schedule.removeView(this.verticalLine);
        }

        long currentTimeMs = this.currentTimeProvider.getCurrentTimeMs();
        if (currentTimeMs < this.maxScheduleTimestampMs) {
            long delayAfterMs;
            if (this.minScheduleTimestampMs < currentTimeMs) {
                delayAfterMs = (long) (60d * 1000d / DIP_PER_MINUTE_PX);
            } else {
                delayAfterMs = (this.minScheduleTimestampMs - currentTimeMs);
            }

            new android.os.Handler().postDelayed(() -> this.putVerticalTimelineAndMarkPassedEvents(false), delayAfterMs);
        }

        if (this.minScheduleTimestampMs < currentTimeMs && currentTimeMs < this.maxScheduleTimestampMs) {
            this.verticalLine = new View(this.getContext());
            this.schedule.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int scheduleHeight = this.schedule.getMeasuredHeight();
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(DIP_PER_MINUTE_PX, scheduleHeight);
            params.leftMargin = (int) (DIP_PER_MINUTE_PX * (currentTimeMs - this.minScheduleTimestampMs) / 1000 / 60);
            params.topMargin = 0;
            verticalLine.setBackgroundColor(
                    this.getResources().getColor(R.color.scheduleCurrentTimeLine));
            verticalLine.setLayoutParams(params);
            schedule.addView(verticalLine, params);
            if (scrollToTime) {
                HorizontalScrollView hsvScheduleContainer = this.getView().findViewById(R.id.hsv_schedule_container);

                hsvScheduleContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                        hsvScheduleContainer.scrollTo(params.leftMargin, 0);
                        hsvScheduleContainer.removeOnLayoutChangeListener(this);
                    }
                });

            }
        }

        // Mark passed events
        while (!this.eventContainerWithExpireTime.isEmpty()) {
            long earliestEndTimestamp = this.eventContainerWithExpireTime.firstKey();
            if (currentTimeMs < earliestEndTimestamp) {
                break;
            }

            for (LinearLayout currentEventContainer : this.eventContainerWithExpireTime.get(earliestEndTimestamp)) {
                currentEventContainer.setBackgroundResource(R.color.pastEventBackground);
            }

            this.eventContainerWithExpireTime.remove(earliestEndTimestamp);
        }
    }

    private void addViewToSchedule(int leftMargin, int topMargin, int width, int height, View newView) {
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(width, height);
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        schedule.addView(newView, params);
    }
}
