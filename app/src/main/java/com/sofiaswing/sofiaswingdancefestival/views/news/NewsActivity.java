package com.sofiaswing.sofiaswingdancefestival.views.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.ui.DrawerNavigationFragmentAttacher;
import com.sofiaswing.sofiaswingdancefestival.ui.TitleFragmentAttacher;

import javax.inject.Inject;

public class NewsActivity extends AppCompatActivity {
    @Inject
    public DrawerNavigationFragmentAttacher drawerNavigationFragmentAttacher;
    @Inject
    TitleFragmentAttacher titleFragmentAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        this.inject();

        this.drawerNavigationFragmentAttacher.attachNavigationFragment(this);
        this.titleFragmentAttacher.attachTitleFragment(this, this.getString(R.string.news));
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getApplication())
                .getComponent()
                .inject(this);
    }
}
