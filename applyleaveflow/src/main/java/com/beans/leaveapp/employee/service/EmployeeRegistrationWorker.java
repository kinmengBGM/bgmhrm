package com.beans.leaveapp.employee.service;

import java.util.Date;
import java.util.HashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.beans.common.security.users.model.Users;
import com.beans.leaveapp.address.model.Address;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.model.RegisteredEmployee;
import com.beans.leaveapp.jbpm6.util.ApplicationContextProvider;
import com.beans.leaveapp.registeredemployeeemail.service.RegisteredEmployeeEmailService;

public class EmployeeRegistrationWorker {
	public static void registerEmployee(RegisteredEmployee registeredEmployee) {
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		EmployeeService employeeService = (EmployeeService) applicationContext.getBean("employeeService");
		
		Employee employee = new Employee();
		employee.setName(registeredEmployee.getFullname());
		employee.setEmployeeNumber(registeredEmployee.getEmployeeNumber());
		employee.setGender(registeredEmployee.getGender());
		employee.setIdNumber(registeredEmployee.getIdNumber());
		employee.setOfficePhone(registeredEmployee.getWorkPhoneNumber());
		employee.setPassportNumber(registeredEmployee.getPassportNumber());
		employee.setPersonalEmailAddress(registeredEmployee.getPersonalEmailAddress());
		employee.setPersonalPhone(registeredEmployee.getPersonalPhoneNumber());
		employee.setPosition(registeredEmployee.getPosition());
		employee.setWorkEmailAddress(registeredEmployee.getWorkEmailAddress());
		employee.setJoinDate(registeredEmployee.getJoinDate());
		employee.setCreatedBy(employee.getName());
		employee.setCreationTime(new Date());
		employee.setMaritalStatus(registeredEmployee.getMaritalStatus());
		Users user = new Users();
		user.setUsername(registeredEmployee.getUsername());
		user.setPassword(registeredEmployee.getPassword());
		user.setEnabled(true);
		user.setCreatedBy(employee.getName());
		user.setCreationTime(new Date());
		
		
		employeeService.createEmployee(employee, registeredEmployee.getEmployeeGradeId(), registeredEmployee.getEmployeeTypeId(), registeredEmployee.getDepartmentId(), user, new HashMap<Integer, Address>());
		
		sendWelcomeEmail(registeredEmployee);
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
	
}
