package com.beans.leaveflow.service.email;

import java.util.Date;

import com.beans.common.leave.rules.model.LeaveFlowDecisionsTaken;
import com.beans.common.leave.rules.model.LeaveRuleBean;
import com.beans.exceptions.BSLException;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.beans.util.enums.Leave;

public class LeaveApplicationEmailNotificationService {

	public static void sendingIntimationEmail(LeaveTransaction leaveTransaction)
	{   
		try{
		EmailNotificationPreparingServiceImpl sendMailService = new EmailNotificationPreparingServiceImpl();
		if(sendMailService.getCurrentLevelDecisionByApprover(leaveTransaction)==null){
			sendMailService.sendEmailNotificationToLeaveApplicant(leaveTransaction);
			sendMailService.sendEmailNotificationToLeaveApprover(leaveTransaction);
		}else{
			if("NO".equalsIgnoreCase(sendMailService.getCurrentLevelDecisionByApprover(leaveTransaction)) || sendMailService.getTotalNumberofLevelsDecisionTaken(leaveTransaction)==sendMailService.getTotalLevelsOfApprovalRequired(leaveTransaction)){
				sendMailService.sendEmailNotificationToLeaveApplicant(leaveTransaction);
				sendMailService.sendEmailNotificationToHR(leaveTransaction);
			}else
				sendMailService.sendEmailNotificationToLeaveApprover(leaveTransaction);
		}
		}catch(BSLException e)
		{
			e.printStackTrace();
			throw new BSLException(e.getMessage());
		}
	}
	
	public static void main(String[] args){
		Employee employee = new Employee();
		employee.setName("Lakshminarayana R");
		employee.setWorkEmailAddress("rlnarayana4java@gmail.com");
		LeaveType leaveType = new LeaveType();
		leaveType.setDescription("Annaul Leave");
		leaveType.setId(2);
		LeaveRuleBean leaveRuleBean = new LeaveRuleBean();
		leaveRuleBean.setId(6);
		leaveRuleBean.setLeaveType(Leave.ANNUAL.toString());
		leaveRuleBean.setRoleType("ROLE_EMP");
		leaveRuleBean.setApproverNameLevel1("ROLE_TL");
		leaveRuleBean.setApproverNameLevel2("ROLE_PL");
		/*leaveRuleBean.setApproverNameLevel3("ROLE_PM");
		leaveRuleBean.setApproverNameLevel4("ROLE_HEAD");*/
		//leaveRuleBean.setApproverNameLevel4("ROLE_BOSS");
		LeaveFlowDecisionsTaken decisionsBean = new LeaveFlowDecisionsTaken();
		decisionsBean.setLeaveFlowDecisionTakenId(15);
		decisionsBean.setDecisionLevel1("YES");
		decisionsBean.setDecisionLevel2("NO");
		/*decisionsBean.setDecisionLevel3(YES);
		decisionsBean.setDecisionLevel4(YES);*/
		decisionsBean.setDecisionUserLevel1("Tester1");
		decisionsBean.setDecisionUserLevel2("Tester2");
		/*decisionsBean.setDecisionUserLevel3("Tester3");
		decisionsBean.setDecisionUserLevel4("Tester4");*/
		LeaveTransaction leave = new LeaveTransaction(106, new Date(), new Date(),new Date(), new Double(7), new Double(1), "Marriage", leaveType, employee, "Pending", null, leaveRuleBean, decisionsBean, false);
		leave.setRejectReason("have to work");
		//LeaveTransaction leave = new LeaveTransaction();
		sendingIntimationEmail(leave);
	}

	
	
	
	
	
	
	
	
}
