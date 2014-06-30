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
@Entity
@Table(name="AnnualLeaveReport")
public class AnnualLeaveReport implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5047402077840552876L;
	private int id;
	private Integer sortingMonthId;
	private String monthOfYear;
	private Double balanceBroughtForward;
	private Double leavesCredited;
	private Double leavesTaken;
	private Double timeInLieuCredited;
	private Double currentLeaveBalance;
	private Double yearlyLeaveBalance;
	private Employee employee;
	private Integer financialYear;
	
	
	
	public AnnualLeaveReport(int id, Integer sortingMonthId,
			String monthOfYear, Double balanceBroughtForward,
			Double leavesCredited, Double leavesTaken,
			Double timeInLieuCredited, Double currentLeaveBalance,
			Double yearlyLeaveBalance, Employee employee, Integer financialYear) {
		super();
		this.id = id;
		this.sortingMonthId = sortingMonthId;
		this.monthOfYear = monthOfYear;
		this.balanceBroughtForward = balanceBroughtForward;
		this.leavesCredited = leavesCredited;
		this.leavesTaken = leavesTaken;
		this.timeInLieuCredited = timeInLieuCredited;
		this.currentLeaveBalance = currentLeaveBalance;
		this.yearlyLeaveBalance = yearlyLeaveBalance;
		this.employee = employee;
		this.financialYear = financialYear;
	}
	
	
	
	public AnnualLeaveReport() {
		super();
		// TODO Auto-generated constructor stub
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
	
	@Column(name="balanceBroughtForward", nullable=true)
	public Double getBalanceBroughtForward() {
		return balanceBroughtForward;
	}
	
	@Column(name="leavesCredited", nullable=true)
	public Double getLeavesCredited() {
		return leavesCredited;
	}
	
	@Column(name="leavesTaken", nullable=true)
	public Double getLeavesTaken() {
		return leavesTaken;
	}
	
	@Column(name="timeInLieuCredited", nullable=true)
	public Double getTimeInLieuCredited() {
		return timeInLieuCredited;
	}
	
	@Column(name="currentLeaveBalance", nullable=true)
	public Double getCurrentLeaveBalance() {
		return currentLeaveBalance;
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
	public void setId(int id) {
		this.id = id;
	}
	public void setSortingMonthId(Integer sortingMonthId) {
		this.sortingMonthId = sortingMonthId;
	}
	public void setMonthOfYear(String monthOfYear) {
		this.monthOfYear = monthOfYear;
	}
	public void setBalanceBroughtForward(Double balanceBroughtForward) {
		this.balanceBroughtForward = balanceBroughtForward;
	}
	public void setLeavesCredited(Double leavesCredited) {
		this.leavesCredited = leavesCredited;
	}
	public void setLeavesTaken(Double leavesTaken) {
		this.leavesTaken = leavesTaken;
	}
	public void setTimeInLieuCredited(Double timeInLieuCredited) {
		this.timeInLieuCredited = timeInLieuCredited;
	}
	public void setCurrentLeaveBalance(Double currentLeaveBalance) {
		this.currentLeaveBalance = currentLeaveBalance;
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
		return "AnnualLeaveReport [id=" + id + ", sortingMonthId="
				+ sortingMonthId + ", monthOfYear=" + monthOfYear
				+ ", balanceBroughtForward=" + balanceBroughtForward
				+ ", leavesCredited=" + leavesCredited + ", leavesTaken="
				+ leavesTaken + ", timeInLieuCredited=" + timeInLieuCredited
				+ ", currentLeaveBalance=" + currentLeaveBalance
				+ ", yearlyLeaveBalance=" + yearlyLeaveBalance + ", employee="
				+ employee + ", financialYear=" + financialYear + "]";
	}
	
	
	

}
