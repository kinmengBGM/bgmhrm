package com.beans.leaveflow.dbservice;

import java.util.List;

import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;

public interface LeaveApplicationFlowService {
	
	List<LeaveTransaction> getPendingLeaveRequestsByRoleOfUser(String username);
	void updateLeaveBalancesOnceApproved(LeaveTransaction leaveTransaction,Boolean isApproved);
}
