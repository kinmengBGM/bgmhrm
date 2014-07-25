package com.beans.leaveapp.employeegrade.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class EmployeeGradeDataModel extends ListDataModel<EmployeeGrade> implements SelectableDataModel<EmployeeGrade> {

	
	public EmployeeGradeDataModel(){
		
	}
	
	public EmployeeGradeDataModel(List<EmployeeGrade> data){
		super(data);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EmployeeGrade getRowData(String rowkey) {
		 List<EmployeeGrade> employeeGradeList = (List<EmployeeGrade>) getWrappedData();
	        Integer rowKeyInt = Integer.parseInt(rowkey);
	        for(EmployeeGrade employeeGrade : employeeGradeList) {
	            if(employeeGrade.getId() == rowKeyInt) {
	                return employeeGrade;
	            }
	        }
	        
	        return null;

		
	}

	@Override
	public Object getRowKey(EmployeeGrade employeeGrade) {
		// TODO Auto-generated method stub
		
		return employeeGrade.getId();
	}
	
	
    
}

