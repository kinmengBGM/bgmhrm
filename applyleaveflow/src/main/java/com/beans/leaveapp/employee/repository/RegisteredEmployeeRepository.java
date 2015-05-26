package com.beans.leaveapp.employee.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.beans.leaveapp.employee.model.RegisteredEmployee;

public interface RegisteredEmployeeRepository extends CrudRepository<RegisteredEmployee, Integer>{

	@Query("select e from RegisteredEmployee e where e.username = ?")
	RegisteredEmployee findByUsername(String name);
	
	@Query("select e from RegisteredEmployee e where e.registrationStatus = ?")
	List<RegisteredEmployee> findByStatus(String name);
	
}
