package com.sofiaswing.sofiaswingdancefestival.views.about;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sofiaswing.sofiaswingdancefestival.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutView extends Fragment implements AboutInterfaces.IView {
    private AboutInterfaces.IPresenter presenter;

    public AboutView() {
        // Required empty public constructor
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

    public void setPresenter(AboutInterfaces.IPresenter presenter) {
        this.presenter = presenter;
    }

}
