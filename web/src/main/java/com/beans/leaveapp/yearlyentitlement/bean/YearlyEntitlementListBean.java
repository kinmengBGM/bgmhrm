package com.beans.leaveapp.yearlyentitlement.bean;

import java.io.Serializable;
import java.util.List;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementService;


public class YearlyEntitlementListBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<YearlyEntitlement> yearlyEntitlementList = null;
	private YearlyEntitlementService yearlyEntitlementService;
	private Employee employee;
	
	public List<YearlyEntitlement> getYearlyEntitlementList() {
		if(yearlyEntitlementList == null) {
			yearlyEntitlementList = yearlyEntitlementService.findYearlyEntitlementListByEmployee(getEmployee().getId());
		}
		return yearlyEntitlementList;
	}
	public void setYearlyEntitlementList(
			List<YearlyEntitlement> yearlyEntitlementList) {
		this.yearlyEntitlementList = yearlyEntitlementList;
	}
	
	public YearlyEntitlementService getYearlyEntitlementService() {
		return yearlyEntitlementService;
	}
	public void setYearlyEntitlementService(
			YearlyEntitlementService yearlyEntitlementService) {
		this.yearlyEntitlementService = yearlyEntitlementService;
	}
	
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
}
