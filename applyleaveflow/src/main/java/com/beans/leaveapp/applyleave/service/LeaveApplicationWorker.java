package com.beans.leaveapp.applyleave.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.beans.exceptions.BSLException;
import com.beans.leaveapp.applyleave.model.ApprovalLevelModel;
import com.beans.leaveapp.jbpm6.util.ApplicationContextProvider;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetransaction.service.LeaveTransactionService;
import com.beans.leaveapp.monthlyreport.service.SendMonthlyLeaveReportService;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementService;

public class LeaveApplicationWorker {
	private static Logger log = Logger.getLogger(LeaveApplicationWorker.class);
	
	public static void sendingIntimationEmail(LeaveTransaction leaveTransaction,ApprovalLevelModel approvalBean,Boolean isApproverApproved,String approverName)
	{   
		try{
		LeaveApplicationSendingMailServiceImpl sendMailService = new LeaveApplicationSendingMailServiceImpl();
		log.info("Data coming from process is assignedRoleInLeaveFlow :");
		if(isApproverApproved==null){
			sendMailService.sendEmailNotificationToLeaveApplicant(leaveTransaction, isApproverApproved,approverName);
			sendMailService.sendEmailNotificationToLeaveApprover(leaveTransaction, approvalBean);
		}
		else{
			sendMailService.sendEmailNotificationToLeaveApplicant(leaveTransaction, isApproverApproved,approverName);
			sendMailService.sendEmailNotificationToHR(leaveTransaction, isApproverApproved,approverName);
		}
		}catch(BSLException e)
		{
			log.error("Error in LeaveApplicationWorker sending mail :",  e);
			e.printStackTrace();
			throw new BSLException(e.getMessage());
		}
	}
	
	public static void updateLeaveBalanceAfterApproval(LeaveTransaction leaveTransaction,Boolean isApproverApproved){
		log.info("Data coming from process is  leaveTransaction : "+leaveTransaction+" and isApproverApproved :"+isApproverApproved);
		if(leaveTransaction!=null && isApproverApproved==true)
		{
			ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
			YearlyEntitlementService yearlyEntitlementService = (YearlyEntitlementService) applicationContext.getBean("yearlyEntitlementService");
			yearlyEntitlementService.updateLeaveBalanceAfterApproval(leaveTransaction.getEmployee().getId(), leaveTransaction.getLeaveType().getId(), leaveTransaction.getNumberOfDays());
			SendMonthlyLeaveReportService annualLeaveService = (SendMonthlyLeaveReportService) applicationContext.getBean("sendLeaveReportService");
			annualLeaveService.updateEmployeeLeavesAfterLeaveApproval(leaveTransaction, leaveTransaction.getApplicationDate());
			
		}
	}
	
	public static void updateLeaveApplicationStatus(LeaveTransaction leaveTransaction,String approverName,Boolean isApproverApproved){
		
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		LeaveTransactionService leaveTransactionService = (LeaveTransactionService) applicationContext.getBean("leaveTransactionService");
		if(!isApproverApproved){
			leaveTransaction.setStatus("Rejected");
		}
		else{
			leaveTransaction.setStatus("Approved");
		}
			leaveTransaction.setYearlyLeaveBalance(leaveTransaction.getYearlyLeaveBalance()-leaveTransaction.getNumberOfDays());
			leaveTransaction.setLastModifiedBy(approverName);
			leaveTransaction.setLastModifiedTime(new Date());
		leaveTransactionService.updateLeaveApplicationStatus(leaveTransaction);
	}
	

}
