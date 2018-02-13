package com.sofiaswing.sofiaswingdancefestival.ui;

import android.app.Activity;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.utils.DrawerItemInfo;

import java.util.ArrayList;

/**
 * Created by shwangshwing on 10/8/17.
 */

public class DrawerNavigationFactory implements UiInterfaces.IDrawerNavigationFactory {

    public Drawer setUpDrawer(UiInterfaces.INavigationActivity activity) {
        Toolbar toolbar = ((Activity)activity).findViewById(R.id.toolbar);
        ArrayList<PrimaryDrawerItem> items = new ArrayList<PrimaryDrawerItem>();
        for (DrawerItemInfo itemInfo : activity.getNavigationItems()) {
            items.add(new PrimaryDrawerItem()
                    .withIdentifier(itemInfo.getId())
                    .withName(itemInfo.getDisplayName()));
        }
        Drawer drawer = new DrawerBuilder()
                .withActivity((Activity) activity)
                .withToolbar(toolbar)
                .withDrawerItems(new ArrayList<IDrawerItem>(items))
                .withOnDrawerItemClickListener(activity.getNavigationItemClickListener())
                .withSelectedItem(0)
                .build();
        return drawer;
    }
}
