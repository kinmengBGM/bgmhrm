package com.beans.common.audit.service;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beans.common.audit.model.SystemAuditTrail;
import com.beans.common.audit.repository.SystemAuditTrailRecordRepository;

@Service
public class SystemAuditTrailRecordServiceImpl implements SystemAuditTrailRecordService {

	@Resource
	SystemAuditTrailRecordRepository systemAuditTrailRecordRepository;
	
	
	
    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm");

	
	
	@Override
	@Transactional
	public SystemAuditTrail create(SystemAuditTrail systemAuditTrail) {
		SystemAuditTrail s = systemAuditTrail;
		return systemAuditTrailRecordRepository.save(s);
	}

	@Override
	@Transactional
	public SystemAuditTrail delete(int id) throws Exception {
		SystemAuditTrail ss = systemAuditTrailRecordRepository.findOne(id);
		if(ss == null) throw new Exception();
			ss.setDeleted(true);
			systemAuditTrailRecordRepository.save(ss);
			return ss;
	}

	@Override
	public List<SystemAuditTrail> findAll() {
		List<SystemAuditTrail> l =  systemAuditTrailRecordRepository.findByisDeleted(0);
		
		
		return l;
	}

	@Override
	public SystemAuditTrail update(SystemAuditTrail systemAuditTrail)
			throws Exception {
		SystemAuditTrail systemAuditTrailRecord =  systemAuditTrailRecordRepository.findOne(systemAuditTrail.getId());
		if(systemAuditTrailRecord == null)
			throw new Exception();
		systemAuditTrailRecord.setDate(systemAuditTrail.getDate());
		systemAuditTrailRecord.setActorUserId(systemAuditTrail.getActorUserId());
		systemAuditTrailRecord.setDescription(systemAuditTrail.getDescription());
		systemAuditTrailRecord.setDeleted(systemAuditTrail.isDeleted());
		systemAuditTrailRecordRepository.save(systemAuditTrailRecord);
		return systemAuditTrailRecord;
	}

	@Override
	public SystemAuditTrail findById(int id) throws Exception {
		SystemAuditTrail systemAuditTrail  = systemAuditTrailRecordRepository.findOne(id);
		if(systemAuditTrail == null)
			throw new Exception();
		return systemAuditTrail;
		
	}

	@Override
	public List<SystemAuditTrail> findSelectedDates(Date date1, Date date2,String activity) {
		
		// final dates format yyyy-mm-dd
		List<SystemAuditTrail> l = systemAuditTrailRecordRepository.findByDate(activity,date1,date2);
		System.out.println(l.size());
		return l;
		
		/*java.util.Date d1 = new java.util.Date();
		Date d1 = new Date("15-03-2013");
	 	Date d2 = new Date("16-03-2014");
		s.format(d1);  s.parse(source);
		s.format(d2);*/
		
	}
}

