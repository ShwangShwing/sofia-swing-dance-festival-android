package com.sofiaswing.sofiaswingdancefestival.data.FirebaseData;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by shwangshwing on 3/3/18.
 */

public final class FirebaseHelpers {
    public static String getNodePathFromSnapshot(final DataSnapshot dataSnapshot) {
        final int rootUrlLength = dataSnapshot.getRef().getRoot().toString().length();
        final String nodePath = dataSnapshot.getRef().toString().substring(rootUrlLength + 1);

        return nodePath;
    }
}
