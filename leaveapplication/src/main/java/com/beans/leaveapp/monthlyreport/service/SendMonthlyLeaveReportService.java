package com.beans.leaveapp.monthlyreport.service;

import java.util.Date;

import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;


public interface SendMonthlyLeaveReportService {
	
	void sendMonthlyLeaveReportToEmployees();
	
	void sendMonthlyLeaveReportToHR();
	
	void updateEmployeeLeavesAfterLeaveApproval(LeaveTransaction leaveTransaction,Date applicationDate);
	
	void initializeMonthlyLeaveReportWithDefaultValues();
	
}
