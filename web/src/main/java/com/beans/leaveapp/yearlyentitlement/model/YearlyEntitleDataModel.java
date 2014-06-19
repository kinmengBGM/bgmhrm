package com.beans.leaveapp.yearlyentitlement.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class YearlyEntitleDataModel extends ListDataModel<YearlyEntitlement> implements
		SelectableDataModel<YearlyEntitlement> {

	public YearlyEntitleDataModel(){
		
	}
	
	public YearlyEntitleDataModel(List<YearlyEntitlement> data){
		super(data);
	}
	
	
	@Override
	public Object getRowKey(YearlyEntitlement object) {
		
		return object.getId();
	}

	@Override
	public YearlyEntitlement getRowData(String rowKey) {
		// TODO Auto-generated method stub
		return null;
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public LeaveEntitlement getRowData(String rowKey) {
		List<YearlyEntitlement>   yearlyEntitlementList = (List<YearlyEntitlement>)getWrappedData();// To get object representing the data wrapped by ListDataModel  
        Integer rowKeyInt = Integer.parseInt(rowKey);
        for(YearlyEntitlement yearlyEntitlement : yearlyEntitlementList) {
            if(yearlyEntitlement.getId() == rowKeyInt) {
                return leaveEntitlement;
            }
        }
        
		return null;
	}*/

}
