package edu.pdx.cs410J.dion;

import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Unit tests for the Student class.  In addition to the JUnit annotations,
 * they also make use of the <a href="http://hamcrest.org/JavaHamcrest/">hamcrest</a>
 * matchers for more readable assertion statements.
 */
public class StudentTest
{

  @Test
  public void studentNamedPatIsNamedPat() {
    String name = "Pat";
    var pat = new Student(name, new ArrayList<>(), 0.0, "Doesn't matter");
    assertThat(pat.getName(), equalTo(name));
  }

  @Test(expected = NullPointerException.class)
  public void whenNameIsNullThrowANullPointerException() {
    String name = null;
    createStudentNamed(name);
  }

  private Student createStudentNamed(String name) {
    return new Student(name, new ArrayList<>(), 0.0, "Doesn't matter");
  }

  @Test(expected = GPAOutOfBoundsException.class)
  public void whenGPAIsLessThanZeroThrowGPAOutOfBoundsException() {
      double gpa = -1.0;
      new Student("name", new ArrayList<>(), gpa, "");
  }

  @Test(expected = GPAOutOfBoundsException.class)
  public void whenGPAIsGreaterThanFourThrowGPAOutOfBoundsException() {
      double gpa = 4.1;
      new Student("name", new ArrayList<>(), gpa, "");
  }

  //When the GPA is not a valid double, exit with an error message saying that the GPA must be a decimal

  //When then there are not enough command line arguments, exit with as error message

}
