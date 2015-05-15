package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beans.leaveapp.employeegrade.model.EmployeeGrade;
import com.beans.leaveapp.employeegrade.service.EmployeeGradeNotFound;
import com.beans.leaveapp.employeegrade.service.EmployeeGradeService;

@RestController
@RequestMapping("/protected/employeeGrade")
public class EmployeeGradeController {
	
	@Autowired
	EmployeeGradeService employeeGradeService;
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public EmployeeGrade create(@RequestBody EmployeeGrade employeeGrade){
		return employeeGradeService.create(employeeGrade);
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.GET)
	public EmployeeGrade delete(@RequestParam int id){
		 return employeeGradeService.delete(id);
	 }
	
	@RequestMapping("/findAll")
	public List<EmployeeGrade> findAll(){
		 return employeeGradeService.findAll();
	 }
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public EmployeeGrade update(@RequestBody EmployeeGrade employeeGrade){
		return employeeGradeService.update(employeeGrade);
	}
	
	@RequestMapping(value="/findById", method=RequestMethod.GET)
	public EmployeeGrade findById(@RequestParam int id) throws EmployeeGradeNotFound{
		return employeeGradeService.findById(id);
	}
}
