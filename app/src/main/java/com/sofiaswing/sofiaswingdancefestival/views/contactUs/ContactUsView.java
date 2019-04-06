package com.sofiaswing.sofiaswingdancefestival.views.contactUs;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Dimension;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.utils.DimensionUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsView extends Fragment implements ContactUsInterfaces.IView {
    private static final int MY_PERMISSIONS_REQUEST_PHONE = 533;

    @Inject
    public ContactUsInterfaces.IPresenter presenter;

    private ArrayAdapter contactsAdapter;
    private String phoneToCallAfterPermissionIsGranted;

    public static ContactUsView newInstance() {
        ContactUsView fragment = new ContactUsView();
        return fragment;
    }

    public ContactUsView() {
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
        View root = inflater.inflate(R.layout.fragment_contact_us_view, container, false);

        this.contactsAdapter = new ContactsAdapter(root.getContext(), android.R.layout.simple_list_item_1);
        ListView lvContacts = root.findViewById(R.id.lvContacts);
        lvContacts.setAdapter(this.contactsAdapter);

        LinearLayout footer = new LinearLayout(getContext());
        footer.setOrientation(LinearLayout.VERTICAL);
        String[] contactLinks = getResources().getStringArray(R.array.contact_links);
        for (String contactLink : contactLinks) {
            TextView tvLink = new TextView(getContext());
            tvLink.setClickable(true);
            tvLink.setText(Html.fromHtml(contactLink));
            tvLink.setMovementMethod(LinkMovementMethod.getInstance());
            tvLink.setTextSize(Dimension.SP, 20);
            int margin = DimensionUtils.dipToPixels(getContext(), 15);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(margin, margin, margin, margin);
            tvLink.setLayoutParams(params);
            footer.addView(tvLink);
        }

        lvContacts.addFooterView(footer);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.presenter.start();
    }

    @Override
    public void setContacts(List<ContactViewModel> contacts) {
        contactsAdapter.clear();
        contactsAdapter.addAll(contacts);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_PHONE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (this.phoneToCallAfterPermissionIsGranted != null) {
                        this.callPhone(this.phoneToCallAfterPermissionIsGranted);
                        this.phoneToCallAfterPermissionIsGranted = null;
                    }
                }
        }
    }

    private void inject() {
        ((SofiaSwingDanceFestivalApplication) this.getActivity().getApplication())
                .getComponent()
                .inject(this);
    }

    private void callPhone(String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                this.phoneToCallAfterPermissionIsGranted = phoneNumber;
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_PHONE);
        }
        else {
            Intent intent = new Intent(Intent.ACTION_CALL);

            intent.setData(Uri.parse(String.format("tel:%s", phoneNumber)));
            getContext().startActivity(intent);
        }
    }

    private void addPhoneToContacts(String name, String phoneNumber) {
        // Creates a new Intent to insert a contact
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        // Sets the MIME type to match the Contacts Provider
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,
                ContactsContract.CommonDataKinds.Phone.TYPE_MAIN);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        startActivity(intent);
    }

    private class ContactsAdapter extends ArrayAdapter<ContactViewModel> {
        public ContactsAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View contactView = convertView;

            if (contactView == null) {
                LayoutInflater inflater;
                inflater = LayoutInflater.from(this.getContext());
                contactView = inflater.inflate(R.layout.layout_contact_row, null);
            }

            final ContactViewModel contact = getItem(position);

            ((TextView) contactView.findViewById(R.id.tvContactName)).setText(contact.getName());
            ((TextView) contactView.findViewById(R.id.tvContactPhone)).setText(contact.getPhoneNumber());
            ((ImageButton) contactView.findViewById(R.id.ivCall))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        callPhone(contact.getPhoneNumber());
                    }
                });
            ((ImageButton) contactView.findViewById(R.id.ivSaveNumber))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addPhoneToContacts(contact.getName(), contact.getPhoneNumber());
                        }
                    });


            return contactView;
        }
    }
}
