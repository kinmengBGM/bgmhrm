package com.beans.leaveapp.employee.repository;

import java.util.List;

import com.beans.leaveapp.employee.model.Employee;

public interface EmployeeRepositoryCustom {
	
	List<Employee> getAllUsersWithRole(String role);

	
	List<Employee> findAllEmployeesForSendingMonthlyLeaveReport();
}
