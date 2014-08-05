package com.beans.leaveapp.quartzjobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.beans.leaveapp.batch.service.MonthlyAddedLeave;

public class MonthlyAddedLeaveJob extends QuartzJobBean{	

	MonthlyAddedLeave monthlyAddedLeave = new MonthlyAddedLeave();	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException{
			try {
				monthlyAddedLeave.MonthlyAddedLeaves();
			} catch (Exception e) {
				e.printStackTrace();
			}		
	}	
	
	public MonthlyAddedLeave getMonthlyAddedLeave() {
		return monthlyAddedLeave;
	}

	public void setMonthlyAddedLeave(MonthlyAddedLeave monthlyAddedLeave) {
		this.monthlyAddedLeave = monthlyAddedLeave;
	}
}
