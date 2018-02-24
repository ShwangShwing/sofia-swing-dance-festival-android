package com.sofiaswing.sofiaswingdancefestival.views.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;

import java.util.List;

import javax.inject.Inject;

public class MapsActivity extends FragmentActivity implements MapsInterfaces.IView, OnMapReadyCallback {

  private static final int MY_PERMISSIONS_REQUEST_LOCATION = 123;
  private static final String TAG = "MapsActivity";

  @Inject
  public MapsInterfaces.IPresenter presenter;

  private GoogleMap mMap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    inject();
    presenter.setView(this);

    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
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
    for (VenueModel venue : venues) {
      Location location = venue.getLocation();
      if (location != null) {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(venue.getName())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ssdf_pin))
        );
      }
    }
  }

  private void inject() {
    ((SofiaSwingDanceFestivalApplication) this.getApplication())
            .getComponent()
            .inject(this);
  }
}
