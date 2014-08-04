package com.beans.common.audit.service;

import java.sql.Date;
import java.util.List;

import com.beans.common.audit.model.SystemAuditTrail;

public interface SystemAuditTrailRecordService {

	 SystemAuditTrail create(SystemAuditTrail systemAuditTrail);
	 SystemAuditTrail delete(int id) throws Exception;
	 List<SystemAuditTrail> findAll();
	 SystemAuditTrail update(SystemAuditTrail systemAuditTrail) throws Exception;
	 SystemAuditTrail findById(int id) throws Exception;
	 List<SystemAuditTrail> findSelectedDates(Date date1,Date date2,String activity);
		
}

