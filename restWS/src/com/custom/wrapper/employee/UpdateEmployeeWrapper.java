package com.custom.wrapper.employee;

import java.util.HashMap;
import java.util.List;

import com.beans.common.security.users.model.Users;
import com.beans.leaveapp.address.model.Address;
import com.beans.leaveapp.employee.model.Employee;

public class UpdateEmployeeWrapper {
	private Employee employee = null;
	private int employeeGradeId;
	private int employeeTypeId;
	private int departmentId;
	private Users users = null;
	private List<Address> existingAddressList = null;
	private HashMap<Integer, Address> newAddressMap = null;
	
	public UpdateEmployeeWrapper(){
	}

	public UpdateEmployeeWrapper(Employee employee, int employeeGradeId, int employeeTypeId, int departmentId, Users users, 
			List<Address> existingAddressList, HashMap<Integer, Address> newAddressMap){
		this.employee=employee;
		this.employeeGradeId=employeeGradeId;
		this.employeeTypeId=employeeTypeId;
		this.departmentId=departmentId;
		this.users=users;
		this.existingAddressList=existingAddressList;
		this.newAddressMap=newAddressMap;
	}

	public Employee getEmployee(){
		return this.employee;
	}
	public void setEmployee(Employee employee){
		this.employee = employee;
	}
	public int getEmployeeGradeId(){
		return this.employeeGradeId;
	}
	public void setEmployeeGradeId(int employeeGradeId){
		this.employeeGradeId = employeeGradeId;
	}
	public int getEmployeeTypeId(){
		return this.employeeTypeId;
	}
	public void setEmployeeTypeId(int employeeTypeId){
		this.employeeTypeId=employeeTypeId;
	}
	public int getDepartmentId(){
		return this.departmentId;
	}
	public void setDepartmentId(int departmentId){
		this.departmentId=departmentId;
	}
	public Users getUsers(){
		return this.users;
	}
	public void setUsers(Users users){
		this.users=users;
	}
	public List<Address> getExistingAddressList(){
		return existingAddressList;
	}
	public void setExistingAddressList(List<Address> existingAddressList){
		this.existingAddressList= existingAddressList;
	}
	public HashMap<Integer, Address> getNewAddressMap(){
		return newAddressMap;
	}
	public void setNewAddressMap(HashMap<Integer, Address> newAddressMap){
		this.newAddressMap= newAddressMap;
	}
}
