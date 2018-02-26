package com.sofiaswing.sofiaswingdancefestival.views.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.ui.UiInterfaces;
import com.sofiaswing.sofiaswingdancefestival.utils.DrawerItemInfo;
import com.sofiaswing.sofiaswingdancefestival.utils.EventSubscriptionAlarmReceiver;
import com.sofiaswing.sofiaswingdancefestival.views.about.AboutView;
import com.sofiaswing.sofiaswingdancefestival.views.classes.ClassesView;
import com.sofiaswing.sofiaswingdancefestival.views.contactUs.ContactUsView;
import com.sofiaswing.sofiaswingdancefestival.views.instructors.InstructorsView;
import com.sofiaswing.sofiaswingdancefestival.views.news.NewsView;
import com.sofiaswing.sofiaswingdancefestival.views.parties.PartiesView;
import com.sofiaswing.sofiaswingdancefestival.views.settings.SettingsView;
import com.sofiaswing.sofiaswingdancefestival.views.venues.VenuesView;

import java.util.ArrayList;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements UiInterfaces.INavigationActivity {

    private static final String CURRENT_TITLE = "current_title_key";
    private static final String CURRENT_SELECTION = "current_selection";
    @Inject
    public UiInterfaces.IDrawerNavigationFactory drawerNavigationFactory;

    private Drawer drawer;

    private String currentTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.inject();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = drawerNavigationFactory.setUpDrawer(this);

        if (savedInstanceState == null) {
            currentTitle = getString(R.string.news);
            setContentView(NewsView.newInstance(), currentTitle);
        } else {
            currentTitle = savedInstanceState.getString(CURRENT_TITLE);
            setTitle(currentTitle);
            drawer.setSelectionAtPosition(savedInstanceState.getInt(CURRENT_SELECTION));
        }

        this.handleIntent(getIntent());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_TITLE, currentTitle);
        outState.putInt(CURRENT_SELECTION, drawer.getCurrentSelectedPosition());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent.hasExtra(EventSubscriptionAlarmReceiver.EVENT_ID_KEY)) {
            setContentView(ClassesView.newInstance(), getString(R.string.classes));
            drawer.setSelectionAtPosition(2);
        }
        // delete extras, because they are already handled
        intent.replaceExtras(new Bundle());
    }

    private void setContentView(Fragment fragment, String title) {
        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_container, fragment)
                .commit();
        setTitle(title);
        currentTitle = title;
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getApplication())
                .getComponent()
                .inject(this);
    }

    @Override
    public ArrayList<DrawerItemInfo> getNavigationItems() {
        ArrayList<DrawerItemInfo> items = new ArrayList<>();

        items.add(new DrawerItemInfo(1, getString(R.string.news)));
        items.add(new DrawerItemInfo(2, getString(R.string.instructors)));
        items.add(new DrawerItemInfo(3, getString(R.string.classes)));
        items.add(new DrawerItemInfo(4, getString(R.string.parties)));
        //items.add(new DrawerItemInfo(5, activity.getString(R.string.schedule)));
        items.add(new DrawerItemInfo(6, getString(R.string.venues)));
        //items.add(new DrawerItemInfo(7, activity.getString(R.string.map)));
        items.add(new DrawerItemInfo(8, getString(R.string.contacts)));
        items.add(new DrawerItemInfo(9, getString(R.string.settings)));
        items.add(new DrawerItemInfo(10, getString(R.string.about)));

        return items;
    }

    @Override
    public Drawer.OnDrawerItemClickListener getNavigationItemClickListener() {
        return new Drawer.OnDrawerItemClickListener() {

            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                switch ((int) drawerItem.getIdentifier()) {
                    case 1:
                        setContentView(NewsView.newInstance(), getString(R.string.news));
                        break;
                    case 2:
                        setContentView(InstructorsView.newInstance(), getString(R.string.instructors));
                        break;
                    case 3:
                        setContentView(ClassesView.newInstance(), getString(R.string.classes));
                        break;
                    case 4:
                        setContentView(PartiesView.newInstance(), getString(R.string.parties));
                        break;
                    case 6:
                        setContentView(VenuesView.newInstance(), getString(R.string.venues));
                        break;
                    case 8:
                        setContentView(ContactUsView.newInstance(), getString(R.string.contacts));
                        break;
                    case 9:
                        setContentView(SettingsView.newInstance(), getString(R.string.settings));
                        break;
                    case 10:
                        setContentView(AboutView.newInstance(), getString(R.string.about));
                        break;
                    default:
                        return false;
                }
                drawer.closeDrawer();
                return true;
            }
        };
    }
}
