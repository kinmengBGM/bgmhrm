package com.beans.leaveapp.employeeregistration.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.beans.exceptions.BSLException;
import com.beans.leaveapp.employee.service.EmployeeRegistrationService;
import com.beans.leaveapp.web.bean.BaseMgmtBean;

public class EmployeeRegistrationBean extends BaseMgmtBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private String fullname;
	private String personalEmailAddress;
	private String personalPhoneNumber;
	private String gender;
	private String idNumber;
	private String passportNumber;
	private String maritalStatus;
	
	private EmployeeRegistrationService employeeRegistrationService;
	
	public String doRegister() {
		try{
			HashMap<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("username", username);
			parameterMap.put("password", password);
			parameterMap.put("fullname", fullname);
			parameterMap.put("personalEmailAddress", personalEmailAddress);
			parameterMap.put("personalPhoneNumber", personalPhoneNumber);
			parameterMap.put("gender", gender);
			parameterMap.put("idNumber", idNumber);
			parameterMap.put("passportNumber", passportNumber);
			parameterMap.put("maritalStatus", maritalStatus);

			employeeRegistrationService.submitRegistration(parameterMap);
			
			return "/thankyou.xhtml";
		}catch(BSLException e){
			FacesMessage msg = new FacesMessage("Error : "+ getExcptnMesProperty("error.registration.submit"),"Registration Error");  
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
			return "";
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	
	
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getPersonalEmailAddress() {
		return personalEmailAddress;
	}
	public void setPersonalEmailAddress(String personalEmailAddress) {
		this.personalEmailAddress = personalEmailAddress;
	}
	public String getPersonalPhoneNumber() {
		return personalPhoneNumber;
	}
	public void setPersonalPhoneNumber(String personalPhoneNumber) {
		this.personalPhoneNumber = personalPhoneNumber;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getPassportNumber() {
		return passportNumber;
	}
	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}
	
	public EmployeeRegistrationService getEmployeeRegistrationService() {
		return employeeRegistrationService;
	}
	public void setEmployeeRegistrationService(
			EmployeeRegistrationService employeeRegistrationService) {
		this.employeeRegistrationService = employeeRegistrationService;
	}
	
	
}
