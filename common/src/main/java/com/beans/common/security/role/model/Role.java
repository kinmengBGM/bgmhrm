package com.beans.common.security.role.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.beans.common.security.accessrights.model.AccessRights;
import com.beans.common.security.users.model.Users;


@Entity
@Table(name="Role")
public class Role implements Serializable{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	 private String role;
	 private boolean isDeleted= false;
	 private String description;
	 private String createdBy;
	 private java.util.Date creationTime;
	 private String lastModifiedBy;
	 private java.util.Date lastModifiedTime;
	 private Set<AccessRights> accessRights = new HashSet<AccessRights>();
	 
	@Id
	@GeneratedValue
	@Column(name="id", nullable=false, unique=true)
	public int getId() {
		return id;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="role", nullable=false)
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}

	@Column(name="isDeleted", columnDefinition="TINYINT(1)")
	@Type(type="org.hibernate.type.NumericBooleanType")
	public boolean isDeleted() {
		return isDeleted;
	}


	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	} 
	 
	@Column(name="description", nullable=true)
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public void setCreationTime(java.util.Date creationTime) {
		this.creationTime = creationTime;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public void setLastModifiedTime(java.util.Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
			
	@Column(name="creationTime",nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	public java.util.Date getCreationTime() {
		return creationTime;
	}
	@Column(name="createdBy",nullable=true)
	public String getCreatedBy() {
		return createdBy;
	}
	@Column(name="lastModifiedTime",nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	public java.util.Date getLastModifiedTime() {
		return lastModifiedTime;
	}
	@Column(name="lastModifiedBy",nullable=true)
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	
	
	
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "RoleToAccessRights" , joinColumns = { @JoinColumn(name = "roleId", referencedColumnName = "id" )},
	inverseJoinColumns=
			{@JoinColumn (name = "accessRightsId", referencedColumnName = "id" ) } )
	public Set<AccessRights> getAccessRights() {
		return accessRights;
	}


	public void setAccessRights(Set<AccessRights> accessRights) {
		this.accessRights = accessRights;
	}


	@Override
	public boolean equals(Object obj) {
		Role inputRole = (Role) obj;
		if(getId() == inputRole.getId()) {
			return true;
		} else {
			return false;
		}
	}


	@Override
	public int hashCode() {
		return id;
	}	
}
