package com.beans.leaveapp.department.bean;

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
import com.beans.leaveapp.department.model.Department;
import com.beans.leaveapp.department.model.DepartmentDataModel;
import com.beans.leaveapp.department.service.DepartmentNotFound;
import com.beans.leaveapp.department.service.DepartmentService;
import com.beans.leaveapp.refresh.Refresh;
import com.beans.leaveapp.web.bean.BaseMgmtBean;



public class DepartmentManagementBean extends BaseMgmtBean implements Serializable{
private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(this.getClass());
	DepartmentService departmentService;
	private List<Department> departmentList;
	private DepartmentDataModel departmentDataModel;
	private Department newDepartment = new Department();
	private Department selectedDepartment = new Department();
	private boolean insertDeleted = false;
	private Users actorUsers;
	private AuditTrail auditTrail;
	

	public DepartmentService getDepartmentService() {
		return departmentService;
	}

	public Department getNewDepartment() {
		return newDepartment;
	}

	public void setNewDepartment(Department newDepartment) {
		this.newDepartment = newDepartment;
	}

	public Department getSelectedDepartment() {
		return selectedDepartment;
	}

	public void setSelectedDepartment(Department selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
	}

	public boolean isInsertDeleted() {
		return insertDeleted;
	}

	public void setInsertDelete(boolean insertDeleted) {
		this.insertDeleted = insertDeleted;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public List<Department> getDepartmentList() {
		if(departmentList == null || insertDeleted == true) {
			 try {
				departmentList = getDepartmentService().findAll();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return departmentList;	
	}
	
	public DepartmentDataModel getDepartmentDataModel() {
		if(departmentDataModel == null || insertDeleted == true) {
			departmentDataModel = new DepartmentDataModel(getDepartmentList());
		}
		
		return departmentDataModel;
	}

	public void setDepartmentDataModel(
			DepartmentDataModel departmentDataModel) {
		this.departmentDataModel = departmentDataModel;
	}

	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}

	public void doCreateDepartment() throws DepartmentNotFound {

		try{
			newDepartment.setDeleted(false);
			newDepartment.setCreatedBy(getActorUsers().getUsername());
			newDepartment.setCreationTime(new java.util.Date());
			getDepartmentService().create(newDepartment);
			setInsertDelete(true);
			newDepartment = new Department();
			auditTrail.log(SystemAuditTrailActivity.CREATED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has created a department");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info",getExcptnMesProperty("info.department.create")));
			new Refresh().refreshPage();
		}catch(BSLException e){
			FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
		}
	}
	
	public void doUpdateDepartment() {
		try {
			log.info("New name:" + selectedDepartment.getName());
			log.info("ID: " + selectedDepartment.getId());
			selectedDepartment.setLastModifiedBy(getActorUsers().getUsername());
			getDepartmentService().update(selectedDepartment);
			auditTrail.log(SystemAuditTrailActivity.UPDATED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has updated a department");
			setInsertDelete(true);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info",getExcptnMesProperty("info.department.update")));
			new Refresh().refreshPage();
		}catch(BSLException e){
			FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
		setSelectedDepartment((Department) event.getObject());
        FacesMessage msg = new FacesMessage("Department Selected", selectedDepartment.getName());  
        
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }  
	
	
	public void doDeleteDepartment(){
		try {
			getDepartmentService().delete(selectedDepartment.getId());
			auditTrail.log(SystemAuditTrailActivity.DELETED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has deleted a department");
			setInsertDelete(true);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info",getExcptnMesProperty("info.department.delete")));
			new Refresh().refreshPage();
		}catch(BSLException e){
			FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
		}
		
	}
	
	public void doResetFrom() throws DepartmentNotFound {
		
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
