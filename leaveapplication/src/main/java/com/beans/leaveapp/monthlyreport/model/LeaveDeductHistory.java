package com.beans.leaveapp.monthlyreport.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.beans.leaveapp.employee.model.Employee;
@Entity
@Table(name="LeaveDeductHistory")
public class LeaveDeductHistory implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6203968445567187541L;
	/**
	 * 
	 */
	private int id;
	private Double numberOfLeavesDeducted;
	private Employee employee;
	private Integer financialYear;
	
	public LeaveDeductHistory(){
		
	}
	
	public LeaveDeductHistory(int id, Double numberOfLeavesDeducted,
			Employee employee, Integer financialYear) {
		super();
		this.id = id;
		this.numberOfLeavesDeducted = numberOfLeavesDeducted;
		this.employee = employee;
		this.financialYear = financialYear;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", nullable=false, unique=true)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="numberOfLeavesDeducted", nullable=true)
	public Double getNumberOfLeavesDeducted() {
		return numberOfLeavesDeducted;
	}
	public void setNumberOfLeavesDeducted(Double numberOfLeavesDeducted) {
		this.numberOfLeavesDeducted = numberOfLeavesDeducted;
	}
	
	@OneToOne
	@JoinColumn(name = "employeeId")
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	@Column(name="financialYear", nullable=true)
	public Integer getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(Integer financialYear) {
		this.financialYear = financialYear;
	}
	@Override
	public String toString() {
		return "LeaveDeductHistory [id=" + id + ", numberOfLeavesDeducted="
				+ numberOfLeavesDeducted + ", employee=" + employee
				+ ", financialYear=" + financialYear + "]";
	}
	
	
	
	

}
