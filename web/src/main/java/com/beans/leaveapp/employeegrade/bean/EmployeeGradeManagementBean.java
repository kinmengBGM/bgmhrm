package com.beans.leaveapp.employeegrade.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;

import com.beans.common.security.users.model.Users;
import com.beans.exceptions.BSLException;
import com.beans.leaveapp.employeegrade.model.EmployeeGrade;
import com.beans.leaveapp.employeegrade.model.EmployeeGradeDataModel;
import com.beans.leaveapp.employeegrade.service.EmployeeGradeNotFound;
import com.beans.leaveapp.employeegrade.service.EmployeeGradeService;
import com.beans.leaveapp.refresh.Refresh;
import com.beans.leaveapp.web.bean.BaseMgmtBean;

public class EmployeeGradeManagementBean extends BaseMgmtBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	// EmployeeGradeRepository employeeGradeRepository;
	EmployeeGradeService employeeGradeService;
	private List<EmployeeGrade> employeeGradeList;
	private EmployeeGradeDataModel employeeGradeDataModel;
	private EmployeeGrade newEmployeeGrade = new EmployeeGrade();
	private EmployeeGrade selectedEmployeeGrade = new EmployeeGrade();
	private boolean insertDeleted = false;
	private String searchName;

	private Users actorUsers;

	public Users getActorUsers() {
		return actorUsers;
	}

	public void setActorUsers(Users actorUsers) {
		this.actorUsers = actorUsers;
	}

	public EmployeeGradeService getEmployeeGradeService() {
		return employeeGradeService;
	}

	public EmployeeGrade getNewEmployeeGrade() {
		return newEmployeeGrade;
	}

	public void setNewEmployeeGrade(EmployeeGrade newEmployeeGrade) {
		this.newEmployeeGrade = newEmployeeGrade;
	}

	public EmployeeGrade getSelectedEmployeeGrade() {
		return selectedEmployeeGrade;
	}

	public void setSelectedEmployeeGrade(EmployeeGrade selectedEmployeeGrade) {
		this.selectedEmployeeGrade = selectedEmployeeGrade;
	}

	public boolean isInsertDeleted() {
		return insertDeleted;
	}

	public void setInsertDelete(boolean insertDeleted) {
		this.insertDeleted = insertDeleted;
	}

	public void setEmployeeGradeService(
			EmployeeGradeService employeeGradeService) {
		this.employeeGradeService = employeeGradeService;
	}

	public List<EmployeeGrade> getEmployeeGradeList() {
		if (employeeGradeList == null || insertDeleted == true) {
			try {
				employeeGradeList = getEmployeeGradeService().findAll();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return employeeGradeList;
	}

	public EmployeeGradeDataModel getEmployeeGradeDataModel() {
		if (employeeGradeDataModel == null || insertDeleted == true) {
			System.out.println(getEmployeeGradeList().size());
			employeeGradeDataModel = new EmployeeGradeDataModel(
					getEmployeeGradeList());

		}

		return employeeGradeDataModel;
	}

	public void setEmployeeGradeDataModel(
			EmployeeGradeDataModel employeeGradeDataModel) {
		this.employeeGradeDataModel = employeeGradeDataModel;
	}

	public void setEmployeeGradeList(List<EmployeeGrade> employeeGradeList) {
		this.employeeGradeList = employeeGradeList;
	}

	public void doCreateEmployeeGrade() throws EmployeeGradeNotFound {
		try{
			newEmployeeGrade.setDeleted(false);
			newEmployeeGrade.setCreatedBy(actorUsers.getUsername());
			newEmployeeGrade.setCreationTime(new java.util.Date());
			getEmployeeGradeService().create(newEmployeeGrade);
			setInsertDelete(true);
			newEmployeeGrade = new EmployeeGrade();
			new Refresh().refreshPage();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info",getExcptnMesProperty("info.empgrade.create")));
		}catch(BSLException e){
			FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
		}
	}

	public void doUpdateEmployeeGrade() throws EmployeeGradeNotFound {
		try {
				log.info("New name:" + selectedEmployeeGrade.getName());
				log.info("ID: " + selectedEmployeeGrade.getId());
				log.info("Username in session : "+ actorUsers.getUsername());
				selectedEmployeeGrade.setLastModifiedBy(actorUsers.getUsername());
				getEmployeeGradeService().update(selectedEmployeeGrade);
				this.setInsertDelete(true);
				new Refresh().refreshPage();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info",getExcptnMesProperty("info.empgrade.update")));
			}catch(BSLException e){
				FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
				msg.setSeverity(FacesMessage.SEVERITY_INFO);
		        FacesContext.getCurrentInstance().addMessage(null, msg); 
		}
	}

	public void onRowSelect(SelectEvent event) {
		setSelectedEmployeeGrade((EmployeeGrade) event.getObject());
		FacesMessage msg = new FacesMessage("Employee Grade Selected",
				selectedEmployeeGrade.getName());

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void doDeleteEmployeeGrade() throws Exception, EmployeeGradeNotFound {
		try{
		getEmployeeGradeService().delete(selectedEmployeeGrade.getId());
		setInsertDelete(true);
		new Refresh().refreshPage();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Info",getExcptnMesProperty("info.empgrade.delete")));
		}catch(BSLException e){
			FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
		}
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	

}
