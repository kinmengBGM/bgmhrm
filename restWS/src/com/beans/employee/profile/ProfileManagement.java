package com.beans.employee.profile;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.beans.common.audit.service.AuditTrail;
import com.beans.common.audit.service.SystemAuditTrailActivity;
import com.beans.common.audit.service.SystemAuditTrailLevel;
import com.beans.common.security.users.model.Users;
import com.beans.leaveapp.address.model.Address;
import com.beans.leaveapp.employee.model.Employee;
import com.custom.wrapper.audittrail.LogWrapper;
import com.custom.wrapper.employee.UpdateEmployeeWrapper;

public class ProfileManagement implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Employee employee = new Employee();
	private Users user = new Users();
	
	//address
	private Address address = new Address();
	private String selectedAddressType;

	private int selectedDepartment;
	private int selectedEmployeeType;
	private int selectedEmployeeGrade;
	
	AuditTrail auditTrail = new AuditTrail();
	
	//RestTemplate
	private RestTemplate restTemplate;
	private List<HttpMessageConverter<?>> messageConverters;
	
	public Users getUser(){
		return this.user;
	}
	
	public void setUser(Users user){
		this.user = user;
	}
	
	public Employee getEmployee(){
		return this.employee;
	}
	
	public void setEmployee(Employee employee){
		this.employee = employee;
	}
	public void initRestTemplate() {
		restTemplate = new RestTemplate();
		messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new MappingJackson2HttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
		// Add the message converters to the restTemplate
		restTemplate.setMessageConverters(messageConverters);
	}
	
	

	public Employee findEmployeeByUserId(int id) throws IOException {

		String url = "http://10.0.2.2:8080/restWS-0.0.1-SNAPSHOT/protected/employee/findByUserId?userId={id}";
		initRestTemplate();

		this.employee = restTemplate.getForObject(url, Employee.class, id);
		this.user = employee.getUsers();
		//print
		//printEmployeeDetails();
		return this.employee;
	}
	
	public void printEmployeeDetails(){

		System.out.println("User Id		:" + this.user.getId());
		System.out.println("Id		:" + this.employee.getId());
		System.out.println("Name		:" + this.employee.getName());
		System.out.println("I.D Number	:" + this.employee.getIdNumber());
		System.out.println("Roles		:" + this.user.getUserRoles());
		System.out.println("Department	:" + this.employee.getDepartment());
	}

	public void updateProfile(int id,String empNo, String name, String position,
			String idNo, String passportNo, String gender, String religion,
			String maritalStatus, String workEmail, String personalEmail,
			String officePhone, String personalPhone, String nationality)
			throws IOException {
		String url = "http://localhost:8080/restWS-0.0.1-SNAPSHOT/protected/employee/update";
		initRestTemplate();

		this.employee.setId(id);
		this.employee.setEmployeeNumber(empNo);
		this.employee.setName(name);
		this.employee.setPosition(position);
		this.employee.setIdNumber(idNo);
		this.employee.setPassportNumber(passportNo);
		this.employee.setGender(gender);
		this.employee.setReligion(religion);
		this.employee.setMaritalStatus(maritalStatus);
		this.employee.setWorkEmailAddress(workEmail);
		this.employee.setPersonalEmailAddress(personalEmail);
		this.employee.setOfficePhone(officePhone);
		this.employee.setPersonalPhone(personalPhone);
		this.employee.setNationality(nationality);

		restTemplate.postForObject(url, this.employee, Employee.class);
	}

	public void updateProfile(Employee employeeToUpdate)
			throws IOException {
		String url = "http://localhost:8080/restWS-0.0.1-SNAPSHOT/protected/employee/update";
		initRestTemplate();
		this.employee = employeeToUpdate;
		this.user=employee.getUsers();
		employee.setLastModifiedBy(user.getUsername());		
		this.employee = restTemplate.postForObject(url, employeeToUpdate, Employee.class);
		
		//AuditTrail
		/*LogWrapper logWrapper = new LogWrapper();
		logWrapper.setSystemAuditTrailActivity(SystemAuditTrailActivity.UPDATED);
		logWrapper.setSystemAuditTrailLevel(SystemAuditTrailLevel.INFO);
		logWrapper.setUserId(this.user.getId());
		logWrapper.setUsername(this.user.getUsername());
		logWrapper.setDescription(this.user.getUsername() + " has updated his/her own profile");
		url = "http://localhost:8080/restWS-0.0.1-SNAPSHOT/auditTrail/log";
		restTemplate.postForObject(url, logWrapper, Void.class);*/

		
		//auditTrail.log(logWrapper);
		//auditTrail.log(SystemAuditTrailActivity.UPDATED, SystemAuditTrailLevel.INFO, user.getId(), user.getUsername(), user.getUsername() + " has updated his/her own profile");

		//return employeeToUpdate;
	}
	
	public void updateEmployee(Employee employee, int employeeGradeId, int employeeTypeId, int employeeDepartmentId, List<Address> existingAddressList, HashMap<Integer, Address> newAddressMap) throws IOException{
		String url = "http://localhost:8080/restWS-0.0.1-SNAPSHOT/protected/employee/updateEmployee";
		initRestTemplate();
		System.out.println(employee.getId());
		//UpdateEmployeeWrapper updateEmployeeWrapper= new UpdateEmployeeWrapper(employee, employeeGradeId,employeeTypeId,employeeDepartmentId, null, existingAddressList,newAddressMap);
		UpdateEmployeeWrapper updateEmployeeWrapper= new UpdateEmployeeWrapper();
		updateEmployeeWrapper.setEmployee(employee);
		updateEmployeeWrapper.setEmployeeGradeId(employeeGradeId);
		updateEmployeeWrapper.setEmployeeTypeId(employeeTypeId);
		updateEmployeeWrapper.setDepartmentId(employeeDepartmentId);
		updateEmployeeWrapper.setUsers(null);
		updateEmployeeWrapper.setExistingAddressList(existingAddressList);
		updateEmployeeWrapper.setNewAddressMap(newAddressMap);

		employee.setLastModifiedBy(user.getUsername());
		restTemplate.postForObject(url, updateEmployeeWrapper, Employee.class);
		
		//employee.getName();
		//To save
		//url = "http://localhost:8080/restWS-0.0.1-SNAPSHOT/employee/update";
		//employee =  restTemplate.postForObject(url, employee, Employee.class);
		//return employee;
	}
}
