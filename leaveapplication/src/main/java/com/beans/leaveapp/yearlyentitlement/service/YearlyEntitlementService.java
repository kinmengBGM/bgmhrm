package com.beans.leaveapp.yearlyentitlement.service;

import java.util.List;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.beans.leaveapp.yearlyentitlement.model.EmployeeEntitlement;
import com.beans.leaveapp.yearlyentitlement.model.LeaveEntitlement;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;

public interface YearlyEntitlementService {

	public List<YearlyEntitlement> findAll() throws Exception;

	public YearlyEntitlement update(YearlyEntitlement selectedYearlyEntitlement) throws Exception;

	public YearlyEntitlement delete(int id);

	public YearlyEntitlement create(YearlyEntitlement yearlyEntitlement);
   
	public List<String> employeeNames();

	public List<String> findLeaveTypes(int employeeTypeId);
	
	public Employee findByEmployee(String name);
	
	public LeaveType findByLeaveType(String name,int employeeTyped);
	
	public List<YearlyEntitlement> findByEmployeeOrfindByLeaveTypeOrBoth(String employeeName,String leaveType);
	
	public List<YearlyEntitlement> findByEmployeeId(int employeeId);
	
	public YearlyEntitlement findByEmployeeAndLeaveType(int employeeId, int leaveTypeId) throws YearlyEntitlementNotFound;
	//TODO Change method name after Pradeep removed his
	public List<YearlyEntitlement> findYearlyEntitlementListByEmployee(int employeeId);
	
	public YearlyEntitlement findOne(int yearlyEntitlementId) throws YearlyEntitlementNotFound;
	

	public YearlyEntitlement findByEmployeeIdPermAndCont(int employeeId);
	
	public YearlyEntitlement findByEmployeeIdPerm(int employeeId);
	
	public List<YearlyEntitlement> findByEmployeeIdForRemainingLeaves(int employeeId);

	void updateLeaveBalanceAfterApproval(int employeeId,int leaveTypeId,double numberOfDaysApproved);
	
	YearlyEntitlement findYearlyEntitlementById(int employeeId, int leaveTypeId);
	
	void addAllEntitlementsToNewEmployee(Employee newlyRegisteredEmployee);
}
