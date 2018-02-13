package com.sofiaswing.sofiaswingdancefestival.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.mikepenz.materialdrawer.Drawer;
import com.sofiaswing.sofiaswingdancefestival.utils.DrawerItemInfo;

import java.util.ArrayList;

/**
 * Created by shwangshwing on 10/8/17.
 */

public class UiInterfaces {
    public interface IDrawerNavigationFactory {
        Drawer setUpDrawer(final INavigationActivity activity);
    }

    public interface IPopupCreator {
        void popup(Context ctx, String text);
    }

    public interface INavigationActivity {
        ArrayList<DrawerItemInfo> getNavigationItems();

        Drawer.OnDrawerItemClickListener getNavigationItemClickListener();
    }
}
