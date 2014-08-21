package com.beans.leaveapp.quartzjobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.beans.leaveapp.montlhyreport.LeaveReportWorker;

public class InitializeCurrentMonthJob extends QuartzJobBean{

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
	  LeaveReportWorker.doInitializeCurrentMonthLeaveRecords();
		
	}

}
