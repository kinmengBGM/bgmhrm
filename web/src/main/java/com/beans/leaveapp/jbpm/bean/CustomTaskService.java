package com.beans.leaveapp.jbpm.bean;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.kie.api.task.TaskService;
import org.kie.api.task.model.Attachment;
import org.kie.api.task.model.Content;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;

@ApplicationScoped
public class CustomTaskService implements TaskService{

	@Override
	public void activate(long arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long addTask(Task arg0, Map<String, Object> arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void claim(long arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void claimNextAvailable(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void complete(long arg0, String arg1, Map<String, Object> arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delegate(long arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit(long arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fail(long arg0, String arg1, Map<String, Object> arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forward(long arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Attachment getAttachmentById(long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Content getContentById(long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task getTaskById(long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task getTaskByWorkItemId(long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskSummary> getTasksAssignedAsBusinessAdministrator(
			String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskSummary> getTasksAssignedAsPotentialOwner(String arg0,
			String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskSummary> getTasksAssignedAsPotentialOwnerByStatus(
			String arg0, List<Status> arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> getTasksByProcessInstanceId(long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskSummary> getTasksByStatusByProcessInstanceId(long arg0,
			List<Status> arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskSummary> getTasksByVariousFields(List<Long> arg0,
			List<Long> arg1, List<Long> arg2, List<String> arg3,
			List<String> arg4, List<String> arg5, List<Status> arg6,
			List<String> arg7, boolean arg8) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskSummary> getTasksByVariousFields(Map<String, List<?>> arg0,
			boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskSummary> getTasksOwned(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskSummary> getTasksOwnedByStatus(String arg0,
			List<Status> arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void nominate(long arg0, String arg1, List<OrganizationalEntity> arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void release(long arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume(long arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skip(long arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(long arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop(long arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void suspend(long arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
