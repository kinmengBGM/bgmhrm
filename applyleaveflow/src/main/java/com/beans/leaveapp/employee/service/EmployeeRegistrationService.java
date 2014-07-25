package com.beans.leaveapp.employee.service;

import java.util.HashMap;
import java.util.List;

import com.beans.leaveapp.employee.model.RegisteredEmployee;

public interface EmployeeRegistrationService {
	 void submitRegistration(HashMap<String, Object> parameterMap);
	 List<RegisteredEmployee> getPendingRegisteredEmployee(String username);
	 void approveRegistration(RegisteredEmployee registeredEmployee, String actorId);
	 void rejectRegistration(RegisteredEmployee registeredEmployee, String actorId);
}
