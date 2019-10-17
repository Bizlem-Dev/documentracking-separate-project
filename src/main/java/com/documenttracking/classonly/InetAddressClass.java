package com.documenttracking.classonly;

//how to fetch public IP Address 
import java.net.*; 

public class InetAddressClass {

	public static void main(String[] args) throws UnknownHostException {

		// The URL for which IP address needs to be fetched 
//        String s = "https://uk.bluealgo.com"; // get domain to ip
		 String s = "http://bluealgo.com"; // get domain to ip
  
        try { 
            // Fetch IP address by getByName() 
            InetAddress ip = InetAddress.getByName(new URL(s).getHost()); 
                                                      
          /*  InetAddress host = InetAddress.getByName("35.197.227.150");
            System.out.println(host.getHostName());*/
            
            System.out.println(ip.getHostAddress());
            // Print the IP address 
//            System.out.println("Public IP Address of: " + ip); 
            
            
        } 
        catch (MalformedURLException e) { 
            // It means the URL is invalid 
            System.out.println("Invalid URL"); 
        } 
		
	}

}
