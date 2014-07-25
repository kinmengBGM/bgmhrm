
package com.beans.common.security.usertoaccessrights.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.beans.common.security.accessrights.model.AccessRights;
import com.beans.common.security.users.model.Users;

@Entity
@Table(name="UserToAccessRights")
public class UserToAccessRights {

	private int id;
	private Users users;
	private AccessRights accessRights;
	private boolean enabled;
	private boolean isDeleted= false;
	
	@Id
	@GeneratedValue
	@Column(name="id", nullable=false, unique=true)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@OneToOne
	@JoinColumn(name="userId")
	public Users getUsers() {
		return users;
	}
	
	public void setUsers(Users users) {
		this.users = users;
	}
	
	@OneToOne
	@JoinColumn(name="accessRightsId")
	public AccessRights getAccessRights() {
		return accessRights;
	}
	
	public void setAccessRights(AccessRights accessRights) {
		this.accessRights = accessRights;
	}
	
	
	@Column(name="enabled", columnDefinition="TINYINT(1)") 
	@Type(type="org.hibernate.type.NumericBooleanType")
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Column(name="isDeleted", columnDefinition="TINYINT(1)")
	@Type(type="org.hibernate.type.NumericBooleanType")
	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}	
	
}

