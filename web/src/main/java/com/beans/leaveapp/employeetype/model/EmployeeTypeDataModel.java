package com.beans.leaveapp.employeetype.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;


import com.beans.leaveapp.employeetype.model.EmployeeType;



public class EmployeeTypeDataModel extends ListDataModel<EmployeeType> implements SelectableDataModel<EmployeeType> {

	
	public EmployeeTypeDataModel(){
		
	}
	
	public EmployeeTypeDataModel(List<EmployeeType> data){
		super(data);
		
	}
	
	@Override
	public EmployeeType getRowData(String rowkey) {
		  List<EmployeeType> employeeTypeList = (List<EmployeeType>) getWrappedData();
	        Integer rowKeyInt = Integer.parseInt(rowkey);
	        for(EmployeeType employeeType : employeeTypeList) {
	            if(employeeType.getId() == rowKeyInt) {
	                return employeeType;
	            }
	        }
	        
	        return null;

		
	}

	@Override
	public Object getRowKey(EmployeeType employeeType) {
		// TODO Auto-generated method stub
		
		return employeeType.getId();
	}
	
	
    
}

