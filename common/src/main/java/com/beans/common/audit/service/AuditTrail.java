package com.beans.common.audit.service;

import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.beans.common.audit.model.SystemAuditTrail;
import com.beans.common.audit.service.SystemAuditTrailActivity;
import com.beans.common.audit.service.SystemAuditTrailLevel;
import com.beans.common.audit.service.SystemAuditTrailRecordService;
import com.beans.util.config.ConfigurationHolder;

public class AuditTrail {
	private SystemAuditTrailRecordService systemAuditTrailRecordService;
	
	@Transactional
	public void log(SystemAuditTrailActivity systemAuditTrailActivity, SystemAuditTrailLevel systemAuditTrailLevel,
			int userId, String username, String description) {
		
		if(ConfigurationHolder.getBoolean("audit.enabled")) {
			
			SystemAuditTrailLevel configuredLevel = SystemAuditTrailLevel.valueOf(ConfigurationHolder.getString("audit.minimumlevel"));
			
			if(configuredLevel.getLevelInt() <= systemAuditTrailLevel.getLevelInt()) {
				SystemAuditTrail systemAuditTrail = new SystemAuditTrail();
				Date currentDate = new Date();
				systemAuditTrail.setDate(currentDate);
				systemAuditTrail.setActivity(systemAuditTrailActivity.getActivity());
				systemAuditTrail.setLevel(systemAuditTrailLevel.getLevel());
				systemAuditTrail.setActorUsername(username);
				systemAuditTrail.setActorUserId(userId);
				systemAuditTrail.setDescription(description);
				systemAuditTrail.setDeleted(false);
				
				systemAuditTrailRecordService.create(systemAuditTrail);
			}
		}
	}
	
	
	public SystemAuditTrailRecordService getSystemAuditTrailRecordService() {
		return systemAuditTrailRecordService;
	}
	public void setSystemAuditTrailRecordService(
			SystemAuditTrailRecordService systemAuditTrailRecordService) {
		this.systemAuditTrailRecordService = systemAuditTrailRecordService;
	}
}
