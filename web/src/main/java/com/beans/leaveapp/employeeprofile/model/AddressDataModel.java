package com.beans.leaveapp.employeeprofile.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.beans.leaveapp.address.model.Address;


public class AddressDataModel extends ListDataModel<Address> implements SelectableDataModel<Address> {

	
	public AddressDataModel(){
		
	}
	
	public AddressDataModel(List<Address> data){
		super(data);
		
	}
	
	@Override
	public Address getRowData(String rowkey) {
		  List<Address> addressList = (List<Address>) getWrappedData();
	        Integer rowKeyInt = Integer.parseInt(rowkey);
	        for(Address address : addressList) {
	            if(address.getId() == rowKeyInt) {
	                return address;
	            }
	        }
	        
	        return null;
		
	}

	@Override
	public Object getRowKey(Address address) {
		// TODO Auto-generated method stub
		
		return address.getId();
	}
}
