package com.pimservice;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.httpclient.HttpClient;

public class MigrationService {
	
	

	public MigrationService() {
		// TODO Auto-generated constructor stub
	}
	
	public void migrate(Plan plan) throws ClientProtocolException, IOException {
		
		
		String source =  plan.getSourceContainer();
		String target = plan.getTargetContainer();
		String processId = plan.getProcessId();
		
		CreatePlanService planService = new CreatePlanService();
		
		List<Long> processInstances = planService.getProcessInstancestoMigrate(source, target, processId);
		
		
		HttpClient httpClient = new HttpClient(); 
		
		CloseableHttpClient client = httpClient.getHttpClientWithBasicAuth();
		
		try {
			
			String url = System.getProperty("KIE_SERVER_URL", "http://localhost:8080");
			
			String queryParam = "?";
	
			
			for (int i = 0; i < processInstances.size(); i++) {
	        	
	        	
				String str =  Long.toString(processInstances.get(i));
	        	queryParam += "processInstanceId="+ str + "&" ;
	        	                	
			}
			
			queryParam += "targetContainerId=" + target + "&targetProcessId=" +processId ; 
			
			String migUrl = url + "/kie-server/services/rest/server/admin/containers/"+ source + "/processes/instances" + queryParam;
			
			System.out.println("query param " + migUrl);
	
			HttpPut httpPut = new HttpPut(migUrl );
			
			httpPut.setHeader("Content-Type", "application/json");
	
	        CloseableHttpResponse response = client.execute(httpPut);
	        try {
	        
		        System.out.println(response.getStatusLine().getStatusCode());   
		
		        HttpEntity entity = response.getEntity();
		        
		        if (entity != null) {
		        	
		        	String result = EntityUtils.toString(entity);
		           
		        	System.out.println(result);
		        }
		        
	        }finally {
				response.close();
			}

		
		} finally {
			
			client.close();
		}
		
		
		
	}
	
	
	
public static void main(String[] args) throws Exception {
    	
		MigrationService service = new MigrationService();
		
		Plan plan = new Plan ("pimdemoprocess_1.0.0", "pimdemoprocess_1.0.1", "pimdemoprocess.rhpimprocess");
    	
    	service.migrate(plan);
    
    }

}
