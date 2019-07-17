package edu.pdx.cs410J.dion;
import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;
import java.io.*;
import java.util.*;
import java.io.File;

public class TextDumper implements AppointmentBookDumper {
    private String filename;
    FileOutputStream os = null;

    public TextDumper(String filename) {
        this.filename = filename;
    }

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
