package com.beans.common.security.users.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.beans.common.security.role.model.Role;
import com.beans.common.security.users.model.Users;

public class UserDetailsImpl implements Serializable, UserDetails{

	private static final long serialVersionUID = 2755218805643703788L;

    private final String username;
    private final String password;
    private final boolean isEnabled;
    private final List<GrantedAuthority> authorities;
    
    public UserDetailsImpl(final Users user) {
    	this.username = user.getUsername();
    	this.password = user.getPassword();
    	this.isEnabled = user.isEnabled();
    	Set<Role> roleSet = user.getUserRoles();
    	authorities = new ArrayList<GrantedAuthority>();
        Iterator<Role> roleIterator = roleSet.iterator();
        while(roleIterator.hasNext()) {
        	Role currentRole = roleIterator.next();
        	authorities.add(new SimpleGrantedAuthority(currentRole.getRole()));
        }
    	
    }
    
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.isEnabled;
	}
	
}
