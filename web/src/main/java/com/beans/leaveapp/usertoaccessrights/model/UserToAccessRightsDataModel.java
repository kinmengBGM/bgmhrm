package com.beans.leaveapp.usertoaccessrights.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.beans.common.security.users.model.Users;
import com.beans.common.security.usertoaccessrights.model.UserToAccessRights;

public class UserToAccessRightsDataModel extends ListDataModel<Users> implements SelectableDataModel<Users> {

	  public UserToAccessRightsDataModel(){
		  
	  }
	  
	  public UserToAccessRightsDataModel(List<Users> data){	  
		  super(data);
	  }
			
	  					@SuppressWarnings("unchecked")
						@Override
						public Users getRowData(String rowKey) {
						    
							  List<Users> userList = (List<Users>)getWrappedData();
							  Integer rowKeyInt = Integer.parseInt(rowKey);
						      for(Users users : userList) {
						          if(users.getId() == rowKeyInt) {
						              return users;
						          }
						      }
							return null;
						}
						
						@Override
						public Object getRowKey(Users users) {
						
							return users.getId();
						}
  
  

}
