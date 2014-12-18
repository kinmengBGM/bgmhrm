package com.beans.leaveflow.dbservice;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beans.common.security.role.repository.RoleRepository;
import com.beans.leaveapp.applyleave.service.LeaveApplicationWorker;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetransaction.repository.LeaveTransactionRepository;
import com.beans.util.enums.Leave;
import com.beans.util.log.ApplLogger;

@Service
public class LeaveApplicationFlowServiceImpl implements	LeaveApplicationFlowService {
	
	@Resource
	RoleRepository roleRepository;
	
	@Resource
	LeaveTransactionRepository leaveTransactionRepository;

	@Override
	public List<LeaveTransaction> getPendingLeaveRequestsByRoleOfUser(String username) {
	List<String>  rolesList =	roleRepository.findRoleNamesByUsername(username);
	if(rolesList!=null && rolesList.size()!=0){
		String rolesString = "(" + StringUtils.join(rolesList, ",") + ")";
		ApplLogger.getLogger().info(String.format("For the Logged in user : %s list of roles found are : %s", username,rolesString));
		List<LeaveTransaction>	pendingLeavesList =	leaveTransactionRepository.findAllPendingLeavesWithGivenRoles(rolesList);
		ApplLogger.getLogger().info( String.format("We have found %d number of pending leaves the user : %s have to take decision", pendingLeavesList.size(),username));
		if(pendingLeavesList!=null && pendingLeavesList.size()!=0)
			return pendingLeavesList;
		
	}
		return new ArrayList<LeaveTransaction>();
	}

	// This is used by our own process engine dealing with DB
	@Transactional
	@Override
	public void updateLeaveBalancesOnceApproved(LeaveTransaction leaveTransaction,Boolean isApproved) {
		if(isApproved){
		if(Leave.TIMEINLIEU.equalsName(leaveTransaction.getLeaveType().getName())){
			LeaveApplicationWorker.updateToAnnualLeaveBalanceAfterApprovalTimeInLieuLeave(leaveTransaction);
		}else{
			LeaveApplicationWorker.updateLeaveBalanceAfterApprovalNonTimeInLieuLeave(leaveTransaction);
		}
		LeaveApplicationWorker.updateLeaveApplicationStatusForLeavesAfterFinalApproval(leaveTransaction,isApproved);
		}else
			LeaveApplicationWorker.updateLeaveApplicationStatusForLeavesAfterFinalApproval(leaveTransaction,isApproved);
	}

}
