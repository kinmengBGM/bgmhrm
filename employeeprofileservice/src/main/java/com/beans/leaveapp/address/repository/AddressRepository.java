package com.beans.leaveapp.address.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.beans.leaveapp.address.model.Address;

public interface AddressRepository extends CrudRepository<Address, Integer>{
	@Query("select e from Address e where isDeleted = ?")
	List<Address> findByisDeleted(int isDeleted);
	
	@Query("select e from Address e where employeeId = ? and isDeleted = ?")
	List<Address> findByEmployeeId(int employeeId, int deleted);
}
