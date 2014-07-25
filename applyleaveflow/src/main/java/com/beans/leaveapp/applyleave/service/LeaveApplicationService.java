
package com.beans.leaveapp.applyleave.service;

import java.util.List;

import com.beans.common.security.role.service.RoleNotFound;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;

public interface LeaveApplicationService {
	 void submitLeave(Employee employee, YearlyEntitlement yearlyEntitlement, LeaveTransaction leaveTransaction) throws RoleNotFound, LeaveApplicationException;
	 List<LeaveTransaction> getPendingLeaveRequestsList(String username);
	 void approveLeaveOfEmployee(LeaveTransaction leaveTransaction, String actorId);
	 void rejectLeaveOfEmployee(LeaveTransaction leaveTransaction, String actorId);
}
