package com.beans.leaveapp.usertorole.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import com.beans.common.audit.service.AuditTrail;
import com.beans.common.audit.service.SystemAuditTrailActivity;
import com.beans.common.audit.service.SystemAuditTrailLevel;
import com.beans.common.security.role.model.Role;
import com.beans.common.security.role.service.RoleService;
import com.beans.common.security.users.model.Users;
import com.beans.common.security.users.service.UsersNotFound;
import com.beans.common.security.users.service.UsersService;
import com.beans.leaveapp.usertorole.model.UserToRoleDataModel;

public class UserToRoleManagement implements Serializable{
	private static final long serialVersionUID = 1L;
	private UsersService usersService;
	private RoleService roleService;
	private List<Users> usersList;
	private UserToRoleDataModel userToRoleDataModel;
	private Users selectedUsers = new Users();
	private boolean insertDelete = false;
	private List<Users> searchUsers;
	private Set<Role> roleSet = null;
	private Set<Role> assignedRoleSet = new HashSet<Role>();
	private Set<Role> unassignedRoleSet = new HashSet<Role>();
	private DualListModel<Role> dualRoleList = new DualListModel<Role>();
	
	private String searchUsername = "";
	
	private Users actorUsers;
	private AuditTrail auditTrail; 
	
	@PostConstruct
	public void init() {
		roleSet = roleService.findAllInSet();
	}
	public UsersService getUsersService() {
		return usersService;
	}
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	
	public Set<Role> getAssignedRoleSet() {
		return assignedRoleSet;
	}
	public void setAssignedRoleSet(Set<Role> assignedRoleSet) {
		this.assignedRoleSet = assignedRoleSet;
	}
	
	public Set<Role> getUnassignedRoleSet() {
		return unassignedRoleSet;
	}
	public void setUnassignedRoleSet(Set<Role> unassignedRoleSet) {
		this.unassignedRoleSet = unassignedRoleSet;
	}
	
	public List<Users> getUsersList() {
		
		if(usersList == null || insertDelete == true ){
			
			usersList = usersService.findAll();
		}
		
		return usersList;
	}
	
	public void setUsersList(List<Users> usersList) {
		this.usersList = usersList;
	}
	
	
	public UserToRoleDataModel getUserToRoleDataModel() {
		if(userToRoleDataModel == null || insertDelete == true){			
			userToRoleDataModel = new UserToRoleDataModel(getUsersList());			
		}		
		return userToRoleDataModel;
	}	
	
	public void setUserToRoleDataModel(UserToRoleDataModel userToRoleDataModel) {
		this.userToRoleDataModel = userToRoleDataModel;
	}
	
	
	public Users getSelectedUsers() {
		return selectedUsers;
	}
	
	public void setSelectedUsers(Users selectedUsers) {
		this.selectedUsers = selectedUsers;
		this.assignedRoleSet = this.selectedUsers.getUserRoles();
		this.unassignedRoleSet.addAll(this.roleSet);
		this.unassignedRoleSet.removeAll(this.assignedRoleSet);
		List<Role> unassignedRoleList = new ArrayList<Role>();
		unassignedRoleList.addAll(unassignedRoleSet);
		
		List<Role> assignedRoleList = new ArrayList<Role>();
		assignedRoleList.addAll(assignedRoleSet);
		this.dualRoleList = new DualListModel<Role>(unassignedRoleList, assignedRoleList);
	}
	
	public DualListModel<Role> getDualRoleList() {
		return dualRoleList;
	}
	public void setDualRoleList(DualListModel<Role> dualRoleList) {
		this.dualRoleList = dualRoleList;
	}
	
	public void saveRoleMapping() {
		try{
			List<Role> selectedRoleList = dualRoleList.getTarget();
			HashSet<Role> selectedRoleSet = new HashSet<Role>();
			selectedRoleSet.addAll(selectedRoleList);
			selectedUsers.setUserRoles(selectedRoleSet);
			getUsersService().update(selectedUsers);
			
			auditTrail.log(SystemAuditTrailActivity.UPDATED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has Assigned the roles for " +selectedUsers.getUsername());
			
			
		}catch(UsersNotFound e){
			
			FacesMessage msg = new FacesMessage("Error" , "User Role With userId" + selectedUsers.getId() + "not found");
			 FacesContext.getCurrentInstance().addMessage(null, msg);  
		}		
	}
	
	public void onRowSelect(SelectEvent event){
		setSelectedUsers((Users) event.getObject());

	}	
	
	public boolean isInsertDelete() {
		return insertDelete;
	}
	public void setInsertDelete(boolean insertDelete) {
		this.insertDelete = insertDelete;
	}
	public List<Users> getSearchUsers() {
		return searchUsers;
	}
	public void setSerachUserToRole(List<Users> serachUsers) {
		this.searchUsers = serachUsers;
	}		
	public RoleService getRoleService() {
		return roleService;
	}
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	
	
	public String getSearchUsername() {
		return searchUsername;
	}
	public void setSearchUsername(String searchUsername) {
		this.searchUsername = searchUsername;
	}
	
	public void searchUser(){		
		if(getSearchUsername() == null || getSearchUsername().trim().equals("")){
			this.usersList = null;
			this.userToRoleDataModel = null;			
		}else {
			this.usersList = usersService.findUsersByUsername(getSearchUsername());
			this.userToRoleDataModel = null;
		}		
	}
	
	public Set<Role> getRoleSet() {
		return roleSet;
	}
	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
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
	public void setSearchUsers(List<Users> searchUsers) {
		this.searchUsers = searchUsers;
	}
	
	
	
}
