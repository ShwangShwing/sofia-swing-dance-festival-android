package com.sofiaswing.sofiaswingdancefestival.views.parties;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.ui.DrawerNavigationFragmentFactory;
import com.sofiaswing.sofiaswingdancefestival.ui.TitleFragmentAttacher;
import com.sofiaswing.sofiaswingdancefestival.views.news.NewsInterfaces;

import javax.inject.Inject;

public class PartiesActivity extends AppCompatActivity {
    @Inject
    PartiesInterfaces.IPresenter presenter;
    @Inject
    TitleFragmentAttacher titleFragmentAttacher;
    @Inject
    public DrawerNavigationFragmentFactory drawerNavigationFragmentFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parties);

        this.inject();

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragment, (Fragment) this.presenter.getView())
                .replace(R.id.container_title, this.titleFragmentAttacher.getTitleFragment(this.getString(R.string.parties)))
                .replace(R.id.container_drawer_navigation,
                        this.drawerNavigationFragmentFactory.getNavigationFragment(this))
                .commit();
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getApplication())
                .getComponent().inject(this);
    }
}
