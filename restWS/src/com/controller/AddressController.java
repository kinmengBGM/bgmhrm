package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beans.leaveapp.address.model.Address;
import com.beans.leaveapp.address.service.AddressNotFound;
import com.beans.leaveapp.address.service.AddressService;

@RestController
@RequestMapping("/protected/address")
public class AddressController {

	@Autowired
	AddressService addressService;
	
	@RequestMapping(value ="/create", method = RequestMethod.POST)
	public Address create(@RequestBody Address address){
		return addressService.create(address);		
	}
	
	@RequestMapping(value ="/delete", method = RequestMethod.GET)
	public Address delete(@RequestParam int id) throws AddressNotFound{
		return addressService.delete(id);
	}	
	
	@RequestMapping(value ="/update", method = RequestMethod.POST)
	public Address update(@RequestBody Address address) throws AddressNotFound{
		return addressService.update(address);		
	}
	
	@RequestMapping(value ="/findAll")
	public List<Address> findAll() {
		return addressService.findAll();
	}
	
	@RequestMapping(value ="/findById", method = RequestMethod.GET)
	public Address findById(@RequestParam int id) throws AddressNotFound {
		return addressService.findById(id);
	}

	@RequestMapping(value ="/findByEmployeeId", method = RequestMethod.GET)
	public List<Address> findByEmployeeId(@RequestParam int employeeId){
		return addressService.findByEmployeeId(employeeId);
	}
}
