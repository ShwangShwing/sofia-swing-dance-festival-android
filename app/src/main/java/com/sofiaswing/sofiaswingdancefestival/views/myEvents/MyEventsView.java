package com.sofiaswing.sofiaswingdancefestival.views.myEvents;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.inject.Inject;

public class MyEventsView extends Fragment implements MyEventsInterfaces.IView {
    @Inject
    public MyEventsInterfaces.IPresenter presenter;

    private EventsAdapter myEventsAdapter;
    private Map<String, String> classLevelStrings;
    private long currentTimestampMs;

    public MyEventsView() {
        // Required empty public constructor
    }

    public static MyEventsView newInstance() {
        MyEventsView fragment = new MyEventsView();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.inject();
        this.presenter.setView(this);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_my_events_view, container, false);

        this.classLevelStrings = new HashMap<>();

        ListView lvMyEvents = root.findViewById(R.id.lvMyEventsSchedule);
        this.myEventsAdapter = new EventsAdapter(root.getContext(), android.R.layout.simple_list_item_1);
        lvMyEvents.setAdapter(this.myEventsAdapter);
        lvMyEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventViewModel eventItem = myEventsAdapter.getItem(position);

                if (eventItem.isSubscribed()) {
                    presenter.setEventSubscription(position, false);
                }
                else {
                    presenter.setEventSubscription(position, true);
                }
            }
        });
        return root;
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getActivity().getApplication())
                .getComponent()
                .inject(this);
    }

    @Override
    public void setClassLevelString(String levelId, String classLevelString) {
        this.classLevelStrings.put(levelId, classLevelString);
        this.myEventsAdapter.notifyDataSetChanged();
    }

    @Override
    public void setEvents(List<EventViewModel> events) {
        this.myEventsAdapter.clear();
        this.myEventsAdapter.addAll(events);
    }

    @Override
    public void setEventVenue(int eventPosition, VenueModel venueModel) {
        EventViewModel event = this.myEventsAdapter.getItem(eventPosition);
        event.setVenue(venueModel);
        this.myEventsAdapter.notifyDataSetChanged();
    }

    @Override
    public void setEventSubscriptionState(int position, boolean subscriptionStatus) {
        EventViewModel event = this.myEventsAdapter.getItem(position);
        event.setSubscribed(subscriptionStatus);
        this.myEventsAdapter.notifyDataSetChanged();
    }

    @Override
    public void setCurrentTimestampMs(long currentTimestampMs) {
        this.currentTimestampMs = currentTimestampMs;
        this.myEventsAdapter.notifyDataSetChanged();
    }

    private class EventsAdapter extends ArrayAdapter<EventViewModel> {

        public EventsAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View eventRow = convertView;

            if (eventRow == null) {
                LayoutInflater inflater;
                inflater = LayoutInflater.from(this.getContext());
                eventRow = inflater.inflate(R.layout.layout_event_row, null);
            }

            EventViewModel eventItem = getItem(position);

            LinearLayout llEventContainer = eventRow.findViewById(R.id.ll_event_container);
            TextView notifyView = eventRow.findViewById(R.id.tvIsSubscribed);
            Date endTime = eventItem.getEndTime();
            boolean isPastEvent = endTime != null && endTime.getTime() <= currentTimestampMs;
            if (isPastEvent) {
                llEventContainer.setBackgroundResource(R.color.pastEventBackground);
            }

            if (eventItem.isSubscribed()) {
                llEventContainer.setBackgroundResource(android.R.color.transparent);
                notifyView.setVisibility(View.VISIBLE);
            } else {
                llEventContainer.setBackgroundResource(android.R.color.transparent);
                notifyView.setVisibility(View.GONE);
            }

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, HH:mm", Locale.getDefault());
            dateFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Sofia"));

            ((TextView) eventRow.findViewById(R.id.tvTime))
                    .setText(String.format("%s - %s",
                            dateFormatter.format(eventItem.getStartTime()),
                            dateFormatter.format(eventItem.getEndTime())));

            ((TextView) eventRow.findViewById(R.id.tvName))
                    .setText(eventItem.getName());

            String eventTypeStr = getString(R.string.misc_event);
            String eventType = eventItem.getEventType();
            if (eventType.startsWith("class_")) {
                String classLevel = classLevelStrings.get(eventType.substring("class_".length()));
                if (classLevel == null) {
                    classLevel = "";
                }

                eventTypeStr = String.format("%s %s", getString(R.string.dance_class), classLevel);
            }
            else if (eventType.equals("party")) {
                eventTypeStr = getString(R.string.party);
            }

            ((TextView) eventRow.findViewById(R.id.tvEventType))
                    .setText(eventTypeStr);

            VenueModel eventVenue = eventItem.getVenue();
            if (eventVenue != null) {
                ((TextView) eventRow.findViewById(R.id.tvVenue))
                        .setText(eventVenue.getName());
            }

            return eventRow;
        }
    }
}
