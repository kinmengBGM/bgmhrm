package com.beans.common.audit.service;

public enum SystemAuditTrailActivity {
	LOGIN("LOGIN"), LOGOUT("LOGOUT"), CREATED("CREATED"), ACCESSED("ACCESSED"), UPDATED("UPDATED"), DELETED("DELETED"), APPROVED("APPROVED"), REJECTED("REJECTED");
	
	private String activity;
	private SystemAuditTrailActivity(String activity) {
		this.activity = activity;
	}
	
	public String getActivity() {
		return this.activity;
	}
}
