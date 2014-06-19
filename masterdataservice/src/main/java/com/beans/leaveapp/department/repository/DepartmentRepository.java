package com.beans.leaveapp.department.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.beans.leaveapp.department.model.Department;

public interface DepartmentRepository extends CrudRepository<Department, Integer>{
	
	@Query("select e from Department e where isDeleted = ?")
	List<Department> findByisDeleted(int x);
}
