package com.beans.leaveapp.quartzjobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.beans.leaveapp.batch.service.UpdateAnnualLeaveReport;

public class UpdateAnnualLeaveReportJob extends QuartzJobBean{

	
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		UpdateAnnualLeaveReport updateAnnualLeaveReport = new UpdateAnnualLeaveReport();		
		
		try {
			updateAnnualLeaveReport.UpdatingAnnualLeaveReport();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
