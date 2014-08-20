package com.beans.leaveapp.batch.service;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.service.EmployeeService;
import com.beans.leaveapp.jbpm6.util.ApplicationContextProvider;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.beans.leaveapp.leavetype.service.LeaveTypeNotFound;
import com.beans.leaveapp.leavetype.service.LeaveTypeService;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementService;

@Service
public class YearlyRefreshedLeaves implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
	EmployeeService employeeService = (EmployeeService) applicationContext.getBean("employeeService");
	YearlyEntitlementService yearlyEntitlementService = (YearlyEntitlementService) applicationContext.getBean("yearlyEntitlementService");
	LeaveTypeService leaveTypeService = (LeaveTypeService) applicationContext.getBean("leaveTypeService");

	public void YearlyrefreshedLeaves() throws Exception, LeaveTypeNotFound {
		
		double currentYear;
		double joinYear;
		
		Date currentDate = new Date();		
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		currentYear = cal.get(Calendar.YEAR);
		
		List<Employee> employeeList = employeeService.findAll();
		for(Employee employee : employeeList){
			
			Date joinDate;
			joinDate = employee.getJoinDate();
			if(joinDate != null){
			cal.setTime(joinDate);
			joinYear = cal.get(Calendar.YEAR);			
			
			double yearBalanceRemaining;
			yearBalanceRemaining = currentYear - joinYear;
			
		List<YearlyEntitlement> yearlyEntitlementList = yearlyEntitlementService.findByEmployeeIdForRemainingLeaves(employee.getId());
		for(YearlyEntitlement yearlyEntitlement : yearlyEntitlementList){
			int leaveTypeId = yearlyEntitlement.getLeaveType().getId();
			
				LeaveType leaveType = leaveTypeService.findById(leaveTypeId);			        
				if(leaveType.getId() == 1 && leaveType.getEmployeeType().getName().equals("PERM"))
				{
						if (yearBalanceRemaining <= 4) {
							double carryForwardDays = yearlyEntitlement.getCurrentLeaveBalance();
							double yearlyBalance = carryForwardDays + yearBalanceRemaining + 12.0;
							double entitlement = yearBalanceRemaining + 12.0;
							yearlyEntitlement.setEntitlement(entitlement);
							yearlyEntitlement.setYearlyLeaveBalance(yearlyBalance);
							yearlyEntitlementService.update(yearlyEntitlement);
							} 
						else 
							{
							double carryForwardDays = yearlyEntitlement.getCurrentLeaveBalance();
							double yearlyBalance = carryForwardDays + 4.0 + 12.0;
							double entitlement = yearBalanceRemaining + 12.0;
							yearlyEntitlement.setEntitlement(entitlement);
							yearlyEntitlement.setYearlyLeaveBalance(yearlyBalance);
							yearlyEntitlementService.update(yearlyEntitlement);
							}
					} 
					else if (leaveType.getId() == 2) {
						double carryForwardDays = yearlyEntitlement.getCurrentLeaveBalance();
						double yearlyBalance = carryForwardDays + 12.0;
						double entitlement = leaveType.getEntitlement();
						yearlyEntitlement.setEntitlement(entitlement);
						yearlyEntitlement.setYearlyLeaveBalance(yearlyBalance);
						yearlyEntitlementService.update(yearlyEntitlement);
					}
					else {
						double entitlement = leaveType.getEntitlement();
						yearlyEntitlement.setEntitlement(entitlement);
						yearlyEntitlement.setcurrentLeaveBalance(entitlement);
						yearlyEntitlement.setYearlyLeaveBalance(entitlement);
						yearlyEntitlementService.update(yearlyEntitlement);
					}				
				}
			}
		}
	}
	
	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public YearlyEntitlementService getyearlyEntitlementService() {
		return yearlyEntitlementService;
	}

	public void setYearEntitlementService(
			YearlyEntitlementService yearlyEntitlementService) {
		this.yearlyEntitlementService = yearlyEntitlementService;
	}

	public LeaveTypeService getLeaveTypeService() {
		return leaveTypeService;
	}

	public void setLeaveTypeService(LeaveTypeService leaveTypeService) {
		this.leaveTypeService = leaveTypeService;
	}	
	
}
