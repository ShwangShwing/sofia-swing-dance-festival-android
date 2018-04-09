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
import com.sofiaswing.sofiaswingdancefestival.views.myEvents.MyEventsView;
import com.sofiaswing.sofiaswingdancefestival.views.news.NewsView;
import com.sofiaswing.sofiaswingdancefestival.views.parties.PartiesView;
import com.sofiaswing.sofiaswingdancefestival.views.schedule.ScheduleView;
import com.sofiaswing.sofiaswingdancefestival.views.settings.SettingsView;
import com.sofiaswing.sofiaswingdancefestival.views.venues.VenuesView;

import java.util.ArrayList;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements UiInterfaces.INavigationActivity {

    private static final String CURRENT_TITLE = "current_title_key";
    private static final String CURRENT_SELECTION = "current_selection";

    private static final int MENU_ITEM_NEWS_ID = 1;
    private static final int MENU_ITEM_INSTRUCTORS_ID = 2;
    private static final int MENU_ITEM_CLASSES_ID = 3;
    private static final int MENU_ITEM_PARTIES_ID = 4;
    private static final int MENU_ITEM_MY_EVENTS_ID = 5;
    private static final int MENU_ITEM_SCHEDULE_ID = 6;
    private static final int MENU_ITEM_VENUES_ID = 7;
    private static final int MENU_ITEM_MAP_ID = 8;
    private static final int MENU_ITEM_CONTACTS_ID = 9;
    private static final int MENU_ITEM_SETTINGS_ID = 10;
    private static final int MENU_ITEM_ABOUT_ID = 11;


    @Inject
    public UiInterfaces.IDrawerNavigationFactory drawerNavigationFactory;

    private Drawer drawer;

    private String currentTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
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

        items.add(new DrawerItemInfo(MENU_ITEM_NEWS_ID, getString(R.string.news)));
        items.add(new DrawerItemInfo(MENU_ITEM_INSTRUCTORS_ID, getString(R.string.instructors)));
        items.add(new DrawerItemInfo(MENU_ITEM_CLASSES_ID, getString(R.string.classes)));
        items.add(new DrawerItemInfo(MENU_ITEM_PARTIES_ID, getString(R.string.parties)));
        items.add(new DrawerItemInfo(MENU_ITEM_MY_EVENTS_ID, getString(R.string.my_events)));
        items.add(new DrawerItemInfo(MENU_ITEM_SCHEDULE_ID, getString(R.string.schedule)));
        items.add(new DrawerItemInfo(MENU_ITEM_VENUES_ID, getString(R.string.venues)));
        //items.add(new DrawerItemInfo(MENU_ITEM_MAP_ID, getString(R.string.map)));
        items.add(new DrawerItemInfo(MENU_ITEM_CONTACTS_ID, getString(R.string.contacts)));
        items.add(new DrawerItemInfo(MENU_ITEM_SETTINGS_ID, getString(R.string.settings)));
        items.add(new DrawerItemInfo(MENU_ITEM_ABOUT_ID, getString(R.string.about)));

        return items;
    }

    @Override
    public Drawer.OnDrawerItemClickListener getNavigationItemClickListener() {
        return new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                switch ((int) drawerItem.getIdentifier()) {
                    case MENU_ITEM_NEWS_ID:
                        setContentView(NewsView.newInstance(), getString(R.string.news));
                        break;
                    case MENU_ITEM_INSTRUCTORS_ID:
                        setContentView(InstructorsView.newInstance(), getString(R.string.instructors));
                        break;
                    case MENU_ITEM_CLASSES_ID:
                        setContentView(ClassesView.newInstance(), getString(R.string.classes));
                        break;
                    case MENU_ITEM_PARTIES_ID:
                        setContentView(PartiesView.newInstance(), getString(R.string.parties));
                        break;
                    case MENU_ITEM_MY_EVENTS_ID:
                        setContentView(MyEventsView.newInstance(), getString(R.string.my_events));
                        break;
                    case MENU_ITEM_SCHEDULE_ID:
                        setContentView(ScheduleView.newInstance(), getString(R.string.schedule));
                        break;
                    case MENU_ITEM_VENUES_ID:
                        setContentView(VenuesView.newInstance(), getString(R.string.venues));
                        break;
                    case MENU_ITEM_CONTACTS_ID:
                        setContentView(ContactUsView.newInstance(), getString(R.string.contacts));
                        break;
                    case MENU_ITEM_SETTINGS_ID:
                        setContentView(SettingsView.newInstance(), getString(R.string.settings));
                        break;
                    case MENU_ITEM_ABOUT_ID:
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
