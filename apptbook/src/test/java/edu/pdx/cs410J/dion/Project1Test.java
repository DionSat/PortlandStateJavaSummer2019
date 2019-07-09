package edu.pdx.cs410J.dion;

import static org.junit.Assert.*;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link Project1} class.
 */

public class Project1Test {
    @Test
    public void printTheReadMe() {
        Project1.printReadme();
    }

    @Test
    public void checkOptionPrintForArgumentParsing() {
        String[] toPrint = Project1.parseText(new String[]{"-README"});
    }

    @Test
    public void checkPrintForReadMeStopsProcess() {
        String[] toPrint = Project1.parseText(new String[]{"-README", "Bill"});
        for(String arg : toPrint) {
            System.out.println(arg);
        }
    }

    @Test
    public void checkWithOneWordDescriptionForArgumentParsing() {
        String[] toPrint = Project1.parseText(new String[]{"\"Bill\"", "Example.", "01-12-2011", "09:20", "am", "01-13-2011", "11:11", "am"});
        for(String arg : toPrint) {
            System.out.println(arg);
        }
    }

    @Test
    public void checkSentenceDescriptionForArgumentParsing() {
        String[] toPrint = Project1.parseText(new String[]{"\"Bill Harrington\"", "This", "is", "a", "good", "program.", "01-12-2011", "19:20", "am", "01-13-2011", "14:11", "am"});
        for(String arg : toPrint) {
            System.out.println(arg);
        }
    }

    @Test
    public void checkMultipleQuotesForArgumentParsing() {
        String[] toPrint = Project1.parseText(new String[]{"\"Bill\"", "\"Harrington\"", "This", "is", "a", "good", "program.", "01-12-2011", "19:20", "am", "01-13-2011", "14:11", "am"});
        for(String arg : toPrint) {
            System.out.println(arg);
        }
    }

    @Test
    public void checkNullDates() {
        String[] toPrint = Project1.parseText(new String[]{"Bill", "This", "is", "a", "good", "program.", null, "19:20", "am",  "01-13-2011", "14:11", "am"});
        for(String arg : toPrint) {
            System.out.println(arg);
        }
    }

    @Test
    public void checkInvalidDate() {
        String[] toPrint = Project1.parseText(new String[]{"Bill", "This", "is", "a", "good", "program.", "a", "19:20", "am",  "01-13-2011", "14:11", "am"});
        for(String arg : toPrint) {
            System.out.println(arg);
        }
    }

    @Test
    public void checkInvalidDate2() {
        String[] toPrint = Project1.parseText(new String[]{"Bill", "This", "is", "a", "good", "program.", "13-12-2011", "19:20", "am", "01-13-2011", "14:11", "am"});
        for(String arg : toPrint) {
            System.out.println(arg);
        }
    }

    @Test
    public void checkNullTime() {
        String[] toPrint = Project1.parseText(new String[]{"Bill", "This", "is", "a", "good", "program.", "01-12-2011", null, "am", "01-13-2011", "14:11", "am"});
        for(String arg : toPrint) {
            System.out.println(arg);
        }
    }

    @Test
    public void checkInvalidTime() {
        String[] toPrint = Project1.parseText(new String[]{"Bill", "This", "is", "a", "good", "program.", "01-12-2011", "a", "am", "01-13-2011", "14:11", "am"});
        for(String arg : toPrint) {
            System.out.println(arg);
        }
    }

    @Test
    public void checkInvalidTime2() {
        String[] toPrint = Project1.parseText(new String[]{"Bill", "This", "is", "a", "good", "program.", "01-12-2011", "25:20", "am", "01-13-2011", "14:11", "am"});
        for(String arg : toPrint) {
            System.out.println(arg);
        }
    }

    @Test
    public void checkInvalidDay() {
        String[] toPrint = Project1.parseText(new String[]{"Bill", "This", "is", "a", "good", "program.", "01-12-2011", "09:20", "am", "01-13-2011", "14:11", "am"});
        for(String arg : toPrint) {
            System.out.println(arg);
        }
    }

    @Test
    public void invalidName() {
        String[] toPrint = Project1.parseText(new String[]{"12", "This", "is", "a", "good", "program.", "01-12-2011", "19:20", "am", "01-13-2011", "14:11", "am"});
        for(String arg : toPrint) {
            System.out.println(arg);
        }
    }

    @Test
    public void invalidDescription() {
        String[] toPrint = Project1.parseText(new String[]{"Bill", "This", "is", "1", "good", "program.", "01-12-2011", "19:20", "am", "01-13-2011", "14:11", "am"});
        for(String arg : toPrint) {
            System.out.println(arg);
        }
    }

    @Test
    public void InvalidLongArgument() {
        String[] toPrint = Project1.parseText(new String[]{"Bill", "This", "is", "1", "good", "program.", "01-12-2011", "19:20", "am", "01-13-2011", "14:11", "am", "Example"});
        for(String arg : toPrint) {
            System.out.println(arg);
        }
    }

    @Test
    public void testConvertDates() {
        Project1.PrintDateAndTime(Project1.ConvertDateAndTime("01-12-2011", "19:20"), "am");
    }

    @Test
    public void testDateRange() {
        Project1.PrintDateAndTime(Project1.ConvertDateAndTime("23-12-2011", "19:20"), "am");
    }
}