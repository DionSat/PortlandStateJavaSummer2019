package edu.pdx.cs410J.dion;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client
 */
public class AppointmentBookRestClient extends HttpRequestHelper {
  private static final String WEB_APP = "apptbook";
  private static final String SERVLET = "appointments";

  private String url;


  /**
   * Creates a client to the appointment book REST service running on the given host and port
   *
   * @param hostName The name of the host
   * @param port     The port
   */
  public AppointmentBookRestClient(String hostName, int port) {
    this.url = String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET);
  }

  /**
   * Returns all keys and values from the server
   * @return
   * @throws IOException
   */
  public Response getAllKeysAndValues() throws IOException
  {
    return get(this.url,Map.of());
  }


  /**
   * Adds a new appointment to the appointment book specified by the newOwner parameter.
   * @param newOwner - Owner of the appointment book to add the appointment to.
   * @param newDescription - Description of the new appointment.
   * @param newBeginTime - Beginning time of the new appointment.
   * @param newEndTime - Ending time of the new appointment.
   * @return - A server response object from the HTTP server that was contacted.
   * @throws IOException - Throws an exception when there was a problem contacting
   *                       the specified HTTP server.
   */
  public Response addAppointment( String newOwner, String newDescription, String newBeginTime, String newEndTime) throws IOException
  {
    // Build the parameter array.
    String[] params = new String[8];
    params[0] = "owner";
    params[1] = newOwner;
    params[2] = "description";
    params[3] = newDescription;
    params[4] = "beginTime";
    params[5] = newBeginTime;
    params[6] = "endTime";
    params[7] = newEndTime;

    // Encode the parameters of our POST URL.
    this.url += "?owner=" + URLEncoder.encode(newOwner, "UTF-8");

    return post(this.url, Map.of("owner", newOwner,
            "description", newDescription,
            "beginTime", newBeginTime,
            "endTime", newEndTime));
  }

  /**
   * Searchs for appointments belonging to the specified owner, between
   * the specified beginning and end dates.
   * @param newOwner - Owner of the appointment book to search.
   * @param newBeginTime - beginning time and date of the appointments to search.
   * @param newEndTime - ending time and date of the appointments to search.
   * @return - A response object from the HTTP server that was contacted.
   * @throws IOException - Throws an exception when there is a problem contacting
   *                       the HTTP server.
   */
  public Response searchAppointments( String newOwner, String newBeginTime, String newEndTime) throws IOException
  {

    // Build our array of parameters.
    String[] params = new String[6];
    params[0] = "owner";
    params[1] = newOwner;
    params[2] = "beginTime";
    params[3] = newBeginTime;
    params[4] = "endTime";
    params[5] = newEndTime;


    // Encode our URL as UTF-8.
    this.url += "?owner=" + URLEncoder.encode(newOwner, "UTF-8")
            + "&beginTime=" + URLEncoder.encode(newBeginTime, "UTF-8")
            + "&endTime=" + URLEncoder.encode(newEndTime, "UTF-8");

    return get(this.url, Map.of("owner", newOwner,
            "beginTime", newBeginTime,
            "endTime", newEndTime));
  }


  public Response removeAllMappings() throws IOException {
    return delete(this.url, Map.of());
  }
}
