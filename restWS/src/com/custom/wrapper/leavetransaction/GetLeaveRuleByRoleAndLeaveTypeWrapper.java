package com.custom.wrapper.leavetransaction;

import java.util.List;

public class GetLeaveRuleByRoleAndLeaveTypeWrapper {
	private List<String> roleType;
	private String leaveType;
	
	public void setRoleType(List<String> roleType){
		this.roleType = roleType;
	}
	
	public List<String> getRoleType(){
		return roleType;
	}
	
	public void setLeaveType(String leaveType){
		this.leaveType = leaveType;
	}
	
	public String getLeaveType(){
		return leaveType;
	}
	
}
