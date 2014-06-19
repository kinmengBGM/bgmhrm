package com.beans.leaveapp.batch.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.service.EmployeeService;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementService;
import com.beans.leaveapp.jbpm6.util.ApplicationContextProvider;

public class MonthlyAddedLeave implements Serializable{	

	private static final long serialVersionUID = 1L;
	
	ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
	EmployeeService employeeService = (EmployeeService) applicationContext.getBean("employeeService");
	YearlyEntitlementService yearlyEntitlementService = (YearlyEntitlementService) applicationContext.getBean("yearlyEntitlementService");
	
	public void MonthlyAddedLeaves() throws Exception {				
		List<Employee> employeeList = employeeService.findByEmployeeTypePermAndCont();
		for(Employee employee:employeeList){
			YearlyEntitlement yearlyEntitlement = yearlyEntitlementService.findByEmployeeIdPermAndCont(employee.getId());
			if(yearlyEntitlement != null){
			double currentLeaveBalance =yearlyEntitlement.getCurrentLeaveBalance();
			double currentLeaveBalaceWithAddedLeave = currentLeaveBalance+1.00;			
			yearlyEntitlement.setcurrentLeaveBalance(currentLeaveBalaceWithAddedLeave);
			yearlyEntitlementService.update(yearlyEntitlement);
		}	
		}	
	}

	public YearlyEntitlementService yearlyEntitlementService() {
		return yearlyEntitlementService;
	}

	public void setyearlyEntitlementService(
			YearlyEntitlementService yearlyEntitlementService) {
		this.yearlyEntitlementService = yearlyEntitlementService;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}	
}	

