package com.documenttracking.classonly;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class BypassSSlCertificate {

	public static void main(String[] args) {
	}
    
	public static void ignoreHttps(String urlstring){
		try{
		if(urlstring.indexOf("https:") != -1){
		TrustManager[] trustAllCerts = new TrustManager[] {
		new X509TrustManager() {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		return null;
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType) { }

		public void checkServerTrusted(X509Certificate[] certs, String authType) { }

		}
		};


		try {
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		}

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
		return true;
		}
		};
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		/*
		* end of the fix
		*/
		}
		}catch(Exception e){

		}
		}
    
	

}
