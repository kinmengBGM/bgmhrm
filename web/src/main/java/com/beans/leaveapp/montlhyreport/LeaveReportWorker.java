package com.beans.leaveapp.montlhyreport;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.jbpm6.util.ApplicationContextProvider;
import com.beans.leaveapp.monthlyreport.service.MonthlyLeaveReportPrePreparation;
import com.beans.leaveapp.monthlyreport.service.SendMonthlyLeaveReportService;

public class LeaveReportWorker {
	
	
	public static void doSendLeaveReport(){
		//leaveReportService.sendMonthlyLeaveReport();
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		SendMonthlyLeaveReportService sendLeaveReportService = (SendMonthlyLeaveReportService) applicationContext.getBean("sendLeaveReportService");
		
		sendLeaveReportService.sendMonthlyLeaveReportToEmployees();
		sendLeaveReportService.sendMonthlyLeaveReportToHR();
	}
	
	
	public static void doInsertEmployeeYearlyData(){
		
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		MonthlyLeaveReportPrePreparation monthlyLeaveReportService = (MonthlyLeaveReportPrePreparation) applicationContext.getBean("monthlyLeaveReportService");
		
		List<Employee>  allEmployeeList =  monthlyLeaveReportService.getAllEmployees();
		
		if(allEmployeeList!=null && allEmployeeList.size()>0)
			for (Employee employee : allEmployeeList) {
				monthlyLeaveReportService.prepareAnnualLeaveDataForYearOfEmployee(employee);
				monthlyLeaveReportService.prepareAllLeaveDataForYearOfEmployee(employee);
			}
	}
	public static void doInitializeCurrentMonthLeaveRecords(){
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		SendMonthlyLeaveReportService sendLeaveReportService = (SendMonthlyLeaveReportService) applicationContext.getBean("sendLeaveReportService");
		
		sendLeaveReportService.initializeMonthlyLeaveReportWithDefaultValues();
	}
	
	

}
