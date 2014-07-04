package com.beans.leaveapp.monthlyreport.service;

import java.util.List;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.monthlyreport.model.AnnualLeaveReport;

public interface MonthlyLeaveReportPrePreparation {

	void prepareAnnualLeaveDataForYearOfEmployee(Employee employee);
	void prepareAllLeaveDataForYearOfEmployee(Employee employee);
	List<Employee> getAllEmployees();
	public AnnualLeaveReport findAnnualLeaveReportByEmployeeId(int employeeId);
}
