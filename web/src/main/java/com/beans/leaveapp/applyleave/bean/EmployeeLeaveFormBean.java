package com.beans.leaveapp.applyleave.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;

import com.beans.common.audit.service.AuditTrail;
import com.beans.common.audit.service.SystemAuditTrailActivity;
import com.beans.common.audit.service.SystemAuditTrailLevel;
import com.beans.common.security.role.service.RoleNotFound;
import com.beans.common.security.users.model.Users;
import com.beans.exceptions.BSLException;
import com.beans.leaveapp.applyleave.service.LeaveApplicationException;
import com.beans.leaveapp.applyleave.service.LeaveApplicationService;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.web.bean.BaseMgmtBean;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementNotFound;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementService;
import com.beans.util.enums.Leave;

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
	private AuditTrail auditTrail;
	private Double allowedMaximumLeaves;
	
	
	
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
	
	public AuditTrail getAuditTrail() {
		return auditTrail;
	}
	public void setAuditTrail(AuditTrail auditTrail) {
		this.auditTrail = auditTrail;
	}
	
	public String applyLeave() throws LeaveApplicationException, RoleNotFound  {
		
		if(isEmployeeFinishedOneYear()){
			if(!Leave.UNPAID.equalsName(leaveType) && !(numberOfDays<= yearlyEntitlement.getYearlyLeaveBalance()))
			{
				FacesMessage msg = new FacesMessage(getExcptnMesProperty("error.sick.validation"), "Leave error message");  
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		        FacesContext.getCurrentInstance().addMessage(null, msg);  
		        return "";
			}
		}
		else
		{
		if(numberOfDays<0.5){
			FacesMessage msg = new FacesMessage(getExcptnMesProperty("error.applyleave.numberofdays"), "Leave error message");  
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
	        return "";
		}
		// validating applied leaves is in the range of current balance - applied leaves > = -3
		if("Annual".equalsIgnoreCase(leaveType)&& StringUtils.isNotBlank(leaveType) && StringUtils.isNotEmpty(leaveType)){
			
				
			if(!(yearlyEntitlement.getCurrentLeaveBalance()-numberOfDays >= -3)){
				FacesMessage msg = new FacesMessage(getExcptnMesProperty("error.sick.validation"), "Leave error message");  
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		        FacesContext.getCurrentInstance().addMessage(null, msg);  
		        return "";
			}
		} else if(!"Unpaid".equalsIgnoreCase(leaveType)&& StringUtils.isNotBlank(leaveType) && StringUtils.isNotEmpty(leaveType) && numberOfDays > yearlyEntitlement.getYearlyLeaveBalance())
		{
				FacesMessage msg = new FacesMessage(getExcptnMesProperty("error.sick.validation"), "Leave error message");  
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		        FacesContext.getCurrentInstance().addMessage(null, msg);  
		        return "";
		}
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
		    leaveTransaction.setYearlyLeaveBalance(getYearlyEntitlement().getYearlyLeaveBalance());
			leaveTransaction.setReason(getReason());
			leaveTransaction.setStartDateTime(getStartDate());
			leaveTransaction.setEndDateTime(getEndDate());
			leaveTransaction.setCreatedBy(getActorUsers().getUsername());
			leaveTransaction.setCreationTime(new Date());
			leaveTransaction.setStatus("Pending");
			try {
				leaveApplicationService.submitLeave(getEmployee(), getYearlyEntitlement(), leaveTransaction);
				
				setSelectedYearlyEntitlement(0);
				setLeaveType("");
				setStartDate(null);
				setEndDate(null);
				setReason("");
				auditTrail.log(SystemAuditTrailActivity.CREATED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has successfully applied annual leave for " + getNumberOfDays() + " day(s).");
				FacesMessage msg = new FacesMessage(getExcptnMesProperty("info.applyleave"), getExcptnMesProperty("info.applyleave")); 
				msg.setSeverity(FacesMessage.SEVERITY_INFO);
				FacesContext.getCurrentInstance().addMessage("index.xhtml", msg); 
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			}  catch (BSLException e) {
				e.printStackTrace();
				auditTrail.log(SystemAuditTrailActivity.CREATED, SystemAuditTrailLevel.ERROR, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has failed to apply annual leave for " + getNumberOfDays() + " day(s).");
				FacesMessage msg = new FacesMessage(getExcptnMesProperty(e.getMessage()), getExcptnMesProperty(e.getMessage())); 
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage("index.xhtml", msg); 
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			} catch (Exception e) {
				e.printStackTrace();
				auditTrail.log(SystemAuditTrailActivity.CREATED, SystemAuditTrailLevel.ERROR, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has failed to apply annual leave for " + getNumberOfDays() + " day(s).");
				FacesMessage msg = new FacesMessage(getExcptnMesProperty(e.getMessage()), getExcptnMesProperty(e.getMessage())); 
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage("index.xhtml", msg); 
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			}
			
			
		}
		
		return "/protected/index.jsf?faces-redirect=true";
		
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
	
	
	
}
