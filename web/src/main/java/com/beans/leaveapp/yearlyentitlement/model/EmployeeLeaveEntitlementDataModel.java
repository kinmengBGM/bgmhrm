package com.beans.leaveapp.yearlyentitlement.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class EmployeeLeaveEntitlementDataModel extends ListDataModel<EmployeeEntitlement> implements
		SelectableDataModel<EmployeeEntitlement> {

	
	EmployeeLeaveEntitlementDataModel(){
		
	}
	
	public EmployeeLeaveEntitlementDataModel(List<EmployeeEntitlement> data){
		super(data);
	}
	
	@Override
	public Object getRowKey(EmployeeEntitlement object) {
		// TODO Auto-generated method stub
		return object;
	}

	
	
	@Override
	public EmployeeEntitlement getRowData(String rowKey) {
		// TODO Auto-generated method stub
		return null;
	}

}
