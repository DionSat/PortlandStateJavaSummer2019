package edu.pdx.cs410J.dion;

import edu.pdx.cs410J.AbstractAppointment;

public class Appointment extends AbstractAppointment {
  private String beginTime;
  private String endTime;
  private String description;
  private String beginDay;
  private String endDay;

  public Appointment(String newDescription, String newBeginTime, String newEndTime, String newBeginDay, String newEndDay) {
    this.description = newDescription;
    this.beginTime = newBeginTime;
    this.beginDay = newBeginDay;
    this.endTime = newEndTime;
    this.endDay = newEndDay;
  }

  @Override
  public String getBeginTimeString() {
    //throw new UnsupportedOperationException("This method is not implemented yet");
    return beginTime + " " + beginDay;
  }

  @Override
  public String getEndTimeString() {
    //throw new UnsupportedOperationException("This method is not implemented yet");
    return endTime + " " + endDay;
  }

  @Override
  public String getDescription() {
    //return "This method is not implemented yet";
    return description;
  }
}
