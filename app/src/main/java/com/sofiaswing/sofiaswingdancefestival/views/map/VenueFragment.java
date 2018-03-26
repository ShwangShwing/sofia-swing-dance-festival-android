package com.sofiaswing.sofiaswingdancefestival.views.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.squareup.picasso.Picasso;


public class VenueFragment extends Fragment {
  private static final String VENUE_ARGUMENT_KEY = "venueArgumentKey";

  private VenueModel mVenue;

  public VenueFragment() {
    // Required empty public constructor
  }

  public static VenueFragment newInstance(VenueModel venue) {
    VenueFragment fragment = new VenueFragment();
    Bundle args = new Bundle();
    args.putParcelable(VENUE_ARGUMENT_KEY, venue);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      this.mVenue = getArguments().getParcelable(VENUE_ARGUMENT_KEY);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_venue, container, false);
    TextView tvTitle = root.findViewById(R.id.tvTitle);
    ImageView venueImage = root.findViewById(R.id.imgVenue);
    Picasso.with(getContext()).load("https://i0.wp.com/rossiwrites.com/wp-content/uploads/2016/08/NDK-with-the-fountains-Sofia-Bulgaria-www.rossiwrites.com_.jpg").into(venueImage);
    tvTitle.setText(this.mVenue.getName());
    return root;
  }
}
