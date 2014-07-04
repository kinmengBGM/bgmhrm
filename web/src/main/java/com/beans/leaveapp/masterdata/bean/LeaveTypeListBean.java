package com.beans.leaveapp.masterdata.bean;

import java.io.Serializable;
import java.util.List;

import com.beans.leaveapp.employeetype.model.EmployeeType;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.beans.leaveapp.leavetype.service.LeaveTypeService;

public class LeaveTypeListBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<LeaveType> leaveTypeList = null;
	private LeaveTypeService leaveTypeService;
	
	public List<LeaveType> getLeaveTypeList() {
		if(leaveTypeList == null) {
			leaveTypeList = leaveTypeService.findAll();
		}
		return leaveTypeList;
	}
	public void setLeaveTypeList(List<LeaveType> leaveTypeList) {
		this.leaveTypeList = leaveTypeList;
	}
	
	public LeaveTypeService getLeaveTypeService() {
		return leaveTypeService;
	}
	public void setLeaveTypeService(LeaveTypeService leaveTypeService) {
		this.leaveTypeService = leaveTypeService;
	}
	
}
