package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.beans.common.audit.service.AuditTrail;
import com.beans.common.audit.service.SystemAuditTrailActivity;
import com.beans.common.audit.service.SystemAuditTrailLevel;
import com.beans.common.audit.service.SystemAuditTrailRecordService;
import com.custom.wrapper.audittrail.LogWrapper;

@RestController
@RequestMapping("/protected/auditTrail")
public class AuditTrailController {
	
	@Autowired
	AuditTrail auditTrail;
	
	@RequestMapping(value="/log", method=RequestMethod.POST)
	public void log(@RequestBody LogWrapper logWrapper) {
		auditTrail.log(logWrapper.getSystemAuditTrailActivity(), logWrapper.getSystemAuditTrailLevel(),logWrapper.getUserId() ,logWrapper.getUsername(),logWrapper.getDescription());
	}
	
	@RequestMapping(value="/getSystemAuditTrailRecordService", method=RequestMethod.GET)
	public SystemAuditTrailRecordService getSystemAuditTrailRecordService() {
		return auditTrail.getSystemAuditTrailRecordService();
	}
	
	@RequestMapping(value="/setSystemAuditTrailRecordService")
	@ResponseStatus(value = HttpStatus.OK)
	public void setSystemAuditTrailRecordService(SystemAuditTrailRecordService systemAuditTrailRecordService){
		auditTrail.setSystemAuditTrailRecordService(systemAuditTrailRecordService);
	}
}
