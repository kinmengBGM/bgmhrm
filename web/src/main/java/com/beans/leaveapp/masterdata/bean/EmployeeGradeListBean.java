package com.beans.leaveapp.masterdata.bean;

import java.io.Serializable;
import java.util.List;

import com.beans.leaveapp.employeegrade.model.EmployeeGrade;
import com.beans.leaveapp.employeegrade.service.EmployeeGradeService;

public class EmployeeGradeListBean implements Serializable{

	private List<EmployeeGrade> employeeGradeList = null;
	private EmployeeGradeService employeeGradeService;
	private static final long serialVersionUID = 1L;
	
	
	public List<EmployeeGrade> getEmployeeGradeList() throws Throwable {
			
		employeeGradeList = employeeGradeService.findAll();
		return employeeGradeList;
	}
	public void setEmployeeGradeList(List<EmployeeGrade> employeeGradeList) {
		this.employeeGradeList = employeeGradeList;
	}
	
	
	public EmployeeGradeService getEmployeeGradeService() {
		return employeeGradeService;
	}
	public void setEmployeeGradeService(
			EmployeeGradeService employeeGradeService) {
		this.employeeGradeService = employeeGradeService;
	}
	
}
