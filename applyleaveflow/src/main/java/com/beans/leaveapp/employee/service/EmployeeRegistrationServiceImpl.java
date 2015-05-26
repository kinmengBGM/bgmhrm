package com.beans.leaveapp.employee.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beans.common.security.users.model.Users;
import com.beans.exceptions.BSLException;
import com.beans.leaveapp.address.model.Address;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.model.RegisteredEmployee;
import com.beans.leaveapp.employee.repository.RegisteredEmployeeRepository;
import com.beans.leaveapp.jbpm6.util.ApplicationContextProvider;
import com.beans.leaveapp.registeredemployeeemail.service.RegisteredEmployeeEmailService;
import com.beans.util.log.ApplLogger;

@Service
public class EmployeeRegistrationServiceImpl implements EmployeeRegistrationService {

	@Resource
	private RegisteredEmployeeRepository registeredEmployeeRepository;
		
	private RegisteredEmployee createRegisteredEmployee(Map<String, Object> contentMap) {
		RegisteredEmployee registeredEmployee = new RegisteredEmployee();
		registeredEmployee.setRegistrationStatus("PEND");
		
		registeredEmployee.setRegistrationDate(new Date());
		
		if(contentMap != null) {
			String fullname = (String) contentMap.get("fullname");
			registeredEmployee.setFullname(fullname);
			
			String username = (String) contentMap.get("username");
			registeredEmployee.setUsername(username);
			
			String password = (String) contentMap.get("password");
			registeredEmployee.setPassword(password);
			
			String passportNumber = (String) contentMap.get("passportNumber");
			registeredEmployee.setPassportNumber(passportNumber);
			
			String personalEmailAddress = (String) contentMap.get("personalEmailAddress");
			registeredEmployee.setPersonalEmailAddress(personalEmailAddress);
			
			String personalPhoneNumber = (String) contentMap.get("personalPhoneNumber");
			registeredEmployee.setPersonalPhoneNumber(personalPhoneNumber);
			
			String gender = (String) contentMap.get("gender");
			registeredEmployee.setGender(gender);
			
			String idNumber = (String) contentMap.get("idNumber");
			registeredEmployee.setIdNumber(idNumber);
			
			String maritalStatus = (String) contentMap.get("maritalStatus");
			registeredEmployee.setMaritalStatus(maritalStatus);
		}
		
		return registeredEmployee;
	}
	
	@Override
	@Transactional
	public void submitRegistration(HashMap<String, Object> parameterMap) throws BSLException{
		// TODO Auto-generated method stub
		RegisteredEmployee employee = createRegisteredEmployee(parameterMap);
		try{
			registeredEmployeeRepository.save(employee);
			sendRegistrationEmailToHR(employee);
			ApplLogger.getLogger().info("Successfully registered employee: "+ employee.getUsername());

		}catch(Exception e){
			ApplLogger.getLogger().info("Error registering employee: "+ employee.getUsername());
			throw new BSLException("error.registration.submit");
		}
	}

	@Override
	public List<RegisteredEmployee> getPendingRegisteredEmployee(String username) {
		List<RegisteredEmployee> resultList = new ArrayList<RegisteredEmployee>();
		resultList = registeredEmployeeRepository.findByStatus("PEND");
		return resultList;
	}
	
	@Override
	public void approveRegistration(RegisteredEmployee registeredEmployee,
			String actorId) {
		// TODO Auto-generated method stub

		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		EmployeeService employeeService = (EmployeeService) applicationContext.getBean("employeeService");
		
		Employee newEmployee = new Employee();
		newEmployee.setName(registeredEmployee.getFullname());
		newEmployee.setEmployeeNumber(registeredEmployee.getEmployeeNumber());
		newEmployee.setGender(registeredEmployee.getGender());
		newEmployee.setIdNumber(registeredEmployee.getIdNumber());
		newEmployee.setOfficePhone(registeredEmployee.getWorkPhoneNumber());
		newEmployee.setPassportNumber(registeredEmployee.getPassportNumber());
		newEmployee.setPersonalEmailAddress(registeredEmployee.getPersonalEmailAddress());
		newEmployee.setPersonalPhone(registeredEmployee.getPersonalPhoneNumber());
		newEmployee.setPosition(registeredEmployee.getPosition());
		newEmployee.setWorkEmailAddress(registeredEmployee.getWorkEmailAddress());
		newEmployee.setJoinDate(registeredEmployee.getJoinDate());
		newEmployee.setCreatedBy(newEmployee.getName());
		newEmployee.setCreationTime(new Date());
		newEmployee.setMaritalStatus(registeredEmployee.getMaritalStatus());
		Users user = new Users();
		user.setUsername(registeredEmployee.getUsername());
		user.setPassword(registeredEmployee.getPassword());
		user.setEnabled(true);
		user.setCreatedBy(newEmployee.getName());
		user.setCreationTime(new Date());
		try{
			registeredEmployee.setRegistrationStatus("APR");
			registeredEmployeeRepository.save(registeredEmployee);		
			employeeService.createEmployee(newEmployee, registeredEmployee.getEmployeeGradeId(), registeredEmployee.getEmployeeTypeId(), registeredEmployee.getDepartmentId(), user, new HashMap<Integer, Address>());
			sendWelcomeEmail(registeredEmployee);
			ApplLogger.getLogger().info("Successfully approved employee registration for "+ registeredEmployee.getUsername());
		}
		catch(Exception e){			
			ApplLogger.getLogger().info("Error approving employee registration for "+ registeredEmployee.getUsername());
			throw new BSLException("err.registration.approve");
		}
	}
	

	@Override
	public void rejectRegistration(RegisteredEmployee registeredEmployee,
			String actorId) {
		// TODO Auto-generated method stub
		try{
			registeredEmployee.setRegistrationStatus("REJ");
			registeredEmployeeRepository.save(registeredEmployee);	
			sendRejectionEmail(registeredEmployee);
			ApplLogger.getLogger().info("Successfully rejected employee registration for "+ registeredEmployee.getUsername());
			
		}catch(Exception e){
			ApplLogger.getLogger().info("Error rejecting employee registration for "+ registeredEmployee.getUsername());
			throw new BSLException("err.registration.reject");
		}
	}
	
	public static void sendRejectionEmail(RegisteredEmployee registeredEmployee) {
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/META-INF/spring-welcomeemail.xml");
		RegisteredEmployeeEmailService registeredEmployeeEmailService = (RegisteredEmployeeEmailService) applicationContext.getBean("registeredEmployeeEmailService");
		
		registeredEmployeeEmailService.sendRejectedEmail(registeredEmployee);
	}

	public static void sendWelcomeEmail(RegisteredEmployee registeredEmployee) {
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/META-INF/spring-welcomeemail.xml");
		RegisteredEmployeeEmailService registeredEmployeeEmailService = (RegisteredEmployeeEmailService) applicationContext.getBean("registeredEmployeeEmailService");
		
		registeredEmployeeEmailService.sendWelcomeEmail(registeredEmployee);
		
	}

	public static void sendRegistrationEmailToHR(RegisteredEmployee registeredEmployee) {
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/META-INF/spring-welcomeemail.xml");
		RegisteredEmployeeEmailService registeredEmployeeEmailService = (RegisteredEmployeeEmailService) applicationContext.getBean("registeredEmployeeEmailService");
		
		registeredEmployeeEmailService.sendRegistrationEmailToHR(registeredEmployee);
		
	}

	
}
