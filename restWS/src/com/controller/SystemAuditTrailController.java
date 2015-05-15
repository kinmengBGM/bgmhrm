package com.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beans.common.audit.model.SystemAuditTrail;
import com.beans.common.audit.service.SystemAuditTrailRecordService;

@RestController
@RequestMapping("/protected/systemAuditTrail")
public class SystemAuditTrailController {

	@Autowired
	SystemAuditTrailRecordService systemAuditTrailRecordService;
	
	@RequestMapping(value="/create", method = RequestMethod.POST)
	public SystemAuditTrail create(@RequestBody SystemAuditTrail systemAuditTrail){
		return systemAuditTrailRecordService.create(systemAuditTrail);
	 }
	
	@RequestMapping(value="/delete", method = RequestMethod.GET)
	public SystemAuditTrail delete(@RequestParam int id) throws Exception{
		return systemAuditTrailRecordService.delete(id);
		
	}
	
	@RequestMapping("findAll")
	public List<SystemAuditTrail> findAll(){
		return systemAuditTrailRecordService.findAll();
	}

	@RequestMapping(value="/update", method = RequestMethod.POST)
	public SystemAuditTrail update(@RequestBody SystemAuditTrail systemAuditTrail) throws Exception{
		return systemAuditTrailRecordService.update(systemAuditTrail);
	}
	
	@RequestMapping(value="/findById", method = RequestMethod.GET)
	public SystemAuditTrail findById(@RequestParam int id) throws Exception{
		return systemAuditTrailRecordService.findById(id);
	}
	
	@RequestMapping(value="/findSelectedDates", method = RequestMethod.GET)
	public List<SystemAuditTrail> findSelectedDates(@RequestParam Date date1,@RequestParam Date date2,@RequestParam String activity){
		return systemAuditTrailRecordService.findSelectedDates(date1,date2,activity);
	}
}
