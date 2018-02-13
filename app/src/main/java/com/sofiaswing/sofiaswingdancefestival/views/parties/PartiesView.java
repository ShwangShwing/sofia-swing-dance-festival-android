package com.sofiaswing.sofiaswingdancefestival.views.parties;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.models.PartyModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartiesView extends Fragment implements PartiesInterfaces.IView {
    @Inject
    public PartiesInterfaces.IPresenter presenter;

    private ArrayAdapter<PartyViewModel> partiesAdapter;

    public static PartiesView newInstance() {
        PartiesView fragment = new PartiesView();
        return fragment;
    }

    public PartiesView() {
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
        final View root = inflater.inflate(R.layout.fragment_parties_view, container, false);

        this.setRetainInstance(true);

        ListView lvParties = root.findViewById(R.id.lvPartySchedule);
        this.partiesAdapter = new PartiesAdapter(root.getContext(), android.R.layout.simple_list_item_1);
        lvParties.setAdapter(this.partiesAdapter);
        lvParties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PartyViewModel partyItem = partiesAdapter.getItem(position);

                if (partyItem.isSubscribed()) {
                    presenter.setPartySubscription(position, false);
                    root.findViewById(R.id.tvIsSubscribed).setVisibility(View.GONE);
                }
                else {
                    presenter.setPartySubscription(position, true);
                    root.findViewById(R.id.tvIsSubscribed).setVisibility(View.VISIBLE);
                }
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.presenter.stop();
    }

    @Override
    public void setParties(List<PartyViewModel> parties) {
        partiesAdapter.clear();
        partiesAdapter.addAll(parties);
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getActivity().getApplication())
                .getComponent()
                .inject(this);
    }

    private class PartiesAdapter extends ArrayAdapter<PartyViewModel> {

        public PartiesAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View partyRow = convertView;

            if (partyRow == null) {
                LayoutInflater inflater;
                inflater = LayoutInflater.from(this.getContext());
                partyRow = inflater.inflate(R.layout.layout_party_row, null);
            }

            PartyViewModel partyItem = getItem(position);

            DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.getDefault());

            ((TextView) partyRow.findViewById(R.id.tvTime))
                    .setText(String.format("%s - %s",
                            dateFormatter.format(partyItem.getStartTime()),
                            dateFormatter.format(partyItem.getEndTime())));

            ((TextView) partyRow.findViewById(R.id.tvName))
                    .setText(partyItem.getName());

            ((TextView) partyRow.findViewById(R.id.tvVenue))
                    .setText(partyItem.getVenue().getName());

            if (partyItem.isSubscribed()) {
                partyRow.findViewById(R.id.tvIsSubscribed).setVisibility(View.VISIBLE);
            }
            else {
                partyRow.findViewById(R.id.tvIsSubscribed).setVisibility(View.GONE);
            }

            return partyRow;
        }
    }
}
