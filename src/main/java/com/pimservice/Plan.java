package com.pimservice;

import java.util.Arrays;
import java.util.List;

public class Plan {

	private String id ;
	private String type;
	private String sourceContainer;
	private String targetContainer;
	private String processId;
	private List<String> processIds;
	private Long processInstance ;
	private List<Long> processInstanceTobeMigrated ;
	private List<Long> processInstances ;
	
	private String execution ;
	
	
	public Plan(String sourceContainer, String targetContainer, String processId) {
		super();
		this.sourceContainer = sourceContainer;
		this.targetContainer = targetContainer;
		this.processId = processId;
	}


	
	public String getExecution() {
		return execution;
	}



	public void setExecution(String execution) {
		this.execution = execution;
	}



	public List<Long> getProcessInstanceTobeMigrated() {
		return processInstanceTobeMigrated;
	}



	public void setProcessInstanceTobeMigrated(List<Long> processInstanceTobeMigrated) {
		this.processInstanceTobeMigrated = processInstanceTobeMigrated;
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



	

	public List<Long> getProcessInstances() {
		return processInstances;
	}



	public void setProcessInstances(List<Long> processInstances) {
		this.processInstances = processInstances;
	}



	public void createPlan() {
		
		
		
	}


	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public List<String> getProcessIds() {
		return processIds;
	}



	public void setProcessIds(List<String> processIds) {
		this.processIds = processIds;
	}



	public Long getProcessInstance() {
		return processInstance;
	}



	public void setProcessInstance(Long processInstance) {
		this.processInstance = processInstance;
	}



	



	@Override
	public String toString() {
		return "Plan [id=" + id + ", type=" + type + ", sourceContainer=" + sourceContainer + ", targetContainer="
				+ targetContainer + ", processId=" + processId + ", processIds=" + processIds + ", processInstance="
				+ processInstance + ", processInstanceTobeMigrated=" + processInstanceTobeMigrated
				+ ", processInstances=" + processInstances + "]";
	}



	public Plan() {
		// TODO Auto-generated constructor stub
	}
	

}
