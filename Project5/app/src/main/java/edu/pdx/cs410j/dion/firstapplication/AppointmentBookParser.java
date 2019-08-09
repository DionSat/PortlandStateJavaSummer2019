//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package edu.pdx.cs410j.dion.firstapplication;

public interface AppointmentBookParser<T extends AbstractAppointmentBook> {
    T parse() throws ParserException;
}
