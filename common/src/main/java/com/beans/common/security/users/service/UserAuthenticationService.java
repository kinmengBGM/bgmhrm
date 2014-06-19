package com.beans.common.security.users.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserAuthenticationService {
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException;
}
