package com.beans.common.security.accessrights.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beans.common.security.accessrights.model.AccessRights;
import com.beans.common.security.accessrights.repository.AccessRightsRepository;
import com.beans.common.security.role.service.RoleNotFound;
import com.beans.exceptions.BSLException;

@Service
public class AccessRightsServiceImpl implements AccessRightsService{

	
	
	@Resource
	private AccessRightsRepository accessRightsRepository;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	@Override
	@Transactional(rollbackFor={Exception.class,BSLException.class})
	public AccessRights create(AccessRights accessRights) {
		try{
		AccessRights accessRightsToBeCreated = accessRights;

		log.info("New Access Right is saving into database with Name :"+accessRights.getAccessRights());
		return accessRightsRepository.save(accessRightsToBeCreated);
	}catch (Exception e) {
		log.error("Error while saving new Access Right :"+accessRights.getAccessRights(), e);
		throw new BSLException("err.accessRights.create", e);
	}
}

	@Override
	@Transactional(rollbackFor=AccessRightsNotFound.class)
	public AccessRights delete(int id){
        try{  
		AccessRights accessRights = accessRightsRepository.findOne(id);
          
          if(accessRights != null){        	   
          accessRights.setDeleted(true);
          accessRightsRepository.save(accessRights);		
          }
          return accessRights;
	}catch(Exception e) {
		log.error("Error while deleting new Access Right :"+id, e);
		throw new BSLException("err.accessRights.delete", e);
	}
        }	

	@Override	
	public List<AccessRights> findAll() {
		List<AccessRights> accessRightsList = accessRightsRepository.findByIsDeleted(0);
		return accessRightsList;
	}

	@Override
	@Transactional(rollbackFor={Exception.class,BSLException.class})
	public AccessRights update(AccessRights accessRights){
		try{
		AccessRights accessRightsToBeUpdated = accessRightsRepository.findOne(accessRights.getId());
		
		if(accessRightsToBeUpdated != null)
		{	 
		accessRightsToBeUpdated.setId(accessRights.getId());
		accessRightsToBeUpdated.setAccessRights(accessRights.getAccessRights());
		accessRightsToBeUpdated.setDescription(accessRights.getDescription());
		accessRightsToBeUpdated.setLastModifiedBy(accessRights.getLastModifiedBy());
		accessRightsToBeUpdated.setLastModifiedTime(new java.util.Date());
		accessRightsRepository.save(accessRightsToBeUpdated);		   
		}
		return accessRightsToBeUpdated;
	}catch (Exception e) {
		log.error("Error while updating Access Right :"+accessRights.getAccessRights(), e);
		throw new BSLException("err.accessRights.update", e);
	}
}

	@Override
	@Transactional
	public AccessRights findById(int id) throws AccessRightsNotFound {
            AccessRights accessRights = accessRightsRepository.findOne(id);
            
            if(accessRights == null) 
            	throw new AccessRightsNotFound();
            
		return accessRights;
	}

	@Override
	public Set<AccessRights> findAllInSet() {
			Set<AccessRights> resultSet = accessRightsRepository.findAllInSet();
		return resultSet;
	}

	@Override
	public List<AccessRights> findAccessRightsByAccessRight(String accessRight) {
		String accessRightSearchTerm = "%" + accessRight + "%";
		return accessRightsRepository.findByAccessRightLike(accessRightSearchTerm);
	}

	@Override
	public AccessRights findAccessRights(String accessRight) {
		AccessRights accessRights = accessRightsRepository.findByAccessRight(accessRight);
		return accessRights;
	}

}


