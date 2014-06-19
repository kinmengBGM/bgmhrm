package com.beans.test.util.config;

import org.junit.Assert;
import org.junit.Test;

import com.beans.util.config.ConfigurationHolder;

public class ConfigurationHolderTest {
	
	
	
	@Test
	public void testGetString() {
		Assert.assertNotNull("Testing for mail.smtp.host", ConfigurationHolder.getString("mail.smtp.host"));
	}
	
	@Test
	public void testGetInt() {
		Assert.assertNotNull("Testing for mail.smtp.port", ConfigurationHolder.getInt("mail.smtp.socketFactory.port"));
	}
	
	@Test
	public void testGetBoolean() {
		Assert.assertNotNull("Testing for mail.smtp.ssl", ConfigurationHolder.getBoolean("mail.smtp.auth"));
	}
	
	@Test
	public void testGetStringList() {
		Assert.assertNotNull("Testing for bpmn.file", ConfigurationHolder.getStringList("bpmn.file"));
	}
}
