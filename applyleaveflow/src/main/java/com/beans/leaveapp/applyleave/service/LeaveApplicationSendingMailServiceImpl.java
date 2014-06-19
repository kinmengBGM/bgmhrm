package com.beans.leaveapp.applyleave.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.context.ApplicationContext;

import com.beans.exceptions.BSLException;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.service.EmployeeService;
import com.beans.leaveapp.jbpm6.util.ApplicationContextProvider;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.beans.util.email.EmailSender;
public class LeaveApplicationSendingMailServiceImpl {
	
	public void sendEmailNotificationToLeaveApplicant(LeaveTransaction leaveTransaction,String role,Boolean isTeamLeadApproved,Boolean isOperDirApproved,String teamLeadName, String oprDirName,int status) {
		try {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("LeaveMailTemplate.html");
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
			e3.printStackTrace();
		}
		// load your HTML email template
		String htmlEmailTemplate = responseData.toString();
		
		// create the email message
		HtmlEmail email = new HtmlEmail();

		// replacing the employee details in HTML
		htmlEmailTemplate = htmlEmailTemplate.replace("##employeeName##",leaveTransaction.getEmployee().getName());
		htmlEmailTemplate = htmlEmailTemplate.replace("##leaveType##",leaveTransaction.getLeaveType().getDescription());
		htmlEmailTemplate = htmlEmailTemplate.replace("##startDate##",leaveTransaction.fetchStartTimeStr());
		htmlEmailTemplate = htmlEmailTemplate.replace("##endDate##",leaveTransaction.fetchEndTimeStr());
		htmlEmailTemplate = htmlEmailTemplate.replace("##numberOfDays##",leaveTransaction.getNumberOfDays().toString());
		htmlEmailTemplate = htmlEmailTemplate.replace("##reason##",leaveTransaction.getReason());
		if(!"Unpaid".equalsIgnoreCase(leaveTransaction.getLeaveType().getName()))
				htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalance##",leaveTransaction.getYearlyLeaveBalance().toString());
		else 
			htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalance##","N/A for Unpaid Leave");
		
		if(status==1){
			htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","Hi <b>"+leaveTransaction.getEmployee().getName()+"</b>,  <br/> Details for your leave application are as shown below:");
			// set email subject
			email.setSubject("Reg : Leave Application acknowledgment");
		}
		else if(("ROLE_EMPLOYEE".equalsIgnoreCase(role) && isTeamLeadApproved==true && isOperDirApproved==true) || (!"ROLE_EMPLOYEE".equalsIgnoreCase(role) && isOperDirApproved==true)){
			if("ROLE_EMPLOYEE".equalsIgnoreCase(role))
				htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","Hi <b>"+leaveTransaction.getEmployee().getName()+"</b>,  <br/> Congratulations!!! Your leave has been approved by Team Lead <b>"+teamLeadName+"</b> and Director <b>"+oprDirName+"</b>. Following are the leave deatils you have applied for");
			else
				htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","Hi <br>"+leaveTransaction.getEmployee().getName()+"</b>,  <br/> Congratulations!!! Your leave has been approved by Director <b>"+oprDirName+"</b>. Following are the leave deatils you have applied for");
			// set email subject
			email.setSubject("Reg : Your Leave request has been approved");
		}
		else{
			if(status==4){
				htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","Hi <b>"+leaveTransaction.getEmployee().getName()+"</b>, <br/> Sorry!!! Your leave has been rejected by Director <b>"+oprDirName+"</b>. Following are the leave deatils you have applied for");
				// set email subject
				email.setSubject("Reg : Your Leave request has been rejected "+oprDirName);
			}
				else {
				htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","Hi <b>"+leaveTransaction.getEmployee().getName()+"</b>, <br/> Sorry!!! Your leave has been rejected by Team Lead <b>"+teamLeadName+"</b>. Following are the leave deatils you have applied for");
				// set email subject
				email.setSubject("Reg : Your Leave request has been rejected by "+teamLeadName);
			}
		}
		// add reciepiant email address
		try {
			if(leaveTransaction.getEmployee().getWorkEmailAddress()!=null && !leaveTransaction.getEmployee().getWorkEmailAddress().equals(""))
			email.addTo(leaveTransaction.getEmployee().getWorkEmailAddress(), leaveTransaction.getEmployee().getName());
		} catch (EmailException e) {
			e.printStackTrace();
		}
		// set the html message
		try {
			email.setHtmlMsg(htmlEmailTemplate);
		} catch (EmailException e2) {
			e2.printStackTrace();
		}

		// send the email
			EmailSender.sendEmail(email);
			System.out.println("Email has been sent successfully to Leave Applicant");
		
		}catch(Exception e){
			e.printStackTrace();
			throw new BSLException("err.leave.emailSendFailToApplicant");
		}

	}

	
public void sendEmailNotificationToLeaveApprover(LeaveTransaction leaveTransaction,String role,Boolean isTeamLeadApproved,Boolean isOperDirApproved,String teamLeadName, String oprDirName,int status) {
		try{
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("LeaveMailTemplate.html");
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
			e3.printStackTrace();
		}
		// load your HTML email template
		String htmlEmailTemplate = responseData.toString();

		// create the email message
		HtmlEmail email = new HtmlEmail();
				
		// replacing the employee details in HTML
		
		htmlEmailTemplate = htmlEmailTemplate.replace("##employeeName##",leaveTransaction.getEmployee().getName());
		htmlEmailTemplate = htmlEmailTemplate.replace("##leaveType##",leaveTransaction.getLeaveType().getDescription());
		htmlEmailTemplate = htmlEmailTemplate.replace("##startDate##",leaveTransaction.fetchStartTimeStr());
		htmlEmailTemplate = htmlEmailTemplate.replace("##endDate##",leaveTransaction.fetchEndTimeStr());
		htmlEmailTemplate = htmlEmailTemplate.replace("##numberOfDays##",leaveTransaction.getNumberOfDays().toString());
		htmlEmailTemplate = htmlEmailTemplate.replace("##reason##",leaveTransaction.getReason());
		htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalance##",leaveTransaction.getYearlyLeaveBalance().toString());
		if(!"Unpaid".equalsIgnoreCase(leaveTransaction.getLeaveType().getName()))
			htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalance##",leaveTransaction.getYearlyLeaveBalance().toString());
		else 
			htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalance##","N/A for Unpaid Leave");
		
		if(status==1){
			htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","Hi ,<br/><b>"+leaveTransaction.getEmployee().getName()+"</b> has applied for a leave and is pending for your approval.<br/> Details for leave application are as shown below");
			// set email subject
			email.setSubject("Reg : Reminder to take decision on the applied leave by "+leaveTransaction.getEmployee().getName());
		}
		else{
			htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","Hi ,<br/><b>"+leaveTransaction.getEmployee().getName()+"</b> has applied for a leave and is approved by <b>"+teamLeadName+"</b> and waiting for your final approval. So please take a decision on this leave request <br/> Details for leave application are as shown below");
			// set email subject
			email.setSubject("Reg : Reminder to take final decision on the applied leave by "+leaveTransaction.getEmployee().getName());
		}
		
		List<String> emailList = new ArrayList<String>();
		if(!isTeamLeadApproved){
		// Get all users with role ROLE_TEAMLEAD
		List<Employee> teamLeadEmpolyeeList = getEmployeeService().findAllEmployeesByRole("ROLE_TEAMLEAD");
				
		// add reciepiant email address
		for (Employee employee : teamLeadEmpolyeeList) {
			if(employee.getWorkEmailAddress()!=null && employee.getWorkEmailAddress()!="")
			emailList.add(employee.getWorkEmailAddress());
		}
		}
		else{
			// Get all users with role ROLE_OPERDIR
			List<Employee> operationalDirectorList = getEmployeeService().findAllEmployeesByRole("ROLE_OPERDIR");
					
			// add reciepiant email address
			for (Employee employee : operationalDirectorList) {
				if(employee.getWorkEmailAddress()!=null && employee.getWorkEmailAddress()!="")
				emailList.add(employee.getWorkEmailAddress());
			}
		}
		try {
			email.addTo(emailList.toArray(new String[emailList.size()]));
		} catch (EmailException e) {
			e.printStackTrace();
		}

		// set the html message
		try {
			email.setHtmlMsg(htmlEmailTemplate);
		} catch (EmailException e2) {
			e2.printStackTrace();
		}

		// send the email
		try {
			EmailSender.sendEmail(email);
			System.out.println("Email has been sent successfully to Leave Approver");
		} catch (EmailException e) {
			e.printStackTrace();
		}
		}catch(Exception e){
			e.printStackTrace();
			throw new BSLException("err.leave.emailSendFailToApprover");
		}
	}

public void sendEmailNotificationToHR(LeaveTransaction leaveTransaction,String role,Boolean isTeamLeadApproved,Boolean isOperDirApproved,String teamLeadName, String oprDirName,int status) {
	try{
	InputStream inputStream = getClass().getClassLoader().getResourceAsStream("LeaveMailTemplate.html");
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
		e3.printStackTrace();
	}
	// load your HTML email template
	String htmlEmailTemplate = responseData.toString();

	// create the email message
	HtmlEmail email = new HtmlEmail();
	
	// replacing the employee details in HTML
	htmlEmailTemplate = htmlEmailTemplate.replace("##employeeName##",leaveTransaction.getEmployee().getName());
	htmlEmailTemplate = htmlEmailTemplate.replace("##leaveType##",leaveTransaction.getLeaveType().getDescription());
	htmlEmailTemplate = htmlEmailTemplate.replace("##startDate##",leaveTransaction.fetchStartTimeStr());
	htmlEmailTemplate = htmlEmailTemplate.replace("##endDate##",leaveTransaction.fetchEndTimeStr());
	htmlEmailTemplate = htmlEmailTemplate.replace("##numberOfDays##",leaveTransaction.getNumberOfDays().toString());
	htmlEmailTemplate = htmlEmailTemplate.replace("##reason##",leaveTransaction.getReason());
	htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalance##",leaveTransaction.getYearlyLeaveBalance().toString());
	if(!"Unpaid".equalsIgnoreCase(leaveTransaction.getLeaveType().getName()))
		htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalance##",leaveTransaction.getYearlyLeaveBalance().toString());
	else 
		htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalance##","N/A for Unpaid Leave");
	
	
	if(status==3){
		htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","<br/> <b>"+leaveTransaction.getEmployee().getName()+"</b> has applied for a leave and is rejected by Team Lead <b>"+teamLeadName+"</b>.\n Details for leave application are as shown below");
		// set email subject
		email.setSubject("Reg : Leave request has been rejected by Team Lead "+teamLeadName);
	}
	else {
		if(isOperDirApproved){
		htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","<br/><b>"+leaveTransaction.getEmployee().getName()+"</b> has applied for a leave approve by Team Lead "+teamLeadName+" and is finally approved by <b>"+oprDirName+"</b>.<br/> Details for leave application are as shown below");
		// set email subject
		email.setSubject("Reg : Leave request has been approved by "+teamLeadName+" and "+oprDirName);
	}
	else{
		htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","<br/><b>"+leaveTransaction.getEmployee().getName()+"</b> has applied for a leave and is rejected by Operational Director <b>"+oprDirName+"</b>.<br/> Details for leave application are as shown below");
		// set email subject
		email.setSubject("Reg : Leave request has been rejected by Opearational Director "+oprDirName);
	}
	}
	// Get all users with role ROLE_TEAMLEAD
	List<Employee> hrEmpolyeeList = getEmployeeService().findAllEmployeesByRole("ROLE_HR");
	
	List<String> emailList = new ArrayList<String>();
	// add reciepiant email address
	for (Employee employee : hrEmpolyeeList) {
		emailList.add(employee.getWorkEmailAddress());
	}
	try {
		email.addTo( emailList.toArray(new String[emailList.size()]));
	} catch (EmailException e) {
		e.printStackTrace();
	}

	// set the html message
	try {
		email.setHtmlMsg(htmlEmailTemplate);
	} catch (EmailException e2) {
		e2.printStackTrace();
	}

	// send the email
	try {
		EmailSender.sendEmail(email);
		System.out.println("Email has been sent successfully to HR");
	} catch (EmailException e) {
		e.printStackTrace();
	}
	}catch(Exception e){
		e.printStackTrace();
		throw new BSLException("err.leave.emailSendFailToHR");
	}
}

	private EmployeeService getEmployeeService()
	{
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		return (EmployeeService) applicationContext.getBean("employeeService");
	}
	
	public static void main(String[] args){
		Employee employee = new Employee();
		employee.setName("Lakshminarayana R");
		employee.setWorkEmailAddress("lakshmin.ravuri@beans.com.my");
		LeaveType leaveType = new LeaveType();
		leaveType.setDescription("Annaul Leave");
		LeaveTransaction leave = new LeaveTransaction(3, new Date(), new Date(), new Date(), new Double(18.0), new Double(2.0), "Testing", "Waiting", leaveType, employee, false);
		LeaveApplicationSendingMailServiceImpl service = new LeaveApplicationSendingMailServiceImpl();
		//service.sendEmailNotificationToLeaveApplicant(leave, "ROLE_EMPLOYEE", false, false, 1);
		service.sendEmailNotificationToHR(leave, "ROLE_EMPLOYEE", false, false,null,null, 1);
	}
	
	

}