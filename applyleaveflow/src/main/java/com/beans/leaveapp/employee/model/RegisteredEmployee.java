package com.beans.leaveapp.employee.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="RegisteredEmployee")
public class RegisteredEmployee implements Serializable{
	private static final long serialVersionUID = 1L;
	private long taskId;
	private int id;
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
	private String maritalStatus;
	private String registrationStatus;

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	
	@Id
	@GeneratedValue
	@Column(name="id", nullable=false, unique=true)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="username", nullable=false)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name="password", nullable=false)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="fullname", nullable=false)
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	@Column(name="personalEmailAddress", nullable=true)
	public String getPersonalEmailAddress() {
		return personalEmailAddress;
	}
	public void setPersonalEmailAddress(String personalEmailAddress) {
		this.personalEmailAddress = personalEmailAddress;
	}
	
	@Column(name="personalPhoneNumber", nullable=true)
	public String getPersonalPhoneNumber() {
		return personalPhoneNumber;
	}
	public void setPersonalPhoneNumber(String personalPhoneNumber) {
		this.personalPhoneNumber = personalPhoneNumber;
	}
		
	@Column(name="gender", nullable=false)
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Column(name="idNumber", nullable=true)
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	
	@Column(name="passportNumber", nullable=true)
	public String getPassportNumber() {
		return passportNumber;
	}
	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}
	
	@Column(name="maritalstatus", nullable=false)
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	
	@Column(name="status", nullable=false)
	public String getRegistrationStatus() {
		return registrationStatus;
	}
	public void setRegistrationStatus(String registrationStatus) {
		this.registrationStatus = registrationStatus;
	}
	
	@Column(name="registrationDate", nullable=false)
	@Temporal(TemporalType.DATE)
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
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
	@Override
	public String toString() {
		return "RegisteredEmployee [dd=" + id + ", fullname="
				+ fullname + ", username=" + username + ", registrationDate="
				+ registrationDate + ", idNumber=" + idNumber
				+ ", passportNumber=" + passportNumber + ", departmentId="
				+ departmentId + ", employeeTypeId=" + employeeTypeId
				+ ", employeeGradeId=" + employeeGradeId
				+ ", personalEmailAddress=" + personalEmailAddress
				+ ", personalPhoneNumber=" + personalPhoneNumber
				+ ", workEmailAddress=" + workEmailAddress
				+ ", workPhoneNumber=" + workPhoneNumber + ", password="
				+ password + ", gender=" + gender + ", reason=" + reason
				+ ", employeeNumber=" + employeeNumber + ", position="
				+ position + ", joinDate=" + joinDate + ", maritalStatus="
				+ maritalStatus + "]";
	}
	
	
}
