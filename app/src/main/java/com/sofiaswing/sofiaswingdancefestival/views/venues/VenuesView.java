package com.sofiaswing.sofiaswingdancefestival.views.venues;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.views.map.MapsActivity;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class VenuesView extends Fragment implements VenuesInterfaces.IView {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 666;

    @Inject
    public VenuesInterfaces.IPresenter presenter;

    private ArrayAdapter<VenueModel> venuesAdapter;
    private boolean hasLocationPermission;
    private boolean hasAskedForPermissions;

    private Location currentLocation;

    public static VenuesView newInstance() {
        VenuesView fragment = new VenuesView();
        return fragment;
    }

    public VenuesView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject();
        presenter.setView(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_venues_view, container, false);

        this.setRetainInstance(true);

        this.hasLocationPermission = false;
        this.hasAskedForPermissions = false;

        ListView lvVenues = root.findViewById(R.id.lvVenues);
        this.venuesAdapter = new VenuesAdapter(root.getContext(), android.R.layout.simple_list_item_1);
        lvVenues.setAdapter(this.venuesAdapter);
        lvVenues.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getContext(), MapsActivity.class);
            intent.putExtra(MapsActivity.SELECTED_VENUE_INDEX, position);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.checkAndAskPermission();
        this.presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.presenter.stop();
    }

    @Override
    public void setVenues(List<VenueModel> venues) {
        this.venuesAdapter.clear();
        this.venuesAdapter.addAll(venues);
    }

    @Override
    public void setLocation(Location location) {
        this.currentLocation = location;
        this.venuesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.hasLocationPermission = true;
                    this.venuesAdapter.notifyDataSetChanged();
                }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.venues_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_map:
                Intent intent = new Intent(getContext(), MapsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getActivity().getApplication())
                .getComponent()
                .inject(this);
    }

    private void checkAndAskPermission() {

        if (ActivityCompat.checkSelfPermission(
                this.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (!this.hasAskedForPermissions) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                this.hasAskedForPermissions = true;
            }
        }
        else {
            this.hasLocationPermission = true;
        }
    }

    private class VenuesAdapter extends ArrayAdapter<VenueModel> {
        public VenuesAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View venueRow = convertView;

            if (venueRow == null) {
                LayoutInflater inflater;
                inflater = LayoutInflater.from(this.getContext());
                venueRow = inflater.inflate(R.layout.layout_venue_row, null);
            }

            final VenueModel venue = getItem(position);

            ((TextView) venueRow.findViewById(R.id.tvVenueName)).setText(venue.getName());
            ((TextView) venueRow.findViewById(R.id.tvVenueAddress)).setText(venue.getAddress());

            if (hasLocationPermission && venue.getLocation() != null && currentLocation != null) {
                TextView tvDistance = venueRow.findViewById(R.id.tvDistance);
                float distanceInMeters = currentLocation.distanceTo(venue.getLocation());
                String formatedDistance = String.format("%.0fm", distanceInMeters);
                if (distanceInMeters >= 1000) {
                    formatedDistance = String.format("%.1fkm", distanceInMeters / 1000);
                }
                tvDistance.setText(String.format("%s %s", getString(R.string.distance), formatedDistance));
                tvDistance.setVisibility(View.VISIBLE);
            }

            return venueRow;
        }
    }
}
