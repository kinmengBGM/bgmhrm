package com.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.monthlyreport.service.SendMonthlyLeaveReportService;

@RestController
@RequestMapping("/sendMonthlyLeaveReport")
public class SendMonthlyLeaveReportController {

	@Autowired
	private SendMonthlyLeaveReportService sendLeaveReportService;
	
	@RequestMapping(value="/sendMonthlyLeaveReportToEmployees", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void sendMonthlyLeaveReportToEmployees(){
		sendLeaveReportService.sendMonthlyLeaveReportToEmployees();
	
	}
	
	@RequestMapping(value="/sendMonthlyLeaveReportToHR", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void sendMonthlyLeaveReportToHR(){
		sendLeaveReportService.sendMonthlyLeaveReportToHR();
	}
	
	@RequestMapping(value="/updateEmployeeLeavesAfterLeaveApproval", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void updateEmployeeLeavesAfterLeaveApproval(LeaveTransaction leaveTransaction,Date applicationDate){
		sendLeaveReportService.updateEmployeeLeavesAfterLeaveApproval(leaveTransaction, applicationDate);
	}
	
	@RequestMapping(value="/updateEmployeeAnnualLeavesAfterLeaveApproval", method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.OK)
	public void updateEmployeeAnnualLeavesAfterLeaveApproval(LeaveTransaction leaveTransaction,Date applicationDate){
		sendLeaveReportService.updateEmployeeAnnualLeavesAfterLeaveApproval(leaveTransaction, applicationDate);
	}
	
	@RequestMapping(value="/initializeMonthlyLeaveReportWithDefaultValues")
	@ResponseStatus(value = HttpStatus.OK)
	public void initializeMonthlyLeaveReportWithDefaultValues(){
		sendLeaveReportService.initializeMonthlyLeaveReportWithDefaultValues();
	}
	
	@RequestMapping(value="/updateLeaveBalanceAfterCancelled", method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.OK)
	public void updateLeaveBalanceAfterCancelled(@RequestBody LeaveTransaction leaveTransaction){
		sendLeaveReportService.updateLeaveBalanceAfterCancelled(leaveTransaction);
	}

	@RequestMapping(value="/initializeMonthlyLeaveReportForNewEmployee", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void initializeMonthlyLeaveReportForNewEmployee(Employee employee){
		sendLeaveReportService.initializeMonthlyLeaveReportForNewEmployee(employee);

	}

}
