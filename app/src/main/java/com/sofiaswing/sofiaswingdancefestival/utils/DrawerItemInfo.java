package com.sofiaswing.sofiaswingdancefestival.utils;

/**
 * Created by shwangshwing on 10/8/17.
 */

public class DrawerItemInfo {
    private final int id;
    private final String displayName;

    public DrawerItemInfo(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }
}