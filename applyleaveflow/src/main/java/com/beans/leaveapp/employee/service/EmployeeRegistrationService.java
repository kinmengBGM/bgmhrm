package com.beans.leaveapp.employee.service;

import java.util.HashMap;
import java.util.List;

import com.beans.leaveapp.employee.model.RegisteredEmployee;

public interface EmployeeRegistrationService {
	public void submitRegistration(HashMap<String, Object> parameterMap);
	public List<RegisteredEmployee> getPendingRegisteredEmployee(String username);
	public void approveRegistration(RegisteredEmployee registeredEmployee, String actorId);
	public void rejectRegistration(RegisteredEmployee registeredEmployee, String actorId);
}
