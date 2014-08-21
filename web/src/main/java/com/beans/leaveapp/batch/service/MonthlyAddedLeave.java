package com.beans.leaveapp.batch.service;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
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
		
		int currentDateDay;
		int currentDateMonth;
		int currentDateYear;
		int joinDateday;
		int joinDateMonth;
		int joinDateYear;		
		
		Date currentDate = new Date();		
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		currentDateDay = cal.get(Calendar.DAY_OF_MONTH);
		currentDateMonth = cal.get(Calendar.MONTH);
		currentDateYear = cal.get(Calendar.YEAR);
		
		List<Employee> employeeList = employeeService.findByEmployeeTypePermAndCont();
		for(Employee employee:employeeList){
			
			Date joinDate;
			joinDate = employee.getJoinDate();
			if(joinDate != null){
			cal.setTime(joinDate);
			joinDateday = cal.get(Calendar.DAY_OF_MONTH);
			joinDateMonth = cal.get(Calendar.MONTH);
			joinDateYear = cal.get(Calendar.YEAR);
		
				YearlyEntitlement yearlyEntitlement = yearlyEntitlementService.findByEmployeeIdPermAndCont(employee.getId());
				if(yearlyEntitlement != null){
					double currentLeaveBalance =yearlyEntitlement.getCurrentLeaveBalance();
					double currentLeaveBalaceWithAddedLeave = currentLeaveBalance+1.00;			
					yearlyEntitlement.setcurrentLeaveBalance(currentLeaveBalaceWithAddedLeave);
					yearlyEntitlementService.update(yearlyEntitlement);
				}			
				if(employee.getEmployeeType().getName().equals("PERM") && yearlyEntitlement != null){
					if(yearlyEntitlement.getEntitlement() < 16){						
						if(currentDateMonth == joinDateMonth && currentDateYear != joinDateYear){
							double entitlement =yearlyEntitlement.getEntitlement();
							double addedEntitlement = entitlement+1.00;
							System.out.println(addedEntitlement);
							yearlyEntitlement.setEntitlement(addedEntitlement);
							double addToCurrentBalance = addedEntitlement - 12.0;
							double currentBalance = yearlyEntitlement.getCurrentLeaveBalance();
							double addedCurrentBalance = currentBalance + addToCurrentBalance;
							yearlyEntitlement.setcurrentLeaveBalance(addedCurrentBalance);
							yearlyEntitlementService.update(yearlyEntitlement);
						}
						else if(joinDateday == 29 && joinDateMonth == 1  && currentDateDay == 1 && currentDateMonth == 2 && currentDateYear != joinDateYear){
							double entitlement =yearlyEntitlement.getEntitlement();
							double addedEntitlement = entitlement+1.00;
							System.out.println(addedEntitlement);
							yearlyEntitlement.setEntitlement(addedEntitlement);
							double addToCurrentBalance = addedEntitlement - 12.0;
							double currentBalance = yearlyEntitlement.getCurrentLeaveBalance();
							double addedCurrentBalance = currentBalance + addToCurrentBalance;
							yearlyEntitlement.setcurrentLeaveBalance(addedCurrentBalance);
							yearlyEntitlementService.update(yearlyEntitlement);
						}				
					}
					else if(yearlyEntitlement.getEntitlement() == 16){
						yearlyEntitlementService.update(yearlyEntitlement);
						if(currentDateMonth == joinDateMonth && currentDateYear != joinDateYear){
							double entitlement =yearlyEntitlement.getEntitlement();
							double addToCurrentBalance = entitlement - 12.0;
							double currentBalance = yearlyEntitlement.getCurrentLeaveBalance();
							double addedCurrentBalance = currentBalance + addToCurrentBalance;
							yearlyEntitlement.setcurrentLeaveBalance(addedCurrentBalance);
							yearlyEntitlementService.update(yearlyEntitlement);
						}
						else if(joinDateday == 29 && joinDateMonth == 1  && currentDateDay == 1 && currentDateMonth == 2 && currentDateYear != joinDateYear){
							double entitlement =yearlyEntitlement.getEntitlement();					
							double addToCurrentBalance = entitlement - 12.0;
							double currentBalance = yearlyEntitlement.getCurrentLeaveBalance();
							double addedCurrentBalance = currentBalance + addToCurrentBalance;
							yearlyEntitlement.setcurrentLeaveBalance(addedCurrentBalance);
							yearlyEntitlementService.update(yearlyEntitlement);
						}
					}			
				}
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

