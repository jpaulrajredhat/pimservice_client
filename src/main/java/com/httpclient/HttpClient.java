package com.httpclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.http.conn.ssl.TrustAllStrategy;
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

	public  CloseableHttpClient getHttpClientWithCertAuth1() {
	       
		
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
	


public  CloseableHttpClient getHttpClientWithCertAuth() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException, UnrecoverableKeyException {
	
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

public CloseableHttpClient getHttpCertAuth() throws IOException{
	
	final KeyStore truststore = readStore();

	final SSLContext sslContext;
	CloseableHttpClient httpClient = null;;
	
	  try {
		sslContext = SSLContexts.custom()
		      .loadTrustMaterial(truststore, new TrustAllStrategy())
		     // .loadKeyMaterial(truststore, "keyStorePass".toCharArray(), (aliases, socket) -> "qlikClient")
		      .loadKeyMaterial(truststore, "keyStorePass".toCharArray())
		      .build();
		
		 httpClient = HttpClients.custom().setSSLContext(sslContext).build();
	} catch (KeyManagementException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (UnrecoverableKeyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (KeyStoreException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
	return  httpClient;

}

public CloseableHttpClient getHttpCertClientAuth() throws IOException, CertificateException{
	
	
	final CloseableHttpClient httpClient;
	
	
		
		SSLConnectionSocketFactory csf = null;
		try {
			csf = new SSLConnectionSocketFactory(
					createSslContext(),
					new String[]{"TLSv1"}, // Allow TLSv1 protocol only
			        null,
			        SSLConnectionSocketFactory.getDefaultHostnameVerifier());
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

	final String CLIENT_KEYSTORE_TYPE = System.getProperty("CLIENT_KEYSTORE_TYPE", "PKCS12") ;
	final String CLIENT_KEYSTORE_PATH = System.getProperty("CLIENT_KEYSTORE_PATH", "/client.p12") ;
	final String CLIENT_KEYSTORE_PASS = System.getProperty("CLIENT_KEYSTORE_PASS ", "changeit") ;
	
    KeyStore tks = KeyStore.getInstance(CA_KEYSTORE_TYPE);
    tks.load(new FileInputStream(CA_KEYSTORE_PATH), CA_KEYSTORE_PASS.toCharArray());

    // Client keystore
    KeyStore cks = KeyStore.getInstance(CLIENT_KEYSTORE_TYPE);
    cks.load(new FileInputStream(CLIENT_KEYSTORE_PATH), CLIENT_KEYSTORE_PASS.toCharArray());

    SSLContext sslcontext = SSLContexts.custom()
            //.loadTrustMaterial(tks, new TrustSelfSignedStrategy()) // use it to customize
            .loadKeyMaterial(cks, CLIENT_KEYSTORE_PASS.toCharArray()) // load client certificate
            .build();
    return sslcontext;
}

public KeyStore readStore() throws IOException {
	  
	    InputStream keyStoreStream = new FileInputStream("KEYSTORE_PATH" );
	    KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance("PKCS12");
			 try {
				keyStore.load(keyStoreStream, "keyStorePass".toCharArray());
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
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // or "JKS"
	   
	    return keyStore;
	  
	}


}