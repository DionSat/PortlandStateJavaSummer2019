//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package edu.pdx.cs410j.dion.firstapplication;

import java.io.IOException;

public interface AppointmentBookDumper<T extends AbstractAppointmentBook> {
    void dump(T var1) throws IOException;
}