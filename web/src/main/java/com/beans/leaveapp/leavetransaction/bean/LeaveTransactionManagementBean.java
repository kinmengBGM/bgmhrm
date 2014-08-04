package com.beans.leaveapp.leavetransaction.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import com.beans.common.audit.service.AuditTrail;
import com.beans.common.security.users.model.Users;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.service.EmployeeService;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetransaction.model.LeaveTransactionsDataModel;
import com.beans.leaveapp.leavetransaction.service.LeaveTransactionService;
import com.beans.leaveapp.leavetype.model.LeaveType;

public class LeaveTransactionManagementBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	LeaveTransaction leaveTransaction = new LeaveTransaction();

	private LeaveTransactionService leaveTransactionService;
	private EmployeeService employeeService;
	private List<LeaveTransaction> leaveTransactionlist;
	private LeaveTransactionsDataModel leaveTransactionDataModel;
	private String employeename = this.getEmployeename();
	private String leaveType = this.getLeaveType();
	private LeaveTransaction selectedLeaveTransaction = new LeaveTransaction();
	private LeaveTransaction newLeaveTransaction = new LeaveTransaction();
	private List<String> employeeList;
	private List<String> leaveTypeList;
	boolean isInsert = false;
	private String name = this.getName(), searchLeaveType = this
			.getSearchLeaveType();
	private AuditTrail auditTrail;
	private Users actorUsers;
	private String status;
	private List<String> leaveList;
	Date date1;

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}
 
	
	

	public LeaveTransaction getSelectedLeaveTransaction() {
		return selectedLeaveTransaction;
	}

	public void setSelectedLeaveTransaction(
			LeaveTransaction selectedLeaveTransaction) {
		this.selectedLeaveTransaction = selectedLeaveTransaction;
	}

	public LeaveTransaction getNewLeaveTransaction() {
		return newLeaveTransaction;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setNewLeaveTransaction(LeaveTransaction newLeaveTransaction) {
		this.newLeaveTransaction = newLeaveTransaction;
	}

	public LeaveTransaction getLeaveTransaction() {
		return leaveTransaction;
	}

	public void setLeaveTransaction(LeaveTransaction leaveTransaction) {
		this.leaveTransaction = leaveTransaction;
	}

	public LeaveTransactionService getLeaveTransactionService() {
		return leaveTransactionService;
	}

	public void setLeaveTransactionService(
			LeaveTransactionService leaveTransactionService) {
		this.leaveTransactionService = leaveTransactionService;
	}

	public LeaveTransactionsDataModel getLeaveTransactionDataModel() {
		if (leaveTransactionDataModel == null || isInsert == true) {

			leaveTransactionDataModel = new LeaveTransactionsDataModel(
					this.getList());

		}
		return leaveTransactionDataModel;
	}

	public AuditTrail getAuditTrail() {
		return auditTrail;
	}

	public void setAuditTrail(AuditTrail auditTrail) {
		this.auditTrail = auditTrail;
	}

	public Users getActorUsers() {
		return actorUsers;
	}

	public void setActorUsers(Users actorUsers) {
		this.actorUsers = actorUsers;
	}

	public void setLeaveTransactionDataModel(
			LeaveTransactionsDataModel leaveTransactionDataModel) {
		this.leaveTransactionDataModel = leaveTransactionDataModel;
	}

	public List<LeaveTransaction> getList() {

		if (leaveTransactionlist == null || this.isInsert == true) {

			leaveTransactionlist = getLeaveTransactionService().findAll();
			System.out.println(leaveTransactionlist.size());
		}
		return leaveTransactionlist;
	}

	public void setList(List<LeaveTransaction> leaveTransactionlist) {
		this.leaveTransactionlist = leaveTransactionlist;
	}

	
	public void doSearchLeaveTransaction() {


		try {
			if (getName().equals("") && getSearchLeaveType().equals("") && date1 == null && getStatus().equals("")) {
				this.leaveTransactionlist = null;
				this.leaveTransactionDataModel = null;
			} else {
				
				leaveTransactionlist = this
						.getLeaveTransactionService()
						.findByEmployeeORfindByLeaveTypeORLeaveDatesORStatusORAll(
								getName(), getSearchLeaveType(), date1,
								getStatus());

				// System.out.println(leaveTransactionlist.size());
				this.leaveTransactionDataModel = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doUpdateLeaveTransaction() {


		Employee employee = getLeaveTransactionService().findByEmployee(
				selectedLeaveTransaction.getEmployee().getName());
		LeaveType leaveType = getLeaveTransactionService().findByLeaveType(
				this.selectedLeaveTransaction.getLeaveType().getName(),
				employee.getEmployeeType().getId());
		selectedLeaveTransaction.setEmployee(employee);
		selectedLeaveTransaction.setLeaveType(leaveType);
		selectedLeaveTransaction.setLastModifiedBy(actorUsers.getUsername());
		selectedLeaveTransaction.setLastModifiedTime(new Date());
		this.getLeaveTransactionService().update(selectedLeaveTransaction);
		this.setInsert(true);
	}

	public void doDeletedLeaveTransaction() {
		int id = selectedLeaveTransaction.getId();
		System.out.println(id);
		this.getLeaveTransactionService().delete(id);
		this.setInsert(true);
	}

	public void doCreateLeaveTransaction() {

		Employee employee = this.getLeaveTransactionService().findByEmployee(
				employeename);
		LeaveType leaveType = getLeaveTransactionService().findByLeaveType(
				this.leaveType, employee.getEmployeeType().getId());
		newLeaveTransaction.setEmployee(employee);
		newLeaveTransaction.setLeaveType(leaveType);
		newLeaveTransaction.setCreatedBy(actorUsers.getUsername());
		newLeaveTransaction.setCreationTime(new Date());
		getLeaveTransactionService().create(newLeaveTransaction);
		this.setInsert(true);
	}

	public void onRowSelect(SelectEvent event) {
		this.setSelectedLeaveTransaction((LeaveTransaction) event.getObject());
	}

	public String getEmployeename() {
		return employeename;
	}

	public void setEmployeename(String employeename) {
		this.employeename = employeename;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public List<String> getEmployeeList() {
		employeeList = this.getLeaveTransactionService().findEmployeeNames();
		return employeeList;
	}

	public void setEmployeeList(List<String> employeeList) {
		this.employeeList = employeeList;
	}

	public List<String> getLeaveTypeList() {

		try {

			if (this.getSelectedLeaveTransaction().getEmployee() != null) {
				System.out.println(this.selectedLeaveTransaction);
				leaveTypeList = (List<String>) this.getLeaveTransactionService().findLeaveTypes(selectedLeaveTransaction.getEmployee().getName().trim());
				return leaveTypeList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return leaveTypeList;
	}

	public void setLeaveTypeList(List<String> leaveTypeList) {
		this.leaveTypeList = leaveTypeList;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public boolean isInsert() {
		return isInsert;
	}

	public void setInsert(boolean isInsert) {
		this.isInsert = isInsert;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void correspondingList(AjaxBehaviorEvent event) {
	}

	public void selectedChangeList(AjaxBehaviorEvent event) {
	}

	public String getSearchLeaveType() {
		return searchLeaveType;
	}

	public void setSearchLeaveType(String searchLeaveType) {
		this.searchLeaveType = searchLeaveType;
	}

	public List<String> getLeaveList() {
		try {

			if (this.employeename != null) {

				leaveList = (List<String>) this.getLeaveTransactionService()
						.findLeaveTypes(employeename.trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return leaveList;
	}

	public void setLeaveList(List<String> leaveList) {
		this.leaveList = leaveList;
	}

}
