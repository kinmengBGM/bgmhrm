package com.beans.common.leavetransaction;

public enum LeaveTransactionReason {
    
	APPROVED("Approved"),CANCELED("Canceled"),PENDING("Pending"),REJECTED("Rejected");
	
     
	private String reason;
	
	LeaveTransactionReason(String reason){
		this.reason = reason;
	}
	public String getReason() {
		return reason;
	}

	
	
     
}
