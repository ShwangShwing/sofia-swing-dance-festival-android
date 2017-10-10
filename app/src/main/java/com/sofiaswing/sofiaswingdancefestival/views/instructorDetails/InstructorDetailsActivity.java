package com.sofiaswing.sofiaswingdancefestival.views.instructorDetails;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;

import javax.inject.Inject;

public class InstructorDetailsActivity extends AppCompatActivity {
    public static final String INSTRUCTOR_ID_KEY = "instructor_id";

    @Inject
    InstructorDetailsInterfaces.IPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_details);

        this.inject();

        Intent intent = getIntent();
        String instructorId = intent.getStringExtra(INSTRUCTOR_ID_KEY);
        this.presenter.setInstructorId(instructorId);

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragment, (Fragment) this.presenter.getView())
                .commit();
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getApplication())
                .getComponent()
                .inject(this);
    }
}
