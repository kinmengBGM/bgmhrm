package com.beans.leaveapp.yearlyentitlement.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.leavetype.model.LeaveType;

@Entity
@Table(name = "YearlyEntitlement")
public class YearlyEntitlement implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;;
	private double entitlement;
	private double currentLeaveBalance;
	private double yearlyLeaveBalance;
	private boolean isDeleted = false;
	private String createdBy;
	private java.util.Date creationTime;
	private String lastModifiedBy;
	private java.util.Date lastModifiedTime;
	private Employee employee;
	private LeaveType leaveType;

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false, unique = true)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "entitlement", nullable = false)
	public double getEntitlement() {
		return entitlement;
	}

	public void setEntitlement(double entitlement) {
		this.entitlement = entitlement;
	}

	@Column(name = "currentLeaveBalance", nullable = false)
	public double getCurrentLeaveBalance() {
		return currentLeaveBalance;
	}

	public void setcurrentLeaveBalance(double currentLeaveBalance) {
		this.currentLeaveBalance = currentLeaveBalance;
	}

	@Column(name = "yearlyLeaveBalance", nullable = false)
	public double getYearlyLeaveBalance() {
		return yearlyLeaveBalance;
	}

	public void setYearlyLeaveBalance(double yearlyLeaveBalance) {
		this.yearlyLeaveBalance = yearlyLeaveBalance;
	}
	@Column(name = "isdeleted", columnDefinition = "TINYINT(1)")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public void setCreationTime(java.util.Date creationTime) {
		this.creationTime = creationTime;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public void setLastModifiedTime(java.util.Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
			
	@Column(name="creationTime",nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	public java.util.Date getCreationTime() {
		return creationTime;
	}
	@Column(name="createdBy",nullable=true)
	public String getCreatedBy() {
		return createdBy;
	}
	@Column(name="lastModifiedTime",nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	public java.util.Date getLastModifiedTime() {
		return lastModifiedTime;
	}
	@Column(name="lastModifiedBy",nullable=true)
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	
	@OneToOne
	@JoinColumn(name = "employeeId")
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@OneToOne
	@JoinColumn(name = "leaveTypeId")
	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}

}

