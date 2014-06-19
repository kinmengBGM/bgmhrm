package com.beans.leaveapp.employeetype.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beans.exceptions.BSLException;
import com.beans.leaveapp.employeetype.model.EmployeeType;
import com.beans.leaveapp.employeetype.repository.EmployeeTypeRepository;

@Service
public class EmployeeTypeServiceImpl implements EmployeeTypeService {
	
	@Resource
	EmployeeTypeRepository employeeTypeRepository;
	private Logger log = Logger.getLogger(this.getClass());
	
	// This method is used to create new Employee Type and returns the saved entity.
	@Override
	@Transactional(rollbackFor = {Exception.class, BSLException.class})
	public EmployeeType create(EmployeeType employeeType) {
		try{
			EmployeeType employeeTypeToBeCreated = employeeType;
			employeeTypeToBeCreated = employeeTypeRepository.save(employeeTypeToBeCreated);
			if(log.isInfoEnabled() && employeeTypeToBeCreated != null)
			log.info("New employee type is saved in Database :"+employeeTypeToBeCreated.getName()); 
			return employeeTypeToBeCreated;
		}catch(Exception e){
			log.error("Error while creating new Employee Type :"+employeeType.getName(), e);
			throw new BSLException("err.emptype.create", e);
		}
	}
	// This method is used to delete the existing Employee Grade from database and returns the deleted entity
	@Override
	@Transactional(rollbackFor={Exception.class, BSLException.class})
	public EmployeeType delete(int id){
		try{
			EmployeeType employeeType = employeeTypeRepository.findOne(id);
			if(employeeType != null){
				employeeType.setDeleted(true);
				employeeTypeRepository.save(employeeType);
		}
			return employeeType;
		}catch(Exception e){
			log.error("Error while deleting existed Employee Type :"+id, e);
			throw new BSLException("err.emptype.delete", e);
		}
	}

	@Override
	public List<EmployeeType> findAll() {
		List<EmployeeType> resultList = employeeTypeRepository.findByisDeleted(0);
		return resultList;
	}
	// This method is used to update existing Employee Type and returns the updated entity.
	@Override
	@Transactional(rollbackFor={Exception.class, BSLException.class})
	public EmployeeType update(EmployeeType employeeType)  {
		try{
			EmployeeType employeeTypeToBeUpdated = employeeTypeRepository.findOne(employeeType.getId());
			if(employeeTypeToBeUpdated != null){
			employeeTypeToBeUpdated.setName(employeeType.getName());
			employeeTypeToBeUpdated.setDescription(employeeType.getDescription());
			employeeTypeToBeUpdated.setDeleted(employeeType.isDeleted());
			employeeTypeToBeUpdated.setLastModifiedBy(employeeType.getLastModifiedBy());
			employeeTypeToBeUpdated.setLastModifiedTime(new java.util.Date());
			employeeTypeRepository.save(employeeTypeToBeUpdated);
		}
			return employeeTypeToBeUpdated;
		}catch(Exception e){
			log.error("Error while updating existing Employee Type :"+employeeType.getName(), e);
			throw new BSLException("err.emptype.update", e);
		}
		
	}

	@Override
	public EmployeeType findById(int id) throws EmployeeTypeNotFound {
		EmployeeType employeeType = employeeTypeRepository.findOne(id);
		
		if(employeeType == null)
			throw new EmployeeTypeNotFound();
		
		return employeeType;
	}

}

