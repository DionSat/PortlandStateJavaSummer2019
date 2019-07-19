package edu.pdx.cs410J.dion;

import edu.pdx.cs410J.AbstractAppointment;

import java.util.Date;

/**
 * This class will create an appointment and store each component the appointment
 * It is a child of the AbstractAppointment Class
 */
public class Appointment extends AbstractAppointment {
  private String beginTime;
  private String endTime;
  private String description;
  private Date beginDay;
  private Date endDay;

  /**
   * Function to instantiate the Appointment class
   * @param newDescription
   *        the description passed in
   * @param newBeginTime
   *        the beginning time passed in
   * @param newEndTime
   *        the ending time passed in
   */
  public Appointment(String newDescription, String newBeginTime, String newEndTime, Date newBeginDay, Date newEndDay) {
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
    return beginTime;
  }

  public void setBeginTimeString(int beginTime, int beginDay) {
    this.beginTime = beginTime + " " + beginDay;
  }

  /**
   * This function concatenates ending time and the endDay(am/pm) and returns the resultant string
   * @return
   */
  @Override
  public String getEndTimeString() {
    return endTime;
  }

  @Override
  public  Date getEndTime() {
    return beginDay;
  }

  @Override
  public Date getBeginTime() {
    return endDay;
  }

  public void setEndTimeString(int endTime, int endDay) {
    this.endTime = endTime + " " + endDay;
  }

  /**
   * This function gets the description of the appointment
   * @return
   */
  @Override
  public String getDescription() {
    return description;
  }
}
