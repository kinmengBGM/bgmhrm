package com.beans.leaveapp.usertorole.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.beans.common.security.users.model.Users;

public class UserToRoleDataModel extends ListDataModel<Users> implements SelectableDataModel<Users> {
	
   public UserToRoleDataModel(){  	   
   }
   
   public UserToRoleDataModel(List<Users> data) {
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
