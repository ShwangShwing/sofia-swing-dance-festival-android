package com.sofiaswing.sofiaswingdancefestival.ui;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by shwangshwing on 10/8/17.
 */

public class UiInterfaces {
    public interface IDrawerNavigationFragmentAttacher {
        void attachNavigationFragment(final AppCompatActivity activity);
    }

    public interface ITitleFragmentAttacher {
        void attachTitleFragment(final AppCompatActivity activity, String title);
    }
}
