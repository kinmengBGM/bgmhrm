package com.beans.leaveapp.quartzjobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.beans.leaveapp.batch.service.YearlyRefreshedLeaves;
import com.beans.leaveapp.leavetype.service.LeaveTypeNotFound;
import com.beans.leaveapp.montlhyreport.LeaveReportWorker;

public class YearlyRefreshedLeavesJob extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
		YearlyRefreshedLeaves yearlyRefreshedLeaves = new YearlyRefreshedLeaves();
		
		try {
			LeaveReportWorker.doInsertEmployeeYearlyData();
			yearlyRefreshedLeaves.YearlyrefreshedLeaves();			
		} catch (LeaveTypeNotFound e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}	
	
}
