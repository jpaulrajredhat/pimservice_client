package com.httpclient;

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
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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

	

public CloseableHttpClient getHttpCertClientAuth() throws IOException, CertificateException{
	
	
	 CloseableHttpClient httpClient = null;;
	
	
		
		SSLConnectionSocketFactory csf = null;
		try {
			csf = new SSLConnectionSocketFactory(
					createSslContext(),
					//new String[]{"TLSv1"}, // Allow TLSv1 protocol only
			        //null,
			        SSLConnectionSocketFactory.getDefaultHostnameVerifier()
					
					//NoopHostnameVerifier.INSTANCE
					);
			

		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
		 
		
	 
	
	
	 
	return  httpClient;

}

public  SSLContext createSslContext() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, KeyManagementException, UnrecoverableKeyException {
    
	// Trusted CA keystore
	
	final String CA_KEYSTORE_TYPE = KeyStore.getDefaultType(); //"JKS";
	final String CA_KEYSTORE_PATH = System.getProperty("CA_KEYSTORE_PATH", "/cacert.jks") ;
	final String CA_KEYSTORE_PASS = System.getProperty("CA_KEYSTORE_PASS ", "changeit") ;

//	final String CLIENT_KEYSTORE_TYPE = System.getProperty("CLIENT_KEYSTORE_TYPE", "PKCS12") ;
//	final String CLIENT_KEYSTORE_PATH = System.getProperty("CLIENT_KEYSTORE_PATH", "/client.p12") ;
//	final String CLIENT_KEYSTORE_PASS = System.getProperty("CLIENT_KEYSTORE_PASS ", "changeit") ;
	
    KeyStore tks = KeyStore.getInstance(CA_KEYSTORE_TYPE);
    tks.load(new FileInputStream(CA_KEYSTORE_PATH), CA_KEYSTORE_PASS.toCharArray());

//    // Client keystore
//    KeyStore cks = KeyStore.getInstance(CLIENT_KEYSTORE_TYPE);
//    cks.load(new FileInputStream(CLIENT_KEYSTORE_PATH), CLIENT_KEYSTORE_PASS.toCharArray());

    SSLContext sslcontext = SSLContexts.custom()
            .loadTrustMaterial(tks, new TrustSelfSignedStrategy()) // use it to customize
            .loadKeyMaterial(tks, CA_KEYSTORE_PASS.toCharArray()) // load client certificate
            .build();
    return sslcontext;
}



}