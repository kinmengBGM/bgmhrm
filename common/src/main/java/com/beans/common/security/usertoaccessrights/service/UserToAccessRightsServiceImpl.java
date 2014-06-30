package com.beans.common.security.usertoaccessrights.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import com.beans.common.security.accessrights.model.AccessRights;
import com.beans.common.security.accessrights.repository.AccessRightsRepository;
import com.beans.common.security.usertoaccessrights.model.UserToAccessRights;
import com.beans.common.security.usertoaccessrights.repository.UserToAccessRightsRepository;

@Service
public class UserToAccessRightsServiceImpl implements UserToAccessRightsService{
	
	@Resource
	private UserToAccessRightsRepository userToAccessRightsRepository;

	@Resource
	private AccessRightsRepository accessRightsRepository;


	@Override
	public UserToAccessRights delete(int id) {
		UserToAccessRights userToAccessRights = userToAccessRightsRepository.findOne(id);
		if(userToAccessRights != null){
		userToAccessRights.setDeleted(true);
		userToAccessRightsRepository.save(userToAccessRights);			
		}
		return userToAccessRights;
	}

	@Override
	public List<UserToAccessRights> findAll() {
		List<UserToAccessRights> userToAccessRightsList = userToAccessRightsRepository.findByIsDeleted(0);
		return userToAccessRightsList;
	}

	@Override
	public UserToAccessRights update(UserToAccessRights userToAccessRights)	{

	//	UserToAccessRights userAccessRightsToBeUpdated = new UserToAccessRights();
		UserToAccessRights userAccessRightsToBeUpdated = userToAccessRightsRepository.findOne(userToAccessRights.getId());
		if(userAccessRightsToBeUpdated != null){
		userAccessRightsToBeUpdated.setId(userToAccessRights.getId());
		userAccessRightsToBeUpdated.setAccessRights(userToAccessRights.getAccessRights());
		userAccessRightsToBeUpdated.setUsers(userToAccessRights.getUsers());
		userAccessRightsToBeUpdated.setEnabled(userToAccessRights.isEnabled());
		userAccessRightsToBeUpdated.setDeleted(userToAccessRights.isDeleted());
		userToAccessRightsRepository.save(userAccessRightsToBeUpdated);
		}			
		return userAccessRightsToBeUpdated;
	}



/*	@Override
	public List<AssignedAccessRights> findAssignedAccessRights(int id) {
	    List<AssignedAccessRights> assignedAccessRightsList = new ArrayList<AssignedAccessRights>();	    
	    List<UserToAccessRights> userToAccessRightsList = userToAccessRightsRepository.findByUserId(id);
	    for(UserToAccessRights userToAccessRights: userToAccessRightsList){
	    	String accessRights = userToAccessRights.getAccessRights().getAccessRights();
	    	Boolean enabled= userToAccessRights.isEnabled();
	    	
	    	assignedAccessRightsList.add(new AssignedAccessRights(accessRights,enabled));
	    }
		return assignedAccessRightsList;
	}
>>>>>>> 47141d0f367cb2b7f042077bb27af40d3659e1ee*/

	@Override
	public List<UserToAccessRights> findByUserId(int userId) {
		List<UserToAccessRights> userToAccessRightsList = userToAccessRightsRepository.findByUserId(userId);
		return userToAccessRightsList;
	}

	@Override
	public List<AccessRights> findAllAccessRights() {
		List<AccessRights> accessRightsList = accessRightsRepository.findByIsDeleted(0);
		return accessRightsList;
	}

	@Override
	public UserToAccessRights create(UserToAccessRights userToAccessRights) {
		
		UserToAccessRights userToAccessRightsToBeCreated = userToAccessRights;
		 
		 userToAccessRightsToBeCreated.setId(userToAccessRights.getId());
		 userToAccessRightsToBeCreated.setAccessRights(userToAccessRights.getAccessRights());
		 userToAccessRightsToBeCreated.setUsers(userToAccessRights.getUsers());
		 userToAccessRightsToBeCreated.setEnabled(userToAccessRights.isEnabled());
		 userToAccessRights.setDeleted(userToAccessRights.isDeleted());
		 userToAccessRightsRepository.save(userToAccessRightsToBeCreated);
		return userToAccessRightsToBeCreated;
	}

	@Override
	public UserToAccessRights findByAccessRight(UserToAccessRights userToAccessRights) {
		UserToAccessRights userToAccessRights1 = userToAccessRightsRepository.findByAccessRight(userToAccessRights.getUsers().getId(),userToAccessRights.getAccessRights().getId());
		return userToAccessRights1;
	}	
	
	}	
	
	



