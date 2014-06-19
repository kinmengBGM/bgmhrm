package com.beans.leaveapp.web.bean;

import java.io.Serializable;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementService;

public class UserInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private Employee employee;
    private YearlyEntitlementService yearlyEntitlementService;
	private String name;
	private AuthenticationBean authenticationBean;

	public YearlyEntitlementService getYearlyEntitlementService() {
		return yearlyEntitlementService;
	}

	public void setYearlyEntitlementService(YearlyEntitlementService yearlyEntitlementService) {
		this.yearlyEntitlementService = yearlyEntitlementService;
	}

	public Employee getEmployee() {
		return employee;
	}


	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	
	 public void init() {
		
	  	    	System.out.println("userinfobean");
	  	    	
	  	    	System.out.println(name);
	    
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AuthenticationBean getAuthenticationBean() {
		return authenticationBean;
	}

	public void setAuthenticationBean(AuthenticationBean authenticationBean) {
		this.authenticationBean = authenticationBean;
	}
	
}