package com.sofiaswing.sofiaswingdancefestival.views.map;

import android.Manifest;
import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.utils.DimensionUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MapsActivity extends FragmentActivity implements MapsInterfaces.IView, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, ViewPager.OnPageChangeListener {

  public static final String SELECTED_VENUE_INDEX = "venueIndex";

  private static final int MY_PERMISSIONS_REQUEST_LOCATION = 123;
  private static final String TAG = "MapsActivity";

  @Inject
  public MapsInterfaces.IPresenter presenter;

  private GoogleMap mMap;
  private ViewPager mPager;
  private List<VenueModel> mVenues = new ArrayList<>();
  private VenuesPagerAdapter mAdapter;
  private List<Marker> mMarkers = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    inject();
    presenter.setView(this);

    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);

    this.mPager = findViewById(R.id.viewPager);
    this.setPagerBottomMargin(-getResources().getDimensionPixelSize(R.dimen.venue_card_height));
    mAdapter = new VenuesPagerAdapter(getSupportFragmentManager(), this.mVenues);
    this.mPager.setAdapter(mAdapter);
    this.mPager.setPageMargin(DimensionUtils.dipToPixels(this, 20));
    this.mPager.addOnPageChangeListener(this);

    //enable layout animations
    ((FrameLayout)findViewById(R.id.mapsContainer)).getLayoutTransition()
            .enableTransitionType(LayoutTransition.CHANGING);
  }

  @Override
  protected void onResume() {
    super.onResume();
    presenter.start();
  }

  @Override
  protected void onPause() {
    super.onPause();
    presenter.stop();
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;

    try {
      boolean success = googleMap.setMapStyle(
              MapStyleOptions.loadRawResourceStyle(
                      this, R.raw.map_style));

      if (!success) {
        Log.e(TAG, "Style parsing failed.");
      }
    } catch (Resources.NotFoundException e) {
      Log.e(TAG, "Can't find style. Error: ", e);
    }

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
      mMap.setMyLocationEnabled(true);
    } else {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
              MY_PERMISSIONS_REQUEST_LOCATION);
    }

    LatLng sofia = new LatLng(42.692493, 23.322443);
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sofia, 13));
    mMap.setOnMarkerClickListener(this);
    mMap.setOnMapClickListener(this);
    mMap.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.venue_card_height));
  }

  @SuppressLint("MissingPermission")
  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
      if (permissions.length == 1 &&
              permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
              grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        mMap.setMyLocationEnabled(true);
      }
    }
  }

  @Override
  public void setVenues(List<VenueModel> venues) {
    this.mVenues.clear();
    this.mVenues.addAll(venues);
    this.mAdapter.notifyDataSetChanged();

    if (mMap == null) {
      return;
    }

    for (int i = 0; i < venues.size(); i++) {
      VenueModel venue = venues.get(i);
      Location location = venue.getLocation();
      if (location != null) {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(venue.getName())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin))
        );
        marker.setTag(i);
        this.mMarkers.add(marker);
      }
    }

    int selectedVenueIndex = getIntent().getIntExtra(SELECTED_VENUE_INDEX, -1);
    if (selectedVenueIndex != -1) {
      this.mPager.setCurrentItem(selectedVenueIndex, false);
      this.setPagerBottomMargin(0);
      animateToVenueWithPosition(selectedVenueIndex);
      getIntent().putExtra(SELECTED_VENUE_INDEX, -1);
    }
  }

  private void inject() {
    ((SofiaSwingDanceFestivalApplication) this.getApplication())
            .getComponent()
            .inject(this);
  }

  @Override
  public boolean onMarkerClick(Marker marker) {
    this.setPagerBottomMargin(0);
    this.mPager.setCurrentItem((Integer) marker.getTag(), false);
    return false;
  }

  @Override
  public void onMapClick(LatLng latLng) {
    this.setPagerBottomMargin(-getResources().getDimensionPixelSize(R.dimen.venue_card_height));
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

  }

  @Override
  public void onPageSelected(int position) {
    animateToVenueWithPosition(position);
  }

  @Override
  public void onPageScrollStateChanged(int state) {

  }

  private void setPagerBottomMargin(int marginPixels) {
    ViewGroup.MarginLayoutParams pagerParams = (ViewGroup.MarginLayoutParams) this.mPager.getLayoutParams();
    pagerParams.bottomMargin = marginPixels;
    this.mPager.setLayoutParams(pagerParams);
  }

  private void animateToVenueWithPosition(int position) {
    VenueModel venue = mVenues.get(position);
    CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(new LatLng(venue.getLocation().getLatitude(), venue.getLocation().getLongitude()))
            .zoom(15)
            .build();
    this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 300, null);
    this.mMarkers.get(position).showInfoWindow();
  }
}
