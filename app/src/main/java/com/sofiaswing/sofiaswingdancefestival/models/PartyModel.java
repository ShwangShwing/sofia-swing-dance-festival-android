package com.sofiaswing.sofiaswingdancefestival.models;

import java.util.Date;

/**
 * Created by shwangshwing on 10/11/17.
 */

public class PartyModel extends EventModel {
    public PartyModel(String id, Date startTime, Date endTime, String name, String venueId) {
        super(id, startTime, endTime, name, venueId, "party");
    }
}
