package com.beans.leaveapp.applyleave.bean;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.beans.common.audit.service.AuditTrail;
import com.beans.common.audit.service.SystemAuditTrailActivity;
import com.beans.common.audit.service.SystemAuditTrailLevel;
import com.beans.common.leave.rules.model.LeaveFlowDecisionsTaken;
import com.beans.common.leave.rules.model.LeaveRuleBean;
import com.beans.common.security.role.model.Role;
import com.beans.common.security.users.model.Users;
import com.beans.exceptions.BSLException;
import com.beans.leaveapp.applyleave.model.LeaveApprovalDataModel;
import com.beans.leaveapp.applyleave.service.LeaveApplicationService;
import com.beans.leaveapp.applyleave.service.LeaveApplicationWorker;
import com.beans.leaveapp.calendar.service.CalendarEventService;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetransaction.service.LeaveTransactionService;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.beans.leaveapp.leavetype.service.LeaveTypeService;
import com.beans.leaveapp.monthlyreport.service.SendMonthlyLeaveReportService;
import com.beans.leaveapp.web.bean.BaseMgmtBean;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementNotFound;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementService;
import com.beans.leaveflow.dbservice.LeaveApplicationFlowService;
import com.beans.leaveflow.service.email.LeaveApplicationEmailNotificationService;
import com.beans.util.enums.Leave;
import com.beans.util.log.ApplLogger;

public class LeaveApprovalMgmtBean extends BaseMgmtBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	List<LeaveTransaction> leaveRequestList;
	List<LeaveTransaction> pendingLeaveRequestList;
	private boolean insertDeleted = false;
	private Users actorUsers;
	private AuditTrail auditTrail;
	private LeaveApplicationService leaveApplicationService;
	private LeaveTypeService leaveTypeService;
	private LeaveTransactionService leaveTransactionService;
	private LeaveApprovalDataModel LeaveApprovalDataModel;
	private LeaveTransaction selectedLeaveRequest;
	private YearlyEntitlementService yearlyEntitlementService;
	private Double currentLeaveBalance;
	private String param;
	private SendMonthlyLeaveReportService monthlyLeaveReportService;
	private StreamedContent sickLeaveAttachment;
	private LeaveApplicationFlowService leaveFlowService;
	
	public LeaveApprovalDataModel getLeaveApprovalDataModel() {
		FacesContext f = FacesContext.getCurrentInstance();
		Map<String, String> parameterMap = (Map<String, String>) f.getExternalContext().getRequestParameterMap();
		if(LeaveApprovalDataModel == null || insertDeleted == true) {
			LeaveApprovalDataModel = new LeaveApprovalDataModel(getLeaveRequestApprovalList());
		}
		param = parameterMap.get("id");		
		if(param!=null){
			  pendingLeaveRequestList = getLeaveRequestApprovalList();
			for (LeaveTransaction leaveTransaction : pendingLeaveRequestList) {
				if(leaveTransaction.getId()==Integer.parseInt(param)){
					selectedLeaveRequest = leaveTransaction;
				}
			}
		}		
		
		return LeaveApprovalDataModel;
	}
	
	public List<LeaveTransaction> getLeaveRequestList() {
		return leaveRequestList;
	}

	public void setLeaveRequestList(List<LeaveTransaction> leaveRequestList) {
		this.leaveRequestList = leaveRequestList;
	}

	public LeaveApprovalDataModel getLeaveApprovalDataModelFutureLeaves() {
		return new LeaveApprovalDataModel(getLeaveRequestFutureLeaveList());
		
	}

	public LeaveApprovalDataModel getLeaveApprovalDataModelApprovedLeaves() {
		if(LeaveApprovalDataModel == null || insertDeleted == true) {
			LeaveApprovalDataModel = new LeaveApprovalDataModel(getLeaveTransactionService().getAllApprovedLeavesAppliedByEmployee());
		}
		return LeaveApprovalDataModel;
		
	}
	
	
	public LeaveApprovalDataModel getLeaveApprovalDataModelAllLeaves() {
		if(LeaveApprovalDataModel == null || insertDeleted == true) {
			LeaveApprovalDataModel = new LeaveApprovalDataModel(getLeaveTransactionService().getAllLeavesAppliedByEmployee(actorUsers.getId()));
		}
		return LeaveApprovalDataModel;
	}	
	
	public LeaveApprovalDataModel getLeaveApprovalDataModelPendingLeaves() {
		if(LeaveApprovalDataModel == null || insertDeleted == true) {
			LeaveApprovalDataModel = new LeaveApprovalDataModel(getLeaveTransactionService().getAllLeavesAppliedByEmployee());
		}
		return LeaveApprovalDataModel;
	}	


	public List<LeaveTransaction> getPendingLeaveRequestList() {
		return pendingLeaveRequestList;
	}

	public void setPendingLeaveRequestList(
			List<LeaveTransaction> pendingLeaveRequestList) {
		this.pendingLeaveRequestList = pendingLeaveRequestList;
	}
	
	
	public void setLeaveApprovalDataModel(LeaveApprovalDataModel leaveApprovalDataModel) {
		LeaveApprovalDataModel = leaveApprovalDataModel;
	}

	public LeaveApplicationService getLeaveApplicationService() {
		return leaveApplicationService;
	}
	public void setLeaveApplicationService(
			LeaveApplicationService leaveApplicationService) {
		this.leaveApplicationService = leaveApplicationService;
	}
	public boolean isInsertDeleted() {
		return insertDeleted;
	}
	public void setInsertDeleted(boolean insertDeleted) {
		this.insertDeleted = insertDeleted;
	}
	public Users getActorUsers() {
		return actorUsers;
	}
	public void setActorUsers(Users actorUsers) {
		this.actorUsers = actorUsers;
	}
	public AuditTrail getAuditTrail() {
		return auditTrail;
	}
	public void setAuditTrail(AuditTrail auditTrail) {
		this.auditTrail = auditTrail;
	} 	

	public LeaveTransaction getSelectedLeaveRequest() {
		return selectedLeaveRequest;
	}

	public void setSelectedLeaveRequest(LeaveTransaction selectedLeaveRequest) {
		this.selectedLeaveRequest = selectedLeaveRequest;
	}
	
	public YearlyEntitlementService getYearlyEntitlementService() {
		return yearlyEntitlementService;
	}

	public void setYearlyEntitlementService(
			YearlyEntitlementService yearlyEntitlementService) {
		this.yearlyEntitlementService = yearlyEntitlementService;
	}

	
	
	public SendMonthlyLeaveReportService getMonthlyLeaveReportService() {
		return monthlyLeaveReportService;
	}

	public void setMonthlyLeaveReportService(
			SendMonthlyLeaveReportService monthlyLeaveReportService) {
		this.monthlyLeaveReportService = monthlyLeaveReportService;
	}

	public Double getCurrentLeaveBalance() {
		return currentLeaveBalance;
	}

	public void setCurrentLeaveBalance(Double currentLeaveBalance) {
		this.currentLeaveBalance = currentLeaveBalance;
	}

	public LeaveApplicationFlowService getLeaveFlowService() {
		return leaveFlowService;
	}

	public void setLeaveFlowService(LeaveApplicationFlowService leaveFlowService) {
		this.leaveFlowService = leaveFlowService;
	}
	
	public LeaveTypeService getLeaveTypeService() {
		return leaveTypeService;
	}

	public void setLeaveTypeService(LeaveTypeService leaveTypeService) {
		this.leaveTypeService = leaveTypeService;
	}

	public List<LeaveTransaction> getLeaveRequestApprovalList() {
		if(pendingLeaveRequestList==null || insertDeleted==true){
			// return getLeaveApplicationService().getPendingLeaveRequestsList(actorUsers.getUsername()); Enable it for jbpm
			pendingLeaveRequestList = getLeaveFlowService().getPendingLeaveRequestsByRoleOfUser(actorUsers.getUsername());  // This is for own process engine
		}
			return  pendingLeaveRequestList;
			
	}
	
	public List<LeaveTransaction> getLeaveRequestFutureLeaveList() {
		if(leaveRequestList == null || insertDeleted == true) {
			 try {
				 leaveRequestList =   getLeaveTransactionService().getAllFutureLeavesAppliedByEmployee(actorUsers.getId(), new java.sql.Date(new Date().getTime()));
			} catch (Throwable e) {
				e.printStackTrace();
			}
			
		}
		
		return leaveRequestList;
	}
	

	public void doApproveLeaveRequest() {
		try {
			
	/*		
			Map<String,Object>  processInstanceData =	leaveApplicationService.getContentMapDataByTaskId(selectedLeaveRequest.getTaskId());
			if(processInstanceData!=null){
				TimeInLieuBean processDecisionData =   (TimeInLieuBean) processInstanceData.get("timeInLieuBean");
				ApprovalLevelModel approverBean =  (ApprovalLevelModel) processInstanceData.get("approvalLevelModel");
					if(processDecisionData!=null && processDecisionData.getIsTwoLeveApproval()){
						if(selectedLeaveRequest.getLeaveType()!=null && Leave.TIMEINLIEU.equalsName(selectedLeaveRequest.getLeaveType().getName()) ){
							leaveApplicationService.approveLeaveOfEmployee(selectedLeaveRequest, getActorUsers().getUsername(),"SECOND");
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info : "+getExcptnMesProperty("info.leave.approve.dir"),"Leave Approved"));
							CalendarEventService.createEventForApprovedLeave(selectedLeaveRequest);
						}
					}
					else{
						leaveApplicationService.approveLeaveOfEmployee(selectedLeaveRequest, getActorUsers().getUsername(),"FIRST");
						// Creating calendar event for approved leave slot
						if(selectedLeaveRequest.getLeaveType()!=null && !Leave.TIMEINLIEU.equalsName(selectedLeaveRequest.getLeaveType().getName()) || approverBean!=null && !"ROLE_EMPLOYEE".equalsIgnoreCase(approverBean.getRole()) ){
							CalendarEventService.createEventForApprovedLeave(selectedLeaveRequest);
							
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info : "+getExcptnMesProperty("info.leave.approve.dir"),"Leave Approved"));
						}else{
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info : "+getExcptnMesProperty("info.leave.approve.operdir"),"Leave Approved"));
						}
					}
			}
			
	*/		
			// This is for approving leave by Our own process engine
			
			LeaveRuleBean leaveRule = selectedLeaveRequest.getLeaveRuleBean();
			LeaveTransaction leaveTransactionPersist=null;
			
			Map<String,Integer> rulesMap = getMapByLeaveRules(leaveRule);
			int totalLevelsRequired = rulesMap.size();
			
			Integer currentLevel =	rulesMap.get(selectedLeaveRequest.getDecisionToBeTaken());
			if(currentLevel==totalLevelsRequired){
				// write the code for leave process completed, do the operations like db updates, calendar events and other stuff
				if(totalLevelsRequired==1){
					selectedLeaveRequest.getDecisionsBean().setDecisionLevel1("YES");
					selectedLeaveRequest.getDecisionsBean().setDecisionUserLevel1(getActorUsers().getUsername());
				}else if(totalLevelsRequired==2){
					selectedLeaveRequest.getDecisionsBean().setDecisionLevel2("YES");
					selectedLeaveRequest.getDecisionsBean().setDecisionUserLevel2(getActorUsers().getUsername());
				}else if(totalLevelsRequired==3){
					selectedLeaveRequest.getDecisionsBean().setDecisionLevel3("YES");
					selectedLeaveRequest.getDecisionsBean().setDecisionUserLevel3(getActorUsers().getUsername());
				}else if(totalLevelsRequired==4){
					selectedLeaveRequest.getDecisionsBean().setDecisionLevel4("YES");
					selectedLeaveRequest.getDecisionsBean().setDecisionUserLevel4(getActorUsers().getUsername());
				}else{
					selectedLeaveRequest.getDecisionsBean().setDecisionLevel5("YES");
					selectedLeaveRequest.getDecisionsBean().setDecisionUserLevel5(getActorUsers().getUsername());
				}
				selectedLeaveRequest.setDecisionToBeTaken("NONE");
				// saving the decisions taken by the approvers on a list
				LeaveFlowDecisionsTaken leaveFlowDecisions = leaveTransactionService.saveLeaveApprovalDecisions(selectedLeaveRequest.getDecisionsBean());
				selectedLeaveRequest.setDecisionsBean(leaveFlowDecisions);
				selectedLeaveRequest.setLastModifiedBy(getActorUsers().getUsername());
				// Getting the latest yearly balance and do the operations on it.
				YearlyEntitlement entitlementBean = null;
				try {
					if(Leave.TIMEINLIEU.equalsName(selectedLeaveRequest.getLeaveType().getName())){
						LeaveType leaveType = getLeaveTypeService().findByEmployeeNameAndTypeId(Leave.ANNUAL.toString(), selectedLeaveRequest.getEmployee().getEmployeeType().getId());
						entitlementBean =	yearlyEntitlementService.findByEmployeeAndLeaveType(selectedLeaveRequest.getEmployee().getId(), leaveType.getId());
					}else
						entitlementBean =	yearlyEntitlementService.findByEmployeeAndLeaveType(selectedLeaveRequest.getEmployee().getId(), selectedLeaveRequest.getLeaveType().getId());
					
					if(entitlementBean!=null)
						selectedLeaveRequest.setYearlyLeaveBalance(entitlementBean.getYearlyLeaveBalance());
				} catch (YearlyEntitlementNotFound e) {
					e.printStackTrace();
				}
				// Saving the current state of the Leave Transaction 
				leaveTransactionPersist =	leaveTransactionService.processAppliedLeaveOfEmployee(selectedLeaveRequest);
				// Updating the leave balances once approved at final stage
				getLeaveFlowService().updateLeaveBalancesOnceApproved(leaveTransactionPersist, Boolean.TRUE);
				
				CalendarEventService.createEventForApprovedLeave(selectedLeaveRequest);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info : "+getExcptnMesProperty("info.leave.approve.dir"),"Leave Approved"));
				ApplLogger.getLogger().info(String.format("Final Level is approved by : %s and current level is : %s", getActorUsers().getUsername(),selectedLeaveRequest.getDecisionToBeTaken()));
				
			}else{
				if(rulesMap.get(selectedLeaveRequest.getDecisionToBeTaken())==1){
					selectedLeaveRequest.getDecisionsBean().setDecisionLevel1("YES");
					selectedLeaveRequest.getDecisionsBean().setDecisionUserLevel1(getActorUsers().getUsername());
					selectedLeaveRequest.setDecisionToBeTaken(leaveRule.getApproverNameLevel2());
					ApplLogger.getLogger().info(String.format("Current Level is approved by : %s and case moved to next Level : %s", getActorUsers().getUsername(),leaveRule.getApproverNameLevel2()));
				}else if(rulesMap.get(selectedLeaveRequest.getDecisionToBeTaken())==2){
					selectedLeaveRequest.getDecisionsBean().setDecisionLevel2("YES");
					selectedLeaveRequest.getDecisionsBean().setDecisionUserLevel2(getActorUsers().getUsername());
					selectedLeaveRequest.setDecisionToBeTaken(leaveRule.getApproverNameLevel3());
					ApplLogger.getLogger().info(String.format("Current Level is approved by : %s and case moved to next Level : %s", getActorUsers().getUsername(),leaveRule.getApproverNameLevel3()));
				}else if(rulesMap.get(selectedLeaveRequest.getDecisionToBeTaken())==3){
					selectedLeaveRequest.getDecisionsBean().setDecisionLevel3("YES");
					selectedLeaveRequest.getDecisionsBean().setDecisionUserLevel3(getActorUsers().getUsername());
					selectedLeaveRequest.setDecisionToBeTaken(leaveRule.getApproverNameLevel3());
					ApplLogger.getLogger().info(String.format("Current Level is approved by : %s and case moved to next Level : %s", getActorUsers().getUsername(),leaveRule.getApproverNameLevel4()));
				}else if(rulesMap.get(selectedLeaveRequest.getDecisionToBeTaken())==4){
					selectedLeaveRequest.getDecisionsBean().setDecisionLevel4("YES");
					selectedLeaveRequest.getDecisionsBean().setDecisionUserLevel4(getActorUsers().getUsername());
					selectedLeaveRequest.setDecisionToBeTaken(leaveRule.getApproverNameLevel4());
					ApplLogger.getLogger().info(String.format("Current Level is approved by : %s and case moved to next Level : %s", getActorUsers().getUsername(),leaveRule.getApproverNameLevel5()));
				}
				// saving the decisions taken by the approvers on a list
				LeaveFlowDecisionsTaken leaveFlowDecisions = leaveTransactionService.saveLeaveApprovalDecisions(selectedLeaveRequest.getDecisionsBean());
				selectedLeaveRequest.setDecisionsBean(leaveFlowDecisions);
				selectedLeaveRequest.setLastModifiedBy(getActorUsers().getUsername());
				// Saving the current state of the Leave Transaction 
				leaveTransactionPersist =	leaveTransactionService.processAppliedLeaveOfEmployee(selectedLeaveRequest);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info : Leave is approved and case is moved to next level approval","Leave Approved"));
			}
			// sending email to notify
			LeaveApplicationEmailNotificationService.sendingIntimationEmail(leaveTransactionPersist);
			
			Set<String> roleSet = new HashSet<String>();
			for (Role role : getActorUsers().getUserRoles()) {
				roleSet.add(role.getRole());
			}
			setInsertDeleted(true);
		}catch(BSLException e){
			FacesMessage msg = new FacesMessage("Error : "+getExcptnMesProperty(e.getMessage()),"Leave approve error");  
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
		}catch(Exception e) {
			log.error("Error while approving leave by "+getActorUsers().getUsername(), e);
			FacesMessage msg = new FacesMessage("Error : "+getExcptnMesProperty(e.getMessage()),"Leave approve error");  
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	
	private Map<String,Integer> getMapByLeaveRules(LeaveRuleBean leaveRule){
		Map<String,Integer> rulesMap = new HashMap<String,Integer>();
		if(StringUtils.trimToNull(leaveRule.getApproverNameLevel1())!=null)
			rulesMap.put(leaveRule.getApproverNameLevel1(), 1);
		if(StringUtils.trimToNull(leaveRule.getApproverNameLevel2())!=null)
			rulesMap.put(leaveRule.getApproverNameLevel2(), 2);
		if(StringUtils.trimToNull(leaveRule.getApproverNameLevel3())!=null)
			rulesMap.put(leaveRule.getApproverNameLevel3(), 3);
		if(StringUtils.trimToNull(leaveRule.getApproverNameLevel4())!=null)
			rulesMap.put(leaveRule.getApproverNameLevel4(), 4);
		if(StringUtils.trimToNull(leaveRule.getApproverNameLevel5())!=null)
			rulesMap.put(leaveRule.getApproverNameLevel5(), 5);
		return rulesMap;
	}
	
	public void doRejectLeaveRequest() {
		try {
			/*
			 * This should be used by JBPM only
			 * 
			 * if(selectedLeaveRequest.getReason() == null || selectedLeaveRequest.getReason().trim().equals("")) {
				FacesMessage msg = new FacesMessage("Please enter Reason for rejection");  
		        
		        FacesContext.getCurrentInstance().addMessage(null, msg);  
			} else {
				
				log.info("Leave Request Rejected...");
				leaveApplicationService.rejectLeaveOfEmployee(selectedLeaveRequest, getActorUsers().getUsername());
				
				auditTrail.log(SystemAuditTrailActivity.REJECTED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has rejected a leave request of " + selectedLeaveRequest.getEmployee().getName() + " due to " + selectedLeaveRequest.getReason());
				
			}*/
			
			LeaveRuleBean leaveRule = selectedLeaveRequest.getLeaveRuleBean();
			LeaveTransaction leaveTransactionPersist=null;
			ApplLogger.getLogger().info(String.format("Leave Application is rejected by user %s with role : %s", getActorUsers().getUsername(),selectedLeaveRequest.getDecisionToBeTaken()));
			Map<String,Integer> rulesMap = getMapByLeaveRules(leaveRule);
			if(rulesMap.get(selectedLeaveRequest.getDecisionToBeTaken())==1){
				selectedLeaveRequest.getDecisionsBean().setDecisionLevel1("NO");
				selectedLeaveRequest.getDecisionsBean().setDecisionUserLevel1(getActorUsers().getUsername());
				ApplLogger.getLogger().info(String.format("Current Level is rejected by : %s ", getActorUsers().getUsername()));
			}else if(rulesMap.get(selectedLeaveRequest.getDecisionToBeTaken())==2){
				selectedLeaveRequest.getDecisionsBean().setDecisionLevel2("NO");
				selectedLeaveRequest.getDecisionsBean().setDecisionUserLevel2(getActorUsers().getUsername());
				ApplLogger.getLogger().info(String.format("Current Level is rejected by : %s", getActorUsers().getUsername()));
			}else if(rulesMap.get(selectedLeaveRequest.getDecisionToBeTaken())==3){
				selectedLeaveRequest.getDecisionsBean().setDecisionLevel3("NO");
				selectedLeaveRequest.getDecisionsBean().setDecisionUserLevel3(getActorUsers().getUsername());
				ApplLogger.getLogger().info(String.format("Current Level is rejected by : %s", getActorUsers().getUsername()));
			}else if(rulesMap.get(selectedLeaveRequest.getDecisionToBeTaken())==4){
				selectedLeaveRequest.getDecisionsBean().setDecisionLevel4("NO");
				selectedLeaveRequest.getDecisionsBean().setDecisionUserLevel4(getActorUsers().getUsername());
				ApplLogger.getLogger().info(String.format("Current Level is rejected by : %s", getActorUsers().getUsername()));
			}else{
				selectedLeaveRequest.getDecisionsBean().setDecisionLevel5("NO");
				selectedLeaveRequest.getDecisionsBean().setDecisionUserLevel5(getActorUsers().getUsername());
				ApplLogger.getLogger().info(String.format("Current Level is rejected by : %s", getActorUsers().getUsername()));
			}
			
			// saving the decisions taken by the approvers on a list
			LeaveFlowDecisionsTaken leaveFlowDecisions = leaveTransactionService.saveLeaveApprovalDecisions(selectedLeaveRequest.getDecisionsBean());
			selectedLeaveRequest.setDecisionsBean(leaveFlowDecisions);
			selectedLeaveRequest.setLastModifiedBy(getActorUsers().getUsername());
			// Saving the current state of the Leave Transaction 
			leaveTransactionPersist =	leaveTransactionService.processAppliedLeaveOfEmployee(selectedLeaveRequest);
			// Updating the Status in the Transaction table
			getLeaveFlowService().updateLeaveBalancesOnceApproved(leaveTransactionPersist, Boolean.FALSE);
			// sending email to notify
			LeaveApplicationEmailNotificationService.sendingIntimationEmail(leaveTransactionPersist);
						
			setInsertDeleted(true);
		    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info : "+getExcptnMesProperty("info.leave.reject"),"Leave Rejected"));
			}catch(BSLException e){
				FacesMessage msg = new FacesMessage("Error : "+getExcptnMesProperty(e.getMessage()),"Leave Reject Error");  
				msg.setSeverity(FacesMessage.SEVERITY_INFO);
		        FacesContext.getCurrentInstance().addMessage(null, msg); 
			}catch(Exception e) {
				log.error("Error while approving leave by "+getActorUsers().getUsername(), e);
				FacesMessage msg = new FacesMessage("Error : "+getExcptnMesProperty(e.getMessage()),"Leave approve error");  
				msg.setSeverity(FacesMessage.SEVERITY_INFO);
		        FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}

	public void doCancelLeaveTransaction(){
		// Write code to update yearly balance,current balance and status and send mail to HR and Leave Applicant.
		try{
		
		YearlyEntitlement entitlementBean =	yearlyEntitlementService.findByEmployeeAndLeaveType(selectedLeaveRequest.getEmployee().getId(), selectedLeaveRequest.getLeaveType().getId());
		if(entitlementBean!=null){
			
		selectedLeaveRequest.setStatus("Cancelled");
		selectedLeaveRequest.setLastModifiedBy(actorUsers.getUsername());
		selectedLeaveRequest.setLastModifiedTime(new Date());
		selectedLeaveRequest.setYearlyLeaveBalance(entitlementBean.getYearlyLeaveBalance()+selectedLeaveRequest.getNumberOfDays());
		LeaveTransaction leaveTransactionPersist = leaveTransactionService.updateLeaveApplicationStatus(selectedLeaveRequest);
		
		// write code for adding this cancelled leave number of days to yearly ettilement table as well annual leave report table
		
		yearlyEntitlementService.updateLeaveBalanceAfterCancelled(selectedLeaveRequest.getEmployee().getId(),selectedLeaveRequest.getLeaveType().getId(),selectedLeaveRequest.getNumberOfDays());
		
		monthlyLeaveReportService.updateLeaveBalanceAfterCancelled(selectedLeaveRequest);
		
		LeaveApplicationWorker.sendCancelLeaveMail(leaveTransactionPersist, actorUsers.getUsername());

		auditTrail.log(SystemAuditTrailActivity.REJECTED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has cancelled a leave request of " + selectedLeaveRequest.getEmployee().getName() + " due to " + selectedLeaveRequest.getReason());
		setInsertDeleted(true);
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info : "+getExcptnMesProperty("info.leave.cancel"),"Leave Cancelled"));
		}
		}catch(BSLException e){
			FacesMessage msg = new FacesMessage("Error : "+getExcptnMesProperty(e.getMessage()),"Leave Cancel Error");  
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
		}catch(Exception e) {
			log.error("Error while cancelling leave by "+getActorUsers().getUsername(), e);
		} catch (YearlyEntitlementNotFound e) {
			e.printStackTrace();
		}
	}
	
	
	public void onRowSelect(SelectEvent event) {  
		LeaveTransaction leaveTransaction = (LeaveTransaction) event.getObject();
		setSelectedLeaveRequest(leaveTransaction); 
		YearlyEntitlement entitlement = yearlyEntitlementService.findYearlyEntitlementById(leaveTransaction.getEmployee().getId(), leaveTransaction.getLeaveType().getId());
		currentLeaveBalance = entitlement.getCurrentLeaveBalance();
		RequestContext.getCurrentInstance().addCallbackParam("leaveType", leaveTransaction.getLeaveType().getName());
    }
	
	public void showDialogBox(){
		if(selectedLeaveRequest != null){
		YearlyEntitlement entitlement = yearlyEntitlementService.findYearlyEntitlementById(selectedLeaveRequest.getEmployee().getId(), selectedLeaveRequest.getLeaveType().getId());
		currentLeaveBalance = entitlement.getCurrentLeaveBalance();
		RequestContext.getCurrentInstance().addCallbackParam("leaveType", selectedLeaveRequest.getLeaveType().getName());
		RequestContext.getCurrentInstance().execute("leaveRequestDialogVar.show();");
		}	
		}		
	

	public StreamedContent getSickLeaveAttachment() {
		int id;
		id = selectedLeaveRequest.getId();
		LeaveTransaction sickLeaveTransaction = leaveTransactionService.findById(id);
		byte[] data = sickLeaveTransaction.getSickLeaveAttachment();
		String sickLeaveAttachmentName = sickLeaveTransaction.getSickLeaveAttachmentName();
		String name = sickLeaveAttachmentName.substring(sickLeaveAttachmentName.length()-3);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);		
		if(name.equals("pdf")){
			sickLeaveAttachment = new DefaultStreamedContent(byteArrayInputStream, "application/pdf",sickLeaveAttachmentName);
		}else{		
		sickLeaveAttachment = new DefaultStreamedContent(byteArrayInputStream, "image/jpeg",sickLeaveAttachmentName);
		}
		return sickLeaveAttachment;
	}

	public void setSickLeaveAttachment(StreamedContent sickLeaveAttachment) {
		this.sickLeaveAttachment = sickLeaveAttachment;
	}

	public LeaveTransactionService getLeaveTransactionService() {
		return leaveTransactionService;
	}

	public void setLeaveTransactionService(
			LeaveTransactionService leaveTransactionService) {
		this.leaveTransactionService = leaveTransactionService;
	}
	
}
