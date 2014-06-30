/**
 * 
 */
package com.beans.util.enums;

/**
 * @author lakshminarayana
 *
 */
public enum EmployeeTypes {

	CONTRACT("CONT"),
	PERMANENT("PERM"),
	INTERNSHIP("INT");
		
	private final String value;

    private EmployeeTypes(String value) {
            this.value = value;
    }
    public String toString(){
        return value;
     }
    public boolean equalsName(String otherName){
        return (otherName == null)? false:value.equals(otherName);
    }

};
