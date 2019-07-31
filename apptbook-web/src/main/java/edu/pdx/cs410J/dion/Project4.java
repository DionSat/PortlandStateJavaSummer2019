package edu.pdx.cs410J.dion;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * The main class for the CS410J appointment book Project.
 * This class handles and implements the command argument parsing and options
 * @author Dion Satcher
 * @version 2.1 7/24/2019
 */
public class Project4 {
    public static final String MISSING_ARGS = "Missing command line arguments";

    public static void main(String[] args) {
        boolean printFlag;
        boolean readMeFlag;
        Date beginDateTime = null;
        Date endDateTime = null;
        boolean validDate = false;
        boolean searchFlag = false;
        boolean serverFlag = true;

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        printFlag = checkPrintOption(args);
        readMeFlag = checkReadMeFileOption(args);

        if(readMeFlag) {
            printReadme();
            System.exit(2);
        }
        String[] cmdArg = parseText(args);
        try {
            beginDateTime = format.parse(cmdArg[4]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            endDateTime = format.parse(cmdArg[5]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!isValidDateRange(beginDateTime, endDateTime, validDate)) {
            System.err.println("Dates are invalid!");
            System.exit(3);
        }

        String stringBegin = format.format(beginDateTime);
        String stringEnd = format.format(endDateTime);

        Appointment appointment = new Appointment(cmdArg[3], beginDateTime, endDateTime);
        AppointmentBook appointmentBook = new AppointmentBook(cmdArg[2]);

        int port;
        try {
            port = Integer.parseInt( cmdArg[1] );

        } catch (NumberFormatException ex) {
            usage("Port \"" + cmdArg[1] + "\" must be an integer");
            return;
        }

        AppointmentBookRestClient client = new AppointmentBookRestClient(cmdArg[0], port);

        HttpRequestHelper.Response response = null;

        if (searchFlag) {
            try {
                response = client.searchAppointments(cmdArg[2], stringBegin, stringEnd);
            } catch (IOException ex) {
                System.out.println("Web connection issue");
                serverFlag = false;
            }
        } else {
            try {
                response = client.addAppointment(cmdArg[2], cmdArg[3], stringBegin, stringEnd);
            } catch (IOException ex) {
                System.out.println("Web connection issue");
                serverFlag = false;
            }
        }

        if(serverFlag) {
            checkResponseCode(HttpURLConnection.HTTP_OK, response);
            System.out.println(response.getContent());
        }

        if (printFlag) {
            System.out.println(appointmentBook.toString());
            System.out.println(appointment.toString());
        }

        System.exit(1);
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }

    /**
     * Makes sure that the give response has the expected HTTP status code
     * @param code The expected status code
     * @param response The response from the server
     */
    private static void checkResponseCode( int code, HttpRequestHelper.Response response )
    {
        if (response.getCode() != code) {
            error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
                    response.getCode(), response.getContent()));
        }
    }

    /**
     * This function checks whether the dates are correctly placed and not misplaced and whether start/end date are switched.
     * @param startDate
     *        The start date of the appointment
     * @param endDate
     *        The end date of the appointment
     * @param equalOK
     *        Boolean flag to check whether valid
     * @return
     *        return true if both date are valid or false if otherwise
     */
    public static boolean isValidDateRange(Date startDate, Date endDate, boolean equalOK) {
        // false if either value is null
        if (startDate == null || endDate == null) { return false; }

        if (equalOK) {
            // true if they are equal
            if (startDate.equals(endDate)) { return true; }
        }

        // true if endDate after startDate
        if (endDate.after(startDate)) { return true; }

        return false;
    }

    /**
     * A void function that prints the read me when called
     * @return there is no return type
     */
    static void printReadme() {
        System.out.println("\n\n");
        System.out.println("Project 1 README Dion Satcher, CS410J " +
                "\nThis program will allow user to enter appointment information" +
                "\non the command line, then saves the information. This program" +
                "\n will allow you to write that information to a text file." +
                "\n---------------" +
                "\nusage: java edu.pdx.edu.cs410J.<login-id>.Project1 [options] <args>" +
                "\nargs are (in this order):" +
                "owner - The person whose owns the appt book\n" +
                "description - A non-blank description of the appointment\n" +
                "beginTime - (date AND time) When the appt begins (12-hour time)\n" +
                "endTime - (date AND time) When the appt ends (12-hour time)\n" +
                "  options are (options may appear in any order):\n" +
                "    -pretty                  file Pretty print the appointment book to\n" +
                "                             a text file or standard out (file -)\n" +
                "    -print                   Prints a description of the new appointment\n" +
                "    -README                  Prints a README for this project and exits\n" +
                "    -textFile                Where to read/write the appointment book\n" +
                "  Date and time should be in the format: mm/dd/yyyy hh:mm am/pm (or mm/dd/yy hh:mm am/pm)\n" +
                "\n***END OF README***\n");
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project4 host port [owner] [descriptio] [Begin Time] [End Time]");
        err.println("  host    Host of web server");
        err.println("  port    Port of web server");
        err.println("  owner             - The person whose owns the appt book");
        err.println("  description       - A description of the appointment");
        err.println("  beginTime         - When the appt begins (12-hour time)");
        err.println("  endTime           - When the appt ends (12-hour time)");
        err.println();
        err.println("Options are (options may appear in any order");
        err.println("-host hostname      - Host computer on which the server runs");
        err.println("-port port          - Port on which the server is listening");
        err.println("-search             - Appointments should be searched for");
        err.println("-print              - Prints a description of the new appointment");
        err.println("-README             - Prints a README for this project and exits");
        err.println();
        err.println("Date and time should be in the format: mm/dd/yyyy hh:mm \n");
        err.println("This simple program posts appointments to the server");
        err.println("If no value is specified, then all appointments are printed");
        err.println("If no key is specified, all appointments pairs are printed");
        err.println();

        System.exit(1);
    }

    /**
     * Print a error message if something is missing
     * @param error
     */
    private static void errorMessage(String error) {
        System.out.println(error);
        System.exit(3);
    }

    /**
     * This function handles the command line parsing and reads the command line input to sort out the appointment information.
     * The function then calls a error checking program that will check if a field hasn't been entered or entered correctly.
     * If the error checking returns then there are no errors and the input will be written in to a string that will be returned.
     * @param args
     *      The command line argument passed in from main
     * @return the command line arguments such as owner, description, etc  in a string array
     */
    static String[] parseText(String[] args) {
        String commandArg[] = new String[6];
        String hostName = null;
        String portString = null;
        String owner = null;
        String startTime = null;
        String startDate = null;
        String endTime = null;
        String endDate = null;
        String description = null;
        String startDay = null;
        String endDay = null;
        //A flag that stops depending on whether the delimiter or token has been found
        boolean descriptionFlag = false;
        //Flag for checking the first assignment to description
        boolean descriptionTrigger = false;
        boolean exitFlag = false;
        boolean print = false;
        boolean portFlag = false;
        boolean hostFlag = false;

    /*
    Check if the command line argument is empty in which case print an error message and exit
     */
        for(String arg : args) {
            if(arg == null) {
                return commandArg;
            }
        }

        //Check if the command argument is empty
        if(args.length == 0) {
            System.err.println("Missing command line arguments!");
            System.exit(1);
        }

        for (String arg : args) {
            if (arg.startsWith("-host") && !hostFlag) {
                hostFlag = true;
            }

            else if (hostFlag && hostName == null) {
                hostName = arg;
            }

            else if(arg.startsWith("-port") && !portFlag) {
                portFlag = true;
            }

            else if (portFlag && portString == null) {
                portString = arg;
            }

            else if (arg.startsWith("-print") && !print) {
                print = true;

            }

            else if(owner == null) {
                if(isNumeric(arg)) {
                    System.err.println("Invalid name type!");
                    exitFlag = true;
                    break;
                }
                owner = arg;
            }

            else if(descriptionFlag == false) {
                if(isValidDate(arg) || arg.contains("/")) {
                    if(description == null) {
                        description = null;
                        break;
                    }
                    else {
                        descriptionFlag = true;
                        startDate = arg;
                    }
                }
                if(descriptionTrigger == false) {
                    description = arg;
                    descriptionTrigger = true;
                }
                else if(!descriptionFlag) {
                    description += " " + arg;
                }
            }

            else if(startDate == null) {
                startDate = arg;
            }

            else if(startTime == null) {
                startTime = arg;
            }

            else if(startDay == null) {
                startDay = arg;
            }

            else if(endDate == null) {
                endDate = arg;
            }

            else if(endTime == null) {
                endTime = arg;
            }

            else if(endDay == null) {
                endDay = arg;
            }

            else {
                usage("Too many arguments: " + arg);
            }
        }


        if(!exitFlag) {
            checkCommandArgument(hostName, portString, owner, description, startDate, startTime, endDate, endTime, startDay, endDay);

            commandArg[0] = hostName;
            commandArg[1] = portString;
            commandArg[2] = owner;
            commandArg[3] = description;
            commandArg[4] = startDate + " " + startTime + " " + startDay;
            commandArg[5] = endDate + " " + endTime + " " + endDay;

            return commandArg;
        }
        else {
            return commandArg;
        }
    }

    /**
     * This function uses a Regular Expression to check the format of date and time.
     * It compares the strings to the regEx using pattern matcher.
     * @param regEx
     *        A regular expression to compare date and time to.
     * @param date
     *        The date that is passed into the function
     * @param time
     *        The time that is passed into the function
     * @return this returns whether the date and time format are valid. True if they they are and false if not.
     */
    private static boolean checkFormat(String regEx, String date, String time, String day) {
        boolean isValid = false;
        Pattern p = Pattern.compile(regEx);
        if(p.matcher(date + " " + time + " " + day).find()) {
            isValid = true;
        }
        return !isValid;
    }

    /**
     * Check if date is valid
     * @param inDate
     *        String of date
     * @return  true if valid date or false if not
     */
    private static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    /**
     * This program checks whether all of the arguments have been entered and whether they have been entered correctly.
     * @param owner
     *        The owners name
     * @param description
     *        Description of the appointment
     * @param startDate
     *        The start date of the appointment
     * @param startTime
     *        The start time of the appointment
     * @param endDate
     *        The end date of the appointment
     * @param endTime
     *        The end time of the appointment
     * @param startDay
     *        am/pm of the start date/time
     * @param endDay
     *        am/pm or the end date/time
     */
    private static void checkCommandArgument(String hostName, String portName, String owner, String description, String startDate, String startTime, String endDate, String endTime, String startDay, String endDay) {
        String regEx = "^(([0]?[1-9]|1[0-2])/([0-2]?[0-9]|3[0-1])/[1-2]\\d{3})? ?((([0-1]?\\d)|(2[0-3])):[0-5]\\d)?(:[0-5]\\d)? ?(AM|am|PM|pm)?$";

        if(hostName == null) {
            System.err.println("Missing host name!");
            System.exit(3);
        }

        else if(portName == null) {
            System.err.println("Missing port string!");
            System.exit(3);
        }

        else if(owner == null) {
            System.err.println("Missing owner field!");
            System.exit(3);
        }
        //check if description is present in argument
        else if(description == null) {
            System.err.println("Missing description field!");
            System.exit(3);
        }
        //check if startDate is present in argument
        else if(startDate == null) {
            System.err.println("Missing beginDate field!");
            System.exit(3);
        }
        //check if startTime is present in argument
        else if(startTime == null) {
            System.err.println("Missing beginTime field!");
            System.exit(3);
        }
        else if(startDay == null) {
            System.err.println("Missing start am/pm field");
            System.exit(3);
        }
        //check if end date is present in argument
        else if(endDate == null) {
            System.err.println("Missing endDate field");
            System.exit(3);
        }
        //check if end time is present in argument
        else if(endTime == null) {
            System.err.println("Missing endTime field");
            System.exit(3);
        }
        else if(endDay == null) {
            System.err.println("Missing end am/pm field");
            System.exit(3);
        }
        //check if the start date and time are in the correct format
        if(checkFormat(regEx, startDate, startTime, startDay)) {
            System.err.println("Invalid Start date/time format! (Ex: MM/DD/YYYY hh:mm am/pm)");
            System.exit(3);
        }
        //check if the end date and time are in the correct format
        if(checkFormat(regEx, endDate, endTime, endDay)) {
            System.err.println("Invalid End date/time format! (Ex: MM/DD/YYYY hh:mm)");
            System.exit(3);
        }
    }

    /**
     * This function looks for the print options in the command arguments
     * @param args
     *        The whole command argument
     * @return the boolean value print as true, if print is present or false if not
     */
    private static boolean checkPrintOption(String[] args) {
        boolean print = false;
        for (String arg : args) {
            //look for print option
            if (arg.startsWith("-print")) {
                print = true;
                break;
            }
        }
        return print;
    }

    /**
     * This function looks for the README option in the command argument
     * @param args
     *        The whole command line argument
     * @return  the boolean value flag as true, if README is present or false if not
     */
    private static boolean checkReadMeFileOption(String[] args) {
        boolean flag = false;
        for (String arg : args) {
            //look for print option
            if (arg.startsWith("-README")) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * This functions job is check whether a string represents a number or not.
     * @param strNum
     *        the string to be checked whether is a number or not
     * @return return true if the string is a number or false if not
     */
    private static boolean isNumeric(String strNum) {
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

}