package com.beans.leaveapp.employee.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;

import com.beans.leaveapp.employee.model.RegisteredEmployee;
import com.beans.leaveapp.jbpm6.util.JBPM6Runtime;

public class EmployeeRegistrationServiceImpl implements
		EmployeeRegistrationService {

	private static final String PROCESS_NAME = "com.beans.leaveapp.bpmn.empreg";
	private JBPM6Runtime employeeRegistrationRuntime;
	
	@Override
	public void submitRegistration(HashMap<String, Object> parameterMap) {		
		employeeRegistrationRuntime.startProcessWithInitialParameters(PROCESS_NAME, parameterMap);		
	}
	
	@Override
	public List<RegisteredEmployee> getPendingRegisteredEmployee(String username) {
		List<RegisteredEmployee> resultList = new ArrayList<RegisteredEmployee>();
		List<TaskSummary> taskList = employeeRegistrationRuntime.getTaskAssignedForUser(username);
		Iterator<TaskSummary> taskIterator = taskList.iterator();
		while(taskIterator.hasNext()) {
			TaskSummary currentTaskSummary = taskIterator.next();
			Task currentTask = employeeRegistrationRuntime.getTaskById(currentTaskSummary.getId());
			Map<String, Object> contentMap = employeeRegistrationRuntime.getContentForTask(currentTask);
			RegisteredEmployee registeredEmployee = mapTaskSummaryToRegisteredEmployee(currentTaskSummary, contentMap);
			resultList.add(registeredEmployee);
		}
		return resultList;
	}

	private RegisteredEmployee mapTaskSummaryToRegisteredEmployee(TaskSummary taskSummary, Map<String, Object> contentMap) {
		RegisteredEmployee registeredEmployee = new RegisteredEmployee();
		registeredEmployee.setTaskId(taskSummary.getId());
		registeredEmployee.setRegistrationDate(taskSummary.getCreatedOn());
		
		if(contentMap != null) {
			String fullname = (String) contentMap.get("fullname");
			registeredEmployee.setFullname(fullname);
			
			String username = (String) contentMap.get("username");
			registeredEmployee.setUsername(username);
			
			String password = (String) contentMap.get("password");
			registeredEmployee.setPassword(password);
			
			String passportNumber = (String) contentMap.get("passportNumber");
			registeredEmployee.setPassportNumber(passportNumber);
			
			String personalEmailAddress = (String) contentMap.get("personalEmailAddress");
			registeredEmployee.setPersonalEmailAddress(personalEmailAddress);
			
			String personalPhoneNumber = (String) contentMap.get("personalPhoneNumber");
			registeredEmployee.setPersonalPhoneNumber(personalPhoneNumber);
			
			String gender = (String) contentMap.get("gender");
			registeredEmployee.setGender(gender);
			
			String idNumber = (String) contentMap.get("idNumber");
			registeredEmployee.setIdNumber(idNumber);
			
			String maritalStatus = (String) contentMap.get("maritalStatus");
			registeredEmployee.setMaritalStatus(maritalStatus);
			
		}
		
		return registeredEmployee;
	}
	
	private HashMap<String, Object> getContentMapFromRegisteredEmployee(RegisteredEmployee registeredEmployee) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		resultMap.put("fullname", registeredEmployee.getFullname());
		resultMap.put("username", registeredEmployee.getUsername());
		resultMap.put("password", registeredEmployee.getPassword());
		resultMap.put("passportNumber", registeredEmployee.getPassportNumber());
		resultMap.put("idNumber", registeredEmployee.getIdNumber());
		resultMap.put("gender", registeredEmployee.getGender());
		resultMap.put("personalPhoneNumber", registeredEmployee.getPersonalPhoneNumber());
		resultMap.put("personalEmailAddress", registeredEmployee.getPersonalEmailAddress());
		resultMap.put("workPhoneNumber", registeredEmployee.getWorkPhoneNumber());
		resultMap.put("workEmailAddress", registeredEmployee.getWorkEmailAddress());
		resultMap.put("workPhoneNumber", registeredEmployee.getWorkPhoneNumber());
		resultMap.put("position", registeredEmployee.getPosition());
		resultMap.put("reason", registeredEmployee.getReason());
		resultMap.put("joinDate", registeredEmployee.getJoinDate());
		resultMap.put("maritalStatus", registeredEmployee.getMaritalStatus());
		resultMap.put("registeredEmployee", registeredEmployee);
		return resultMap;
	}
	
	
	
	@Override
	public void approveRegistration(RegisteredEmployee registeredEmployee,
			String actorId) {
		HashMap<String, Object> resultMap = getContentMapFromRegisteredEmployee(registeredEmployee);
		resultMap.put("isApproved", Boolean.TRUE);
		long taskId = registeredEmployee.getTaskId();
		employeeRegistrationRuntime.submitTask(actorId, taskId, resultMap);
		
	}

	@Override
	public void rejectRegistration(RegisteredEmployee registeredEmployee,
			String actorId) {
		HashMap<String, Object> resultMap = getContentMapFromRegisteredEmployee(registeredEmployee);
		resultMap.put("isApproved", Boolean.FALSE);
		long taskId = registeredEmployee.getTaskId();
		employeeRegistrationRuntime.submitTask(actorId, taskId, resultMap);
		
	}

	public JBPM6Runtime getEmployeeRegistrationRuntime() {
		return employeeRegistrationRuntime;
	}
	public void setEmployeeRegistrationRuntime(
			JBPM6Runtime employeeRegistrationRuntime) {
		this.employeeRegistrationRuntime = employeeRegistrationRuntime;
	}
}
