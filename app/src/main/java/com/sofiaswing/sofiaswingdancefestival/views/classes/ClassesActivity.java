package com.sofiaswing.sofiaswingdancefestival.views.classes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.ui.DrawerNavigationFragmentFactory;
import com.sofiaswing.sofiaswingdancefestival.ui.TitleFragmentAttacher;

import javax.inject.Inject;

public class ClassesActivity extends AppCompatActivity {
    @Inject
    public DrawerNavigationFragmentFactory drawerNavigationFragmentFactory;
    @Inject
    TitleFragmentAttacher titleFragmentAttacher;
    @Inject
    ClassesInterfaces.IPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        this.inject();

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_drawer_navigation,
                        this.drawerNavigationFragmentFactory.getNavigationFragment(this))
                .commit();
        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragment, (Fragment) this.presenter.getView())
                .replace(R.id.container_title, this.titleFragmentAttacher.getTitleFragment(this.getString(R.string.classes)))
                .commit();
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getApplication())
                .getComponent()
                .inject(this);
    }
}
