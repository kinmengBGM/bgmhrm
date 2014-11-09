package com.beans.leaveapp.leavetransaction.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;

public interface LeaveTransactionRepository extends CrudRepository<LeaveTransaction, Integer> {
	
	
	@Query("select l from LeaveTransaction l where isDeleted =?")
	List<LeaveTransaction> findAll(int x);

	@Query("select l from LeaveTransaction l join l.employee e where e.name like :employeeName" )
	 List<LeaveTransaction> findByEmployeeLike(@Param("employeeName") String employeeName);
	
	@Query("select l from  LeaveTransaction l join l.leaveType lt where lt.name like :leaveTypeName")
	 List<LeaveTransaction> findByLeaveTypeLike(@Param("leaveTypeName") String leaveTypeName);
	
	@Query("select l from LeaveTransaction l join l.employee e join l.leaveType lt  where  e.name like :employeeName or lt.name like :leaveTypeName or l.startDateTime =:startDateTime or l.status like :status ")
	List<LeaveTransaction> findByEmployeeOrLeaveTypeOrLeaveDatesOrStatusLike(@Param("employeeName") String employeeName,@Param("leaveTypeName") String leaveTypeName,@Param("startDateTime") Date startDateTime,@Param("status") String status);
	
	@Query("select y from LeaveTransaction y join y.employee e join y.leaveType l where e.name like :employeeName and l.name like :leaveTypeName ")
	 List<LeaveTransaction> findByEmployeeAndLeaveTypeLike(@Param("employeeName") String employeeName,@Param("leaveTypeName") String leaveTypeName);
	
	@Query("select l from LeaveTransaction l where status like :status and isDeleted = 0")
	List<LeaveTransaction> findByStatusLike(@Param("status")String status);
	 
	@Query("select l from LeaveTransaction l join l.employee e join l.leaveType lt  where  e.name like :employeeName and lt.name like :leaveTypeName and l.startDateTime =:startDateTime and l.status like :status")
	List<LeaveTransaction> findByEmployeeAndLeaveTypeAndLeaveDatesAndStatusLike(@Param("employeeName") String employeeName,@Param("leaveTypeName") String leaveTypeName,@Param("startDateTime") Date startDateTime,@Param("status") String status);

	@Query("select l from LeaveTransaction l join l.employee e join l.leaveType lt  where  e.name like :employeeName or lt.name like :leaveTypeName")
	List<LeaveTransaction> findByEmployeeOrLeaveTypeLike(@Param("employeeName") String employeeName,@Param("leaveTypeName") String leaveTypeName);
	
    @Query("select l from LeaveTransaction l join l.employee e join l.leaveType lt  where e.name like :employeeName and lt.name like :leaveTypeName and l.status like :status")
	List<LeaveTransaction> findByEmployeeAndLeaveTypeAndStatusLike(@Param("employeeName") String employeeName,@Param("leaveTypeName") String leaveTypeName,@Param("status") String status);

   @Query("select l from LeaveTransaction l join l.employee e where e.name like :employeeName and l.status like :status")
   List<LeaveTransaction> findByEmployeeAndStatusLike(@Param("employeeName") String employeeName,@Param("status") String status);
   
   @Query("select l from LeaveTransaction l join l.leaveType e where e.name like :leaveTypeName and l.status like :status")
   List<LeaveTransaction> findByLeaveTypeAndStatusLike(@Param("leaveTypeName") String leaveTypeName,@Param("status") String status);
   
   @Query("select l from LeaveTransaction l  where l.startDateTime =:startDateTime and l.status like :status")
   List<LeaveTransaction> findByStartDateTimeAndStatusLike(@Param("startDateTime")Date startDateTime,@Param("status") String status);
   
  
   @Query("select l from LeaveTransaction l join l.leaveType e where e.name like :employeeName and e.name like :leaveTypeName and l.startDateTime  =:startDateTime")
   List<LeaveTransaction> findByEmployeeAndLeaveTypeAndStartDateLike(@Param("employeeName") String employeeName,@Param("leaveTypeName") String leaveTypeName,@Param("startDateTime") Date startDateTime);
   
   @Query("select l from LeaveTransaction l join l.employee e where e.name like :employeeName and l.startDateTime =:startDateTime")
   List<LeaveTransaction> findByEmployeeNameAndStartDate(@Param("employeeName") String employeeName,@Param("startDateTime") Date startDateTime);
   
   @Query("select l from LeaveTransaction l join l.leaveType lt where lt.name like :leaveTypeName and l.startDateTime =:startDateTime")
   List<LeaveTransaction> findByLeaveTypeAndStartDate(@Param("leaveTypeName") String leaveTypeName,@Param("startDateTime") Date startDateTime);
   
   @Query("select l from LeaveTransaction l where startDateTime > ? and isDeleted =0")
   List<LeaveTransaction> findByStartDateTimeAfter(Date startDateTime);
   
   @Query("select l from LeaveTransaction l where startDateTime =? and isDeleted =0")
   List<LeaveTransaction> findByStartDateTime(Date startDateTime);
   
   @Query("select l from LeaveTransaction l where employeeId= :employeeId and applicationDate>=:monthFirstDayOfYear and applicationDate<=:applicationDate and leaveType.id in (select l from LeaveType l where name !='Time-In-Lieu')")
   List<LeaveTransaction> findAllLeavesAppliedByEmployee(@Param("employeeId")int employeeId, @Param("monthFirstDayOfYear") Date monthFirstDayOfYear,@Param("applicationDate") Date applicationDate );
   
   
   @Query("select l from LeaveTransaction l where employeeId= :employeeId and applicationDate>=:monthFirstDayOfYear and applicationDate<=:applicationDate and leaveType.id in (select l from LeaveType l where name ='Time-In-Lieu')")
   List<LeaveTransaction> findAllTimeInLieuLeavesAppliedByEmployee(@Param("employeeId")int employeeId, @Param("monthFirstDayOfYear") Date monthFirstDayOfYear,@Param("applicationDate") Date applicationDate );

   @Query("select l from LeaveTransaction l where id = ? and isDeleted = 0")
   LeaveTransaction findById(int id);
   @Query("select l from LeaveTransaction l where status in ('Pending','Approved') and employeeId in (select id from Employee e where users.id= :userId) and endDateTime>=:todayDate  and leaveType.id in (select l from LeaveType l where name !='Time-In-Lieu')")
   List<LeaveTransaction> findAllFutureLeavesOfEmployee(@Param("userId")int userId, @Param("todayDate") Date todayDate );
   
   @Query("select l from LeaveTransaction l where  leaveType.id in (select l from LeaveType l where name !='Time-In-Lieu') and status='Approved'")
   List<LeaveTransaction> findAllApprovedLeavesOfEmployees();

   @Query("select l from LeaveTransaction l where employeeId in (select id from Employee e where users.id= :userId) and leaveType.id in (select l from LeaveType l)")
   List<LeaveTransaction> findAllLeavesHistoryOfEmployee(@Param("userId")int userId);
   
   
   @Query("select l from LeaveTransaction l where  leaveType.id in (select l from LeaveType l) and status='Pending'")
   List<LeaveTransaction> findAllPendingLeavesOfEmployees();
   
  }
