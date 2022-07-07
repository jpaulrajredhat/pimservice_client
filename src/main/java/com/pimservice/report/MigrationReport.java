package com.pimservice.report;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.pimservice.Migration;

import pimservice_client.PimKieMigrationService;

public class MigrationReport {
	
	
	private List<Migration>  migrations ;
	
	Map<String, List<MigrationResult> > migrationReports = new HashMap<String, List<MigrationResult>>();
	
	//List<MigrationResult> success = new ArrayList<MigrationResult>();
	//List<MigrationResult> failed = new ArrayList<MigrationResult>();
	
	public MigrationReport() {
		// TODO Auto-generated constructor stub
	}


	public MigrationReport(List<Migration> migrations) {
		super();
		this.migrations = migrations;
	}
	
	
	
	
	public void genReport() throws IOException, URISyntaxException {
		
		
		for (Migration migration : this.migrations) {
			//System.out.println(migration);
			
			String planId = migration.getPlan().getPlanId();
			
			if ("M".equalsIgnoreCase(migration.getExecution() )) {
				
				
				List<JsonArray> migRes = migration.getMigrationResults();
				
				if (migRes != null && migRes.size() >0) {
					processLog(migRes , migration.getPlan().getPlanId());
					//result.addAll(migresult);
				}
					
			}else {
				
				JsonArray array = migration.getMigrationResult();
				
				if (array != null) {
					
					System.out.println("is null array ");
					processLog(array,migration.getPlan().getPlanId());
					//result.addAll(migresult);
				}else {
					migration.getMigraationfailLog();
					MigrationResult mResult = new MigrationResult(null,migration.getMigraationfailLog(),false);
					List<MigrationResult> failed = migrationReports.get(planId + "_failed");
	        		if (failed==null) {
	        			
	        			failed = new ArrayList<MigrationResult>();
	        			failed.add(mResult);
	        			migrationReports.put(planId + "_failed", failed);
	        		}else {
	        			failed.add(mResult);
	        		}
					
				}
				
			}
			FileWriter file = null;
			FileWriter fileFAiled = null;
			CodeSource codeSource = PimKieMigrationService.class.getProtectionDomain().getCodeSource();

			Path source = Paths.get(codeSource.getLocation().toURI()).getParent();
			
			System.out.println(source.toAbsolutePath() + "/migrationlog/");
	        
			Path logfolder = Paths.get(source.toAbsolutePath() + "/migrationlog/");
	      
	        if(!Files.exists(logfolder)) {  // Noncompliant
	        	Files.createDirectories(logfolder);
	        }
	        
			try {
				//Gson gson = new Gson();
		        Gson gson = new GsonBuilder().setPrettyPrinting().create();

		        List<MigrationResult> success = migrationReports.get(planId+ "_success");
				String json = gson.toJson(success);
				
				file = new FileWriter(source + "/migrationlog/"+ migration.getPlan().getPlanId() +"_" +  migration.getPlan().getTargetContainer() + "_"  + LocalDateTime.now() + "-log.json");
			
				file.write(json);
				
				
				fileFAiled = new FileWriter(source + "/migrationlog/"+ planId + "_" + migration.getPlan().getTargetContainer() + "_"  + LocalDateTime.now() + "-logfailed.json");
				
		        List<MigrationResult> failed = migrationReports.get(planId + "_failed");

				String jsonfailed = gson.toJson(failed);
				fileFAiled.write(jsonfailed);
				
				
			
			}finally{
				if ( file != null) {
				file.close();
				}
				if ( fileFAiled != null) {
					fileFAiled.close();
					}
			}
			
		}
		//System.out.println(result);
		
	}
	public void processLog(JsonArray array , String planId) {
		
		//List<MigrationResult> result = new ArrayList<MigrationResult>();
		
		
		
		for (int i = 0; i < array.size(); i++) {
        	
        	
        	Long pid = array.get(i).getAsJsonObject().get("migration-process-instance").getAsLong();  
        	
        	JsonArray logs = array.get(i).getAsJsonObject().get("migration-logs").getAsJsonArray(); 
        	List<String> log = new ArrayList<String>();
        	
        	for (int j = 0; j < logs.size(); j++) {
        		
        		String e = logs.get(i).getAsString();
        		log.add(e);
        	}
        	
        	
        	boolean migSuccess = array.get(i).getAsJsonObject().get("migration-successful").getAsBoolean();  
        	
        

        	MigrationResult mResult = new MigrationResult(pid,log,migSuccess);
        	//result.add(mResult);
        	
        	if(migSuccess) {
        		List<MigrationResult> success = migrationReports.get(planId+ "_success");
        		
        		if (success == null ) {
        			success = new ArrayList<MigrationResult>();
        			success.add(mResult);
        			migrationReports.put(planId+ "_success", success);
        		}else {
        			success.add(mResult);
        		}
            	
        	}else {
        		List<MigrationResult> failed = migrationReports.get(planId + "_failed");
        		if (failed==null) {
        			
        			failed = new ArrayList<MigrationResult>();
        			failed.add(mResult);
        			migrationReports.put(planId + "_failed", failed);
        		}else {
        			failed.add(mResult);
        		}
            	
        	}
			        	
			        	
			//System.out.println("Report " + result);
		}
		
		
	}

	//public List<MigrationResult> processLog(List<JsonArray> arrays) {
	public void processLog(List<JsonArray> arrays ,  String planId) {
	
		List<MigrationResult> result = new ArrayList<MigrationResult>();
		for (JsonArray array : arrays) {
		
			processLog(array , planId);
	    	
			
		}
		
	}

}
