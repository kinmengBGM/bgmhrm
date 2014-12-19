package com.beans.leaveapp.applyleave.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.beans.exceptions.BSLException;
import com.beans.leaveapp.applyleave.model.ApprovalLevelModel;
import com.beans.leaveapp.applyleave.model.TimeInLieuBean;
import com.beans.leaveapp.jbpm6.util.ApplicationContextProvider;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetransaction.service.LeaveTransactionService;
import com.beans.leaveapp.monthlyreport.service.SendMonthlyLeaveReportService;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementService;
import com.beans.util.enums.Leave;
import com.beans.util.log.ApplLogger;

public class LeaveApplicationWorker {
	private static Logger log = Logger.getLogger(LeaveApplicationWorker.class);
	
	public static void sendingIntimationEmail(LeaveTransaction leaveTransaction,ApprovalLevelModel approvalBean,TimeInLieuBean timeInLieuBean)
	{   
		try{
		LeaveApplicationSendingMailServiceImpl sendMailService = new LeaveApplicationSendingMailServiceImpl();
		log.info("Data coming from process is assignedRoleInLeaveFlow :");
		if(timeInLieuBean.getIsFirstApproverApproved()==null){
			sendMailService.sendEmailNotificationToLeaveApplicant(leaveTransaction, timeInLieuBean);
			sendMailService.sendEmailNotificationToLeaveApprover(leaveTransaction, approvalBean,timeInLieuBean);
		}
		else if (timeInLieuBean.getIsFirstApproverApproved()!=null && timeInLieuBean.getIsFirstApproverApproved() && Leave.TIMEINLIEU.equalsName(leaveTransaction.getLeaveType().getName()) && timeInLieuBean.getIsSecondApproverApproved()==null && "ROLE_TEAMLEAD".equalsIgnoreCase(approvalBean.getApprover()) ){
			sendMailService.sendEmailNotificationToLeaveApprover(leaveTransaction, approvalBean,timeInLieuBean);
		}
		else{
			sendMailService.sendEmailNotificationToLeaveApplicant(leaveTransaction, timeInLieuBean);
			sendMailService.sendEmailNotificationToHR(leaveTransaction, timeInLieuBean);
		}
		}catch(BSLException e)
		{
			log.error("Error in LeaveApplicationWorker sending mail :",  e);
			e.printStackTrace();
			throw new BSLException(e.getMessage());
		}
	}
	
	public static void sendCancelLeaveMail(LeaveTransaction leaveTransaction, String hrName){
		LeaveApplicationSendingMailServiceImpl sendMailService = new LeaveApplicationSendingMailServiceImpl();
		sendMailService.sendEmailNotificationForCancelLeave(leaveTransaction, hrName);
	}
	
	public static void sendTerminateMail(String leaveApplicant,String emailId){
		LeaveApplicationSendingMailServiceImpl sendMailService = new LeaveApplicationSendingMailServiceImpl();
		sendMailService.sendLeaveTerminateMailToApplicant(leaveApplicant, emailId);
	}
	
	// This is used by JBPM
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
	
	// This is used by our own process engine dealing with DB
	public static void updateLeaveBalanceAfterApprovalNonTimeInLieuLeave(LeaveTransaction leaveTransaction){
		ApplLogger.getLogger().info(String.format("Finally Leave is approved and updating to Yearly Balance in Entitlement, leaveTransaction object is : %s",leaveTransaction));
		if(leaveTransaction!=null)
		{
			ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
			YearlyEntitlementService yearlyEntitlementService = (YearlyEntitlementService) applicationContext.getBean("yearlyEntitlementService");
			yearlyEntitlementService.updateLeaveBalanceAfterApproval(leaveTransaction.getEmployee().getId(), leaveTransaction.getLeaveType().getId(), leaveTransaction.getNumberOfDays());
			SendMonthlyLeaveReportService annualLeaveService = (SendMonthlyLeaveReportService) applicationContext.getBean("sendLeaveReportService");
			annualLeaveService.updateEmployeeLeavesAfterLeaveApproval(leaveTransaction, leaveTransaction.getApplicationDate());
			
		}
	}
	
	// This is used by JBPM
	public static void updateToAnnualLeaveBalanceAfterApproval(LeaveTransaction leaveTransaction,TimeInLieuBean timeInLieuBean){
		log.info("Data coming from process is  leaveTransaction : "+leaveTransaction+" and approval bean :"+timeInLieuBean);
		if(leaveTransaction!=null && timeInLieuBean!=null && (timeInLieuBean.getIsSecondApproverApproved()==null || timeInLieuBean.getIsSecondApproverApproved()))
		{
			ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
			YearlyEntitlementService yearlyEntitlementService = (YearlyEntitlementService) applicationContext.getBean("yearlyEntitlementService");
			yearlyEntitlementService.updateAnnualLeaveBalanceAfterApproval(leaveTransaction.getEmployee().getId(), leaveTransaction.getLeaveType().getId(), leaveTransaction.getNumberOfDays());
			SendMonthlyLeaveReportService annualLeaveService = (SendMonthlyLeaveReportService) applicationContext.getBean("sendLeaveReportService");
			annualLeaveService.updateEmployeeAnnualLeavesAfterLeaveApproval(leaveTransaction, leaveTransaction.getApplicationDate());
			
		}
	}
	
	// This is used by our own process engine dealing with DB
		public static void updateToAnnualLeaveBalanceAfterApprovalTimeInLieuLeave(LeaveTransaction leaveTransaction){
			ApplLogger.getLogger().info(String.format("Finally Leave is approved and updating to Annual/Monthly Leave report Balance for Monthly Email, leaveTransaction object is : %s",leaveTransaction));
			if(leaveTransaction!=null)
			{
				ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
				YearlyEntitlementService yearlyEntitlementService = (YearlyEntitlementService) applicationContext.getBean("yearlyEntitlementService");
				yearlyEntitlementService.updateAnnualLeaveBalanceAfterApproval(leaveTransaction.getEmployee().getId(), leaveTransaction.getLeaveType().getId(), leaveTransaction.getNumberOfDays());
				SendMonthlyLeaveReportService annualLeaveService = (SendMonthlyLeaveReportService) applicationContext.getBean("sendLeaveReportService");
				annualLeaveService.updateEmployeeAnnualLeavesAfterLeaveApproval(leaveTransaction, leaveTransaction.getApplicationDate());
				
			}
		}
	
	// This method used for 2 level approval and updating the annual leave
	public static void updateLeaveBalanceAfterApproval(LeaveTransaction leaveTransaction,TimeInLieuBean timeInLieuBean){
		System.out.println("Updating Yearly entitlement and Annual Leave report : "+leaveTransaction.getYearlyLeaveBalance());
	}
	// This method is used by JBPM
	public static void updateLeaveApplicationStatusForLeaves(LeaveTransaction leaveTransaction,TimeInLieuBean timeInLieuBean){
		
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		LeaveTransactionService leaveTransactionService = (LeaveTransactionService) applicationContext.getBean("leaveTransactionService");
		if(timeInLieuBean!=null && timeInLieuBean.getIsFirstApproverApproved())
			leaveTransaction.setStatus("Approved");
		else
			leaveTransaction.setStatus("Rejected");
		if(timeInLieuBean!=null && timeInLieuBean.getIsFirstApproverApproved())
			leaveTransaction.setYearlyLeaveBalance(leaveTransaction.getYearlyLeaveBalance()-leaveTransaction.getNumberOfDays());
		
			leaveTransaction.setLastModifiedBy(timeInLieuBean!=null?timeInLieuBean.getFirstApprover():"Admin");
			leaveTransaction.setLastModifiedTime(new Date());
		leaveTransactionService.updateLeaveApplicationStatus(leaveTransaction);
	}
	
	// This is used by our own process engine dealing with DB
	public static void updateLeaveApplicationStatusForLeavesAfterFinalApproval(LeaveTransaction leaveTransaction,Boolean isApproved){
		
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		LeaveTransactionService leaveTransactionService = (LeaveTransactionService) applicationContext.getBean("leaveTransactionService");
		if(isApproved!=null && isApproved){
			leaveTransaction.setStatus("Approved");
			if(Leave.TIMEINLIEU.equalsName(leaveTransaction.getLeaveType().getName()))
				leaveTransaction.setYearlyLeaveBalance(leaveTransaction.getYearlyLeaveBalance()+leaveTransaction.getNumberOfDays());
			else
				leaveTransaction.setYearlyLeaveBalance(leaveTransaction.getYearlyLeaveBalance()-leaveTransaction.getNumberOfDays());
		}
		else
			leaveTransaction.setStatus("Rejected");
		
			leaveTransaction.setLastModifiedBy(leaveTransaction.getLastModifiedBy());
			leaveTransaction.setLastModifiedTime(new Date());
		leaveTransactionService.updateLeaveApplicationStatus(leaveTransaction);
	}

	// This method used for 2 level approval and updating the leave Transaction table by JPBM
	public static void updateLeaveApplicationStatusForFinalApproval(LeaveTransaction leaveTransaction,TimeInLieuBean timeInLieuBean){
		System.out.println("Updated Leave Transaction : "+timeInLieuBean.getIsSecondApproverApproved());
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		LeaveTransactionService leaveTransactionService = (LeaveTransactionService) applicationContext.getBean("leaveTransactionService");
		if(timeInLieuBean!=null && timeInLieuBean.getIsSecondApproverApproved()!=null && timeInLieuBean.getIsSecondApproverApproved() ||(timeInLieuBean!=null && timeInLieuBean.getIsTwoLeveApproval()==null))
			leaveTransaction.setStatus("Approved");
		else
			leaveTransaction.setStatus("Rejected");
		if(timeInLieuBean!=null && timeInLieuBean.getIsSecondApproverApproved()!=null && timeInLieuBean.getIsSecondApproverApproved() || (timeInLieuBean!=null && timeInLieuBean.getIsTwoLeveApproval()==null))
			leaveTransaction.setYearlyLeaveBalance(leaveTransaction.getYearlyLeaveBalance()+leaveTransaction.getNumberOfDays());
		
			leaveTransaction.setLastModifiedBy(timeInLieuBean!=null?timeInLieuBean.getSecondApprover():"Admin");
			leaveTransaction.setLastModifiedTime(new Date());
		leaveTransactionService.updateLeaveApplicationStatus(leaveTransaction);
	}
	
}
