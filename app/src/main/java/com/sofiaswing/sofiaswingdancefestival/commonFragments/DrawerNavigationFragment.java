package com.sofiaswing.sofiaswingdancefestival.commonFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.utils.DrawerItemInfo;

import java.util.ArrayList;

public class DrawerNavigationFragment extends Fragment {
    private Drawer.OnDrawerItemClickListener onDrawerItemClick;
    private ArrayList<DrawerItemInfo> drawerItems;
    private Drawer drawer;
    private int selectedItem;

    public DrawerNavigationFragment() {
        // Required empty public constructor
    }

    public static DrawerNavigationFragment createFragment(
            ArrayList<DrawerItemInfo> drawerItems,
            Drawer.OnDrawerItemClickListener drawerItemClickListerner) {

        DrawerNavigationFragment fragment = new DrawerNavigationFragment();
        fragment.onDrawerItemClick = drawerItemClickListerner;
        fragment.drawerItems = drawerItems;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drawer_navigation, container, false);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.setupDrawer();
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    private void closeDrawer() {
        if (this.drawer != null) {
            this.drawer.closeDrawer();
        }
    }

    private void setupDrawer() {
        ArrayList<PrimaryDrawerItem> items = new ArrayList<PrimaryDrawerItem>();

        for (DrawerItemInfo itemInfo : this.drawerItems) {
            items.add(new PrimaryDrawerItem()
                    .withIdentifier(itemInfo.getId())
                    .withName(itemInfo.getDisplayName()));
        }

        View v = this.getView();
        Toolbar toolbar = (Toolbar) this.getView().findViewById(R.id.drawer_toolbar);

        final Drawer.OnDrawerItemClickListener undecoratedOnClickListener = this.onDrawerItemClick;
        final Drawer.OnDrawerItemClickListener onClickListenerWithClose = new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                drawer.setSelection(selectedItem, false);
                if (undecoratedOnClickListener.onItemClick(view, position, drawerItem)) {
                    closeDrawer();
                    return true;
                }

                return false;
            }
        };

        //create the drawer and remember the `Drawer` result object
        this.drawer = new DrawerBuilder()
                .withActivity(this.getActivity())
                .withToolbar(toolbar)
                .withDrawerItems(new ArrayList<IDrawerItem>(items))
                .withOnDrawerItemClickListener(onClickListenerWithClose)
                .withSelectedItem(this.selectedItem)
                .build();
    }
}
