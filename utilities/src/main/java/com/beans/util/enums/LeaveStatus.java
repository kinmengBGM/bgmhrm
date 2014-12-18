/**
 * 
 */
package com.beans.util.enums;

/**
 * @author lakshminarayana
 *
 */
public enum LeaveStatus {

	PENDING("Pending"),
	REJECTED("Rejected"),
	APPROVED("Approved"),
	CANCELLED("Cancelled");
	
	private final String value;

    private LeaveStatus(String value) {
            this.value = value;
    }
    public String toString(){
        return value;
     }
    public boolean equalsName(String otherName){
        return (otherName == null)? false:value.equalsIgnoreCase(otherName);
    }

};
