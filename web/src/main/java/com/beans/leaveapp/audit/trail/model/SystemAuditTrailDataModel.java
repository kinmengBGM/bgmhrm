package com.beans.leaveapp.audit.trail.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.beans.common.audit.model.SystemAuditTrail;

public class SystemAuditTrailDataModel extends ListDataModel<SystemAuditTrail>  implements SelectableDataModel<SystemAuditTrail>{

	
	public SystemAuditTrailDataModel(){
		
	}
	
    public SystemAuditTrailDataModel(List<SystemAuditTrail> data){
		super(data);
	}


	@Override
	public Object getRowKey(SystemAuditTrail object) {
		// TODO Auto-generated method stub
		return object.getId();
	}

	@Override
	public SystemAuditTrail getRowData(String rowKey) {
		List<SystemAuditTrail> employeeGradeList = (List<SystemAuditTrail>) getWrappedData();
        Integer rowKeyInt = Integer.parseInt(rowKey);
        for(SystemAuditTrail systemAuditTrail : employeeGradeList) {
            if(systemAuditTrail.getId() == rowKeyInt) {
                return systemAuditTrail;
            }
        }
        
        return null;
	}

}

