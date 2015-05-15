package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveflow.service.email.LeaveApplicationEmailNotificationService;

@RestController
@RequestMapping("/protected/leaveApplicationEmailNotification")
public class LeaveApplicationEmailNotificationController {
		
	@RequestMapping(value="/sendingIntimationEmail", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void sendingIntimationEmail(@RequestBody LeaveTransaction leaveTransaction){
		LeaveApplicationEmailNotificationService.sendingIntimationEmail(leaveTransaction);
	}

}
