package com.sofiaswing.sofiaswingdancefestival.models;

import android.support.annotation.NonNull;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class ClassLevelModel implements Comparable<ClassLevelModel> {
    private String id;
    private String name;
    private int position;

    public ClassLevelModel(String id, String name, int position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public int compareTo(@NonNull ClassLevelModel o) {
        return this.getPosition() - o.getPosition();
    }
}
