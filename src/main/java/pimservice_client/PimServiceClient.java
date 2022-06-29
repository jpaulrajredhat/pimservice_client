package pimservice_client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pimservice.MigrationService;
import com.pimservice.MultiThreadMigrationService;
import com.pimservice.Plan;

public class PimServiceClient {

	public PimServiceClient() {
		// TODO Auto-generated constructor stub
	}
	
    public static void main(String[] args) throws Exception {
    
    	String migrationTYpe = System.getProperty("MIGRATION_TYPE", "containerAll");
    	
    	String source = null;
    	String target = null;
    	String proessId = null ; 
    	
        if (args.length > 0) {
            // Arrays.
        	source =args[0];
        	target =args[1];
        	proessId =args[2];
        }
    	
        Plan plan = new Plan (source, target, proessId);
        
    	if ("containerAll".equals(migrationTYpe)) {
	    	MigrationService service = new MigrationService();
	    	
			
			//Plan plan = new Plan ("pimdemoprocess_1.0.0", "pimdemoprocess_1.0.1", "pimdemoprocess.rhpimprocess");
	       
	    	
	    	service.migrate(plan);
    	}  else if ("batch".equals(migrationTYpe)) {
    		
    		MultiThreadMigrationService service = new MultiThreadMigrationService();
    		
        	
        	service.migrate(plan);
    	}
    	
    }
	

}
