package com.beans.leaveapp.yearlyentitlement.service;

import java.util.List;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;

public interface YearlyEntitlementService {

	 List<YearlyEntitlement> findAll() throws Exception;

	 YearlyEntitlement update(YearlyEntitlement selectedYearlyEntitlement) throws Exception;

	 YearlyEntitlement delete(int id);

	 YearlyEntitlement create(YearlyEntitlement yearlyEntitlement);
   
	 List<String> employeeNames();

	 List<String> findLeaveTypes(int employeeTypeId);
	
	 Employee findByEmployee(String name);
	
	 LeaveType findByLeaveType(String name,int employeeTyped);
	
	 List<YearlyEntitlement> findByEmployeeOrfindByLeaveTypeOrBoth(String employeeName,String leaveType);
	
	 List<YearlyEntitlement> findByEmployeeId(int employeeId);
	
	 YearlyEntitlement findByEmployeeAndLeaveType(int employeeId, int leaveTypeId) throws YearlyEntitlementNotFound;
	//TODO Change method name after Pradeep removed his
	 List<YearlyEntitlement> findYearlyEntitlementListByEmployee(int employeeId);
	
	 YearlyEntitlement findOne(int yearlyEntitlementId) throws YearlyEntitlementNotFound;
	

	 YearlyEntitlement findByEmployeeIdPermAndCont(int employeeId);
	
	 YearlyEntitlement findByEmployeeIdPerm(int employeeId);
	
	 List<YearlyEntitlement> findByEmployeeIdForRemainingLeaves(int employeeId);

	void updateLeaveBalanceAfterApproval(int employeeId,int leaveTypeId,double numberOfDaysApproved);
	
	void updateLeaveBalanceAfterCancelled(int employeeId,int leaveTypeId,double numberOfDaysApproved);
	
	YearlyEntitlement findYearlyEntitlementById(int employeeId, int leaveTypeId);
	
	void addAllEntitlementsToNewEmployee(Employee newlyRegisteredEmployee);
}
