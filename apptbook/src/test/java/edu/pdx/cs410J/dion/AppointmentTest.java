package edu.pdx.cs410J.dion;

import org.junit.Ignore;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link Appointment} class.
 */
public class AppointmentTest {

    @Ignore
    @Test(expected = UnsupportedOperationException.class)
    public void getBeginTimeStringNeedsToBeImplemented() throws ParseException {
        Appointment appointment = new Appointment("", "", "");
        appointment.getBeginTimeString();
    }
    @Ignore
    @Test
    public void initiallyAllAppointmentsHaveTheSameDescription() throws ParseException {
        Appointment appointment = new Appointment(null, "", "");
        assertThat(appointment.getDescription(), equalTo(null));
    }
    @Ignore
    @Test
    public void forProject2ItIsOkayIfGetBeginTimeReturnsNull() throws ParseException {
        Appointment appointment = new Appointment(" ", null ,"");
        assertThat(appointment.getBeginTimeString(), is(nullValue()));
    }
    @Ignore
    @Test
    public void AppointmentWithDescription() throws ParseException {
        String description = "Description";
        Appointment appointment = new Appointment(description, "", "");
        assertThat(appointment.getDescription(), equalTo(description));
    }

    @Ignore
    @Test
    public void AppointmentWithStartTimeString() throws ParseException {
        String beginTime = "5/2/2014 12:30";
        Appointment appointment = new Appointment("", beginTime, "");
        assertThat(appointment.getBeginTimeString(), containsString(beginTime));
    }

    @Ignore
    @Test
    public void AppointmentWithEndtTimeString() throws ParseException {
        String endTime = "5/2/2014 12:30";
        Appointment appointment = new Appointment("", "", endTime);
        assertThat(appointment.getEndTimeString(), containsString(endTime));
    }
    @Ignore
    @Test
    public void AppointmentWithStartTime() {
        Date beginTime = new Date();
        Appointment appointment = new Appointment("", beginTime, null);
        assertThat(appointment.getBeginTime(), equalTo(beginTime));
    }
    @Ignore
    @Test
    public void AppointmentWithEndtTime() {
        Date endTime = new Date();
        Appointment appointment = new Appointment("", endTime, endTime);
        assertThat(appointment.getEndTime(), equalTo(endTime));
    }
}
