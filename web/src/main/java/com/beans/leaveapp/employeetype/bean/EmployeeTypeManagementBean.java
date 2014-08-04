
package com.beans.leaveapp.employeetype.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;

import com.beans.common.audit.service.AuditTrail;
import com.beans.common.audit.service.SystemAuditTrailActivity;
import com.beans.common.audit.service.SystemAuditTrailLevel;
import com.beans.common.security.users.model.Users;
import com.beans.exceptions.BSLException;
import com.beans.leaveapp.employeetype.model.EmployeeType;
import com.beans.leaveapp.employeetype.model.EmployeeTypeDataModel;
import com.beans.leaveapp.employeetype.service.EmployeeTypeNotFound;
import com.beans.leaveapp.employeetype.service.EmployeeTypeService;
import com.beans.leaveapp.refresh.Refresh;
import com.beans.leaveapp.web.bean.BaseMgmtBean;


public class EmployeeTypeManagementBean extends BaseMgmtBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	// EmployeeGradeRepository employeeGradeRepository;
	EmployeeTypeService employeeTypeService;
	private List<EmployeeType> employeeTypeList;
	private EmployeeTypeDataModel employeeTypeDataModel;
	private EmployeeType newEmployeeType = new EmployeeType();
	private EmployeeType selectedEmployeeType = new EmployeeType();
	private boolean insertDeleted = false;
	private Users actorUsers;
	private AuditTrail auditTrail;
	
	public EmployeeTypeService getEmployeeTypeService() {
		return employeeTypeService;
	}

	public EmployeeType getNewEmployeeType() {
		return newEmployeeType;
	}

	public void setNewEmployeeType(EmployeeType newEmployeeType) {
		this.newEmployeeType = newEmployeeType;
	}

	public EmployeeType getSelectedEmployeeType() {
		return selectedEmployeeType;
	}

	public void setSelectedEmployeeType(EmployeeType selectedEmployeeType) {
		this.selectedEmployeeType = selectedEmployeeType;
	}

	public boolean isInsertDeleted() {
		return insertDeleted;
	}

	public void setInsertDelete(boolean insertDeleted) {
		this.insertDeleted = insertDeleted;
	}

	public void setEmployeeTypeService(EmployeeTypeService employeeTypeService) {
		this.employeeTypeService = employeeTypeService;
	}

	public List<EmployeeType> getEmployeeTypeList() {
		if(employeeTypeList == null || insertDeleted == true) {
			 try {
				employeeTypeList = getEmployeeTypeService().findAll();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return employeeTypeList;	
	}
	
	public EmployeeTypeDataModel getEmployeeTypeDataModel() {
		if(employeeTypeDataModel == null || insertDeleted == true) {
			employeeTypeDataModel = new EmployeeTypeDataModel(getEmployeeTypeList());
		}
		
		return employeeTypeDataModel;
	}

	public void setEmployeeTypeDataModel(
			EmployeeTypeDataModel employeeTypeDataModel) {
		this.employeeTypeDataModel = employeeTypeDataModel;
	}

	public void setEmployeeTypeList(List<EmployeeType> employeeTypeList) {
		this.employeeTypeList = employeeTypeList;
	}

	public void doCreateEmployeeType() throws EmployeeTypeNotFound {
		try{
		newEmployeeType.setDeleted(false);
		newEmployeeType.setCreatedBy(getActorUsers().getUsername());
		newEmployeeType.setCreationTime(new Date());
		getEmployeeTypeService().create(newEmployeeType);
		setInsertDelete(true);
		newEmployeeType = new EmployeeType();
		auditTrail.log(SystemAuditTrailActivity.CREATED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has created an employee type");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info",getExcptnMesProperty("info.emptype.create")));
		new Refresh().refreshPage();
		}catch(BSLException e){
			FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
		}
	}
	
	public void doUpdateEmployeeType(){
		try {
				log.info("New name:" + selectedEmployeeType.getName());
				log.info("ID: " + selectedEmployeeType.getId());
				selectedEmployeeType.setLastModifiedBy(getActorUsers().getUsername());
				getEmployeeTypeService().update(selectedEmployeeType);
				
				auditTrail.log(SystemAuditTrailActivity.UPDATED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has updated an employee type");
				this.setInsertDelete(true);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info",getExcptnMesProperty("info.emptype.update")));
				new Refresh().refreshPage();
			}catch(BSLException e){
				FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
				msg.setSeverity(FacesMessage.SEVERITY_INFO);
		        FacesContext.getCurrentInstance().addMessage(null, msg); 
			}
	}
	
	public void onRowSelect(SelectEvent event) {  
		setSelectedEmployeeType((EmployeeType) event.getObject());
        FacesMessage msg = new FacesMessage("Employee Type Selected", selectedEmployeeType.getName());  
        
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }  
	
	
	public void doDeleteEmployeeType(){
		try {
			getEmployeeTypeService().delete(selectedEmployeeType.getId());
			auditTrail.log(SystemAuditTrailActivity.DELETED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has deleted an employee type");
			setInsertDelete(true);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info",getExcptnMesProperty("info.emptype.delete")));
			new Refresh().refreshPage();
		}catch(BSLException e){
			FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
		}
		
	}
	
	public void doResetFrom() throws EmployeeTypeNotFound {
		
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

}

