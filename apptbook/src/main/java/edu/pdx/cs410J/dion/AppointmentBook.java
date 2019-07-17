package edu.pdx.cs410J.dion;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class will create Collection of appointment books and store each appointment
 * It is a child of the AbstractAppointmentBook Class
 */
public class AppointmentBook extends AbstractAppointmentBook {
    private String owner;
    private Collection<Appointment> appointments = new ArrayList<>();

    /**
     * Function to instantiate the AppointmentBook class
     * @param owner
     *          The owner of the appointment
     */
    public AppointmentBook(String owner) {
        this.owner = owner;
    }

    /**
     * Function to return the owner of the appointment
     * @return return the owner string
     */
    @Override
    public String getOwnerName() {
        return owner;
    }

    public void setOwnerName(String owner) {
        this.owner = owner;
    }

    /**
     * Function to return the appointment
     * @return returns appointment object
     */
    @Override
    public Collection getAppointments() {
        return appointments;
    }

    /**
     * This function adds an appointment to the appointmentbook
     * @param newAppointment
     *          The new appointment to be added to the book
     */
    @Override
    public void addAppointment(AbstractAppointment newAppointment) {
        appointments.add((Appointment) newAppointment);
    }
}
