package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveflow.dbservice.LeaveApplicationFlowService;

@RestController
@RequestMapping("/protected/leaveApplicationFlow")
public class LeaveApplicationFlowController {
	
	@Autowired
	LeaveApplicationFlowService leaveFlowService;
	
	@RequestMapping(value="/getPendingLeaveRequestsByRoleOfUser", method=RequestMethod.GET)
	public List<LeaveTransaction> getPendingLeaveRequestsByRoleOfUser(@RequestParam String username){
		return leaveFlowService.getPendingLeaveRequestsByRoleOfUser(username);
	}

	@RequestMapping(value="/updateLeaveBalancesOnceApproved", method=RequestMethod.POST)
	public boolean updateLeaveBalancesOnceApproved(@RequestBody LeaveTransaction leaveTransactionPersist){
		leaveFlowService.updateLeaveBalancesOnceApproved(leaveTransactionPersist, Boolean.TRUE);
		return true;
	}
	
	@RequestMapping(value="/updateLeaveBalancesOnceRejected", method=RequestMethod.POST)
	public boolean updateLeaveBalancesOnceRejected(@RequestBody LeaveTransaction leaveTransactionPersist){
		leaveFlowService.updateLeaveBalancesOnceApproved(leaveTransactionPersist, Boolean.FALSE);
		return false;
	}
	
}
