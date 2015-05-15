package com.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.beans.common.security.users.model.Users;
import com.beans.leaveapp.address.model.Address;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.service.EmployeeNotFound;
import com.beans.leaveapp.employee.service.EmployeeService;
import com.custom.wrapper.employee.UpdateEmployeeWrapper;

@RestController
@RequestMapping("/protected/employee")
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	@RequestMapping(value ="/create", method=RequestMethod.POST)
	 public Employee create(@RequestBody Employee employee){
		return employeeService.create(employee);
	 }
	 
	@RequestMapping(value ="/delete", method=RequestMethod.GET)
	public Employee delete(@RequestParam int id) throws EmployeeNotFound {
		return employeeService.delete(id);
	}
	
	@RequestMapping(value ="/createEmployee", method=RequestMethod.POST)
	public Employee createEmployee(@RequestBody UpdateEmployeeWrapper updateEmployeeWrapper){
		return employeeService.createEmployee(updateEmployeeWrapper.getEmployee(), updateEmployeeWrapper.getEmployeeGradeId(), updateEmployeeWrapper.getEmployeeTypeId(), updateEmployeeWrapper.getDepartmentId(), updateEmployeeWrapper.getUsers(), updateEmployeeWrapper.getNewAddressMap());
	}
	
	@RequestMapping(value ="/updateEmployee", method=RequestMethod.POST)
	public Employee updateEmployee(@RequestBody UpdateEmployeeWrapper updateEmployeeWrapper){
		return employeeService.updateEmployee(updateEmployeeWrapper.getEmployee(), updateEmployeeWrapper.getEmployeeGradeId(), updateEmployeeWrapper.getEmployeeTypeId(), updateEmployeeWrapper.getDepartmentId(), updateEmployeeWrapper.getUsers(), updateEmployeeWrapper.getExistingAddressList(), updateEmployeeWrapper.getNewAddressMap());
	}

	@RequestMapping("/findAll")
	public List<Employee> findAll(){
		return employeeService.findAll();
	}
	
	@RequestMapping(value ="/update", method=RequestMethod.POST)
	public Employee update(@RequestBody Employee employee) throws EmployeeNotFound{
		return employeeService.update(employee);
	}
	
	
	@RequestMapping(value ="/findById", method = RequestMethod.GET)
	public Employee findById(@RequestParam int id) throws EmployeeNotFound{
		return employeeService.findById(id);
	}
	
	
	@RequestMapping(value="/findByUsername", method = RequestMethod.GET)
	public  Employee findByUsername(@RequestParam String username) throws EmployeeNotFound{
		return employeeService.findByUsername(username);
		
	}
	
	@RequestMapping(value="/findEmployeeByNameOrEmployeeNumberOrBoth", method = RequestMethod.GET)
	public List<Employee> findEmployeeByNameOrEmployeeNumberOrBoth(@RequestParam(required=false)String name, @RequestParam(required=false)String employeeNumber){
		return employeeService.findEmployeeByNameOrEmployeeNumberOrBoth(name, employeeNumber);
	}
	
	@RequestMapping(value="/findByEmployee", method = RequestMethod.GET)
	public Employee findByEmployee(@RequestParam String employeeName){
		return employeeService.findByEmployee(employeeName);
	}

	@RequestMapping("/findByEmployeeTypePermAndCont")
	public List<Employee> findByEmployeeTypePermAndCont(){
		return employeeService.findByEmployeeTypePermAndCont();
	}
	
	@RequestMapping("/findByEmployeeTypePerm")
	public List<Employee> findByEmployeeTypePerm(){
		return employeeService.findByEmployeeTypePerm();
	}
	
	@RequestMapping(value="/findAllEmployeesByRole", method = RequestMethod.GET)
	public List<Employee> findAllEmployeesByRole(@RequestParam String role){
		return employeeService.findAllEmployeesByRole(role);
	}
	
	@RequestMapping(value="/getFullNameOfEmployee", method = RequestMethod.GET)
	public String getFullNameOfEmployee(@RequestParam String username){
		return employeeService.getFullNameOfEmployee(username);
	}

	@RequestMapping(value="/findByUserId", method = RequestMethod.GET)
	public Employee findByUserId(@RequestParam int userId) throws EmployeeNotFound{
		return employeeService.findByUserId(userId);
	}




	
}
