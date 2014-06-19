package com.beans.leaveapp.leaveapplicationcomment.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;

@Entity
@Table(name="LeaveApplicationComment")
public class LeaveApplicationComment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	private LeaveTransaction leaveTransaction;
	private String comment;
	private Date creationTime;
	private String createdBy;
	private Date lastModifiedTime;
	private String lastModifiedBy;
	private Boolean isDeleted;
	
	
	@Id
	@GeneratedValue
	@Column(name="id", nullable=false, unique=true)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@ManyToOne(targetEntity=LeaveTransaction.class)
	@JoinColumn(columnDefinition="leaveTransactionId")
	public LeaveTransaction getLeaveTransaction() {
		return leaveTransaction;
	}
	public void setLeaveTransaction(LeaveTransaction leaveTransaction) {
		this.leaveTransaction = leaveTransaction;
	}
	
	@Column(name="comment", nullable=false)
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Column(name="creationTime",nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	
	@Column(name="createdBy",nullable=true)
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name="lastModifiedTime",nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}
	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	@Column(name="lastModifiedBy",nullable=true)
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
	@Column(name="isdeleted", columnDefinition="TINYINT(1)") 
	@Type(type="org.hibernate.type.NumericBooleanType")
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
			
	
	
	
	
	
	
}
