package com.beans.leaveapp.audit.trail.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.beans.common.audit.model.SystemAuditTrail;
import com.beans.common.audit.service.SystemAuditTrailRecordService;
import com.beans.leaveapp.audit.trail.model.SystemAuditTrailDataModel;

public class SystemAuditTrailManagementBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private SystemAuditTrailRecordService systemAuditTrailService;
	private List<SystemAuditTrail> systemAuditTrailList;
	private SystemAuditTrailDataModel systemAuditTrailDataModel;
	private SystemAuditTrail newSystemAuditTrail = new SystemAuditTrail();
	private SystemAuditTrail selectedSystemAuditTrail = new SystemAuditTrail();
	private boolean insertDeleted = false;
	private boolean forDates = false;

	SimpleDateFormat s = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

	public Date date1;
	public Date date2;
	public String activity;

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public void search() throws Exception {

		if (date1 != null && date2 != null && !activity.equals("") ) {
			java.sql.Date fromDate = new java.sql.Date(date1.getTime());
			java.sql.Date toDate = new java.sql.Date(date2.getTime());

			setSystemAuditTrailList(getSystemAuditTrailService().findSelectedDates(fromDate, toDate,activity));
			System.out.println(getSystemAuditTrailList().size());
			forDates = true;
			this.getSystemAuditTrailDataModel();
			date1.getTime();
			String currentTime = s.format(fromDate);
			System.out.println(currentTime);
		}
	}

	public SystemAuditTrailRecordService getSystemAuditTrailService() {
		return systemAuditTrailService;
	}

	public void setSystemAuditTrailService(
			SystemAuditTrailRecordService systemAuditTrailService) {
		this.systemAuditTrailService = systemAuditTrailService;
	}

	public SystemAuditTrail getNewSystemAuditTrail() {
		return newSystemAuditTrail;
	}

	public void setNewSystemAuditTrail(SystemAuditTrail newSystemAuditTrail) {
		this.newSystemAuditTrail = newSystemAuditTrail;
	}

	public SystemAuditTrail getSelectedSystemAuditTrail() {
		return selectedSystemAuditTrail;
	}

	public void setSelectedSystemAuditTrail(
			SystemAuditTrail selectedSystemAuditTrail) {
		this.selectedSystemAuditTrail = selectedSystemAuditTrail;
	}

	public boolean isInsertDeleted() {
		return insertDeleted;
	}

	public void setInsertDelete(boolean insertDeleted) {
		this.insertDeleted = insertDeleted;
	}

	public List<SystemAuditTrail> getSystemAuditTrailList() {
		if (systemAuditTrailList == null || insertDeleted == true) {
			try {
				systemAuditTrailList = getSystemAuditTrailService().findAll();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return systemAuditTrailList;
	}

	public void setSystemAuditTrailDataModel(
			SystemAuditTrailDataModel systemAuditTrailDataModel) {
		this.systemAuditTrailDataModel = systemAuditTrailDataModel;
	}

	public SystemAuditTrailDataModel getSystemAuditTrailDataModel() {
		if (systemAuditTrailDataModel == null || insertDeleted == true) {
			systemAuditTrailDataModel = new SystemAuditTrailDataModel(
					getSystemAuditTrailList());
		}
		if (forDates == true) {
			systemAuditTrailDataModel = new SystemAuditTrailDataModel(
					getSystemAuditTrailList());
			// forDates = false;
		}

		return systemAuditTrailDataModel;

	};

	public void setSystemAuditTrailList(
			List<SystemAuditTrail> systemAuditTrailLists) {
		this.systemAuditTrailList = systemAuditTrailLists;
	}
}
