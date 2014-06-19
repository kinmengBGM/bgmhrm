package com.beans.leaveapp.department.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beans.exceptions.BSLException;
import com.beans.leaveapp.department.model.Department;
import com.beans.leaveapp.department.repository.DepartmentRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Resource
	DepartmentRepository departmentRepository;
	private Logger log = Logger.getLogger(this.getClass());
	
	@Override
	@Transactional(rollbackFor ={Exception.class,BSLException.class})
	public Department create(Department department) {
		try{ 
		Department departmentToBeCreated = department;
		log.info("New Department is saving into database with Name :"+departmentToBeCreated.getName());
		return departmentRepository.save(departmentToBeCreated);
		} catch (Exception e) {
			log.error("Error while saving new department :"+department.getName(), e);
			throw new BSLException("err.department.create", e);
		}
	}

	@Override
	@Transactional(rollbackFor=DepartmentNotFound.class)
	public Department delete(int id) {
		try{
		Department department = departmentRepository.findOne(id);
		if(department != null){
		department.setDeleted(true);
		departmentRepository.save(department);
		}
		return department;
		} catch (Exception e) {
			log.error("Error while saving new department :"+id, e);
			throw new BSLException("err.department.delete", e);
		}
	}

	@Override
	public List<Department> findAll() {
		List<Department> resultList = departmentRepository.findByisDeleted(0);
		
		
		return resultList;
	}

	@Override
	@Transactional(rollbackFor={Exception.class,BSLException.class})
	public Department update(Department department) {
		try{
			Department departmentToBeUpDepartment = departmentRepository.findOne(department.getId());
			if(departmentToBeUpDepartment != null){
				departmentToBeUpDepartment.setName(department.getName());
				departmentToBeUpDepartment.setDescription(department.getDescription());
				departmentToBeUpDepartment.setLastModifiedBy(department.getLastModifiedBy());
				departmentToBeUpDepartment.setLastModifiedTime(new java.util.Date());
				departmentToBeUpDepartment.setDeleted(department.isDeleted());
			}
			return departmentRepository.save(departmentToBeUpDepartment);
		} catch (Exception e) {
			log.error("Error while updating department :"+department.getName(), e);
			throw new BSLException("err.department.update", e);
		}
	}

	@Override
	public Department findById(int id) throws DepartmentNotFound {
		Department department = departmentRepository.findOne(id);
		
		if(department == null)
			throw new DepartmentNotFound();
		
		return department;
	}

}
