package com.beans.common.security.users.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.beans.common.security.users.model.Users;

public class UserAuthenticationServiceImpl implements UserAuthenticationService, UserDetailsService{

	UsersService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Users user = null;
		try {
			user = username != null && !username.equals("") ? userService.findByUsername(username) : null;
	        if (user == null || user.getPassword() == null) {
	            throw new UsernameNotFoundException("No such user: " + username);
	        }
		} catch(UsersNotFound e) {
			throw new UsernameNotFoundException("No such user: " + username);
		}
        return new UserDetailsImpl(user);
	}
	
	public UsersService getUserService() {
		return userService;
	}
	public void setUserService(UsersService userService) {
		this.userService = userService;
	}
	
}
