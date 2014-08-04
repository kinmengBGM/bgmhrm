package com.beans.leaveapp.welcomeemail.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.util.email.EmailSender;

public class WelcomeEmailServiceImpl implements WelcomeEmailService {

	public void sendHTMLEmail(Employee employee) {

		// initializing variable
		String emailAddress = employee.getPersonalEmailAddress();
		String empName = employee.getName();
		String empPosition = employee.getPosition();

		
		
		 InputStream inputStream = getClass().getClassLoader().getResourceAsStream("welcomeemailtemplate/WelcomeEmail.HTML");
         InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
         BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
         
         
		String line = null;

		StringBuilder responseData = new StringBuilder();
		try {
			while((line = bufferedReader.readLine()) != null) {
			    responseData.append(line);
			}
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		// load your HTML email template
				String htmlEmailTemplate = responseData.toString();
		
		// replacing the employee details in HTML
		htmlEmailTemplate = htmlEmailTemplate.replace("##employeeName##", empName.toString());
		htmlEmailTemplate = htmlEmailTemplate.replace("##employeeUserName##",employee.getUsers().getUsername());
		htmlEmailTemplate = htmlEmailTemplate.replace("##employeePosition##", empPosition.toString());
		
		// create the email message
		HtmlEmail email = new HtmlEmail();
		
		//add reciepiant email address
		try {
			email.addTo(emailAddress, empName);
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//set email subject
		email.setSubject("Welcome to Beans Group");

		// set the html message
		try {
			email.setHtmlMsg(htmlEmailTemplate);
		} catch (EmailException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// send the email
		try {
			EmailSender.sendEmail(email);
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
