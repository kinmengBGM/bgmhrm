package com.beans.leaveapp.accessrights.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import com.beans.common.audit.service.AuditTrail;
import com.beans.common.audit.service.SystemAuditTrailActivity;
import com.beans.common.audit.service.SystemAuditTrailLevel;
import com.beans.common.security.accessrights.model.AccessRights;
import com.beans.common.security.accessrights.service.AccessRightsNotFound;
import com.beans.common.security.accessrights.service.AccessRightsService;
import com.beans.common.security.users.model.Users;
import com.beans.exceptions.BSLException;
import com.beans.leaveapp.accessrights.model.AccessRightsDataModel;
import com.beans.leaveapp.refresh.Refresh;
import com.beans.leaveapp.web.bean.BaseMgmtBean;

public class AccessRightsManagement extends BaseMgmtBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	AccessRightsService accessRightsService;
	private List<AccessRights> accessRightsList;
	private AccessRightsDataModel accessRightsDataModel;
	private AccessRights newAccessRights = new AccessRights();
	private AccessRights selectedAccessRights = new AccessRights();
	private boolean insertDelete = false;
	private List<AccessRights> searchAccessRights;
	
	private String searchAccessRight = "";
	
	private Users actorUsers;
	private AuditTrail auditTrail;	
	
	public AccessRightsService getAccessRightsService() {
		return accessRightsService;
	}
	
	public void setAccessRightsService(AccessRightsService accessRightsService) {
		this.accessRightsService = accessRightsService;
	}
	
	
	public List<AccessRights> getAccessRightsList() {
		
		if(accessRightsList == null || insertDelete == true){
			
			accessRightsList = accessRightsService.findAll();
		}		
		return accessRightsList;
	}
	
	public void setAccessRightsList(List<AccessRights> accessRightsList) {
		this.accessRightsList = accessRightsList;
	}
	
	
	public AccessRightsDataModel getAccessRightsDataModel() {
		
		if(accessRightsDataModel == null || insertDelete == true){
			
			accessRightsDataModel = new AccessRightsDataModel(getAccessRightsList());
		}		
		return accessRightsDataModel;
	}
	
	public void setAccessRightsDataModel(AccessRightsDataModel accessRightsDataModel) {
		this.accessRightsDataModel = accessRightsDataModel;
	}
	
	
	public AccessRights getNewAccessRights() {
		return newAccessRights;
	}
	
	public void setNewAccessRights(AccessRights newAccessRights) {
		this.newAccessRights = newAccessRights;
	}
	
	public void doCreateAccessRights() throws AccessRightsNotFound {
	try{
		newAccessRights.setDeleted(false);
		newAccessRights.setCreatedBy(getActorUsers().getUsername());
		newAccessRights.setCreationTime(new java.util.Date());
		getAccessRightsService().create(newAccessRights);
		setInsertDelete(true);	
		newAccessRights = new AccessRights();
		auditTrail.log(SystemAuditTrailActivity.CREATED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has Created Access Right " + newAccessRights.getAccessRights());	
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info",getExcptnMesProperty("info.accessRights.create")));
		new Refresh().refreshPage();
	}catch(BSLException e){
		FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
		msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, msg); 
	}
}
	
	
	public AccessRights getSelectedAccessRights() {
		return selectedAccessRights;
	}
	
	public void setSelectedAccessRights(AccessRights selectedAccessRights) {
		this.selectedAccessRights = selectedAccessRights;
	}
	
	
	public void doUpdateAccessRights() throws AccessRightsNotFound {
		try {
			System.out.println("New UserAccess:" + selectedAccessRights.getAccessRights());
			System.out.println("Id:" + selectedAccessRights.getId());
			System.out.println("Description:" + selectedAccessRights.getDescription());
			selectedAccessRights.setLastModifiedBy(actorUsers.getUsername());
			accessRightsService.update(selectedAccessRights);		
		
			auditTrail.log(SystemAuditTrailActivity.UPDATED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has Updated Access Right " + selectedAccessRights.getAccessRights() + " with id " + selectedAccessRights.getId());
			setInsertDelete(true);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info",getExcptnMesProperty("info.accessRights.update")));
		} catch (BSLException e){
			FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);  
			FacesContext.getCurrentInstance().addMessage(null, msg);  
		}		
	}
	
	public void onRowSelect(SelectEvent event){
		setSelectedAccessRights((AccessRights)event.getObject());
		 FacesMessage msg = new FacesMessage("UserAccess Name Selected: ", selectedAccessRights.getAccessRights()); 
	        
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
	}
	
	
	public void doDeleteAccessRights() throws AccessRightsNotFound {
		try {
			getAccessRightsService().delete(selectedAccessRights.getId());
			
			auditTrail.log(SystemAuditTrailActivity.CREATED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has Deleted Access Right " + selectedAccessRights.getAccessRights());
			setInsertDelete(true);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info",getExcptnMesProperty("info.accessRights.delete")));
			new Refresh().refreshPage();
		}catch (BSLException e) {
			FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
			msg.setSeverity(FacesMessage.SEVERITY_ERROR); 
			FacesContext.getCurrentInstance().addMessage(null, msg);  
		}			
	}
	
	public boolean isInsertDelete() {
		return insertDelete;
	}
	public void setInsertDelete(boolean insertDelete) {
		this.insertDelete = insertDelete;
	}
	public List<AccessRights> getSearchAccessRights() {
		return searchAccessRights;
	}
	public void setSearchAccessRights(List<AccessRights> searchAccessRights) {
		this.searchAccessRights = searchAccessRights;
	}

	public String getSearchAccessRight() {
		return searchAccessRight;
	}

	public void setSearchAccessRight(String searchAccessRight) {
		this.searchAccessRight = searchAccessRight;
	}	
	
	
	public void searchAccessRightName(){		
		if(getSearchAccessRight() == null || getSearchAccessRight().trim().equals("")){
			this.accessRightsList = null;
			this.accessRightsDataModel = null;			
		}else {
			this.accessRightsList = accessRightsService.findAccessRightsByAccessRight(getSearchAccessRight());
			this.accessRightsDataModel = null;
		}
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
