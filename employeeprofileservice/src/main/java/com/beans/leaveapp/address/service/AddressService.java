package com.beans.leaveapp.address.service;

import java.util.List;

import com.beans.leaveapp.address.model.Address;


public interface AddressService {
	 Address create(Address address);
	 Address delete(int id) throws AddressNotFound;
	 Address update(Address address) throws AddressNotFound;
	 List<Address> findAll();
	 Address findById(int id) throws AddressNotFound;
	 List<Address> findByEmployeeId(int employeeId);
}
