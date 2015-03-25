package com.beans.leaveapp.quartzjobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.beans.leaveapp.batch.service.MonthlyAddedLeave;

public class MonthlyAddedLeaveJob extends QuartzJobBean{	

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException{
		MonthlyAddedLeave monthlyAddedLeave = new MonthlyAddedLeave();	
			try {
				monthlyAddedLeave.MonthlyAddedLeaves();
			} catch (Exception e) {
				e.printStackTrace();
			}		
	}	
	
}
