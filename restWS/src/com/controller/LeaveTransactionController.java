package com.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.beans.common.leave.rules.model.LeaveFlowDecisionsTaken;
import com.beans.common.leave.rules.model.LeaveRuleBean;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetransaction.service.LeaveTransactionService;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.custom.wrapper.leavetransaction.GetLeaveRuleByRoleAndLeaveTypeWrapper;

@RestController
@RequestMapping("/protected/leaveTransaction")
public class LeaveTransactionController {

	@Autowired
	LeaveTransactionService leaveTransactionService;
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public int create(@RequestBody LeaveTransaction adminLeaveTransaction){
		return leaveTransactionService.create(adminLeaveTransaction);
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@RequestBody LeaveTransaction adminLeaveTransaction){
		leaveTransactionService.update(adminLeaveTransaction);
	}

	@RequestMapping(value="/delete", method=RequestMethod.GET)	
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@RequestParam int id){
		leaveTransactionService.delete(id);
	}
	
	@RequestMapping(value="/insertFromWorkflow", method=RequestMethod.POST)
	public LeaveTransaction insertFromWorkflow(@RequestBody LeaveTransaction leaveTransaction){
		return leaveTransactionService.insertFromWorkflow(leaveTransaction);
	}
	
	@RequestMapping(value="/updateLeaveApplicationStatus", method=RequestMethod.POST)
    public LeaveTransaction updateLeaveApplicationStatus(@RequestBody LeaveTransaction leaveTransaction){
		return leaveTransactionService.updateLeaveApplicationStatus(leaveTransaction);
	}
	
	@RequestMapping(value="/processAppliedLeaveOfEmployee", method=RequestMethod.POST)
	LeaveTransaction processAppliedLeaveOfEmployee(@RequestBody LeaveTransaction leaveTransaction){
		return leaveTransactionService.processAppliedLeaveOfEmployee(leaveTransaction);
	}
	
	@RequestMapping("/findAll")
	public List<LeaveTransaction> findAll(){
		return leaveTransactionService.findAll();
	}
	
	@RequestMapping("/findEmployeeNames")
	public List<String> findEmployeeNames(){
		return leaveTransactionService.findEmployeeNames();
	}
	
	@RequestMapping(value ="/findLeaveTypes", method=RequestMethod.GET)
	public List<String> findLeaveTypes(@RequestParam String name){
		return leaveTransactionService.findLeaveTypes(name);		
	}
	
	@RequestMapping(value ="/findByEmployee", method=RequestMethod.GET)
	public Employee findByEmployee(@RequestParam String name) {
		return leaveTransactionService.findByEmployee(name);
	}
	
	@RequestMapping(value ="/findByLeaveType", method=RequestMethod.GET)
	public LeaveType findByLeaveType(@RequestParam String name, @RequestParam int id){
		return leaveTransactionService.findByLeaveType(name,id);
	}
	
	@RequestMapping(value ="/findByEmployeeORfindByLeaveTypeORLeaveDatesORStatusORAll", method=RequestMethod.GET)
	public List<LeaveTransaction> findByEmployeeORfindByLeaveTypeORLeaveDatesORStatusORAll(@RequestParam(required=false)String employeename, @RequestParam(required=false)String leaveType,@RequestParam(required=false)Date startDate,@RequestParam(required=false)String status){
		return leaveTransactionService.findByEmployeeORfindByLeaveTypeORLeaveDatesORStatusORAll(employeename, leaveType, startDate, status);
	}
	
	@RequestMapping(value="/findByEmployeeORfindByLeaveType", method=RequestMethod.GET)
	public List<LeaveTransaction> findByEmployeeORfindByLeaveType(@RequestParam(required=false)String employeeName, @RequestParam(required=false)String leaveType){
		return leaveTransactionService.findByEmployeeORfindByLeaveType(employeeName, leaveType);
	}
	
	@RequestMapping(value="/findByStatus", method=RequestMethod.GET)
    public List<LeaveTransaction> findByStatus(@RequestParam String status){
		return leaveTransactionService.findByStatus(status);
	}
	
	@RequestMapping(value="/findById", method=RequestMethod.GET)
	public LeaveTransaction findById(@RequestParam int id){
		return leaveTransactionService.findById(id);
	}
	
	@RequestMapping(value="/getAllFutureLeavesAppliedByEmployee", method=RequestMethod.GET)
	public List<LeaveTransaction> getAllFutureLeavesAppliedByEmployee(@RequestParam int employeeId,@RequestParam java.sql.Date todayDate){
		return leaveTransactionService.getAllFutureLeavesAppliedByEmployee(employeeId,todayDate);
	}
	
	@RequestMapping(value="/getAllApprovedLeavesAppliedByEmployee", method=RequestMethod.GET)
    public List<LeaveTransaction> getAllApprovedLeavesAppliedByEmployee(){
		return leaveTransactionService.getAllApprovedLeavesAppliedByEmployee();

	}
	@RequestMapping(value="/getAllLeavesAppliedByEmployee", method=RequestMethod.GET)
    public List<LeaveTransaction> getAllLeavesAppliedByEmployee(@RequestParam(required=false) int employeeId){
		if(employeeId != 0)
			return leaveTransactionService.getAllLeavesAppliedByEmployee(employeeId);
		else 
			return leaveTransactionService.getAllLeavesAppliedByEmployee();

	}
	
	@RequestMapping(value="/getLeaveRuleByRoleAndLeaveType", method=RequestMethod.POST)
    public LeaveRuleBean getLeaveRuleByRoleAndLeaveType(@RequestBody(required=false) GetLeaveRuleByRoleAndLeaveTypeWrapper leaveWrapper){
		return leaveTransactionService.getLeaveRuleByRoleAndLeaveType(leaveWrapper.getLeaveType(), leaveWrapper.getRoleType());
	}
	
	@RequestMapping(value="/saveLeaveApprovalDecisions", method=RequestMethod.POST)
	public LeaveFlowDecisionsTaken saveLeaveApprovalDecisions(@RequestBody(required=false) LeaveFlowDecisionsTaken leaveFlowDecisions){
		return leaveTransactionService.saveLeaveApprovalDecisions(leaveFlowDecisions);
    	
    }


}
