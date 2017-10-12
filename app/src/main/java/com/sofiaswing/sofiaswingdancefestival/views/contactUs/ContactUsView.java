package com.sofiaswing.sofiaswingdancefestival.views.contactUs;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sofiaswing.sofiaswingdancefestival.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsView extends Fragment implements ContactUsInterfaces.IView {
    private static final int MY_PERMISSIONS_REQUEST_PHONE = 533;
    private ContactUsInterfaces.IPresenter presenter;
    private ArrayAdapter contactsAdapter;
    private String phoneToCallAfterPermissionIsGranted;

    public ContactUsView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_contact_us_view, container, false);

        this.contactsAdapter = new ContactsAdapter(root.getContext(), android.R.layout.simple_list_item_1);
        ((ListView) root.findViewById(R.id.lvContacts)).setAdapter(this.contactsAdapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.presenter.start();
    }

    public void setPresenter(ContactUsInterfaces.IPresenter presenter) {
        this.presenter = presenter;
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
