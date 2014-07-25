package com.beans.leaveapp.department.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;


public class DepartmentDataModel extends ListDataModel<Department> implements SelectableDataModel<Department> {

	
	public DepartmentDataModel(){
		
	}
	
	public DepartmentDataModel(List<Department> data){
		super(data);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Department getRowData(String rowkey) {
		  List<Department> departmentList = (List<Department>) getWrappedData();
	        Integer rowKeyInt = Integer.parseInt(rowkey);
	        for(Department department : departmentList) {
	            if(department.getId() == rowKeyInt) {
	                return department;
	            }
	        }
	        
	        return null;

		
	}

	@Override
	public Object getRowKey(Department department) {
		// TODO Auto-generated method stub
		
		return department.getId();
	}


	
    
}