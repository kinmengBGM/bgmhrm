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
import org.springframework.data.domain.Auditable;

import com.beans.common.security.role.model.Role;
import com.beans.common.security.role.service.RoleNotFound;
import com.beans.exceptions.BSLException;
import com.beans.leaveapp.applyleave.model.ApprovalLevelModel;
import com.beans.leaveapp.applyleave.model.TimeInLieuBean;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.jbpm6.util.JBPM6Runtime;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetransaction.service.LeaveTransactionService;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;
import com.beans.util.enums.Leave;

public class LeaveApplicationServiceImpl implements LeaveApplicationService {

	private Logger log = Logger.getLogger(this.getClass());
	private static final String PROCESS_NAME = "com.beans.leaveapp.bpmn.applyleave";
	private JBPM6Runtime applyLeaveRuntime;
	private LeaveTransactionService leaveTransactionService;
	
	@Override
	public void submitLeave(Employee employee,YearlyEntitlement yearlyEntitlement,LeaveTransaction leaveTransaction) throws RoleNotFound, LeaveApplicationException{
		Long currentTaskId=new Long(1);
		try{
		Set<Role> userRoles = employee.getUsers().getUserRoles();
		
		if(!isEmployee(userRoles)) {
			throw new RoleNotFound("You are not an employee.");
		}
		Role assignedRoleInLeaveFlow = getHighestRoleOfEmployee(userRoles);
		ApprovalLevelModel approvalLevelModel = new ApprovalLevelModel();
		approvalLevelModel.setRole(assignedRoleInLeaveFlow.getRole());
		
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("approvalLevelModel", approvalLevelModel);
		parameterMap.put("leaveTransaction", leaveTransaction);
		parameterMap.put("timeInLieuBean", null);
		long processInstanceId = applyLeaveRuntime.startProcessWithInitialParametersAndFireBusinessRules(PROCESS_NAME, parameterMap);
		
		List<Long> taskIdList = applyLeaveRuntime.getTaskIdsByProcessInstanceId(processInstanceId);
		if(taskIdList.size() == 0) {
			throw new LeaveApplicationException("Ooops! Something serious has happened. Please contact your Administrator");
		}
		try{
		currentTaskId = taskIdList.get(0);
		leaveTransaction.setTaskId(currentTaskId);
		LeaveTransaction leaveTransactionPersist = getLeaveTransactionService().insertFromWorkflow(leaveTransaction);
		leaveTransaction.setId(leaveTransactionPersist.getId());
		parameterMap.put("leaveTransaction", leaveTransaction);
		applyLeaveRuntime.submitTask(employee.getUsers().getUsername(), currentTaskId, parameterMap);
		}catch(Exception e){
			log.error("Error is handled by Leave approver :", e);
		}
		}catch(Exception e){
			log.error("Following exception caught while applying for a leave :", e);
			throw new BSLException("error.leaveapp.leaveapply");
		}
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
	
	
	private Role getHighestRoleOfEmployee(Set<Role> userRoles) {
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
			return 15;
		}else if(roleName.equals("ROLE_JRHR")) {
			return 10;
		}else if(roleName.equals("ROLE_SRHR")) {
			return 10;
		}
		else if(roleName.equals("ROLE_HR")) {
			return 10;
		}
		else if(roleName.equals("ROLE_PM")) {
			return 10;
		}
		else if(roleName.equals("ROLE_OPERDIR")) {
			return 20;
		}
		else if(roleName.equals("ROLE_MANGDIR")) {
			return 20;
		}
		else if(roleName.equals("ROLE_DIR")) {
			return 20;
		}
		else if(roleName.equals("ROLE_PROJCOR")) {
			return 10;
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
			leaveRequest.setTimings(leaveTransactionFromWorkFlow.getTimings());
			leaveRequest.setReason(leaveTransactionFromWorkFlow.getReason());
			leaveRequest.setEmployee(leaveTransactionFromWorkFlow.getEmployee());
			leaveRequest.setYearlyLeaveBalance(leaveTransactionFromWorkFlow.getYearlyLeaveBalance());
			leaveRequest.setId(leaveTransactionFromWorkFlow.getId());
			leaveTransactionFromWorkFlow.getEmployee().getName();
			}
		}
		return leaveRequest;
	}
	
	@Override
	public void approveLeaveOfEmployee(LeaveTransaction leaveTransaction, String actorId,String level) {
		long taskId = 0;
		try{
			HashMap<String, Object> parameterMap = new HashMap<String, Object>();
			TimeInLieuBean timeInLieu = null;
			Task currentTask = applyLeaveRuntime.getTaskById(leaveTransaction.getTaskId());
			Map<String, Object> contentMap = applyLeaveRuntime.getContentForTask(currentTask);
			ApprovalLevelModel approvalLevelModel =(ApprovalLevelModel) contentMap.get("approvalLevelModel");
			Set<Role> userRoles = 	leaveTransaction.getEmployee().getUsers().getUserRoles();
			
			Role highestRole =	getHighestRoleOfEmployee(userRoles);
			
			if("FIRST".equalsIgnoreCase(level)){
				timeInLieu = new TimeInLieuBean();
				timeInLieu.setFirstApprover(actorId);
				timeInLieu.setIsFirstApproverApproved(Boolean.TRUE);
				if(Leave.TIMEINLIEU.equalsName(leaveTransaction.getLeaveType().getName()) && "ROLE_EMPLOYEE".equalsIgnoreCase(highestRole.getRole()))
					timeInLieu.setIsTwoLeveApproval(Boolean.TRUE);
			}
			else{
				timeInLieu = (TimeInLieuBean) contentMap.get("timeInLieuBean");
				timeInLieu.setSecondApprover(actorId);
				timeInLieu.setIsSecondApproverApproved(Boolean.TRUE);
			}
			parameterMap.put("approvalLevelModel", approvalLevelModel);
			parameterMap.put("leaveTransaction", leaveTransaction);
			parameterMap.put("timeInLieuBean", timeInLieu);
		 taskId = leaveTransaction.getTaskId();
		applyLeaveRuntime.submitTask(actorId, taskId, parameterMap);
		}catch(Exception e){
			// Terminating the failed task
			applyLeaveRuntime.terminateTask(taskId, actorId);
			// Sending mail to leave applicant to reapply leave
			LeaveApplicationWorker.sendTerminateMail(leaveTransaction.getEmployee().getName(), leaveTransaction.getEmployee().getWorkEmailAddress());
			// Removing the line item from the LeaveTransaction Table as well
			getLeaveTransactionService().delete(leaveTransaction.getId());
			log.info("Error while approving leave and process is getting terminated "+taskId);
			throw new BSLException("error.leaveapp.terminate");
		}
		
	}

	
	@Override
	public void rejectLeaveOfEmployee(LeaveTransaction leaveTransaction, String actorId) {
		long taskId=0;
		try{
			HashMap<String, Object> parameterMap = new HashMap<String, Object>();
			Task currentTask = applyLeaveRuntime.getTaskById(leaveTransaction.getTaskId());
			Map<String, Object> contentMap = applyLeaveRuntime.getContentForTask(currentTask);
			TimeInLieuBean timeInLieuBean =(TimeInLieuBean) contentMap.get("timeInLieuBean");
			ApprovalLevelModel approvalLevelModel =(ApprovalLevelModel) contentMap.get("approvalLevelModel");
			if(timeInLieuBean!=null){
				timeInLieuBean.setIsSecondApproverApproved(Boolean.FALSE);
				timeInLieuBean.setSecondApprover(actorId);
			}
			else
			{
				timeInLieuBean = new TimeInLieuBean();
				timeInLieuBean.setIsFirstApproverApproved(Boolean.FALSE);
				timeInLieuBean.setFirstApprover(actorId);
			}
			parameterMap.put("leaveTransaction", leaveTransaction);
			parameterMap.put("timeInLieuBean", timeInLieuBean);
			parameterMap.put("approvalLevelModel", approvalLevelModel);
		taskId = leaveTransaction.getTaskId();
		applyLeaveRuntime.submitTask(actorId, taskId, parameterMap);
		}catch(Exception e){
			// Terminating the failed task
			applyLeaveRuntime.terminateTask(taskId, actorId);
			// Sending mail to leave applicant to reapply leave
			LeaveApplicationWorker.sendTerminateMail(leaveTransaction.getEmployee().getName(), leaveTransaction.getEmployee().getWorkEmailAddress());
			// Removing the line item from the LeaveTransaction Table as well
			getLeaveTransactionService().delete(leaveTransaction.getId());
			log.info("Error while rejecting leave and process is getting terminated "+taskId);
			throw new BSLException("error.leaveapp.terminate");
		}
	}
	
	@Override
	public Map<String, Object> getContentMapDataByTaskId(long taskId){
		Task currentTask = applyLeaveRuntime.getTaskById(taskId);
		return applyLeaveRuntime.getContentForTask(currentTask);
	}

	
	
}
