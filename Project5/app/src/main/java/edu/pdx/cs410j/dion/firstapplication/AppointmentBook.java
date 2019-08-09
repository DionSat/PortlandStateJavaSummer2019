package edu.pdx.cs410j.dion.firstapplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This class will create Collection of appointment books and store each appointment
 * It is a child of the AbstractAppointmentBook Class
 */
public class AppointmentBook extends AbstractAppointmentBook {
    private String owner;
    private ArrayList<Appointment> appointments = new ArrayList<>();

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
    public ArrayList getAppointments() {
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

    /**
     * This function uses the built in collections.sort() to sort the appointmentbook
     * @return AppointmentBook
     *         the sort of appointments by start, then end, then description
     *
     */
    public AppointmentBook sort() {
        Collections.sort(appointments);
        return this;
    }
}