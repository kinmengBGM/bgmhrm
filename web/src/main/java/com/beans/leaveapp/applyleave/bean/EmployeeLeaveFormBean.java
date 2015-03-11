package com.beans.leaveapp.applyleave.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

import com.beans.common.audit.service.AuditTrail;
import com.beans.common.audit.service.SystemAuditTrailActivity;
import com.beans.common.audit.service.SystemAuditTrailLevel;
import com.beans.common.leave.rules.model.LeaveFlowDecisionsTaken;
import com.beans.common.leave.rules.model.LeaveRuleBean;
import com.beans.common.security.role.model.Role;
import com.beans.common.security.role.service.RoleNotFound;
import com.beans.common.security.users.model.Users;
import com.beans.exceptions.BSLException;
import com.beans.leaveapp.applyleave.service.LeaveApplicationException;
import com.beans.leaveapp.applyleave.service.LeaveApplicationService;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetransaction.service.LeaveTransactionService;
import com.beans.leaveapp.web.bean.BaseMgmtBean;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementNotFound;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementService;
import com.beans.leaveflow.service.email.LeaveApplicationEmailNotificationService;
import com.beans.util.enums.Leave;
import com.beans.util.enums.LeaveStatus;
import com.beans.util.log.ApplLogger;

public class EmployeeLeaveFormBean extends BaseMgmtBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private int selectedYearlyEntitlement = 0;
	private String leaveType;
	private Employee employee;
	private Users actorUsers;
	private YearlyEntitlement yearlyEntitlement = new YearlyEntitlement();
	private Date startDate;
	private Date endDate;
	private String reason;
	private Double numberOfDays;
	private Double yearlyBalance;
	private YearlyEntitlementService yearlyEntitlementService;
	private LeaveApplicationService leaveApplicationService;
	private LeaveTransactionService leaveTransactionService;
	private AuditTrail auditTrail;
	private Double allowedMaximumLeaves;
	private UploadedFile sickLeaveAttachment;
	private byte[] byteData = null;
    private String timings;
	public Double getAllowedMaximumLeaves() {
		return allowedMaximumLeaves;
	}
	public void setAllowedMaximumLeaves(Double allowedMaximumLeaves) {
		this.allowedMaximumLeaves = allowedMaximumLeaves;
	}
	public int getSelectedYearlyEntitlement() {
		return selectedYearlyEntitlement;
	}
	public void setSelectedYearlyEntitlement(int selectedYearlyEntitlement) {
		this.selectedYearlyEntitlement = selectedYearlyEntitlement;
	}
	
	public void yearlyEntitlementSelected(ValueChangeEvent e) {
		setSelectedYearlyEntitlement(Integer.parseInt(e.getNewValue().toString()));
		findYearlyEntitlement();
		
		if(getYearlyEntitlement() != null) {
			allowedMaximumLeaves=0.0;
			setLeaveType(getYearlyEntitlement().getLeaveType().getName());
			setYearlyBalance(getYearlyEntitlement().getYearlyLeaveBalance());
			if(isEmployeeFinishedOneYear()){
				if(Leave.UNPAID.equalsName(leaveType))
					allowedMaximumLeaves=30.0;
				else
					allowedMaximumLeaves=getYearlyEntitlement().getYearlyLeaveBalance();
			}
			else{
				if(Leave.ANNUAL.equalsName(leaveType))
					allowedMaximumLeaves = getYearlyEntitlement().getCurrentLeaveBalance()+3.0;
				else if(Leave.UNPAID.equalsName(leaveType))
					allowedMaximumLeaves=30.0;
				else
					allowedMaximumLeaves=getYearlyEntitlement().getYearlyLeaveBalance();
			}
		}
		RequestContext.getCurrentInstance().addCallbackParam("currentBalance", yearlyEntitlement.getCurrentLeaveBalance());
		RequestContext.getCurrentInstance().addCallbackParam("leaveType", leaveType);
		RequestContext.getCurrentInstance().addCallbackParam("isOneYearOver", isEmployeeFinishedOneYear());
	}
	
	
	
	
	public Double getYearlyBalance() {
		return yearlyBalance;
	}
	public void setYearlyBalance(Double yearlyBalance) {
		this.yearlyBalance = yearlyBalance;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public Users getActorUsers() {
		return actorUsers;
	}
	public void setActorUsers(Users actorUsers) {
		this.actorUsers = actorUsers;
	}
	
	public YearlyEntitlement getYearlyEntitlement() {
		return yearlyEntitlement;
	}
	public void setYearlyEntitlement(YearlyEntitlement yearlyEntitlement) {
		this.yearlyEntitlement = yearlyEntitlement;
	}
	
	private void findYearlyEntitlement() {
		try {
			yearlyEntitlement = yearlyEntitlementService.findOne(selectedYearlyEntitlement);
		} catch(YearlyEntitlementNotFound e) {
			FacesMessage msg = new FacesMessage("Error", "Ooops! Something serious has happened. Contact Administrator.");  
			  
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
		}
	}
	private Double findAnnualYearlyEntitlement(int employeeId){
		try{
				 yearlyEntitlement = yearlyEntitlementService.findAnnualYearlyEntitlementOfEmployee(employeeId);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return	yearlyEntitlement.getYearlyLeaveBalance();
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Double getNumberOfDays() {
		return numberOfDays;
	}
	public void setNumberOfDays(Double numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	
	public YearlyEntitlementService getYearlyEntitlementService() {
		return yearlyEntitlementService;
	}
	public void setYearlyEntitlementService(
			YearlyEntitlementService yearlyEntitlementService) {
		this.yearlyEntitlementService = yearlyEntitlementService;
	}
	
	public LeaveApplicationService getLeaveApplicationService() {
		return leaveApplicationService;
	}
	public void setLeaveApplicationService(
			LeaveApplicationService leaveApplicationService) {
		this.leaveApplicationService = leaveApplicationService;
	}
	
	public LeaveTransactionService getLeaveTransactionService() {
		return leaveTransactionService;
	}
	public void setLeaveTransactionService(
			LeaveTransactionService leaveTransactionService) {
		this.leaveTransactionService = leaveTransactionService;
	}
	public AuditTrail getAuditTrail() {
		return auditTrail;
	}
	public void setAuditTrail(AuditTrail auditTrail) {
		this.auditTrail = auditTrail;
	}
	
	public String applyLeave() throws LeaveApplicationException, RoleNotFound  {
	// Validating whether number of days is half day or full day or not valid
		if(getNumberOfDays()!=null){
		double x = getNumberOfDays().doubleValue() - (long) getNumberOfDays().doubleValue();
		if (!(x == 0.0 || x == 0.5)){
			FacesMessage msg = new FacesMessage(getExcptnMesProperty("error.days.validation"), "Leave error message");  
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
	        return "";
			}
		}
		
		if(isEmployeeFinishedOneYear()){
			if(!(Leave.UNPAID.equalsName(leaveType) || Leave.TIMEINLIEU.equalsName(leaveType)) && !(numberOfDays<= yearlyEntitlement.getYearlyLeaveBalance()))
			{
				FacesMessage msg = new FacesMessage(getExcptnMesProperty("error.sick.validation"), "Leave error message");  
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		        FacesContext.getCurrentInstance().addMessage(null, msg);  
		        return "";
			}
		}
		else
		{
		// validating applied leaves is in the range of current balance - applied leaves > = -3
		if("Annual".equalsIgnoreCase(leaveType)&& StringUtils.isNotBlank(leaveType) && StringUtils.isNotEmpty(leaveType)){
			
				
			if(!(yearlyEntitlement.getCurrentLeaveBalance()-numberOfDays >= -3)){
				FacesMessage msg = new FacesMessage(getExcptnMesProperty("error.sick.validation"), "Leave error message");  
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		        FacesContext.getCurrentInstance().addMessage(null, msg);  
		        return "";
			}
		} else if(!("Unpaid".equalsIgnoreCase(leaveType) || Leave.TIMEINLIEU.equalsName(leaveType))&& StringUtils.isNotBlank(leaveType) && StringUtils.isNotEmpty(leaveType) && numberOfDays > yearlyEntitlement.getYearlyLeaveBalance())
		{
				FacesMessage msg = new FacesMessage(getExcptnMesProperty("error.sick.validation"), "Leave error message");  
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		        FacesContext.getCurrentInstance().addMessage(null, msg);  
		        return "";
		}
		}
		if(numberOfDays<0.5){
			FacesMessage msg = new FacesMessage(getExcptnMesProperty("error.applyleave.numberofdays"), "Leave error message");  
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
	        return "";
		}
		// checking unpaid leaves allowing only maximum 30 days.
		if(Leave.UNPAID.equalsName(leaveType) && numberOfDays>30){
			FacesMessage msg = new FacesMessage(getExcptnMesProperty("error.unpaid.validation"), "Leave error message");  
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
	        return "";
		}
		if(startDate.after(endDate)) {
			FacesMessage msg = new FacesMessage(getExcptnMesProperty("error.applyleave.datesRange"), "Leave error message.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        return "";
		} else {
			
			
			LeaveTransaction leaveTransaction = new LeaveTransaction();
			leaveTransaction.setApplicationDate(new Date());
			leaveTransaction.setDeleted(false);
			leaveTransaction.setEmployee(getEmployee());
			leaveTransaction.setLeaveType(getYearlyEntitlement().getLeaveType());
			leaveTransaction.setNumberOfDays(getNumberOfDays());
			if(!Leave.TIMEINLIEU.equalsName(leaveTransaction.getLeaveType().getName()))
				leaveTransaction.setYearlyLeaveBalance(getYearlyEntitlement().getYearlyLeaveBalance());
			else
				leaveTransaction.setYearlyLeaveBalance(findAnnualYearlyEntitlement(employee.getId()));
			leaveTransaction.setReason(getReason());
			leaveTransaction.setStartDateTime(getStartDate());
			leaveTransaction.setEndDateTime(getEndDate());
			if(getNumberOfDays().doubleValue() - (long) getNumberOfDays().doubleValue()==0.5)
					leaveTransaction.setTimings(getTimings());
			else
				leaveTransaction.setTimings(null);
			leaveTransaction.setCreatedBy(getActorUsers().getUsername());
			leaveTransaction.setCreationTime(new Date());
			leaveTransaction.setStatus(LeaveStatus.PENDING.toString());
			if("Sick".equalsIgnoreCase(leaveType)){
				if(byteData != null){
				String sickLeaveAttachmentName = sickLeaveAttachment.getFileName();	
				leaveTransaction.setSickLeaveAttachment(byteData);
				leaveTransaction.setSickLeaveAttachmentName(sickLeaveAttachmentName);
				} else {
					FacesMessage msg = new FacesMessage(getExcptnMesProperty("error.sickleaveattachment.mcNotFound"), getExcptnMesProperty("error.sickleaveattachment.mcNotFound")); 
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			        FacesContext.getCurrentInstance().addMessage(null, msg);
			        return "";
				}
			}
			
			// Get the Leave Rule for the applying leave applicant
			List<String> roleList = new ArrayList<String>();
			for (Role role : getActorUsers().getUserRoles()) {
				roleList.add(role.getRole().trim());
			}
			LeaveRuleBean  leaveRuleBean = leaveTransactionService.getLeaveRuleByRoleAndLeaveType(leaveType, roleList);
			leaveTransaction.setLeaveRuleBean(leaveRuleBean);
			// saving the decisions taken by the approvers on a list
			LeaveFlowDecisionsTaken leaveFlowDecisions = leaveTransactionService.saveLeaveApprovalDecisions(null);
			// saving the leave transaction bean with the defined rule and decision bean
			leaveTransaction.setDecisionsBean(leaveFlowDecisions);
			
			
			try {
				leaveTransaction.setDecisionToBeTaken(leaveRuleBean.getApproverNameLevel1());
				LeaveTransaction leavePersistBean =	leaveTransactionService.processAppliedLeaveOfEmployee(leaveTransaction);
				ApplLogger.getLogger().info("Employee Leave is applied successfully with transaction ID : "+leavePersistBean.getId());
				ApplLogger.getLogger().info("Employee Transaction Details : "+leavePersistBean.toString());
				LeaveApplicationEmailNotificationService.sendingIntimationEmail(leavePersistBean);
				
				//leaveApplicationService.submitLeave(getEmployee(), getYearlyEntitlement(), leaveTransaction);  Enable for Jbpm process
				
				
				setSelectedYearlyEntitlement(0);
				setLeaveType("");
				setStartDate(null);
				setEndDate(null);
				setReason("");
				auditTrail.log(SystemAuditTrailActivity.CREATED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has successfully applied leave for " + getNumberOfDays() + " day(s).");
				FacesMessage msg = new FacesMessage(getExcptnMesProperty("info.applyleave"), getExcptnMesProperty("info.applyleave")); 
				msg.setSeverity(FacesMessage.SEVERITY_INFO);
				FacesContext.getCurrentInstance().addMessage("index.xhtml", msg); 
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			}  catch (BSLException e) {
				e.printStackTrace();
				auditTrail.log(SystemAuditTrailActivity.CREATED, SystemAuditTrailLevel.ERROR, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has failed to apply leave for " + getNumberOfDays() + " day(s).");
				FacesMessage msg = new FacesMessage(getExcptnMesProperty(e.getMessage()), getExcptnMesProperty(e.getMessage())); 
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage("index.xhtml", msg); 
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			} catch (Exception e) {
				e.printStackTrace();
				auditTrail.log(SystemAuditTrailActivity.CREATED, SystemAuditTrailLevel.ERROR, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has failed to apply leave for " + getNumberOfDays() + " day(s).");
				FacesMessage msg = new FacesMessage(getExcptnMesProperty(e.getMessage()), getExcptnMesProperty(e.getMessage())); 
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage("index.xhtml", msg); 
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			}			
			
		}
		
		return "/protected/index.jsf?faces-redirect=true";
		
	}
	
	public void upload() throws IOException{
		if(sickLeaveAttachment!=null){
		String fileName = sickLeaveAttachment.getFileName();
		String format = fileName.substring(fileName.length()-3);
		if(sickLeaveAttachment!= null && (format.equalsIgnoreCase("jpg") || format.equalsIgnoreCase("png") || format.equalsIgnoreCase("gif") || format.equalsIgnoreCase("pdf")))
		{
			byteData = IOUtils.toByteArray(sickLeaveAttachment.getInputstream());
		}		
		else {
			FacesMessage msg = new FacesMessage(getExcptnMesProperty("error.sickleaveattachment.formatException"), getExcptnMesProperty("error.sickleaveattachment.formatException")); 
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		}		
	}
	
	
	private boolean isEmployeeFinishedOneYear(){
		
		if(employee!=null){
			Calendar joinDate = Calendar.getInstance();
			joinDate.setTime(employee.getJoinDate());
			Calendar today = Calendar.getInstance();
			int curYear = today.get(Calendar.YEAR);
			int curMonth = today.get(Calendar.MONTH);
			int curDay = today.get(Calendar.DAY_OF_MONTH);

			int year = joinDate.get(Calendar.YEAR);
			int month = joinDate.get(Calendar.MONTH);
			int day = joinDate.get(Calendar.DAY_OF_MONTH);

			int age = curYear - year;
			if (curMonth < month || (month == curMonth && curDay < day))
			    age--;
			
			if(age>=1)
				return true;
		}
	
		
		return false;
	}
	public UploadedFile getSickLeaveAttachment() {
		return sickLeaveAttachment;
	}
	public void setSickLeaveAttachment(UploadedFile sickLeaveAttachment) {
		this.sickLeaveAttachment = sickLeaveAttachment;
	}
	public byte[] getByteData() {
		return byteData;
	}
	public void setByteData(byte[] byteData) {
		this.byteData = byteData;
	}

	public String getTimings() {
		return timings;
	}
	public void setTimings(String timings) {
		this.timings = timings;
	}
	
	public void checkHalfDayLeave(Long days){
		if(days==0.5){
			numberOfDays=0.5;
		}
	}

}	
