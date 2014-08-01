package com.beans.leaveapp.quartzjobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.beans.leaveapp.batch.service.YearlyRefreshedLeaves;

public class YearlyRefreshedLeavesJob extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
		YearlyRefreshedLeaves yearlyRefreshedLeaves = new YearlyRefreshedLeaves();
		
		try {
			yearlyRefreshedLeaves.YearlyrefreshedLeaves();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
}
