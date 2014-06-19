package com.beans.leaveapp.masterdata.bean;

import java.util.Set;

import com.beans.common.security.accessrights.model.AccessRights;
import com.beans.common.security.accessrights.service.AccessRightsService;

public class AccessRightsListBean {

	private Set<AccessRights> accessRightsSet = null;
	private AccessRightsService accessRightsService;
	
	
	
	public Set<AccessRights> getDepList() {		
		if(accessRightsSet == null)
			accessRightsSet = accessRightsService.findAllInSet();		
		return accessRightsSet;
	}
	public void setDepList(Set<AccessRights> accessRightsSet) {
		this.accessRightsSet = accessRightsSet;
	}
	
	
	public AccessRightsService getAccessRightsService() {
		return accessRightsService;
	}
	
	public void setAccessRightsService(AccessRightsService accessRightsService) {
		this.accessRightsService = accessRightsService;
	}
	
	
	
	
}
