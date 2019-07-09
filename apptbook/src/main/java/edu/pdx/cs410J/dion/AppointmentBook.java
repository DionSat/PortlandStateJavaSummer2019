package edu.pdx.cs410J.dion;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;

public class AppointmentBook extends AbstractAppointmentBook {
    private String owner;
    private Collection<Appointment> appointments = new ArrayList<>();

    public AppointmentBook(String owner) {
        this.owner = owner;
    }

    @Override
    public String getOwnerName() {
        return owner;
    }

    @Override
    public Collection getAppointments() {
        return appointments;
    }

    @Override
    public void addAppointment(AbstractAppointment newAppointment) {
        appointments.add((Appointment) newAppointment);
    }
}
