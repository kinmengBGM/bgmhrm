package com.beans.leaveapp.employeeregistration.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import com.beans.common.audit.service.AuditTrail;
import com.beans.common.audit.service.SystemAuditTrailActivity;
import com.beans.common.audit.service.SystemAuditTrailLevel;
import com.beans.common.security.users.model.Users;
import com.beans.exceptions.BSLException;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.model.RegisteredEmployee;
import com.beans.leaveapp.employee.service.EmployeeNotFound;
import com.beans.leaveapp.employee.service.EmployeeRegistrationService;
import com.beans.leaveapp.employee.service.EmployeeService;
import com.beans.leaveapp.employeeregistration.model.RegisteredEmployeeDataModel;
import com.beans.leaveapp.web.bean.BaseMgmtBean;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementService;

public class EmployeeRegistrationApprovalBean extends BaseMgmtBean implements Serializable{
private static final long serialVersionUID = 1L;
	
	
	EmployeeRegistrationService employeeRegistrationService;
	private List<RegisteredEmployee> registeredEmployeeList;
	private RegisteredEmployeeDataModel registeredEmployeeDataModel;
	private RegisteredEmployee selectedRegisteredEmployee = new RegisteredEmployee();
	private boolean insertDeleted = false;
	private Users actorUsers;
	private AuditTrail auditTrail;
	private YearlyEntitlementService yearlyEntitlementService;
	private EmployeeService employeeService;
	private int selectedEmployeeGrade;
	private int selectedDepartment;
	private int selectedEmployeeType;
	
	

	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	public YearlyEntitlementService getYearlyEntitlementService() {
		return yearlyEntitlementService;
	}
	public void setYearlyEntitlementService(
			YearlyEntitlementService yearlyEntitlementService) {
		this.yearlyEntitlementService = yearlyEntitlementService;
	}
	public EmployeeRegistrationService getEmployeeRegistrationService() {
		return employeeRegistrationService;
	}
	public void setEmployeeRegistrationService(
			EmployeeRegistrationService employeeRegistrationService) {
		this.employeeRegistrationService = employeeRegistrationService;
	}
	
	public RegisteredEmployee getSelectedRegisteredEmployee() {
		return selectedRegisteredEmployee;
	}
	public void setSelectedRegisteredEmployee(
			RegisteredEmployee selectedRegisteredEmployee) {
		this.selectedRegisteredEmployee = selectedRegisteredEmployee;
	}

	public boolean isInsertDeleted() {
		return insertDeleted;
	}

	public void setInsertDelete(boolean insertDeleted) {
		this.insertDeleted = insertDeleted;
	}

	public List<RegisteredEmployee> getRegisteredEmployeeList() {
		if(registeredEmployeeList == null || insertDeleted == true) {
			 try {
				 registeredEmployeeList = getEmployeeRegistrationService().getPendingRegisteredEmployee(actorUsers.getUsername());
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return registeredEmployeeList;
	}
	public void setRegisteredEmployeeDataModel(
			RegisteredEmployeeDataModel registeredEmployeeDataModel) {
		this.registeredEmployeeDataModel = registeredEmployeeDataModel;
	}
	
	public RegisteredEmployeeDataModel getRegisteredEmployeeDataModel() {
		if(registeredEmployeeDataModel == null || insertDeleted == true) {
			registeredEmployeeDataModel = new RegisteredEmployeeDataModel(getRegisteredEmployeeList());
		}
				
		return registeredEmployeeDataModel;
	}
	public void setRegisteredEmployeeList(
			List<RegisteredEmployee> registeredEmployeeList) {
		this.registeredEmployeeList = registeredEmployeeList;
	}
	
	
	public void doApproveEmployeeRegistration() throws EmployeeNotFound {
		try {
			selectedRegisteredEmployee.setDepartmentId(selectedDepartment);
			selectedRegisteredEmployee.setEmployeeGradeId(selectedEmployeeGrade);
			selectedRegisteredEmployee.setEmployeeTypeId(selectedEmployeeType);
			
			employeeRegistrationService.approveRegistration(selectedRegisteredEmployee, getActorUsers().getUsername());
			
			Employee employee =  employeeService.findByUsername(selectedRegisteredEmployee.getUsername());
			
			yearlyEntitlementService.addAllEntitlementsToNewEmployee(employee);
			
			auditTrail.log(SystemAuditTrailActivity.APPROVED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has approved a employee registration of " + selectedRegisteredEmployee.getFullname());
			setInsertDelete(true);
			setSelectedDepartment(0);
			setSelectedEmployeeGrade(0);
			setSelectedEmployeeType(0);
		}catch(BSLException e){
			FacesMessage msg = new FacesMessage("Error : "+getExcptnMesProperty(e.getMessage()),"Approve Error");  
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
			
		} catch(Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public void doRejectEmployeeRegistration() {
		try {
			if(selectedRegisteredEmployee.getReason() == null || selectedRegisteredEmployee.getReason().trim().equals("")) {
				FacesMessage msg = new FacesMessage("Please enter Reason for rejection");  
		        
		        FacesContext.getCurrentInstance().addMessage(null, msg);  
			} else {
				
				employeeRegistrationService.rejectRegistration(selectedRegisteredEmployee, getActorUsers().getUsername());
				
				auditTrail.log(SystemAuditTrailActivity.REJECTED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has approved a employee registration of " + selectedRegisteredEmployee.getFullname() + " due to " + selectedRegisteredEmployee.getReason());
				setInsertDelete(true);
				setSelectedDepartment(0);
				setSelectedEmployeeGrade(0);
				setSelectedEmployeeType(0);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
		setSelectedRegisteredEmployee((RegisteredEmployee) event.getObject()); 
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
	
	public int getSelectedDepartment() {
		return selectedDepartment;
	}
	public void setSelectedDepartment(int selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
	}
	public int getSelectedEmployeeGrade() {
		return selectedEmployeeGrade;
	}
	public void setSelectedEmployeeGrade(int selectedEmployeeGrade) {
		this.selectedEmployeeGrade = selectedEmployeeGrade;
	}
	public int getSelectedEmployeeType() {
		return selectedEmployeeType;
	}
	public void setSelectedEmployeeType(int selectedEmployeeType) {
		this.selectedEmployeeType = selectedEmployeeType;
	}
}

