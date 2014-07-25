package com.beans.leaveapp.employeeprofile.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.primefaces.event.SelectEvent;

import com.beans.common.audit.service.AuditTrail;
import com.beans.common.audit.service.SystemAuditTrailActivity;
import com.beans.common.audit.service.SystemAuditTrailLevel;
import com.beans.common.security.users.model.Users;
import com.beans.leaveapp.address.model.Address;
import com.beans.leaveapp.address.service.AddressNotFound;
import com.beans.leaveapp.address.service.AddressService;
import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.service.EmployeeService;
import com.beans.leaveapp.employeeprofile.model.AddressDataModel;

public class MyProfileManagementBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private EmployeeService employeeService;
	private AddressService addressService;
	private Employee employee;
	private int selectedDepartment;
	private int selectedEmployeeType;
	private int selectedEmployeeGrade;
	private AddressDataModel addressDataModel;
	private boolean isRenderAddress = false;
	private String addressOperation = "Create";
	private String selectedAddressType;
	private Address selectedAddress = new Address();
	private HashMap<Integer, Address> newAddressMap = new HashMap<Integer, Address>();
	private List<Address> existingAddressList = new ArrayList<Address>();
	private boolean insertDeleteAddress = false;
	private Users actorUsers;
	private AuditTrail auditTrail;
	
	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	public AddressService getAddressService() {
		return addressService;
	}
	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}
	
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
		setSelectedDepartment(this.employee.getDepartment().getId());
		setSelectedEmployeeGrade(this.employee.getEmployeeGrade().getId());
		setSelectedEmployeeType(this.employee.getEmployeeType().getId());
	}
	public int getSelectedDepartment() {
		return selectedDepartment;
	}
	public void setSelectedDepartment(int selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
	}
	public int getSelectedEmployeeType() {
		return selectedEmployeeType;
	}
	public void setSelectedEmployeeType(int selectedEmployeeType) {
		this.selectedEmployeeType = selectedEmployeeType;
	}
	public int getSelectedEmployeeGrade() {
		return selectedEmployeeGrade;
	}
	public void setSelectedEmployeeGrade(int selectedEmployeeGrade) {
		this.selectedEmployeeGrade = selectedEmployeeGrade;
	}
	
	public AddressDataModel getAddressDataModel() {
		if(addressDataModel == null || insertDeleteAddress == true ) {
			if(employee != null && (existingAddressList == null || existingAddressList.size() == 0)) {
				existingAddressList = addressService.findByEmployeeId(this.employee.getId());
			}
			if(existingAddressList == null || existingAddressList.size() == 0) {
				existingAddressList = new ArrayList<Address>();
			}
			List<Address> addressList = new ArrayList<Address>();
			Iterator<Address> existingAddressIterator = existingAddressList.iterator();
			
			while(existingAddressIterator.hasNext()) {
				
				Address currentAddress = existingAddressIterator.next();
				if(currentAddress.isDeleted() != true) {
					addressList.add(currentAddress);
				}
			}
			addressList.addAll(newAddressMap.values());
			addressDataModel = new AddressDataModel(addressList);
		}
		
		return addressDataModel;
	}
	public void setAddressDataModel(AddressDataModel addressDataModel) {
		this.addressDataModel = addressDataModel;
	}
	
	public boolean isRenderAddress() {
		return isRenderAddress;
	}
	
	public void setRenderAddress(boolean isRenderAddress) {
		this.isRenderAddress = isRenderAddress;
	}
	
	public String getSelectedAddressType() {
		return selectedAddressType;
	}
	public void setSelectedAddressType(String selectedAddressType) {
		this.selectedAddressType = selectedAddressType;
	}
	
	public void setAddressOperation(boolean isRenderAddress, String addressOperation) {
		setRenderAddress(isRenderAddress);
		this.addressOperation = addressOperation;
		this.selectedAddress = new Address();
	}
	
	public void renderCreateAddress() {
		setAddressOperation(true, "Create");
	}
	
	public void cancelCreateAddress() {
		setAddressOperation(false, "Create");
	}
	
	public String getAddressOperation() {
		return addressOperation;
	}
	
	public Address getSelectedAddress() {
		return selectedAddress;
	}
	public void setSelectedAddress(Address selectedAddress) {
		this.selectedAddress = selectedAddress;
	}
	
	public void onAddressRowSelect(SelectEvent event) {  
		setAddressOperation(true, "Update");
		setSelectedAddress((Address) event.getObject());
		setSelectedAddressType(selectedAddress.getAddressType());
    } 
	
	public void addAddressToEmployee() {
		int index = newAddressMap.size() + 9000;
		selectedAddress.setId(index);
		selectedAddress.setAddressType(selectedAddressType);
		selectedAddress.setDeleted(false);
		Address addressToBeAdded = selectedAddress;
		newAddressMap.put(index, addressToBeAdded);
		
		resetAddressOperation();
	}
	
	public void updateAddressToEmployee() {
		selectedAddress.setAddressType(selectedAddressType);
		selectedAddress.setLastModifiedBy(actorUsers.getUsername());
		selectedAddress.setLastModifiedTime(new java.util.Date());
		
		resetAddressOperation();
	}
	
	public void deleteAddressToEmployee() {
		int addressId = this.selectedAddress.getId();
		if(newAddressMap.containsKey(addressId)) {
			newAddressMap.remove(addressId);
		} else {
			this.selectedAddress.setDeleted(true);
		}
		
		resetAddressOperation();
	}
	
	private void resetAddressOperation() {
		setAddressOperation(false, "Create");
		setInsertDeleteAddress(true);
	//	setSelectedAddressType("Permanent");
	}
	
	public void setInsertDeleteAddress(boolean insertDeleteAddress) {
		this.insertDeleteAddress = insertDeleteAddress;
	}
	
	public void doUpdateEmployee() {
		employee.setLastModifiedBy(actorUsers.getUsername());
		getEmployeeService().updateEmployee(employee, selectedEmployeeGrade, selectedEmployeeType, selectedDepartment, null, existingAddressList, newAddressMap);
		newAddressMap = new HashMap<Integer, Address>();
		existingAddressList = null;
		
		auditTrail.log(SystemAuditTrailActivity.UPDATED, SystemAuditTrailLevel.INFO, getActorUsers().getId(), getActorUsers().getUsername(), getActorUsers().getUsername() + " has updated his/her own profile");
	}
	
	public Users getActorUsers() {
		return actorUsers;
	}
	public void setActorUsers(Users actorUsers) {
		this.actorUsers = actorUsers;
	}
	
	
	public AuditTrail getAuditTrail() {
		return auditTrail;
	}
	public void setAuditTrail(AuditTrail auditTrail) {
		this.auditTrail = auditTrail;
	}
}
