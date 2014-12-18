package com.beans.leaveapp.leavetransaction.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.beans.common.leave.rules.model.LeaveFlowDecisionsTaken;
import com.beans.common.leave.rules.model.LeaveRuleBean;
import com.beans.common.leave.rules.repository.LeaveFlowDecisionsTakenRepository;
import com.beans.common.leave.rules.repository.LeaveRuleBeanRepository;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.repository.EmployeeRepository;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.leaveapp.leavetransaction.repository.LeaveTransactionRepository;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.beans.leaveapp.leavetype.repository.LeaveTypeRepository;
import com.beans.util.log.ApplLogger;

public class LeaveTransactionServiceImpl implements LeaveTransactionService {

	@Resource
	LeaveTransactionRepository leaveTransactionRepository;

	@Resource
	LeaveTypeRepository leaveTypeRepository;

	@Resource
	EmployeeRepository employeeRepository;
	
	@Resource
	LeaveRuleBeanRepository leaveRuleBeanRepository;
	
	@Resource
	LeaveFlowDecisionsTakenRepository leaveFlowDecisionRepository;
	
	
	@Override
	public List<LeaveTransaction> findAll() {
		List<LeaveTransaction> ls = leaveTransactionRepository.findAll(0);
		System.out.println(ls.size());
		return ls;
	}


	@Override
	public List<String> findEmployeeNames() {
		List<String> employees = (List<String>) employeeRepository.findByNames();
				
		return employees;
	}

	@Override
	public List<String> findLeaveTypes(String name) {
		
		List<String> leaveTypeNames  = new LinkedList<String>();
		
		try{
			if(employeeRepository.findByEmployeeTypeId(name) != null){
		leaveTypeNames = leaveTypeRepository.findByLeaveTypes(this.employeeRepository.findByEmployeeTypeId(name));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return leaveTypeNames;
	}

	@Override
	public int create(LeaveTransaction leaveTransaction) {

		try {
			if (leaveTransaction != null) {
				leaveTransaction.setApplicationDate(new Date());
				LeaveTransaction leaveTransactionObj = leaveTransactionRepository.save(leaveTransaction);
				return leaveTransactionObj.getId();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void update(LeaveTransaction leaveTransaction) {
		try {
			if (leaveTransaction != null) {
				leaveTransactionRepository.save(leaveTransaction);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	@Override
	public void delete(int id) {
		try{
		System.out.println("Caught error with transaction id and removing : "+id);
		leaveTransactionRepository.delete(id);
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	@Override
	public Employee findByEmployee(String name) {
		Employee employee = employeeRepository.findByName(name);
		return employee;
	}


	@Override
	public LeaveType findByLeaveType(String name,int employeeId) {
		
		LeaveType leaveType = leaveTypeRepository.findByName(name,employeeId);
		return leaveType;
	}



	@Override
	public LeaveTransaction insertFromWorkflow(LeaveTransaction leaveTransaction) {
		
		return leaveTransactionRepository.save(leaveTransaction);
	}


	@Override
	public List<LeaveTransaction> findByStatus(String status) {
		String employeeStatus = "%"+status+"%";
		List<LeaveTransaction> leaveTransaction = leaveTransactionRepository.findByStatusLike(employeeStatus);
		return leaveTransaction;
	}


	@Override
	public List<LeaveTransaction> findByEmployeeORfindByLeaveTypeORLeaveDatesORStatusORAll(
			String employeeName, String leaveType, Date startDate, String status) {

		String name = "%"+ employeeName.trim()+"%";
		String leaveName = "%"+ leaveType.trim()+"%";
		String leaveStatus = "%"+ status.trim() +"%";
		java.sql.Date date = null ;
		if(startDate != null){
	     date = new java.sql.Date(startDate.getTime());
		}
		List<LeaveTransaction> leaveTransaction = null;
		
		 if(!employeeName.trim().equals("") && !leaveType.trim().equals("") && startDate != null && !status.trim().equals("")){
			 leaveTransaction =	leaveTransactionRepository.findByEmployeeAndLeaveTypeAndLeaveDatesAndStatusLike(name,leaveName,date,leaveStatus);
		    	   	
		    	
		    }else if(!employeeName.trim().equals("") && !leaveType.trim().equals("") && startDate != null){
		    	leaveTransaction = leaveTransactionRepository.findByEmployeeAndLeaveTypeAndStartDateLike(name, leaveName, date);
		    }
		 else if(!employeeName.trim().equals("") && !leaveType.trim().equals("") && !leaveStatus.trim().equals("")){
			 leaveTransaction = leaveTransactionRepository.findByEmployeeAndLeaveTypeAndStatusLike(name, leaveName,leaveStatus);
		    }
		 else if(!employeeName.trim().equals("") && !leaveType.trim().equals("")){
		  
		    	//leaveTransaction = leaveTransactionRepository.findByEmployeeLike(name,s);
		    	leaveTransaction =	leaveTransactionRepository.findByEmployeeAndLeaveTypeLike(name, leaveName);
		    	
		    }else if(!employeeName.trim().equals("") && !status.trim().equals(""))
		    {
		    	leaveTransaction = leaveTransactionRepository.findByEmployeeAndStatusLike(name, leaveStatus);
		    }else if(!leaveType.trim().equals("") && !status.trim().equals("")){
		    	leaveTransaction = leaveTransactionRepository.findByLeaveTypeAndStatusLike(leaveName, leaveStatus);
		    }else if(!employeeName.trim().equals("") && date != null){
		    	leaveTransaction = leaveTransactionRepository.findByEmployeeNameAndStartDate(name, date);
		    }else if(!leaveType.trim().equals("") && startDate != null){
		    	leaveTransaction = leaveTransactionRepository.findByLeaveTypeAndStartDate(leaveName,date);
		    }else if(startDate!= null && !status.trim().equals("")){
		    	leaveTransaction = leaveTransactionRepository.findByStartDateTimeAndStatusLike(date, leaveStatus);
		    }
		    else if(!employeeName.trim().equals("")){
		    	leaveTransaction = leaveTransactionRepository.findByEmployeeLike(name);
		    }else if(!leaveType.trim().equals("")){
		    	leaveTransaction = leaveTransactionRepository.findByLeaveTypeLike(leaveName);
		    }else if(!status.trim().equals("")){
		    	leaveTransaction = leaveTransactionRepository.findByStatusLike(leaveStatus);
		    }else if(date != null){
		    	leaveTransaction = leaveTransactionRepository.findByStartDateTime(date);
		    }
		 
			return leaveTransaction;
		
	}

	@Override
	public List<LeaveTransaction> findByEmployeeORfindByLeaveType(String employeeName, String leaveType) {

		String name = null;
		String leaveName =null;
		if(!employeeName.isEmpty())
			name = "%"+ employeeName.trim()+"%";
		if(!leaveType.isEmpty())
		leaveName =  "%"+ leaveType.trim()+"%";
		List<LeaveTransaction> leaveTransaction = null;
		
		 if(name==null && leaveName==null ){
			 leaveTransaction =	leaveTransactionRepository.findAllApprovedLeavesOfEmployees();

		 }else if(name==null && leaveName!=null){
		    	leaveTransaction = leaveTransactionRepository.findByLeaveTypeLike(leaveName);
		 }
		 else if(name!=null && leaveName==null){
			 leaveTransaction = leaveTransactionRepository.findByEmployeeLike(name);
		 }
		 else{
			 leaveTransaction=leaveTransactionRepository.findByEmployeeAndLeaveTypeLike(name, leaveName);
		 }
		 if(leaveTransaction==null)
			 leaveTransaction = new ArrayList<LeaveTransaction>();
		 
			return leaveTransaction;
		
	}

	
	@Override
	public LeaveTransaction updateLeaveApplicationStatus(LeaveTransaction leaveTransaction) {
	LeaveTransaction  leaveTransactionPersist =	leaveTransactionRepository.findOne(leaveTransaction.getId());
	leaveTransactionPersist.setStatus(leaveTransaction.getStatus());
	leaveTransactionPersist.setRejectReason(leaveTransaction.getRejectReason());
	leaveTransactionPersist.setLastModifiedBy(leaveTransaction.getLastModifiedBy());
	leaveTransactionPersist.setLastModifiedTime(leaveTransaction.getLastModifiedTime());
	leaveTransactionPersist.setYearlyLeaveBalance(leaveTransaction.getYearlyLeaveBalance());
	return leaveTransactionRepository.saveAndFlush(leaveTransactionPersist);
	
	}

	@Override
	public LeaveTransaction findById(int id) {
		LeaveTransaction leaveTransaction = leaveTransactionRepository.findById(id);
		return leaveTransaction;
	}
	public List<LeaveTransaction> getAllFutureLeavesAppliedByEmployee(int userId, java.sql.Date todayDate) {
		return leaveTransactionRepository.findAllFutureLeavesOfEmployee(userId, todayDate);
	}

	public List<LeaveTransaction> getAllApprovedLeavesAppliedByEmployee() {
		return leaveTransactionRepository.findAllApprovedLeavesOfEmployees();
	}


	@Override
	public List<LeaveTransaction> getAllLeavesAppliedByEmployee(int userId) {
		return leaveTransactionRepository.findAllLeavesHistoryOfEmployee(userId);
	}
	
	@Override
	public List<LeaveTransaction> getAllLeavesAppliedByEmployee() {
		return leaveTransactionRepository.findAllPendingLeavesOfEmployees();
	}


	@Override
	public LeaveRuleBean getLeaveRuleByRoleAndLeaveType(String leaveType, List<String> roleType) {
		List<LeaveRuleBean> leaveRuleList=null;
		List<String> defaultRoleList = new ArrayList<String>();
		defaultRoleList.add("ROLE_USER");
		defaultRoleList.add("ROLE_EMPLOYEE");
		
		int count = leaveRuleBeanRepository.findApplicantInApproverList(leaveType, roleType);
		if(count==0){
			// Applicant is not an approver any more, now check the exact path based on special roles by him
			if(roleType.removeAll(defaultRoleList) && roleType.size()==0){
				// Applicant is Normal employee, so can take the default path
				leaveRuleList = leaveRuleBeanRepository.findRuleByLeaveandListOfRoleType(leaveType, defaultRoleList);
			}else{
				leaveRuleList = leaveRuleBeanRepository.findRuleByLeaveandListOfRoleType(leaveType, roleType);
			}
			
		}else{
			// Applicant is an approver any more, now check the exact role which is approver role
			
			leaveRuleList = leaveRuleBeanRepository.findRuleByApplicantApproverRole(leaveType, roleType);
		}
		// if more than one leave rule found, then find the minimum levels role and return it.
		if(leaveRuleList!=null && leaveRuleList.size()>0)
		{
			if(leaveRuleList.size()==1){
				LeaveRuleBean leaveRuleBean = leaveRuleList.get(0);
				ApplLogger.getLogger().info("Leave rule found and approval path is : "+leaveRuleBean.getRoleType()+"==>"+leaveRuleBean.getApproverNameLevel1()+"==>"+leaveRuleBean.getApproverNameLevel2()+"==>"+leaveRuleBean.getApproverNameLevel3()+"==>"+leaveRuleBean.getApproverNameLevel4()+"==>"+leaveRuleBean.getApproverNameLevel5());
				return leaveRuleBean;
			}
			LeaveRuleBean leaveRuleBean = null;
			int totalLevels = 0;
			for (LeaveRuleBean leaveRule : leaveRuleList) {
				int levelsOfApproval =0;
				if(StringUtils.trimToNull(leaveRule.getApproverNameLevel1())!=null){
					++levelsOfApproval;
					if(StringUtils.trimToNull(leaveRule.getApproverNameLevel2())!=null){
						++levelsOfApproval;
						if(StringUtils.trimToNull(leaveRule.getApproverNameLevel3())!=null){
							++levelsOfApproval;
							if(StringUtils.trimToNull(leaveRule.getApproverNameLevel4())!=null){
								++levelsOfApproval;
								if(StringUtils.trimToNull(leaveRule.getApproverNameLevel5())!=null){
									++levelsOfApproval;
								}
							}
						}
					}
				}
				
				if(levelsOfApproval>=0){
					if(totalLevels==0){
						leaveRuleBean = leaveRule;
						totalLevels = levelsOfApproval;
					}
					if(levelsOfApproval<=totalLevels){
						leaveRuleBean = leaveRule;
						totalLevels = levelsOfApproval;
					}
				}
			}
			ApplLogger.getLogger().info("Leave rule found and approval path is : "+leaveRuleBean.getRoleType()+"==>"+leaveRuleBean.getApproverNameLevel1()+"==>"+leaveRuleBean.getApproverNameLevel2()+"==>"+leaveRuleBean.getApproverNameLevel3()+"==>"+leaveRuleBean.getApproverNameLevel4()+"==>"+leaveRuleBean.getApproverNameLevel5());
			return leaveRuleBean;
		}
			
		return null;
	}
	
	public LeaveFlowDecisionsTaken saveLeaveApprovalDecisions(LeaveFlowDecisionsTaken leaveFlowDecisions){
		if(leaveFlowDecisions==null)
			leaveFlowDecisions = leaveFlowDecisionRepository.saveAndFlush(new LeaveFlowDecisionsTaken());
		else
			leaveFlowDecisions = leaveFlowDecisionRepository.saveAndFlush(leaveFlowDecisions);
		return leaveFlowDecisions;
	}


	@Override
	public LeaveTransaction processAppliedLeaveOfEmployee(LeaveTransaction leaveTransaction) {
		return leaveTransactionRepository.saveAndFlush(leaveTransaction);
	}
}


