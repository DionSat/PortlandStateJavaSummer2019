package edu.pdx.cs410J.dion;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>AppointmentBook</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple key/value pairs.
 */
public class AppointmentBookServlet extends HttpServlet
{
    private final Map<String, AppointmentBook> data = new HashMap<>();
    private AppointmentBook appointmentBook;
    private Appointment appointment;

    /**
     * Handles an HTTP GET request from a client by writing the appointment of the
     * owner specified in the "owner" HTTP parameter to the HTTP response.  If the
     * "owner" parameter is not specified, all of the entries in the appointmentbook
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
        PrintWriter pw = response.getWriter();
        boolean viewAll = decodedQueryString.matches("^owner=(\\w|[+])+$");

        String beginTime = getParameter("beginTime", request);
        String endTime = getParameter("endTime", request);

        if(owner == null && beginTime == null && endTime == null) {
            if(data == null) {
                pw.println("No appointments created yet.");
                return;
            }
            else {
                for (Map.Entry<String, AppointmentBook> entry : this.data.entrySet()) {
                    prettyPrint(data.get(entry.getKey()), pw);
                }

                pw.flush();
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }
        }

        if(owner == null) {
            noOwnerPrint(response.getWriter());
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        appointmentBook = data.get(owner);

        if(appointmentBook == null) {
            noOwnerPrint(response.getWriter());
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        if(owner != null && beginTime != null && endTime != null) {
            printRange(appointmentBook, pw, beginTime, endTime);
        }

        else if(owner != null && beginTime == null && endTime == null) {
            prettyPrint(appointmentBook, pw);
        }

        else if(owner != null && beginTime == null && endTime == null){
            pw.println("The owner parameter must be specified.");
            pw.flush();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        else {
            //pw.println(Messages.getMappingCount(data.size()));

            for (Map.Entry<String, AppointmentBook> entry : this.data.entrySet()) {
                pw.println(Messages.formatKeyValuePair(entry.getKey(), entry.getValue().toString()));
            }

            pw.flush();
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Function to validate dates passed in
     * @param aBook
     * @param writer
     * @param beginTime
     * @param endTime
     */
    private void printRange(AppointmentBook aBook, PrintWriter writer, String beginTime, String endTime) {
        PrettyPrinter pretty = new PrettyPrinter(writer);
        int count = 0;
        Date startDateTime = null;
        Date endDateTime = null;

        String[] validDateFormats = {"MM/dd/yyyy hh:mm a",
                "MM/d/yyyy hh:mm a",
                "M/dd/yyyy hh:mm a",
                "M/d/yyyy hh:mm a"};

        for (String dates : validDateFormats) {
            try {
                Date myDate = null;
                SimpleDateFormat sdf = new SimpleDateFormat(dates);
                sdf.setLenient(false);

                if (count == 0)
                    myDate = sdf.parse(beginTime);
                else
                    myDate = sdf.parse(endTime);

                if (myDate != null) {
                    if (count == 0) //first iteration, set the start
                        startDateTime = myDate;
                    else
                        endDateTime = myDate;
                    ++count;
                }

                if (count == 2) break;

            } catch (ParseException pe) {
                System.err.println("ERROR -> " + pe.getMessage());
            }

        }

        if(startDateTime != null && endDateTime != null)
            pretty.rangePrint(aBook, startDateTime, endDateTime);
    }

    /**
     * @param aBook book to print
     * @param pw print writer
     * @throws IOException
     */
    private void prettyPrint(AppointmentBook aBook, PrintWriter pw) throws IOException {
        PrettyPrinter pretty = new PrettyPrinter(pw);
        pretty.dump(aBook);
    }

    /**
     * Handles an HTTP POST request by storing the appointmentbook entry for the
     * "owner" and "appointment" request parameters.  It writes the appointmentBook
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

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date beginDateTime = null;
        Date endDateTime = null;

        try {
            beginDateTime = df.parse(beginTime);
        } catch (ParseException e) {
            pw.println("Incorrect begin date");
            pw.flush();
            return;
        }
        try {
            endDateTime = df.parse(endTime);
        } catch (ParseException e) {
            pw.println("Incorrect end date");
            pw.flush();
            return;
        }

        if(data != null && data.get(owner) != null) {
            appointmentBook = data.get(owner);
            appointmentBook.addAppointment(new Appointment(description, beginTime, endTime));
            appointmentBook.sort();
            data.put(owner, appointmentBook);

            pw.print("\n\nAdded the below appointment to ");
            pw.print(owner);
            pw.println("'s stored AppointmentBook");
        }

        else {
            appointmentBook = new AppointmentBook(owner);
            appointment = new Appointment(description, beginTime, endTime);
            appointmentBook.addAppointment(appointment);
            appointmentBook.sort();
            data.put(owner, appointmentBook);

            pw.print("\n\nNew appointment for: ");
            pw.println(owner);
        }

        pw.println("-------------------------------------");

        int duration = (int) ((endDateTime.getTime() - beginDateTime.getTime())
                / (1000*60));

        pw.println(String.format( "Appointment with description: %s \nFrom: %s to %s \nDuration: %d\n",
                appointment.getDescription(), appointment.getBeginTimeString(),
                appointment.getEndTimeString(), duration));
        pw.println();

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK);
    }

    /**
     *
     * @param pw print writer
     * @throws IOException
     */
    private void noOwnerPrint(PrintWriter pw) throws IOException{
        PrettyPrinter pretty = new PrettyPrinter(pw);
        pretty.noOwnerPrint();
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
