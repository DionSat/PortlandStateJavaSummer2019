package edu.pdx.cs410J.dion;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link Appointment} class.
 */
public class AppointmentTest {

    @Ignore
    @Test(expected = UnsupportedOperationException.class)
    public void getBeginTimeStringNeedsToBeImplemented() {
        Appointment appointment = new Appointment("", "", "");
        appointment.getBeginTimeString();
    }

    @Test
    public void initiallyAllAppointmentsHaveTheSameDescription() {
        Appointment appointment = new Appointment(null, "", "");
        assertThat(appointment.getDescription(), equalTo(null));
    }

    @Test
    public void forProject2ItIsOkayIfGetBeginTimeReturnsNull() {
        Appointment appointment = new Appointment(" ", null ,"");
        assertThat(appointment.getBeginTimeString(), is(nullValue()));
    }

    @Test
    public void AppointmentWithDescription() {
        String description = "Description";
        Appointment appointment = new Appointment(description, "", "");
        assertThat(appointment.getDescription(), equalTo(description));
    }

    @Ignore
    @Test
    public void AppointmentWithStartTimeString() {
        String beginTime = "5/2/2014 12:30";
        Appointment appointment = new Appointment("", beginTime, "");
        assertThat(appointment.getBeginTimeString(), containsString(beginTime));
    }

    @Ignore
    @Test
    public void AppointmentWithEndtTimeString() {
        String endTime = "5/2/2014 12:30";
        Appointment appointment = new Appointment("", "", endTime);
        assertThat(appointment.getEndTimeString(), containsString(endTime));
    }
}
