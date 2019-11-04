package com.documenttracking.classonly;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

@SuppressWarnings("serial")
@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/getClientip" }),
		@Property(name = "sling.servlet.extensions", value = { "addAccount", "account", "success", "ajax", "newBlog",
				"ajaxBlog", "searchBlog", "search", "following", "follower", "userContent", "userPost", "userDraft",
				"userQueue", "home", "menu", "likeBlog", "deleteBlog", "tagSearch", "followerSearch", "edit",
				"viewBlog", "tagPosts", "blogSearch", "deleteBlogId", "deleteAccount", "confirmAccount", "confirmBlog",
				"randomBlog" })

})
public class getClientIpAndDetails extends SlingAllMethodsServlet {


	@Reference
	private SlingRepository repo;

	Session session = null;

	private static final String[] IP_HEADER_CANDIDATES = { 
		    "X-Forwarded-For",
		    "Proxy-Client-IP",
		    "WL-Proxy-Client-IP",
		    "HTTP_X_FORWARDED_FOR",
		    "HTTP_X_FORWARDED",
		    "HTTP_X_CLUSTER_CLIENT_IP",
		    "HTTP_CLIENT_IP",
		    "HTTP_FORWARDED_FOR",
		    "HTTP_FORWARDED",
		    "HTTP_VIA",
		    "REMOTE_ADDR" };
	
	
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out=response.getWriter();
	       
	       try {
			
	    	   session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
	    	   
	  		try {
	  		
	  			String clientIp=getClientIpAddress(request);
	  			out.println("clientIp: "+clientIp);
	  			
	  			/*String ipAddress = request.getRemoteAddr();
	  			out.println("ipAddress_mkyong: "+ipAddress);*/
	  			
	  			if( !GetAllMethodsHere.isNullString(clientIp) ){
	  				String jsonStr=sendGet(clientIp);
//		  			out.println("jsonStr: "+jsonStr);
		  			if( GetAllMethodsHere.isJSONValid(jsonStr) ){
		  				 JSONObject obj=new JSONObject(jsonStr);
//		  				out.println("obj1: "+obj);
		  				 if(obj.length()!=0 && obj!=null){
		  					out.println("obj: "+obj);
		  				 }
		  			}
		  			
	  			}
	  			
	  			
	  			
	  		}catch (Exception e) {
	  			out.println(e.getMessage().toString());
	  		}
	    	  
	    	   
		} catch (Exception e) {
			e.printStackTrace(out);
		}

	}
	
	public static String getClientIpAddress(SlingHttpServletRequest request) {
	    for (String header : IP_HEADER_CANDIDATES) {
	        String ip = request.getHeader(header);
	        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
	            return ip;
	        }
	    }
	    return request.getRemoteAddr();
	}
	
	private static String sendGet(String ip) throws Exception {

        String url = "http://www.geoplugin.net/json.gp?ip="+ip;

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
       // httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");

//        int responseCode = httpClient.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.getInputStream())) ;

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            //print result
//            System.out.println(response.toString());
              return response.toString();
        }

    }

