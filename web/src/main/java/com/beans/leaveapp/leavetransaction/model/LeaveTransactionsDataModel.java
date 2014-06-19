package com.beans.leaveapp.leavetransaction.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class LeaveTransactionsDataModel extends ListDataModel<LeaveTransaction> implements
		SelectableDataModel<LeaveTransaction> {

	public LeaveTransactionsDataModel(){
		
	}
	
	public LeaveTransactionsDataModel(List<LeaveTransaction> data){
		super(data);
	}
	@Override
	public Object getRowKey(LeaveTransaction object) {
		// TODO Auto-generated method stub
		return object;
	}

	@Override
	public LeaveTransaction getRowData(String rowKey) {
		// TODO Auto-generated method stub
		return null;
	}

}
