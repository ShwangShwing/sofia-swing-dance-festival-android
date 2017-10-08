package com.sofiaswing.sofiaswingdancefestival.ui;

import android.support.v7.app.AppCompatActivity;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.commonFragments.TitleFragment;

import javax.inject.Inject;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class TitleFragmentAttacher implements UiInterfaces.ITitleFragmentAttacher {
    @Inject
    public TitleFragmentAttacher() {
    }

    @Override
    public void attachTitleFragment(AppCompatActivity activity, String title) {
        TitleFragment titleFragment = TitleFragment.createFragment(title);

        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_title, titleFragment)
                .commit();
    }
}
