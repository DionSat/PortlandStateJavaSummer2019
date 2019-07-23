package edu.pdx.cs410J.dion;

import edu.pdx.cs410J.AbstractAppointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class will create an appointment and store each component the appointment
 * It is a child of the AbstractAppointment Class
 */
public class Appointment extends AbstractAppointment {
  private Date beginDate;
  private Date endDate;
  private String description;

  /**
   * Function to instantiate the Appointment class
   * @param newDescription
   *        the description passed in
   * @param beginDate
   *        the beginning time passed in
   * @param endDate
   *        the ending time passed in
   */
  public Appointment(String newDescription, String beginDate, String endDate) {
    this.description = newDescription;

    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyy hh:mm a");
    try {
      this.beginDate = df.parse(beginDate.trim());
    } catch (ParseException e) {
      e.printStackTrace();
    }
    try {
      this.endDate = df.parse(endDate.trim());
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public Appointment(String newDescription, Date beginDate, Date endDate) {
    this.description = newDescription;
    this.beginDate = beginDate;
    //this.beginDay = newBeginDay;
    this.endDate = endDate;
    //this.endDay = newEndDay;
  }

  /**
   * This function concatenates beginning time and the beginDay(am/pm) and returns the resultant string
   * @return
   */
  @Override
  public String getBeginTimeString() {
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyy hh:mm a");
    return df.format(beginDate);
  }

  /**
   * This function concatenates ending time and the endDay(am/pm) and returns the resultant string
   * @return
   */
  @Override
  public String getEndTimeString() {
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyy hh:mm a");
    return df.format(endDate);
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

  @Override
  public  Date getEndTime() {
    return endDate;
  }

  @Override
  public Date getBeginTime() {
    return beginDate;
  }
}

