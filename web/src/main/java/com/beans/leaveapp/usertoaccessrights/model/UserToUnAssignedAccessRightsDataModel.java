package com.beans.leaveapp.usertoaccessrights.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.beans.common.security.accessrights.model.AccessRights;

public class UserToUnAssignedAccessRightsDataModel extends ListDataModel<List<String>> implements SelectableDataModel<String>   {

	public UserToUnAssignedAccessRightsDataModel() {
		
	}
	
	

	
	
	public UserToUnAssignedAccessRightsDataModel(List<List<String>> accessRightsList) {
		super(accessRightsList);
	}


	@Override
	public String getRowData(String rowKey) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object getRowKey(String object) {
		// TODO Auto-generated method stub
		return null;
	}


}
