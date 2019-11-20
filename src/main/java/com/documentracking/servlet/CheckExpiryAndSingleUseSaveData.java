package com.documentracking.servlet;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.jcr.Node;
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

import com.documenttracking.classonly.GetAllMethodsHere;

@SuppressWarnings("serial")
@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/CheckExpiryAndSingleUseSaveData" }),
		@Property(name = "sling.servlet.extensions", value = { "addAccount", "account", "success", "ajax", "newBlog",
				"ajaxBlog", "searchBlog", "search", "following", "follower", "userContent", "userPost", "userDraft",
				"userQueue", "home", "menu", "likeBlog", "deleteBlog", "tagSearch", "followerSearch", "edit",
				"viewBlog", "tagPosts", "blogSearch", "deleteBlogId", "deleteAccount", "confirmAccount", "confirmBlog",
				"randomBlog" })

})
public class CheckExpiryAndSingleUseSaveData extends SlingAllMethodsServlet {


	@Reference
	private SlingRepository repo;

	Session session = null;

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out=response.getWriter();
	       
	       try {
			
	    	   session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
	    	
	    	   BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
	    	   ByteArrayOutputStream buf = new ByteArrayOutputStream();
	    	   int result = bis.read();

	    	   while (result != -1) {
	    	   buf.write((byte) result);
	    	   result = bis.read();
	    	   }
	    	   String res = buf.toString("UTF-8");
	    	   JSONObject resultjsonobject=new JSONObject(res);
	    	   
	    	   String email="";
	    	   String nodeName="";
				
				JSONObject js = new JSONObject();
	    	   
	    	   if(resultjsonobject.has("Email")){
	    		   email=resultjsonobject.getString("Email");
	    	   }
				if(resultjsonobject.has("nodeName")) {
					nodeName = resultjsonobject.getString("nodeName");
				}
	    	   GetAllMethodsHere GAMH =new GetAllMethodsHere(email, session, response);
	    	   Node userEmailNode= GAMH.freeTrialCheck();
	    	   
	    	   if( userEmailNode!=null ){
	    		   
	    		   if( !GetAllMethodsHere.isNullString(nodeName) ){

	    			  if( session.getNode(nodeName) != null ){
	    				  
	    				 Node nodeNameNode= session.getNode(nodeName);
	    				  
	    				 if( resultjsonobject.has("expiryDate") && resultjsonobject.getString("expiryDate").equalsIgnoreCase("expiryDate") ){
	    					 
	    					 if( nodeNameNode.hasProperty("expiryDate") ){
	    						 nodeNameNode.setProperty("expiryDate", resultjsonobject.getString("date"));
	    					 }else{
	    						 nodeNameNode.setProperty("expiryDate", resultjsonobject.getString("date"));
	    					 }
	    					 
	    					 session.save();
	    					 out.println("saved");
	    					 
	    				 } // expiryDate close check
	    				 
	    				 else{
	    					 if( resultjsonobject.has("singleUse") && resultjsonobject.getString("singleUse").equalsIgnoreCase("singleUse") ){
	    						 if( nodeNameNode.hasProperty("singleUse") ){
		    						  nodeNameNode.setProperty("singleUse", "0");
		    					 }else{
		    						 nodeNameNode.setProperty("singleUse", "0");
		    					 }
	    						 
	    						 session.save();
	    						 out.println("saved");
	    						 
	    					 }
	    					 
	    				 }
	    				 
	    				
	    				 
	    			  }
	    			   
	    		   
	    	   }
	    		   
	    	   } // userEmailNode blank check
	    	   else {
					js.put("status", "error");
					js.put("message", "Invalid user");
					out.println(js);
				}
	    	   
	    	   
	    	   
		} catch (Exception e) {
			e.printStackTrace(out);
		}

		

	}

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
	}

}
