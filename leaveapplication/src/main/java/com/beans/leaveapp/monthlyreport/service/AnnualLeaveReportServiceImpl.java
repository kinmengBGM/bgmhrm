package com.beans.leaveapp.monthlyreport.service;

import java.util.List;

import javax.annotation.Resource;

import com.beans.leaveapp.monthlyreport.model.AnnualLeaveReport;
import com.beans.leaveapp.monthlyreport.repository.AnnualLeaveReportRepository;

public class AnnualLeaveReportServiceImpl implements AnnualLeaveReportService {

	@Resource
	private AnnualLeaveReportRepository annualLeaveReportRepository;
	
	
	@Override
	public AnnualLeaveReport update(AnnualLeaveReport annualLeaveReport) {
			AnnualLeaveReport annualLeaveReportToBeUpdated = annualLeaveReport;
		
			if(annualLeaveReportToBeUpdated != null){
				
				annualLeaveReportToBeUpdated.setId(annualLeaveReportToBeUpdated.getId());
				annualLeaveReportToBeUpdated.setBalanceBroughtForward(annualLeaveReportToBeUpdated.getBalanceBroughtForward());
				annualLeaveReportToBeUpdated.setCurrentLeaveBalance(annualLeaveReport.getCurrentLeaveBalance());
				annualLeaveReportToBeUpdated.setLeavesCredited(annualLeaveReportToBeUpdated.getLeavesCredited());
				annualLeaveReportToBeUpdated.setFinancialYear(annualLeaveReportToBeUpdated.getFinancialYear());
				annualLeaveReportToBeUpdated.setYearlyLeaveBalance(annualLeaveReport.getYearlyLeaveBalance());
				annualLeaveReportToBeUpdated.setLeavesTaken(annualLeaveReportToBeUpdated.getLeavesTaken());
				annualLeaveReportToBeUpdated.setMonthOfYear(annualLeaveReportToBeUpdated.getMonthOfYear());
				annualLeaveReportToBeUpdated.setSortingMonthId(annualLeaveReportToBeUpdated.getSortingMonthId());
				annualLeaveReportToBeUpdated.setTimeInLieuCredited(annualLeaveReportToBeUpdated.getTimeInLieuCredited());				
			}
			annualLeaveReportRepository.save(annualLeaveReportToBeUpdated);
			return annualLeaveReportToBeUpdated;
	}


	@Override
	public List<AnnualLeaveReport> findAnnualLeaveReportByEmployeeId(int employeeId,int sortingMonthId,int financialYear) {
		List<AnnualLeaveReport> annuaLeaveReportList = annualLeaveReportRepository.getEmployeeMonthlyLeaveReportData(employeeId, sortingMonthId, financialYear);
		return annuaLeaveReportList;
	}


	@Override
	public AnnualLeaveReport findAnnualLeaveReport(int employeeId,int sortingMonthId, int financialYear) {
		 AnnualLeaveReport annualLeaveReport = annualLeaveReportRepository.findAnnualLeaveReportByEmployeeId(employeeId, sortingMonthId, financialYear);
		return annualLeaveReport;
	}	
	
}
