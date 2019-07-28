package edu.pdx.cs410J.dion;

import edu.pdx.cs410J.ParserException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * The main class for the CS410J appointment book Project.
 * This class handles and implements the command argument parsing and options
 * @author Dion Satcher
 * @version 2.1 7/24/2019
 */
public class Project3 {

    public static void main(String[] args) {
        boolean printFlag;
        boolean textFlag;
        boolean readMeFlag;
        boolean prettyFlag = false;
        Date beginDateTime = null;
        Date endDateTime = null;
        String dash = "-";

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        printFlag = checkPrintOption(args);
        textFlag = checkTextFileOption(args);
        readMeFlag = checkReadMeFileOption(args);
        prettyFlag = checkPrettyPrintFileOption(args);

        if(readMeFlag) {
            printReadme();
            System.exit(2);
        }
        String[] cmdArg = parseText(args);
        try {
            beginDateTime = format.parse(cmdArg[3]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            endDateTime = format.parse(cmdArg[4]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Appointment appointment = new Appointment(cmdArg[2], beginDateTime, endDateTime);
        AppointmentBook appointmentBook = new AppointmentBook(cmdArg[1]);

        if(textFlag && cmdArg[0] != null) {
            TextParser textParser = new TextParser(cmdArg[0]);
            TextDumper textDumper = new TextDumper(cmdArg[0]);
            AppointmentBook parsedAppointment = new AppointmentBook(cmdArg[1]);

            try {
                parsedAppointment = (AppointmentBook) textParser.parse();
            } catch (ParserException e) {
                System.out.println(e);
            }

            if (Objects.equals(cmdArg[1], parsedAppointment.getOwnerName())) {
                parsedAppointment.addAppointment(appointment);

                textDumper.dump(parsedAppointment);
            } else {
                File file = new File(cmdArg[0]);
                if (file.exists()) {
                    appointmentBook.addAppointment(appointment);
                    System.out.println("Owner is not in file");
                } else {
                    appointmentBook.addAppointment(appointment);
                    textDumper.dump(appointmentBook);
                }
            }
        }

        if (prettyFlag) {
            PrettyPrinter prettyPrint = new PrettyPrinter(cmdArg[0]);

            if(!textFlag) {
                appointmentBook.addAppointment(appointment);
            }
            if (cmdArg[0].equals("-")) {
                System.out.println();
                prettyPrint.screendump(appointmentBook);
            }
            else{
                File aFile = new File(cmdArg[0]);
                if (aFile.exists() && !aFile.isDirectory()) {
                    prettyPrint.dump(appointmentBook);
                } else {
                    prettyPrint.dump(appointmentBook);
                }
            }
        }

        if (printFlag) {
            System.out.println(appointmentBook.toString());
            System.out.println(appointment.toString());
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
                "    -pretty                  file Pretty print the appointment book to\n" +
                "                             a text file or standard out (file -)\n" +
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
        String commandArg[] = new String[5];
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
        boolean prettyFlag = false;

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
            if (arg.startsWith("-textFile") && !textFlag) {
                textFlag = true;

            }

            else if (arg.startsWith("-print") && !print) {
                print = true;

            }

            else if (arg.startsWith("-pretty") && !prettyFlag) {
                prettyFlag = true;
            }

            else if ((textFlag || prettyFlag) && filePath == null) {
                filePath = arg;
            }

            else if(!ownerFlag) {
                arg = "\"" + arg + "\"";
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

            else if(owner != null && description != null && startDate!= null && startTime != null && endDate != null  && endTime != null  && arg != null && startDay != null && endDay != null) {
                System.err.println("Too many arguments!");
                System.exit(3);
            }
        }


        if(!exitFlag) {
            checkCommandArgument(owner, description, startDate, startTime, endDate, endTime, startDay, endDay);

            commandArg[0] = filePath;
            commandArg[1] = owner;
            commandArg[2] = description;
            commandArg[3] = startDate + " " + startTime + " " + startDay;
            commandArg[4] = endDate + " " + endTime + " " + endDay;

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
    private static void checkCommandArgument(String owner, String description, String startDate, String startTime, String endDate, String endTime, String startDay, String endDay) {
        //String regEx = "^([0]\\d|[1][0-2])\\/([0-2]\\d|[3][0-1])\\/([2][01]|[1][6-9])\\d{2}(\\s([0-1]\\d|[2][0-3])(\\:[0-5]\\d){1,2})?$";
        String regEx = "^(((0[13578]|1[02])[\\/\\.-](0[1-9]|[12]\\d|3[01])[\\/\\.-]((19|[2-9]\\d)\\d{2})\\s(0[0-9]|1[0-2]):(0[0-9]|[1-59]\\d)\\s(AM|am|PM|pm))|((0[13456789]|1[012])[\\/\\.-](0[1-9]|[12]\\d|30)[\\/\\.-]((19|[2-9]\\d)\\d{2})\\s(0[0-9]|1[0-2]):(0[0-9]|[1-59]\\d)\\s(AM|am|PM|pm))|((02)[\\/\\.-](0[1-9]|1\\d|2[0-8])[\\/\\.-]((19|[2-9]\\d)\\d{2})\\s(0[0-9]|1[0-2]):(0[0-9]|[1-59]\\d)\\s(AM|am|PM|pm))|((02)[\\/\\.-](29)[\\/\\.-]((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))\\s(0[0-9]|1[0-2]):(0[0-9]|[1-59]\\d))\\s(AM|am|PM|pm))$";

        if(owner == null) {
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
     * This function looks for the pretty option in the command argument
     * @param args
     *        The whole command line argument
     * @return  the boolean value flag as true, if README is present or false if not
     */
    private static boolean checkPrettyPrintFileOption(String[] args) {
        boolean flag = false;
        for (String arg : args) {
            //look for print option
            if (arg.startsWith("-pretty")) {
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

}
