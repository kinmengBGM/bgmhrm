package com.beans.leaveapp.yearlyentitlement.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import com.beans.common.audit.service.AuditTrail;
import com.beans.common.security.users.model.Users;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.service.EmployeeService;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.beans.leaveapp.leavetype.service.LeaveTypeService;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitleDataModel;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementService;

public class YearlyEntitlementManagementBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private YearlyEntitlementService yearlyEntitlementService;
	private YearlyEntitleDataModel yearlyEntitlementDataModel;
	// private YearlyEntitleDataModel y;
	private YearlyEntitlement yearlyEntitlement = new YearlyEntitlement();
	private YearlyEntitlement selectedYearlyEntitlement = new YearlyEntitlement();
	private boolean employeeYearlyEntitlement = false;
	private boolean InsertDelete = false;
	private List<String> employeeList;
	private List<String> leaveType;
	private Employee employee;
	private EmployeeService employeeService;
	private LeaveTypeService leaveTypeService;
	private String employeeName = this.getEmployeeName();
	private String leaveTypeName = this.getLeaveTypeName();
	private String searchEmployeeName,searchLeaveType;;
	private String userName;
	List<YearlyEntitlement> listOfEmployeeYearlyEntitlement;
	private Users actorUsers;
	private List<YearlyEntitlement> yearlyEntitlementList;
	private YearlyEntitleDataModel employeeYearlyEntitlementDataModel;
	private List<String> correspondingList;

	private AuditTrail auditTrail;

	public AuditTrail getAuditTrail() {
		return auditTrail;
	}

	public void setAuditTrail(AuditTrail auditTrail) {
		this.auditTrail = auditTrail;
	}

	public YearlyEntitlementService getYearlyEntitlementService() {
		return yearlyEntitlementService;
	}

	public void setYearlyEntitlementService(
			YearlyEntitlementService yearlyEntitlementService) {
		this.yearlyEntitlementService = yearlyEntitlementService;
	}

	public YearlyEntitlement getYearlyEntitlement() {
		return yearlyEntitlement;
	}

	public void setYearlyEntitlement(YearlyEntitlement yearlyEntitlement) {
		this.yearlyEntitlement = yearlyEntitlement;
	}

	public List<YearlyEntitlement> getYearlyEntitlementList() throws Exception {
		if (yearlyEntitlementList == null || InsertDelete == true) {

			yearlyEntitlementList = (List<YearlyEntitlement>) getYearlyEntitlementService()
					.findAll();

		}
		return yearlyEntitlementList;
	}

	public YearlyEntitleDataModel getYearlyEntitlementDataModel()
			throws Exception {
		if (yearlyEntitlementDataModel == null || InsertDelete == true) {

			yearlyEntitlementDataModel = new YearlyEntitleDataModel(
					this.getYearlyEntitlementList());
		}
		return yearlyEntitlementDataModel;
	}

	public void setYearlyEntitlementDataModel(
			YearlyEntitleDataModel yearlyEntitlementDataModel) {
		this.yearlyEntitlementDataModel = yearlyEntitlementDataModel;
	}

	public YearlyEntitlement getSelectedYearlyEntitlement() {
		return selectedYearlyEntitlement;
	}

	public void setSelectedYearlyEntitlement(
			YearlyEntitlement selectedYearlyEntitlement) {
		this.selectedYearlyEntitlement = selectedYearlyEntitlement;
	}

	public String getLeaveTypeName() {
		return leaveTypeName;
	}

	public void setLeaveTypeName(String leaveTypeName) {
		this.leaveTypeName = leaveTypeName;
	}

	public void doUpdateYearlyEntitlement() {
		try {
			selectedYearlyEntitlement.setLastModifiedBy(actorUsers.getUsername());
			selectedYearlyEntitlement.setLastModifiedTime(new java.util.Date());
			getYearlyEntitlementService().update(selectedYearlyEntitlement);
			setInsertDelete(true);
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error",
					"Yearly Entitle With id: "
							+ selectedYearlyEntitlement.getId() + " not found!");

			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void onRowSelect(SelectEvent event) {
		this.setSelectedYearlyEntitlement((YearlyEntitlement) event.getObject());
		System.out.println(this.getYearlyEntitlement().getId());
		FacesMessage msg = new FacesMessage("Yearly Entitlement Seleted");

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void doDeleteYearlyEntitlement() {
		try {
			int id = selectedYearlyEntitlement.getId();
			this.getYearlyEntitlementService().delete(id);
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error",
					"Yearly Entitle With id: "
							+ selectedYearlyEntitlement.getId() + " not found!");

			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		setInsertDelete(true);
	}

	public void doCreateYearlyEntitlement() {

		Employee employee = getYearlyEntitlementService().findByEmployee(employeeName);
		System.out.println(employeeName + " " + this.yearlyEntitlement);
		LeaveType leaveType = getYearlyEntitlementService().findByLeaveType(
				leaveTypeName,employee.getEmployeeType().getId());
		System.out.println(employee.getEmployeeType().getId()+" "+leaveTypeName);
		
		yearlyEntitlement.setEmployee(employee);
		yearlyEntitlement.setLeaveType(leaveType);
		yearlyEntitlement.setCreatedBy(actorUsers.getUsername());
		yearlyEntitlement.setCreationTime(new java.util.Date());
		this.getYearlyEntitlementService().create(yearlyEntitlement);

		setInsertDelete(true);

		// auditTrail.log(SystemAuditTrailActivity.CREATED,
		// SystemAuditTrailLevel.INFO,
		// this.getActorUsers().getId(),getActorUsers().getUsername(),
		// getActorUsers().getUsername()+" created yearly Entitlement: "+yearlyEntitlement.getEmployee().getName());
	}

	public boolean isInsertDelete() {
		return InsertDelete;
	}

	public void setInsertDelete(boolean insertDelete) {
		InsertDelete = insertDelete;
	}

	public List<String> getEmployeeList() {
		List<String> employeeNames = (List<String>) getYearlyEntitlementService()
				.employeeNames();
		return employeeNames;

	}

	public void setEmployeeList(List<String> employeeList) {
		this.employeeList = employeeList;
	}

	public List<String> getLeaveType() {
		try {
			if (employeeName != null) {
				Employee employee = this.getYearlyEntitlementService()
						.findByEmployee(employeeName);
				if (employee != null) {
					leaveType = getYearlyEntitlementService().findLeaveTypes(
							employee.getEmployeeType().getId());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return leaveType;
	}

	public void setLeaveType(List<String> leaveType) {
		this.leaveType = leaveType;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void search() {
		try {
			if ((getSearchEmployeeName().trim().equals(""))
					&& (getSearchLeaveType().trim().equals(""))) {
				this.yearlyEntitlementList = null;
				this.yearlyEntitlementDataModel = null;
			} else {

				yearlyEntitlementList = this.getYearlyEntitlementService()
						.findByEmployeeOrfindByLeaveTypeOrBoth(
								this.getSearchEmployeeName(), this.getSearchLeaveType());

				this.yearlyEntitlementDataModel = null;
				// if(leaveEntitlementList != null){
				// auditTrail.log(SystemAuditTrailActivity.ACCESSED,
				// SystemAuditTrailLevel.INFO,
				// actorUsers.getId(),actorUsers.getUsername(),
				// actorUsers.getUsername()+" searching Entitlement of : "+getEmployeeName());
				// }

				// auditTrail.log(SystemAuditTrailActivity.ACCESSED,
				// SystemAuditTrailLevel.INFO,
				// actorUsers.getId(),actorUsers.getUsername(),
				// actorUsers.getUsername()+" searching Entitlement of : "+getEmployeeName());
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object getUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	public LeaveTypeService getLeaveTypeService() {
		return leaveTypeService;
	}

	public void setLeaveTypeService(LeaveTypeService leaveTypeService) {
		this.leaveTypeService = leaveTypeService;
	}

	public Users getActorUsers() {
		return actorUsers;
	}

	public void setActorUsers(Users actorUsers) {
		this.actorUsers = actorUsers;
	}

	public void setYearlyEntitlementList(
			List<YearlyEntitlement> yearlyEntitlementList) {
		this.yearlyEntitlementList = yearlyEntitlementList;
	}

	public List<YearlyEntitlement> getListOfEmployeeYearlyEntitlement() {
		try {
			System.out.println(employee.getId());
			if (employee.getId() > 0) {
				this.listOfEmployeeYearlyEntitlement = this
						.getYearlyEntitlementService().findByEmployeeId(
								employee.getId());
				this.setEmployeeYearlyEntitlement(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listOfEmployeeYearlyEntitlement;
	}

	public void setListOfEmployeeYearlyEntitlement(
			List<YearlyEntitlement> listOfEmployeeYearlyEntitlement) {
		this.listOfEmployeeYearlyEntitlement = listOfEmployeeYearlyEntitlement;
	}

	/*
	 * public EmployeeYearlyEntitlementDataModel
	 * getEmployeeYearlyEntitlementDataModel() { try{
	 * employeeYearlyEntitlementDataModel = new
	 * EmployeeYearlyEntitlementDataModel(getListOfEmployeeYearlyEntitlement());
	 * 
	 * }catch(Exception e){ e.printStackTrace(); } return
	 * employeeYearlyEntitlementDataModel; }
	 * 
	 * public void setEmployeeYearlyEntitlementDataModel(
	 * EmployeeYearlyEntitlementDataModel employeeYearlyEntitlementDataModel) {
	 * this.employeeYearlyEntitlementDataModel =
	 * employeeYearlyEntitlementDataModel; }
	 */

	public boolean isEmployeeYearlyEntitlement() {
		return employeeYearlyEntitlement;
	}

	public void setEmployeeYearlyEntitlement(boolean employeeYearlyEntitlement) {
		this.employeeYearlyEntitlement = employeeYearlyEntitlement;
	}

	public YearlyEntitleDataModel getEmployeeYearlyEntitlementDataModel() {

		if (getListOfEmployeeYearlyEntitlement().size() > 0) {
			employeeYearlyEntitlementDataModel = new YearlyEntitleDataModel(
					getListOfEmployeeYearlyEntitlement());
			return employeeYearlyEntitlementDataModel;
		}
		return employeeYearlyEntitlementDataModel;

	}

	public void setEmployeeYearlyEntitlementDataModel(
			YearlyEntitleDataModel employeeYearlyEntitlementDataModel) {
		this.employeeYearlyEntitlementDataModel = employeeYearlyEntitlementDataModel;
	}

	public List<String> correspondingList(AjaxBehaviorEvent event) {

		return correspondingList;
	}

	
	public String getSearchEmployeeName() {
		return searchEmployeeName;
	}

	public void setSearchEmployeeName(String searchEmployeeName) {
		this.searchEmployeeName = searchEmployeeName;
	}

	public String getSearchLeaveType() {
		return searchLeaveType;
	}

	public void setSearchLeaveType(String searchLeaveType) {
		this.searchLeaveType = searchLeaveType;
	}


}