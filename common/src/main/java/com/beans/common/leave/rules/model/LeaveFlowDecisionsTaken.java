package com.beans.common.leave.rules.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * @author lakshminarayana
 * 
 */
@Entity
@Table(name="LeaveFlowDecisionTaken")
public class LeaveFlowDecisionsTaken  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int leaveFlowDecisionTakenId;
	private String decisionLevel1;
	private String decisionLevel2;
	private String decisionLevel3;
	private String decisionLevel4;
	private String decisionLevel5;
	private String decisionUserLevel1;
	private String decisionUserLevel2;
	private String decisionUserLevel3;
	private String decisionUserLevel4;
	private String decisionUserLevel5;
	private Boolean isDeleted;

	public LeaveFlowDecisionsTaken() {
		super();
	}

	public void setLeaveFlowDecisionTakenId(int leaveFlowDecisionTakenId) {
		this.leaveFlowDecisionTakenId = leaveFlowDecisionTakenId;
	}

	@Id
	@GeneratedValue
	@Column(name = "leaveFlowDecisionTakenId", nullable = false, unique = true)
	public int getLeaveFlowDecisionTakenId() {
		return leaveFlowDecisionTakenId;
	}

	@Column(name="isDeleted", columnDefinition="TINYINT(1)")
	@Type(type="org.hibernate.type.NumericBooleanType")
	public Boolean isDeleted() {
		return isDeleted;
	}
	
	@Column(name = "decisionLevel1", nullable = true)
	public String getDecisionLevel1() {
		return decisionLevel1;
	}
	@Column(name = "decisionLevel2", nullable = true)
	public String getDecisionLevel2() {
		return decisionLevel2;
	}
	@Column(name = "decisionLevel3", nullable = true)
	public String getDecisionLevel3() {
		return decisionLevel3;
	}
	@Column(name = "decisionLevel4", nullable = true)
	public String getDecisionLevel4() {
		return decisionLevel4;
	}
	@Column(name = "decisionLevel5", nullable = true)
	public String getDecisionLevel5() {
		return decisionLevel5;
	}
	@Column(name = "decisionUserLevel1", nullable = true)
	public String getDecisionUserLevel1() {
		return decisionUserLevel1;
	}
	@Column(name = "decisionUserLevel2", nullable = false)
	public String getDecisionUserLevel2() {
		return decisionUserLevel2;
	}
	@Column(name = "decisionUserLevel3", nullable = true)
	public String getDecisionUserLevel3() {
		return decisionUserLevel3;
	}
	@Column(name = "decisionUserLevel4", nullable = true)
	public String getDecisionUserLevel4() {
		return decisionUserLevel4;
	}
	@Column(name = "decisionUserLevel5", nullable = true)
	public String getDecisionUserLevel5() {
		return decisionUserLevel5;
	}

	public void setDecisionLevel1(String decisionLevel1) {
		this.decisionLevel1 = decisionLevel1;
	}

	public void setDecisionLevel2(String decisionLevel2) {
		this.decisionLevel2 = decisionLevel2;
	}

	public void setDecisionLevel3(String decisionLevel3) {
		this.decisionLevel3 = decisionLevel3;
	}

	public void setDecisionLevel4(String decisionLevel4) {
		this.decisionLevel4 = decisionLevel4;
	}

	public void setDecisionLevel5(String decisionLevel5) {
		this.decisionLevel5 = decisionLevel5;
	}

	public void setDecisionUserLevel1(String decisionUserLevel1) {
		this.decisionUserLevel1 = decisionUserLevel1;
	}

	public void setDecisionUserLevel2(String decisionUserLevel2) {
		this.decisionUserLevel2 = decisionUserLevel2;
	}

	public void setDecisionUserLevel3(String decisionUserLevel3) {
		this.decisionUserLevel3 = decisionUserLevel3;
	}

	public void setDecisionUserLevel4(String decisionUserLevel4) {
		this.decisionUserLevel4 = decisionUserLevel4;
	}

	public void setDecisionUserLevel5(String decisionUserLevel5) {
		this.decisionUserLevel5 = decisionUserLevel5;
	}

	public void setDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "LeaveFlowDecisionsTaken [leaveFlowDecisionTakenId="
				+ leaveFlowDecisionTakenId + ", decisionLevel1="
				+ decisionLevel1 + ", decisionLevel2=" + decisionLevel2
				+ ", decisionLevel3=" + decisionLevel3 + ", decisionLevel4="
				+ decisionLevel4 + ", decisionLevel5=" + decisionLevel5
				+ ", decisionUserLevel1=" + decisionUserLevel1
				+ ", decisionUserLevel2=" + decisionUserLevel2
				+ ", decisionUserLevel3=" + decisionUserLevel3
				+ ", decisionUserLevel4=" + decisionUserLevel4
				+ ", decisionUserLevel5=" + decisionUserLevel5 + ", isDeleted="
				+ isDeleted + "]";
	}

	public LeaveFlowDecisionsTaken(int leaveFlowDecisionTakenId,
			String decisionLevel1, String decisionLevel2,
			String decisionLevel3, String decisionLevel4,
			String decisionLevel5, String decisionUserLevel1,
			String decisionUserLevel2, String decisionUserLevel3,
			String decisionUserLevel4, String decisionUserLevel5,
			Boolean isDeleted) {
		super();
		this.leaveFlowDecisionTakenId = leaveFlowDecisionTakenId;
		this.decisionLevel1 = decisionLevel1;
		this.decisionLevel2 = decisionLevel2;
		this.decisionLevel3 = decisionLevel3;
		this.decisionLevel4 = decisionLevel4;
		this.decisionLevel5 = decisionLevel5;
		this.decisionUserLevel1 = decisionUserLevel1;
		this.decisionUserLevel2 = decisionUserLevel2;
		this.decisionUserLevel3 = decisionUserLevel3;
		this.decisionUserLevel4 = decisionUserLevel4;
		this.decisionUserLevel5 = decisionUserLevel5;
		this.isDeleted = isDeleted;
	} 
	
	
	
}
