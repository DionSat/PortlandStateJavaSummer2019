package edu.pdx.cs410J.dion;
import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;
import java.io.*;
import java.util.*;
import java.io.File;

/**
 * This class will handle dumping/writing to a text file specified on the command line argument.
 * It is the child of the AppointmentBookDumper
 */
public class TextDumper implements AppointmentBookDumper {
    private String filename;
    private FileOutputStream os = null;

    /**
     * This is a constructor for the TextParser class
     * @param filename
     *        File path name
     */
    TextDumper(String filename) {
        this.filename = filename;
    }

    /**
     * Function to write to the text file specified by the fileName.
     * @return  returns an appointmentBook of appointments from the text file.
     * @param appointment
     *        an appointment
     */
    @Override
    public void dump(AbstractAppointmentBook appointment) {
        String content;
        try {
            File file = new File(filename);
            os = new FileOutputStream(file, false);
            Collection<Appointment> appointments = appointment.getAppointments();

            if(!file.exists()) {
                file.createNewFile();
            }

            for(Appointment app : appointments) {
                content = appointment.getOwnerName() + ";" + app.getDescription() + ";" + app.getBeginTimeString() + ";" + app.getEndTimeString() + "\n";
                byte[] bytesArray = content.getBytes();
                os.write(bytesArray);
                os.flush();
                System.out.println("Write Successful");
            }
        } catch(IOException e) {e.printStackTrace();}
        finally {
            try {
                if (os != null)
                {
                    os.close();
                }
            }
            catch (IOException e) {
                System.out.println("Error in closing the Stream");
            }
        }

    }
}
