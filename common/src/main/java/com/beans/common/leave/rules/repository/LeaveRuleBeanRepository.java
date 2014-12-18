package com.beans.common.leave.rules.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.beans.common.leave.rules.model.LeaveRuleBean;

public interface LeaveRuleBeanRepository extends CrudRepository<LeaveRuleBean, Integer>{

	 @Query("select l from LeaveRuleBean l where isDeleted = ?")
	 List<LeaveRuleBean> findByIsDeleted(int x);
	
	@Query("select l from LeaveRuleBean l where id=? and isDeleted=0")
	LeaveRuleBean findByRuleId(int ruleId);
	
	
	@Query("select l from LeaveRuleBean l where l.leaveType = :leaveType and l.roleType in :roleType")
	List<LeaveRuleBean> findRuleByLeaveandListOfRoleType(@Param("leaveType") String leaveType,@Param("roleType") List<String> roleType);
	
	@Query("select l from LeaveRuleBean l where l.leaveType = :leaveType and l.roleType=:roleType")
	LeaveRuleBean findRuleByLeaveandRoleType(@Param("leaveType") String leaveType,@Param("roleType") String roleType);
	
	
	@Query("select count(*) from LeaveRuleBean l where l.leaveType = :leaveType and l.approverNameLevel1 in :inputList")
	int findApplicantInApproverList(@Param("leaveType") String leaveType,@Param("inputList") List<String> inputList);
	
	
	@Query("select l from LeaveRuleBean l where l.leaveType = :leaveType and l.roleType in (select k.approverNameLevel1 from LeaveRuleBean k where k.leaveType = :leaveType and k.approverNameLevel1 in (:inputRoles))")
	List<LeaveRuleBean> findRuleByApplicantApproverRole(@Param("leaveType") String leaveType,@Param("inputRoles") List<String> inputRoles);
	
}
