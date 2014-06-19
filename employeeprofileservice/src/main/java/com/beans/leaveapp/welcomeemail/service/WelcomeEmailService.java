package com.beans.leaveapp.welcomeemail.service;

import com.beans.leaveapp.employee.model.Employee;



public interface WelcomeEmailService {
	
	public void sendHTMLEmail(Employee employee);
	
}
