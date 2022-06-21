package com.pimservice;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class ClientCertificateAuthendication {


    public static void main(String[] args) throws Exception {
       
    	// this code will implemented for certiicate based authendication
    	HttpGet request = new HttpGet("http://localhost:8080/kie-server/services/rest/server/containers");

        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials("rhpamAdmin", "jboss123$")
        );

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(provider)
                .build();
        try {
     
             CloseableHttpResponse response = httpClient.execute(request);

             try {
            	 // 401 if wrong user/password
            	 System.out.println(response.getStatusLine().getStatusCode());   

            	 HttpEntity entity = response.getEntity();
            	 if (entity != null) {
            		 // return it as a String
            		 String result = EntityUtils.toString(entity);
            		 System.out.println(result);
            	 }
             	}finally {
             		response.close();
             	}

        } finally {
        	
        	httpClient.close();
        }
			
    }
    
}
    