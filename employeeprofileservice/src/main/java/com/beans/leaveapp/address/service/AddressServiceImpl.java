package com.beans.leaveapp.address.service;

import java.util.List;

import javax.annotation.Resource;

import com.beans.leaveapp.address.model.Address;
import com.beans.leaveapp.address.repository.AddressRepository;

public class AddressServiceImpl implements AddressService {

	
	@Resource
	AddressRepository addressRepository;
	
	@Override
	public Address create(Address address) {
		Address addressToBeCreated = address;
		addressRepository.save(addressToBeCreated);
		return addressToBeCreated;
	}

	@Override
	public Address delete(int id) throws AddressNotFound {
		Address addressToBeDeleted = addressRepository.findOne(id);
		
		if(addressToBeDeleted == null) {
			throw new AddressNotFound();
		}
		
		addressToBeDeleted.setDeleted(true);
		return addressRepository.save(addressToBeDeleted);
	}

	@Override
	public List<Address> findAll() {
		List<Address> addressList = addressRepository.findByisDeleted(0);
		return addressList;
	}

	@Override
	public Address update(Address address) throws AddressNotFound {
		Address addressToBeUpdated = addressRepository.findOne(address.getId());
		if(addressToBeUpdated != null){
			addressToBeUpdated.setId(address.getId());
			addressToBeUpdated.setAddressType(address.getAddressType());
			addressToBeUpdated.setCity(address.getCity());
			addressToBeUpdated.setCountry(address.getCountry());
			addressToBeUpdated.setDeleted(address.isDeleted());
			addressToBeUpdated.setEmployee(address.getEmployee());
			addressToBeUpdated.setLine1(address.getLine1());
			addressToBeUpdated.setLine2(address.getLine2());
			addressToBeUpdated.setLine3(address.getLine3());
			addressToBeUpdated.setPostcode(address.getPostcode());
			addressToBeUpdated.setState(address.getState());
			addressToBeUpdated.setLastModifiedBy(address.getLastModifiedBy());
			addressToBeUpdated.setLastModifiedTime(address.getLastModifiedTime());
			addressRepository.save(addressToBeUpdated);
		}
		return addressToBeUpdated;
	}

	@Override
	public Address findById(int id) throws AddressNotFound {
		Address address = addressRepository.findOne(id);
		return address;
	}

	@Override
	public List<Address> findByEmployeeId(int employeeId) {
		List<Address> addressList = addressRepository.findByEmployeeId(employeeId, 0);
		return addressList;
	}

}
