package com.beans.leaveapp.leavetype.service;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.beans.exceptions.BSLException;
import com.beans.leaveapp.employeetype.model.EmployeeType;
import com.beans.leaveapp.employeetype.repository.EmployeeTypeRepository;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.beans.leaveapp.leavetype.repository.LeaveTypeRepository;

@Service
public class LeaveTypeServiceImpl implements LeaveTypeService {

	@Resource
	private LeaveTypeRepository leaveTypeRepository;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Resource
	private EmployeeTypeRepository employeeTypeRepository;
	
	@Override
	@Transactional
	public LeaveType create(LeaveType leaveType) {
		try{
		LeaveType leaveTypeToBeCreated = leaveType;
		return leaveTypeRepository.save(leaveTypeToBeCreated);
		} catch (Exception e) {
			log.error("Error while saving new Leave Type :"+leaveType.getName(), e);
			throw new BSLException("err.leavetype.create", e);
		}
	}

	@Override
	@Transactional(rollbackFor={BSLException.class})
	public LeaveType delete(int id) {
		try{
		LeaveType leaveType = leaveTypeRepository.findOne(id);
		if(leaveType!=null){
			leaveType.setDeleted(true);
			leaveTypeRepository.save(leaveType);
		}
		return leaveType;
		} catch (Exception e) {
			log.error("Error while deleting existed Leave Type :"+id, e);
			throw new BSLException("err.leavetype.delete", e);
		}
	}

	@Override
	public List<LeaveType> findAll() {
		List<LeaveType> resultList = leaveTypeRepository.findByisDeleted(0);
		List<LeaveType> list = new LinkedList<LeaveType>();
		/*for(LeaveType leaveType: resultList){
			//EmployeeType e = employeeTypeRepository.findOne();
			// String employeeTypeName = e.getName();
			leaveType.getEmployeeTypeId().getName();
		}
		*/
		return resultList;
	}

	@Override
	@Transactional(rollbackFor=BSLException.class)
	public LeaveType update(LeaveType leaveType) {
		try{
		LeaveType leaveTypeToBeUpdated = leaveTypeRepository.save(leaveType);
		return leaveTypeToBeUpdated;
		} catch (Exception e) {
			log.error("Error while saving new Leave Type :"+leaveType.getName(), e);
			throw new BSLException("err.leavetype.update", e);
		}
	}

	@Override
	public LeaveType findById(int id) throws LeaveTypeNotFound{
		LeaveType leaveType = leaveTypeRepository.findOne(id);
		
		if(leaveType == null)
			throw new LeaveTypeNotFound();
		
		return leaveType;
	}

	@Override
	public LeaveType findByLeaveType(String name,int id) throws LeaveTypeNotFound {
		LeaveType leaveTypeList =  leaveTypeRepository.findByName(name,id);
		return leaveTypeList;
	}

	@Override
	public List<String> findByName() {
		List<String> list = leaveTypeRepository.findNamesList();
		return list;
	}

	@Override
	public EmployeeType findByEmployeeName(String name) {
		EmployeeType employeeType =  employeeTypeRepository.findByName(name);
		return employeeType;
	}

	@Override
	public List<String> findByEmployeeTypes() {
		List<String> namesList = new LinkedList<String>();
		 try{
			  namesList = (List<String>)employeeTypeRepository.findByEmployeeNames();
	         return namesList;
		 }catch(Exception e){
			 e.printStackTrace();
		 }
			return namesList;
		}
	}
	

	

