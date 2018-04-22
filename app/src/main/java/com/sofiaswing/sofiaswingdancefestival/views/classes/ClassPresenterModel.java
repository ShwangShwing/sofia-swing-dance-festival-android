package com.sofiaswing.sofiaswingdancefestival.views.classes;

import com.sofiaswing.sofiaswingdancefestival.models.ClassModel;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;

import java.util.List;

public class ClassPresenterModel {
  private ClassModel classModel;
  private List<InstructorModel> instructors;
  private VenueModel venue;
  private boolean isSubscribed;

  public ClassPresenterModel(ClassModel classModel, List<InstructorModel> instructors, VenueModel venue, boolean isSubscribed) {
    this.classModel = classModel;
    this.instructors = instructors;
    this.venue = venue;
    this.isSubscribed = isSubscribed;
  }

  public ClassModel getClassModel() {
    return classModel;
  }

  public void setClassModel(ClassModel classModel) {
    this.classModel = classModel;
  }

  public List<InstructorModel> getInstructors() {
    return instructors;
  }

  public void setInstructors(List<InstructorModel> instructors) {
    this.instructors = instructors;
  }

  public VenueModel getVenue() {
    return venue;
  }

  public void setVenue(VenueModel venue) {
    this.venue = venue;
  }

  public boolean isSubscribed() {
    return isSubscribed;
  }

  public void setSubscribed(boolean subscribed) {
    isSubscribed = subscribed;
  }
}
