package edu.pdx.cs410J.dion;

import static org.junit.Assert.*;
import org.junit.Test;

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
    public void checkOwnerPrintForArgumentParsing() {
        String[] toPrint = Project1.parseText(new String[]{"-README", "Bill"});
        for(String arg : toPrint) {
            System.out.println(arg);
        }
    }

    @Test
    public void checkDescriptionPrintForArgumentParsing() {
        String[] toPrint = Project1.parseText(new String[]{"-README", "Bill", "This", "is", "a", "good", "program."});
        for(String arg : toPrint) {
            System.out.println(arg);
        }
    }
}