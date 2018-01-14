package com.sofiaswing.sofiaswingdancefestival.views.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.ui.DrawerNavigationFragmentFactory;
import com.sofiaswing.sofiaswingdancefestival.ui.TitleFragmentFactory;
import com.sofiaswing.sofiaswingdancefestival.ui.UiInterfaces;

import javax.inject.Inject;

public class SettingsActivity extends AppCompatActivity {
    @Inject
    public SettingsInterfaces.IPresenter presenter;
    @Inject
    public UiInterfaces.ITitleFragmentFactory titleFragmentFactory;
    @Inject
    public UiInterfaces.IDrawerNavigationFragmentFactory drawerNavigationFragmentFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.inject();

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragment, (Fragment) this.presenter.getView())
                .replace(R.id.container_title, this.titleFragmentFactory.getTitleFragment(this.getString(R.string.settings)))
                .replace(R.id.container_drawer_navigation,
                        this.drawerNavigationFragmentFactory.getNavigationFragment(this))
                .commit();
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getApplication())
                .getComponent().inject(this);
    }
}
