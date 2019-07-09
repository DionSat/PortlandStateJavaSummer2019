package edu.pdx.cs410J.dion;

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


    if(owner.equals(null)) {
      System.err.print("Missing owner field");
      System.exit(3);
    }
    else if(description.equals(null)) {
      System.err.print("Missing description field");
      System.exit(3);
    }
    else if(owner.equals(null)) {
      System.err.print("Missing beginTime field");
      System.exit(3);
    }
    else if(owner.equals(null)) {
      System.err.print("Missing endTime field");
      System.exit(3);
    }

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
    String commandArg[] = new String[4];
    String owner = null;
    String startTime = null;
    String endTime = null;
    String description = null;
    //A flag that stops depending on whether the delimiter or token has been found
    boolean descriptionFlag = false;
    //Flag for checking the first assignment to description
    boolean descriptionTrigger = false;
    boolean print = false;

    //Check if the command argument is empty
    if(args.length == 0) {
      System.err.println("Missing command line arguments");
      System.exit(1);
    }

    for (String arg : args) {
      if(arg.startsWith("-README")) {
        printReadme();
      }

      else if(arg.startsWith("-print")) {
        print = true;
      }

      else if(owner == null) {
        owner = arg;
      }

      else if(descriptionFlag == false) {
        if(descriptionTrigger == false) {
          description = arg;
          descriptionTrigger = true;
        }
        else {
          description += " " + arg;
        }
        if(arg == "." || arg == "?" || arg == "!") {
          descriptionFlag = true;
        }
        //description = arg;
      }

      else if(startTime == null) {
        startTime = arg;
      }

      else if(endTime == null) {
        endTime = arg;
      }
    }
    commandArg[0] = owner;
    commandArg[1] = description;
    commandArg[2] = startTime;
    commandArg[3] = endTime;

    return commandArg;
  }

}