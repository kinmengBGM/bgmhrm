package com.beans.leaveapp.employee.service;

import java.util.HashMap;
import java.util.List;

import com.beans.common.security.users.model.Users;
import com.beans.leaveapp.address.model.Address;
import com.beans.leaveapp.employee.model.Employee;

public interface EmployeeService {
	 Employee create(Employee employee);
	 Employee delete(int id) throws EmployeeNotFound;
	
	 List<Employee> findAll();
	 Employee update(Employee employee) throws EmployeeNotFound;
	 Employee findById(int id) throws EmployeeNotFound;
	
	 Employee createEmployee(Employee employee, int employeeGradeId, int employeeTypeId, int departmentId, Users users, HashMap<Integer, Address> newAddressMap);
	 Employee updateEmployee(Employee employee, int employeeGradeId, int employeeTypeId, int departmentId, Users users, List<Address> existingAddressList, HashMap<Integer, Address> newAddressMap);
	 Employee findByUsername(String username) throws EmployeeNotFound;
	
	 List<Employee> findEmployeeByNameOrEmployeeNumberOrBoth(String name, String employeeNumber);
	 Employee findByEmployee(String employeeName);
	 List<Employee> findByEmployeeTypePermAndCont();
	 List<Employee> findByEmployeeTypePerm();

	 List<Employee> findAllEmployeesByRole(String role);
	
	 String getFullNameOfEmployee(String userName);
	 Employee findByUserId(int userId) throws EmployeeNotFound;
	
	
}



