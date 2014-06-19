package com.beans.util.validation;

import org.apache.commons.validator.routines.EmailValidator;

public class CustomValidator {
	private static CustomValidator instance = null;
	private EmailValidator emailValidator = null;
	
	private CustomValidator() {
		emailValidator = EmailValidator.getInstance();
	}
	
	public static CustomValidator getInstance() {
		if(instance == null) {
			instance = new CustomValidator();
		}
		
		return instance;
	}
	
	public boolean isValidEmail(String input) {
		return emailValidator.isValid(input);
	}
	
	
}
