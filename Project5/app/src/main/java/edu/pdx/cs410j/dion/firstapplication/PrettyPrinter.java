package edu.pdx.cs410j.dion.firstapplication;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * This function takes an abstractAppointmentBook and pretty prints it or nicely formats it
 * to display all the fields including the appointment session duration. If - command is not given
 * then the appointmentbook will be pretty printed to a text file if the dash command is found then it will
 * be printed to the screen.
 */
public class PrettyPrinter implements AppointmentBookDumper {
    private String filename;
    private PrintWriter os = null;

    /**
     * Function to instantiate the PrettyPrinter class
     * @param filename
     *        The file path to the txt file
     */
    public PrettyPrinter(String filename){
        this.filename = filename;
    }

    /**
     * Function to instantiate the PrettyPrinter class
     * @param os
     *        the output type
     */
    public PrettyPrinter(PrintWriter os){
        this.filename = "apptbook.txt";
        this.os = os;
    }

    //print appointments that START and END in the time range provided to STANDARD OUT
    //these are printed from the appointment book argument provided to the method
    /**
     * Print the appointments within the specified ranges
     * @param aBook, startDate, endDate
     * @throws IOException exception thrown
     */
    public void rangePrint(AbstractAppointmentBook aBook, Date startDate, Date endDate) {
        ArrayList<Appointment> apptList;
        ArrayList<Appointment> sortedList;
        AppointmentBook appointmentBook;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String content;

        if(aBook == null || startDate == null || endDate == null || this.os == null)
            return;

        sortedList = new ArrayList<>();
        appointmentBook = (AppointmentBook)aBook;
        apptList = appointmentBook.getAppointments();

        for(Appointment appt: apptList) {
            if(appt.getBeginTime().after(startDate) || appt.getBeginTime().compareTo(startDate) == 0) {
                if(appt.getEndTime().before(endDate) || appt.getEndTime().compareTo(endDate) == 0)
                    sortedList.add(appt);
            }
        }

        if(sortedList.size() != 0) {
            os.print("====== Appointment Book ======\n\nOwner Name: ");
            os.print(aBook.getOwnerName());
            os.print("\n\n------------------------------\nAppointments:\n");
            for (Appointment appt : sortedList) {
                try {
                    startDate = format.parse(appt.getBeginTimeString());
                } catch (ParseException e) {
                    System.out.println("Date format incorrect");
                }
                try {
                    endDate = format.parse(appt.getEndTimeString());
                } catch (ParseException e) {
                    System.out.println("Date format incorrect");
                }
                int duration = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60));

                content = "Description: " + appt.getDescription() + "\nStart Time: " + appt.getBeginTimeString()
                        + "\nEnd Time: " + appt.getEndTimeString() + "\nDuration: " + duration + " minutes\n";

                os.println(content);
            }
            os.println("\n--- End of Appointments ---\n");
        }
        else
            this.os.println("\n\nThere are NO APPOINTMENTS in the date/time range provided...");
    }

    /**
     * Function to pretty print to the text file.
     * @param abstractAppointmentBook
     *        appointmentBook to be dumped
     */
    @Override
    public void dump(AbstractAppointmentBook abstractAppointmentBook) throws IOException {
        //String content;
        Collection<Appointment> appointments;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date beginDate = null;
        Date endDate = null;
        AppointmentBook apptBook = (AppointmentBook)abstractAppointmentBook;
        appointments = apptBook.getAppointments();
        os.println(apptBook.getOwnerName());

        os.println("====== Appointment Book ======\n\nOwner Name: ");
        os.println(abstractAppointmentBook.getOwnerName());
        os.println("\n\n------------------------------\nAppointments:\n");
        for (Appointment appt : appointments) {
            try {
                beginDate = format.parse(appt.getBeginTimeString());
            } catch (ParseException e) {
                System.out.println("Date format incorrect");
            }
            try {
                endDate = format.parse(appt.getEndTimeString());
            } catch (ParseException e) {
                System.out.println("Date format incorrect");
            }
            int duration = (int) ((endDate.getTime() - beginDate.getTime()) / (1000*60));

            String aline = "Description: " + appt.getDescription() + "\nStart Time: " + appt.getBeginTimeString()
                    + "\nEnd Time: " + appt.getEndTimeString() + "\nDuration: " + duration + " minutes\n";
            os.println(aline);
        }
        os.println("\n--- End of Appointments ---\n");
    }

    /**
     * Function to pretty print to the screen
     * @param abstractAppointmentBook
     *        appointmentBook to be printed to the screen
     */
    public void screendump(AbstractAppointmentBook abstractAppointmentBook) {
        String content;
        Collection<Appointment> appointments = abstractAppointmentBook.getAppointments();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date beginDate = null;
        Date endDate = null;

        System.out.print("====== Appointment Book ======\n\nOwner Name: ");
        System.out.print(abstractAppointmentBook.getOwnerName());
        System.out.print("\n\n------------------------------\nAppointments:\n");
        for (Appointment app : appointments) {
            try {
                beginDate = format.parse(app.getBeginTimeString());
            } catch (ParseException e) {
                System.out.println("Date format incorrect");
            }
            try {
                endDate = format.parse(app.getEndTimeString());
            } catch (ParseException e) {
                System.out.println("Date format incorrect");
            }
            int duration = (int) ((endDate.getTime() - beginDate.getTime()) / (1000 * 60));

            content = "Description: " + app.getDescription() + "\nStart Time: " + app.getBeginTimeString()
                    + "\nEnd Time: " + app.getEndTimeString() + "\nDuration: " + duration + " minutes\n";

            System.out.println(content);
        }
        System.out.println("\n--- End of Appointments ---\n");
    }

    /**
     * prints error message if no owner by that name
     */
    public void noOwnerPrint() {
        if(this.os != null) {
            this.os.println("\n\nThere is NO OWNER by that name yet...");
        }
    }
}