package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.beans.common.security.users.service.UserAuthenticationService;


@RestController
public class UserAuthenticationController {
	@Autowired
	UserAuthenticationService userAuthenticationService;

	@RequestMapping(value="/userAuthentication", method=RequestMethod.GET)
	UserDetails loadUserByUsername(@RequestParam String username) throws UsernameNotFoundException, DataAccessException {
		return userAuthenticationService.loadUserByUsername(username);
	}

}
