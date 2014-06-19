package com.beans.leaveapp.employee.service;

import java.util.HashMap;
import java.util.List;

import com.beans.common.security.users.model.Users;
import com.beans.leaveapp.address.model.Address;
import com.beans.leaveapp.employee.model.Employee;

public interface EmployeeService {
	public Employee create(Employee employee);
	public Employee delete(int id) throws EmployeeNotFound;
	
	public List<Employee> findAll();
	public Employee update(Employee employee) throws EmployeeNotFound;
	public Employee findById(int id) throws EmployeeNotFound;
	
	public Employee createEmployee(Employee employee, int employeeGradeId, int employeeTypeId, int departmentId, Users users, HashMap<Integer, Address> newAddressMap);
	public Employee updateEmployee(Employee employee, int employeeGradeId, int employeeTypeId, int departmentId, Users users, List<Address> existingAddressList, HashMap<Integer, Address> newAddressMap);
	public Employee findByUsername(String username) throws EmployeeNotFound;
	
	public List<Employee> findEmployeeByNameOrEmployeeNumberOrBoth(String name, String employeeNumber);
	public Employee findByEmployee(String employeeName);
	public List<Employee> findByEmployeeTypePermAndCont();
	public List<Employee> findByEmployeeTypePerm();

	List<Employee> findAllEmployeesByRole(String role);
	
	
}



