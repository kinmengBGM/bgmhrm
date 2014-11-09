package com.beans.leaveapp.leavetransaction.service;

import java.util.Date;
import java.util.List;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetype.model.LeaveType;

public interface LeaveTransactionService {

	 List<LeaveTransaction> findAll();
	
	 List<String> findEmployeeNames();
	
	 List<String> findLeaveTypes(String name);
	
	 int create(LeaveTransaction adminLeaveTransaction);
	
	 void update(LeaveTransaction adminLeaveTransaction);
	
	 void delete(int id);
	
	 Employee findByEmployee(String name);
	
	 LeaveType findByLeaveType(String name, int id);

	 List<LeaveTransaction> findByEmployeeORfindByLeaveTypeORLeaveDatesORStatusORAll(String employeename, String leaveType,Date startDate,String status);

	 List<LeaveTransaction> findByEmployeeORfindByLeaveType(String employeeName, String leaveType);
	
	 LeaveTransaction insertFromWorkflow(LeaveTransaction leaveTransaction);
	
     List<LeaveTransaction> findByStatus(String status);
    
    LeaveTransaction updateLeaveApplicationStatus(LeaveTransaction leaveTransaction);

    LeaveTransaction findById(int id);

    List<LeaveTransaction> getAllFutureLeavesAppliedByEmployee(int employeeId,java.sql.Date todayDate);
   
    List<LeaveTransaction> getAllApprovedLeavesAppliedByEmployee();
    
    List<LeaveTransaction> getAllLeavesAppliedByEmployee(int employeeId);
    
    List<LeaveTransaction> getAllLeavesAppliedByEmployee();
}



