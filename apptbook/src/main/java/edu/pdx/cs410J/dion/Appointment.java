package edu.pdx.cs410J.dion;

import edu.pdx.cs410J.AbstractAppointment;

public class Appointment extends AbstractAppointment {
  private String beginTime;
  private String endTime;
  private String description;

  public Appointment(String newDescription, String newBeginTime, String newEndTime) {
    this.description = newDescription;
    this.beginTime = newBeginTime;
    this.endTime = newEndTime;
  }

  @Override
  public String getBeginTimeString() {
    //throw new UnsupportedOperationException("This method is not implemented yet");
    return beginTime;
  }

  @Override
  public String getEndTimeString() {
    //throw new UnsupportedOperationException("This method is not implemented yet");
    return endTime;
  }

  @Override
  public String getDescription() {
    //return "This method is not implemented yet";
    return description;
  }
}
