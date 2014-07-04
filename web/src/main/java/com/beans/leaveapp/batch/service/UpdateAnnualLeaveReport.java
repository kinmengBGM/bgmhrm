package com.beans.leaveapp.batch.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.service.EmployeeService;
import com.beans.leaveapp.jbpm6.util.ApplicationContextProvider;
import com.beans.leaveapp.monthlyreport.model.AnnualLeaveReport;
import com.beans.leaveapp.monthlyreport.service.MonthlyLeaveReportPrePreparation;

public class UpdateAnnualLeaveReport implements Serializable {
 
private static final long serialVersionUID = 1L;
	
	ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
	EmployeeService employeeService = (EmployeeService) applicationContext.getBean("employeeService");
	MonthlyLeaveReportPrePreparation monthlyLeaveReportPrePreparation = (MonthlyLeaveReportPrePreparation) applicationContext.getBean("monthlyLeaveReportPrePreparation");   

public void UpdatingAnnualLeaveReport(){
	
	List<Employee> employeeList = employeeService.findByEmployeeTypePermAndCont();
	for(Employee employee:employeeList){
		
		/*AnnualLeaveReport annualLeaveReport = monthlyLeaveReportPrePreparation.findAnnualLeaveReportByEmployeeId(employee.getId());
		double currentLeaveBalance = annualLeaveReport.getCurrentLeaveBalance();
		double currentLeaveBalanceWithAddedLeave = currentLeaveBalance + 1.0;
		annualLeaveReport.setCurrentLeaveBalance(currentLeaveBalanceWithAddedLeave);*/
		
		
	}
}




}
