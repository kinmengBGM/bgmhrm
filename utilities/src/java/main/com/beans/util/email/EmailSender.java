package com.beans.util.email;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

import com.beans.util.config.ConfigurationHolder;

public class EmailSender {
	
		
	public static void sendEmail(Email email) throws EmailException{
		email.setHostName(ConfigurationHolder.getString("mail.smtp.host"));
		email.setSmtpPort(ConfigurationHolder.getInt("mail.smtp.port"));
		email.setSSLOnConnect(ConfigurationHolder.getBoolean("mail.smtp.ssl"));
		if(ConfigurationHolder.getBoolean("mail.smtp.auth")) {
			email.setAuthentication(ConfigurationHolder.getString("mail.smtp.sender.username"), ConfigurationHolder.getString("mail.smtp.sender.password"));
		}
		email.setFrom(ConfigurationHolder.getString("mail.smtp.sender.address"));
		
		email.send();
	}
}

