package com.corejsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.employee.Employee;

import com.corejsf.Access.TimesheetManager;
import com.corejsf.Model.EmployeeModel;
import com.corejsf.Model.TimesheetModel;

/**
 * TimesheetBean is a CDI bean that holds relevant information and business
 * logic to interact with JSF XHTML presentation layer.
 *
 */
@Named("timesheetData")
@SessionScoped
public class TimesheetBean implements Serializable {

    /** Manages access and persistence of timesheets in data layer. */
    @Inject
    private TimesheetManager timesheetManager;
    /** CDI manages manages employee related data over a session. */
    @Inject
    private EmployeeInfo employeeSession;

    /** All timesheets belonging to current employee. */
    private List<TimesheetModel> allTimesheets;
    /** End of week of the timesheet to view or viewing. */
    private Date currEndWeek;
    /** A timesheet belonging to current employee, specified by currEndWeek. */
    private TimesheetModel timesheet;
    /** Flag indicate whether successfully saved timesheet. */
    private boolean saveSuccess = false;
    /** Flag deciding whether to show message areas. */
    private boolean showMessages = false;
    /** List of error messages related to saving timesheet. */
    private List<String> errorMessages = new ArrayList<String>();

    /**
     * Get saveSuccess flag.
     * @return whether timesheet successfully saved.
     */
    public boolean getSaveSuccess() {
        return saveSuccess;
    }

    /**
     * Get showMessages flag.
     * @return whether show message error on timesheet page.
     */
    public boolean getShowMessages() {
        return showMessages;
    }

    /**
     * Get list of error messages related to timesheet saving.
     * @return list of timesheet saving error messages.
     */
    public List<String> getErrorMessages() {
        return errorMessages;
    }

    /**
     * Get all timesheets belonging to currently logged in employee.
     * @return all timesheets belonging to currently logged in employee.
     */
    public List<TimesheetModel> getAllTimesheets() {
        // refresh timesheetList only if not initialized, empty or current
        // employee changed.
        if (allTimesheets == null) {
            refreshTimesheetList();
        } else {
            if (allTimesheets.size() == 0) {
                refreshTimesheetList();
            } else {
                EmployeeModel reference = (EmployeeModel) allTimesheets.get(0)
                        .getEmployee();
                if (!reference.equals(employeeSession.getCurrentEmployee())) {
                    refreshTimesheetList();
                }

            }
        }

        return allTimesheets;
    }

    /**
     * Get specified week's timesheet of current employee. Week dictated by
     * currEndWeek property.
     * @return a timesheet.
     */
    public TimesheetModel getTimesheet() {
        // refresh timesheet only if not initialized or currEndWeek changed
        final boolean isSameCurrEndWeek = timesheet != null
                && timesheet.isSameWeekEnd(currEndWeek);
        if (timesheet == null || !isSameCurrEndWeek) {
            refreshTimesheet();
        }

        return timesheet;
    }

    /**
     * Update current employee's timesheet list from data layer.
     */
    private void refreshTimesheetList() {
        System.out.println("Refreshing employee's TimesheetList");
        Employee currEmployee = employeeSession.getCurrentEmployee();
        allTimesheets = timesheetManager.getTimesheets(currEmployee);
    }

    /**
     * Update current employee's timesheet from data layer.
     */
    private void refreshTimesheet() {
        System.out.println("Refreshing current timesheet.");
        Employee currEmployee = employeeSession.getCurrentEmployee();
        TimesheetModel newSheet = timesheetManager.getTimesheet(currEmployee,
                currEndWeek);
        timesheet = newSheet;
    }

    /**
     * Validate and save timesheet to data layer.
     *
     * @return navigation outcome - return to same page.
     */
    public String addTimesheet() {
        boolean isValid = true;

        errorMessages.clear();
        showMessages = true;

        timesheet.trimmedDetails();

        if (!timesheet.isValid()) {
            isValid = false;
            saveSuccess = false;
            errorMessages.add("Work hours must add up to 40. Please add " 
                    + "additional hours into overtime or flexible hour field");
            System.out.println("Work hours must add up to 40.");
        }
        if (!timesheet.isRowsValid()) {
            isValid = false;
            saveSuccess = false;
            errorMessages.add("A combination of project id & workpage must be"
                    + "filled and unique for each row.");
            System.out.println("A combination of project id & workpage must be"
                    + "filled and unique for each row.");
        }
        if (isValid) {
            timesheetManager.saveTimesheet(timesheet);
            saveSuccess = true;
            System.out.println("Saving timesheet");
        }

        return null;
    }

    /**
     * Add a new timesheetRow to timesheet table.
     * @return navigation outcome - return to same page.
     */
    public String addRow() {
        timesheet.addRow();
        return null;
    }

    /**
     * Go to Fill timesheet page to create / edit current week's timesheet.
     * Set currEndWeek to current week's Friday.
     * @return navigation outcome - createTimesheet page.
     */
    public String currTimesheet() {
        showMessages = false;
        currEndWeek = TimesheetModel.getCurrDate();
        return "createTimesheet";
    }

    /**
     * Go to View a past timesheet page. Set currEndWeek to specified
     * week's Friday.
     * @param endWeek specified week's Friday.
     * @return navigation outcome - viewTimesheet page.
     */
    public String viewTimesheet(final Date endWeek) {
        currEndWeek = endWeek;

        // TODO return proper view page of timesheet
        return "viewTimesheet";
    }

}
