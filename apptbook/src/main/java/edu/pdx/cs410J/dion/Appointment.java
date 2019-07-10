package edu.pdx.cs410J.dion;

import edu.pdx.cs410J.AbstractAppointment;

/**
 * This class will create an appointment and store each component the appointment
 * It is a child of the AbstractAppointment Class
 */
public class Appointment extends AbstractAppointment {
  private String beginTime;
  private String endTime;
  private String description;
  private String beginDay;
  private String endDay;

  /**
   * Function to instantiate the Appointment class
   * @param newDescription
   *        the description passed in
   * @param newBeginTime
   *        the beginning time passed in
   * @param newEndTime
   *        the ending time passed in
   * @param newBeginDay
   *        whether the beginning time is am/pm
   * @param newEndDay
   *        whether the beginning time is am/pm
   */
  public Appointment(String newDescription, String newBeginTime, String newEndTime, String newBeginDay, String newEndDay) {
    this.description = newDescription;
    this.beginTime = newBeginTime;
    this.beginDay = newBeginDay;
    this.endTime = newEndTime;
    this.endDay = newEndDay;
  }

  /**
   * This function concatenates beginning time and the beginDay(am/pm) and returns the resultant string
   * @return
   */
  @Override
  public String getBeginTimeString() {
    //throw new UnsupportedOperationException("This method is not implemented yet");
    return beginTime + " " + beginDay;
  }

  /**
   * This function concatenates ending time and the endDay(am/pm) and returns the resultant string
   * @return
   */
  @Override
  public String getEndTimeString() {
    //throw new UnsupportedOperationException("This method is not implemented yet");
    return endTime + " " + endDay;
  }

  /**
   * This function gets the description of the appointment
   * @return
   */
  @Override
  public String getDescription() {
    //return "This method is not implemented yet";
    return description;
  }
}
