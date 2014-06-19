package com.beans.util.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplLogger {
	private static Logger log;
	
	public static Logger getLogger() {
		if(log == null) {
			log = LoggerFactory.getLogger(ApplLogger.class);
		}
		
		return log;
	}
}
