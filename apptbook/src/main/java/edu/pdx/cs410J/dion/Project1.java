package edu.pdx.cs410J.dion;

import java.util.regex.Pattern;

/**
 * The main class for the CS410J appointment book Project
 */
public class Project1 {

  public static void main(String[] args) {
    String owner = null;
    String startTime = null;
    String endTime = null;
    String description = null;
    boolean descriptionFlag = false;
    boolean print = false;

    String[] cmdArg = parseText(args);

    Appointment appointment = new Appointment(description, startTime, endTime);
    AppointmentBook appointmentBook = new AppointmentBook(owner);
    System.exit(1);

    if(print) {
      System.out.println(appointmentBook.toString());
      System.out.println(appointment.toString());
    }

    System.exit(1);
  }

  static void printReadme() {
    System.out.println("\n\n");
    System.out.println("Project 1 README Dion Satcher, CS410J " +
        "\nThis program will allow user to enter appointment information" +
        "\non the command line, then saves the information." +
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
        "  Date and time should be in the format: mm/dd/yyyy hh:mm am/pm (or mm/dd/yy hh:mm am/pm)\n" +
        "\n***END OF README***\n");
  }

  static String[] parseText(String[] args) {
    String commandArg[] = new String[6];
    String owner = null;
    String startTime = null;
    String startDate = null;
    String endTime = null;
    String endDate = null;
    String description = null;
    //A flag that stops depending on whether the delimiter or token has been found
    boolean descriptionFlag = false;
    //Flag for checking the first assignment to description
    boolean descriptionTrigger = false;
    boolean exitFlag = false;
    boolean print = false;

    for(String arg : args) {
      if(arg == null) {
        return commandArg;
      }
    }

    //Check if the command argument is empty
    if(args.length == 0) {
      System.err.println("Missing command line arguments");
      System.exit(1);
    }

    for (String arg : args) {
      if(arg.startsWith("-README")) {
        printReadme();
        exitFlag = true;
        break;
      }

      else if(arg.startsWith("-print")) {
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
        if(descriptionTrigger == false) {
          /*if(isNumeric(arg)) {
            System.err.println("Invalid description type!");
            exitFlag = true;
            break;
          }*/
          description = arg;
          descriptionTrigger = true;
        }
        else {
          /*if(isNumeric(arg)) {
            System.err.println("Invalid description type!");
            exitFlag = true;
            break;
          }*/
          description += " " + arg;
        }
        if(arg.contains(".") || arg.contains("?") || arg.contains("!")) {
          descriptionFlag = true;
        }
        //description = arg;
      }

      else if(startDate == null) {
        startDate = arg;
      }

      else if(startTime == null) {
        startTime = arg;
      }

      else if(endDate == null) {
        endDate = arg;
      }

      else if(endTime == null) {
        endTime = arg;
      }
    }

    if(!exitFlag) {
      checkCommandArgument(owner, description, startDate, startTime, endDate, endTime);

      commandArg[0] = owner;
      commandArg[1] = description;
      commandArg[2] = startDate;
      commandArg[3] = startTime;
      commandArg[4] = endDate;
      commandArg[5] = endTime;

      return commandArg;
    }
    else {
      return commandArg;
    }
  }

  static boolean checkFormat(String startDate, String startTime, String endDate, String endTime) {
    boolean isValid = false;
    //
    String regEx = "^([1-9]|([012][0-9])|(3[01]))-([0]{0,1}[1-9]|1[012])-\\d\\d\\d\\d [012]{0,1}[0-9]:[0-6][0-9]$";
    //^([1-9]|([012][0-9])|(3[01]))-([0]{0,1}[1-9]|1[012])-\d\d\d\d [012]{0,1}[0-9]:[0-6][0-9]$
    Pattern p = Pattern.compile(regEx);
    /*if((startDate + " " + startTime).matches(regEx) && (endDate + " " + endTime).matches(regEx)) {
      isValid = true;
    }*/
    if(p.matcher(startDate + " " + startTime).find() && p.matcher(endDate + " " + endTime).find()) {
      isValid = true;
    }
    return isValid;
  }

  static void checkCommandArgument(String owner, String description, String startDate, String startTime, String endDate, String endTime) {
    if(owner.equals(null)) {
      System.err.print("Missing owner field!");
      System.exit(3);
    }
    else if(description.equals(null)) {
      System.err.print("Missing description field!");
      System.exit(3);
    }
    else if(startDate.equals(null)) {
      System.err.print("Missing beginDate field!");
      System.exit(3);
    }
    else if(startTime.equals(null)) {
      System.err.print("Missing beginTime field!");
      System.exit(3);
    }
    else if(endDate.equals(null)) {
      System.err.print("Missing endDate field");
      System.exit(3);
    }
    else if(endTime.equals(null)) {
      System.err.print("Missing endTime field");
      System.exit(3);
    }
    if(!checkFormat(startDate, startTime, endDate, endTime)) {
      System.err.println("Invalid date format!");
      System.exit(3);
    }
  }

  public static boolean isNumeric(String strNum) {
    try {
      double d = Double.parseDouble(strNum);
    } catch (NumberFormatException | NullPointerException nfe) {
      return false;
    }
    return true;
  }

}