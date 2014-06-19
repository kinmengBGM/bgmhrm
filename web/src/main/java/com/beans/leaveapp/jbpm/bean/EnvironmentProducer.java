package com.beans.leaveapp.jbpm.bean;

import javax.enterprise.context.ApplicationScoped;
import org.kie.internal.io.ResourceFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

import org.kie.api.io.ResourceType;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.internal.runtime.manager.cdi.qualifier.PerProcessInstance;
import org.kie.internal.runtime.manager.cdi.qualifier.PerRequest;
import org.kie.internal.runtime.manager.cdi.qualifier.Singleton;




@ApplicationScoped
public class EnvironmentProducer {

	@Inject
	private EntityManagerFactory emf;
	private BeanManager beanManager;
	
	@PersistenceUnit(unitName = "masterDataMysql")
	@ApplicationScoped
	@Produces
	public EntityManagerFactory getEntityManagerFactory() {
		if(this.emf == null) {

			
			System.out.println("Retrieve Entity manager factory...");
			this.emf = Persistence.createEntityManagerFactory("masterDataMysql");
			System.out.println("Retrieved Entity manager factory!!!");
		}
		return emf;
	}
	
	@Produces
	@Singleton
	@PerRequest
	@PerProcessInstance
	public RuntimeEnvironment produceEnvironment(EntityManagerFactory myEMF) {
		
		RuntimeEnvironment renvironment = null;
				
		try {

			System.out.println("Create runtime builder");
			RuntimeEnvironmentBuilder rbuilder = RuntimeEnvironmentBuilder.Factory.get()
					.newDefaultBuilder()
					.entityManagerFactory(emf);

			System.out.println("Runtime builder created");
			
			
			System.out.println("Adding assets to the runtime builder...");
			rbuilder.addAsset(ResourceFactory.newClassPathResource("sample.bpmn"), ResourceType.BPMN2);
			System.out.println("Added assets to the runtime builder.");
	
			System.out.println("Create runtime environment");
			renvironment = rbuilder.get();
			
			System.out.println("Runtime environment created");

			
		}catch(Exception excep) {
			excep.printStackTrace();
		}
		return renvironment;
	}
}
