package edu.pdx.cs410J.dion;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for the {@link Project3} main class.
 */
public class Project1IT extends InvokeMainTestCase {

  /**
   * Invokes the main method of {@link Project3} with the given arguments.
   */
  private MainMethodResult invokeMain(String... args) {
    return invokeMain( Project3.class, args );
  }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  public void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
  }

  /**
   * Tests that the -README command will cause the exit code of 2
   */
  @Test
  public void testReadmeCommand() {
    MainMethodResult result = invokeMain(Project3.class, "-README");
    assertThat(result.getExitCode(), equalTo(2));
  }

  @Test
  public void missingDescription() {
    MainMethodResult result = invokeMain(Project3.class, "Owner");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing description field!"));
  }

  @Test
  public void missingBeginDate() {
    MainMethodResult result = invokeMain(Project3.class, "Owner", "Description");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing beginDate field!"));
  }

  @Test
  public void missingBeginTime() {
    MainMethodResult result = invokeMain(Project3.class, "Owner", "Description", "5/2/15");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing beginTime field!"));
  }

  @Test
  public void missingEndDate() {
    MainMethodResult result = invokeMain(Project3.class, "Owner", "Description", "5/2/15", "12:30");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing endDate field"));
  }

  @Test
  public void missingEndTime() {
    MainMethodResult result = invokeMain(Project3.class, "Owner", "Description", "5/2/15", "12:30", "5/12/2015");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing endTime field"));
  }

  @Test
  public void IncorrectBeginTimeFormat() {
    MainMethodResult result = invokeMain(Project3.class, "Owner", "Description", "5/215", "12:30", "05/12/2015", "1:00");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Start date/time format! (Ex: MM/DD/YYYY hh:mm am/pm)"));
  }

  @Test
  public void IncorrectEndTimeFormat() {
    MainMethodResult result = invokeMain(Project3.class, "Owner", "Description", "05/02/2015", "12:30", "5/122015", "1:00");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid End date/time format! (Ex: MM/DD/YYYY hh:mm)"));
  }
}