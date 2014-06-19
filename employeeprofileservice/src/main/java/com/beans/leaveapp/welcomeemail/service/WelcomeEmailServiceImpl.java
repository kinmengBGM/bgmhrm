package com.beans.leaveapp.welcomeemail.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.ImageHtmlEmail;






import org.apache.log4j.chainsaw.Main;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.util.email.EmailSender;

public class WelcomeEmailServiceImpl implements WelcomeEmailService {

	public void sendHTMLEmail(Employee employee) {

		// initializing variable
		String emailAddress = employee.getPersonalEmailAddress();
		String empName = employee.getName();
		String empUserName = employee.getUsers().getUsername();
		String empPosition = employee.getPosition();

		
		
		 InputStream inputStream = getClass().getClassLoader().getResourceAsStream("welcomeemailtemplate/WelcomeEmail.HTML");
         InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
         BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
         
         
		//InputStream is = getClass().getResourceAsStream("welcomeemailtemplate/WelcomeEmail.xHTML");

		//BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("welcomeemailtemplate/WelcomeEmail.xHTML")));
		
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
		
		// reading the html file to String
//		try {
//			BufferedReader in = new BufferedReader(new FileReader());
//			String str;
//			while ((str = in.readLine()) != null) {
//				htmlEmailTemplate += str;
//			}
//			in.close();
//		} catch (IOException e) {
//		}

		// replacing the employee details in HTML
		//htmlEmailTemplate.replace("##employeeName##", empName.toString());
		htmlEmailTemplate.replace("##employeeUserName##", "abc");
		htmlEmailTemplate.replace("##employeePosition##", empPosition.toString());
		htmlEmailTemplate.replace("employeeName", "abc");
		
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

		// set the alternative message
//		try {
//			email.setTextMsg("Your email client does not support HTML messages");
//		} catch (EmailException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

		// send the email
		try {
			EmailSender.sendEmail(email);
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
