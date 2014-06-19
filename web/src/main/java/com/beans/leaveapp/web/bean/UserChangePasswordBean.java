package com.beans.leaveapp.web.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.beans.common.audit.service.AuditTrail;
import com.beans.common.audit.service.SystemAuditTrailActivity;
import com.beans.common.audit.service.SystemAuditTrailLevel;
import com.beans.common.security.users.model.Users;
import com.beans.common.security.users.service.ChangePasswordException;
import com.beans.common.security.users.service.UsersNotFound;
import com.beans.common.security.users.service.UsersService;

public class UserChangePasswordBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private Users actorUsers;
	private String oldPassword;
	private String newPassword;
	private UsersService usersService;
	private AuditTrail auditTrail;
	
	public Users getActorUsers() {
		return actorUsers;
	}
	public void setActorUsers(Users actorUsers) {
		this.actorUsers = actorUsers;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	public UsersService getUsersService() {
		return usersService;
	}
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	public AuditTrail getAuditTrail() {
		return auditTrail;
	}
	public void setAuditTrail(AuditTrail auditTrail) {
		this.auditTrail = auditTrail;
	}
	
	public void doUpdatePassword() {
		try {
			usersService.changePassword(actorUsers, getOldPassword(), getNewPassword());
			auditTrail.log(SystemAuditTrailActivity.UPDATED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has updated his/her password");
			FacesMessage msg = new FacesMessage("Info", "New password saved.");  
			  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
		} catch(ChangePasswordException e) {
			FacesMessage msg = new FacesMessage("Error", e.getLocalizedMessage());  
			  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
	        auditTrail.log(SystemAuditTrailActivity.UPDATED, SystemAuditTrailLevel.ERROR, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " tried to update his/her password but failed due to '" + e.getMessage() + "'");
		} catch(UsersNotFound e) {
			FacesMessage msg = new FacesMessage("Error", "Unexpected error. Please contact your Administrator");  
			  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
	        auditTrail.log(SystemAuditTrailActivity.UPDATED, SystemAuditTrailLevel.ERROR, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " tried to update his/her password but failed due to '" + e.getMessage() + "'");
		}
	}
	
}
