package com.beans.leaveapp.monthlyreport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.beans.leaveapp.monthlyreport.model.MonthlyLeaveReport;

public interface MonthlyLeaveReportRepository extends CrudRepository<MonthlyLeaveReport, Integer>{

	@Query("select m from MonthlyLeaveReport m where employee.id=:employeeId and financialYear=:financialYear and leaveType.id=:leaveTypeId ORDER BY sortingMonthId ASC")
	public List<MonthlyLeaveReport> findAllLeaveReportsOfYearByIdAndLeaveId(@Param("employeeId") Integer employeeId,@Param("leaveTypeId") Integer leaveTypeId,@Param("financialYear") Integer financialYear);

}
