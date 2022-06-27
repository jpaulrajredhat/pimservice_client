package com.pimservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.httpclient.HttpClient;

public class CreatePlanService {
	

	
	public CreatePlanService() {
		// TODO Auto-generated constructor stub
	}
	
	public List<Long>  getProcessInstancestoMigrate(String source , String target , String processId) throws  IOException {
		
		List<Long> instances =  new ArrayList<>();
		
		HttpClient httpClient = new HttpClient(); 
		
		CloseableHttpClient client = httpClient.getHttpClientWithBasicAuth();
		
		String url = System.getProperty("KIE_SERVER_URL", "http://localhost:8080");
    	
		
		
		boolean allFetched = false;
        int page = 0;
        int pageSize = 100;
        CloseableHttpResponse response = null;
        try {
        while (!allFetched) {
        	
        	String uri = url+ "/kie-server/services/rest/server/queries/containers/" + source + "/process/instances?status=1&page=" + page + "&pageSize=" + pageSize + "&sortOrder=true";
        	HttpGet request = new HttpGet(uri);
        	request.setHeader("Content-Type", "application/json");
        	
        	System.out.println(uri);

        	try {
       	     
                 response = client.execute(request);
                
                try {
                	
                
                	 System.out.println(response.getStatusLine().getStatusCode());   

                   	 HttpEntity entity = response.getEntity();
                   	 
                   	 if (entity != null) {
                   		 
                   		String result = EntityUtils.toString(entity);
                    	System.out.println(result);

                   		JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();

                   		
                		JsonArray array =  jsonObject.get("process-instance").getAsJsonArray();
                		
                		for (int i = 0; i < array.size(); i++) {
                        	
                			System.out.println("List Elements 22 " + array.get(i).getAsJsonObject().get("process-instance-id"));
                        	
                        	Long pid = array.get(i).getAsJsonObject().get("process-instance-id").getAsLong();     
                        	instances.add(pid);
        				}
                		
                		if (array.size() < pageSize) {
                       		System.out.println("instances" + instances);
                            allFetched = true;
                        }else {
                        	page ++;
                        }
                   	 }
                   	 
                   
                	
                }  catch (Exception ex) {
                	
                	System.out.print(ex.toString());
                	response.close();
                	
                }
                
                
        	} catch (ClientProtocolException ex ) {
        		
        		System.out.print(ex.toString());
        		client.close();
			}  
        	
        	
        }
        }finally {
			response.close();
			client.close();
		}
		return instances;
		
		
	}
	
	public void CreateMigrationPlan(String source , String target , String processId) throws ClientProtocolException, IOException {
		
		 List<Long> instances = getProcessInstancestoMigrate(source, target, processId);
		 
		
	}

	public void CreatePlan(String source , String target , String processId) throws ClientProtocolException, IOException {
		
		
		HttpClient httpClient = new HttpClient(); 
				
		//BASIC AUTH
		CloseableHttpClient client = httpClient.getHttpClientWithBasicAuth();
		
		//CERTIFICATE AUTH
		//CloseableHttpClient clientCERT = httpClient.getHttpClientWithCertAuth1();

		
		String url = System.getProperty("KIE_SERVER_URL", "http://localhost:8080");
    	
		HttpGet request = new HttpGet(url+ "/kie-server/services/rest/server/queries/containers/" + source + "/process/instances?status=1&page=0&pageSize=100&sortOrder=true");
    	
		request.setHeader("Content-Type", "application/json");

    	try {
    	     
            CloseableHttpResponse response = client.execute(request);

            try {
           	 
             // 401 if wrong user/password
           	 System.out.println(response.getStatusLine().getStatusCode());   

           	 HttpEntity entity = response.getEntity();
           	 
           	 if (entity != null) {
           		 // return it as a String
           		String result = EntityUtils.toString(entity);
            	System.out.println(result);


           		Gson gson = new Gson();

           		JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
           		
        		JsonArray array =  jsonObject.get("process-instance").getAsJsonArray();
        		
        		String queryParam = "?";
        		
        		for (int i = 0; i < array.size(); i++) {
                	
        			System.out.println("List Elements 22 " + array.get(i).getAsJsonObject().get("process-instance-id"));
                	
                	String str = array.get(i).getAsJsonObject().get("process-instance-id").getAsString();                	
                	queryParam += "processInstanceId="+ str + "&" ;
                	                	
				}
        		
        		queryParam += "targetContainerId=" + target + "&targetProcessId=" +processId ; 
        		
        		String migUrl = "http://localhost:8080/kie-server/services/rest/server/admin/containers/pimdemoprocess_1.0.0/processes/instances" + queryParam;
        		
        		System.out.println("query param " + migUrl);

        		HttpPut httpPut = new HttpPut(migUrl );
        		
        		httpPut.setHeader("Content-Type", "application/json");

                CloseableHttpResponse response1 = client.execute(httpPut);
                
                // 401 if wrong user/password
                System.out.println(response1.getStatusLine().getStatusCode());   

                HttpEntity entity1 = response1.getEntity();
                
                if (entity1 != null) {
                  		 // return it as a String
                	String result1 = EntityUtils.toString(entity1);
                   	System.out.println(result1);
                }

           	 }
            }finally {
            	response.close();
            }

       } finally {
       	
    	   client.close();
       }
		
    	//HttpPut request = new HttpPut("http://localhost:8080/kie-server/services/rest/server/containers");

	}
    public static void main(String[] args) throws Exception {
    	
    	CreatePlanService service = new CreatePlanService();
    	
    	//service.CreatePlan("pimdemoprocess_1.0.0", "pimdemoprocess_1.0.1", "pimdemoprocess.rhpimprocess");
    	service.getProcessInstancestoMigrate("pimdemoprocess_1.0.0", "pimdemoprocess_1.0.1", "pimdemoprocess.rhpimprocess");
    
    }
	
}
