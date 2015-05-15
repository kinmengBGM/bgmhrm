package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beans.leaveapp.employeetype.model.EmployeeType;
import com.beans.leaveapp.employeetype.service.EmployeeTypeNotFound;
import com.beans.leaveapp.employeetype.service.EmployeeTypeService;

@RestController
@RequestMapping("/protected/employeeType")
public class EmployeeTypeController {
	
	@Autowired
	EmployeeTypeService employeeTypeService;
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public EmployeeType create(@RequestBody EmployeeType employeeType){
		return employeeTypeService.create(employeeType);
	}
	@RequestMapping(value="/delete", method=RequestMethod.GET)
	public EmployeeType delete(@RequestParam int id){
		 return employeeTypeService.delete(id);
	 }
	
	@RequestMapping(value="/findAll")
	public List<EmployeeType> findAll(){
		return employeeTypeService.findAll();
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public EmployeeType update(@RequestBody EmployeeType employeeType){
		return employeeTypeService.update(employeeType);
	}
	
	@RequestMapping(value="/findById", method=RequestMethod.GET)
	public EmployeeType findById(@RequestParam int id) throws EmployeeTypeNotFound{
		return employeeTypeService.findById(id);
		
	}
}
