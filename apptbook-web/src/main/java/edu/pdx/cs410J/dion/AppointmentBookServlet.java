package edu.pdx.cs410J.dion;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>AppointmentBook</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple key/value pairs.
 */
public class AppointmentBookServlet extends HttpServlet
{
    private final Map<String, AppointmentBook> data = new HashMap<>();
    private ArrayList<AppointmentBook> appointmentBooks = new ArrayList<>();
    private AppointmentBook appointmentBook;
    private Appointment appointment;

    /**
     * Handles an HTTP GET request from a client by writing the definition of the
     * word specified in the "word" HTTP parameter to the HTTP response.  If the
     * "word" parameter is not specified, all of the entries in the dictionary
     * are written to the HTTP response.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        String owner = getParameter("owner", request);
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        String fullUrl = requestURL + "?" + queryString;
        String decodedUrl = URLDecoder.decode(fullUrl);
        String decodedQueryString = decodedUrl.substring(decodedUrl.lastIndexOf("?") + 1);
        PrettyPrinter pretty = new PrettyPrinter("apptbook.txt");
        PrintWriter pw = response.getWriter();

        String beginTime = getParameter("beginTime", request);
        String endTime = getParameter("endTime", request);

        if(owner != null && beginTime != null && endTime != null) {
            try {
                searchAndPrint(owner, beginTime, endTime, response);
            } catch (ParseException e) {
                System.out.println("Issue while searching.");
            }
        } else if(owner != null && beginTime == null && endTime == null) {
            appointmentBook = data.get(owner);
            PrintWriter pw = response.getWriter();
            if(data.get(owner) != null) {
                pw.println();
                pw.println(String.format("Owner Name: %s \n", owner));
                List<Appointment> appointmentList = data.get(owner).getAppointments();
                boolean foundSomthing = false;
                for(Appointment app : appointmentList) {
                    foundSomthing = true;
                    pw.println(Messages.printAppointment(app));
                }
                if (!foundSomthing) {
                    pw.println("No Appointments found within " + owner + " AppointmentBook." );
                }
            }

            else {
                pw.println("The owner parameter must be specified.");
                pw.flush();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            pw.flush();

        } else {
            PrintWriter pw = response.getWriter();
            //pw.println(Messages.getMappingCount(data.size()));

            for (Map.Entry<String, AppointmentBook> entry : this.data.entrySet()) {
                pw.println(String.format("  %s -> %s", entry.getKey(), entry.getValue().toString()));
            }

            pw.flush();

        }
    }

    /**
     * Allows viewing of all appointments for a specified owner.
     * @param request - The request made by the client.
     * @param response - The response to be sent back to the client.
     * @throws ServletException - Thrown when the server encounters an error.
     * @throws IOException - Thrown when the server encounters an IO error.
     */
    protected void viewAllAppointments(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {

        response.setContentType( "text/plain" );

        AppointmentBook currentAppointmentBook = null;
        String owner = request.getParameter("owner");
        PrettyPrinter pretty = new PrettyPrinter("apptbook.txt");
        PrintWriter pw = response.getWriter();

        // Make sure owner is specified in the parameters.
        if(owner == null) {
            pw.println("The owner parameter must be specified.");
            pw.flush();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Let's try to find an appointment book that
        for(AppointmentBook apptBook : appointmentBook) {

            if(apptBook.getOwnerName().equals(owner)) {
                currentAppointmentBook = apptBook;
            }
        }

        // No appointment book was found, nothing we can do
        // in a GET request.
        if(currentAppointmentBook == null) {
            pw.println("No appointment book found for " + owner);
            pw.flush();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Dump the appointment book we found to a file,
        // then read it back and send it back to the user
        // as text.
        pretty.dump(currentAppointmentBook);
        byte[] appointments = Files.readAllBytes(Paths.get("apptbook.txt"));
        String apptBook = new String(appointments);

        if (currentAppointmentBook.getAppointments().size() > 0){
            pw.println(apptBook);
        }
        else {
            pw.println("No appointments found.");
        }

        pw.flush();
        response.setStatus(HttpServletResponse.SC_OK);
    }


    private void searchAndPrint(String owner, String beginTime, String endTime, HttpServletResponse response) throws ParseException, IOException {

        PrintWriter pw = response.getWriter();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date beginDateTime = format.parse(beginTime);
        Date endDateTime = format.parse(endTime);

        if(data.get(owner) != null) {
            List<Appointment> appointmentList = data.get(owner).getAppointments();
            boolean foundFlag = false;
            boolean printFlag = false;

            for(Appointment app : appointmentList) {
                Date appBegin = format.parse(app.getBeginTimeString());
                Date appEnd = format.parse(app.getEndTimeString());
                if(appBegin.compareTo(beginDateTime) >= 0 && appEnd.compareTo(endDateTime) <= 0) {
                    if(!printFlag){
                        pw.println("\n- Appointment's Found -");
                        pw.println("--------------------------------------------");
                        printFlag = true;
                    }
                    foundFlag = true;
                    pw.println(Messages.printAppointment(app));
                }
            }
            if (!foundFlag) {
                pw.println("No Appointments found within " + owner + " AppointmentBook.");
            }
        } else {
            pw.println("owner AppointmentBook not found.");
        }

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK);
    }

    /**
     * Handles an HTTP POST request by storing the dictionary entry for the
     * "word" and "definition" request parameters.  It writes the dictionary
     * entry to the HTTP response.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/plain");

        String parameter = "owner";
        String owner = getRequiredParameter(request, response, parameter);
        if (owner == null) return;

        String description = getRequiredParameter(request, response, "description");
        if (description == null) return;

        String beginTime = getRequiredParameter(request, response, "beginTime");
        if (beginTime == null) return;

        String endTime = getRequiredParameter(request, response, "endTime");
        if (endTime == null) return;

        //Creat appointments and add it to the appointmentBook
        PrintWriter pw = response.getWriter();

        if(data != null && data.get(owner) != null) {
            appointmentBook = data.get(owner);
            appointmentBook.addAppointment(new Appointment(description, beginTime, endTime));
            data.put(owner, appointmentBook);

            pw.print("\n\nAdded the below appointment to ");
            pw.print(owner);
            pw.println("'s stored AppointmentBook");

        } else {
            appointmentBook = new AppointmentBook(owner);
            appointment = new Appointment(description, beginTime, endTime);
            appointmentBook.addAppointment(appointment);
            data.put(owner, appointmentBook);

            pw.print("\n\nNew appointment for: ");
            pw.println(owner);

        }

        pw.println("-------------------------------------");
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date beginDateTime = null;
        Date endDateTime = null;

        try {
            beginDateTime = df.parse(appointment.getBeginTimeString());
        } catch (ParseException e) {
            System.out.println("Incorrect begin date");
        }
        try {
            endDateTime = df.parse(appointment.getEndTimeString());
        } catch (ParseException e) {
            System.out.println("Incorrect end date");
        }

        int duration = (int) ((endDateTime.getTime() - beginDateTime.getTime())
                / (1000*60));

        System.out.println(String.format( "Appointment with description: %s \nFrom: %s to %s \nDuration: %d\n",
                appointment.getDescription(), appointment.getBeginTimeString(),
                appointment.getEndTimeString(), duration));
        pw.println();

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK);
    }

    private String getRequiredParameter(HttpServletRequest request, HttpServletResponse response, String parameter) throws IOException {
        String value = getParameter(parameter, request);
        if (value == null) {
            missingRequiredParameter(response, parameter);
            return null;
        }
        return value;
    }

    /**
     * Handles an HTTP DELETE request by removing all dictionary entries.  This
     * behavior is exposed for testing purposes only.  It's probably not
     * something that you'd want a real application to expose.
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        this.data.clear();

        PrintWriter pw = response.getWriter();
        pw.println("All mappings have been deleted");
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);

    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        String message = Messages.missingRequiredParameter(parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }

    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }
}
