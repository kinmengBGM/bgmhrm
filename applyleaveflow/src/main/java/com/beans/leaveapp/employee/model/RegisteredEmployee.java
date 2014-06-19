package com.beans.leaveapp.employee.model;

import java.io.Serializable;
import java.util.Date;

public class RegisteredEmployee implements Serializable{
	private static final long serialVersionUID = 1L;
	private long taskId;
	private String fullname;
	private String username;
	private Date registrationDate;
	private String idNumber;
	private String passportNumber;
	private int departmentId;
	private int employeeTypeId;
	private int employeeGradeId;
	private String personalEmailAddress;
	private String personalPhoneNumber;
	private String workEmailAddress;
	private String workPhoneNumber;
	private String password;
	private String gender;
	private String reason;
	private String employeeNumber;
	private String position;
	private Date joinDate;
	
	public long getTaskId() {
		return taskId;
	}
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
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
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public int getEmployeeTypeId() {
		return employeeTypeId;
	}
	public void setEmployeeTypeId(int employeeTypeId) {
		this.employeeTypeId = employeeTypeId;
	}
	public int getEmployeeGradeId() {
		return employeeGradeId;
	}
	public void setEmployeeGradeId(int employeeGradeId) {
		this.employeeGradeId = employeeGradeId;
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
	public String getWorkEmailAddress() {
		return workEmailAddress;
	}
	public void setWorkEmailAddress(String workEmailAddress) {
		this.workEmailAddress = workEmailAddress;
	}
	public String getWorkPhoneNumber() {
		return workPhoneNumber;
	}
	public void setWorkPhoneNumber(String workPhoneNumber) {
		this.workPhoneNumber = workPhoneNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	public Date getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
}
