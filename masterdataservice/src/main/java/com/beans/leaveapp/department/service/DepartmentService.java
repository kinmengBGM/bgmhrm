package com.beans.leaveapp.department.service;

import java.util.List;

import com.beans.leaveapp.department.model.Department;


public interface DepartmentService {
	 Department create(Department department);
	 Department delete(int id);
	
	 List<Department> findAll();
	 Department update(Department department);
	 Department findById(int id) throws DepartmentNotFound;
}
