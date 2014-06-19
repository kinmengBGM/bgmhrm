package com.beans.leaveapp.employeegrade.service;



import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beans.exceptions.BSLException;
import com.beans.leaveapp.employeegrade.model.EmployeeGrade;
import com.beans.leaveapp.employeegrade.repository.EmployeeGradeRepository;

@Service
public class EmployeeGradeServiceImpl implements EmployeeGradeService {

	
	@Resource
	private EmployeeGradeRepository employeeGradeRepository;
	private Logger log = Logger.getLogger(this.getClass());
	
	// This method is used to create new Employee Grade and retuns the saved entity.
	@Override
	@Transactional
	public EmployeeGrade create(EmployeeGrade employeeGrade){
		EmployeeGrade  employeeGradeTableCreate = employeeGrade;
		try {
			employeeGradeTableCreate =  employeeGradeRepository.save(employeeGradeTableCreate);
		} catch (Exception e) {
			log.error("Error while saving new Employee Grade :"+employeeGrade.getName(), e);
			throw new BSLException("err.empgrade.create", e);
		}	
		return employeeGradeTableCreate;
	}

	// This method is used to delete existing Employee Grade entity in Database and retuns the deleted entity.
	@Override
	@Transactional
	public EmployeeGrade delete(int id){
		
		log.info("Employee Grade to be deleted is :"+id);
		try{
			EmployeeGrade employeeGrade = employeeGradeRepository.findOne(id);
			if(employeeGrade != null) {
				employeeGrade.setDeleted(true);
				employeeGradeRepository.save(employeeGrade);
				return employeeGrade;
			}
		}catch(Exception e)
		{
			log.error("Error while deleting Employee Grade :"+id, e);
			throw new BSLException("err.empgrade.delete", e);
		}
		return null;
	}

	@Override
	public List<EmployeeGrade> findAll()  {
		 List<EmployeeGrade> ref = employeeGradeRepository.findByisDeleted(0);
		 System.out.println("Size: " +ref.size());
		 return ref;
		
	}

	
	// This method is used to update existing Employee Grade entity in Database and retuns the updated entity.
	@Override
	@Transactional
	public EmployeeGrade update(EmployeeGrade employeeGrade) {
		try{
			EmployeeGrade employeeGradeToBeUpdated = employeeGradeRepository.findOne(employeeGrade.getId());
			if(employeeGradeToBeUpdated != null){
				employeeGradeToBeUpdated.setName(employeeGrade.getName());
				employeeGradeToBeUpdated.setDescription(employeeGrade.getDescription());
				employeeGradeToBeUpdated.setDeleted(employeeGrade.isDeleted());
				employeeGradeToBeUpdated.setLastModifiedBy(employeeGrade.getLastModifiedBy());
				employeeGradeToBeUpdated.setLastModifiedTime(new java.util.Date());
				employeeGradeRepository.save(employeeGradeToBeUpdated);
				log.info("update recode with username:" + employeeGradeToBeUpdated.getLastModifiedBy());
				return employeeGradeToBeUpdated;
				}
			}catch(Exception e){
				log.error("Error while updating existing Employee Grade :"+employeeGrade.getName(), e);
				throw new BSLException("err.empgrade.update", e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public EmployeeGrade findById(int id) {
		// TODO Auto-generated method stub
		EmployeeGrade employeeGrade = employeeGradeRepository.findOne(id);
		if(employeeGrade == null){	
			return null;
		}
		
		return employeeGrade;
	}
	

}
