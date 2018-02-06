package com.sofiaswing.sofiaswingdancefestival.views.venues;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class VenuesView extends Fragment
        implements VenuesInterfaces.IView {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 666;

    private VenuesInterfaces.IPresenter presenter;
    private ArrayAdapter<VenueModel> venuesAdapter;
    private ProvidersInterfaces.ILocationProvider locationProvider;
    private boolean hasLocationPermission;
    private CompositeDisposable locationSubscriptions;
    private boolean hasAskedForPermissions;

    public VenuesView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_venues_view, container, false);

        this.setRetainInstance(true);

        this.hasLocationPermission = false;
        this.hasAskedForPermissions = false;

        this.locationSubscriptions = new CompositeDisposable();

        ListView lvVenues = root.findViewById(R.id.lvVenues);
        this.venuesAdapter = new VenuesAdapter(root.getContext(), android.R.layout.simple_list_item_1);
        lvVenues.setAdapter(this.venuesAdapter);

        final Fragment thisFragment = this;
        lvVenues.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.checkAndAskPermission();
        this.presenter.start();
        this.locationProvider.startLocationService(this.getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        this.locationProvider.stopLocationService();
        this.presenter.stop();
        this.locationSubscriptions.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void setPresenter(VenuesInterfaces.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setVenues(List<VenueModel> venues) {
        this.venuesAdapter.clear();
        this.venuesAdapter.addAll(venues);
    }

    @Override
    public void setLocationProvider(ProvidersInterfaces.ILocationProvider locationProvider) {
        this.locationProvider = locationProvider;
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

            if (hasLocationPermission && venue.getLocation() != null) {
                final View finalVenueRow = venueRow;
                Disposable d = locationProvider.getCurrentLocation()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Location>() {
                            @Override
                            public void accept(Location currentLocation) throws Exception {
                                float distanceInMeters = currentLocation.distanceTo(venue.getLocation());
                                String formatedDistance = String.format("%.0fm", distanceInMeters);
                                if (distanceInMeters >= 1000) {
                                    formatedDistance = String.format("%.1fkm", distanceInMeters / 1000);
                                }

                                TextView tvDistance = finalVenueRow.findViewById(R.id.tvDistance);
                                tvDistance.setText(String.format("%s %s", getString(R.string.distance), formatedDistance));
                                tvDistance.setVisibility(View.VISIBLE);
                            }
                        });
                locationSubscriptions.add(d);
            }

            return venueRow;
        }

        @Override
        public void clear() {
            super.clear();
            locationSubscriptions.clear();
        }
    }
}
