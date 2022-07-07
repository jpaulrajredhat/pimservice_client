package com.pimservice.report;

import java.util.Arrays;
import java.util.List;

public class MigrationResult {
	
	
	private Long processinstanceId;
	private boolean status;
	private List<String> migrationLog;
	
	
	
	
	public MigrationResult() {
		// TODO Auto-generated constructor stub
	}



	public MigrationResult(Long processinstanceId, List<String>  migrationLog, boolean status) {
		super();
		this.processinstanceId = processinstanceId;
		this.migrationLog = migrationLog;
		this.status = status;
	}



	
	
}
