package com.beans.leaveapp.registeredemployeeemail.service;

import com.beans.leaveapp.employee.model.RegisteredEmployee;



public interface RegisteredEmployeeEmailService {
	
	 void sendWelcomeEmail(RegisteredEmployee RegisteredEmployee);
	 void sendRejectedEmail(RegisteredEmployee RegisteredEmployee);
	
}
