//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package edu.pdx.cs410j.dion.firstapplication;

import java.io.Serializable;
import java.util.Collection;

public abstract class AbstractAppointmentBook<T extends AbstractAppointment> implements Serializable {
    public AbstractAppointmentBook() {
    }

    public abstract String getOwnerName();

    public abstract Collection<T> getAppointments();

    public abstract void addAppointment(T var1);

    public final String toString() {
        String var10000 = this.getOwnerName();
        return var10000 + "'s appointment book with " + this.getAppointments().size() + " appointments";
    }
}