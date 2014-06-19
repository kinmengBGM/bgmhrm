package com.beans.leaveapp.quartzjobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.beans.leaveapp.batch.service.YearlyAddedLeave;

public class YearlyAddedLeaveJob extends QuartzJobBean {
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException{

		YearlyAddedLeave yearlyAddedLeave = new YearlyAddedLeave();
		try {
			yearlyAddedLeave.YearlyAddedLeaves();
		} catch (Exception e) {
			e.printStackTrace();
		}
}
}	
