package edu.pdx.cs410J.dion;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages
{
    public static String missingRequiredParameter( String parameterName )
    {
        return String.format("The required parameter \"%s\" is missing", parameterName);
    }

    public  static String printAppointment ( Appointment aAppointment) {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date beginDateTime = null;
        Date endDateTime = null;

        try {
            beginDateTime = df.parse(aAppointment.getBeginTimeString());
        } catch (ParseException e) {
            System.out.println("Incorrect begin date");
        }
        try {
            endDateTime = df.parse(aAppointment.getEndTimeString());
        } catch (ParseException e) {
            System.out.println("Incorrect end date");
        }

        int duration = (int) ((endDateTime.getTime() - beginDateTime.getTime())
                / (1000*60));

        return String.format( "Appointment with description: %s \nFrom: %s to %s \nDuration: %d\n",
                aAppointment.getDescription(), aAppointment.getBeginTimeString(),
                aAppointment.getEndTimeString(), duration);
    }

    public static String printAppointment (String description, String startTime, String endTime) {

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date beginDateTime = null;
        Date endDateTime = null;

        try {
            beginDateTime = df.parse(startTime);
        } catch (ParseException e) {
            System.out.println("Incorrect begin date");
        }
        try {
            endDateTime = df.parse(endTime);
        } catch (ParseException e) {
            System.out.println("Incorrect end date");
        }

        int duration = (int) ((endDateTime.getTime() - beginDateTime.getTime())
                / (1000*60));

        return String.format("Appointment with description: %s \n From: %s to %s \n Duration: %d\n",
                description, startTime, endTime, duration);
    }
}
