package com.beans.test.util.validation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.beans.util.validation.CustomValidator;

public class ValidatorTest {
	private CustomValidator validator = null;
	
	@Before
	public void init() {
		validator = CustomValidator.getInstance();
	}
	
	@Test
	public void testValidEmail() {
		Assert.assertTrue("Validator testing for email kinmeng.chan@beans.com.my", validator.isValidEmail("kinmeng.chan@beans.com.my"));
	}
	
	@Test
	public void testInvalidEmail1() {
		Assert.assertFalse("Validator testing for email kinmeng.chanbeans.com.my", validator.isValidEmail("kinmeng.chanbeans.com.my"));
	}
	
	@Test
	public void testInvalidEmail2() {
		Assert.assertFalse("Validator testing for email kinmeng.chan@beans.@com.my", validator.isValidEmail("kinmeng.chan@beans.@com.my"));
	}
	
}
