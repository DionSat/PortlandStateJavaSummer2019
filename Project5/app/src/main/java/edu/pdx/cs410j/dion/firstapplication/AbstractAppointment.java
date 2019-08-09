//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package edu.pdx.cs410j.dion.firstapplication;

import java.io.Serializable;
import java.util.Date;

public abstract class AbstractAppointment implements Serializable {
    public AbstractAppointment() {
    }

    public abstract String getBeginTimeString();

    public abstract String getEndTimeString();

    public Date getBeginTime() {
        return null;
    }

    public Date getEndTime() {
        return null;
    }

    public abstract String getDescription();

    public final String toString() {
        String var10000 = this.getDescription();
        return var10000 + " from " + this.getBeginTimeString() + " until " + this.getEndTimeString();
    }
}