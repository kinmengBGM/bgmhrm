package com.beans.leaveapp.department.service;

import java.util.List;

import com.beans.leaveapp.department.model.Department;


public interface DepartmentService {
	public Department create(Department department);
	public Department delete(int id);
	
	public List<Department> findAll();
	public Department update(Department department);
	public Department findById(int id) throws DepartmentNotFound;
}
