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

import com.astuetz.PagerSlidingTabStrip;
import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.models.ClassLevelModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassesView extends Fragment implements ClassesInterfaces.IView {
    private int resumeCount = 0;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (resumeCount <= 0) {
            this.presenter.start();
        }
        resumeCount++;
    }

    @Override
    public void onPause() {
        super.onPause();

        this.presenter.stop();
    }

    @Override
    public void setClassesTabs(final List<ClassLevelModel> classLevels, String defaultClassLevel) {
        ViewPager pager = rootView.findViewById(R.id.tabsPager);
        pager.setAdapter(new TabsNavigationAdapter(
                getChildFragmentManager(),
                classLevels));

        PagerSlidingTabStrip tabs = rootView.findViewById(R.id.tabs);
        tabs.setTextColor(getResources().getColor(R.color.colorTabTitles));
        tabs.setViewPager(pager);

        // select the class level
        int defaultClassIndex = 0;
        for (int i = 0; i < classLevels.size(); i++) {
            if (classLevels.get(i).getId().equals(defaultClassLevel)) {
                defaultClassIndex = i;
                break;
            }
        }
        pager.setCurrentItem(defaultClassIndex);

        pager.clearOnPageChangeListeners();
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                presenter.setDefaultClassLevel(classLevels.get(position).getId());
            }
        });
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getActivity().getApplication())
                .getComponent()
                .inject(this);
    }

    private class TabsNavigationAdapter extends FragmentStatePagerAdapter {
        private final List<ClassLevelModel> classLevels;
        private final ArrayList<Fragment> fragments;

        public TabsNavigationAdapter(FragmentManager fm, List<ClassLevelModel> classLevels) {
            super(fm);
            this.classLevels = new ArrayList<ClassLevelModel>(classLevels);

            this.fragments = new ArrayList<>();
            for (ClassLevelModel classLevel : this.classLevels) {
                Fragment fragment = ClassScheduleFragment.newInstance(classLevel.getId());
                this.fragments.add(fragment);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < classLevels.size()) {
                return this.classLevels.get(position).getName();
            }
            else {
                return "unknown";
            }
        }

        @Override
        public int getCount() {
            return classLevels.size();
        }
    }
}
