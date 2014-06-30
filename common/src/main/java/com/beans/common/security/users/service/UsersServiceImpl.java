package com.beans.common.security.users.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beans.common.security.accessrights.model.AccessRights;
import com.beans.common.security.role.model.Role;
import com.beans.common.security.role.service.RoleNotFound;
import com.beans.common.security.role.service.RoleService;
import com.beans.common.security.users.model.Users;
import com.beans.common.security.users.repository.UsersRepository;
import com.beans.common.security.usertoaccessrights.model.UserToAccessRights;
import com.beans.common.security.usertoaccessrights.service.UserToAccessRightsService;


@Service
public class UsersServiceImpl implements UsersService {
	

	@Resource
	private UsersRepository usersRepository;
	
	private RoleService roleService;
	private UserToAccessRightsService userToAccessRightsService;
	
	@Override
	@Transactional
	public Users create(Users users) {	
		 Users usersToBeCreated = users;
		 PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		 String hashedPassword = passwordEncoder.encode(usersToBeCreated.getPassword());
		 usersToBeCreated.setPassword(hashedPassword);
		return usersRepository.save(usersToBeCreated);
	}

	@Override
	@Transactional(rollbackFor=UsersNotFound.class)
	public Users delete(int id) throws UsersNotFound {	
		
		return null;
	}

	@Override
	public List<Users> findAll() {
			List<Users> usersList = usersRepository.findByIsEnabled(true);
		return usersList;
	}

	@Override
	@Transactional(rollbackFor=UsersNotFound.class)
	public Users update(Users users) throws UsersNotFound {
		Users usersToBeUpdated = usersRepository.findOne(users.getId());
		
		if(usersToBeUpdated == null)
			 throw new UsersNotFound();
		usersToBeUpdated.setUsername(users.getUsername());
		usersToBeUpdated.setPassword(users.getPassword());
		usersToBeUpdated.setEnabled(users.isEnabled());
		usersToBeUpdated.setLastModifiedBy(users.getLastModifiedBy());
		usersToBeUpdated.setLastModifiedTime(users.getLastModifiedTime());
		Set<Role> roleSet = new HashSet<Role>();
		roleSet.addAll(users.getUserRoles());
		usersToBeUpdated.setUserRoles(roleSet);
		usersRepository.save(usersToBeUpdated);		
		return usersToBeUpdated;
	}

	@Override	
	public Users registerUser(Users users) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(users.getPassword());
		users.setPassword(hashedPassword);
		Users createdUsers = usersRepository.save(users);
		try {
			Role userRole = roleService.findByRole("ROLE_USER");
			Role employeeRole = roleService.findByRole("ROLE_EMPLOYEE");
			Set<Role> roleSet = new HashSet<Role>();
			roleSet.add(userRole);
			roleSet.add(employeeRole);
			createdUsers.setUserRoles(roleSet);
		} catch(RoleNotFound e) {
			e.printStackTrace();
		}
		
		return usersRepository.save(createdUsers);
	}
	
	@Override	
	public Users findById(int id) throws UsersNotFound {
		Users users = usersRepository.findOne(id);
		
		if(users == null)
			throw new UsersNotFound();
		
		return users;
	}
	
	
	
	@Override
	public Users findByUsername(String username) throws UsersNotFound {
		Users users = usersRepository.findByUsername(username);
		
		if(users == null) {
			throw new UsersNotFound();
		}
		
		return users;
	}

	@Override
	public List<Users> findUsersByUsername(String username) {
			String usernameSearchTerm = "%" + username + "%";
		return usersRepository.findByUsernameLike(usernameSearchTerm);
	}

	@Override
	public HashSet<String> getAccessRightsMapForUser(int userId) throws UsersNotFound {
		HashSet<String> accessRightsSet = new HashSet<String>();
		
		Users user = findById(userId);
		
		if(user == null) {
			throw new UsersNotFound();
		}
		
		
		Set<Role> roleSet =  user.getUserRoles();
		
		Iterator<Role> roleIterator = roleSet.iterator();
		while(roleIterator.hasNext()) {
			Role currentRole = roleIterator.next();
			Set<AccessRights> currentRoleAccessRightSet = currentRole.getAccessRights();
			Iterator<AccessRights> currentRoleAccessRightIterator = currentRoleAccessRightSet.iterator();
			while(currentRoleAccessRightIterator.hasNext()) {
				AccessRights currentAccessRights = currentRoleAccessRightIterator.next();
				accessRightsSet.add(currentAccessRights.getAccessRights());
			}
		}
		
		List<UserToAccessRights> userToAccessRightList = userToAccessRightsService.findByUserId(userId);
		Iterator<UserToAccessRights> userToAccessRightIterator = userToAccessRightList.iterator();
		while(userToAccessRightIterator.hasNext()) {
			UserToAccessRights currentUserToAccessRights = userToAccessRightIterator.next();
			AccessRights currentAccessRights = currentUserToAccessRights.getAccessRights();
			if(!currentUserToAccessRights.isEnabled() && accessRightsSet.contains(currentAccessRights.getAccessRights())) {
				accessRightsSet.remove(currentAccessRights.getAccessRights());
			} else {
				accessRightsSet.add(currentAccessRights.getAccessRights());
			}
		}
		
		return accessRightsSet;
	}
	
	

	@Override
	public void changePassword(Users users, String oldPassword,
			String newPassword) throws ChangePasswordException, UsersNotFound{
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if(!passwordEncoder.matches(oldPassword, users.getPassword())) {
			throw new ChangePasswordException("Invalid old password");
		}
		
		String hashedPassword = passwordEncoder.encode(newPassword);		
		users.setPassword(hashedPassword);		
		update(users);		
	}

	public RoleService getRoleService() {
		return roleService;
	}
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	
	public UserToAccessRightsService getUserToAccessRightsService() {
		return userToAccessRightsService;
	}
	public void setUserToAccessRightsService(
			UserToAccessRightsService userToAccessRightsService) {
		this.userToAccessRightsService = userToAccessRightsService;
	}
	 
}
