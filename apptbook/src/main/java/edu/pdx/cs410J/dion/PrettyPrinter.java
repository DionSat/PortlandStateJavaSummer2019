package edu.pdx.cs410J.dion;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private FileOutputStream os = null;

    /**
     * Function to instantiate the PrettyPrinter class
     * @param filename
     *        The file path to the txt file
     */
    public PrettyPrinter(String filename){
        this.filename = filename;
    }

    /**
     * Function to pretty print to the text file.
     * @param abstractAppointmentBook
     *        appointmentBook to be dumped
     */
    @Override
    public void dump(AbstractAppointmentBook abstractAppointmentBook) {
        String content;
        try {
            File file = new File(filename);
            os = new FileOutputStream(file, false);
            Collection<Appointment> appointments = abstractAppointmentBook.getAppointments();
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            Date beginDate = null;
            Date endDate = null;

            if(!file.exists()) {
                file.createNewFile();
            }

            content = "--- Appointment Book ---\n\n Owner Name: " + abstractAppointmentBook.getOwnerName() + "\n\n-------------------------------------------\nAppointments:\n";
            byte[] startBytesArray = content.getBytes();
            os.write(startBytesArray);
            os.flush();
            for(Appointment app : appointments) {
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
                int duration = (int) ((endDate.getTime() - beginDate.getTime()) / (1000*60));

                content = "Description: " + app.getDescription() + "\nStart Time: " + app.getBeginTimeString()
                        + "\nEnd Time: " + app.getEndTimeString() + "\nDuration: " + duration + " minutes\n";

                byte[] bytesArray = content.getBytes();
                os.write(bytesArray);
                os.flush();
            }
            System.out.println("Write Successful");
        } catch(IOException e) {e.printStackTrace();}
        finally {
            try {
                if (os != null)
                {
                    content = "\n--- End of Appointments ---\n";
                    byte[] startBytesArray = content.getBytes();
                    os.write(startBytesArray);
                    os.flush();
                    os.close();
                }
            }
            catch (IOException e) {
                System.out.println("Error in closing the Stream");
            }
        }
    }

    /**
     * Function to pretty print to the screen
     * @param abstractAppointmentBook
     *        appointmentBook to be printed to the screen
     */
    public void screendump(AbstractAppointmentBook abstractAppointmentBook) {
        String content;
        try {
            File file = new File(filename);
            os = new FileOutputStream(file, false);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n--- End of Appointments ---\n");
    }
}
