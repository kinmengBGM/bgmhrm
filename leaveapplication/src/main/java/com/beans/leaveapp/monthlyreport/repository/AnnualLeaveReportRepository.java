package com.beans.leaveapp.monthlyreport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.beans.leaveapp.monthlyreport.model.AnnualLeaveReport;

public interface AnnualLeaveReportRepository extends CrudRepository<AnnualLeaveReport, Integer> {

	@Query("select a from AnnualLeaveReport a where employee.id=:employeeId and financialYear=:financialYear ORDER BY sortingMonthId ASC")
	 List<AnnualLeaveReport> getAnnualLeaveDataOfEmployee(@Param("employeeId") int employeeId,@Param("financialYear") int financialYear);
	
	@Query("select a from AnnualLeaveReport a where employee.id=:employeeId and sortingMonthId in (:sortingMonthId,13) and financialYear=:financialYear ORDER BY sortingMonthId ASC")
	 List<AnnualLeaveReport> getEmployeeMonthlyLeaveReportData(@Param("employeeId") int employeeId, @Param("sortingMonthId") int sortingMonthId,@Param("financialYear") int financialYear);

	@Query("select a from AnnualLeaveReport a where employee.id = ? and sortingMonthId = ? and financialYear = ?")
	 AnnualLeaveReport findAnnualLeaveReportByEmployeeId(int employeeId,int sortingMonthId,int financialYear);

	@Query("select a from AnnualLeaveReport a where employee.id=:employeeId and sortingMonthId in (:sortingMonthId) and financialYear=:financialYear ORDER BY sortingMonthId ASC")
	 List<AnnualLeaveReport> getEmployeeMonthlyLeaveReportDataForInitialization(@Param("employeeId") int employeeId, @Param("sortingMonthId") List<Integer> sortingMonthId,@Param("financialYear") int financialYear);
	
	@Query("select a from AnnualLeaveReport a where employee.id=:employeeId and sortingMonthId in (:sortingMonthId) and financialYear=:financialYear ORDER BY sortingMonthId ASC")
	 AnnualLeaveReport getCurrentMonthAnnualLeaveRecord(@Param("employeeId") int employeeId, @Param("sortingMonthId") int sortingMonthId,@Param("financialYear") int financialYear);

	@Query("select a from AnnualLeaveReport a where employee.id=:employeeId and sortingMonthId in (1,2) and financialYear=:financialYear ORDER BY sortingMonthId ASC")
	 List<AnnualLeaveReport> getRecordsOfCurrentYearJanAndFeb(@Param("employeeId") int employeeId, @Param("financialYear") int financialYear);

	
}
