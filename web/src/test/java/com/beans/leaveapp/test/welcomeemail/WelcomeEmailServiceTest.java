package com.beans.leaveapp.test.welcomeemail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.beans.leaveapp.employee.model.RegisteredEmployee;
import com.beans.leaveapp.registeredemployeeemail.service.RegisteredEmployeeEmailService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/META-INF/spring-welcomeemail.xml")
public class WelcomeEmailServiceTest {

	ApplicationContext applicationContext;
	RegisteredEmployeeEmailService registeredEmployeeEmailService;
	
	@Before
	public void setup() {
		applicationContext = new ClassPathXmlApplicationContext("classpath:/META-INF/spring-welcomeemail.xml");
		registeredEmployeeEmailService = (RegisteredEmployeeEmailService) applicationContext.getBean("registeredEmployeeEmailService");
		
	}
	
	@Ignore("not required to run") @Test
	public void testWelcomeEmailSuccessful() {
		Date date = new Date();	
		RegisteredEmployee registeredEmployee = new RegisteredEmployee();
		registeredEmployee.setRegistrationDate(date);
		registeredEmployee.setFullname("Yeoh Seng Keat");
		registeredEmployee.setPassword("test1");
		registeredEmployee.setPosition("Junior Programmer");
		registeredEmployee.setPersonalEmailAddress("kira_yamato5645@hotmail.com");
		registeredEmployee.setWorkPhoneNumber("039056263");
		registeredEmployee.setWorkEmailAddress("sengkeat.yeoh@beans.com.my");
		registeredEmployee.setUsername("SengKeat");
		
		 	
		
		
		registeredEmployeeEmailService.sendWelcomeEmail(registeredEmployee);
	}
	
	@Ignore("not required to run") @Test
	public void testRejectedEmailSuccessful() {
		Date date = new Date();	
		RegisteredEmployee registeredEmployee = new RegisteredEmployee();
		registeredEmployee.setPersonalEmailAddress("kira_yamato5645@hotmail.com");
		registeredEmployee.setFullname("Yeoh Seng Keat");
		registeredEmployee.setRegistrationDate(date);
		registeredEmployee.setReason("your expected salary exceeded our expectations");
		
		
		
		
		
		registeredEmployeeEmailService.sendRejectedEmail(registeredEmployee);
	}
	
	
	
	@Test
	public void testNothing() {
		Assert.assertEquals(1, 1);
	}
}
