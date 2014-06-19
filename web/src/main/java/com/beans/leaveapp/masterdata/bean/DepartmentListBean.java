package com.beans.leaveapp.masterdata.bean;

import java.io.Serializable;
import java.util.List;

import com.beans.leaveapp.department.model.Department;
import com.beans.leaveapp.department.service.DepartmentService;

public class DepartmentListBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Department> departmentList =  null;
	private DepartmentService departmentService;
	
	public List<Department> getDepartmentList() {
		departmentList = departmentService.findAll();
		return departmentList;
	}
	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}
	public DepartmentService getDepartmentService() {
		return departmentService;
	}
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	
}
