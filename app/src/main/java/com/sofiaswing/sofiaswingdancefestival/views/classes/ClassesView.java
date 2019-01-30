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

        this.presenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // After the first classes load it is not necessary to load class levels again
        // as they will probably never change during the festival.
        // Since the presenter is started at onActivityCreated(), the tabs will stop loading
        // after the first fragment pause. I want to avoid restarting the presneter in onResume()
        // Restarting the presnenter in onResume causes the current tab to reset when the user
        // switches to another app and then returns.
        this.presenter.stop();
    }

    @Override
    public void setClassesTabs(List<ClassLevelModel> classLevels) {
        ViewPager pager = rootView.findViewById(R.id.tabsPager);
        pager.setAdapter(new TabsNavigationAdapter(
                getChildFragmentManager(),
                classLevels));

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
