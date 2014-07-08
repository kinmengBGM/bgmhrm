package com.beans.leaveapp.monthlyreport.service;

import java.util.List;

import com.beans.leaveapp.monthlyreport.model.AnnualLeaveReport;

public interface AnnualLeaveReportService {
	public AnnualLeaveReport update(AnnualLeaveReport annualLeaveReport);
	public List<AnnualLeaveReport> findAnnualLeaveReportByEmployeeId(int employeeId,int sortingMonthId,int financialYear);
	public AnnualLeaveReport findAnnualLeaveReport(int employeeId,int sortingMonthId,int financialYear);
}
