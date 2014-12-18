package com.beans.leaveflow.service.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.context.ApplicationContext;

import com.beans.common.leave.rules.model.LeaveFlowDecisionsTaken;
import com.beans.common.leave.rules.model.LeaveRuleBean;
import com.beans.exceptions.BSLException;
import com.beans.leaveapp.applyleave.service.LeaveApplicationSendingMailServiceImpl;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.service.EmployeeNotFound;
import com.beans.leaveapp.employee.service.EmployeeService;
import com.beans.leaveapp.jbpm6.util.ApplicationContextProvider;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.beans.util.email.EmailSender;
import com.beans.util.enums.Leave;
import com.beans.util.log.ApplLogger;

public class EmailNotificationPreparingServiceImpl {
	
	private static final String YES = "YES";
	private static final String NO = "NO";

	
	public void sendEmailNotificationToLeaveApplicant(LeaveTransaction leaveTransaction) {
		try {
			String htmlEmailTemplate = getHTMLEmailStreamObject("leaveApplicantMailTemplate.html");
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
		
			// This is for leave acknowledgement, where no leave is approved.
			if(leaveTransaction.getDecisionsBean().getDecisionLevel1()==null){
				htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##"," Details for your leave application are as shown below:");
				htmlEmailTemplate = htmlEmailTemplate.replace("##firstApplied##","<br/>You will receive another email notification when your application is  approved or rejected.");
				htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalanceLabel##","Current Yearly Balance");
				// set email subject
				email.setSubject("Reg : Leave Application acknowledgment");
			}
			else{
				String decision =	getCurrentLevelDecisionByApprover(leaveTransaction);
				if(YES.equalsIgnoreCase(decision)){
					htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","Your leave application has been approved by <b>"+getAllTheApproverNames(leaveTransaction)+"</b><br/>");
					htmlEmailTemplate = htmlEmailTemplate.replace("##firstApplied##","");
					htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalanceLabel##","New Yearly Balance");
					// set email subject
					email.setSubject("Reg : Leave Application Approved");
				}
				else{
					htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","Sorry but your leave application has been rejected by <b>"+getRejectedDecisionTakenUser(leaveTransaction)+"</b> due to <b>"+leaveTransaction.getRejectReason()+"</b>");
					htmlEmailTemplate = htmlEmailTemplate.replace("##firstApplied##","");
					htmlEmailTemplate = htmlEmailTemplate.replace("##yearlyBalanceLabel##","New Yearly Balance");
					// set email subject
					email.setSubject("Reg : Leave Application Rejected");
				}
				}
		// Adding emails and setting message body to the email
		email = prepareSendingEmail(leaveTransaction, email, htmlEmailTemplate);
		// send the email
			sendEMail(email);
			ApplLogger.getLogger().info("Email has been sent successfully to Leave Applicant :"+leaveTransaction.getEmployee().getName());
		}catch(Exception e){
			e.printStackTrace();
			throw new BSLException("err.leave.emailSendFailToApplicant");
		}
	}
	
	public void sendEmailNotificationToLeaveApprover(LeaveTransaction leaveTransaction) {
		try{
			// load your HTML email template
			String htmlEmailTemplate = getHTMLEmailStreamObject("leaveApproverMailTemplate.html");
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
			if(getTotalNumberofLevelsApproved(leaveTransaction)==0)
				htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","<b>"+leaveTransaction.getEmployee().getName()+"</b> has applied for leave and is pending for your approval at <a href='http://beans-my.dyndns.biz:8182/hrm/protected/applyleave/approveleavetasklist.jsf?id="+leaveTransaction.getId()+"'>HRM Application</a>");
			else{
				htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","<b>"+leaveTransaction.getEmployee().getName()+"</b> has applied for leave and is approved by <b>"+getAllTheApproverNames(leaveTransaction)+"</b> and is pending for your (final) approval at <a href='http://beans-my.dyndns.biz:8182/hrm/protected/applyleave/approveleavetasklist.jsf?id="+leaveTransaction.getId()+"'>HRM Application</a>");
			}
			// set email subject
			email.setSubject("Reg : Leave Approval Required For "+leaveTransaction.getEmployee().getName());

			List<String> emailList = new ArrayList<String>();
			List<Employee> approverList= new ArrayList<Employee>();
			String role = getApproverRoleForTheGivenLevel(leaveTransaction,getTotalNumberofLevelsApproved(leaveTransaction));
			//approverList = getMockApproverList(role); Use this for mock
			approverList = getEmployeeService().findAllEmployeesByRole(role);
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
			sendEMail(email);
			ApplLogger.getLogger().info("Email has been sent successfully to Leave Approver :"+emailList);
			}catch(Exception e){
				e.printStackTrace();
				throw new BSLException("err.leave.emailSendFailToApprover");
			}
		}
	

	public void sendEmailNotificationToHR(LeaveTransaction leaveTransaction) {
		try{
		// load your HTML email template
		String htmlEmailTemplate = getHTMLEmailStreamObject("leaveApproverMailTemplate.html");
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
		
		String rejectedUser = getRejectedDecisionTakenUser(leaveTransaction);
		
		if(rejectedUser!=null){
			htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","<br/><b>"+leaveTransaction.getEmployee().getName()+"</b> leave application has been rejected by <b>"+rejectedUser+"</b> due to <b>"+leaveTransaction.getRejectReason()+"</b> <br/> Details for your rejected leave application are shown below:");
			// set email subject
			email.setSubject("Reg : Leave Application Rejected for "+leaveTransaction.getEmployee().getName());
		}
		else {
			htmlEmailTemplate = htmlEmailTemplate.replace("##mainMessage##","<b>"+leaveTransaction.getEmployee().getName()+"</b> leave application has been approved by <b>"+getAllTheApproverNames(leaveTransaction)+"</b>.<br/> Details for the approved leave application are shown below:<br/>");
			// set email subject
			email.setSubject("Reg : Leave Application Approved for "+leaveTransaction.getEmployee().getName());
			}
		// Get all users with role ROLE_HR
		List<Employee> hrEmpolyeeList = getEmployeeService().findAllEmployeesByRole("ROLE_HR_MANAGER");
		
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
		sendEMail(email);
		ApplLogger.getLogger().info("Email has been sent successfully to HR : "+emailList);
		}catch(Exception e){
			e.printStackTrace();
			throw new BSLException("err.leave.emailSendFailToHR");
		}

	}
	
	public void sendEmailNotificationForCancelLeave(LeaveTransaction leaveTransaction,String hrName) {
		try {
		// load your HTML email template
		String htmlEmailTemplate = getHTMLEmailStreamObject("leaveCancelMailTemplate.html");
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
		List<Employee> hrEmpolyeeList = getEmployeeService().findAllEmployeesByRole("ROLE_HR_MANAGER");
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
			sendEMail(email);
			ApplLogger.getLogger().info("Email has been sent successfully to Leave Applicant and HRs");
		}catch(Exception e){
			e.printStackTrace();
			throw new BSLException("err.leave.emailSendFailToApplicant");
		}
	}

	
	
	
	
	
	
	
	private HtmlEmail prepareSendingEmail(LeaveTransaction leaveTransaction,HtmlEmail email,String htmlEmailTemplate){
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
		return email;
	}

	private String getHTMLEmailStreamObject(String fileName){
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
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
		return responseData.toString();
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
	
		
	// This method finds the total number of levels required for the leave approval.
	int getTotalLevelsOfApprovalRequired(LeaveTransaction leaveTransaction){
		LeaveRuleBean ruleBean = leaveTransaction.getLeaveRuleBean();
		int count =0;
		if(ruleBean!=null && StringUtils.trimToNull(ruleBean.getApproverNameLevel1())!=null){
					++count;
					if(StringUtils.trimToNull(ruleBean.getApproverNameLevel2())!=null){
						++count;
						if(StringUtils.trimToNull(ruleBean.getApproverNameLevel3())!=null){
							++count;
							if(StringUtils.trimToNull(ruleBean.getApproverNameLevel4())!=null){
								++count;
								if(StringUtils.trimToNull(ruleBean.getApproverNameLevel5())!=null){
									++count;
								}
							}
						}
					}
		}
		return count;
	}
	
	// This method will return the number of levels the application is approved.
	int getTotalNumberofLevelsApproved(LeaveTransaction leaveTransaction){
		LeaveFlowDecisionsTaken decisionsTaken = leaveTransaction.getDecisionsBean();
		int count =0;
		if(decisionsTaken!=null && decisionsTaken.getDecisionLevel1()!=null && YES.equalsIgnoreCase(decisionsTaken.getDecisionLevel1())){
			++count;
			if(decisionsTaken.getDecisionLevel2()!=null && YES.equalsIgnoreCase(decisionsTaken.getDecisionLevel2())){
				++count;
				if(decisionsTaken.getDecisionLevel3()!=null && YES.equalsIgnoreCase(decisionsTaken.getDecisionLevel3())){
					++count;
					if(decisionsTaken.getDecisionLevel4()!=null && YES.equalsIgnoreCase(decisionsTaken.getDecisionLevel4())){
						++count;
						if(decisionsTaken.getDecisionLevel5()!=null && YES.equalsIgnoreCase(decisionsTaken.getDecisionLevel5())){
							++count;
						}
					}
				}
			}
		}
		
		return count;
	}
	
	
	// This method will return the number of levels the decision had taken
	int getTotalNumberofLevelsDecisionTaken(LeaveTransaction leaveTransaction){
		LeaveFlowDecisionsTaken decisionsTaken = leaveTransaction.getDecisionsBean();
		int count =0;
		if(decisionsTaken!=null && decisionsTaken.getDecisionLevel1()!=null){
			++count;
			if(decisionsTaken.getDecisionLevel2()!=null){
				++count;
				if(decisionsTaken.getDecisionLevel3()!=null){
					++count;
					if(decisionsTaken.getDecisionLevel4()!=null){
						++count;
						if(decisionsTaken.getDecisionLevel5()!=null){
							++count;
						}
					}
				}
			}
		}
		
		return count;
	}
	
	
	
	
	// This method will return the current level decision taken by Approver.
	String getCurrentLevelDecisionByApprover(LeaveTransaction leaveTransaction){
		LeaveFlowDecisionsTaken decisionsTaken = leaveTransaction.getDecisionsBean();
		if(decisionsTaken!=null && decisionsTaken.getDecisionLevel1()!=null){
			if(StringUtils.trimToNull(decisionsTaken.getDecisionLevel2())==null)
				return decisionsTaken.getDecisionLevel1();
			else if(StringUtils.trimToNull(decisionsTaken.getDecisionLevel3())==null)
				return decisionsTaken.getDecisionLevel2();
			else if(StringUtils.trimToNull(decisionsTaken.getDecisionLevel4())==null)
				return decisionsTaken.getDecisionLevel3();
			else if(StringUtils.trimToNull(decisionsTaken.getDecisionLevel5())==null)
				return decisionsTaken.getDecisionLevel4();
			else return decisionsTaken.getDecisionLevel5();
			}
		return null;
	}
	
	String getRejectedDecisionTakenUser(LeaveTransaction leaveTransaction){
		LeaveFlowDecisionsTaken decisionsTaken = leaveTransaction.getDecisionsBean();
		if(decisionsTaken!=null && decisionsTaken.getDecisionLevel1()!=null && NO.equalsIgnoreCase(decisionsTaken.getDecisionLevel1()))
			return getEmployeeService().getFullNameOfEmployee(decisionsTaken.getDecisionUserLevel1());
		if(decisionsTaken.getDecisionLevel2()!=null && NO.equalsIgnoreCase(decisionsTaken.getDecisionLevel2()))
			return getEmployeeService().getFullNameOfEmployee(decisionsTaken.getDecisionUserLevel2());
		if(decisionsTaken.getDecisionLevel3()!=null && NO.equalsIgnoreCase(decisionsTaken.getDecisionLevel3()))
			return getEmployeeService().getFullNameOfEmployee(decisionsTaken.getDecisionUserLevel3());
		if(decisionsTaken.getDecisionLevel4()!=null && NO.equalsIgnoreCase(decisionsTaken.getDecisionLevel4()))
			return getEmployeeService().getFullNameOfEmployee(decisionsTaken.getDecisionUserLevel4());
		if(decisionsTaken.getDecisionLevel5()!=null && NO.equalsIgnoreCase(decisionsTaken.getDecisionLevel5()))
			return getEmployeeService().getFullNameOfEmployee(decisionsTaken.getDecisionUserLevel5());
		return null;
	}
	
	private EmployeeService getEmployeeService()
	{
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		return (EmployeeService) applicationContext.getBean("employeeService");
		
		//return new EmployeeService(); use this for mock
		
	}
	
	String getAllTheApproverNames(LeaveTransaction leaveTransaction){
		LeaveFlowDecisionsTaken decisionsTaken = leaveTransaction.getDecisionsBean();
		StringBuffer namesList = new StringBuffer();
		int totalLevels =	getTotalLevelsOfApprovalRequired(leaveTransaction);
		int count = 0;
		if(decisionsTaken.getDecisionUserLevel1()!=null){
			namesList.append(getEmployeeService().getFullNameOfEmployee(decisionsTaken.getDecisionUserLevel1())) ;
			++count;
		}
		if(decisionsTaken.getDecisionUserLevel2()!=null){
			if(totalLevels==count)
				namesList.append(getEmployeeService().getFullNameOfEmployee(decisionsTaken.getDecisionUserLevel2()));
			else{
				namesList.append(", "+getEmployeeService().getFullNameOfEmployee(decisionsTaken.getDecisionUserLevel2()));
				++count;
			}
		}
		if(decisionsTaken.getDecisionUserLevel3()!=null){
			if(totalLevels==count){
				namesList.append(getEmployeeService().getFullNameOfEmployee(decisionsTaken.getDecisionUserLevel3()));
			}else{
			namesList.append(", "+getEmployeeService().getFullNameOfEmployee(decisionsTaken.getDecisionUserLevel3()));
			++count;
			}
		}
		if(decisionsTaken.getDecisionUserLevel4()!=null){
			if(totalLevels==count)
				namesList.append(getEmployeeService().getFullNameOfEmployee(decisionsTaken.getDecisionUserLevel4()));
			else{
				namesList.append(", "+getEmployeeService().getFullNameOfEmployee(decisionsTaken.getDecisionUserLevel4()));
				++count;
			}
		}
		if(decisionsTaken.getDecisionUserLevel5()!=null)
			namesList.append("and "+getEmployeeService().getFullNameOfEmployee(decisionsTaken.getDecisionUserLevel5()));
		
		return namesList.toString();
		
		
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
	
	// By passing the current level, we get the current level role which should be take decision
	String getApproverRoleForTheGivenLevel(LeaveTransaction leaveTransaction,int currentLevel){
		LeaveRuleBean approvalRoles =	leaveTransaction.getLeaveRuleBean();
		Map<Integer,String> rolesMap = new HashMap<Integer, String>();
		rolesMap.put(0, approvalRoles.getApproverNameLevel1());
		rolesMap.put(1, approvalRoles.getApproverNameLevel2());
		rolesMap.put(2, approvalRoles.getApproverNameLevel3());
		rolesMap.put(3, approvalRoles.getApproverNameLevel4());
		rolesMap.put(4, approvalRoles.getApproverNameLevel5());
		
		return rolesMap.get(currentLevel);
	}
	
	public static void main(String[] args){
		Employee employee = new Employee();
		employee.setName("Lakshminarayana R");
		employee.setWorkEmailAddress("rlnarayana4java@gmail.com");
		LeaveType leaveType = new LeaveType();
		leaveType.setDescription("Annaul Leave");
		leaveType.setId(2);
		LeaveRuleBean leaveRuleBean = new LeaveRuleBean();
		leaveRuleBean.setId(6);
		leaveRuleBean.setLeaveType(Leave.ANNUAL.toString());
		leaveRuleBean.setRoleType("ROLE_EMP");
		leaveRuleBean.setApproverNameLevel1("ROLE_TL");
		leaveRuleBean.setApproverNameLevel2("ROLE_PL");
		/*leaveRuleBean.setApproverNameLevel3("ROLE_PM");
		leaveRuleBean.setApproverNameLevel4("ROLE_HEAD");*/
		//leaveRuleBean.setApproverNameLevel4("ROLE_BOSS");
		LeaveFlowDecisionsTaken decisionsBean = new LeaveFlowDecisionsTaken();
		decisionsBean.setLeaveFlowDecisionTakenId(15);
		decisionsBean.setDecisionLevel1(YES);
		decisionsBean.setDecisionLevel2(YES);
		/*decisionsBean.setDecisionLevel3(YES);
		decisionsBean.setDecisionLevel4(YES);*/
		decisionsBean.setDecisionUserLevel1("Tester1");
		decisionsBean.setDecisionUserLevel2("Tester2");
		/*decisionsBean.setDecisionUserLevel3("Tester3");
		decisionsBean.setDecisionUserLevel4("Tester4");*/
		LeaveTransaction leave = new LeaveTransaction(106, new Date(), new Date(),new Date(), new Double(7), new Double(1), "Marriage", leaveType, employee, "Pending", null, leaveRuleBean, decisionsBean, false);
		leave.setRejectReason("have to work");
		//LeaveTransaction leave = new LeaveTransaction();
		EmailNotificationPreparingServiceImpl service = new EmailNotificationPreparingServiceImpl();
		service.sendEmailNotificationToLeaveApplicant(leave);
		//service.sendEmailNotificationToLeaveApprover(leave);
		service.sendEmailNotificationToHR(leave);
	}
	
	private List<Employee> getMockApproverList(String role) {
		Employee emp1 = new Employee();
		Employee emp2 = new Employee();
		emp1.setWorkEmailAddress("rlnarayana4java@gmail.com");
		emp2.setWorkEmailAddress("lakshmin.ravuri@beans.com.my");
		ArrayList<Employee> emplist = new ArrayList<Employee>();
		emplist.add(emp1);
		//emplist.add(emp2);
		return emplist;
	}
	
	/*
	 * Enable it only for mock
	 * 
	 * static class EmployeeService{
		String getFullNameOfEmployee(String name){
			return name;
		}
		List<Employee> findAllEmployeesByRole(String role){
			Employee emp2 = new Employee();
			emp2.setWorkEmailAddress("lakshmin.ravuri@beans.com.my");
			ArrayList<Employee> emplist = new ArrayList<Employee>();
			emplist.add(emp2);
			return emplist;
		}
	}*/
}
