package edu.pdx.cs410J.dion;

import edu.pdx.cs410J.ParserException;

import java.io.File;
import java.util.Objects;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.lang.String;

/**
 * The main class for the CS410J appointment book Project.
 * This class handles and implements the command argument parsing and options
 * @author Dion Satcher
 * @version 1 7/10/2019
 */
public class Project2 {

    public static void main(String[] args) {
        boolean printFlag = false;
        boolean textFlag = false;
        boolean readMeFlag = false;

        printFlag = checkPrintOption(args);
        textFlag = checkTextFileOption(args);
        readMeFlag = checkReadMeFileOption(args);
        String[] cmdArg = parseText(args);


        Appointment appointment = new Appointment(cmdArg[2], cmdArg[3] + " " + cmdArg[6], cmdArg[4] + " " + cmdArg[5]);
        AppointmentBook appointmentBook = new AppointmentBook(cmdArg[1]);
        appointmentBook.addAppointment(appointment);

        if(!readMeFlag) {
            if (printFlag) {
                System.out.println(appointmentBook.toString());
                System.out.println(appointment.toString());
            }
        }

        if(textFlag && cmdArg[0] != null) {
            TextParser textParser = new TextParser(cmdArg[0]);
            TextDumper textDumper = new TextDumper(cmdArg[0]);
            AppointmentBook parsedAppointment = new AppointmentBook(cmdArg[1]);

            try {
                parsedAppointment = (AppointmentBook) textParser.parse();
            } catch (ParserException e) {
                System.out.println(e);
            }

            if(parsedAppointment == null) {
                System.exit(1);
            }

            if (Objects.equals(cmdArg[1], parsedAppointment.getOwnerName())) {
                parsedAppointment.addAppointment(appointment);

                textDumper.dump(parsedAppointment);
            } else {
                File file = new File(cmdArg[0]);
                if (!file.exists()) {
                    appointmentBook.addAppointment(appointment);
                    System.out.println("Owner does not exist in the file");
                } else {
                    //appointmentBook.addAppointment(appointment);
                    textDumper.dump(appointmentBook);
                }
            }
        }

        System.exit(1);
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
                "    -print                   Prints a description of the new appointment\n" +
                "    -README                  Prints a README for this project and exits\n" +
                "    -textFile                Where to read/write the appointment book\n" +
                "  Date and time should be in the format: mm/dd/yyyy hh:mm am/pm (or mm/dd/yy hh:mm am/pm)\n" +
                "\n***END OF README***\n");
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
        String commandArg[] = new String[7];
        String owner = null;
        String startTime = null;
        String startDate = null;
        String endTime = null;
        String endDate = null;
        String description = null;
        String startDay = null;
        String endDay = null;
        Integer quoteCount = 0;
        boolean textFlag = false;
        String filePath = null;
        //A flag that stops depending on whether the delimiter or token has been found
        boolean descriptionFlag = false;
        //Flag for checking the first assignment to description
        boolean descriptionTrigger = false;
        //A flag that stops depending on whether the delimiter or token has been found
        boolean ownerFlag = false;
        //Flag for checking the first assignment to owner
        boolean ownerTrigger = false;
        boolean exitFlag = false;
        boolean print = false;

    /*
    Check if the command line arguments have multiple quotes or more than one
    Argument should only have one quote for the owner name only.
     */
        for(String arg: args) {
            if(quoteCount > 1) {
                System.err.println("Invalid Name format(Ex: \"first lastname\")!");
                System.exit(3);
            }
            if(arg.endsWith("\"")) {
                quoteCount++;
            }
        }




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
            if(arg.startsWith("-README")) {
                printReadme();
                exitFlag = true;
                break;
            }

            else if (arg.startsWith("-textFile") && !textFlag) {
                textFlag = true;
            }

            else if (arg.startsWith("-print")) {
                print = true;
            }

            else if (textFlag && filePath == null) {
                if(arg.contains("\"")) {
                    filePath = null;
                    break;
                }
                else {
                    filePath = arg;
                }
            }

            else if(!ownerFlag) {
                //Had to add quotes to the owners field because it wasn't reading them
                if(!arg.startsWith("\"") && !arg.endsWith("\"")) {
                    owner = "bad";
                    //exitFlag = true;
                    break;
                }
                if(isNumeric(arg)) {
                    System.err.println("Invalid name type!");
                    exitFlag = true;
                    break;
                }
                if(!ownerTrigger) {
                    owner = arg;
                    ownerTrigger = true;
                }
                else {
                    owner += " " + arg;
                }
                if(arg.endsWith("\"")) {
                    ownerFlag = true;
                }
            }

            else if(descriptionFlag == false) {
                if(descriptionTrigger == false) {
                    description = arg;
                    descriptionTrigger = true;
                }
                else {
                    description += " " + arg;
                }
                if(arg.contains(".") || arg.contains("?") || arg.contains("!")) {
                    descriptionFlag = true;
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
        }

        if(!exitFlag) {
            checkCommandArgument(filePath, owner, description, startDate, startTime, endDate, endTime, startDay, endDay);

            commandArg[0] = filePath;
            commandArg[1] = owner;
            commandArg[2] = description;
            commandArg[3] = startDate + " " + startTime;
            commandArg[4] = endDate + " " + endTime;
            commandArg[5] = endDay;
            commandArg[6] = startDay;

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
    private static boolean checkFormat(String regEx, String date, String time) {
        boolean isValid = false;
        Pattern p = Pattern.compile(regEx);
        if(p.matcher(date + " " + time).find()) {
            isValid = true;
        }
        return !isValid;
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
     *        Whether the start time is am or pm
     * @param endDay
     *        Whether the end time is am or pm
     */
    private static void checkCommandArgument(String filepath, String owner, String description, String startDate, String startTime, String endDate, String endTime, String startDay, String endDay) {
        //regex taken from public regex libary @http://regexlib.com/REDetails.aspx?regexp_id=761
        String regEx = "(?=\\d)^(?:(?!(?:10\\D(?:0?[5-9]|1[0-4])\\D(?:1582))|(?:0?9\\D(?:0?[3-9]|1[0-3])\\D(?:1752)))((?:0?[13578]|1[02])|(?:0?[469]|11)(?!\\/31)(?!-31)(?!\\.31)|(?:0?2(?=.?(?:(?:29.(?!000[04]|(?:(?:1[^0-6]|[2468][^048]|[3579][^26])00))(?:(?:(?:\\d\\d)(?:[02468][048]|[13579][26])(?!\\x20BC))|(?:00(?:42|3[0369]|2[147]|1[258]|09)\\x20BC))))))|(?:0?2(?=.(?:(?:\\d\\D)|(?:[01]\\d)|(?:2[0-8])))))([-.\\/])(0?[1-9]|[12]\\d|3[01])\\2(?!0000)((?=(?:00(?:4[0-5]|[0-3]?\\d)\\x20BC)|(?:\\d{4}(?!\\x20BC)))\\d{4}(?:\\x20BC)?)(?:$|(?=\\x20\\d)\\x20))?((?:(?:0?[1-9]|1[012])(?::[0-5]\\d){0,2}(?:\\x20[aApP][mM]))|(?:[01]\\d|2[0-3])(?::[0-5]\\d){1,2})?$";
        //array of am/pm strings to compare to
        String[] ampm = {"am", "AM", "Am", "aM", "pm", "PM", "Pm", "pM"};

        //check if owner is present in argument
        if(owner.equals("bad")) {
            System.err.println("Error name needs to be of the form \\\"NAME\\\"!");
            System.exit(3);
        }
        else if (filepath == null) {
            System.err.println("No file path specified!");
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
        //check if whether the start time, am/pm is present in argument
        else if(startDay == null) {
            System.err.println("Missing beginning am/pm field!");
            System.exit(3);
        }
        //check if start time am/pm is in correct format
        else if(checkDay(ampm, startDay)) {
            System.err.println("Invalid beginning am/pm field!");
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
        //check if end time am/pm is present in argument
        else if(endDay == null) {
            System.err.println("Missing ending am/pm field!");
            System.exit(3);
        }
        //check if end time am/pm is in correct format
        else if(checkDay(ampm, endDay)) {
            System.err.println("Invalid ending am/pm field!");
            System.exit(3);
        }
        //check if the start date and time are in the correct format
        if(checkFormat(regEx, startDate, startTime)) {
            System.err.println("Invalid Start date/time format! (Ex: MM/DD/YYYY hh:mm)");
            System.exit(3);
        }
        //check if the end date and time are in the correct format
        if(checkFormat(regEx, endDate, endTime)) {
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
     * This function looks for the textFile option in the command argument
     * @param args
     *        The whole command line argument
     * @return  the boolean value flag as true, if textFile is present or false if not
     */
    private static boolean checkTextFileOption(String[] args) {
        boolean flag = false;
        for (String arg : args) {
            //look for print option
            if (arg.startsWith("-textFile")) {
                flag = true;
                break;
            }
        }
        return flag;
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

    /**
     * This function compares the am/pm or start and end time to a string of valid am/pm formats and checks whether they match.
     * @param arr
     *        the array of valid am/pm examples
     * @param ampm
     *        the assumed am/pm string to compare with
     * @return return isValid as true if the am/pm is valid or false if its not.
     */
    private static boolean checkDay(String[] arr, String ampm) {
        boolean isValid = false;
        for(String element : arr) {
            if(ampm.equals(element)) {
                isValid = true;
            }
        }
        return !isValid;
    }

    /**
     * This is for project2 for now im leaving it blank because im not done. I plan on turning all the strings into date objects in project 2.
     * @param date
     * @param time
     * @return
     */
    public static Date ConvertDateAndTime(String date, String time) {
        //Displaying given time in 12 hour format with AM/PM
        SimpleDateFormat sdf4 = null;
        Date date4 = null;
        String dateString = date + " " + time;
        //old format
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try{
            date4 = sdf3.parse(dateString);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return date4;
    }

    public static void PrintDateAndTime(Date toPrint, String ampm) {
        SimpleDateFormat sdf4 = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        System.out.println("Given date and time in AM/PM: "+sdf4.format(toPrint) + " " + ampm);
    }

}
