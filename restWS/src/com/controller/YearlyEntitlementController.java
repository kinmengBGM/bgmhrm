package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementNotFound;
import com.beans.leaveapp.yearlyentitlement.service.YearlyEntitlementService;


@RestController
@RequestMapping("/protected/yearlyEntitlement")
public class YearlyEntitlementController {
	
	@Autowired
	YearlyEntitlementService yearlyEntitlementService;
	
	@RequestMapping("/findAll")
	public List<YearlyEntitlement> findAll() throws Exception {
		return yearlyEntitlementService.findAll();
	}

	@RequestMapping(value="/update", method= RequestMethod.POST)
	public YearlyEntitlement update(@RequestBody YearlyEntitlement selectedYearlyEntitlement) throws Exception{
		return yearlyEntitlementService.update(selectedYearlyEntitlement);
	}

	@RequestMapping(value="/delete", method=RequestMethod.GET)
	public YearlyEntitlement delete(@RequestParam int id){
		return yearlyEntitlementService.delete(id);
	}

	@RequestMapping(value="/create", method= RequestMethod.POST)
	public YearlyEntitlement create(@RequestBody YearlyEntitlement yearlyEntitlement){
		return yearlyEntitlementService.create(yearlyEntitlement);
	}
  
	@RequestMapping("/employeeNames")
	public List<String> employeeNames(){
		return yearlyEntitlementService.employeeNames();
	}

	@RequestMapping(value="/findLeaveTypes", method=RequestMethod.GET)
	public List<String> findLeaveTypes(@RequestParam int employeeTypeId){
		return yearlyEntitlementService.findLeaveTypes(employeeTypeId);
	}
	
	@RequestMapping(value="/findByEmployee", method= RequestMethod.GET)
	public Employee findByEmployee(@RequestParam String name){
		return yearlyEntitlementService.findByEmployee(name);
		 
	 }
	
	@RequestMapping(value="/findByLeaveType", method= RequestMethod.GET)
	public LeaveType findByLeaveType(@RequestParam String name,@RequestParam int employeeTypeId){
		return yearlyEntitlementService.findByLeaveType(name, employeeTypeId);
	}
	
	@RequestMapping(value="/findByEmployeeOrfindByLeaveTypeOrBoth", method= RequestMethod.GET)
	public List<YearlyEntitlement> findByEmployeeOrfindByLeaveTypeOrBoth(@RequestParam(required=false)String employeeName,@RequestParam(required=false) String leaveType){
		return yearlyEntitlementService.findByEmployeeOrfindByLeaveTypeOrBoth(employeeName, leaveType);
	}
	
	@RequestMapping(value="/findByEmployeeId", method=RequestMethod.GET)
	public List<YearlyEntitlement> findByEmployeeId(@RequestParam int employeeId){
		return yearlyEntitlementService.findByEmployeeId(employeeId);
		
	}
	
	@RequestMapping(value="/findByEmployeeAndLeaveType", method=RequestMethod.GET)
	public YearlyEntitlement findByEmployeeAndLeaveType(@RequestParam int employeeId,@RequestParam int leaveTypeId) throws YearlyEntitlementNotFound{
		return yearlyEntitlementService.findByEmployeeAndLeaveType(employeeId, leaveTypeId);
	}
	
	@RequestMapping(value="/findYearlyEntitlementById", method=RequestMethod.GET)
	public YearlyEntitlement findYearlyEntitlementById(@RequestParam int employeeId,@RequestParam int leaveTypeId){
		return yearlyEntitlementService.findYearlyEntitlementById(employeeId,leaveTypeId);
	}
	

	@RequestMapping(value="/findYearlyEntitlementListByEmployee", method=RequestMethod.GET)
	public List<YearlyEntitlement> findYearlyEntitlementListByEmployee(@RequestParam int employeeId){
		return yearlyEntitlementService.findYearlyEntitlementListByEmployee(employeeId);
	}	
	
	@RequestMapping(value="/findOne", method=RequestMethod.GET)
	public YearlyEntitlement findOne(@RequestParam int yearlyEntitlementId) throws YearlyEntitlementNotFound{
		return yearlyEntitlementService.findOne(yearlyEntitlementId);
	}
	
	@RequestMapping(value="/findByEmployeeIdPermAndCont", method=RequestMethod.GET)
	public YearlyEntitlement findByEmployeeIdPermAndCont(@RequestParam int employeeId){
		return yearlyEntitlementService.findByEmployeeIdPermAndCont(employeeId);
	}
	
	@RequestMapping(value="/findByEmployeeIdPerm", method=RequestMethod.GET)
	public YearlyEntitlement findByEmployeeIdPerm(@RequestParam int employeeId){
		return yearlyEntitlementService.findByEmployeeIdPerm(employeeId);
	}
	
	@RequestMapping(value="/findByEmployeeIdForRemainingLeaves", method=RequestMethod.GET)
	public List<YearlyEntitlement> findByEmployeeIdForRemainingLeaves(@RequestParam int employeeId){
		return yearlyEntitlementService.findByEmployeeIdForRemainingLeaves(employeeId);
	}

	@RequestMapping(value="/updateLeaveBalanceAfterApproval", method=RequestMethod.GET)
	public void updateLeaveBalanceAfterApproval(@RequestParam int employeeId,@RequestParam int leaveTypeId,@RequestParam double numberOfDaysApproved){
		yearlyEntitlementService.updateLeaveBalanceAfterApproval(employeeId, leaveTypeId, numberOfDaysApproved);
	}
	
	@RequestMapping(value="/updateAnnualLeaveBalanceAfterApproval", method=RequestMethod.GET)
	void updateAnnualLeaveBalanceAfterApproval(@RequestParam int employeeId,@RequestParam int leaveTypeId,@RequestParam double numberOfDaysApproved){
		yearlyEntitlementService.updateAnnualLeaveBalanceAfterApproval(employeeId,leaveTypeId,numberOfDaysApproved);
	}
	
	@RequestMapping(value="/updateLeaveBalanceAfterCancelled", method=RequestMethod.GET)
	public void updateLeaveBalanceAfterCancelled(@RequestParam int employeeId,@RequestParam int leaveTypeId,@RequestParam double numberOfDaysApproved){
		yearlyEntitlementService.updateLeaveBalanceAfterCancelled(employeeId, leaveTypeId, numberOfDaysApproved);
	}
	
	@RequestMapping(value="/addAllEntitlementsToNewEmployee", method=RequestMethod.POST)
	public void addAllEntitlementsToNewEmployee(@RequestBody Employee newlyRegisteredEmployee){
		yearlyEntitlementService.addAllEntitlementsToNewEmployee(newlyRegisteredEmployee);
	}
	
	@RequestMapping(value="/findAnnualYearlyEntitlementOfEmployee", method=RequestMethod.GET)
	public YearlyEntitlement findAnnualYearlyEntitlementOfEmployee(@RequestParam int employeeId){
		return yearlyEntitlementService.findAnnualYearlyEntitlementOfEmployee(employeeId);
	}
	
}
