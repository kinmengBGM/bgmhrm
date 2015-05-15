package com.custom.wrapper.audittrail;

import com.beans.common.audit.service.SystemAuditTrailActivity;
import com.beans.common.audit.service.SystemAuditTrailLevel;

public class LogWrapper {
	SystemAuditTrailActivity systemAuditTrailActivity;
	SystemAuditTrailLevel systemAuditTrailLevel;
	int userId;
	String username;
	String description;
	
	public LogWrapper(SystemAuditTrailActivity systemAuditTrailActivity, SystemAuditTrailLevel systemAuditTrailLevel, int userId, String username, String description){
		this.systemAuditTrailActivity = systemAuditTrailActivity;
		this.systemAuditTrailLevel =systemAuditTrailLevel;
		this.userId = userId;
		this.username = username;
		this.description = description;
	}
	
	public SystemAuditTrailActivity getSystemAuditTrailActivity(){
		return this.systemAuditTrailActivity;
	}

	public void setSystemAuditTrailActivity(SystemAuditTrailActivity systemAuditTrailActivity){
		this.systemAuditTrailActivity = systemAuditTrailActivity;
	}
	
	public SystemAuditTrailLevel getSystemAuditTrailLevel(){
		return this.systemAuditTrailLevel;
	}	

	public void setSystemAuditTrailLevel(SystemAuditTrailLevel systemAuditTrailLevel){
		this.systemAuditTrailLevel = systemAuditTrailLevel;
	}
		
	public int getUserId(){
		return this.userId;
	}	

	public void setUserId(int userId){
		this.userId = userId;
	}
	
	public String getUsername(){
		return this.username;
	}

	public void setUsername(String username){
		this.username = username;
	}
	
	public String getDescription(){
		return this.description;
	}

	public void setDescription(String description){
		this.description = description;
	}
	
	
}

