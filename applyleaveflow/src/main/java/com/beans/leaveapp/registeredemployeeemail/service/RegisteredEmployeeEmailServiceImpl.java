package com.beans.leaveapp.registeredemployeeemail.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.context.ApplicationContext;

import com.beans.exceptions.BSLException;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.model.RegisteredEmployee;
import com.beans.leaveapp.employee.service.EmployeeService;
import com.beans.leaveapp.jbpm6.util.ApplicationContextProvider;
import com.beans.util.email.EmailSender;
import com.beans.util.log.ApplLogger;

public class RegisteredEmployeeEmailServiceImpl implements
		RegisteredEmployeeEmailService {

	public void sendWelcomeEmail(RegisteredEmployee RegisteredEmployee) {
		try{
			// initializing variable
			String emailAddress = RegisteredEmployee.getPersonalEmailAddress();
			String empName = RegisteredEmployee.getFullname();
			String empUserName = RegisteredEmployee.getUsername();
			String empPassword = RegisteredEmployee.getPassword();
			String empPosition = RegisteredEmployee.getPosition();
			String empWorkEmail = RegisteredEmployee.getWorkEmailAddress();
			String empWorkPhone = RegisteredEmployee.getWorkPhoneNumber();
			String registrationDate = RegisteredEmployee.getRegistrationDate().toString();
	
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("emailtemplate/WelcomeEmail.html");
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	
			// read file into string
			String line = null;
	
			StringBuilder responseData = new StringBuilder();
			try {
				while ((line = bufferedReader.readLine()) != null) {
					responseData.append(line);
				}
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			// load your HTML email template
			String htmlEmailTemplate = responseData.toString();
	
			// replacing the employee details in HTML
			htmlEmailTemplate = htmlEmailTemplate.replace("##registeredDate##",
					registrationDate.toString());
			htmlEmailTemplate = htmlEmailTemplate.replace("##employeeName##",
					empName.toString());
			htmlEmailTemplate = htmlEmailTemplate.replace("##employeePassword##",
					empPassword.toString());
			htmlEmailTemplate = htmlEmailTemplate.replace("##employeeUserName##",
					empUserName.toString());
			htmlEmailTemplate = htmlEmailTemplate.replace("##employeePosition##",
					empPosition.toString());
			htmlEmailTemplate = htmlEmailTemplate.replace("##employeeWorkEmail##",
					empWorkEmail.toString());
			htmlEmailTemplate = htmlEmailTemplate.replace("##employeeWorkPhone##",
					empWorkPhone.toString());
	
			// create the email message
			HtmlEmail email = new HtmlEmail();
	
			/*List<String> emailList = new ArrayList<String>();
			emailList.add(emailAddress);
			emailList.add(empWorkEmail);*/
			try {
				//email.addTo(emailList.toArray(new String[emailList.size()]));
				email.addTo(emailAddress, empName);
				email.addTo(empWorkEmail, empName);
			} catch (EmailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			// set email subject
			email.setSubject("Welcome to Beans Group !");
	
			// set the html message
			try {
				email.setHtmlMsg(htmlEmailTemplate);
			} catch (EmailException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
	
			// set the alternative message
			// try {
			// email.setTextMsg("Your email client does not support HTML messages");
			// } catch (EmailException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
	
			// send the email
			try {
				EmailSender.sendEmail(email);
				ApplLogger.getLogger().info("Email successfully sent to "+ emailAddress + " and " + empWorkEmail);

			} catch (EmailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch(Exception e){
			throw new BSLException("err.leave.emailSendFailToApplicant");
		}

	}

	@Override
	public void sendRejectedEmail(RegisteredEmployee RegisteredEmployee) {
		// TODO Auto-generated method stub
		// initializing variable
		try{
		String emailAddress = RegisteredEmployee.getPersonalEmailAddress();
		String empName = RegisteredEmployee.getFullname();
		String rejectReason = RegisteredEmployee.getReason();
		String registrationDate = RegisteredEmployee.getRegistrationDate()
				.toString();

		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream("emailtemplate/RejectedEmail.html");
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		// read file into string
		String line = null;

		StringBuilder responseData = new StringBuilder();
		try {
			while ((line = bufferedReader.readLine()) != null) {
				responseData.append(line);
			}
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		// load your HTML email template
		String htmlEmailTemplate = responseData.toString();

		// replacing the employee details in HTML
		htmlEmailTemplate = htmlEmailTemplate.replace("##employeeName##",
				empName.toString());
		htmlEmailTemplate = htmlEmailTemplate.replace("##registeredDate##",
				registrationDate.toString());
		htmlEmailTemplate = htmlEmailTemplate.replace("##rejectReason##",
				rejectReason.toString());

		// create the email message
		HtmlEmail email = new HtmlEmail();

		// add recipient email address
		try {
			email.addTo(emailAddress, empName);
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// set email subject
		email.setSubject("Beans HRM Registration Rejected");

		// set the html message
		try {
			email.setHtmlMsg(htmlEmailTemplate);
		} catch (EmailException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// set the alternative message
		// try {
		// email.setTextMsg("Your email client does not support HTML messages");
		// } catch (EmailException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		// send the email
		try {
			EmailSender.sendEmail(email);
			ApplLogger.getLogger().info("Email successfully sent to "+ emailAddress);

		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}catch(Exception e){
			throw new BSLException("err.leave.emailSendFailToApplicant");
		}
	}

	@Override
	public void sendRegistrationEmailToHR(RegisteredEmployee RegisteredEmployee) {
		// TODO Auto-generated method stub
		try{
		// initializing variable
		String empName = RegisteredEmployee.getFullname();
		String empUserName = RegisteredEmployee.getUsername();
		String registrationDate = RegisteredEmployee.getRegistrationDate().toString();

		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream("emailtemplate/RegistrationEmail.html");
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		// read file into string
		String line = null;

		StringBuilder responseData = new StringBuilder();
		try {
			while ((line = bufferedReader.readLine()) != null) {
				responseData.append(line);
			}
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		// load your HTML email template
		String htmlEmailTemplate = responseData.toString();

		// replacing the employee details in HTML
		htmlEmailTemplate = htmlEmailTemplate.replace("##registeredDate##",
				registrationDate.toString());
		htmlEmailTemplate = htmlEmailTemplate.replace("##fullName##",
				empName.toString());
		htmlEmailTemplate = htmlEmailTemplate.replace("##username##",
				empUserName.toString());
		
		// create the email message
		HtmlEmail email = new HtmlEmail();

		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		EmployeeService employeeService = (EmployeeService) applicationContext.getBean("employeeService");
		
		//get all employees with role HR manager
		List<Employee> hrEmpolyeeList = employeeService.findAllEmployeesByRole("ROLE_HR_MANAGER");
		
		List<String> emailList = new ArrayList<String>();
		// add recipient email address
		for (Employee employee : hrEmpolyeeList) {
			emailList.add(employee.getWorkEmailAddress());
		}
		try {
			email.addTo( emailList.toArray(new String[emailList.size()]));
		} catch (EmailException e) {
			e.printStackTrace();
		}

		// set email subject
		email.setSubject("Reg: Registration Approval Required");

		// set the html message
		try {
			email.setHtmlMsg(htmlEmailTemplate);
		} catch (EmailException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// set the alternative message
		// try {
		// email.setTextMsg("Your email client does not support HTML messages");
		// } catch (EmailException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		// send the email
		try {
			EmailSender.sendEmail(email);
			for (Employee employee : hrEmpolyeeList)
				ApplLogger.getLogger().info("Email successfully sent to "+ employee.getWorkEmailAddress());
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}catch(Exception e){
			throw new BSLException("err.leave.emailSendFailToHR");
		}
	}

}
