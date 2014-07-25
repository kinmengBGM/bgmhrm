package com.beans.leaveapp.employeetype.service;

import java.util.List;

import com.beans.leaveapp.employeetype.model.EmployeeType;


public interface EmployeeTypeService {
	 EmployeeType create(EmployeeType employeeType);
	 EmployeeType delete(int id);
	
	 List<EmployeeType> findAll();
	 EmployeeType update(EmployeeType employeeType);
	 EmployeeType findById(int id) throws EmployeeTypeNotFound;
}
