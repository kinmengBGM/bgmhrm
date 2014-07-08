package com.beans.leaveapp.employee.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beans.common.audit.service.AuditTrail;
import com.beans.common.audit.service.SystemAuditTrailActivity;
import com.beans.common.audit.service.SystemAuditTrailLevel;
import com.beans.common.security.users.model.Users;
import com.beans.common.security.users.service.UsersNotFound;
import com.beans.common.security.users.service.UsersService;
import com.beans.leaveapp.address.model.Address;
import com.beans.leaveapp.address.service.AddressNotFound;
import com.beans.leaveapp.address.service.AddressService;
import com.beans.leaveapp.department.model.Department;
import com.beans.leaveapp.department.service.DepartmentNotFound;
import com.beans.leaveapp.department.service.DepartmentService;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.repository.EmployeeRepository;
import com.beans.leaveapp.employeegrade.model.EmployeeGrade;
import com.beans.leaveapp.employeegrade.service.EmployeeGradeNotFound;
import com.beans.leaveapp.employeegrade.service.EmployeeGradeService;
import com.beans.leaveapp.employeetype.model.EmployeeType;
import com.beans.leaveapp.employeetype.service.EmployeeTypeNotFound;
import com.beans.leaveapp.employeetype.service.EmployeeTypeService;
//import com.beans.util.log.ApplLogger;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);
	@Resource
	EmployeeRepository employeeRepository;
	
	AddressService addressService;
	DepartmentService departmentService;
	EmployeeGradeService employeeGradeService;
	EmployeeTypeService employeeTypeService;
	UsersService usersService;
	AuditTrail auditTrail;
	
	@Override
	@Transactional
	public Employee create(Employee employee) {
		Employee employeeToBeUpdated = employee;
		
		Employee createdEmployee = employeeRepository.save(employeeToBeUpdated);
		
		
		return createdEmployee;
	}

	@Override
	@Transactional(rollbackFor=EmployeeNotFound.class)
	public Employee delete(int id) throws EmployeeNotFound {
		Employee employee = employeeRepository.findOne(id);
		
		if(employee == null)
			throw new EmployeeNotFound();
		
		employee.setDeleted(true);
		
		return employeeRepository.save(employee);
	}

	@Override
	public List<Employee> findAll() {
		
		List<Employee> resultList = employeeRepository.findByisDeleted(0);
		return resultList;
	}
	
	
	
	@Override
	@Transactional(rollbackFor=EmployeeNotFound.class)
	public Employee update(Employee employee) throws EmployeeNotFound {
		
		Employee employeeToBeUpdated = employeeRepository.findOne(employee.getId());
		
		if(employeeToBeUpdated == null) {
			throw new EmployeeNotFound();
		}
		
		employeeToBeUpdated.setEmployeeNumber(employee.getEmployeeNumber());
		employeeToBeUpdated.setName(employee.getName());
		employeeToBeUpdated.setPosition(employee.getPosition());
		employeeToBeUpdated.setIdNumber(employee.getIdNumber());
		employeeToBeUpdated.setPassportNumber(employee.getPassportNumber());
		employeeToBeUpdated.setGender(employee.getGender());
		employeeToBeUpdated.setReligion(employee.getReligion());
		employeeToBeUpdated.setMaritalStatus(employee.getMaritalStatus());
		employeeToBeUpdated.setWorkEmailAddress(employee.getWorkEmailAddress());
		employeeToBeUpdated.setPersonalEmailAddress(employee.getPersonalEmailAddress());
		employeeToBeUpdated.setOfficePhone(employee.getOfficePhone());
		employeeToBeUpdated.setPersonalPhone(employee.getPersonalPhone());
		employeeToBeUpdated.setNationality(employee.getNationality());
		
		//TODO save users information
		
		employeeToBeUpdated.setEmployeeGrade(employee.getEmployeeGrade());
		employeeToBeUpdated.setDepartment(employee.getDepartment());
		employeeToBeUpdated.setEmployeeType(employee.getEmployeeType());
		employeeToBeUpdated.setJoinDate(employee.getJoinDate());
		employeeToBeUpdated.setResignationDate(employee.getResignationDate());
		employeeToBeUpdated.setDeleted(employee.isDeleted());
		employeeToBeUpdated.setResigned(employee.isResigned());
		employeeToBeUpdated.setLastModifiedBy(employee.getLastModifiedBy());
		employeeToBeUpdated.setLastModifiedTime(employee.getLastModifiedTime());
		return employeeRepository.save(employeeToBeUpdated);
	}

	@Override
	public Employee findById(int id) throws EmployeeNotFound {
		Employee employee = employeeRepository.findOne(id);
		
		if(employee == null)
			throw new EmployeeNotFound();
		
		return employee;
	}

	@Override
	@Transactional(rollbackFor={EmployeeGradeNotFound.class, DepartmentNotFound.class, EmployeeTypeNotFound.class})
	public Employee createEmployee(Employee employee, int employeeGradeId,
			int employeeTypeId, int departmentId, Users users, HashMap<Integer, Address> newAddressMap) {
		try {
			EmployeeGrade employeeGrade = employeeGradeService.findById(employeeGradeId);
			EmployeeType employeeType = employeeTypeService.findById(employeeTypeId);
			Department department = departmentService.findById(departmentId);
			
			employee.setEmployeeGrade(employeeGrade);
			employee.setEmployeeType(employeeType);
			employee.setDepartment(department);
			employee.setDeleted(false);
			employee.setResigned(false);			
			employee.setCreatedBy(employee.getCreatedBy());
			employee.setCreationTime(employee.getCreationTime());
			
			Employee newEmployee = create(employee);
			
			if(newAddressMap.size() > 0) {
				Iterator<Address> addressIterator = newAddressMap.values().iterator();
				while(addressIterator.hasNext()) {
					Address currentAddress = addressIterator.next();
					currentAddress.setId(0);
					currentAddress.setEmployee(newEmployee);
					addressService.create(currentAddress);
				}
				
			}
			
			if(users != null) {
				users.setCreatedBy(employee.getCreatedBy());
				users.setCreationTime(employee.getCreationTime());
				Users newUsers = usersService.registerUser(users);
				newEmployee.setUsers(newUsers);
				employeeRepository.save(newEmployee);
				
			}
			
			
		} catch(EmployeeGradeNotFound e) {
			System.out.println("Employee Grade not found: " + employeeGradeId);
			e.printStackTrace();
		} catch(EmployeeTypeNotFound e) {
			System.out.println("Employee Type not found: " + employeeTypeId);
			e.printStackTrace();
		} catch(DepartmentNotFound e) {
			
			System.out.println("Department not found: " + departmentId);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@Transactional(rollbackFor={EmployeeGradeNotFound.class, DepartmentNotFound.class, EmployeeTypeNotFound.class, AddressNotFound.class})
	public Employee updateEmployee(Employee employee, int employeeGradeId,
			int employeeTypeId, int departmentId, Users users, List<Address> existingAddressList, HashMap<Integer, Address> newAddressMap) {
		try {
			EmployeeGrade employeeGrade = employeeGradeService.findById(employeeGradeId);
			EmployeeType employeeType = employeeTypeService.findById(employeeTypeId);
			Department department = departmentService.findById(departmentId);
			
			employee.setEmployeeGrade(employeeGrade);
			employee.setEmployeeType(employeeType);
			employee.setDepartment(department);
			employee.setDeleted(false);
			employee.setResigned(false);
			employee.setLastModifiedBy(employee.getLastModifiedBy());
			employee.setLastModifiedTime(new java.util.Date());
			
			
			Employee newEmployee = update(employee);
			
			if(newAddressMap.size() > 0) {
				Iterator<Address> addressIterator = newAddressMap.values().iterator();
				while(addressIterator.hasNext()) {
					Address currentAddress = addressIterator.next();
					currentAddress.setId(0);
					currentAddress.setEmployee(newEmployee);
					currentAddress.setCreatedBy(employee.getLastModifiedBy());
					currentAddress.setCreationTime(new java.util.Date());
					addressService.create(currentAddress);
				}
				
			}
			
			if(existingAddressList != null) {
				if(existingAddressList.size() > 0) {
					Iterator<Address> existingAddressIterator = existingAddressList.iterator();
					while(existingAddressIterator.hasNext()) {
						Address currentAddress = existingAddressIterator.next();
						addressService.update(currentAddress);
					}
				}
			}
			if(users!=null) {
				users.setLastModifiedBy(employee.getLastModifiedBy());
				users.setLastModifiedTime(new java.util.Date());
				usersService.update(users);
				
			}
			
			
		} catch(EmployeeGradeNotFound e) {
			System.out.println("Employee Grade not found: " + employeeGradeId);
			e.printStackTrace();
		} catch(EmployeeTypeNotFound e) {
			System.out.println("Employee Type not found: " + employeeTypeId);
			e.printStackTrace();
		} catch(DepartmentNotFound e) {
			System.out.println("Department not found: " + departmentId);
			e.printStackTrace();
		} catch(EmployeeNotFound e) {
			System.out.println("Employee not found: " + employee.getId());
			e.printStackTrace();
		} catch(AddressNotFound e) {
			System.out.println("Address not found.");
			e.printStackTrace();
		}catch(UsersNotFound e) {
			System.out.println("Employee Grade not found: " + employeeGradeId);
			e.printStackTrace();
		} 
		
		return null;
	}
	
	@Override
	public Employee findByUsername(String username) throws EmployeeNotFound {
		Employee employee = employeeRepository.findByUsername(username);
		
		if(employee == null) {
			throw new EmployeeNotFound();
		}
		
		return employee;
	}
	
	

	@Override
	public List<Employee> findEmployeeByNameOrEmployeeNumberOrBoth(String name,
			String employeeNumber) {
		
		if(name == null || name.trim().equals("")) {
			String employeeNumberSearchTerm = "%" + employeeNumber + "%";
			return employeeRepository.findByEmployeeNumberLike(employeeNumberSearchTerm);
		} else if(employeeNumber == null || employeeNumber.trim().equals("")) {
			String nameSearchTerm = "%" + name + "%";
			return employeeRepository.findByNameLike(nameSearchTerm);
		} else {
			String employeeNumberSearchTerm = "%" + employeeNumber + "%";
			String nameSearchTerm = "%" + name + "%";
			return employeeRepository.findByNameAndEmployeeNumberLike(nameSearchTerm, employeeNumberSearchTerm);
		}
	}

	public DepartmentService getDepartmentService() {
		return departmentService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public EmployeeGradeService getEmployeeGradeService() {
		return employeeGradeService;
	}

	public void setEmployeeGradeService(EmployeeGradeService employeeGradeService) {
		this.employeeGradeService = employeeGradeService;
	}

	public EmployeeTypeService getEmployeeTypeService() {
		return employeeTypeService;
	}

	public void setEmployeeTypeService(EmployeeTypeService employeeTypeService) {
		this.employeeTypeService = employeeTypeService;
	}
	
	public AddressService getAddressService() {
		return addressService;
	}
	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}
	
	public UsersService getUsersService() {
		return usersService;
	}
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	
	public AuditTrail getAuditTrail() {
		return auditTrail;
	}
	public void setAuditTrail(AuditTrail auditTrail) {
		this.auditTrail = auditTrail;
	}

	@Override
	public Employee findByEmployee(String employeeName) {
		Employee employeeObj = employeeRepository.findByName(employeeName);
		return employeeObj;
	}	

	@Override
	public List<Employee> findAllEmployeesByRole(String role) {
		return employeeRepository.getAllUsersWithRole(role);
	}
	
	@Override
	public List<Employee> findByEmployeeTypePermAndCont() {
		List<Employee> permAndContEmployeeList = employeeRepository.findByEmployeeTypePermAndCont();
		return permAndContEmployeeList;
	}
	
	@Override
	public List<Employee> findByEmployeeTypePerm() {
		List<Employee> permEmployeeList = employeeRepository.findByEmployeeTypePerm();
		return permEmployeeList;
	}

	@Override
	public String getFullNameOfEmployee(String userName) {
		return employeeRepository.findFullNameByUsingUsername(userName);
	}

}
