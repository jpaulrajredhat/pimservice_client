package com.httpclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
public class HttpClient {

	public HttpClient() {
		// TODO Auto-generated constructor stub
	}
	
	public  CloseableHttpClient getHttpClientWithBasicAuth() {
       
			CredentialsProvider credsProvider = new BasicCredentialsProvider();

		
			credsProvider.setCredentials(
					new AuthScope("localhost", 8080),
					new UsernamePasswordCredentials("rhpamAdmin", "jboss123$"));
			CloseableHttpClient httpclient = HttpClients.custom()
					.setDefaultCredentialsProvider(credsProvider)
					.build();
			return httpclient;
    
	}

	public  CloseableHttpClient getHttpClientWithCertAuth() {
	       
		
		    try {
		        SSLContext  sslContext = SSLContexts
		                .custom()
		                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
		                .build();

		        SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext,
		                new DefaultHostnameVerifier());

		        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
		                .register("https", sslConnectionFactory)
		                .register("http", new PlainConnectionSocketFactory())
		                .build();

		        HttpClientBuilder builder = HttpClientBuilder.create();
		        builder.setSSLSocketFactory(sslConnectionFactory);
		        builder.setConnectionManager(new PoolingHttpClientConnectionManager(registry));

		        return builder.build();
		    } catch (Exception ex) {

		        return null;
		    }

		}
	


public  CloseableHttpClient getHttpClientWithCertAuth1() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException, UnrecoverableKeyException {
	
    KeyStore trustStore = KeyStore.getInstance("PKCS12"); //client certificate holder

    FileInputStream instream = new FileInputStream(new File(
            "client-p12-keystore.p12"));
    try {
        trustStore.load(instream, "password".toCharArray());
    } finally {
        instream.close();
    }

    // Trust own CA and all self-signed certs
    SSLContext sslcontext = SSLContexts.custom()
            .loadKeyMaterial(trustStore, "password".toCharArray())
            // .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()) //if you have a trust store
            .build();
    // Allow TLSv1 protocol only
    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
            sslcontext, new String[] { "TLSv1" }, null,
            SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    
    CloseableHttpClient httpclient = HttpClients
            .custom()
            .setHostnameVerifier(
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER) //todo
            .setSSLSocketFactory(sslsf).build();
    
	/*
	 * try {
	 * 
	 * HttpGett httpget = new HttpGet("https://localhost:8443/secure/index");
	 * 
	 * System.out.println("executing request" + httpget.getRequestLine());
	 * 
	 * CloseableHttpResponse response = httpclient.execute(httpget); try {
	 * HttpEntity entity = response.getEntity();
	 * 
	 * System.out.println("----------------------------------------");
	 * System.out.println(response.getStatusLine()); if (entity != null) {
	 * System.out.println("Response content length: " + entity.getContentLength());
	 * } EntityUtils.consume(entity); } finally { response.close(); } } finally {
	 * httpclient.close(); }
	 */
    
    return httpclient;


}
}