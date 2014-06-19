package com.beans.leaveapp.yearlyentitlement.model;


public class LeaveEntitlement {

	private int id;
	private double  entitlement;
	private double  availableBalance;
	private String employeeName;
	private String leaveType;
	
	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public LeaveEntitlement(){
	}
			
	public LeaveEntitlement(int id,double entitlement, double availableBalance,
			String employeeName,String leaveType) {
		super();
		this.id = id;
		this.entitlement = entitlement;
		this.availableBalance = availableBalance;
		this.employeeName = employeeName;
		this.leaveType = leaveType;
	}



	public double getEntitlement() {
		return entitlement;
	}
	public void setEntitlement(double entitlement) {
		this.entitlement = entitlement;
	}
	
	public double getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}



	public String getLeaveTypeId() {
		return leaveType;
	}



	public void setLeaveTypeId(String leaveTypeId) {
		this.leaveType = leaveTypeId;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	} 
	
}
