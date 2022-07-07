package com.pimservice;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;

public class Migration {

	private Plan plan;
	
	private String id;
	
	private List<Long> instancesTobeMigrted ;
	private List<Long> instanceseMigrated ;
	private List<Long> instancesFailed ;
	private String migrationLog;
	
	private int total;
	private int processed ;
	private int failed;
	
	private List<String> migraationSuccessLog = new ArrayList<String>();
	
	private List<String> migraationfailLog = new ArrayList<String>();
	
	private JsonArray migrationResult ;
	
	private List<JsonArray> migrationResults = new ArrayList<JsonArray>();
	
	private String execution ;
	
	public Migration() {
		// TODO Auto-generated constructor stub
	}

	public void addMigraationSuccessLog(String log) {
		
		migraationSuccessLog.add(log);
	}
	
	public void addMigraationfailLog(String log) {
		
		migraationfailLog.add(log);
	}
	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getProcessed() {
		return processed;
	}

	public void setProcessed(int processed) {
		this.processed = processed;
	}

	public int getFailed() {
		return failed;
	}

	public void setFailed(int failed) {
		this.failed = failed;
	}

	public List<String> getMigraationSuccessLog() {
		return migraationSuccessLog;
	}

	public void setMigraationSuccessLog(List<String> migraationSuccessLog) {
		this.migraationSuccessLog = migraationSuccessLog;
	}

	public List<String> getMigraationfailLog() {
		return migraationfailLog;
	}

	public void setMigraationfailLog(List<String> migraationfailLog) {
		this.migraationfailLog = migraationfailLog;
	}

	public List<Long> getInstancesTobeMigrted() {
		return instancesTobeMigrted;
	}

	public void setInstancesTobeMigrted(List<Long> instancesTobeMigrted) {
		this.instancesTobeMigrted = instancesTobeMigrted;
	}

	
	public List<Long> getInstancesFailed() {
		return instancesFailed;
	}

	public void setInstancesFailed(List<Long> instancesFailed) {
		this.instancesFailed = instancesFailed;
	}

	public List<Long> getInstanceseMigrated() {
		return instanceseMigrated;
	}

	public void setInstanceseMigrated(List<Long> instanceseMigrated) {
		this.instanceseMigrated = instanceseMigrated;
	}

	
	public String getMigrationLog() {
		return migrationLog;
	}

	public void setMigrationLog(String migrationLog) {
		this.migrationLog = migrationLog;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public JsonArray getMigrationResult() {
		return migrationResult;
	}

	public void setMigrationResult(JsonArray migrationResult) {
		this.migrationResult = migrationResult;
	}
	
	public void addMigrationResults(JsonArray jsonArray) {
		
		this.migrationResults.add(jsonArray);
	}
	

	public List<JsonArray> getMigrationResults() {
		return migrationResults;
	}

	public void setMigrationResults(List<JsonArray> migrationResults) {
		this.migrationResults = migrationResults;
	}

	public String getExecution() {
		return execution;
	}

	public void setExecution(String execution) {
		this.execution = execution;
	}

	@Override
	public String toString() {
		return "Migration [plan=" + plan + ", id=" + id + ", instancesTobeMigrted=" + instancesTobeMigrted
				+ ", instanceseMigrated=" + instanceseMigrated + ", instancesFailed=" + instancesFailed
				+ ", migrationLog=" + migrationLog + ", total=" + total + ", processed=" + processed + ", failed="
				+ failed + ", migraationSuccessLog=" + migraationSuccessLog + ", migraationfailLog=" + migraationfailLog
				+ ", migrationResult=" + migrationResult + ", migrationResults=" + migrationResults + ", execution="
				+ execution + "]";
	}

	
	
	


}
