package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beans.leaveapp.department.model.Department;
import com.beans.leaveapp.department.service.DepartmentNotFound;
import com.beans.leaveapp.department.service.DepartmentService;

@RestController
@RequestMapping("/protected/department")
public class DepartmentController {

	@Autowired
	DepartmentService departmentService;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Department create(@RequestBody Department department) {
		return departmentService.create(department);

	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public Department delete(@RequestParam int id){
		return departmentService.delete(id);
	}

	@RequestMapping(value = "/findAll")
	public List<Department> findAll(){
		return departmentService.findAll();
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Department update(@RequestBody Department department){
		return departmentService.update(department);
	}

	@RequestMapping(value = "/findById", method = RequestMethod.GET)
	Department findById(@RequestParam int id) throws DepartmentNotFound{
		return departmentService.findById(id);
	}
}
