
package com.beans.leaveapp.roletoaccessrights.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;

import com.beans.common.audit.service.AuditTrail;
import com.beans.common.audit.service.SystemAuditTrailActivity;
import com.beans.common.audit.service.SystemAuditTrailLevel;
import com.beans.common.security.accessrights.model.AccessRights;
import com.beans.common.security.accessrights.service.AccessRightsService;
import com.beans.common.security.role.model.Role;
import com.beans.common.security.role.service.RoleService;
import com.beans.common.security.users.model.Users;
import com.beans.exceptions.BSLException;
import com.beans.leaveapp.role.model.RoleDataModel;
import com.beans.leaveapp.web.bean.BaseMgmtBean;

public class RoleToAccessRightsManagement extends BaseMgmtBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private RoleService roleService;
	private AccessRightsService accessRightsService;
	private List<Role> roleList;
	private RoleDataModel roleDataModel;
	private Role selectedRole = new Role();
	private boolean insertDelete = false;
	private List<Role> searchRole;
	private Set<AccessRights> accessRightsSet = null;
	private Set<AccessRights> assignedAccessRightsSet = new HashSet<AccessRights>();
	private Set<AccessRights> unassignedAccessRightsSet = new HashSet<AccessRights>();
	private DualListModel<AccessRights> dualAccessRightsList = new DualListModel<AccessRights>();
	
	private String searchRoleName = "";
	
	private Users actorUsers;
	private AuditTrail auditTrail; 
	
	@PostConstruct
	public void init(){
		accessRightsSet = accessRightsService.findAllInSet();
	}	
	
	
	public RoleService getRoleService() {
		return roleService;
	}
	
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	
	
	public AccessRightsService getAccessRightsService() {
		return accessRightsService;
	}
	
	public void setAccessRightsService(AccessRightsService accessRightsService) {
		this.accessRightsService = accessRightsService;
	}
	
	public List<Role> getRoleList() {
		
		if(roleList == null || insertDelete == true){
			
			roleList = roleService.findAll();			
		}
		return roleList;
	}
	
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	
	
	public RoleDataModel getRoleDataModel() {
	
		if(roleDataModel == null || insertDelete == true){
			roleDataModel = new RoleDataModel(getRoleList());			
		}		
		return roleDataModel;
	}
	
	public void setRoleDataModel(RoleDataModel roleDataModel) {
		this.roleDataModel = roleDataModel;
	}
	
	
	public Role getSelectedRole() {
		return selectedRole;
	}
	
	public void setSelectedRole(Role selectedRole) {		
		this.selectedRole = selectedRole;
		this.assignedAccessRightsSet = this.selectedRole.getAccessRights();
		this.unassignedAccessRightsSet.addAll(this.accessRightsSet);
		this.unassignedAccessRightsSet.removeAll(this.assignedAccessRightsSet);
		List<AccessRights> unassignedAccessRightsList = new ArrayList<AccessRights>();
		unassignedAccessRightsList.addAll(unassignedAccessRightsSet);
		List<AccessRights> assignedAccessRightsList = new ArrayList<AccessRights>();
		assignedAccessRightsList.addAll(assignedAccessRightsSet);
		this.dualAccessRightsList = new DualListModel<AccessRights>(unassignedAccessRightsList, assignedAccessRightsList);		
	}
	
	public void saveRoleToAccessRightsMapping(){
		try {
			List<AccessRights> selectedAccessRightsList = dualAccessRightsList.getTarget();
			HashSet<AccessRights> selectedAccessRightsSet = new HashSet<AccessRights>();
			selectedAccessRightsSet.addAll(selectedAccessRightsList);
			selectedRole.setAccessRights(selectedAccessRightsSet);
			getRoleService().update(selectedRole);
			
			auditTrail.log(SystemAuditTrailActivity.UPDATED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has Assigned Access Rights for " +selectedRole.getRole());
		} catch (BSLException e){
			FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			 FacesContext.getCurrentInstance().addMessage(null, msg); 
		}		
		
	}
	
	public void onRowSelect(SelectEvent event){
		setSelectedRole((Role) event.getObject());
	}	
	
	public boolean isInsertDelete() {
		return insertDelete;
	}
	public void setInsertDelete(boolean insertDelete) {
		this.insertDelete = insertDelete;
	}
	public List<Role> getSearchRole() {
		return searchRole;
	}
	public void setSearchRole(List<Role> searchRole) {
		this.searchRole = searchRole;
	}
	public Set<AccessRights> getAccessRightsSet() {
		return accessRightsSet;
	}
	public void setAccessRightsSet(Set<AccessRights> accessRightsSet) {
		this.accessRightsSet = accessRightsSet;
	}
	public Set<AccessRights> getAssignedAccessRightsSet() {
		return assignedAccessRightsSet;
	}
	public void setAssignedAccessRightsSet(Set<AccessRights> assignedAccessRightsSet) {
		this.assignedAccessRightsSet = assignedAccessRightsSet;
	}
	public Set<AccessRights> getUnassignedAccessRightsSet() {
		return unassignedAccessRightsSet;
	}
	public void setUnassignedAccessRightsSet(
			Set<AccessRights> unassignedAccessRightsSet) {
		this.unassignedAccessRightsSet = unassignedAccessRightsSet;
	}
	public DualListModel<AccessRights> getDualAccessRightsList() {
		return dualAccessRightsList;
	}
	public void setDualAccessRightsList(
			DualListModel<AccessRights> dualAccessRightsList) {
		this.dualAccessRightsList = dualAccessRightsList;
	}


	public String getSearchRoleName() {
		return searchRoleName;
	}


	public void setSearchRoleName(String searchRoleName) {
		this.searchRoleName = searchRoleName;
	}
	
	
	public void searchRolename(){
		
		if(getSearchRoleName() == null || getSearchRoleName().trim().equals("")){
			this.roleList = null;
			this.roleDataModel = null;
		}else{
			this.roleList = roleService.findRoleByRoleName(getSearchRoleName());
			this.roleDataModel = null;
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

