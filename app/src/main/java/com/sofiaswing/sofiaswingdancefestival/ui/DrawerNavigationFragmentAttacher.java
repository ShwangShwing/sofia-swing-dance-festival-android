package com.sofiaswing.sofiaswingdancefestival.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.utils.DrawerItemInfo;
import com.sofiaswing.sofiaswingdancefestival.views.news.NewsActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import com.sofiaswing.sofiaswingdancefestival.commonFragments.DrawerNavigationFragment;

/**
 * Created by shwangshwing on 10/8/17.
 */

public class DrawerNavigationFragmentAttacher implements UiInterfaces.IDrawerNavigationFragmentAttacher {
    @Inject
    public DrawerNavigationFragmentAttacher() {
    }

    public void attachNavigationFragment(final AppCompatActivity activity) {
        ArrayList<DrawerItemInfo> items = new ArrayList<>();

        items.add(new DrawerItemInfo(1, activity.getString(R.string.news)));

        Fragment drawerFragment = DrawerNavigationFragment.createFragment(items, new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Intent intent;
                switch ((int) drawerItem.getIdentifier()) {
                    case 1:
                        intent = new Intent(activity, NewsActivity.class);
                        activity.startActivity(intent);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_drawer_navigation, drawerFragment)
                .commit();
    }


}
