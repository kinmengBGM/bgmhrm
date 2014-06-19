package com.beans.leaveapp.applyleave.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;

import com.beans.common.security.role.model.Role;
import com.beans.common.security.role.service.RoleNotFound;
import com.beans.exceptions.BSLException;
import com.beans.leaveapp.applyleave.model.ApprovalLevelModel;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.jbpm6.util.JBPM6Runtime;
import com.beans.leaveapp.leaveapplicationcomment.model.LeaveApplicationComment;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetransaction.service.LeaveTransactionService;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;

public class LeaveApplicationServiceImpl implements LeaveApplicationService {

	private Logger log = Logger.getLogger(this.getClass());
	private static final String PROCESS_NAME = "com.beans.leaveapp.bpmn.applyleave";
	private JBPM6Runtime applyLeaveRuntime;
	private LeaveTransactionService leaveTransactionService;
	
	@Override
	public void submitLeave(Employee employee,
			YearlyEntitlement yearlyEntitlement,
			LeaveTransaction leaveTransaction) throws RoleNotFound, LeaveApplicationException{
		Set<Role> userRoles = employee.getUsers().getUserRoles();
		
		if(!isEmployee(userRoles)) {
			throw new RoleNotFound("You are not an employee.");
		}
		Role assignedRoleInLeaveFlow = getEmployeeOrTeamLead(userRoles);
		ApprovalLevelModel approvalLevelModel = new ApprovalLevelModel();
		approvalLevelModel.setRole(assignedRoleInLeaveFlow.getRole());
		
		List<LeaveApplicationComment> leaveApplicationCommentList = new ArrayList<LeaveApplicationComment>();
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("approvalLevelModel", approvalLevelModel);
		parameterMap.put("employee", employee);
		parameterMap.put("leaveTransaction", leaveTransaction);
		parameterMap.put("yearlyEntitlement", yearlyEntitlement);
		parameterMap.put("isTeamLeadApproved", new Boolean(false));
		parameterMap.put("isOperDirApproved", new Boolean(false));
		parameterMap.put("role", assignedRoleInLeaveFlow.getRole());
		parameterMap.put("leaveApplicationCommentList", leaveApplicationCommentList);
		parameterMap.put("teamLeadName", null);
		parameterMap.put("leaveTransactionId", new Integer(0));
		long processInstanceId = applyLeaveRuntime.startProcessWithInitialParametersAndFireBusinessRules(PROCESS_NAME, parameterMap);
		
		List<Long> taskIdList = applyLeaveRuntime.getTaskIdsByProcessInstanceId(processInstanceId);
		if(taskIdList.size() == 0) {
			throw new LeaveApplicationException("Ooops! Something serious has happened. Please contact your Administrator");
		}
		
		Long currentTaskId = taskIdList.get(0);
		
		leaveTransaction.setTaskId(currentTaskId);
		
		LeaveTransaction leaveTransactionPersist = getLeaveTransactionService().insertFromWorkflow(leaveTransaction);
		
		parameterMap.put("leaveTransactionId", leaveTransactionPersist.getId());
		
		applyLeaveRuntime.submitTask(employee.getUsers().getUsername(), currentTaskId, parameterMap);
		
	}
	
	
	public JBPM6Runtime getApplyLeaveRuntime() {
		return applyLeaveRuntime;
	}
	public void setApplyLeaveRuntime(JBPM6Runtime applyLeaveRuntime) {
		this.applyLeaveRuntime = applyLeaveRuntime;
	}
	
	public LeaveTransactionService getLeaveTransactionService() {
		return leaveTransactionService;
	}
	public void setLeaveTransactionService(
			LeaveTransactionService leaveTransactionService) {
		this.leaveTransactionService = leaveTransactionService;
	}
	
	
	private Role getEmployeeOrTeamLead(Set<Role> userRoles) {
		Role resultRole = null;
		
		Iterator<Role> roleIterator = userRoles.iterator();
		while(roleIterator.hasNext()) {
			Role currentRole = roleIterator.next();
			
			if(compareRole(currentRole, resultRole)) {
				resultRole = currentRole;
			}
		}
		
		return resultRole;
	}
	
	
	private boolean compareRole(Role firstRole, Role secondRole) {
		
		if(firstRole == null) {
			return false;
		}
		
		if(secondRole == null) {
			return true;
		}
		Integer firstRoleStanding = getRoleStanding(firstRole.getRole());
		Integer secondRoleStanding = getRoleStanding(secondRole.getRole());
		
		if(firstRoleStanding > secondRoleStanding) {
			return true;
		} else {
			return false;
		}
	}
	
	private Integer getRoleStanding(String roleName) {
		if(roleName == null || roleName.equals("")) {
			return -1;
		}
		
		if(roleName.equals("ROLE_EMPLOYEE")) {
			return 5;
		} else if(roleName.equals("ROLE_TEAMLEAD")) {
			return 10;
		}else if(roleName.equals("ROLE_JRHR")) {
			return 15;
		}else if(roleName.equals("ROLE_SRHR")) {
			return 20;
		}
		else if(roleName.equals("ROLE_HR")) {
			return 25;
		}
		else if(roleName.equals("ROLE_PM")) {
			return 30;
		}
		else {
			return -1;
		}
	}
	
	private boolean isEmployee(Set<Role> userRoles) {
		Iterator<Role> roleIterator = userRoles.iterator();
		while(roleIterator.hasNext()) {
			Role currentRole = roleIterator.next();
			if(currentRole.getRole().equals("ROLE_EMPLOYEE")) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<LeaveTransaction> getPendingLeaveRequestsList(String username) {
		List<LeaveTransaction> leaveRequestList = new ArrayList<LeaveTransaction>();
		List<TaskSummary> taskList = applyLeaveRuntime.getTaskAssignedForUser(username);
		Iterator<TaskSummary> taskIterator = taskList.iterator();
		while(taskIterator.hasNext()) {
			TaskSummary currentTaskSummary = taskIterator.next();
			Task currentTask = applyLeaveRuntime.getTaskById(currentTaskSummary.getId());
			Map<String, Object> contentMap = applyLeaveRuntime.getContentForTask(currentTask);
			LeaveTransaction leaveRequestByEmployee = mapTaskSummaryToLeaveApprovalRequest(currentTaskSummary, contentMap);
			leaveRequestList.add(leaveRequestByEmployee);
		}
		return leaveRequestList;
	}

	private LeaveTransaction mapTaskSummaryToLeaveApprovalRequest(TaskSummary taskSummary, Map<String, Object> contentMap) {
		LeaveTransaction leaveRequest = new LeaveTransaction();
		leaveRequest.setTaskId(taskSummary.getId());
		if(contentMap!=null){
			/*Employee leaveAppliedEmployee =(Employee) contentMap.get("employee");
			System.out.println(leaveAppliedEmployee.getName());*/
			LeaveTransaction leaveTransactionFromWorkFlow = 	(LeaveTransaction) contentMap.get("leaveTransaction");
			if(leaveTransactionFromWorkFlow!=null){
			leaveRequest.setApplicationDate(leaveTransactionFromWorkFlow.getApplicationDate());
			leaveRequest.setStartDateTime(leaveTransactionFromWorkFlow.getStartDateTime());
			leaveRequest.setEndDateTime(leaveTransactionFromWorkFlow.getEndDateTime());
			leaveRequest.setLeaveType(leaveTransactionFromWorkFlow.getLeaveType());
			leaveRequest.setNumberOfDays(leaveTransactionFromWorkFlow.getNumberOfDays());
			leaveRequest.setReason(leaveTransactionFromWorkFlow.getReason());
			leaveRequest.setEmployee(leaveTransactionFromWorkFlow.getEmployee());
			leaveRequest.setYearlyLeaveBalance(leaveTransactionFromWorkFlow.getYearlyLeaveBalance());
			leaveTransactionFromWorkFlow.getEmployee().getName();
			}
		}
		return leaveRequest;
	}
	
	@Override
	public void approveLeaveOfEmployee(LeaveTransaction leaveTransaction, String actorId,Set<Role> actorRoles) {
		try{
		HashMap<String, Object> parameterMap = getContentMapFromSelectedLeaveRequest(leaveTransaction);
		
		// Getting actor roles and checking who is approving the current leave request and setting map properties accordingly
		if(parameterMap!=null && "ROLE_EMPLOYEE".equalsIgnoreCase((String)parameterMap.get("role")))
		{
			Set<String> roleSet = new HashSet<String>();
			for (Role role : actorRoles) {
				roleSet.add(role.getRole());
			}
			if(roleSet.contains("ROLE_TEAMLEAD")){
				parameterMap.put("isTeamLeadApproved", new Boolean(true));
				parameterMap.put("teamLeadName", actorId);
			}
			if(roleSet.contains("ROLE_OPERDIR")){
				parameterMap.put("isOperDirApproved", new Boolean(true));
				parameterMap.put("oprDirName", actorId);
			}
			
		}else
		{
			parameterMap.put("isOperDirApproved", new Boolean(true));
			parameterMap.put("oprDirName", actorId);
		}
		
		long taskId = leaveTransaction.getTaskId();
		applyLeaveRuntime.submitTask(actorId, taskId, parameterMap);
		}catch(RoleNotFound e){
			
			log.error("Role Not Found in service ", e);
			throw new BSLException("err.leave.approve.roleNotFound");
		}catch(BSLException e){
		log.error("Error while approving leave in service ", e);
		throw new BSLException(e.getMessage());
		}catch(Exception e){
			log.error("Error while approving leave in service ", e);
			throw new BSLException("err.leave.approve");
		}
		
	}

	private HashMap<String,Object> getContentMapFromSelectedLeaveRequest(LeaveTransaction leaveTransaction) throws RoleNotFound
	{
		Set<Role> userRoles = leaveTransaction.getEmployee().getUsers().getUserRoles();
		
		if(!isEmployee(userRoles)) {
			throw new RoleNotFound("You are not an employee.");
		}
		Role assignedRoleInLeaveFlow = getEmployeeOrTeamLead(userRoles);
		ApprovalLevelModel approvalLevelModel = new ApprovalLevelModel();
		approvalLevelModel.setRole(assignedRoleInLeaveFlow.getRole());
		
		List<LeaveApplicationComment> leaveApplicationCommentList = new ArrayList<LeaveApplicationComment>();
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("approvalLevelModel", approvalLevelModel);
		parameterMap.put("employee", leaveTransaction.getEmployee());
		parameterMap.put("leaveTransaction", leaveTransaction);
		parameterMap.put("isOperDirApproved", new Boolean(false));
		parameterMap.put("role", assignedRoleInLeaveFlow.getRole());
		parameterMap.put("leaveApplicationCommentList", leaveApplicationCommentList);
		return parameterMap;
	}
	@Override
	public void rejectLeaveOfEmployee(LeaveTransaction leaveTransaction, String actorId, Set<Role> userRoles) {
		try{
		HashMap<String, Object> resultMap = getContentMapFromSelectedLeaveRequest(leaveTransaction);
		Set<String> roleSet = new HashSet<String>();
		for (Role role : userRoles) {
			roleSet.add(role.getRole());
		}
		if(roleSet.contains("ROLE_TEAMLEAD")){
			resultMap.put("isTeamLeadApproved", new Boolean(false));
			resultMap.put("teamLeadName", actorId);
		}
		else {
			resultMap.put("isOperDirApproved", new Boolean(false));
			resultMap.put("oprDirName", actorId);
		}
			
		long taskId = leaveTransaction.getTaskId();
		applyLeaveRuntime.submitTask(actorId, taskId, resultMap);
		
		}catch(RoleNotFound e){
			log.error("Role Not Found in service ", e);
			throw new BSLException("err.leave.reject.roleNotFound");
		}catch(BSLException e){
		log.error("Error while rejecting leave in service ", e);
		throw new BSLException(e.getMessage());
		}catch(Exception e){
			log.error("Error while rejecting leave in service ", e);
			throw new BSLException("err.leave.reject");
		}
	}


}
