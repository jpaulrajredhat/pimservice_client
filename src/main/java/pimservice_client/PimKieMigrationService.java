package pimservice_client;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pimservice.CreatePlanService;
import com.pimservice.Migration;
import com.pimservice.MigrationService;
import com.pimservice.MultiThreadMigrationService;
import com.pimservice.Plan;
import com.pimservice.report.MigrationReport;

public class PimKieMigrationService {

	public PimKieMigrationService() {
		// TODO Auto-generated constructor stub
	}
	
	public List<Migration> migrate() throws ClientProtocolException, IOException, URISyntaxException {
		
		CreatePlanService planService = new CreatePlanService();
    	
    	List<Plan>  plans = planService.createPlan();
	
    	List<Migration> migrations = new ArrayList<Migration>();
    	
    	for (Plan plan : plans) {
    		
    		Migration migration = new Migration();
    		
    		migration.setPlan(plan);
    		
    		if("M".equalsIgnoreCase(plan.getExecution() )) {
    			migration.setExecution("M");
    			MultiThreadMigrationService multiThreadMigrationService = new MultiThreadMigrationService();
    			multiThreadMigrationService.migrate(migration);
    		}else {
    			
    			MigrationService migrationService = new MigrationService();
        		migrationService.migrate(migration);

    			
    		}
    		migrations.add(migration);
    			
    		
			/*
			 * FileWriter file = null; CodeSource codeSource =
			 * PimKieMigrationService.class.getProtectionDomain().getCodeSource();
			 * 
			 * Path source = Paths.get(codeSource.getLocation().toURI()).getParent();
			 * 
			 * System.out.println(source.toAbsolutePath() + "/log/");
			 * 
			 * Path logfolder = Paths.get(source.toAbsolutePath() + "/log/");
			 * 
			 * if(!Files.exists(logfolder)) { // Noncompliant
			 * Files.createDirectories(logfolder); }
			 * 
			 * try {
			 * 
			 * file = new FileWriter(source + "/log/"+plan.getSourceContainer() +
			 * LocalDateTime.now() + "-log.json");
			 * 
			 * Gson gson = new GsonBuilder().setPrettyPrinting().create();
			 * 
			 * String migLog = migration.getMigrationLog() == null ?
			 * "No migration log exist " : migration.getMigrationLog();
			 * 
			 * String json = gson.toJson(migLog);
			 * 
			 * 
			 * file.write(json );
			 * 
			 * }finally{ if ( file != null) { file.close(); } }
			 */
    		
    	}
    	
    	return migrations;
		
	}

    public static void main(String[] args) throws Exception {

    	PimKieMigrationService service = new PimKieMigrationService();
    	List<Migration> migrtinos = service.migrate();
    	//System.out.println(migrtinos.size());
    	
    	MigrationReport report = new MigrationReport(migrtinos);
    	report.genReport();
    	
    	
    }
}
