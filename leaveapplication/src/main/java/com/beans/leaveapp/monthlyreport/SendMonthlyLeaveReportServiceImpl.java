package com.beans.leaveapp.monthlyreport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.repository.EmployeeRepository;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetransaction.repository.LeaveTransactionRepository;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;
import com.beans.leaveapp.yearlyentitlement.repository.YearlyEntitlementRepository;
import com.beans.util.email.EmailSender;
@Service
public class SendMonthlyLeaveReportServiceImpl implements SendMonthlyLeaveReportService{

	@Resource
	EmployeeRepository employeeRepository;
	
	@Resource
	YearlyEntitlementRepository entitlementRepository;
	
	@Resource
	LeaveTransactionRepository leaveRepository;
	
	@Override
	public void sendMonthlyLeaveReportToEmployees() {
		try{
		// Selecting all employee who are PERM and CONT which excludes roles with ROLE_ADMIN and ROLE_OPERDIR
		List<Employee> employeeList = employeeRepository.findAllEmployeesForSendingMonthlyLeaveReport();
		if(employeeList!=null && employeeList.size()>0){
			
			for (Employee employee : employeeList) {
				
				String[] resultData = getMonthlyUnpaidLeaveDataAndTotalDays(employee);
				
				sendEmailMontlyLeaveReportToEmployee(employee, getMonthlyLeaveEntitlementData(employee),resultData[0],resultData[1]);
			}
		}
		}catch(Exception e){
			System.out.println("Error while sending mails to employees of monthly leave report");
			e.printStackTrace();
		}
	}
	private void sendEmailMontlyLeaveReportToEmployee(Employee employee,String entitlementData,String unpaidLeaveData, String unpaidTotalDays){
	
	try {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("monthlyLeaveReportTemplate.html");
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
		htmlEmailTemplate = htmlEmailTemplate.replace("##employeeName##",employee.getName());
		htmlEmailTemplate = htmlEmailTemplate.replace("##entitlementData##",entitlementData);
		htmlEmailTemplate = htmlEmailTemplate.replace("##unpaidLeavesData##",unpaidLeaveData);
		htmlEmailTemplate = htmlEmailTemplate.replace("##totalUnpaidLeaves##",unpaidTotalDays);
		// add reciepiant email address
		try {
			if(employee.getWorkEmailAddress()!=null && !employee.getWorkEmailAddress().equals(""))
			email.addTo(employee.getWorkEmailAddress(), employee.getName());
			email.setSubject("Reg : Monthly Leave Report of "+employee.getName());
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
			System.out.println("Monthly Leave Report Email has been sent successfully to Employee "+employee.getName());
		
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error while sending email to Employee "+employee.getName());
		}
	}
	
	@SuppressWarnings("deprecation")
	private String[] getMonthlyUnpaidLeaveDataAndTotalDays(Employee employee) throws ParseException{
		// Unpaid Leaves Table construction with data
		StringBuffer unpaidLeaveData = new StringBuffer();
		Double totalUnpaidDays = 0.0;
		Date firstDayOfMonth = new Date();
		firstDayOfMonth.setDate(1);
		firstDayOfMonth.setHours(1);
		List<LeaveTransaction>	unpaidLeaveList =	leaveRepository.findAllUnpaidLeavesApproved(employee.getId(), new java.sql.Date(firstDayOfMonth.getTime()));
		if(unpaidLeaveList!=null && unpaidLeaveList.size()>0){
			
			for (LeaveTransaction leaveTransaction : unpaidLeaveList) {
				unpaidLeaveData.append("<tr><td>"+leaveTransaction.getStartDateTime()+"</td>");
				unpaidLeaveData.append("<td>"+leaveTransaction.getEndDateTime()+"</td>");
				unpaidLeaveData.append("<td>"+leaveTransaction.getNumberOfDays()+"</td></tr>");
				totalUnpaidDays = totalUnpaidDays+leaveTransaction.getNumberOfDays();
			}
		}
		else{
			unpaidLeaveData.append("<tr><td colspan='3'> <b>No Data Found</b> </td></tr>");
		}
		return new String[]{unpaidLeaveData.toString(),totalUnpaidDays.toString()};
	}
	
	private String getMonthlyLeaveEntitlementData(Employee employee){
		StringBuffer entilementData = new StringBuffer();
		List<YearlyEntitlement>	employeeEntitlementList = entitlementRepository.findByEmployeeIdNotIncludeUnpaid(employee.getId());
		if(employeeEntitlementList!=null && employeeEntitlementList.size()>0){
			
			for (YearlyEntitlement entitlement : employeeEntitlementList) {
				entilementData.append("<tr><td>"+entitlement.getLeaveType().getDescription()+"</td>");
				entilementData.append("<td>"+entitlement.getEntitlement()+"</td>");
				entilementData.append("<td>"+entitlement.getCurrentLeaveBalance()+"</td>");
				entilementData.append("<td>"+entitlement.getYearlyLeaveBalance()+"</td></tr>");
			}
		}
		return entilementData.toString();
	}

	@Override
	public void sendMonthlyLeaveReportToHR() {
		try{
			// Selecting all employee,which excludes roles with ROLE_ADMIN and ROLE_OPERDIR
			List<Employee> employeeList = employeeRepository.findAllEmployeesForSendingMonthlyLeaveReport();
			if(employeeList!=null && employeeList.size()>0){
				
				for (Employee employee : employeeList) {
					String entitlementData = getMonthlyLeaveEntitlementData(employee);
					String[] resultData = getMonthlyUnpaidLeaveDataAndTotalDays(employee);
				}
				//sendEmailMontlyLeaveReportToEmployee(employee, entilementData.toString());
			}
			}catch(Exception e){
				System.out.println("Error while sending mails to employees of monthly leave report");
				e.printStackTrace();
			}
		
	}
}
