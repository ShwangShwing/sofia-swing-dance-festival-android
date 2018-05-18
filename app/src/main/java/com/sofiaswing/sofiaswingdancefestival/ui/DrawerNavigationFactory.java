package com.sofiaswing.sofiaswingdancefestival.ui;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
                    .withTextColorRes(R.color.md_white_1000)
                    .withSelectedTextColorRes(R.color.md_white_1000)
                    .withSelectedColorRes(R.color.selectedDrawerBackground)
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
        View backgroundImage = ((Activity)activity).getLayoutInflater().inflate(R.layout.menu_background, drawer.getContent());
        RelativeLayout content = drawer.getSlider();
        content.addView(backgroundImage, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return drawer;
    }
}
