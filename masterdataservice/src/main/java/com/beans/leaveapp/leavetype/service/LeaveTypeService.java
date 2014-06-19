package com.beans.leaveapp.leavetype.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.beans.leaveapp.employeetype.model.EmployeeType;
import com.beans.leaveapp.leavetype.model.LeaveType;

public interface LeaveTypeService {
	public LeaveType create(LeaveType leaveType);
	public LeaveType delete(int id);
	public List<LeaveType> findAll();
	public LeaveType update(LeaveType leaveType);
	public LeaveType findById(int id) throws LeaveTypeNotFound;
	public LeaveType findByLeaveType(String name,int id) throws LeaveTypeNotFound;
	public List<String> findByName();
	public EmployeeType findByEmployeeName(String name);
    public List<String> findByEmployeeTypes();
}

