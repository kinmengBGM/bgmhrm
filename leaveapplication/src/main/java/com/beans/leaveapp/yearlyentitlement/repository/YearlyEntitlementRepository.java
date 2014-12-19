package com.beans.leaveapp.yearlyentitlement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;

public interface YearlyEntitlementRepository extends
		CrudRepository<YearlyEntitlement, Integer> {

	@Query("select y from YearlyEntitlement y where isDeleted =?")
	 List<YearlyEntitlement> findByIsDeleted(int isDeleted);

	@Query("select y from YearlyEntitlement y where  employee.id =:employeeId and isDeleted =0")
	 List<YearlyEntitlement> findByEmployeeId(@Param("employeeId") int employeeId);
	
	@Query("select y from YearlyEntitlement y where  employee.id =:employeeId and leaveType.name not in ('Unpaid','Time-In-Lieu') ")
	 List<YearlyEntitlement> findByEmployeeIdNotIncludeUnpaid(@Param("employeeId")int employeeId);

	@Query("select y from  YearlyEntitlement y where leaveTypeId =? and isDeleted = 0")
	 List<YearlyEntitlement> findByLeaveTypeIdLike(int leaveTypeId);

	
	@Query("select y from YearlyEntitlement y join y.employee e join y.leaveType l where e.id =? and l.id =?")
	 List<YearlyEntitlement> findByEmployeeIdAndLeaveTypeId( int employeeId,int leaveTypeId);
	  
	
	@Query("select y from YearlyEntitlement y join y.employee e where e.name like :name")
	 List<YearlyEntitlement> findByEmployeeLike(@Param("name") String name);

	@Query("select y from YearlyEntitlement y join y.leaveType l where l.name like :name")
	 List<YearlyEntitlement> findByLeaveTypeLike(
			@Param("name") String name);

	@Query("select ye from YearlyEntitlement ye join ye.employee ee join ye.leaveType lt where ee.name like :employeeName and lt.name like :leaveTypeName")
	 List<YearlyEntitlement> findByEmployeeAndLeaveTypeLike(
			@Param("employeeName") String employeeName,
			@Param("leaveTypeName") String leaveTypeName);
	

	@Query("select y from YearlyEntitlement y where y.employee.id =? and y.leaveType.id IN (1,2) and isDeleted = 0 ")
	 YearlyEntitlement findByEmployeeIdPermAndCont(int employeeId);
	
	@Query("select y from YearlyEntitlement y where y.employee.id = ? and y.leaveType.id = 1 and isDeleted = 0")
	 YearlyEntitlement findByEmployeeIdPerm(int employeeId);

	@Query("select y from YearlyEntitlement y where y.employee.id = ? and isDeleted = 0")
	 List<YearlyEntitlement> findByEmployeeIdPermForRemainingLeaves(int employeeId);
	
	@Query("select y from YearlyEntitlement y where employee.id=? and leaveType.id=?")
	 YearlyEntitlement findByEmployeeAndLeaveTypeId(int employeeId, int leaveTypeId);
	
	
	@Query("select y from YearlyEntitlement y where employee.id=:employeeId and leaveTypeId in (select id from LeaveType l where name=:leaveTypeName and employeeType.id in (select employeeType.id from Employee e where e.id =:employeeId)) and isDeleted=0)")
	 List<YearlyEntitlement> findByEmployeeIdAndEmployeeTypeAndLeaveTypeName(@Param("employeeId") int employeeId,@Param("leaveTypeName") String leaveTypeName);

}
