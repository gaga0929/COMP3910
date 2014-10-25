package com.corejsf.Access;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.corejsf.Model.EmployeeModel;
import com.corejsf.Model.TimesheetModel;
import com.corejsf.Model.TimesheetRowModel;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.TimesheetRow;

@Named("timesheetManager")
@ApplicationScoped
public class TimesheetManager{
	
	private List<TimesheetModel> timesheetCollection;
	private EmployeeTracker employeeManager = new EmployeeTracker();
	
	public TimesheetManager() {
		timesheetCollection = new LinkedList<TimesheetModel>();
		populateTimesheetCollection();
	}


	public List<TimesheetModel> getTimesheets() {
		return timesheetCollection;
	}

	
	public List<TimesheetModel> getTimesheets(Employee e) {
		if (e == null)	return null;

		List <TimesheetModel> employeeTimesheets = new LinkedList<TimesheetModel>();
		for (TimesheetModel t : timesheetCollection) {
			if (e.equals(t.getEmployee())) {
				employeeTimesheets.add(t);
			}
		}
		return employeeTimesheets;
	}

	public TimesheetModel getCurrentTimesheet(Employee e) {
		Calendar c = new GregorianCalendar();
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);
        Date currEndWeek = c.getTime();

        TimesheetModel currWeek = getTimesheet(e, currEndWeek);
		if (currWeek == null) {
			currWeek = new TimesheetModel();
		}
		
		return currWeek;
	}
	
	public TimesheetModel getTimesheet(final Employee e, final Date weekEnd) {
		List<TimesheetModel> employeeTimesheets = getTimesheets(e);
		TimesheetModel timesheet = null;

		for (TimesheetModel t : employeeTimesheets) {
			if (t.getEndWeek() == weekEnd) {
				timesheet = t;
			}
		}
		if (timesheet == null) {
			timesheet = new TimesheetModel();
		}
		
		return timesheet;
	}

	private void populateTimesheetCollection() {
		final int P1 = 132;

		final List<TimesheetRow> e1Rows = new LinkedList<TimesheetRow>();

		TimesheetRowModel row;
		BigDecimal[] h1 = {null, null, null, new BigDecimal(4), null, null, null};
		row = new TimesheetRowModel(P1, "AA123", h1, "");
		e1Rows.add(row);
		
		BigDecimal[] h2 = {null, null, new BigDecimal(8), null, new BigDecimal(4), null, null};		
		row = new TimesheetRowModel(P1, "AB112", h2, "");
		e1Rows.add(row);
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date end = sdf.parse("10/10/2014");

			final List<EmployeeModel> elist = employeeManager.getEmployees();
			if (elist != null && elist.size() >= 2) {
				final Employee e1 = elist.get(0);
				final TimesheetModel t1 = new TimesheetModel(e1, end, e1Rows);
				timesheetCollection.add(t1);

				final Employee e2 = elist.get(1);
				final TimesheetModel t2 = new TimesheetModel(e2, end, e1Rows);
				timesheetCollection.add(t2);
			}
			else {
				System.out.println("no user!");
			}

		} catch (ParseException e) {
			System.out.println("ERROR: cannot parse strng date");
			e.printStackTrace();
		}
	}

}
