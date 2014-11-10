package com.beans.leaveapp.applyleave.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.context.ApplicationContext;

import com.beans.exceptions.BSLException;
import com.beans.leaveapp.applyleave.model.ApprovalLevelModel;
import com.beans.leaveapp.applyleave.model.TimeInLieuBean;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.service.EmployeeService;
import com.beans.leaveapp.jbpm6.util.ApplicationContextProvider;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.beans.util.email.EmailSender;
public class LeaveApplicationSendingMailServiceImpl {
	
	public void sendEmailNotificationToLeaveApplicant(LeaveTransaction leaveTransaction,TimeInLieuBean timeInLieuBean) {
		try {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("leaveApplicantMailTemplate.html");
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
		htmlEmailTemplate = htmlEmailTemplate.replaceAll("##employeeName##",leaveTransaction.getEmployee().getName());
		htmlEmailTemplate = htmlEmailTemplate.replace("##leaveType##",leaveTransaction.getLeaveType().getDescription());
		htmlEmailTemplate = htmlEmailTemplate.replace("##startDate##",leaveTransaction.fetchStartTimeStr());
		htmlEmailTemplate = htmlEmailTemplate.replace("##endDate##",leaveTransaction.fetchEndTimeStr());
		htmlEmailTemplate = htmlEmailTemplate.replace("##numberOfDays##",leaveTransaction.getNumberOfDays().toString()+checkAMorPM(leaveTransaction));
		htmlEmailTemplate = htmlEmailTemplate.replace("##reason##",leaveTransaction.getReason());
		if(!"Unpaid".equalsIgnoreCase(leaveTransaction.getLeaveType().getName()))
				htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalance##",leaveTransaction.getYearlyLeaveBalance().toString());
		else 
			htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalance##","N/A for Unpaid Leave");
		
		
		if(timeInLieuBean.getIsFirstApproverApproved()==null){
			htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##"," Details for your leave application are as shown below:");
			htmlEmailTemplate = htmlEmailTemplate.replace("##firstApplied##","<br/>You will receive another email notification when your application is  approved or rejected.");
			htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalanceLabel##","Current Yearly Balance");
			// set email subject
			email.setSubject("Reg : Leave Application acknowledgment");
		}
			else if(timeInLieuBean.getIsFirstApproverApproved()){
				if(timeInLieuBean.getIsSecondApproverApproved()==null){
				htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","Your leave application has been approved by <b>"+getEmployeeService().getFullNameOfEmployee(timeInLieuBean.getFirstApprover())+"</b><br/>");
				htmlEmailTemplate = htmlEmailTemplate.replace("##firstApplied##","");
				htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalanceLabel##","New Yearly Balance");
				// set email subject
				email.setSubject("Reg : Leave Application Approved");
				}
				else if(!timeInLieuBean.getIsSecondApproverApproved()){
					htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","Sorry but your leave application has been rejected by <b>"+getEmployeeService().getFullNameOfEmployee(timeInLieuBean.getSecondApprover())+"</b> due to <b>"+leaveTransaction.getRejectReason()+"</b>");
					htmlEmailTemplate = htmlEmailTemplate.replace("##firstApplied##","");
					htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalanceLabel##","New Yearly Balance");
					// set email subject
					email.setSubject("Reg : Leave Application Rejected");
				}
				else {
					htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","Your leave application has been approved by <b>"+getEmployeeService().getFullNameOfEmployee(timeInLieuBean.getFirstApprover())+"</b> and <b>"+getEmployeeService().getFullNameOfEmployee(timeInLieuBean.getSecondApprover())+"</b><br/>");
					htmlEmailTemplate = htmlEmailTemplate.replace("##firstApplied##","");
					htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalanceLabel##","New Yearly Balance");
					// set email subject
					email.setSubject("Reg : Leave Application Approved");
				}
		}
		else
			if(!timeInLieuBean.getIsFirstApproverApproved()){
				htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","Sorry but your leave application has been rejected by <b>"+getEmployeeService().getFullNameOfEmployee(timeInLieuBean.getFirstApprover())+"</b> due to <b>"+leaveTransaction.getRejectReason()+"</b>");
				htmlEmailTemplate = htmlEmailTemplate.replace("##firstApplied##","");
				htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalanceLabel##","New Yearly Balance");
				// set email subject
				email.setSubject("Reg : Leave Application Rejected");
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
			LeaveApplicationSendingMailServiceImpl.sendEMail(email);
			System.out.println("Email has been sent successfully to Leave Applicant");
		}catch(Exception e){
			e.printStackTrace();
			throw new BSLException("err.leave.emailSendFailToApplicant");
		}
	}

	
public void sendEmailNotificationToLeaveApprover(LeaveTransaction leaveTransaction,ApprovalLevelModel approvalBean,TimeInLieuBean timeInLieuBean) {
	try{
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("leaveApproverMailTemplate.html");
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
		htmlEmailTemplate = htmlEmailTemplate.replace("##numberOfDays##",leaveTransaction.getNumberOfDays().toString()+checkAMorPM(leaveTransaction));
		htmlEmailTemplate = htmlEmailTemplate.replace("##reason##",leaveTransaction.getReason());
		htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalanceLabel##"," Current Yearly Balance");
		if(!"Unpaid".equalsIgnoreCase(leaveTransaction.getLeaveType().getName()))
			htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalance##",leaveTransaction.getYearlyLeaveBalance().toString());
		else 
			htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalance##","N/A for Unpaid Leave");
		
		if(timeInLieuBean.getIsTwoLeveApproval()!=null)
			htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","<b>"+leaveTransaction.getEmployee().getName()+"</b> has applied for leave and is approved by <b>"+timeInLieuBean.getFirstApprover()+"</b> and is pending for your final approval at <a href='http://beans-my.dyndns.biz:8182/hrm/protected/applyleave/approveleavetasklist.jsf?id="+leaveTransaction.getId()+"'>HRM Application</a>");
		else
			htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","<b>"+leaveTransaction.getEmployee().getName()+"</b> has applied for leave and is pending for your approval at <a href='http://beans-my.dyndns.biz:8182/hrm/protected/applyleave/approveleavetasklist.jsf?id="+leaveTransaction.getId()+"'>HRM Application</a>");

			// set email subject
			email.setSubject("Reg : Leave Approval Required For "+leaveTransaction.getEmployee().getName());
		
		List<String> emailList = new ArrayList<String>();
		// Get all users with role who can approve the leave, For Time-in-Lieu case, ROLE_OPERDIR will have to get mail at 2nd stage
		List<Employee> approverList= new ArrayList<Employee>();
		if(timeInLieuBean.getIsTwoLeveApproval()!=null)
			approverList = getEmployeeService().findAllEmployeesByRole("ROLE_OPERDIR");
		else
			approverList = getEmployeeService().findAllEmployeesByRole(approvalBean.getApprover());
		// add reciepiant email address
		for (Employee employee : approverList) {
			if(employee.getWorkEmailAddress()!=null && employee.getWorkEmailAddress()!="")
			emailList.add(employee.getWorkEmailAddress());
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
			LeaveApplicationSendingMailServiceImpl.sendEMail(email);
			System.out.println("Email has been sent successfully to Leave Approver");
		}catch(Exception e){
			e.printStackTrace();
			throw new BSLException("err.leave.emailSendFailToApprover");
		}
	}

public void sendEmailNotificationToHR(LeaveTransaction leaveTransaction,TimeInLieuBean timeInLieuBean) {
	try{
	InputStream inputStream = getClass().getClassLoader().getResourceAsStream("leaveApproverMailTemplate.html");
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
	htmlEmailTemplate = htmlEmailTemplate.replace("##numberOfDays##",leaveTransaction.getNumberOfDays().toString()+checkAMorPM(leaveTransaction));
	htmlEmailTemplate = htmlEmailTemplate.replace("##reason##",leaveTransaction.getReason());
	htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalanceLabel##","New Yearly Balance");
	if(!"Unpaid".equalsIgnoreCase(leaveTransaction.getLeaveType().getName()))
		htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalance##",leaveTransaction.getYearlyLeaveBalance().toString());
	else 
		htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalance##","N/A for Unpaid Leave");
	
	
	if(!timeInLieuBean.getIsFirstApproverApproved()){
		htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","<br/><b>"+leaveTransaction.getEmployee().getName()+"</b> leave application has been rejected by <b>"+getEmployeeService().getFullNameOfEmployee(timeInLieuBean.getFirstApprover())+"</b> due to <b>"+leaveTransaction.getRejectReason()+"</b> <br/> Details for your rejected leave application are shown below:");
		// set email subject
		email.setSubject("Reg : Leave Application Rejected for "+leaveTransaction.getEmployee().getName());
		
	}
	else {
		if(timeInLieuBean.getIsSecondApproverApproved()==null){
		htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","<b>"+leaveTransaction.getEmployee().getName()+"</b> leave application has been approved by <b>"+getEmployeeService().getFullNameOfEmployee(timeInLieuBean.getFirstApprover())+"</b>.<br/> Details for the approved leave application are shown below:<br/>");
		// set email subject
		email.setSubject("Reg : Leave Application Approved for "+leaveTransaction.getEmployee().getName());
		}else if(!timeInLieuBean.getIsSecondApproverApproved()){
			htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","<br/><b>"+leaveTransaction.getEmployee().getName()+"</b> leave application has been rejected by <b>"+getEmployeeService().getFullNameOfEmployee(timeInLieuBean.getSecondApprover())+"</b> due to <b>"+leaveTransaction.getRejectReason()+"</b> <br/> Details for your rejected leave application are shown below:");
			// set email subject
			email.setSubject("Reg : Leave Application Rejected for "+leaveTransaction.getEmployee().getName());
		}
		else{
			htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","<b>"+leaveTransaction.getEmployee().getName()+"</b> leave application has been approved by <b>"+getEmployeeService().getFullNameOfEmployee(timeInLieuBean.getFirstApprover())+"</b> and  <b>"+getEmployeeService().getFullNameOfEmployee(timeInLieuBean.getSecondApprover())+"</b>.<br/> Details for the approved leave application are shown below:<br/>");
			// set email subject
			email.setSubject("Reg : Leave Application Approved for "+leaveTransaction.getEmployee().getName());
		}
	}
	// Get all users with role ROLE_HR
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
		LeaveApplicationSendingMailServiceImpl.sendEMail(email);
		System.out.println("Email has been sent successfully to HR");
	}catch(Exception e){
		e.printStackTrace();
		throw new BSLException("err.leave.emailSendFailToHR");
	}
}

public void sendEmailNotificationForCancelLeave(LeaveTransaction leaveTransaction,String hrName) {
	try {
	InputStream inputStream = getClass().getClassLoader().getResourceAsStream("leaveCancelMailTemplate.html");
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
	htmlEmailTemplate = htmlEmailTemplate.replaceAll("##employeeName##",leaveTransaction.getEmployee().getName());
	htmlEmailTemplate = htmlEmailTemplate.replace("##leaveType##",leaveTransaction.getLeaveType().getDescription());
	htmlEmailTemplate = htmlEmailTemplate.replace("##startDate##",leaveTransaction.fetchStartTimeStr());
	htmlEmailTemplate = htmlEmailTemplate.replace("##endDate##",leaveTransaction.fetchEndTimeStr());
	htmlEmailTemplate = htmlEmailTemplate.replace("##numberOfDays##",leaveTransaction.getNumberOfDays().toString()+checkAMorPM(leaveTransaction));
	htmlEmailTemplate = htmlEmailTemplate.replace("##reason##",leaveTransaction.getReason());
	if(!"Unpaid".equalsIgnoreCase(leaveTransaction.getLeaveType().getName()))
			htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalance##",leaveTransaction.getYearlyLeaveBalance().toString());
	else 
		htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalance##","N/A for Unpaid Leave");
	
	
		htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","<br/> Leave is cancelled by <b>"+getEmployeeService().getFullNameOfEmployee(hrName)+"</b> due to <b>"+leaveTransaction.getRejectReason()+"</b>. Details of cancelled leave application are as shown below:");
		// set email subject
		email.setSubject("Reg : Leave Application Cancelled");
	// add reciepiant email address
	try {
		if(leaveTransaction.getEmployee().getWorkEmailAddress()!=null && !leaveTransaction.getEmployee().getWorkEmailAddress().equals(""))
		email.addTo(leaveTransaction.getEmployee().getWorkEmailAddress(), leaveTransaction.getEmployee().getName());
		
	} catch (EmailException e) {
		e.printStackTrace();
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
		LeaveApplicationSendingMailServiceImpl.sendEMail(email);
		System.out.println("Email has been sent successfully to Leave Applicant and HRs");
	}catch(Exception e){
		e.printStackTrace();
		throw new BSLException("err.leave.emailSendFailToApplicant");
	}
}

	public void sendLeaveTerminateMailToApplicant(String leaveApplicant,String emailId){
		try{
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("leaveTerminationMailTemplate.html");
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
			
			htmlEmailTemplate = htmlEmailTemplate.replace("##employeeName##", leaveApplicant);
			// set email subject
			email.setSubject("Reg : Leave Application Terminated, Please apply again");
			// set email TO
			email.addTo(emailId);
			// set the html message
			try {
				email.setHtmlMsg(htmlEmailTemplate);
			} catch (EmailException e2) {
				e2.printStackTrace();
			}
			// send the email
				LeaveApplicationSendingMailServiceImpl.sendEMail(email);
				System.out.println("Email has been sent successfully to Leave Applicant ");
						
		}catch(Exception e){
			
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
		employee.setWorkEmailAddress("rlnarayana4java@gmail.com");
		LeaveType leaveType = new LeaveType();
		leaveType.setDescription("Annaul Leave");
		ApprovalLevelModel approvalBean = new ApprovalLevelModel();
		approvalBean.setApprover("ROLE_TEAMLEAD");
		LeaveTransaction leave = new LeaveTransaction(3, new Date(), new Date(), new Date(), new Double(18.0), new Double(2.0), "Testing", "Waiting", leaveType, employee, false);
		LeaveApplicationSendingMailServiceImpl service = new LeaveApplicationSendingMailServiceImpl();
		service.sendEmailNotificationToLeaveApplicant(leave,null);
		service.sendEmailNotificationToLeaveApprover(leave, approvalBean,new TimeInLieuBean());
		service.sendEmailNotificationToHR(leave, null);
	}

	private static void  sendEMail(final HtmlEmail email){
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new Runnable() {
		    public void run() {
		        try {
					EmailSender.sendEmail(email);
				} catch (EmailException e) {
					e.printStackTrace();
				}
		    }
		});
		executorService.shutdown();
	}
	
	private static String checkAMorPM(LeaveTransaction leaveTransaction){
		if(leaveTransaction.getNumberOfDays()==0.5 || leaveTransaction.getNumberOfDays()==0.50 || leaveTransaction.getNumberOfDays()==0.500){
			if("AM".equalsIgnoreCase(leaveTransaction.getTimings()))
				return " (Morning)";
			else
				return " (Afternoon)";
		}
		return "";
	}
	
	
}