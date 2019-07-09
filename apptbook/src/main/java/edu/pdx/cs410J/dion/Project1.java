package edu.pdx.cs410J.dion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

/**
 * The main class for the CS410J appointment book Project
 */
public class Project1 {

  public static void main(String[] args) {
    //
    String owner = null;
    String startTime = null;
    String endTime = null;
    String description = null;
    boolean descriptionFlag = false;
    boolean printFlag = false;

    String[] cmdArg = parseText(args);
    printFlag = checkPrintOption(args);

    Appointment appointment = new Appointment(cmdArg[1], cmdArg[2], cmdArg[3], cmdArg[4], cmdArg[5]);
    AppointmentBook appointmentBook = new AppointmentBook(cmdArg[0]);
    System.exit(1);

    if(printFlag) {
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
    String startDay = null;
    String endDay = null;
    Integer quoteCount = 0;
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
    if(args.length < 2) {
      System.err.println("Missing command line arguments!");
      System.exit(1);
    }

    for (String arg : args) {
      if(arg.startsWith("-README")) {
        printReadme();
        exitFlag = true;
        break;
      }

      if (arg.startsWith("-print")) {
        print = true;
      }

      else if(!ownerFlag) {
        //Had to add quotes to the owners field because it wasn't reading them
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
      checkCommandArgument(owner, description, startDate, startTime, endDate, endTime, startDay, endDay);

      commandArg[0] = owner;
      commandArg[1] = description;
      commandArg[2] = startDate + " " + startTime;
      commandArg[3] = endDate + " " + endTime;
      commandArg[4] = endDay;
      commandArg[5] = startDay;

      return commandArg;
    }
    else {
      return commandArg;
    }
  }

    static boolean checkFormat(String regEx, String date, String time) {
      boolean isValid = false;
      Pattern p = Pattern.compile(regEx);
      if(p.matcher(date + " " + time).find()) {
        isValid = true;
      }
      return isValid;
    }

  static void checkCommandArgument(String owner, String description, String startDate, String startTime, String endDate, String endTime, String startDay, String endDay) {
    //regex taken from public regex libary @http://regexlib.com/REDetails.aspx?regexp_id=761
    String regEx = "(?=\\d)^(?:(?!(?:10\\D(?:0?[5-9]|1[0-4])\\D(?:1582))|(?:0?9\\D(?:0?[3-9]|1[0-3])\\D(?:1752)))((?:0?[13578]|1[02])|(?:0?[469]|11)(?!\\/31)(?!-31)(?!\\.31)|(?:0?2(?=.?(?:(?:29.(?!000[04]|(?:(?:1[^0-6]|[2468][^048]|[3579][^26])00))(?:(?:(?:\\d\\d)(?:[02468][048]|[13579][26])(?!\\x20BC))|(?:00(?:42|3[0369]|2[147]|1[258]|09)\\x20BC))))))|(?:0?2(?=.(?:(?:\\d\\D)|(?:[01]\\d)|(?:2[0-8])))))([-.\\/])(0?[1-9]|[12]\\d|3[01])\\2(?!0000)((?=(?:00(?:4[0-5]|[0-3]?\\d)\\x20BC)|(?:\\d{4}(?!\\x20BC)))\\d{4}(?:\\x20BC)?)(?:$|(?=\\x20\\d)\\x20))?((?:(?:0?[1-9]|1[012])(?::[0-5]\\d){0,2}(?:\\x20[aApP][mM]))|(?:[01]\\d|2[0-3])(?::[0-5]\\d){1,2})?$";
    String[] ampm = {"am", "AM", "Am", "aM", "pm", "PM", "Pm", "pM"};

    if(owner.equals(null)) {
      System.err.print("Missing owner field!");
      System.exit(3);
    }
    else if(description == null) {
      System.err.print("Missing description field!");
      System.exit(3);
    }
    else if(startDate == null) {
      System.err.print("Missing beginDate field!");
      System.exit(3);
    }
    else if(startTime == null) {
      System.err.print("Missing beginTime field!");
      System.exit(3);
    }
    else if(startDay == null) {
      System.err.print("Missing beginning am/pm field!");
      System.exit(3);
    }
    else if(!checkDay(ampm, startDay)) {
      System.err.print("Invalid beginning am/pm field!");
      System.exit(3);
    }
    else if(endDate == null) {
      System.err.print("Missing endDate field");
      System.exit(3);
    }
    else if(endTime == null) {
      System.err.print("Missing endTime field");
      System.exit(3);
    }
    else if(endDay == null) {
      System.err.print("Missing ending am/pm field!");
      System.exit(3);
    }
    else if(!checkDay(ampm, endDay)) {
      System.err.print("Invalid ending am/pm field!");
      System.exit(3);
    }
    if(!checkFormat(regEx, startDate, startTime)) {
      System.err.println("Invalid Start date/time format!");
      System.exit(3);
    }
    if(!checkFormat(regEx, endDate, endTime)) {
      System.err.println("Invalid End date/time format!");
      System.exit(3);
    }
  }

  public static boolean checkPrintOption(String[] args) {
    boolean print = false;
    for (String arg : args) {
      if (arg.startsWith("-print")) {
        print = true;
        break;
      }
    }
    return print;
  }

  public static boolean isNumeric(String strNum) {
    try {
      double d = Double.parseDouble(strNum);
    } catch (NumberFormatException | NullPointerException nfe) {
      return false;
    }
    return true;
  }

  public static boolean checkDay(String[] arr, String ampm) {
    boolean isValid = false;
    for(String element : arr) {
      if(ampm.equals(element)) {
        isValid = true;
      }
    }
    return isValid;
  }

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