package com.beans.leaveapp.masterdata.bean;

import java.io.Serializable;
import java.util.List;

import com.beans.leaveapp.employeetype.model.EmployeeType;
import com.beans.leaveapp.employeetype.service.EmployeeTypeService;


public class EmployeeTypeListBean implements Serializable{
	
	private List<EmployeeType> employeeTypeList = null;
	private EmployeeTypeService employeeTypeService;
	private static final long serialVersionUID = 1L;
	
	public List<EmployeeType> getEmployeeTypeList() {
		employeeTypeList = employeeTypeService.findAll();
		return employeeTypeList;
	}
	public void setEmployeeTypeList(List<EmployeeType> employeeTypeList) {
		this.employeeTypeList = employeeTypeList;
	}
	public EmployeeTypeService getEmployeeTypeService() {
		return employeeTypeService;
	}
	public void setEmployeeTypeService(EmployeeTypeService employeeTypeService) {
		this.employeeTypeService = employeeTypeService;
	}
	
}
