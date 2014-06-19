package com.beans.util.config;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;

public class ConfigurationHolder {
	private static CombinedConfiguration config = null;
	private static void init() throws ConfigurationException {
		DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder("config/main.xml");
		config = builder.getConfiguration(true);
		
	}
	
	
	public static String getString(String key) {
		try {
			if(config == null) {
				init();
			}
			return config.getString(key);
		} catch(ConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Integer getInt(String key) {
		try {
			if(config == null) {
				init();
			}
			return config.getInt(key);
		} catch(ConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Boolean getBoolean(String key) {
		try {
			if(config == null) {
				init();
			}
			return config.getBoolean(key);
		} catch(ConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
}
