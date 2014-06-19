package com.beans.leaveapp.leavetype.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class LeaveTypeDataModel extends ListDataModel<LeaveType> implements SelectableDataModel<LeaveType> {  

    public LeaveTypeDataModel() {
    }

    public LeaveTypeDataModel(List<LeaveType> data) {
        super(data);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public LeaveType getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
        
        List<LeaveType> leaveTypeList = (List<LeaveType>)getWrappedData();// To get object representing the data wrapped by ListDataModel  
        Integer rowKeyInt = Integer.parseInt(rowKey);
        for(LeaveType leaveType : leaveTypeList) {
            if(leaveType.getId() == rowKeyInt) {
                return leaveType;
            }
        }
        
        return null;
    }

    @Override
    public Object getRowKey(LeaveType leaveType) {
        return leaveType.getId();
    }
    
}
