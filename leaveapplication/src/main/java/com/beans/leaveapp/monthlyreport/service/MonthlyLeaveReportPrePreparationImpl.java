package com.beans.leaveapp.monthlyreport.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.repository.EmployeeRepository;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.beans.leaveapp.leavetype.repository.LeaveTypeRepository;
import com.beans.leaveapp.monthlyreport.model.AnnualLeaveReport;
import com.beans.leaveapp.monthlyreport.model.MonthlyLeaveReport;
import com.beans.leaveapp.monthlyreport.repository.AnnualLeaveReportRepository;
import com.beans.leaveapp.monthlyreport.repository.MonthlyLeaveReportRepository;
import com.beans.util.enums.Leave;
@Service
public class MonthlyLeaveReportPrePreparationImpl implements MonthlyLeaveReportPrePreparation {

	@Resource
	EmployeeRepository employeeRepository;
	
	@Resource
	AnnualLeaveReportRepository annualLeaveReportRepository;
	
	@Resource
	MonthlyLeaveReportRepository monthlyLeaveReportRepository;
	
	@Resource
	LeaveTypeRepository leaveTypeRepository;
	
	@Override
	public void prepareAnnualLeaveDataForYearOfEmployee(Employee employee) {
		try{
		List<AnnualLeaveReport> annualLeaveReportList = new ArrayList<AnnualLeaveReport>();

		AnnualLeaveReport yearlyAnnualReportForJan = new AnnualLeaveReport();
		yearlyAnnualReportForJan.setSortingMonthId(1);
		yearlyAnnualReportForJan.setMonthOfYear("January");
		yearlyAnnualReportForJan.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
		yearlyAnnualReportForJan.setEmployee(employee);
		annualLeaveReportList.add(yearlyAnnualReportForJan);
		
		AnnualLeaveReport yearlyAnnualReportForFeb = new AnnualLeaveReport();
		yearlyAnnualReportForFeb.setSortingMonthId(2);
		yearlyAnnualReportForFeb.setMonthOfYear("February");
		yearlyAnnualReportForFeb.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
		yearlyAnnualReportForFeb.setEmployee(employee);
		annualLeaveReportList.add(yearlyAnnualReportForFeb);
		
		AnnualLeaveReport yearlyAnnualReportForMar = new AnnualLeaveReport();
		yearlyAnnualReportForMar.setSortingMonthId(3);
		yearlyAnnualReportForMar.setMonthOfYear("Mar");
		yearlyAnnualReportForMar.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
		yearlyAnnualReportForMar.setEmployee(employee);
		annualLeaveReportList.add(yearlyAnnualReportForMar);
		
		AnnualLeaveReport yearlyAnnualReportForApr = new AnnualLeaveReport();
		yearlyAnnualReportForApr.setSortingMonthId(4);
		yearlyAnnualReportForApr.setMonthOfYear("April");
		yearlyAnnualReportForApr.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
		yearlyAnnualReportForApr.setEmployee(employee);
		annualLeaveReportList.add(yearlyAnnualReportForApr);
		
		AnnualLeaveReport yearlyAnnualReportForMay = new AnnualLeaveReport();
		yearlyAnnualReportForMay.setSortingMonthId(5);
		yearlyAnnualReportForMay.setMonthOfYear("May");
		yearlyAnnualReportForMay.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
		yearlyAnnualReportForMay.setEmployee(employee);
		annualLeaveReportList.add(yearlyAnnualReportForMay);
		
		AnnualLeaveReport yearlyAnnualReportForJun = new AnnualLeaveReport();
		yearlyAnnualReportForJun.setSortingMonthId(6);
		yearlyAnnualReportForJun.setMonthOfYear("June");
		yearlyAnnualReportForJun.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
		yearlyAnnualReportForJun.setEmployee(employee);
		annualLeaveReportList.add(yearlyAnnualReportForJun);
		
		AnnualLeaveReport yearlyAnnualReportForJul = new AnnualLeaveReport();
		yearlyAnnualReportForJul.setSortingMonthId(7);
		yearlyAnnualReportForJul.setMonthOfYear("July");
		yearlyAnnualReportForJul.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
		yearlyAnnualReportForJul.setEmployee(employee);
		annualLeaveReportList.add(yearlyAnnualReportForJul);
		
		AnnualLeaveReport yearlyAnnualReportForAug = new AnnualLeaveReport();
		yearlyAnnualReportForAug.setSortingMonthId(8);
		yearlyAnnualReportForAug.setMonthOfYear("August");
		yearlyAnnualReportForAug.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
		yearlyAnnualReportForAug.setEmployee(employee);
		annualLeaveReportList.add(yearlyAnnualReportForAug);
		
		AnnualLeaveReport yearlyAnnualReportForSep = new AnnualLeaveReport();
		yearlyAnnualReportForSep.setSortingMonthId(9);
		yearlyAnnualReportForSep.setMonthOfYear("September");
		yearlyAnnualReportForSep.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
		yearlyAnnualReportForSep.setEmployee(employee);
		annualLeaveReportList.add(yearlyAnnualReportForSep);
		
		AnnualLeaveReport yearlyAnnualReportForOct = new AnnualLeaveReport();
		yearlyAnnualReportForOct.setSortingMonthId(10);
		yearlyAnnualReportForOct.setMonthOfYear("October");
		yearlyAnnualReportForOct.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
		yearlyAnnualReportForOct.setEmployee(employee);
		annualLeaveReportList.add(yearlyAnnualReportForOct);
		
		AnnualLeaveReport yearlyAnnualReportForNov = new AnnualLeaveReport();
		yearlyAnnualReportForNov.setSortingMonthId(11);
		yearlyAnnualReportForNov.setMonthOfYear("November");
		yearlyAnnualReportForNov.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
		yearlyAnnualReportForNov.setEmployee(employee);
		annualLeaveReportList.add(yearlyAnnualReportForNov);
		
		AnnualLeaveReport yearlyAnnualReportForDec = new AnnualLeaveReport();
		yearlyAnnualReportForDec.setSortingMonthId(12);
		yearlyAnnualReportForDec.setMonthOfYear("December");
		yearlyAnnualReportForDec.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
		yearlyAnnualReportForDec.setEmployee(employee);
		annualLeaveReportList.add(yearlyAnnualReportForDec);
		
		AnnualLeaveReport yearlyAnnualReportForTot = new AnnualLeaveReport();
		yearlyAnnualReportForTot.setSortingMonthId(13);
		yearlyAnnualReportForTot.setMonthOfYear("Total");
		yearlyAnnualReportForTot.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
		yearlyAnnualReportForTot.setEmployee(employee);
		annualLeaveReportList.add(yearlyAnnualReportForTot);
		
		// Saving all the 13 beans of specific Employee
		annualLeaveReportRepository.save(annualLeaveReportList);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void prepareAllLeaveDataForYearOfEmployee(Employee employee) {
		try{
		
		List<LeaveType>	 employeeLeaveList = leaveTypeRepository.getAllLeaveTypesGivenByEmployeeType(employee.getId());
		if(employeeLeaveList!=null && employeeLeaveList.size()>0){
			for (LeaveType leaveType : employeeLeaveList) {
				// Discard inserting data for Leave Type Maternity but Gender is F and vice versa
				if((Leave.MATERNITY.equalsName(leaveType.getName())&& "M".equalsIgnoreCase(employee.getGender())) || (Leave.PATERNITY.equalsName(leaveType.getName())&& "F".equalsIgnoreCase(employee.getGender())))
					continue;
				List<MonthlyLeaveReport> yearlyLeaveReportList = new ArrayList<MonthlyLeaveReport>();
				
				MonthlyLeaveReport yearlyLeaveReportForJan = new MonthlyLeaveReport();
				yearlyLeaveReportForJan.setSortingMonthId(1);
				yearlyLeaveReportForJan.setMonthOfYear("January");
				yearlyLeaveReportForJan.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
				yearlyLeaveReportForJan.setEmployee(employee);
				yearlyLeaveReportForJan.setLeaveType(leaveType);
				yearlyLeaveReportList.add(yearlyLeaveReportForJan);
				
				MonthlyLeaveReport yearlyLeaveReportForFeb = new MonthlyLeaveReport();
				yearlyLeaveReportForFeb.setSortingMonthId(2);
				yearlyLeaveReportForFeb.setMonthOfYear("February");
				yearlyLeaveReportForFeb.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
				yearlyLeaveReportForFeb.setEmployee(employee);
				yearlyLeaveReportForFeb.setLeaveType(leaveType);
				yearlyLeaveReportList.add(yearlyLeaveReportForFeb);
				
				MonthlyLeaveReport yearlyLeaveReportForMar = new MonthlyLeaveReport();
				yearlyLeaveReportForMar.setSortingMonthId(3);
				yearlyLeaveReportForMar.setMonthOfYear("March");
				yearlyLeaveReportForMar.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
				yearlyLeaveReportForMar.setEmployee(employee);
				yearlyLeaveReportForMar.setLeaveType(leaveType);
				yearlyLeaveReportList.add(yearlyLeaveReportForMar);
				
				MonthlyLeaveReport yearlyLeaveReportForApr = new MonthlyLeaveReport();
				yearlyLeaveReportForApr.setSortingMonthId(4);
				yearlyLeaveReportForApr.setMonthOfYear("April");
				yearlyLeaveReportForApr.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
				yearlyLeaveReportForApr.setEmployee(employee);
				yearlyLeaveReportForApr.setLeaveType(leaveType);
				yearlyLeaveReportList.add(yearlyLeaveReportForApr);
				
				MonthlyLeaveReport yearlyLeaveReportForMay = new MonthlyLeaveReport();
				yearlyLeaveReportForMay.setSortingMonthId(5);
				yearlyLeaveReportForMay.setMonthOfYear("May");
				yearlyLeaveReportForMay.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
				yearlyLeaveReportForMay.setEmployee(employee);
				yearlyLeaveReportForMay.setLeaveType(leaveType);
				yearlyLeaveReportList.add(yearlyLeaveReportForMay);
				
				MonthlyLeaveReport yearlyLeaveReportForJun = new MonthlyLeaveReport();
				yearlyLeaveReportForJun.setSortingMonthId(6);
				yearlyLeaveReportForJun.setMonthOfYear("June");
				yearlyLeaveReportForJun.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
				yearlyLeaveReportForJun.setEmployee(employee);
				yearlyLeaveReportForJun.setLeaveType(leaveType);
				yearlyLeaveReportList.add(yearlyLeaveReportForJun);
				
				MonthlyLeaveReport yearlyLeaveReportForJul = new MonthlyLeaveReport();
				yearlyLeaveReportForJul.setSortingMonthId(7);
				yearlyLeaveReportForJul.setMonthOfYear("July");
				yearlyLeaveReportForJul.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
				yearlyLeaveReportForJul.setEmployee(employee);
				yearlyLeaveReportForJul.setLeaveType(leaveType);
				yearlyLeaveReportList.add(yearlyLeaveReportForJul);
				
				MonthlyLeaveReport yearlyLeaveReportForAug = new MonthlyLeaveReport();
				yearlyLeaveReportForAug.setSortingMonthId(8);
				yearlyLeaveReportForAug.setMonthOfYear("August");
				yearlyLeaveReportForAug.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
				yearlyLeaveReportForAug.setEmployee(employee);
				yearlyLeaveReportForAug.setLeaveType(leaveType);
				yearlyLeaveReportList.add(yearlyLeaveReportForAug);
				
				MonthlyLeaveReport yearlyLeaveReportForSep = new MonthlyLeaveReport();
				yearlyLeaveReportForSep.setSortingMonthId(9);
				yearlyLeaveReportForSep.setMonthOfYear("September");
				yearlyLeaveReportForSep.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
				yearlyLeaveReportForSep.setEmployee(employee);
				yearlyLeaveReportForSep.setLeaveType(leaveType);
				yearlyLeaveReportList.add(yearlyLeaveReportForSep);
				
				MonthlyLeaveReport yearlyLeaveReportForOct = new MonthlyLeaveReport();
				yearlyLeaveReportForOct.setSortingMonthId(10);
				yearlyLeaveReportForOct.setMonthOfYear("October");
				yearlyLeaveReportForOct.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
				yearlyLeaveReportForOct.setEmployee(employee);
				yearlyLeaveReportForOct.setLeaveType(leaveType);
				yearlyLeaveReportList.add(yearlyLeaveReportForOct);
				
				MonthlyLeaveReport yearlyLeaveReportForNov = new MonthlyLeaveReport();
				yearlyLeaveReportForNov.setSortingMonthId(11);
				yearlyLeaveReportForNov.setMonthOfYear("November");
				yearlyLeaveReportForNov.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
				yearlyLeaveReportForNov.setEmployee(employee);
				yearlyLeaveReportForNov.setLeaveType(leaveType);
				yearlyLeaveReportList.add(yearlyLeaveReportForNov);
				
				MonthlyLeaveReport yearlyLeaveReportForDec = new MonthlyLeaveReport();
				yearlyLeaveReportForDec.setSortingMonthId(12);
				yearlyLeaveReportForDec.setMonthOfYear("December");
				yearlyLeaveReportForDec.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
				yearlyLeaveReportForDec.setEmployee(employee);
				yearlyLeaveReportForDec.setLeaveType(leaveType);
				yearlyLeaveReportList.add(yearlyLeaveReportForDec);
				
				MonthlyLeaveReport yearlyLeaveReportForTot = new MonthlyLeaveReport();
				yearlyLeaveReportForTot.setSortingMonthId(13);
				yearlyLeaveReportForTot.setMonthOfYear("Total");
				yearlyLeaveReportForTot.setFinancialYear(Calendar.getInstance().get(Calendar.YEAR));
				yearlyLeaveReportForTot.setEmployee(employee);
				yearlyLeaveReportForTot.setLeaveType(leaveType);
				yearlyLeaveReportList.add(yearlyLeaveReportForTot);
				
				// Saving all the 13 beans of specific Employee
				monthlyLeaveReportRepository.save(yearlyLeaveReportList);
			}
		
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<Employee> getAllEmployees(){
		return employeeRepository.findAllEmployeesForSendingMonthlyLeaveReport();
	}	

	
}
