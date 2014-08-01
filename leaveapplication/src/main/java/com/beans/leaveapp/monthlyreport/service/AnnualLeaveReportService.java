package com.beans.leaveapp.monthlyreport.service;

import java.util.List;

import com.beans.leaveapp.monthlyreport.model.AnnualLeaveReport;

public interface AnnualLeaveReportService {
	 AnnualLeaveReport update(AnnualLeaveReport annualLeaveReport);
	 List<AnnualLeaveReport> findAnnualLeaveReportByEmployeeId(int employeeId,int sortingMonthId,int financialYear);
	 AnnualLeaveReport findAnnualLeaveReport(int employeeId,int sortingMonthId,int financialYear);
}
