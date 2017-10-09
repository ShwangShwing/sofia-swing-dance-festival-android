package com.sofiaswing.sofiaswingdancefestival.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.commonFragments.TitleFragment;

import javax.inject.Inject;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class TitleFragmentAttacher implements UiInterfaces.ITitleFragmentFactory {
    @Inject
    public TitleFragmentAttacher() {
    }

    @Override
    public Fragment getTitleFragment(String title) {
        TitleFragment titleFragment = TitleFragment.createFragment(title);

        return titleFragment;

    }
}
