package com.sofiaswing.sofiaswingdancefestival.views.schedule;

import com.sofiaswing.sofiaswingdancefestival.models.EventModel;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;

import java.util.List;
import java.util.Map;

/**
 * Created by shwangshwing on 3/18/18.
 */

public class ScheduleInterfaces {
    public interface IView {
        void setSchedule(
                List<VenueModel> venues,
                List<ScheduleEventViewModel> events,
                Map<String, String> classLevelStrings);
    }

    public interface IPresenter {
        void setView(IView view);
        void start();
        void stop();
    }
}
