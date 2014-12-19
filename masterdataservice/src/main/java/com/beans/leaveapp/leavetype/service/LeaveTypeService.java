package com.beans.leaveapp.leavetype.service;

import java.util.List;

import com.beans.leaveapp.employeetype.model.EmployeeType;
import com.beans.leaveapp.leavetype.model.LeaveType;

public interface LeaveTypeService {
	 LeaveType create(LeaveType leaveType);
	 LeaveType delete(int id);
	 List<LeaveType> findAll();
	 LeaveType update(LeaveType leaveType);
	 LeaveType findById(int id) throws LeaveTypeNotFound;
	 LeaveType findByLeaveType(String name,int id) throws LeaveTypeNotFound;
	 List<String> findByName();
	 EmployeeType findByEmployeeName(String name);
     List<String> findByEmployeeTypes();
     LeaveType  findByEmployeeNameAndTypeId(String name, int employeeTypeId);
}

