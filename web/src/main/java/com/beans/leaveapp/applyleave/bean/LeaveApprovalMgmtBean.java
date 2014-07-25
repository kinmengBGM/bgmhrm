package com.beans.leaveapp.applyleave.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import com.beans.common.audit.service.AuditTrail;
import com.beans.common.audit.service.SystemAuditTrailActivity;
import com.beans.common.audit.service.SystemAuditTrailLevel;
import com.beans.common.security.role.model.Role;
import com.beans.common.security.users.model.Users;
import com.beans.exceptions.BSLException;
import com.beans.leaveapp.applyleave.model.LeaveApprovalDataModel;
import com.beans.leaveapp.applyleave.service.LeaveApplicationService;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetransaction.service.LeaveTransactionService;
import com.beans.leaveapp.monthlyreport.service.SendMonthlyLeaveReportService;
import com.beans.leaveapp.web.bean.BaseMgmtBean;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementService;

public class LeaveApprovalMgmtBean extends BaseMgmtBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	List<LeaveTransaction>  leaveRequestList;
	private boolean insertDeleted = false;
	private Users actorUsers;
	private AuditTrail auditTrail;
	private LeaveApplicationService leaveApplicationService;
	private LeaveTransactionService leaveTransactionService;
	private LeaveApprovalDataModel LeaveApprovalDataModel;
	private LeaveTransaction selectedLeaveRequest;
	private YearlyEntitlementService yearlyEntitlementService;
	private Double currentLeaveBalance;
	private boolean dialogBoxRendered;
	private String param;
	private SendMonthlyLeaveReportService monthlyLeaveReportService;
	
	public LeaveApprovalDataModel getLeaveApprovalDataModel() {
		FacesContext f = FacesContext.getCurrentInstance();
		Map<String, String> parameterMap = (Map<String, String>) f.getExternalContext().getRequestParameterMap();
		param = parameterMap.get("id");		
		if(LeaveApprovalDataModel == null || insertDeleted == true) {
			LeaveApprovalDataModel = new LeaveApprovalDataModel(getLeaveRequestApprovalList());
		}
		return LeaveApprovalDataModel;
	}
	
	public LeaveApprovalDataModel getLeaveApprovalDataModelFutureLeaves() {
		return new LeaveApprovalDataModel(getLeaveRequestFutureLeaveList());
		
	}

	public LeaveApprovalDataModel getLeaveApprovalDataModelApprovedLeaves() {
		return new LeaveApprovalDataModel(getLeaveTransactionService().getAllApprovedLeavesAppliedByEmployee());
		
	}
	
	
	public LeaveApprovalDataModel getLeaveApprovalDataModelAllLeaves() {
		return new LeaveApprovalDataModel(getLeaveTransactionService().getAllLeavesAppliedByEmployee(actorUsers.getId()));
		
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
	public List<LeaveTransaction> getLeaveRequestList() {
		return leaveRequestList;
	}
	public void setLeaveRequestList(List<LeaveTransaction> leaveRequestList) {
		this.leaveRequestList = leaveRequestList;
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

	
	public Double getCurrentLeaveBalance() {
		return currentLeaveBalance;
	}

	public void setCurrentLeaveBalance(Double currentLeaveBalance) {
		this.currentLeaveBalance = currentLeaveBalance;
	}

	public List<LeaveTransaction> getLeaveRequestApprovalList() {
		if(leaveRequestList == null || insertDeleted == true) {
			 try {
				 leaveRequestList =   getLeaveApplicationService().getPendingLeaveRequestsList(actorUsers.getUsername());
			} catch (Throwable e) {
				e.printStackTrace();
			}
			
		}
		
		return leaveRequestList;
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
			
			log.info("Leave Request Approved...!!!");
			//auditTrail.log(SystemAuditTrailActivity.APPROVED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has approved a employee registration of " + selectedRegisteredEmployee.getFullname());
			leaveApplicationService.approveLeaveOfEmployee(selectedLeaveRequest, getActorUsers().getUsername());
		    setInsertDeleted(true);
		    
		    Set<String> roleSet = new HashSet<String>();
			for (Role role : getActorUsers().getUserRoles()) {
				roleSet.add(role.getRole());
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info : "+getExcptnMesProperty("info.leave.approve.dir"),"Leave Approved"));
		}catch(BSLException e){
			FacesMessage msg = new FacesMessage("Error : "+getExcptnMesProperty(e.getMessage()),"Leave approve error");  
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
		}catch(Exception e) {
			log.error("Error while approving leave by "+getActorUsers().getUsername(), e);
		}
	}
	
	public void doRejectLeaveRequest() {
		try {
			if(selectedLeaveRequest.getReason() == null || selectedLeaveRequest.getReason().trim().equals("")) {
				FacesMessage msg = new FacesMessage("Please enter Reason for rejection");  
		        
		        FacesContext.getCurrentInstance().addMessage(null, msg);  
			} else {
				
				log.info("Leave Request Rejected...");
				leaveApplicationService.rejectLeaveOfEmployee(selectedLeaveRequest, getActorUsers().getUsername());
				
				auditTrail.log(SystemAuditTrailActivity.REJECTED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has rejected a leave request of " + selectedLeaveRequest.getEmployee().getName() + " due to " + selectedLeaveRequest.getReason());
				
			}
			setInsertDeleted(true);
		    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info : "+getExcptnMesProperty("info.leave.reject"),"Leave Rejected"));
			}catch(BSLException e){
				FacesMessage msg = new FacesMessage("Error : "+getExcptnMesProperty(e.getMessage()),"Leave Reject Error");  
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		        FacesContext.getCurrentInstance().addMessage(null, msg); 
			}catch(Exception e) {
				log.error("Error while approving leave by "+getActorUsers().getUsername(), e);
			}
		}

	public void doCancelLeaveTransaction(){
		// Write code to update yearly balance,current balance and status and send mail to HR and Leave Applicant.
		selectedLeaveRequest.setStatus("Cancelled");
		selectedLeaveRequest.setLastModifiedBy(actorUsers.getUsername());
		selectedLeaveRequest.setLastModifiedTime(new Date());
		selectedLeaveRequest.setYearlyLeaveBalance(selectedLeaveRequest.getYearlyLeaveBalance()+selectedLeaveRequest.getNumberOfDays());
		leaveTransactionService.updateLeaveApplicationStatus(selectedLeaveRequest);
		
		// write code for adding this cancelled leave number of days to yearly ettilement table as well annual leave report table
		
		yearlyEntitlementService.updateLeaveBalanceAfterCancelled(selectedLeaveRequest.getEmployee().getId(),selectedLeaveRequest.getLeaveType().getId(),selectedLeaveRequest.getNumberOfDays());
		
		monthlyLeaveReportService.updateLeaveBalanceAfterCancelled(selectedLeaveRequest);
	}
	
	
	public void onRowSelect(SelectEvent event) {  
		LeaveTransaction leaveTransaction = (LeaveTransaction) event.getObject();
		setSelectedLeaveRequest(leaveTransaction); 
		YearlyEntitlement entitlement = yearlyEntitlementService.findYearlyEntitlementById(leaveTransaction.getEmployee().getId(), leaveTransaction.getLeaveType().getId());
		currentLeaveBalance = entitlement.getCurrentLeaveBalance();
		RequestContext.getCurrentInstance().addCallbackParam("leaveType", leaveTransaction.getLeaveType().getName());
    }
	
	public void showDialogBox(){
		if(param != null){
		int leaveTransactionId = Integer.parseInt(param);
	//	int leaveTransactionId = 1;
		LeaveTransaction leaveTransaction = leaveTransactionService.findById(leaveTransactionId);
		
		if(leaveTransaction != null && "Pending".equalsIgnoreCase(leaveTransaction.getStatus())){
		setSelectedLeaveRequest(leaveTransaction); 
		YearlyEntitlement entitlement = yearlyEntitlementService.findYearlyEntitlementById(leaveTransaction.getEmployee().getId(), leaveTransaction.getLeaveType().getId());
		currentLeaveBalance = entitlement.getCurrentLeaveBalance();
		RequestContext.getCurrentInstance().addCallbackParam("leaveType", leaveTransaction.getLeaveType().getName());
		RequestContext.getCurrentInstance().execute("leaveRequestDialogVar.show();");
		}
		}	
		}
	
	public boolean isDialogBoxRendered() {
		return dialogBoxRendered;
	}

	public void setDialogBoxRendered(boolean dialogBoxRendered) {
		this.dialogBoxRendered = dialogBoxRendered;
	}

	public LeaveTransactionService getLeaveTransactionService() {
		return leaveTransactionService;
	}

	public void setLeaveTransactionService(
			LeaveTransactionService leaveTransactionService) {
		this.leaveTransactionService = leaveTransactionService;
	}
	
}
