package com.sofiaswing.sofiaswingdancefestival.models;

import java.util.Date;
import java.util.List;

/**
 * Created by shwangshwing on 10/11/17.
 */

public class ClassModel extends EventModel {
    private final String level;
    private final List<String> instructorIds;

    public ClassModel(String id, Date startTime, Date endTime, String level, String name, String description, String venueId, List<String> instructorIds) {
        super(id, startTime, endTime, name, description, venueId, "class_" + level);
        this.level = level;
        this.instructorIds = instructorIds;
    }

    public String getLevel() {
        return level;
    }

    public List<String> getInstructorIds() {
        return instructorIds;
    }
}
