package com.pimservice;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.httpclient.HttpClient;

public class MultiThreadMigrationService {

	public MultiThreadMigrationService() {
		// TODO Auto-generated constructor stub
	}

	public List<String> buildBatch(List<Long> processInstances , String source , String target , String processId) {
		
		
		List<String>  migurls = new ArrayList<String>();
		int noOfInstances = processInstances.size();
		
		int noOfInsperBacth = noOfInstances > 200 ? noOfInstances / 10 : noOfInstances;
		
		//
		String url = System.getProperty("KIE_SERVER_URL", "http://localhost:8080");
		
		System.out.println("total number of instances " + noOfInstances);
		
		System.out.println("number of instances per batch " + noOfInsperBacth);

		
		String queryParam = "?";
  
		int count =1;
		
		for (int i = 0; i < processInstances.size(); i++) {
        	
        	
			String str =  Long.toString(processInstances.get(i));
        	queryParam += "processInstanceId="+ str + "&" ;
        	if (count == noOfInsperBacth) {
        		
        		queryParam += "targetContainerId=" + target + "&targetProcessId=" +processId ; 
        		
        		String migUrl = url + "/kie-server/services/rest/server/admin/containers/"+ source + "/processes/instances" + queryParam;
        		migurls.add(migUrl);
        		count = 1;
        		queryParam = "?";
        		System.out.println("query param " + migUrl);
        	}else {
        		count ++;
        	}
        	                	
		}

		System.out.println("number of batches " + migurls.size() );

		return migurls;

		//
	}
	public void migrate(Plan plan) throws ClientProtocolException, IOException {
		

		String auth = System.getProperty("AUTH", "cert");

		String source =  plan.getSourceContainer();
		String target = plan.getTargetContainer();
		String processId = plan.getProcessId();
		
		CreatePlanService planService = new CreatePlanService();
		
		List<Long> processInstances = planService.getProcessInstancestoMigrate(source, target, processId);
		
		List<String>  migurls = buildBatch(processInstances, source, target, processId);
		
        final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);

        
        HttpClient httpClient = new HttpClient(); 
		 CloseableHttpClient client = null;
		
        
        if ("basic".equals(auth)) {
			
        	String userId = System.getProperty("BASIC_USERID", "rhpamAdmin");
			String password = System.getProperty("BASIC_PASSWORD", "jboss123");
        	 CredentialsProvider provider = new BasicCredentialsProvider();
             provider.setCredentials(
                     AuthScope.ANY,
                     new UsernamePasswordCredentials(userId, password)
             );
             client = HttpClients.custom()
                     .setConnectionManager(cm)
                     .setDefaultCredentialsProvider(provider)
                     .build() ;
		}else {
			 
				try {
					client = httpClient.getHttpCertClientAuth(cm);
					
				} catch (CertificateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
        
        try {
        	
                    
        	final GetThread[] threads = new GetThread[migurls.size()];
            for (int i = 0; i < threads.length; i++) {
                final HttpPut httpput = new HttpPut(migurls.get(i));
                httpput.setHeader("Content-Type", "application/json");
                threads[i] = new GetThread(client, httpput, null, i + 1);
            }

            // start the threads
            for (final GetThread thread : threads) {
                thread.start();
            }

            // join the threads
            for (final GetThread thread : threads) {
                try {
					thread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }	
               
        	
        }finally {
        	
        	
        }
		
		
	}
	
public void migrate(Migration migration) throws ClientProtocolException, IOException {
		

	
		String auth = System.getProperty("AUTH", "cert");
		Plan plan = migration.getPlan();
		String source =  plan.getSourceContainer();
		String target = plan.getTargetContainer();
		String processId = plan.getProcessId();
		
		List<Long> processInstances = plan.getProcessInstanceTobeMigrated();

		
		List<String>  migurls = buildBatch(processInstances, source, target, processId);
		
        final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);

        
        HttpClient httpClient = new HttpClient(); 
		 CloseableHttpClient client = null;
		
        
        if ("basic".equals(auth)) {
			
        	String userId = System.getProperty("BASIC_USERID", "rhpamAdmin");
			String password = System.getProperty("BASIC_PASSWORD", "jboss123$");
        	 CredentialsProvider provider = new BasicCredentialsProvider();
             provider.setCredentials(
                     AuthScope.ANY,
                     new UsernamePasswordCredentials(userId, password)
             );
             client = HttpClients.custom()
                     .setConnectionManager(cm)
                     .setDefaultCredentialsProvider(provider)
                     .build() ;
		}else {
			 
				try {
					client = httpClient.getHttpCertClientAuth(cm);
					
				} catch (CertificateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
        
        try {
        	
                    
        	final GetThread[] threads = new GetThread[migurls.size()];
            for (int i = 0; i < threads.length; i++) {
                final HttpPut httpput = new HttpPut(migurls.get(i));
                httpput.setHeader("Content-Type", "application/json");
                threads[i] = new GetThread(client, httpput, migration ,i + 1);
            }

            // start the threads
            for (final GetThread thread : threads) {
                thread.start();
            }

            // join the threads
            for (final GetThread thread : threads) {
                try {
					thread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }	
               
        	
        }finally {
        	
        	
        }
		
		
	}
	
	static class GetThread extends Thread {

        private final CloseableHttpClient httpClient;
        private final HttpContext context;
        private final HttpPut httpput;
        private final int id;
        private Migration migration;
        public GetThread(final CloseableHttpClient httpClient, final HttpPut httpput, Migration migration ,final int id) {
            this.httpClient = httpClient;
            this.context = new BasicHttpContext();
            this.httpput = httpput;
            this.id = id;
            this.migration = migration;
        }

        /**
         * Executes the GetMethod and prints some status information.
         */
        @Override
        public void run() {
            try {
                //System.out.println(id + " - about to get something from " + httpget.getUri());
                try {
                	
                	CloseableHttpResponse response = httpClient.execute(httpput, context);
                    System.out.println(id + " - get executed");
                    
                    // get the response body as an array of bytes
                    final HttpEntity entity = response.getEntity();
                    if (entity != null) {
                       //final byte[] bytes = EntityUtils.toByteArray(entity);
                    	String result = EntityUtils.toString(entity);
    		        	System.out.println(id + " - " + result);
    		        	
    		        	try {
	    		        	JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
	
	                   		
	                		JsonArray array =  jsonObject.get("migration-report-instance").getAsJsonArray();
	                		
	                		
	                		
	                		if (migration != null ) {
		                		String migLog = migration.getMigrationLog();
		                		
		                		if ( migLog !=null) {
		                			migLog.concat(array.toString());
		                		}else {
		                			migLog = new String(array.toString());
		             
		                			migration.setMigrationLog(migLog);
		                		}
		                		migration.addMigrationResults(array);
	                		}
                		
    		           }catch (Exception e) {
    		        		if ( result != null ) {
    		        			String planId = migration.getPlan().getPlanId();
    		        			migration.addMigraationfailLog("Error while processing plan Id - " + planId + "--> " + result);
    		        			//migration.getInstancesFailed()
    		        	
    		        		}
    		        	}
    		        	
                    }
                }finally {
					
				}
            } catch (final Exception e) {
                System.out.println(id + " - error: " + e);
            }
        }
	}
	
public static void main(String[] args) throws Exception {
    	
	MultiThreadMigrationService service = new MultiThreadMigrationService();
		
		Plan plan = new Plan ("pimdemoprocess_1.0.0", "pimdemoprocess_1.0.1", "pimdemoprocess.rhpimprocess");
    	List<Long> processInstances = new ArrayList<Long>();
    	
    	for (long i = 0; i < 1000; i++) {
    		processInstances.add(Long.valueOf(i));
    		
		}
    	//service.buildBatch(processInstances, "pimdemoprocess_1.0.0", "pimdemoprocess_1.0.1", "pimdemoprocess.rhpimprocess");
    	service.migrate(plan);
    
    }
}
