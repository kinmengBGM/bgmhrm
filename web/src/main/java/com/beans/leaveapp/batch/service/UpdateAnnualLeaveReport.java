package com.beans.leaveapp.batch.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.service.EmployeeService;
import com.beans.leaveapp.jbpm6.util.ApplicationContextProvider;
import com.beans.leaveapp.monthlyreport.model.AnnualLeaveReport;
import com.beans.leaveapp.monthlyreport.service.AnnualLeaveReportService;

public class UpdateAnnualLeaveReport implements Serializable {
 
private static final long serialVersionUID = 1L;
	
	ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
	EmployeeService employeeService = (EmployeeService) applicationContext.getBean("employeeService");
	AnnualLeaveReportService annualLeaveReportService = (AnnualLeaveReportService) applicationContext.getBean("annualLeaveReportService");   

public void UpdatingAnnualLeaveReport(){
	
	
	int currentDateMonth;
	int currentDateYear;
	int joinDateMonth;
	int joinDateYear;		
	int sortingMonthId;	
	int beforeYear;
	int decemberMonthid;
	
	Date currentDate = new Date();		
	Calendar cal = Calendar.getInstance();
	cal.setTime(currentDate);
	currentDateMonth = cal.get(Calendar.MONTH);
	currentDateYear = cal.get(Calendar.YEAR);
	
	sortingMonthId = currentDateMonth+1;
	beforeYear = currentDateYear-1;
	decemberMonthid = 12;
	
	List<Employee> employeeList = employeeService.findByEmployeeTypePermAndCont();
	for(Employee employee:employeeList){
		
		Date joinDate;
		joinDate = employee.getJoinDate();
		if(joinDate != null){
		cal.setTime(joinDate);
		joinDateMonth = cal.get(Calendar.MONTH);
		joinDateYear = cal.get(Calendar.YEAR);		
	
		int yearBalanceRemaining;
		yearBalanceRemaining = currentDateYear - joinDateYear;
		
		double noOfLeavesCredited;		
		
		if(currentDateMonth == joinDateMonth  && currentDateYear != joinDateYear)
		{
			
			if(sortingMonthId == 1){
				AnnualLeaveReport annualLeaveReportDec = annualLeaveReportService.findAnnualLeaveReport(employee.getId(), decemberMonthid, beforeYear);
				double leavesBroughtForward;
				leavesBroughtForward = annualLeaveReportDec.getCurrentLeaveBalance();
				List<AnnualLeaveReport> annualLeaveReportList = annualLeaveReportService.findAnnualLeaveReportByEmployeeId(employee.getId(), sortingMonthId, currentDateYear);
				ArrayList<AnnualLeaveReport> annualLeaveToBeUpdatedList = new ArrayList<AnnualLeaveReport>();				
					AnnualLeaveReport annualLeaveReportCurrentMonth =	annualLeaveReportList.get(0);
					annualLeaveReportCurrentMonth.setBalanceBroughtForward(leavesBroughtForward);
				if(employee.getEmployeeType().getName().equals("PERM")){	
				if(yearBalanceRemaining <= 4){
					noOfLeavesCredited = yearBalanceRemaining+1.0;
					annualLeaveReportCurrentMonth.setLeavesCredited(noOfLeavesCredited);
					double currentBalance = annualLeaveReportCurrentMonth.getCurrentLeaveBalance();
					double currentBalanceWithAddedLeave = currentBalance+noOfLeavesCredited;					
					annualLeaveReportCurrentMonth.setCurrentLeaveBalance(currentBalanceWithAddedLeave);
					double yearlyLeaveBalance = currentBalance+yearBalanceRemaining+12.0;
					annualLeaveReportCurrentMonth.setYearlyLeaveBalance(yearlyLeaveBalance);
				}
				else
				{
					noOfLeavesCredited = 4.0+1.0;
					annualLeaveReportCurrentMonth.setLeavesCredited(noOfLeavesCredited);
					double currentBalance = annualLeaveReportCurrentMonth.getCurrentLeaveBalance();
					double currentBalanceWithAddedLeave = currentBalance+noOfLeavesCredited;
					double yearlyLeaveBalance = currentBalance+4.0+12.0;
					annualLeaveReportCurrentMonth.setCurrentLeaveBalance(currentBalanceWithAddedLeave);
					annualLeaveReportCurrentMonth.setYearlyLeaveBalance(yearlyLeaveBalance);
					
				}
					
					annualLeaveToBeUpdatedList.add(annualLeaveReportCurrentMonth);
					AnnualLeaveReport annualLeaveReportTotal =	annualLeaveReportList.get(1);
					double noOfLeavesCreditTotal = annualLeaveReportTotal.getLeavesCredited();
					double leavesCreditedTotal = noOfLeavesCredited + noOfLeavesCreditTotal;
					annualLeaveReportTotal.setLeavesCredited(leavesCreditedTotal);
					annualLeaveReportTotal.setBalanceBroughtForward(leavesBroughtForward);
					annualLeaveToBeUpdatedList.add(annualLeaveReportTotal);
					
					for(AnnualLeaveReport annualLeaveReport : annualLeaveToBeUpdatedList){
						annualLeaveReportService.update(annualLeaveReport);
					}
				}
				else {
					noOfLeavesCredited = 1.0;
				    annualLeaveReportCurrentMonth.setLeavesCredited(noOfLeavesCredited);
				    double currentBalance = annualLeaveReportCurrentMonth.getCurrentLeaveBalance();
					double currentBalanceWithAddedLeave = currentBalance+noOfLeavesCredited;					
					annualLeaveReportCurrentMonth.setCurrentLeaveBalance(currentBalanceWithAddedLeave);
					double yearlyLeaveBalance = currentBalance+12.0;
					annualLeaveReportCurrentMonth.setYearlyLeaveBalance(yearlyLeaveBalance);
					annualLeaveToBeUpdatedList.add(annualLeaveReportCurrentMonth);
					AnnualLeaveReport annualLeaveReportTotal =	annualLeaveReportList.get(1);
					double noOfLeavesCreditTotal = annualLeaveReportTotal.getLeavesCredited();
					double leavesCreditedTotal = noOfLeavesCredited + noOfLeavesCreditTotal;
					annualLeaveReportTotal.setLeavesCredited(leavesCreditedTotal);
					annualLeaveReportTotal.setBalanceBroughtForward(leavesBroughtForward);
					annualLeaveToBeUpdatedList.add(annualLeaveReportTotal);
					for(AnnualLeaveReport annualLeaveReport : annualLeaveToBeUpdatedList){
						annualLeaveReportService.update(annualLeaveReport);
					}
			}	
									
			}
			else 
			{
				List<AnnualLeaveReport> annualLeaveReportList = annualLeaveReportService.findAnnualLeaveReportByEmployeeId(employee.getId(), sortingMonthId, currentDateYear);
				ArrayList<AnnualLeaveReport> annualLeaveToBeUpdatedList = new ArrayList<AnnualLeaveReport>();
				
				AnnualLeaveReport annualLeaveReportCurrentMonth =	annualLeaveReportList.get(0);				
				if(employee.getEmployeeType().getName().equals("PERM")){
				if(yearBalanceRemaining <= 4){
					noOfLeavesCredited = yearBalanceRemaining+1.0;
					annualLeaveReportCurrentMonth.setLeavesCredited(noOfLeavesCredited);
					double currentBalance = annualLeaveReportCurrentMonth.getCurrentLeaveBalance();
					double currentBalanceWithAddedLeave = currentBalance+noOfLeavesCredited;
					annualLeaveReportCurrentMonth.setCurrentLeaveBalance(currentBalanceWithAddedLeave);
				}
				else
				{
					noOfLeavesCredited = 4.0+1.0;
					annualLeaveReportCurrentMonth.setLeavesCredited(noOfLeavesCredited);
					double currentBalance = annualLeaveReportCurrentMonth.getCurrentLeaveBalance();
					double currentBalanceWithAddedLeave = currentBalance+noOfLeavesCredited;
					annualLeaveReportCurrentMonth.setCurrentLeaveBalance(currentBalanceWithAddedLeave);
				}
					annualLeaveToBeUpdatedList.add(annualLeaveReportCurrentMonth);
					AnnualLeaveReport annualLeaveReportTotal =	annualLeaveReportList.get(1);
					double noOfLeavesCreditTotal = annualLeaveReportTotal.getLeavesCredited();
					double leavesCreditedTotal = noOfLeavesCredited + noOfLeavesCreditTotal;
					annualLeaveReportTotal.setLeavesCredited(leavesCreditedTotal);
					annualLeaveToBeUpdatedList.add(annualLeaveReportTotal);
					for(AnnualLeaveReport annualLeaveReport : annualLeaveToBeUpdatedList){
						annualLeaveReportService.update(annualLeaveReport);
					}
				}
				else {
					noOfLeavesCredited = 1.0;
					annualLeaveReportCurrentMonth.setLeavesCredited(noOfLeavesCredited);
					double currentBalance = annualLeaveReportCurrentMonth.getCurrentLeaveBalance();
					double currentBalanceWithAddedLeave = currentBalance+noOfLeavesCredited;
					annualLeaveReportCurrentMonth.setCurrentLeaveBalance(currentBalanceWithAddedLeave);
					annualLeaveToBeUpdatedList.add(annualLeaveReportCurrentMonth);
					AnnualLeaveReport annualLeaveReportTotal =	annualLeaveReportList.get(1);
					double noOfLeavesCreditTotal = annualLeaveReportTotal.getLeavesCredited();
					double leavesCreditedTotal = noOfLeavesCredited + noOfLeavesCreditTotal;
					annualLeaveReportTotal.setLeavesCredited(leavesCreditedTotal);
					annualLeaveToBeUpdatedList.add(annualLeaveReportTotal);
					for(AnnualLeaveReport annualLeaveReport : annualLeaveToBeUpdatedList){
						annualLeaveReportService.update(annualLeaveReport);
					}
					
					}
				}
		}		
		else
		{
			
			
			if(sortingMonthId == 1){
				AnnualLeaveReport annualLeaveReportDec = annualLeaveReportService.findAnnualLeaveReport(employee.getId(), decemberMonthid, beforeYear);
				double leavesBroughtForward;
				leavesBroughtForward = annualLeaveReportDec.getCurrentLeaveBalance();
				List<AnnualLeaveReport> annualLeaveReportList = annualLeaveReportService.findAnnualLeaveReportByEmployeeId(employee.getId(), sortingMonthId, currentDateYear);
				ArrayList<AnnualLeaveReport> annualLeaveToBeUpdatedList = new ArrayList<AnnualLeaveReport>();
				
				AnnualLeaveReport annualLeaveReportCurrentMonth =	annualLeaveReportList.get(0);				
				annualLeaveReportCurrentMonth.setBalanceBroughtForward(leavesBroughtForward);
				if(employee.getEmployeeType().getName().equals("PERM")){
				if(yearBalanceRemaining <= 4){
					noOfLeavesCredited = 1.0;
					annualLeaveReportCurrentMonth.setLeavesCredited(noOfLeavesCredited);
					double currentBalance = annualLeaveReportCurrentMonth.getCurrentLeaveBalance();
					double currentBalanceWithAddedLeave = currentBalance+1.0;
					double yearlyLeaveBalance = currentBalance+yearBalanceRemaining+12.0;
					annualLeaveReportCurrentMonth.setCurrentLeaveBalance(currentBalanceWithAddedLeave);
					annualLeaveReportCurrentMonth.setYearlyLeaveBalance(yearlyLeaveBalance);
				}
				else
				{
					noOfLeavesCredited = 1.0;
					annualLeaveReportCurrentMonth.setLeavesCredited(noOfLeavesCredited);
					double currentBalance = annualLeaveReportCurrentMonth.getCurrentLeaveBalance();
					double currentBalanceWithAddedLeave = currentBalance+1.0;
					double yearlyLeaveBalance = currentBalance+4.0+12.0;
					annualLeaveReportCurrentMonth.setCurrentLeaveBalance(currentBalanceWithAddedLeave);
					annualLeaveReportCurrentMonth.setYearlyLeaveBalance(yearlyLeaveBalance);
					
				}			
				annualLeaveToBeUpdatedList.add(annualLeaveReportCurrentMonth);
				AnnualLeaveReport annualLeaveReportTotal =	annualLeaveReportList.get(1);
				double noOfLeavesCreditTotal = annualLeaveReportTotal.getLeavesCredited();
				double leavesCreditedTotal = noOfLeavesCredited + noOfLeavesCreditTotal;
				annualLeaveReportTotal.setLeavesCredited(leavesCreditedTotal);
				annualLeaveReportTotal.setBalanceBroughtForward(leavesBroughtForward);
				annualLeaveToBeUpdatedList.add(annualLeaveReportTotal);
				for(AnnualLeaveReport annualLeaveReport : annualLeaveToBeUpdatedList){
					annualLeaveReportService.update(annualLeaveReport);
				}
			} else {
				noOfLeavesCredited = 1.0;
				annualLeaveReportCurrentMonth.setLeavesCredited(noOfLeavesCredited);
				double currentBalance = annualLeaveReportCurrentMonth.getCurrentLeaveBalance();
				double currentBalanceWithAddedLeave = currentBalance+1.0;
				double yearlyLeaveBalance = currentBalance+12.0;
				annualLeaveReportCurrentMonth.setCurrentLeaveBalance(currentBalanceWithAddedLeave);
				annualLeaveReportCurrentMonth.setYearlyLeaveBalance(yearlyLeaveBalance);
				annualLeaveToBeUpdatedList.add(annualLeaveReportCurrentMonth);
				AnnualLeaveReport annualLeaveReportTotal =	annualLeaveReportList.get(1);
				double noOfLeavesCreditTotal = annualLeaveReportTotal.getLeavesCredited();
				double leavesCreditedTotal = noOfLeavesCredited + noOfLeavesCreditTotal;
				annualLeaveReportTotal.setLeavesCredited(leavesCreditedTotal);
				annualLeaveReportTotal.setBalanceBroughtForward(leavesBroughtForward);
				annualLeaveToBeUpdatedList.add(annualLeaveReportTotal);
				for(AnnualLeaveReport annualLeaveReport : annualLeaveToBeUpdatedList){
					annualLeaveReportService.update(annualLeaveReport);
				}
				}
			}
			else
			{
			List<AnnualLeaveReport> annualLeaveReportList = annualLeaveReportService.findAnnualLeaveReportByEmployeeId(employee.getId(), sortingMonthId, currentDateYear);
			ArrayList<AnnualLeaveReport> annualLeaveToBeUpdatedList = new ArrayList<AnnualLeaveReport>();
			
			AnnualLeaveReport annualLeaveReportCurrentMonth =	annualLeaveReportList.get(0);			
			double currentBalance = annualLeaveReportCurrentMonth.getCurrentLeaveBalance();
			double currentBalanceWithAddedLeave = currentBalance+1.0;
			annualLeaveReportCurrentMonth.setCurrentLeaveBalance(currentBalanceWithAddedLeave);			
			noOfLeavesCredited = 1.0;
			annualLeaveReportCurrentMonth.setLeavesCredited(noOfLeavesCredited);
			annualLeaveToBeUpdatedList.add(annualLeaveReportCurrentMonth);
			AnnualLeaveReport annualLeaveReportTotal =	annualLeaveReportList.get(1);
			double noOfLeavesCreditTotal = annualLeaveReportTotal.getLeavesCredited();
			double leavesCreditedTotal = noOfLeavesCredited + noOfLeavesCreditTotal;
			annualLeaveReportTotal.setLeavesCredited(leavesCreditedTotal);
			annualLeaveToBeUpdatedList.add(annualLeaveReportTotal);
			for(AnnualLeaveReport annualLeaveReport : annualLeaveToBeUpdatedList){
				annualLeaveReportService.update(annualLeaveReport);
			}
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

public AnnualLeaveReportService getAnnualLeaveReportService() {
	return annualLeaveReportService;
}

public void setAnnualLeaveReportService(
		AnnualLeaveReportService annualLeaveReportService) {
	this.annualLeaveReportService = annualLeaveReportService;
}




}
