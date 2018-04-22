package com.sofiaswing.sofiaswingdancefestival.views.classes;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.models.ClassLevelModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassesView extends Fragment implements ClassesInterfaces.IView {

    @Inject
    public ClassesInterfaces.IPresenter presenter;

    private View rootView;

    public static ClassesView newInstance() {
        ClassesView fragment = new ClassesView();
        return fragment;
    }

    public ClassesView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject();
        presenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_classes_view, container, false);

        this.setRetainInstance(true);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        this.presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        this.presenter.stop();
    }

    @Override
    public void setClassesTabs(List<ClassLevelModel> classLevels) {
        ViewPager pager = rootView.findViewById(R.id.tabsPager);
        pager.setAdapter(new TabsNavigationAdapter(
                getChildFragmentManager(),
                classLevels, getString(R.string.taster)));

        PagerSlidingTabStrip tabs = rootView.findViewById(R.id.tabs);
        tabs.setTextColor(getResources().getColor(R.color.md_white_1000));
        tabs.setViewPager(pager);
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getActivity().getApplication())
                .getComponent()
                .inject(this);
    }

    private class TabsNavigationAdapter extends FragmentStatePagerAdapter {
        private final List<ClassLevelModel> classLevels;
        private final String tasterTitle;
        private final ArrayList<Fragment> fragments;
        private final Fragment tasterFramgent;

        public TabsNavigationAdapter(FragmentManager fm, List<ClassLevelModel> classLevels, String tasterTitle) {
            super(fm);
            this.classLevels = new ArrayList<ClassLevelModel>(classLevels);
            this.tasterTitle = tasterTitle;

            this.fragments = new ArrayList<>();
            for (ClassLevelModel classLevel : this.classLevels) {
                Fragment fragment = ClassScheduleFragment.newInstance(classLevel.getId(), false);
                this.fragments.add(fragment);
            }

            this.tasterFramgent = ClassScheduleFragment.newInstance("", true);
        }

        @Override
        public Fragment getItem(int position) {
            if (position < this.fragments.size()) {
                return this.fragments.get(position);
            }
            else {
                return this.tasterFramgent;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < classLevels.size()) {
                return this.classLevels.get(position).getName();
            }
            else {
                return tasterTitle;
            }
        }

        @Override
        public int getCount() {
            return classLevels.size() + 1;
        }
    }
}
