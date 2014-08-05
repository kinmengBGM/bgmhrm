package com.beans.leaveapp.jbpm6.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;

import org.jbpm.services.task.utils.ContentMarshallerHelper;
import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.EnvironmentName;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkflowProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.UserGroupCallback;
import org.kie.api.task.model.Content;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;


public class JBPM6Runtime {
	private RuntimeManager manager;
	private JBPM6Runtime(EntityManagerFactory entityManagerFactory, AbstractPlatformTransactionManager abstractPlatformTransactionManager, UserGroupCallback userGroupCallback, String bpmnFile, String identifier, String... droolFileList) {
		KieServices kservices = KieServices.Factory.get();
		RuntimeEnvironmentBuilder runtimeEnvironmentBuilder = RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder();
		runtimeEnvironmentBuilder.addAsset(kservices.getResources().newClassPathResource(bpmnFile), ResourceType.BPMN2);
		
		
		for(int i = 0; i < droolFileList.length; i++) {
			String droolFile = droolFileList[i];
			runtimeEnvironmentBuilder.addAsset(kservices.getResources().newClassPathResource(droolFile), ResourceType.DRL);
		}
		
		
		runtimeEnvironmentBuilder.addEnvironmentEntry(EnvironmentName.TRANSACTION_MANAGER, abstractPlatformTransactionManager);
		runtimeEnvironmentBuilder.entityManagerFactory(entityManagerFactory);
		runtimeEnvironmentBuilder.userGroupCallback(userGroupCallback);
		RuntimeEnvironment runtimeEnvironment = runtimeEnvironmentBuilder.get();
		manager = RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(runtimeEnvironment, identifier);
		
	}	
	
	public List<TaskSummary> getTaskAssignedForUser(String username) {
		RuntimeEngine runtimeEngine = manager.getRuntimeEngine(ProcessInstanceIdContext.get());	
        TaskService taskService = runtimeEngine.getTaskService();
        
        List<TaskSummary> resultList = taskService.getTasksAssignedAsPotentialOwner(username, "en-UK");
        manager.disposeRuntimeEngine(runtimeEngine);
        
        return resultList;
       
	}
	
	public List<TaskSummary> getTaskAssignedForUserForProcess(String username, String processName) {
		
		List<TaskSummary> resultList = new ArrayList<TaskSummary>();
		
		if(processName != null && !processName.equals("")) {
			List<TaskSummary> taskList = getTaskAssignedForUser(username);
			Iterator<TaskSummary> taskIterator = taskList.iterator();
			while(taskIterator.hasNext()) {
				TaskSummary currentTask = taskIterator.next();
				long processInstanceId = currentTask.getProcessInstanceId();
				WorkflowProcessInstance	processInstance = findProcessInstance(processInstanceId);
				if(processInstance != null && processName.equals(processInstance.getProcessName())) {
					resultList.add(currentTask);
				}
			}
		}
		
		
		return resultList;
	}
	
	public Task getTaskById(long id) {
		RuntimeEngine runtimeEngine = manager.getRuntimeEngine(ProcessInstanceIdContext.get());	
		TaskService taskService = runtimeEngine.getTaskService();
		return taskService.getTaskById(id);
	}
	
	public List<Long> getTaskIdsByProcessInstanceId(long processInstanceId) {
		RuntimeEngine runtimeEngine = manager.getRuntimeEngine(ProcessInstanceIdContext.get());	
		TaskService taskService = runtimeEngine.getTaskService();
		List<Long> resultList = taskService.getTasksByProcessInstanceId(processInstanceId);
		
		
		return resultList;
	}
	
	public List<Task> getTasksByProcessInstanceId(long processInstanceId) {
		List<Task> resultList = new ArrayList<Task>();
		List<Long> taskIdList = getTaskIdsByProcessInstanceId(processInstanceId);
		Iterator<Long> taskIdIterator = taskIdList.iterator();
		while(taskIdIterator.hasNext()) {
			Long currentTaskId = taskIdIterator.next();
			Task currentTask = getTaskById(currentTaskId);
			resultList.add(currentTask);
		}
		
		return resultList;
	}
	
	public WorkflowProcessInstance findProcessInstance(long processInstanceId) {
		RuntimeEngine runtimeEngine = manager.getRuntimeEngine(ProcessInstanceIdContext.get());	
		
		KieSession ksession = runtimeEngine.getKieSession();
		
		WorkflowProcessInstance processInstance = (WorkflowProcessInstance) ksession.getProcessInstance(processInstanceId);
		
		return processInstance;
	}
	
	
	public long startProcess(String processName) {
		
		RuntimeEngine runtimeEngine = manager.getRuntimeEngine(ProcessInstanceIdContext.get());	
		
		KieSession ksession = runtimeEngine.getKieSession();
		ProcessInstance processInstance = ksession.startProcess(processName);
		
		long processInstanceId = processInstance.getId();
		
		return processInstanceId;
		
	}
	
	public long startProcessWithInitialParameters(String processName, Map<String, Object> parameterMap) {
		
		RuntimeEngine runtimeEngine = manager.getRuntimeEngine(ProcessInstanceIdContext.get());	
			
		KieSession ksession = runtimeEngine.getKieSession();
		ProcessInstance processInstance = ksession.startProcess(processName, parameterMap);
		long processInstanceId = processInstance.getId();
		
		return processInstanceId;
	}
	
	public long startProcessWithInitialParametersAndFireBusinessRules(String processName, Map<String, Object> parameterMap) {
		
		RuntimeEngine runtimeEngine = manager.getRuntimeEngine(ProcessInstanceIdContext.get());	
			
		KieSession ksession = runtimeEngine.getKieSession();
		ProcessInstance processInstance = ksession.startProcess(processName, parameterMap);
		long processInstanceId = processInstance.getId();
		ksession.fireAllRules();
		return processInstanceId;
	}
	
	public void submitTask(String username, long taskId, HashMap<String, Object> parameterMap) {
		TaskService taskService=null;
		try{
		RuntimeEngine runtimeEngine = manager.getRuntimeEngine(ProcessInstanceIdContext.get());
		
		taskService = runtimeEngine.getTaskService();
		
		taskService.start(taskId, username);
		taskService.complete(taskId, username, parameterMap);
		}catch(Exception e){
			e.printStackTrace();
			/*if(taskService!=null){
				taskService.exit(taskId, username);
				throw new BSLException("error.leaveapp.terminate");
			}*/
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getContentForTask(Task task) {
		RuntimeEngine runtimeEngine = manager.getRuntimeEngine(ProcessInstanceIdContext.get());
		
		TaskService taskService = runtimeEngine.getTaskService();
		
		Content contentById = taskService.getContentById(task.getTaskData().getDocumentContentId());

        Map<String, Object> taskContent = (Map<String, Object>) ContentMarshallerHelper.unmarshall(contentById.getContent(), null);
        return taskContent;
	}

}
