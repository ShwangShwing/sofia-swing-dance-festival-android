package com.sofiaswing.sofiaswingdancefestival.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.utils.DrawerItemInfo;
import com.sofiaswing.sofiaswingdancefestival.views.classes.ClassesActivity;
import com.sofiaswing.sofiaswingdancefestival.views.instructors.InstructorsActivity;
import com.sofiaswing.sofiaswingdancefestival.views.news.NewsActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import com.sofiaswing.sofiaswingdancefestival.commonFragments.DrawerNavigationFragment;
import com.sofiaswing.sofiaswingdancefestival.views.parties.PartiesActivity;
import com.sofiaswing.sofiaswingdancefestival.views.venues.VenuesActivity;

/**
 * Created by shwangshwing on 10/8/17.
 */

public class DrawerNavigationFragmentFactory implements UiInterfaces.IDrawerNavigationFragmentFactory {
    @Inject
    public DrawerNavigationFragmentFactory() {
    }

    public Fragment getNavigationFragment(final AppCompatActivity activity) {
        ArrayList<DrawerItemInfo> items = new ArrayList<>();

        items.add(new DrawerItemInfo(1, activity.getString(R.string.news)));
        items.add(new DrawerItemInfo(2, activity.getString(R.string.instructors)));
        items.add(new DrawerItemInfo(3, activity.getString(R.string.classes)));
        items.add(new DrawerItemInfo(4, activity.getString(R.string.parties)));
        items.add(new DrawerItemInfo(5, activity.getString(R.string.schedule)));
        items.add(new DrawerItemInfo(6, activity.getString(R.string.venues)));
        items.add(new DrawerItemInfo(7, activity.getString(R.string.map)));
        items.add(new DrawerItemInfo(8, activity.getString(R.string.contacts)));
        items.add(new DrawerItemInfo(9, activity.getString(R.string.settings)));
        items.add(new DrawerItemInfo(10, activity.getString(R.string.about)));

        DrawerNavigationFragment drawerFragment = DrawerNavigationFragment.createFragment(items, new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Intent intent;
                switch ((int) drawerItem.getIdentifier()) {
                    case 1:
                        intent = new Intent(activity, NewsActivity.class);
                        activity.startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(activity, InstructorsActivity.class);
                        activity.startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(activity, ClassesActivity.class);
                        activity.startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(activity, PartiesActivity.class);
                        activity.startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(activity, VenuesActivity.class);
                        activity.startActivity(intent);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        this.setDrawerSelectedElement(activity, drawerFragment);

        return drawerFragment;

    }

    private void setDrawerSelectedElement(Activity activity, DrawerNavigationFragment drawerFragment) {
        if (activity instanceof NewsActivity) {
            drawerFragment.setSelectedItem(1);
        }
        else if (activity instanceof InstructorsActivity) {
            drawerFragment.setSelectedItem(2);
        }
        else if (activity instanceof ClassesActivity) {
            drawerFragment.setSelectedItem(3);
        }
        else if (activity instanceof PartiesActivity) {
            drawerFragment.setSelectedItem(4);
        }
        else if (activity instanceof VenuesActivity) {
            drawerFragment.setSelectedItem(6);
        }
    }


}
