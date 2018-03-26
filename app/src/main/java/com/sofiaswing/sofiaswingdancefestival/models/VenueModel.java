package com.sofiaswing.sofiaswingdancefestival.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class VenueModel implements Parcelable {
    final String id;
    final String name;
    final String address;
    final Location location;

    public VenueModel(String id, String name, String address, Location location) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Location getLocation() {
        return location;
    }

    protected VenueModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<VenueModel> CREATOR = new Creator<VenueModel>() {
        @Override
        public VenueModel createFromParcel(Parcel in) {
            return new VenueModel(in);
        }

        @Override
        public VenueModel[] newArray(int size) {
            return new VenueModel[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeParcelable(location, flags);
    }
}
