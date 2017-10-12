package com.sofiaswing.sofiaswingdancefestival.views.settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.sofiaswing.sofiaswingdancefestival.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsView extends Fragment implements SettingsInterfaces.IView {
    private final long SIGNIFICANTLY_LARGE_TIME_INTERVAL_SECONDS = 2 * 365 * 24 * 60 * 60;
    private SettingsInterfaces.IPresenter presenter;
    private ArrayAdapter<String> eventNotifyTimeAdapter;
    private List<Long> eventNotifyTimesSeconds;
    private Spinner spinner;
    private boolean ignoreNextNotificationTimeCallback;

    public SettingsView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_settings_view, container, false);

        this.spinner = (Spinner) root.findViewById(R.id.spEventNotifyTime);
        this.eventNotifyTimeAdapter = new ArrayAdapter(root.getContext(), android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(this.eventNotifyTimeAdapter);

        eventNotifyTimesSeconds = new ArrayList<>();
        eventNotifyTimesSeconds.add(SIGNIFICANTLY_LARGE_TIME_INTERVAL_SECONDS);
        this.eventNotifyTimeAdapter.add(this.getString(R.string.dont_notify));
        eventNotifyTimesSeconds.add(new Long(15 * 60));
        this.eventNotifyTimeAdapter.add(String.format("%d %s", 15, this.getString(R.string.minutes)));
        eventNotifyTimesSeconds.add(new Long(30 * 60));
        this.eventNotifyTimeAdapter.add(String.format("%d %s", 30, this.getString(R.string.minutes)));
        eventNotifyTimesSeconds.add(new Long(45 * 60));
        this.eventNotifyTimeAdapter.add(String.format("%d %s", 45, this.getString(R.string.minutes)));
        eventNotifyTimesSeconds.add(new Long(60 * 60));
        this.eventNotifyTimeAdapter.add(this.getString(R.string.hour));

        this.ignoreNextNotificationTimeCallback = true; // Avoid events to presenter during initialization of spinner
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!ignoreNextNotificationTimeCallback) {
                    presenter.setEventsNotificationAdvanceTimeSeconds(eventNotifyTimesSeconds.get(position));
                }
                ignoreNextNotificationTimeCallback = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.presenter.start();
    }

    @Override
    public void setEventNotificationTimeSelection(long seconds) {
        this.ignoreNextNotificationTimeCallback = true;
        boolean foundSelection = false;
        if (seconds >= SIGNIFICANTLY_LARGE_TIME_INTERVAL_SECONDS) {
            this.spinner.setSelection(0);
            foundSelection = true;
        } else {
            for (int i = 0; i < eventNotifyTimesSeconds.size(); i++) {
                if (eventNotifyTimesSeconds.get(i) == seconds) {
                    this.spinner.setSelection(i);
                    foundSelection = true;
                }
            }
        }

        if (!foundSelection) {
            eventNotifyTimesSeconds.add(seconds);
            eventNotifyTimeAdapter.add(String.format("%d %s", seconds / 60, this.getString(R.string.minutes)));
            this.spinner.setSelection(eventNotifyTimesSeconds.size() - 1);
        }
    }

    @Override
    public void setPresenter(SettingsInterfaces.IPresenter presenter) {
        this.presenter = presenter;
    }
}
