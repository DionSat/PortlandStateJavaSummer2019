package edu.pdx.cs410J.dion;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.AppointmentBookDumper;
import java.io.*;
import java.util.*;
import java.io.File;

public class TextParser implements AppointmentBookParser {
    private String fileName;
    private String owner;
    public AppointmentBook appointmentBook = new AppointmentBook(owner);


    public TextParser(String fileName) {
        this.fileName = fileName;
        owner = null;
    }

    @Override
    public AbstractAppointmentBook parse() throws ParserException {
        String content = null;
        BufferedReader br = null;
        File file = new File(fileName);
        if(!file.exists()) {
            System.err.println("Error cannot find file specified on path!");
            appointmentBook = null;
        }
        else {
            try {
                br = new BufferedReader(new FileReader(this.fileName));
                String line = null;
                while ((line = br.readLine()) != null) {
                    String[] app = line.split(";");
                    appointmentBook.setOwnerName(app[0]);
                    Appointment appointment = new Appointment(app[1], app[2], app[3]);
                    appointmentBook.addAppointment(appointment);
                }
            } catch (FileNotFoundException e) {
                System.err.println("Caught FileNotFoundException: " + e.getMessage());
                throw new RuntimeException(e);
            } catch (IOException e) {
                System.err.println("Caught IOException: " + e.getMessage());
            } finally {
                if (null != br) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return appointmentBook;
    }
}
