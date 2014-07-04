package com.beans.leaveapp.monthlyreport.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.leavetype.model.LeaveType;

@Entity
@Table(name="MonthlyLeaveReport")
public class MonthlyLeaveReport implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3579359629941921475L;
	private int id;
	private Integer sortingMonthId;
	private String monthOfYear;
	private Double leavesTaken;
	private Double yearlyLeaveBalance;
	private Integer financialYear;
	private Employee employee;
	private LeaveType leaveType;
	
	
	
	public MonthlyLeaveReport() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MonthlyLeaveReport(int id, Integer sortingMonthId,
			String monthOfYear, Double leavesTaken, Double yearlyLeaveBalance,
			Integer financialYear, Employee employee, LeaveType leaveType) {
		super();
		this.id = id;
		this.sortingMonthId = sortingMonthId;
		this.monthOfYear = monthOfYear;
		this.leavesTaken = leavesTaken;
		this.yearlyLeaveBalance = yearlyLeaveBalance;
		this.financialYear = financialYear;
		this.employee = employee;
		this.leaveType = leaveType;
	}




	@Id
	@GeneratedValue
	@Column(name="id", nullable=false, unique=true)
	public int getId() {
		return id;
	}
	
	@Column(name="sortingMonthId", nullable=false)
	public Integer getSortingMonthId() {
		return sortingMonthId;
	}
	
	@Column(name="monthOfYear", nullable=true)
	public String getMonthOfYear() {
		return monthOfYear;
	}
	
		
	@Column(name="leavesTaken", nullable=true)
	public Double getLeavesTaken() {
		return leavesTaken;
	}
	
	
	@Column(name="yearlyLeaveBalance", nullable=true)
	public Double getYearlyLeaveBalance() {
		return yearlyLeaveBalance;
	}
	
	
	@OneToOne
	@JoinColumn(name = "employeeId")
	public Employee getEmployee() {
		return employee;
	}
	
	@Column(name="financialYear", nullable=true)
	public Integer getFinancialYear() {
		return financialYear;
	}
	
	
	@OneToOne
	@JoinColumn(name = "leaveTypeId")
	public LeaveType getLeaveType() {
		return leaveType;
	}

	

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}


	public void setId(int id) {
		this.id = id;
	}
	public void setSortingMonthId(Integer sortingMonthId) {
		this.sortingMonthId = sortingMonthId;
	}
	public void setMonthOfYear(String monthOfYear) {
		this.monthOfYear = monthOfYear;
	}
	
	public void setLeavesTaken(Double leavesTaken) {
		this.leavesTaken = leavesTaken;
	}
	
	public void setYearlyLeaveBalance(Double yearlyLeaveBalance) {
		this.yearlyLeaveBalance = yearlyLeaveBalance;
	}
	
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public void setFinancialYear(Integer financialYear) {
		this.financialYear = financialYear;
	}


	@Override
	public String toString() {
		return "MonthlyLeaveReport [id=" + id + ", sortingMonthId="
				+ sortingMonthId + ", monthOfYear=" + monthOfYear
				+ ", leavesTaken=" + leavesTaken + ", currentLeaveBalance="
				+ yearlyLeaveBalance + ", financialYear=" + financialYear
				+ ", employee=" + employee + ", leaveType=" + leaveType + "]";
	}
	

}
