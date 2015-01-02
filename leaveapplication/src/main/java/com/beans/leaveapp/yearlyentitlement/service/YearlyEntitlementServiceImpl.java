package com.beans.leaveapp.yearlyentitlement.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.beans.common.audit.service.AuditTrail;
import com.beans.exceptions.BSLException;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.repository.EmployeeRepository;
import com.beans.leaveapp.leavetype.model.LeaveType;
import com.beans.leaveapp.leavetype.repository.LeaveTypeRepository;
import com.beans.leaveapp.yearlyentitlement.model.YearlyEntitlement;
import com.beans.leaveapp.yearlyentitlement.repository.YearlyEntitlementRepository;
import com.beans.util.enums.Leave;
import com.beans.util.log.ApplLogger;

public class YearlyEntitlementServiceImpl implements YearlyEntitlementService {

	private Logger log = Logger.getLogger(this.getClass());
	@Resource
	YearlyEntitlementRepository yearlyEntitleRepository;

	@Resource
	EmployeeRepository employeeRepository;

	@Resource
	LeaveTypeRepository leaveTypeRepository;

	YearlyEntitlement yearlyEntitlement = new YearlyEntitlement();
	AuditTrail auditTrail;

	public YearlyEntitlement getYearlyEntitlement() {
		return yearlyEntitlement;
	}

	public void setYearlyEntitlement(YearlyEntitlement yearlyEntitlement) {
		this.yearlyEntitlement = yearlyEntitlement;
	}

	@Override
	public List<YearlyEntitlement> findAll() {
		List<YearlyEntitlement> yearlyEntitlementList = (List<YearlyEntitlement>) yearlyEntitleRepository
				.findByIsDeleted(0);

		return yearlyEntitlementList;
	}

	@Override
	public YearlyEntitlement update(YearlyEntitlement selectedYearlyEntitlement) throws Exception {
		YearlyEntitlement yearlyEntitlementToBeUpdated=null;
		try{
		 yearlyEntitlementToBeUpdated = yearlyEntitleRepository.findOne(selectedYearlyEntitlement.getId());
		if (yearlyEntitlementToBeUpdated != null) {
			yearlyEntitlementToBeUpdated.setcurrentLeaveBalance(selectedYearlyEntitlement.getCurrentLeaveBalance());
			yearlyEntitlementToBeUpdated.setYearlyLeaveBalance(selectedYearlyEntitlement.getYearlyLeaveBalance());
			yearlyEntitlementToBeUpdated.setEntitlement(selectedYearlyEntitlement.getEntitlement());
			yearlyEntitlementToBeUpdated.setLastModifiedBy(selectedYearlyEntitlement.getLastModifiedBy());
			yearlyEntitlementToBeUpdated.setLastModifiedTime(selectedYearlyEntitlement.getLastModifiedTime());
			yearlyEntitleRepository.save(yearlyEntitlementToBeUpdated);
			return yearlyEntitlementToBeUpdated;
		}
		} catch (Exception e) {
			log.error("Error while updating Yearly Entitlement For  :"+yearlyEntitlement.getId(), e);
			throw new BSLException("err.yearlyEntitlement.update", e);
		}
		return yearlyEntitlementToBeUpdated;
	}

	@Override
	public YearlyEntitlement delete(int id) {
		try{
		YearlyEntitlement YearlyEntitlement = yearlyEntitleRepository.findOne(id);
		if (YearlyEntitlement != null) {
			YearlyEntitlement.setDeleted(true);
			yearlyEntitleRepository.save(YearlyEntitlement);
			return YearlyEntitlement;
		}
		} catch (Exception e) {
			log.error("Error while updating Yearly Entitlement For  :"+yearlyEntitlement.getId(), e);
			throw new BSLException("err.yearlyEntitlement.delete", e);
		}
		return null;
	}

	/**/

	@Override
	public List<String> employeeNames() {
		List<String> employeeNamesList = employeeRepository.findByNames();
		return employeeNamesList;
	}

	@Override
	public List<String> findLeaveTypes(int id) {
		
		List<String> leavesNameList = (List<String>) leaveTypeRepository.findByLeaveTypes(id);		
		return leavesNameList;
	}

	@Override
	public YearlyEntitlement create(YearlyEntitlement yearlyEntitlement) {
		try{
		yearlyEntitlement.setDeleted(false);
		YearlyEntitlement yearlyEntitlementObj = yearlyEntitleRepository.save(yearlyEntitlement);
		return yearlyEntitlementObj;
		} catch (Exception e) {
			log.error("Error while updating Yearly Entitlement For  :"+yearlyEntitlement.getId(), e);
			throw new BSLException("err.yearlyEntitlement.create", e);
		}
	}

	@Override
	public Employee findByEmployee(String name) {

		Employee employee = employeeRepository.findByName(name);
		return employee;
	}

	@Override
	public LeaveType findByLeaveType(String name,int employeeTypeId) {
		LeaveType leaveType = leaveTypeRepository.findByName(name,employeeTypeId);
		return leaveType;
	}

	@Override
	public List<YearlyEntitlement> findByEmployeeOrfindByLeaveTypeOrBoth(
			String employeeName, String leaveType) {

		String name = "%" + employeeName.trim() + "%";
		String leaveName = "%" + leaveType.trim() + "%";
		List<YearlyEntitlement> yearlyEntitlementList = new LinkedList<YearlyEntitlement>();
		if (!employeeName.trim().equals("") && !leaveType.trim().equals("")) {
			yearlyEntitlementList = yearlyEntitleRepository.findByEmployeeAndLeaveTypeLike(name, leaveName);

			// yearlyEntitlementList =
			// yearlyEntitleRepository.findByEmployeeOrLeaveTypeLike(employeeName,leaveType);

		} else if (!employeeName.trim().equals("")) {
			yearlyEntitlementList = yearlyEntitleRepository.findByEmployeeLike(name);
		} else if (leaveType!=null && (leaveType.trim() != null || !leaveType.trim().equals(""))) {
			yearlyEntitlementList = yearlyEntitleRepository.findByLeaveTypeLike(leaveName);
		}
		return yearlyEntitlementList;
	}

	@Override
	public List<YearlyEntitlement> findByEmployeeId(int employeeId) {

		List<YearlyEntitlement> listOfYearlyEntitlement = new LinkedList<YearlyEntitlement>();
		try {

			if (employeeId > 0) {
				listOfYearlyEntitlement = yearlyEntitleRepository.findByEmployeeIdNotIncludeUnpaid(employeeId);
				return listOfYearlyEntitlement;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listOfYearlyEntitlement;
	}

	@Override
	public YearlyEntitlement findByEmployeeAndLeaveType(int employeeId,
			int leaveTypeId) throws YearlyEntitlementNotFound {
		List<YearlyEntitlement> resultList = yearlyEntitleRepository.findByEmployeeIdAndLeaveTypeId(employeeId, leaveTypeId);
		
		if(resultList == null || resultList.size() == 0) {
			throw new YearlyEntitlementNotFound("This Employee does not have the entitlement for this leave type");
		}
		return resultList.get(0);
	}

	@Override
	public List<YearlyEntitlement> findYearlyEntitlementListByEmployee(
			int employeeId)  {
		
		return yearlyEntitleRepository.findByEmployeeId(employeeId);
	}

	@Override
	public YearlyEntitlement findOne(int yearlyEntitlementId)
			throws YearlyEntitlementNotFound {
		YearlyEntitlement yearlyEntitlement = yearlyEntitleRepository.findOne(yearlyEntitlementId);
		
		if(yearlyEntitlement == null) {
			throw new YearlyEntitlementNotFound("Can't find Yearly Entitlement with id: " + yearlyEntitlementId);
		}
		return yearlyEntitlement;
	}

	@Override
	public YearlyEntitlement findByEmployeeIdPermAndCont(int employeeId) {
		 YearlyEntitlement yearlyEntitlementPermAndCont = yearlyEntitleRepository.findByEmployeeIdPermAndCont(employeeId);
		return yearlyEntitlementPermAndCont;
	}

	@Override
	public YearlyEntitlement findByEmployeeIdPerm(int employeeId) {
		YearlyEntitlement yearlyEntitlementPerm = yearlyEntitleRepository.findByEmployeeIdPerm(employeeId);
		return yearlyEntitlementPerm;
	}	
	
	@Override
	public List<YearlyEntitlement> findByEmployeeIdForRemainingLeaves(int employeeId) {
		List<YearlyEntitlement> yearlyEntitlementList = yearlyEntitleRepository.findByEmployeeIdPermForRemainingLeaves(employeeId);
		return yearlyEntitlementList;
	}

	@Override
	@Transactional
	public void updateLeaveBalanceAfterApproval(int employeeId,int leaveTypeId,double numberOfDaysLeaveApproved) {
		System.out.println("Input parameters values in updateLeaveBalanceAfterApproval() employeeId: "+employeeId+" leaveTypeId :"+leaveTypeId);
		YearlyEntitlement yearlyEntitlement =	(YearlyEntitlement) yearlyEntitleRepository.findByEmployeeAndLeaveTypeId(employeeId, leaveTypeId);
		yearlyEntitlement.setcurrentLeaveBalance(yearlyEntitlement.getCurrentLeaveBalance()-numberOfDaysLeaveApproved);
		yearlyEntitlement.setYearlyLeaveBalance(yearlyEntitlement.getYearlyLeaveBalance()-numberOfDaysLeaveApproved);
		yearlyEntitleRepository.save(yearlyEntitlement);
		System.out.println("After updating, no'of leaves left is :"+yearlyEntitlement.getCurrentLeaveBalance());
	}
	

	@Override
	@Transactional
	public void updateLeaveBalanceAfterCancelled(int employeeId,int leaveTypeId,double numberOfDaysLeaveCancelled) {
		System.out.println("Input parameters values in updateLeaveBalanceAfterApproval() employeeId: "+employeeId+" leaveTypeId :"+leaveTypeId);
		YearlyEntitlement yearlyEntitlement =	(YearlyEntitlement) yearlyEntitleRepository.findByEmployeeAndLeaveTypeId(employeeId, leaveTypeId);
		yearlyEntitlement.setcurrentLeaveBalance(yearlyEntitlement.getCurrentLeaveBalance()+numberOfDaysLeaveCancelled);
		yearlyEntitlement.setYearlyLeaveBalance(yearlyEntitlement.getYearlyLeaveBalance()+numberOfDaysLeaveCancelled);
		yearlyEntitleRepository.save(yearlyEntitlement);
		System.out.println("After updating, no'of leaves left is :"+yearlyEntitlement.getCurrentLeaveBalance());
	}

	
	
	
	@Override
	public YearlyEntitlement findYearlyEntitlementById(int employeeId, int leaveTypeId) {
		
		return yearlyEntitleRepository.findByEmployeeAndLeaveTypeId(employeeId, leaveTypeId);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void addAllEntitlementsToNewEmployee(Employee newlyRegisteredEmployee) {
		// Add full entitlement for all kinds of leaves except Annual and Unpaid leaves
		final String married="Married";
		try{
		LeaveType leaveTypeBean = null;
		YearlyEntitlement  leaveEntitlement = null;
		
		if("PERM".equalsIgnoreCase(newlyRegisteredEmployee.getEmployeeType().getName())){
		for(Leave leaveType : Leave.values()){
			
			if(!(leaveType.equalsName(Leave.ANNUAL.toString()) || leaveType.equalsName(Leave.UNPAID.toString()) || leaveType.equalsName(Leave.TIMEINLIEU.toString()))){
			// Checking whether the employee is Married or single to add Marriage Leaves
				if(married.equalsIgnoreCase(newlyRegisteredEmployee.getMaritalStatus())){
				if( newlyRegisteredEmployee.getGender().equals("M")&& leaveType.equalsName(Leave.MATERNITY.toString()) || newlyRegisteredEmployee.getGender().equals("F")&& leaveType.equalsName(Leave.PATERNITY.toString()) || leaveType.equalsName(Leave.MARRIAGE.toString()))
					continue;
				leaveTypeBean = leaveTypeRepository.getLeaveTypeByName(leaveType.toString(),newlyRegisteredEmployee.getEmployeeType().getId());
				if(leaveTypeBean!=null){
				leaveEntitlement = new YearlyEntitlement();
				leaveEntitlement.setCreatedBy(leaveTypeBean.getCreatedBy());
				leaveEntitlement.setCreationTime(new Date());
				leaveEntitlement.setcurrentLeaveBalance(leaveTypeBean.getEntitlement());
				leaveEntitlement.setYearlyLeaveBalance(leaveTypeBean.getEntitlement());
				leaveEntitlement.setEntitlement(leaveTypeBean.getEntitlement());
				leaveEntitlement.setEmployee(newlyRegisteredEmployee);
				leaveEntitlement.setDeleted(false);
				leaveEntitlement.setLeaveType(leaveTypeBean);
				yearlyEntitleRepository.save(leaveEntitlement);
				}
			}else{
				if( leaveType.equalsName(Leave.PATERNITY.toString()) || leaveType.equalsName(Leave.MATERNITY.toString()))
				 continue;
				leaveTypeBean = leaveTypeRepository.getLeaveTypeByName(leaveType.toString(),newlyRegisteredEmployee.getEmployeeType().getId());
				if(leaveTypeBean!=null){
				leaveEntitlement = new YearlyEntitlement();
				leaveEntitlement.setCreatedBy(leaveTypeBean.getCreatedBy());
				leaveEntitlement.setCreationTime(new Date());
				leaveEntitlement.setcurrentLeaveBalance(leaveTypeBean.getEntitlement());
				leaveEntitlement.setYearlyLeaveBalance(leaveTypeBean.getEntitlement());
				leaveEntitlement.setEntitlement(leaveTypeBean.getEntitlement());
				leaveEntitlement.setEmployee(newlyRegisteredEmployee);
				leaveEntitlement.setDeleted(false);
				leaveEntitlement.setLeaveType(leaveTypeBean);
				yearlyEntitleRepository.save(leaveEntitlement);
				}
				
			}
		}
			if(leaveType.equalsName(Leave.ANNUAL.toString())){
				leaveTypeBean = leaveTypeRepository.getLeaveTypeByName(leaveType.toString(),newlyRegisteredEmployee.getEmployeeType().getId());
				if(leaveTypeBean!=null){
				leaveEntitlement = new YearlyEntitlement();
				int joinedDate = newlyRegisteredEmployee.getJoinDate().getDate();
				int joinedMonth = newlyRegisteredEmployee.getJoinDate().getMonth();
				if(joinedDate>15){
					leaveEntitlement.setcurrentLeaveBalance(-1);
					leaveEntitlement.setEntitlement(12-joinedMonth-1);
					leaveEntitlement.setYearlyLeaveBalance(12-joinedMonth-1);
				}
				else{
					leaveEntitlement.setcurrentLeaveBalance(0);
					leaveEntitlement.setEntitlement(12-joinedMonth);
					leaveEntitlement.setYearlyLeaveBalance(12-joinedMonth);
				}
					leaveEntitlement.setCreatedBy(leaveTypeBean.getCreatedBy());
					leaveEntitlement.setCreationTime(new Date());
					leaveEntitlement.setDeleted(false);
					leaveEntitlement.setEmployee(newlyRegisteredEmployee);
					leaveEntitlement.setLeaveType(leaveTypeBean);
					yearlyEntitleRepository.save(leaveEntitlement);
				}
			}
			}	
		}
		else if("CONT".equalsIgnoreCase(newlyRegisteredEmployee.getEmployeeType().getName())){
			leaveTypeBean = leaveTypeRepository.getLeaveTypeByName(Leave.ANNUAL.toString(),newlyRegisteredEmployee.getEmployeeType().getId());
			if(leaveTypeBean!=null){
			leaveEntitlement = new YearlyEntitlement();
			int joinedDate = newlyRegisteredEmployee.getJoinDate().getDate();
			int joinedMonth = newlyRegisteredEmployee.getJoinDate().getMonth();
			if(joinedDate>15){
				leaveEntitlement.setcurrentLeaveBalance(-1);
				leaveEntitlement.setEntitlement(12-joinedMonth-1);
				leaveEntitlement.setYearlyLeaveBalance(12-joinedMonth-1);
			}
			else{
				leaveEntitlement.setcurrentLeaveBalance(0);
				leaveEntitlement.setEntitlement(12-joinedMonth);
				leaveEntitlement.setYearlyLeaveBalance(12-joinedMonth);
			}
				leaveEntitlement.setCreatedBy(leaveTypeBean.getCreatedBy());
				leaveEntitlement.setCreationTime(new Date());
				leaveEntitlement.setDeleted(false);
				leaveEntitlement.setEmployee(newlyRegisteredEmployee);
				leaveEntitlement.setLeaveType(leaveTypeBean);
				yearlyEntitleRepository.save(leaveEntitlement);
			}
		
		}
		
		leaveTypeBean = leaveTypeRepository.getLeaveTypeByName(Leave.UNPAID.toString(),newlyRegisteredEmployee.getEmployeeType().getId());
		if(leaveTypeBean!=null){
		leaveEntitlement = new YearlyEntitlement();
		leaveEntitlement.setCreatedBy(leaveTypeBean.getCreatedBy());
		leaveEntitlement.setCreationTime(new Date());
		leaveEntitlement.setcurrentLeaveBalance(leaveTypeBean.getEntitlement());
		leaveEntitlement.setYearlyLeaveBalance(leaveTypeBean.getEntitlement());
		leaveEntitlement.setEntitlement(leaveTypeBean.getEntitlement());
		leaveEntitlement.setEmployee(newlyRegisteredEmployee);
		leaveEntitlement.setDeleted(false);
		leaveEntitlement.setLeaveType(leaveTypeBean);
		yearlyEntitleRepository.save(leaveEntitlement);
		}
		
		leaveTypeBean = leaveTypeRepository.getLeaveTypeByName(Leave.TIMEINLIEU.toString(),newlyRegisteredEmployee.getEmployeeType().getId());
		if(leaveTypeBean!=null){
		leaveEntitlement = new YearlyEntitlement();
		leaveEntitlement.setCreatedBy(leaveTypeBean.getCreatedBy());
		leaveEntitlement.setCreationTime(new Date());
		leaveEntitlement.setcurrentLeaveBalance(0);
		leaveEntitlement.setYearlyLeaveBalance(0);
		leaveEntitlement.setEntitlement(0);
		leaveEntitlement.setEmployee(newlyRegisteredEmployee);
		leaveEntitlement.setDeleted(false);
		leaveEntitlement.setLeaveType(leaveTypeBean);
		yearlyEntitleRepository.save(leaveEntitlement);
		}
		}catch(Exception e){
			e.printStackTrace();
			throw new BSLException("err.newEmployeejoin.creditEntitlement");
			
		}
	}

	@Override
	@Transactional
	public void updateAnnualLeaveBalanceAfterApproval(int employeeId,int leaveTypeId,double numberOfDaysLeaveApproved) {
		ApplLogger.getLogger().info("Input parameters values in updateLeaveBalanceAfterApproval() employeeId: "+employeeId+" leaveTypeId :"+leaveTypeId);
		List<YearlyEntitlement> yearlyEntitlementList =	(List<YearlyEntitlement>) yearlyEntitleRepository.findByEmployeeIdAndEmployeeTypeAndLeaveTypeName(employeeId, Leave.ANNUAL.toString());
		if(yearlyEntitlementList!=null && yearlyEntitlementList.size()>0){
			yearlyEntitlement = yearlyEntitlementList.get(0);
		yearlyEntitlement.setcurrentLeaveBalance(yearlyEntitlement.getCurrentLeaveBalance()+numberOfDaysLeaveApproved);
		yearlyEntitlement.setYearlyLeaveBalance(yearlyEntitlement.getYearlyLeaveBalance()+numberOfDaysLeaveApproved);
		yearlyEntitleRepository.save(yearlyEntitlement);
		System.out.println("After Adding Leaves, no'of leaves left is :"+yearlyEntitlement.getCurrentLeaveBalance());
		}
	}

	@Override
	public YearlyEntitlement findAnnualYearlyEntitlementOfEmployee(int employeeId) {
		List<YearlyEntitlement> yearlyEntitlementList =	(List<YearlyEntitlement>) yearlyEntitleRepository.findByEmployeeIdAndEmployeeTypeAndLeaveTypeName(employeeId, Leave.ANNUAL.toString());
		if(yearlyEntitlementList!=null && yearlyEntitlementList.size()>0){
			yearlyEntitlement = yearlyEntitlementList.get(0);
		}
		return yearlyEntitlement;
	}
	
}
