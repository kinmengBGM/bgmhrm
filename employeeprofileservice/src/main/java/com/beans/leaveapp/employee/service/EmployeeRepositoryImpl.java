package com.beans.leaveapp.employee.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.beans.leaveapp.employee.model.Employee;
import com.beans.leaveapp.employee.repository.EmployeeRepositoryCustom;

@Component
public class EmployeeRepositoryImpl extends JdbcDaoSupport implements EmployeeRepositoryCustom {
	
	@Override
	public List<Employee> getAllUsersWithRole(String role) {
		return getJdbcTemplate().query("SELECT * FROM beans.Employee where userId in ( select userId from UserToRole where userRoleId = (select id from Role where role='"+role+"'))",new RowMapper<Employee>(){
			@Override 
		    public Employee mapRow(ResultSet rs, int rownumber) throws SQLException {  
		        Employee employeeBean=new Employee();  
		        employeeBean.setId(rs.getInt("id"));  
		        employeeBean.setEmployeeNumber(rs.getString("employeeNumber"));
		        employeeBean.setName(rs.getString("name"));
		        employeeBean.setWorkEmailAddress(rs.getString("workEmailAddress"));
		        employeeBean.setPersonalEmailAddress(rs.getString("personalEmailAddress"));
		        return employeeBean;  
		    }  
		});
	}

		
	@Override
	public List<Employee> findAllEmployeesForSendingMonthlyLeaveReport() {
		return getJdbcTemplate().query("SELECT * FROM beans.Employee where employeeTypeId in (select id from beans.EmployeeType) and  userId not in(select userId from beans.UserToRole where userRoleId in (select id from beans.Role where role in('ROLE_OPERDIR','ROLE_ADMIN'))) order by id",new RowMapper<Employee>(){

			@Override
			public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
				 Employee employeeBean=new Employee();  
			        employeeBean.setId(rs.getInt("id"));  
			        employeeBean.setEmployeeNumber(rs.getString("employeeNumber"));
			        employeeBean.setName(rs.getString("name"));
			        employeeBean.setWorkEmailAddress(rs.getString("workEmailAddress"));
			        employeeBean.setPersonalEmailAddress(rs.getString("personalEmailAddress"));
			        employeeBean.setGender(rs.getString("gender"));
			        return employeeBean;  
			}
	});
	}
}
