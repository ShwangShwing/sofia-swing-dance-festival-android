package com.sofiaswing.sofiaswingdancefestival.views.about;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.utils.EventSubscriptionAlarmReceiver;
import com.sofiaswing.sofiaswingdancefestival.views.main.MainActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutView extends Fragment implements AboutInterfaces.IView {

    @Inject
    public AboutInterfaces.IPresenter presenter;

    public static AboutView newInstance() {

        Bundle args = new Bundle();

        AboutView fragment = new AboutView();
        fragment.setArguments(args);
        return fragment;
    }

    public AboutView() {
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
        return inflater.inflate(R.layout.fragment_about_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        this.presenter.start();
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getActivity().getApplication())
                .getComponent()
                .inject(this);
    }
}
