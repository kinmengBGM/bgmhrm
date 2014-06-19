package com.beans.common.audit.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.beans.common.audit.model.SystemAuditTrail;

public interface SystemAuditTrailRecordRepository extends CrudRepository<SystemAuditTrail, Integer>{

	
	 @Query("select l from SystemAuditTrail l where isDeleted = ?")
	 List<SystemAuditTrail> findByisDeleted(int x);
	 
	/* @Query("select l from SystemAuditTrail l where date = ?")
	  List<SystemAuditTrail> findByDate(Date x);*/
	
	 @Query("select l from SystemAuditTrail l where isDeleted = 0 and activity =? and actiondate between ? and  ? ")	 
	 List<SystemAuditTrail> findByDate(String activity,Date date,Date dates);

	
	
}

