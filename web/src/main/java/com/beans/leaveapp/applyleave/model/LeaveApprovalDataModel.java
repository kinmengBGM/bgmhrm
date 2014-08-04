package com.beans.leaveapp.applyleave.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;

public class LeaveApprovalDataModel extends ListDataModel<LeaveTransaction> implements SelectableDataModel<LeaveTransaction> {

	
	public LeaveApprovalDataModel(){
		
	}
	
	public LeaveApprovalDataModel(List<LeaveTransaction> data){
		super(data);
		
	}
	
	@Override
	public LeaveTransaction getRowData(String rowkey) {
		  List<LeaveTransaction> leaveApprovalList = (List<LeaveTransaction>) getWrappedData();
	        Long rowKeyInt = Long.parseLong(rowkey);
	        for(LeaveTransaction leaveApproval : leaveApprovalList) {
	            if(leaveApproval.getTaskId() == rowKeyInt) {
	                return leaveApproval;
	            }
	        }
	        
	        return null;

		
	}

	@Override
	public Object getRowKey(LeaveTransaction leaveApproval) {
		
		return leaveApproval.getTaskId();
	}

}
