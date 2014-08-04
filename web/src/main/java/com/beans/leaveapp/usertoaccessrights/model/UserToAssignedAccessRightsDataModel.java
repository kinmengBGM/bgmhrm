package com.beans.leaveapp.usertoaccessrights.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.beans.common.security.usertoaccessrights.model.UserToAccessRights;


public class UserToAssignedAccessRightsDataModel extends ListDataModel<UserToAccessRights> implements SelectableDataModel<UserToAccessRights>{

	
	UserToAssignedAccessRightsDataModel(){
		
	} 
	
public UserToAssignedAccessRightsDataModel(List<UserToAccessRights> data){

		
		super(data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserToAccessRights getRowData(String rowKey) {
		
		  List<UserToAccessRights> userToAccessRightsList = (List<UserToAccessRights>)getWrappedData();
		  Integer rowKeyInt = Integer.parseInt(rowKey);
	      for(UserToAccessRights userToAccessRights : userToAccessRightsList) {
	          if(userToAccessRights.getId() == rowKeyInt) {
	              return userToAccessRights;
	          }
	      }

		return null;
	}
	
	@Override
	public Object getRowKey(UserToAccessRights userToAccessRights) {
		
		return userToAccessRights.getId();
	}	


}
