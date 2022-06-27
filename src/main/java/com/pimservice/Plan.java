package com.pimservice;

import java.util.List;

public class Plan {

	private String sourceContainer;
	private String targetContainer;
	private String processId;
	private List<Long> processInstancesId ;
	
	
	
	
	public Plan(String sourceContainer, String targetContainer, String processId) {
		super();
		this.sourceContainer = sourceContainer;
		this.targetContainer = targetContainer;
		this.processId = processId;
	}



	public String getSourceContainer() {
		return sourceContainer;
	}



	public void setSourceContainer(String sourceContainer) {
		this.sourceContainer = sourceContainer;
	}



	public String getTargetContainer() {
		return targetContainer;
	}



	public void setTargetContainer(String targetContainer) {
		this.targetContainer = targetContainer;
	}



	public String getProcessId() {
		return processId;
	}



	public void setProcessId(String processId) {
		this.processId = processId;
	}



	public List<Long> getProcessInstancesId() {
		return processInstancesId;
	}



	public void setProcessInstancesId(List<Long> processInstancesId) {
		this.processInstancesId = processInstancesId;
	}

	public void createPlan() {
		
		
		
	}


	public Plan() {
		// TODO Auto-generated constructor stub
	}
	

}
