package com.sofiaswing.sofiaswingdancefestival.models;

import java.util.Date;
import java.util.List;

/**
 * Created by shwangshwing on 10/11/17.
 */

public class ClassModel {
    private final String id;
    private final Date startTime;
    private final Date endTime;
    private final String level;
    private final String levelName;
    private final String name;
    private final VenueModel venue;
    private final List<InstructorModel> instructors;

    public ClassModel(String id, Date startTime, Date endTime, String level, String levelName, String name, VenueModel venue, List<InstructorModel> instructors) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.level = level;
        this.levelName = levelName;
        this.name = name;
        this.venue = venue;
        this.instructors = instructors;
    }

    public String getId() {
        return id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getLevel() {
        return level;
    }

    public String getLevelName() {
        return levelName;
    }

    public String getName() {
        return name;
    }

    public VenueModel getVenue() {
        return venue;
    }

    public List<InstructorModel> getInstructors() {
        return instructors;
    }
}
