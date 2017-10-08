package com.sofiaswing.sofiaswingdancefestival.commonFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TitleFragment extends Fragment {
    private String title;

    public static TitleFragment createFragment(String title) {

        TitleFragment fragment = new TitleFragment();
        fragment.title = title;

        return fragment;
    }

    public TitleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        ((TextView) this.getActivity().findViewById(R.id.tvTitle))
                .setText(this.title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_title, container, false);
    }

}
