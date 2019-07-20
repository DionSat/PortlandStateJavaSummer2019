package edu.pdx.cs410J.dion;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.io.File;
import java.util.regex.Pattern;

/**
 * This class will handle parsing/reading a text file specified on the command line argument.
 * It is the child of the AppointmentBookParser
 */
public class TextParser implements AppointmentBookParser {
    private String fileName;
    String owner = null;
    public AppointmentBook appointmentBook = new AppointmentBook(null);

    /**
     * This is a constructor for the TextParser class
     * @param fileName
     *        File path name
     */
    public TextParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Function to parse the text file specified by the fileName.
     * @return
     *        returns an appointmentBook of appointments from the text file.
     * @throws
     *        ParserException
     */
    @Override
    public AbstractAppointmentBook parse() throws ParserException {
        BufferedReader br = null;
        File file = new File(fileName);
        if(!file.exists() && fileName.endsWith(".txt")) {
            System.err.println("Error cannot find file specified on path or filepath has no specified text file!");
            appointmentBook = null;
        }
        else {
            try {
                br = new BufferedReader(new FileReader(this.fileName));
                String line = null;
                while ((line = br.readLine()) != null) {
                    String[] app = line.split(";");
                    appointmentBook.setOwnerName(app[0]);
                    if(checkFormat(app[2]) && checkFormat(app[3])) {
                        Appointment appointment = new Appointment(app[1], app[2], app[3]);
                        appointmentBook.addAppointment(appointment);
                    }
                    else {
                        System.err.println("Date/Time is not in right format in text file! Ex: MM/DD/YYYY hh:mm am/pm");
                        break;
                    }
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

    /**
     * This function is the same function used in the project 2 class except it is used to check whether the text files
     * data/time is in the right format.
     * @param dateTime
     * @return  returns isValid as true is its in the correct format or false if not.
     */
    private static boolean checkFormat(String dateTime) {
        String regEx = "^([0]\\d|[1][0-2])\\/([0-2]\\d|[3][0-1])\\/([2][01]|[1][6-9])\\d{2}(\\s([0-1]\\d|[2][0-3])(\\:[0-5]\\d){1,2})?$";
        boolean isValid = false;
        Pattern p = Pattern.compile(regEx);
        if(p.matcher(dateTime).find()) {
            isValid = true;
        }
        return isValid;
    }
}
