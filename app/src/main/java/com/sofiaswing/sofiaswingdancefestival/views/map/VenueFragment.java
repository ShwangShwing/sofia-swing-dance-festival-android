package com.sofiaswing.sofiaswingdancefestival.views.map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    TextView tvAddress = root.findViewById(R.id.tvAddress);
    ImageView venueImage = root.findViewById(R.id.imgVenue);
    Picasso.with(getContext()).load(this.mVenue.getImageUrl()).into(venueImage);
    tvTitle.setText(this.mVenue.getName());
    tvAddress.setText(this.mVenue.getAddress());

    Button btnYouTube = root.findViewById(R.id.btnYouTube);
    if (this.mVenue.getYouTubeUrl().isEmpty()) {
      btnYouTube.setVisibility(View.GONE);
    }
    btnYouTube.setOnClickListener(v ->
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(this.mVenue.getYouTubeUrl()))));

    Button btnNavigateMe = root.findViewById(R.id.btnNavigateMe);
    btnNavigateMe.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW,
            Uri.parse("google.navigation:q=" + this.mVenue.getLocation().getLatitude() + "," +
              this.mVenue.getLocation().getLongitude()))));

    return root;
  }
}
