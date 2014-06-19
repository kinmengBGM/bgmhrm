package com.beans.leaveapp.leavetransaction.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.leaveapplicationcomment.model.LeaveApplicationComment;
import com.beans.leaveapp.leavetype.model.LeaveType;

@Entity
@Table(name="LeaveTransaction")
public class LeaveTransaction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8702749544815399487L;
	private int id;
	private Date applicationDate;
	private Date  startDateTime;
	private Date  endDateTime;
	private Double yearlyLeaveBalance;
	private Double numberOfDays;
	private String reason;
	private LeaveType leaveType;
	private Employee employee;
	private List<LeaveApplicationComment> leaveApplicationComments;
	private Long taskId;
	private boolean isDelete;
	private String createdBy;
	private java.util.Date creationTime;
	private String lastModifiedBy;
	private java.util.Date lastModifiedTime;
	private String status;
	
	public LeaveTransaction(int id, Date applicationDate,
			Date startDateTime, Date endDateTime,
			Double yearlyLeaveBalance, Double numberOfDays, String reason,String status,
			LeaveType leaveType, Employee employee, boolean isDeleted) {
		super();
		this.id = id;
		this.applicationDate = applicationDate;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.yearlyLeaveBalance = yearlyLeaveBalance;
		this.numberOfDays = numberOfDays;
		this.reason = reason;
		this.status = status;
		this.leaveType = leaveType;
		this.employee = employee;
		this.isDelete = isDeleted;
	}
	public LeaveTransaction() {
		
	}
	
	@Id
	@GeneratedValue
	@Column( name="id",nullable=false,unique=true)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="applicationDate",nullable=false)
	public Date getApplicationDate() {
		return applicationDate;
	}
	public void setApplicationDate(java.util.Date applicationDate) {
		
		this.applicationDate = applicationDate;
	}
	
	@Column(name="startDateTime",nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}
	
	@Column(name="endDateTime",nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getEndDateTime() {
		return endDateTime;
	}
	
	public String fetchStartTimeStr(){
		if(startDateTime!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			return sdf.format(startDateTime); 
		}
		return startDateTime.toString();
	}
	public String fetchEndTimeStr(){
		if(endDateTime!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			return sdf.format(endDateTime); 
		}
		return endDateTime.toString();
	}
	
	
	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}
	
	@Column(name="yearlyLeaveBalance",nullable=true)
	public Double getYearlyLeaveBalance() {
		return yearlyLeaveBalance;
	}
	public void setYearlyLeaveBalance(Double yearlyLeaveBalance) {
		this.yearlyLeaveBalance = yearlyLeaveBalance;
	}
	
	
	@Column(name="numberOfDays",nullable=true)
	public Double getNumberOfDays() {
		return numberOfDays;
	}
	
	public void setNumberOfDays(Double numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	
	@Column(name="reason",nullable=false)
	public String getReason() {
		return reason;
	} 
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Column(name="isdeleted", columnDefinition="TINYINT(1)") 
	@Type(type="org.hibernate.type.NumericBooleanType")
	public boolean isDeleted() {
		return isDelete;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDelete = isDeleted;
	}
	

	@OneToOne
	@JoinColumn(name="leaveTypeId",nullable=false)
	public LeaveType getLeaveType(){
		return leaveType;
	}
	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}
	
    @OneToOne
	@JoinColumn(name="employeeId")
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	@Column(name="taskId", nullable=true)
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
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

	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="leaveTransaction")
	public List<LeaveApplicationComment> getLeaveApplicationComments() {
		return leaveApplicationComments;
	}
	public void setLeaveApplicationComments(
			List<LeaveApplicationComment> leaveApplicationComments) {
		this.leaveApplicationComments = leaveApplicationComments;
	}
	
	@Column(name="status",nullable=true)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
