package com.documentracking.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/DocumentNameData" }),
		@Property(name = "sling.servlet.extensions", value = { "addAccount", "account", "success", "ajax", "newBlog",
				"ajaxBlog", "searchBlog", "search", "following", "follower", "userContent", "userPost", "userDraft",
				"userQueue", "home", "menu", "likeBlog", "deleteBlog", "tagSearch", "followerSearch", "edit",
				"viewBlog", "tagPosts", "blogSearch", "deleteBlogId", "deleteAccount", "confirmAccount", "confirmBlog",
				"randomBlog" })

})
public class ServletForLinkExpire extends SlingAllMethodsServlet {


	@Reference
	private SlingRepository repo;

	Session session = null;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out=response.getWriter();
		response.setContentType("text/plain");
	       
	       try {
			
	    	   session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
	    	
	    	  
	    	   String paramEmail="email";
	    	   String paramGroup="group";
	    	   String paramFile="file";
	    	   
	    	   String email = request.getParameter(paramEmail);
	    	   String group = request.getParameter(paramGroup);
	    	   group=group.replace("%20", " ");
	    	   String nodeName = request.getParameter(paramFile);
	    	   nodeName=nodeName.replace("%20", " ");
	    	   
	    	   JSONObject js=new JSONObject();
	    	   
	    	   GetAllMethodsHere GAMH =new GetAllMethodsHere(email, session, response);
	    	   Node userEmailNode= GAMH.freeTrialCheck();
	    	   
	    	   if( userEmailNode!=null ){
	    		   
	    		   String nodePath=null;
	    		   
	    		   if (userEmailNode.getPath().toString().contains("freetrial")) {
	    			   
	    			   nodePath=userEmailNode.getPath()+"/"+"DocumentTracking"+"/"+nodeName;
	    			   
	    		   }else{
	    			   nodePath=userEmailNode.getPath()+"/"+group+"/"+"DocumentTracking"+"/"+nodeName;
	    		   }
	    			   
	    			   if( session.getNode(nodePath) != null ){
	    				   
	    				   Node nodeNameNode= session.getNode(nodePath);
	    				   
	    				   if (  nodeNameNode.hasProperty("fileUrl") && !GetAllMethodsHere.isNullString(nodeNameNode.getProperty("fileUrl").getString()) ) {
	    					   String fileUrl = nodeNameNode.getProperty("fileUrl").getString();
	    					   
	    					   if( nodeNameNode.hasProperty("expiryDate") && !GetAllMethodsHere.isNullString(nodeNameNode.getProperty("expiryDate").getString()) ){
		    					   
		    					   SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");	
		    					   Date endDate = formatter.parse(nodeNameNode.getProperty("expiryDate").getString());
		    					   
		    					     Calendar startDate = Calendar.getInstance();
		    				         startDate.set(Calendar.HOUR, 0);
		    				         startDate.set(Calendar.MINUTE, 0);
		    				         startDate.set(Calendar.SECOND, 0);
		    				         startDate.set(Calendar.HOUR_OF_DAY, 0);
		    					   
		    					     boolean checkData= compareDates(startDate.getTime(),endDate);
		    					     
		    					     //out.println(request.getRequestURL().toString());
		    					     
		    					  if( checkData ){ // true
		    						  //out.println(checkData);
		    						  
		    						  //HttpSession session = request.getSession();
		    				          //session.setAttribute("dataFile", fileUrl);
		    						  /*request.setAttribute("dataFile", fileUrl);
		    						  
		    						  RequestDispatcher rd = request.getRequestDispatcher("/content/static/.PdfJs");
		    						  rd.forward(request, response);*/
		    						  
		    						  js.put("status", "success");
		    						  js.put("fileUrl", fileUrl);
		    						  out.println(js);
		    						  
		    						  
//		    						  response.sendRedirect(fileUrl);
		    						  
		    					  }else{  // false 
		    						   /*response.setContentType("text/html");  
		    						   out.println("<script type=\"text/javascript\">");  
		    						   out.println("alert('The file is expired. You need a new one.');");  
		    						   out.println("</script>");*/
		    						  
		    						  js.put("status", "error");
		    						  js.put("message", "The file is expired. You need a new one.");
		    						  out.println(js);
		    						   
		    						   // move link(fileUrl) to same node properties
		    						   
		    						  /* if( nodeNameNode.hasProperty("movedUrl") ){
		    							   nodeNameNode.setProperty("movedUrl", fileUrl);
		    						   }else{
		    							   nodeNameNode.setProperty("movedUrl", fileUrl);
		    						   }*/
		    						   
//		    						   nodeNameNode.setProperty("fileUrl", "");
//		    						   session.save();
		    					  }
		    					   
		    					 }
	    					   
	    					   else if( nodeNameNode.hasProperty("singleUse") && !GetAllMethodsHere.isNullString(nodeNameNode.getProperty("singleUse").getString())){
		    					   
		    					   if( nodeNameNode.getProperty("singleUse").getString()!="1" ){ // 0 he
		    						   
		    						   nodeNameNode.setProperty("singleUse", "1");
//		    						   response.sendRedirect(fileUrl);
		    						   session.save();
		    						   
		    						   js.put("status", "success");
			    					   js.put("fileUrl", fileUrl);
			    					   out.println(js);
		    						   
		    					   } // check here zero
		    					   else{
		    						   /*response.setContentType("text/html");  
		    						   out.println("<script type=\"text/javascript\">");  
		    						   out.println("alert('The file is expired. You need a new one.');");  
		    						 
		    						   out.println("</script>");*/
		    						   
		    						   js.put("status", "error");
			    					   js.put("message", "The file is expired. You need a new one.");
			    					   out.println(js);
		    						   
		    						   /*if( nodeNameNode.hasProperty("movedUrl") ){
		    							   nodeNameNode.setProperty("movedUrl", fileUrl);
		    						   }else{
		    							   nodeNameNode.setProperty("movedUrl", fileUrl);
		    						   }
		    						   
		    						   nodeNameNode.setProperty("fileUrl", "");
		    						   session.save();*/
		    						   
		    					   }
		    					   
		    					 } // singleUse check here close
	    					   else{
	    						   js.put("status", "success");
		    					   js.put("fileUrl", fileUrl);
		    					   out.println(js);
	    					   }
	    					   
	    				   }else{
	    					   /*response.setContentType("text/html");  
	    					   out.println("<script type=\"text/javascript\">");  
	    					   out.println("alert('The file is expired. You need a new one.');");  
	    					   out.println("</script>");*/
	    					   
	    					   js.put("status", "error");
	    					   js.put("message", "Link not Found");
	    					   out.println(js);
	    					   
	    				   }
	    				   
	    			   } // session null check here
	    			   
	    	   } // userEmailNode blank check
	    	   else {
					js.put("status", "error");
					js.put("message", "Invalid user");
	    		   /*response.setContentType("text/html");  
				   out.println("<script type=\"text/javascript\">");  
				   out.println("alert('Invalid user');");  
				   out.println("</script>");*/
					out.println(js);
				}
	    	   
	    	   
	    	   
		} catch (Exception e) {
			e.printStackTrace(out);
		}

		

	}
	
	public static boolean compareDates(Date date1,Date date2)
    {
		boolean bool=false;
        // if you already have date objects then skip 1
        //1

        //1

        //date object is having 3 methods namely after,before and equals for comparing
        //after() will return true if and only if date1 is after date 2
        if(date1.after(date2)){
//            System.out.println("Date1 is after Date2");
            bool=false;
        }

        //before() will return true if and only if date1 is before date2
        if(date1.before(date2)){
//            System.out.println("Date1 is before Date2");
            bool=true;
        }

        //equals() returns true if both the dates are equal
        if(date1.equals(date2)){
//            System.out.println("Date1 is equal Date2");
            bool=true;
        }

//        System.out.println();
		return bool;
    }
	
	public static boolean nowDateBetweenStartDateAndEndDate(Date startDate, Date endDate) {
		  boolean bool = false;
		  Date curDate = new Date();
		  if(curDate.after(startDate) && curDate.before(endDate)) {
		    bool = true;
		  }
		  return bool;
		}

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
	}

}
