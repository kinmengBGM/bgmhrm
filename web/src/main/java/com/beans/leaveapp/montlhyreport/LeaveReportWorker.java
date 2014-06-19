package com.beans.leaveapp.montlhyreport;

import org.springframework.context.ApplicationContext;

import com.beans.leaveapp.jbpm6.util.ApplicationContextProvider;
import com.beans.leaveapp.monthlyreport.SendMonthlyLeaveReportService;

public class LeaveReportWorker {
	
	
	public static void doSendLeaveReport(){
		//leaveReportService.sendMonthlyLeaveReport();
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		SendMonthlyLeaveReportService sendLeaveReportService = (SendMonthlyLeaveReportService) applicationContext.getBean("sendLeaveReportService");
		
		sendLeaveReportService.sendMonthlyLeaveReportToEmployees();
	}

}
