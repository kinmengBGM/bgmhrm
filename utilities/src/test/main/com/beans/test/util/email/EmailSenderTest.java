package com.beans.test.util.email;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.mail.resolver.DataSourceClassPathResolver;
import org.junit.Before;
import org.junit.Test;

import com.beans.util.email.EmailSender;

public class EmailSenderTest {
	
	
	@Test
	public void testSendEmailSuccessful() throws EmailException{
		SimpleEmail email = new SimpleEmail();
		email.setSubject("Testing Simple Email");
		email.setMsg("Testing from unit test for simple text email");
		email.addTo("pradeepchinta432@gmail.com");
		EmailSender.sendEmail(email);
	}
	
	@Test(expected=EmailException.class)
	public void testSendEmailFailedWithInvalidEmailAddressFormat() throws EmailException {
		SimpleEmail email = new SimpleEmail();
		email.setSubject("Testing");
		email.setMsg("Testing from unit test");
		email.addTo("pradeepchinta432gmail.com");
		EmailSender.sendEmail(email);
		
	}
	
	@Test
	public void testSendHTMLEmailSuccessful() throws EmailException, MalformedURLException {
		ImageHtmlEmail email = new ImageHtmlEmail();
		
		
		
		email.setSubject("Testing for HTML Email");
		email.addTo("pradeepchinta432@gmail.com");
		URL url = new URL("http://www.beans.com.my/templates/theme513/images/logo.png");
		String cid = email.embed(url, "Beans Logo");
		StringBuffer bodyStringBuffer = new StringBuffer();
		bodyStringBuffer.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
		bodyStringBuffer.append("<HTML>");
		bodyStringBuffer.append("<HEAD>");
		bodyStringBuffer.append("<TITLE> Unit Testing for HTML Email </TITLE>");
		bodyStringBuffer.append("<meta http-equiv=Content-Type content=text/html; charset=utf-8>");
		bodyStringBuffer.append("</HEAD>");
		bodyStringBuffer.append("<BODY>");
		bodyStringBuffer.append("<div align=center>Beans Logo: <image src='cid:" + cid + "'></div>");
		bodyStringBuffer.append("</BODY>");
		bodyStringBuffer.append("</HTML>");
		
		email.setHtmlMsg(bodyStringBuffer.toString());
		
		EmailSender.sendEmail(email);
	}
	
	@Test
	public void testSendEmailWithAttachmentSuccessful() throws EmailException, IOException {
		
		DataSourceClassPathResolver dataSourceClassPathResolver = new DataSourceClassPathResolver();
		
		
		MultiPartEmail email = new MultiPartEmail();
		
		email.setSubject("Test Email With Attachment");
		email.setMsg("Testing from unit test for email with attachment");
		email.addTo("pradeepchinta432@gmail.com");
		
		email.attach(dataSourceClassPathResolver.resolve("email/attachment.pdf"), "attachment.pdf", "For Unit Testing");
		
		EmailSender.sendEmail(email);
	}
}
