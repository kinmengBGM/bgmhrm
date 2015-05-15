package com.controller;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.beans.leaveapp.employee.service.EmployeeService;
import com.beans.leaveapp.jbpm6.util.ApplicationContextProvider;
import com.beans.leaveapp.leavetransaction.model.LeaveTransaction;
import com.beans.util.config.ConfigurationHolder;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.common.io.Files;


@RestController
@RequestMapping("/protected/calendarEvent")
public class CalendarEventController {
	
	 private static final String APPLICATION_NAME = "Beans-HRM/1.0";
	  
	  /** E-mail address of the service account. */
	  private static final String SERVICE_ACCOUNT_EMAIL = "135965950222-bqeu9m7fric37agdl24eoc3d4c455pm4@developer.gserviceaccount.com";

	  /** Global instance of the HTTP transport. */
	  private static HttpTransport httpTransport;
	  
	  /** Client secrets **/
	  private static final String CLIENT_SECRET="/credentials/BeansHRM-13c8dce63281.p12";

	  /** Global instance of the JSON factory. */
	  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	
	@Autowired
    ServletContext context;
		
	@RequestMapping(value = "/createEventForApprovedLeave", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void createEvent(@RequestBody LeaveTransaction leaveTransaction) throws IOException {
		createEventForApprovedLeave(leaveTransaction);
	}
	
	public Calendar configure() {
	    try {
	      try {
	    	  String absoluteDiskPath = context.getRealPath(CLIENT_SECRET);
	    	  File file = new File(absoluteDiskPath);
	        httpTransport = new NetHttpTransport();
	        // check for valid setup
	        if (SERVICE_ACCOUNT_EMAIL.startsWith("Enter ")) {
	          System.err.println(SERVICE_ACCOUNT_EMAIL);
	          return null;
	        }
	      String p12Content = Files.readFirstLine(file, Charset.defaultCharset());
	        if (p12Content.startsWith("Please")) {	
	          System.err.println(p12Content);
	          return null;
	        }
	        // service account credential (uncomment setServiceAccountUser for domain-wide delegation)
	        GoogleCredential credential = 	new GoogleCredential.Builder()
													        	.setTransport(httpTransport)
													            .setJsonFactory(JSON_FACTORY)
													            .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
													            .setServiceAccountScopes(Collections.singleton(CalendarScopes.CALENDAR))
													            .setServiceAccountPrivateKeyFromP12File(file)
													            .build();
	        Calendar   client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
	     																     .setApplicationName(APPLICATION_NAME).build();
	     System.out.println("Client : "+client);
	     return client;
	     
	      } catch (IOException e) {
	        System.err.println(e.getMessage());
	      }
	    } catch (Throwable t) {
	      t.printStackTrace();
	    }
	    return null;
	  }

	@SuppressWarnings("unused")
	public void createEventForApprovedLeave(LeaveTransaction leaveTransaction) throws IOException{
		  try{
			Event event = new Event();
			Calendar service =null;
			event.setSummary(leaveTransaction.getLeaveType().getName()+" : "+leaveTransaction.getEmployee().getName());
			event.setDescription(leaveTransaction.getEmployee().getName()+ " is on "+leaveTransaction.getLeaveType().getName()+" Leave due to "+leaveTransaction.getReason());
			event.setLocation("Malaysia");
	
			ArrayList<EventAttendee> attendees = new ArrayList<EventAttendee>();
			
			attendees.add(new EventAttendee().setEmail(ConfigurationHolder.getString("calendar.event.email")));

			event.setAttendees(attendees);
			
			Date startDate,endDate=null;
			// Full day leave
			java.util.Calendar startCal = java.util.Calendar.getInstance();
			startCal.setTime(leaveTransaction.getStartDateTime());
			java.util.Calendar endCal = java.util.Calendar.getInstance();
			endCal.setTime(leaveTransaction.getEndDateTime());
			startCal.set(java.util.Calendar.MINUTE, 0);
			endCal.set(java.util.Calendar.MINUTE, 0);
			
			
			// write if condition for full leaves other than half days
			if(leaveTransaction.getNumberOfDays().doubleValue()-(long)leaveTransaction.getNumberOfDays().doubleValue()!=0.5){
			startCal.set(java.util.Calendar.HOUR_OF_DAY, 9);
			endCal.set(java.util.Calendar.HOUR_OF_DAY, 18);
			startDate = startCal.getTime();
			endDate = endCal.getTime();
			}else
				{
			// Half day leave if morning taken
			int startHour=9,endHour=13;
			if("PM".equalsIgnoreCase(leaveTransaction.getTimings())){
				startHour=13;endHour=18;
			}
			startCal.set(java.util.Calendar.HOUR_OF_DAY, startHour);
			endCal.set(java.util.Calendar.HOUR_OF_DAY, endHour);
			startDate = startCal.getTime();
			endDate = endCal.getTime();
			}
			DateTime start = new DateTime(startDate);
			event.setStart(new EventDateTime().setDateTime(start));
			DateTime end = new DateTime(endDate);
			event.setEnd(new EventDateTime().setDateTime(end));
	
			service = configure();
			
			Event createdEvent = service.events().insert("primary", event).execute();
	
			System.out.println("Event is created for user : "+leaveTransaction.getEmployee().getName()+" in  calendar "+leaveTransaction.getEmployee().getWorkEmailAddress());
		  }catch(Exception e){
			  System.out.println("Error while creating event for user : "+leaveTransaction.getEmployee().getName()+" with data : "+leaveTransaction);
			  e.printStackTrace();
		  }
	  }
	  
}
