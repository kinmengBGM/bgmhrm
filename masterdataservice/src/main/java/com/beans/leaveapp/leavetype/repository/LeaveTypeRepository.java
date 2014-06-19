package com.beans.leaveapp.leavetype.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.beans.leaveapp.leavetype.model.LeaveType;

public interface LeaveTypeRepository extends CrudRepository<LeaveType, Integer>{
	
	
	 @Query("select l from LeaveType l where isDeleted = ?")
	 List<LeaveType> findByisDeleted(int x);
	
	 @Query("select l from LeaveType l join l.employeeType e where l.name =:name and e.id =:id")
	 LeaveType findByName(@Param("name")String name,@Param("id")int id);
	 
	 @Query("select l.name from LeaveType l where isDeleted = 0")
	 List<String> findNamesList();
	 
	 @Query("select l from LeaveType l where name like ?")
	 List<LeaveType> findByNameLike(String name);
	 
	 @Query("select l.name from LeaveType l join l.employeeType e where e.id = ?")
	 List<String> findByLeaveTypes(int id);
	 
	 @Query("select l from LeaveType l where name = :name and employeeType.id = :id and isDeleted = 0")
	 LeaveType getLeaveTypeByName(@Param("name")String name,@Param("id")int id);
}
