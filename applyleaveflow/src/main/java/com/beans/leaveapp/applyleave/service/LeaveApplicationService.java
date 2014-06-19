package com.beans.leaveapp.applyleave.service;

import java.util.List;
import java.util.Set;

import com.beans.common.security.role.model.Role;
import com.beans.common.security.role.service.RoleNotFound;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;

public interface LeaveApplicationService {
	public void submitLeave(Employee employee, YearlyEntitlement yearlyEntitlement, LeaveTransaction leaveTransaction) throws RoleNotFound, LeaveApplicationException;
	public List<LeaveTransaction> getPendingLeaveRequestsList(String username);
	public void approveLeaveOfEmployee(LeaveTransaction leaveTransaction, String actorId,Set<Role> actorRoles);
	public void rejectLeaveOfEmployee(LeaveTransaction leaveTransaction, String actorId,Set<Role> userRoles);
}
