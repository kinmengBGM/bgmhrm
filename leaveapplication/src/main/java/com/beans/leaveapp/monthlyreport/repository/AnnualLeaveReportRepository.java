package com.beans.leaveapp.monthlyreport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.beans.leaveapp.monthlyreport.model.AnnualLeaveReport;

public interface AnnualLeaveReportRepository extends CrudRepository<AnnualLeaveReport, Integer> {

	@Query("select a from AnnualLeaveReport a where employee.id=:employeeId and financialYear=:financialYear ORDER BY sortingMonthId ASC")
	public List<AnnualLeaveReport> getAnnualLeaveDataOfEmployee(@Param("employeeId") int employeeId,@Param("financialYear") int financialYear);
}
