/**
 * 
 */
package com.beans.util.enums;

/**
 * @author lakshminarayana
 *
 */
public enum Leave {

	ANNUAL("Annual"),
	SICK("Sick"),
	COMPASSIONATE("Compassionate"),
	MARRIAGE("Marriage"),
	MATERNITY("Maternity"),
	PATERNITY("Paternity"),
	UNPAID("Unpaid"),
	TIMEINLIEU("Time-In-Lieu");
	
	private final String value;

    private Leave(String value) {
            this.value = value;
    }
    public String toString(){
        return value;
     }
    public boolean equalsName(String otherName){
        return (otherName == null)? false:value.equalsIgnoreCase(otherName);
    }

};
