package com.beans.leaveapp.yearlyentitlement.model;

public class EmployeeEntitlement {
	
	int id;
	private String leaveTypeName;
	private double entitlement;
	private double availableBalance;
	
	
	
	public EmployeeEntitlement(int id, String leaveTypeName,
			double entitlement, double avaialbelBalance) {
		this.id = id;
		this.leaveTypeName = leaveTypeName;
		this.entitlement = entitlement;
		this.availableBalance = avaialbelBalance;
	}
	
	public EmployeeEntitlement() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getLeaveTypeName() {
		return leaveTypeName;
	}
	public void setLeaveTypeName(String leaveTypeName) {
		this.leaveTypeName = leaveTypeName;
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

}
