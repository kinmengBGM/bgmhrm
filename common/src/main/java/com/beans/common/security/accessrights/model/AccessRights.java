package com.beans.common.security.accessrights.model;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

@Entity
@Table(name="AccessRights")
public class AccessRights implements Serializable{	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String accessRights;
	private String description;    	
	private String createdBy;
	private Date creationTime;
	private String lastModifiedBy;
	private Date lastModifiedTime;
	private boolean isDeleted = false;   
	   

    public AccessRights(int id, String accessRights, String description,
			String createdBy, Date creationTime, String lastModifiedBy,
			Date lastModifiedTime, boolean isDeleted) {
		super();
		this.id = id;
		this.accessRights = accessRights;
		this.description = description;
		this.createdBy = createdBy;
		this.creationTime = creationTime;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedTime = lastModifiedTime;
		this.isDeleted = isDeleted;
	}

	public AccessRights(){
    	
    }
	
	@Id
	@GeneratedValue
	@Column(name="id", nullable=false, unique=true)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}	
	
	@Column(name="accessRights", nullable=false , length=45) 
	public String getAccessRights() {
		return accessRights;
	}
	
	public void setAccessRights(String accessRights) {
		this.accessRights = accessRights;
	}
	
	@Column(name="description" , nullable=false , length=255)
	public String getDescription() {
		return description;
	}
	

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	@Column(name="isDeleted", columnDefinition="TINYINT(1)") 
	@Type(type="org.hibernate.type.NumericBooleanType")
	public boolean isDeleted() {
		return isDeleted;
	}
	
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}		
	
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
			
	@Column(name="creationTime",nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationTime() {
		return creationTime;
	}
	@Column(name="createdBy",nullable=true)
	public String getCreatedBy() {
		return createdBy;
	}
	@Column(name="lastModifiedTime",nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}
	@Column(name="lastModifiedBy",nullable=true)
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	
	@Override
	public boolean equals(Object obj) {
		AccessRights inputAccessRights = (AccessRights) obj;
		if(getId() == inputAccessRights.getId()) {
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

