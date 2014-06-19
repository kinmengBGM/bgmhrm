package com.beans.leaveapp.employeeprofile.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.beans.leaveapp.employee.model.Employee;


public class EmployeeProfileDataModel extends ListDataModel<Employee> implements SelectableDataModel<Employee> {

	
	public EmployeeProfileDataModel(){
		
	}
	
	public EmployeeProfileDataModel(List<Employee> data){
		super(data);
		
	}
	
	@Override
	public Employee getRowData(String rowkey) {
		  List<Employee> employeeList = (List<Employee>) getWrappedData();
	        Integer rowKeyInt = Integer.parseInt(rowkey);
	        for(Employee employee : employeeList) {
	            if(employee.getId() == rowKeyInt) {
	                return employee;
	            }
	        }
	        
	        return null;
		
	}

	@Override
	public Object getRowKey(Employee employee) {
		// TODO Auto-generated method stub
		
		return employee.getId();
	}
	
	
    
}

