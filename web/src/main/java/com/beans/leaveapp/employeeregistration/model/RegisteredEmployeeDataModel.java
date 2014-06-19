package com.beans.leaveapp.employeeregistration.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.beans.leaveapp.employee.model.RegisteredEmployee;

public class RegisteredEmployeeDataModel extends ListDataModel<RegisteredEmployee> implements SelectableDataModel<RegisteredEmployee> {

	
	public RegisteredEmployeeDataModel(){
		
	}
	
	public RegisteredEmployeeDataModel(List<RegisteredEmployee> data){
		super(data);
		
	}
	
	@Override
	public RegisteredEmployee getRowData(String rowkey) {
		  List<RegisteredEmployee> registeredEmployeeList = (List<RegisteredEmployee>) getWrappedData();
	        Integer rowKeyInt = Integer.parseInt(rowkey);
	        for(RegisteredEmployee registeredEmployee : registeredEmployeeList) {
	            if(registeredEmployee.getTaskId() == rowKeyInt) {
	                return registeredEmployee;
	            }
	        }
	        
	        return null;

		
	}

	@Override
	public Object getRowKey(RegisteredEmployee registeredEmployee) {
		// TODO Auto-generated method stub
		
		return registeredEmployee.getTaskId();
	}

}
