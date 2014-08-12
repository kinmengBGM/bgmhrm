package com.beans.leaveapp.monthlyreport.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.beans.leaveapp.monthlyreport.model.LeaveDeductHistory;

public interface LeaveDeductHistoryRepository extends CrudRepository<LeaveDeductHistory, Integer> {

	@Query("select l from LeaveDeductHistory l where employee.id=? and financialYear=?")
	public LeaveDeductHistory getLeavesDeductionByIdAndYear(int employeeId,int financialYear);
}
