package com.sofiaswing.sofiaswingdancefestival.views.taxiMe;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.sofiaswing.sofiaswingdancefestival.R;

public class TaxiMeView extends Fragment {

  public TaxiMeView() {
    // Required empty public constructor
  }

  public static TaxiMeView newInstance() {
    TaxiMeView fragment = new TaxiMeView();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_taxi_me_view, container, false);

    Button btnGoToApp = root.findViewById(R.id.btnGoToTaxiMe);
    btnGoToApp.setOnClickListener(view -> {
      String packageName = "com.taxime.client";
      Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
      if (intent == null) {
        // Bring user to the market or let them choose an app?
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + packageName));
      }
      if (intent.resolveActivity(getActivity().getPackageManager()) == null) {
        Toast.makeText(getContext(), "Unable to complete this action", Toast.LENGTH_SHORT).show();
        return;
      }
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    });

    root.findViewById(R.id.imgPromoCode).setOnClickListener(view -> {
      ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
      ClipData clip = ClipData.newPlainText("taxi me promo code", "SSDF2018");
      clipboard.setPrimaryClip(clip);
      Toast.makeText(getContext(), "Promo code copied to clipboard", Toast.LENGTH_SHORT).show();
    });

    return root;
  }

}
