package com.sofiaswing.sofiaswingdancefestival.data.FirebaseData;

import com.google.firebase.database.FirebaseDatabase;
import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;

public class BrokenDbConnectionFixer implements DataInterfaces.IBrokenDbConnectionFixer {
    @Override
    public void fixBrokenDbConnection() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.goOffline();
        database.goOnline();
    }
}
