package com.corejsf.Model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ca.bcit.infosys.timesheet.TimesheetRow;

/**
 * Extending TimesheetRow class, representing a single row on a timesheet
 * containing identifier and hours per day.
 */
@Entity
@Table(name = "TimesheetRows")
public class TimesheetRowModel extends TimesheetRow {

    /** Unique id for TimesheetRow. */
    private int id;

    /** timesheet this timesheetrow belongs. */
    private TimesheetModel timesheet;

    /**
     * Creates a TimesheetRowModel object.
     */
    public TimesheetRowModel() {
        this(null);
    }
    
    public TimesheetRowModel(final TimesheetModel t) {
        super();
        timesheet = t;
    }

    /**
     * Creates a TimesheetRowModel object with all fields set. Used to create
     * sample data.
     * 
     * @param id project id
     * @param wp work package number (alphanumeric)
     * @param hours number of hours charged for each day of week. null
     *            represents ZERO
     * @param comments any notes with respect to this work package charges
     */
    public TimesheetRowModel(final int id, final String wp,
            final BigDecimal[] hours, final String comments) {
        super(id, wp, hours, comments);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RowID")
    public int getId() {
        return id;
    }

    public void setId(final int n) {
        id = n;
    }

    @ManyToOne
    @JoinColumn(name = "TimesheetID")
    public TimesheetModel getTimesheet() {
        return timesheet;
    }

    public void setTimesheet(final TimesheetModel t) {
        timesheet = t;
    }

    @Column(name = "ProjectID", nullable = false)
    public int getProjId() {
        return getProjectID();
    }

    public void setProjId(final int n) {
        setProjectID(n);
    }

    @Column(name = "WorkPackage", nullable = false)
    public String getPackageId() {
        return getWorkPackage();
    }

    public void setPackageId(final String wpId) {
        setWorkPackage(wpId);
    }

    @Column(name = "Notes")
    public String getNote() {
        return getNotes();
    }

    public void setNote(final String note) {
        setNotes(note);
    }

    /**
     * @return Monday's hour on this row.
     */
    @Column(name = "Mon")
    public BigDecimal getHourMon() {
        return getHour(TimesheetRow.MON);
    }

    /**
     * Set hour on Monday.
     * 
     * @param hour number of hours worked
     */
    public void setHourMon(final BigDecimal hour) {
        setHourOnDay(TimesheetRow.MON, hour);
    }

    /**
     * @return Tuesday's hour on this row.
     */
    @Column(name = "Tue")
    public BigDecimal getHourTue() {
        return getHour(TimesheetRow.TUE);
    }

    /**
     * @return Wednesday's hour on this row.
     */
    @Column(name = "Wed")
    public BigDecimal getHourWed() {
        return getHour(TimesheetRow.WED);
    }

    /**
     * @return Thursday's hour on this row.
     */
    @Column(name = "Thu")
    public BigDecimal getHourThur() {
        return getHour(TimesheetRow.THU);
    }

    /**
     * @return Friday's hour on this row.
     */
    @Column(name = "Fri")
    public BigDecimal getHourFri() {
        return getHour(TimesheetRow.FRI);
    }

    /**
     * @return Saturday's hour on this row.
     */
    @Column(name = "Sat")
    public BigDecimal getHourSat() {
        return getHour(TimesheetRow.SAT);
    }

    /**
     * @return Sunday's hour on this row.
     */
    @Column(name = "Sun")
    public BigDecimal getHourSun() {
        return getHour(TimesheetRow.SUN);
    }

    /**
     * Set hour on Tuesday.
     * 
     * @param hour number of hours worked
     */
    public void setHourTue(final BigDecimal hour) {
        setHourOnDay(TimesheetRow.TUE, hour);
    }

    /**
     * Set hour on Wednesday.
     * 
     * @param hour number of hours worked
     */
    public void setHourWed(final BigDecimal hour) {
        setHourOnDay(TimesheetRow.WED, hour);
    }

    /**
     * Set hour on Thursday.
     * 
     * @param hour number of hours worked
     */
    public void setHourThur(final BigDecimal hour) {
        setHourOnDay(TimesheetRow.THU, hour);
    }

    /**
     * Set hour on Friday.
     * 
     * @param hour number of hours worked
     */
    public void setHourFri(final BigDecimal hour) {
        setHourOnDay(TimesheetRow.FRI, hour);
    }

    /**
     * Set hour on Saturday.
     * 
     * @param hour number of hours worked
     */
    public void setHourSat(final BigDecimal hour) {
        setHourOnDay(TimesheetRow.SAT, hour);
    }

    /**
     * Set hour on Sunday.
     * 
     * @param hour number of hours worked
     */
    public void setHourSun(final BigDecimal hour) {
        setHourOnDay(TimesheetRow.SUN, hour);
    }

    /**
     * Set hour on a given day.
     * 
     * @param day day to set hours for
     * @param hour hour number of hours worked
     */
    private void setHourOnDay(final int day, final BigDecimal hour) {
        try {
            setHour(day, hour);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to set hour.");
        }
    }

    /**
     * Check if row is a duplicate of given row. Definition of duplicate is when
     * two rows has exact same combination of projectID + WorkPackage.
     * 
     * @param reference row to be checked against.
     * @return whether given row is a duplicate of current row
     */
    public final boolean isDuplicate(final TimesheetRowModel reference) {
        boolean isDuplicate = true;

        final String wp1 = getWorkPackage();
        final String wp2 = reference.getWorkPackage();

        if ((wp1 != null && wp1.length() > 0)
                && (wp2 != null && wp2.length() > 0)) {
            final String id1 = getProjectID() + wp1;
            final String id2 = reference.getProjectID() + wp2;
            if (!id1.equals(id2)) {
                isDuplicate = false;
            }
        }

        return isDuplicate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(getProjectID()).append(getWorkPackage());
        sb.append(": MON: ").append(getHourMon());
        sb.append(", TUE: ").append(getHourTue());
        sb.append(", WED: ").append(getHourWed());
        sb.append(", THU: ").append(getHourThur());
        sb.append(", FRI: ").append(getHourFri());
        sb.append(", SAT: ").append(getHourSat());
        sb.append(", SUN: ").append(getHourSun());
        sb.append("\n");

        return sb.toString();
    }
}
