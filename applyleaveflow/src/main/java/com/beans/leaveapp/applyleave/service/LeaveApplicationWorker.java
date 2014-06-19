package com.beans.leaveapp.applyleave.service;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.beans.exceptions.BSLException;
import com.beans.leaveapp.jbpm6.util.ApplicationContextProvider;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetransaction.service.LeaveTransactionService;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementService;

public class LeaveApplicationWorker {
	private static Logger log = Logger.getLogger(LeaveApplicationWorker.class);
	
	public static void sendingIntimationEmail(LeaveTransaction leaveTransaction,String role,Boolean isTeamLeadApproved,Boolean isOperDirApproved,String teamLeadName,String oprDirName,int status)
	{   
		try{
		LeaveApplicationSendingMailServiceImpl sendMailService = new LeaveApplicationSendingMailServiceImpl();
		log.info("Data coming from process is assignedRoleInLeaveFlow :"+role);
		if(status==1){
			log.info("Hurray... Employee is going to take leave.. Send mail to Team Lead..");
			sendMailService.sendEmailNotificationToLeaveApplicant(leaveTransaction, role, isTeamLeadApproved, isOperDirApproved,teamLeadName,oprDirName, status);
			sendMailService.sendEmailNotificationToLeaveApprover(leaveTransaction, role, isTeamLeadApproved, isOperDirApproved,teamLeadName,oprDirName, status);
		}
		else if(status==2){
			log.info("Hurray... Employee leave is approved by Team Lead..Notify Operational Director for approval");
			sendMailService.sendEmailNotificationToLeaveApprover(leaveTransaction, role, isTeamLeadApproved, isOperDirApproved,teamLeadName,oprDirName, status);
		}
		else if(status==3){
			log.info("Hurray... Leave is rejected by Team Lead.. Notify HR and Leave Requester");
			sendMailService.sendEmailNotificationToHR(leaveTransaction, role, isTeamLeadApproved, isOperDirApproved,teamLeadName,oprDirName, status);
			sendMailService.sendEmailNotificationToLeaveApplicant(leaveTransaction, role, isTeamLeadApproved, isOperDirApproved,teamLeadName,oprDirName, status);
		}
		else{
			log.info("Hurray... Leave is approved/rejected by Operational Director.. Notify HR and Leave Requester");
			sendMailService.sendEmailNotificationToHR(leaveTransaction, role, isTeamLeadApproved, isOperDirApproved,teamLeadName,oprDirName, status);
			sendMailService.sendEmailNotificationToLeaveApplicant(leaveTransaction, role, isTeamLeadApproved, isOperDirApproved,teamLeadName,oprDirName, status);
		}
		}catch(BSLException e)
		{
			log.error("Error in LeaveApplicationWorker sending mail :",  e);
			e.printStackTrace();
			throw new BSLException(e.getMessage());
		}
	}
	
	public static void updateLeaveBalanceAfterApproval(LeaveTransaction leaveTransaction,Boolean isOperDirApproved){
		log.info("Data coming from process is  leaveTransaction : "+leaveTransaction+" and isDirApproved :"+isOperDirApproved);
		if(leaveTransaction!=null && isOperDirApproved==true)
		{
			ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
			YearlyEntitlementService yearlyEntitlementService = (YearlyEntitlementService) applicationContext.getBean("yearlyEntitlementService");
			yearlyEntitlementService.updateLeaveBalanceAfterApproval(leaveTransaction.getEmployee().getId(), leaveTransaction.getLeaveType().getId(), leaveTransaction.getNumberOfDays());
		}
	}
	
	public static void updateLeaveApplicationStatus(LeaveTransaction leaveTransaction,Integer leaveTransactionId, Boolean isTeamLeadApproved, Boolean isOperDirApproved, int status){
		
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		LeaveTransactionService leaveTransactionService = (LeaveTransactionService) applicationContext.getBean("leaveTransactionService");
		if((!isTeamLeadApproved && status==3) || !isOperDirApproved)
			leaveTransaction.setStatus("R");
		else
			leaveTransaction.setStatus("A");
		leaveTransactionService.updateLeaveApplicationStatus(leaveTransaction,leaveTransactionId);
	}
	

}
