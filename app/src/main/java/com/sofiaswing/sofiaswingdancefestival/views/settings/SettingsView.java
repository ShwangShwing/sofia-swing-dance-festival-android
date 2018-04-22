package com.sofiaswing.sofiaswingdancefestival.views.settings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.ui.UiInterfaces;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsView extends Fragment implements SettingsInterfaces.IView {
    private final long SIGNIFICANTLY_LARGE_TIME_INTERVAL_SECONDS = 2 * 365 * 24 * 60 * 60;

    @Inject
    public SettingsInterfaces.IPresenter presenter;
    @Inject
    public UiInterfaces.IPopupCreator popupCreator;

    private ArrayAdapter<String> eventNotifyTimeAdapter;
    private List<Long> eventNotifyTimesSeconds;
    private Spinner spinner;
    private boolean ignoreNextNotificationTimeCallback;
    //the following variables are for enabling some features for debugging
    private GestureDetectorCompat hackerModeGestureDetector;
    private int hackerModeCorrectGestureCount;
    // combination for enabling the hacker mode
    private static final String[] hackerModeCorrectGestureCombination = {
            "up",
            "up",
            "down",
            "down",
            "left",
            "right",
            "left",
            "right"};
    private ArrayAdapter<String> ssdfYearsAdapter;

    public static SettingsView newInstance() {
        SettingsView fragment = new SettingsView();
        return fragment;
    }

    public SettingsView() {
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
        final View root = inflater.inflate(R.layout.fragment_settings_view, container, false);

        this.setupNormalSettings(root);
        this.setupHackerModeEnablingGesture(root);
        this.setupCurrentSsdfYearSelector(root);
        this.setupTestNotificationPanel(root);
        this.setupCurrentTimeTestPanel(root);

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
    public void notifyHackerModeEnabled() {
        popupCreator.popup(getContext(), "Hacker mode enabled");
    }

    @Override
    public void showHackerModeEnabledIndicator() {
        ImageView tvHackerModeEnabledIndicator
                = this.getView().findViewById(R.id.ivHackerModeEnabledIndicator);
        tvHackerModeEnabledIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideHackerModeEnabledIndicator() {
        ImageView tvHackerModeEnabledIndicator
                = this.getView().findViewById(R.id.ivHackerModeEnabledIndicator);
        tvHackerModeEnabledIndicator.setVisibility(View.GONE);
    }

    @Override
    public void showHackerPanel() {
        LinearLayout llHackerPanel = this.getView().findViewById(R.id.ll_hacker_panel);
        llHackerPanel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideHackerPanel() {
        LinearLayout llHackerPanel = this.getView().findViewById(R.id.ll_hacker_panel);
        llHackerPanel.setVisibility(View.GONE);
    }

    @Override
    public void setYearFromDatabase(boolean isSet) {
        CheckBox cbYearFromDatabase = this.getView().findViewById(R.id.cb_year_from_database);
        cbYearFromDatabase.setChecked(isSet);
    }

    @Override
    public void setCustomYear(String customYear) {
        Spinner sp = this.getView().findViewById(R.id.sp_custom_year);
        int index = this.ssdfYearsAdapter.getPosition(customYear);
        if (index >= 0) {
            sp.setSelection(index);
        }
    }

    @Override
    public void setSsdfYears(List<String> ssdfYears) {
        ssdfYearsAdapter.clear();
        ssdfYearsAdapter.addAll(ssdfYears);
    }

    @Override
    public void notifyCurrentTimeMs(long currentTimeMs) {
        DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.getDefault());
        dateFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Sofia"));
        Date curDateTime = new Date(currentTimeMs);
        this.popupCreator.popup(this.getContext(), dateFormatter.format(curDateTime));
    }

    @Override
    public void setNotifyOverrideTimeState(boolean isOverriden, boolean isFrozen) {
        CheckBox cbOverrideTime = this.getView().findViewById(R.id.cb_override_time);
        cbOverrideTime.setChecked(isOverriden);
        CheckBox cbFreezeTime = this.getView().findViewById(R.id.cb_freeze_time);
        cbFreezeTime.setChecked(isFrozen);
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getActivity().getApplication())
                .getComponent()
                .inject(this);
    }

    private void setupNormalSettings(final View root) {
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

        this.ignoreNextNotificationTimeCallback = true; // Avoid events to newPresenter during initialization of spinner
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
    }

    private void setupHackerModeEnablingGesture(View root) {
        this.hackerModeCorrectGestureCount = 0;
        this.hackerModeGestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent event) {
                // don't return false here or else none of the other
                // gestures will work
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                String eventName = "";

                float xDiff = e2.getX() - e1.getX();
                float yDiff = e2.getY() - e1.getY();

                if (Math.abs(xDiff) > Math.abs(yDiff)) {
                    // horizontal
                    if (xDiff > 0) {
                        eventName = "right";
                    }
                    else {
                        eventName = "left";
                    }
                }
                else {
                    // vertical
                    if (yDiff > 0) {
                        eventName = "down";
                    }
                    else {
                        eventName = "up";
                    }
                }

                if (eventName == hackerModeCorrectGestureCombination[hackerModeCorrectGestureCount]) {
                    hackerModeCorrectGestureCount++;
                }
                else {
                    hackerModeCorrectGestureCount = 0;
                    // handle the first gesture in the combination after reset
                    if (eventName == hackerModeCorrectGestureCombination[hackerModeCorrectGestureCount]) {
                        hackerModeCorrectGestureCount++;
                    }
                }

                if (hackerModeCorrectGestureCount >= hackerModeCorrectGestureCombination.length) {
                    presenter.enableHackerMode();
                    hackerModeCorrectGestureCount = 0;
                }


                return true;
            }
        });

        root.findViewById(R.id.ivSecretHackerModeEnabler).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return hackerModeGestureDetector.onTouchEvent(event);
            }
        });
    }

    private void setupCurrentSsdfYearSelector(View root) {
        this.ssdfYearsAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)root.findViewById(R.id.sp_custom_year)).setAdapter(this.ssdfYearsAdapter);

        Button btnSetCustomYear = root.findViewById(R.id.btn_set_custom_year);
        btnSetCustomYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner sp = root.findViewById(R.id.sp_custom_year);
                String customYear = sp.getSelectedItem().toString();
                presenter.setCustomYear(customYear);
            }
        });

        CheckBox cbYearFromDatabase = root.findViewById(R.id.cb_year_from_database);
        cbYearFromDatabase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    presenter.setYearFromDatabase();
                }
            }
        });
    }

    private void setupTestNotificationPanel(View root) {
        Button btnToggleNotifPanel = root.findViewById(R.id.btn_toggle_notif);
        btnToggleNotifPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View notifPanel = root.findViewById(R.id.notification_test_panel);
                if (notifPanel.getVisibility() == View.VISIBLE) {
                    notifPanel.setVisibility(View.GONE);
                }
                else {
                    notifPanel.setVisibility(View.VISIBLE);
                }
            }
        });

        Button btnCreateTestNotif = root.findViewById(R.id.btn_create_test_notification);
        btnCreateTestNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = "test";
                String name = "Test notification";
                DatePicker datePicker = root.findViewById(R.id.notif_date_picker);
                TimePicker timePicker = root.findViewById(R.id.notif_time_picker);
                GregorianCalendar dateTime = new GregorianCalendar(TimeZone.getTimeZone("Europe/Sofia"));
                dateTime.set(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());
                long startTime = 0;
                long notifyTime = dateTime.getTimeInMillis() / 1000;
                presenter.createTestNotification(id, name, startTime, notifyTime);
                popupCreator.popup(root.getContext(), getString(R.string.test_notification_created));
            }
        });
    }

    private void setupCurrentTimeTestPanel(View root) {
        Button btnToggleNotifPanel = root.findViewById(R.id.btn_toggle_curtime);
        btnToggleNotifPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View notifPanel = root.findViewById(R.id.current_time_test_panel);
                if (notifPanel.getVisibility() == View.VISIBLE) {
                    notifPanel.setVisibility(View.GONE);
                }
                else {
                    notifPanel.setVisibility(View.VISIBLE);
                }
            }
        });

        Button btnTestCurrentTime = root.findViewById(R.id.btn_test_curtime);
        btnTestCurrentTime.setOnClickListener(v -> this.presenter.notifyCurrentTime());

        Button btnSetTimeOverride = root.findViewById(R.id.btn_set_override);
        btnSetTimeOverride.setOnClickListener(v -> {
            CheckBox cbOverrideTime = this.getView().findViewById(R.id.cb_override_time);
            boolean overrideTime = cbOverrideTime.isChecked();
            CheckBox cbFreezeTime = this.getView().findViewById(R.id.cb_freeze_time);
            boolean overridenTimeFrozen = cbFreezeTime.isChecked();

            DatePicker datePicker = root.findViewById(R.id.curtime_date_picker);
            TimePicker timePicker = root.findViewById(R.id.curtime_time_picker);
            GregorianCalendar dateTime = new GregorianCalendar(TimeZone.getTimeZone("Europe/Sofia"));
            dateTime.set(datePicker.getYear(),
                    datePicker.getMonth(),
                    datePicker.getDayOfMonth(),
                    timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute());

            long overridenTime = dateTime.getTimeInMillis();

            this.presenter.setTimeOverride(overrideTime, overridenTimeFrozen, overridenTime);
        });
    }
}
