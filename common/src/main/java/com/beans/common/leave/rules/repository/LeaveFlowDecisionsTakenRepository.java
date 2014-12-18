package com.beans.common.leave.rules.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.beans.common.leave.rules.model.LeaveFlowDecisionsTaken;

public interface LeaveFlowDecisionsTakenRepository extends CrudRepository<LeaveFlowDecisionsTaken, Integer>,JpaRepository<LeaveFlowDecisionsTaken, Integer>{

	 @Query("select l from LeaveFlowDecisionsTaken l where isDeleted = ?")
	 List<LeaveFlowDecisionsTaken> findByIsDeleted(int x);
	
	@Query("select l from LeaveFlowDecisionsTaken l where leaveFlowDecisionTakenId=? and isDeleted=0")
	LeaveFlowDecisionsTaken findByRuleId(int leaveFlowDecisionId);
	
}
