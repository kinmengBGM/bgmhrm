package com.beans.leaveapp.registeredemployeeemail.service;

import com.beans.leaveapp.employee.model.RegisteredEmployee;



public interface RegisteredEmployeeEmailService {
	
	public void sendWelcomeEmail(RegisteredEmployee RegisteredEmployee);
	public void sendRejectedEmail(RegisteredEmployee RegisteredEmployee);
	
}
