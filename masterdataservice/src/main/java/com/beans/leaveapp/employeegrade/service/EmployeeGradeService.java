package com.beans.leaveapp.employeegrade.service;

import java.util.List;

import com.beans.leaveapp.employeegrade.model.EmployeeGrade;

public interface EmployeeGradeService {
	 EmployeeGrade create(EmployeeGrade employeeGrade);
	 EmployeeGrade delete(int id);
	 List<EmployeeGrade> findAll();
	 EmployeeGrade update(EmployeeGrade employeeGrade);
	 EmployeeGrade findById(int id) throws EmployeeGradeNotFound;
	// public List<EmployeeGrade> findAll();

}

