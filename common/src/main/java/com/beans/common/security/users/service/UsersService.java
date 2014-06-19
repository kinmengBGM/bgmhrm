package com.beans.common.security.users.service;

import java.util.HashSet;
import java.util.List;

import com.beans.common.security.users.model.Users;

public interface UsersService {

	 public Users create(Users users);
	 public Users delete(int id) throws UsersNotFound;
	 public List<Users> findAll();
	 public Users update(Users users) throws UsersNotFound;
	 public Users findById(int id) throws UsersNotFound;
	 public Users registerUser(Users users);
	 public Users findByUsername(String username) throws UsersNotFound;
	 public List<Users> findUsersByUsername(String username);
	 public HashSet<String> getAccessRightsMapForUser(int userId) throws UsersNotFound;
	 public void changePassword(Users users, String oldPassword, String newPassword) throws ChangePasswordException, UsersNotFound;
}


