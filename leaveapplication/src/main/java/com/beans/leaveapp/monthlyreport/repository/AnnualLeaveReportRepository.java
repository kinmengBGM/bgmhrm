package com.beans.leaveapp.monthlyreport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.beans.leaveapp.monthlyreport.model.AnnualLeaveReport;

public interface AnnualLeaveReportRepository extends CrudRepository<AnnualLeaveReport, Integer> {

	@Query("select a from AnnualLeaveReport a where employee.id=:employeeId and financialYear=:financialYear ORDER BY sortingMonthId ASC")
	public List<AnnualLeaveReport> getAnnualLeaveDataOfEmployee(@Param("employeeId") int employeeId,@Param("financialYear") int financialYear);
	
	@Query("select a from AnnualLeaveReport a where employee.id=:employeeId and sortingMonthId in (:sortingMonthId,13) and financialYear=:financialYear ORDER BY sortingMonthId ASC")
	public List<AnnualLeaveReport> getEmployeeMonthlyLeaveReportData(@Param("employeeId") int employeeId, @Param("sortingMonthId") int sortingMonthId,@Param("financialYear") int financialYear);
	
	@Query("select a from AnnualLeaveReport a where employee.id = ? and sortingMonthId = ? and financialYear = ?")
	public AnnualLeaveReport findAnnualLeaveReportByEmployeeId(int employeeId,int sortingMonthId,int financialYear);
}
