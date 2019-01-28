package com.sofiaswing.sofiaswingdancefestival.views.map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class VenueFragment extends Fragment {
  private static final String VENUE_ARGUMENT_KEY = "venueArgumentKey";

  private VenueModel mVenue;
  private CompositeDisposable subscriptions;

  @Inject
  public ProvidersInterfaces.INetworkImageLoader netImageLoader;

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
    this.subscriptions = new CompositeDisposable();
    this.inject();
  }

  @Override
  public void onPause() {
      super.onPause();
      this.subscriptions.clear();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_venue, container, false);
    TextView tvTitle = root.findViewById(R.id.tvTitle);
    TextView tvAddress = root.findViewById(R.id.tvAddress);
    ImageView venueImage = root.findViewById(R.id.imgVenue);
    this.netImageLoader.getImage(this.mVenue.getImageUrl())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Bitmap>() {
              @Override
              public void onSubscribe(Disposable d) {
                subscriptions.add(d);
              }

              @Override
              public void onNext(Bitmap bitmap) {
                venueImage.setImageBitmap(bitmap);
              }

              @Override
              public void onError(Throwable e) {

              }

              @Override
              public void onComplete() {

              }
            });
    tvTitle.setText(this.mVenue.getName());
    tvAddress.setText(this.mVenue.getAddress());

    View btnYouTube = root.findViewById(R.id.btnYouTube);
    if (this.mVenue.getYouTubeUrl().isEmpty()) {
      btnYouTube.setVisibility(View.GONE);
    }
    btnYouTube.setOnClickListener(v ->
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(this.mVenue.getYouTubeUrl()))));

    View btnNavigateMe = root.findViewById(R.id.btnNavigateMe);
    btnNavigateMe.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW,
            Uri.parse("google.navigation:q=" + this.mVenue.getLocation().getLatitude() + "," +
              this.mVenue.getLocation().getLongitude()))));

    return root;
  }

  private void inject() {
    ((SofiaSwingDanceFestivalApplication) this.getActivity().getApplication())
            .getComponent()
            .inject(this);
  }
}
