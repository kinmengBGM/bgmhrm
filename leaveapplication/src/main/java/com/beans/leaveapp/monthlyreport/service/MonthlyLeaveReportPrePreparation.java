package com.beans.leaveapp.monthlyreport.service;

import java.util.List;

import com.beans.leaveapp.employee.model.Employee;

public interface MonthlyLeaveReportPrePreparation {

	void prepareAnnualLeaveDataForYearOfEmployee(Employee employee);
	void prepareAllLeaveDataForYearOfEmployee(Employee employee);
	List<Employee> getAllEmployees();
	
}
