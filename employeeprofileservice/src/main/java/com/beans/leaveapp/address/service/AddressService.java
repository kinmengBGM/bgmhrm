package com.beans.leaveapp.address.service;

import java.util.List;

import com.beans.leaveapp.address.model.Address;


public interface AddressService {
	public Address create(Address address);
	public Address delete(int id) throws AddressNotFound;
	
	public List<Address> findAll();
/*	public Address update(Address address) throws AddressNotFound;*/
	public Address findById(int id) throws AddressNotFound;
	public List<Address> findByEmployeeId(int employeeId);
}
