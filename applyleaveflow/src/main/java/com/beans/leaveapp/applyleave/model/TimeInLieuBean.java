package com.beans.leaveapp.applyleave.model;

import java.io.Serializable;

public class TimeInLieuBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean isTwoLeveApproval;
	private String firstApprover;
	private String secondApprover;
	private Boolean isFirstApproverApproved;
	private Boolean isSecondApproverApproved;
	
	public TimeInLieuBean() {
		super();
	}

	public TimeInLieuBean(Boolean isTwoLeveApproval, String firstApprover,
			String secondApprover, Boolean isFirstApproverApproved,
			Boolean isSecondApproverApproved) {
		super();
		this.isTwoLeveApproval = isTwoLeveApproval;
		this.firstApprover = firstApprover;
		this.secondApprover = secondApprover;
		this.isFirstApproverApproved = isFirstApproverApproved;
		this.isSecondApproverApproved = isSecondApproverApproved;
	}
	
	public Boolean getIsTwoLeveApproval() {
		return isTwoLeveApproval;
	}

	public void setIsTwoLeveApproval(Boolean isTwoLeveApproval) {
		this.isTwoLeveApproval = isTwoLeveApproval;
	}

	public String getFirstApprover() {
		return firstApprover;
	}

	public void setFirstApprover(String firstApprover) {
		this.firstApprover = firstApprover;
	}

	public String getSecondApprover() {
		return secondApprover;
	}

	public void setSecondApprover(String secondApprover) {
		this.secondApprover = secondApprover;
	}
	
	
	public Boolean getIsFirstApproverApproved() {
		return isFirstApproverApproved;
	}

	public void setIsFirstApproverApproved(Boolean isFirstApproverApproved) {
		this.isFirstApproverApproved = isFirstApproverApproved;
	}

	public Boolean getIsSecondApproverApproved() {
		return isSecondApproverApproved;
	}

	public void setIsSecondApproverApproved(Boolean isSecondApproverApproved) {
		this.isSecondApproverApproved = isSecondApproverApproved;
	}

	@Override
	public String toString() {
		return "TimeInLieuBean [isTwoLeveApproval=" + isTwoLeveApproval
				+ ", firstApprover=" + firstApprover + ", secondApprover="
				+ secondApprover + ", isFirstApproverApproved="
				+ isFirstApproverApproved + ", isSecondApproverApproved="
				+ isSecondApproverApproved + "]";
	}

	
	
	
}
