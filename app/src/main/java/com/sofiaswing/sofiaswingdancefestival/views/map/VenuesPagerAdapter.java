package com.sofiaswing.sofiaswingdancefestival.views.map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;

import java.util.List;

/**
 * Created by ivo on 24.02.18.
 */

public class VenuesPagerAdapter extends FragmentStatePagerAdapter {

  private List<VenueModel> mVenues;

  public VenuesPagerAdapter(FragmentManager fm, List<VenueModel> venues) {
    super(fm);
    this.mVenues = venues;
  }

  @Override
  public Fragment getItem(int position) {
    return VenueFragment.newInstance(this.mVenues.get(position));
  }

  @Override
  public int getCount() {
    return this.mVenues.size();
  }
}
