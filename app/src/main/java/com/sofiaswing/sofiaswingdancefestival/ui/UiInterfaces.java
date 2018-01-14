package com.sofiaswing.sofiaswingdancefestival.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by shwangshwing on 10/8/17.
 */

public class UiInterfaces {
    public interface IDrawerNavigationFragmentFactory {
        Fragment getNavigationFragment(final AppCompatActivity activity);
    }

    public interface ITitleFragmentFactory {
        Fragment getTitleFragment(String title);
    }

    public interface IPopupCreator {
        void popup(Context ctx, String text);
    }
}
