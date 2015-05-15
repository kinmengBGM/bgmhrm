package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beans.leaveapp.employeetype.model.EmployeeType;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.beans.leaveapp.leavetype.service.LeaveTypeNotFound;
import com.beans.leaveapp.leavetype.service.LeaveTypeService;

@RestController
@RequestMapping("/protected/leaveType")
public class LeaveTypeController {

	@Autowired
	private LeaveTypeService leaveTypeService;
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public LeaveType create(@RequestBody LeaveType leaveType){
		return leaveTypeService.create(leaveType);
	}

	@RequestMapping(value="/delete", method=RequestMethod.GET)
	public LeaveType delete(@RequestParam int id){
		return leaveTypeService.delete(id);
	}
	
	@RequestMapping(value="/findAll")
	public List<LeaveType> findAll(){
		return leaveTypeService.findAll();
	}

	@RequestMapping(value="/update", method=RequestMethod.POST)
	public LeaveType update(@RequestBody LeaveType leaveType){
		return leaveTypeService.update(leaveType);
	}

	@RequestMapping(value="/findById", method=RequestMethod.GET)
	public LeaveType findById(@RequestParam int id) throws LeaveTypeNotFound{
		return leaveTypeService.findById(id);
	}

	@RequestMapping(value="/findByLeaveType", method=RequestMethod.GET)
	public LeaveType findByLeaveType(@RequestParam String name, @RequestParam int id) throws LeaveTypeNotFound{
		return leaveTypeService.findByLeaveType(name,id);
	}

	@RequestMapping(value="/findByName")
	public List<String> findByName(){
		return leaveTypeService.findByName();
	}

	@RequestMapping(value="/findByEmployeeName", method=RequestMethod.GET)
	public EmployeeType findByEmployeeName(@RequestParam String name){
		return leaveTypeService.findByEmployeeName(name);
	}

	@RequestMapping(value="/findByEmployeeTypes")
	public List<String> findByEmployeeTypes(){
		return leaveTypeService.findByEmployeeTypes();
	}

	@RequestMapping(value="/findByEmployeeNameAndTypeId", method=RequestMethod.GET)
	public LeaveType findByEmployeeNameAndTypeId(@RequestParam String name, @RequestParam int employeeTypeId){
		return leaveTypeService.findByEmployeeNameAndTypeId(name,employeeTypeId);
	}
}
