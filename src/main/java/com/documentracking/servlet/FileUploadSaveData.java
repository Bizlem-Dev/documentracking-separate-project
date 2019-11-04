package com.documentracking.servlet;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

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
import com.sun.jersey.core.util.Base64;

@SuppressWarnings("serial")
@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/FileUploadedSaveData" }),
		@Property(name = "sling.servlet.extensions", value = { "addAccount", "account", "success", "ajax", "newBlog",
				"ajaxBlog", "searchBlog", "search", "following", "follower", "userContent", "userPost", "userDraft",
				"userQueue", "home", "menu", "likeBlog", "deleteBlog", "tagSearch", "followerSearch", "edit",
				"viewBlog", "tagPosts", "blogSearch", "deleteBlogId", "deleteAccount", "confirmAccount", "confirmBlog",
				"randomBlog" })

})
public class FileUploadSaveData extends SlingAllMethodsServlet {


	@Reference
	private SlingRepository repo;

	Session session = null;

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out=response.getWriter();
	       
	       try {
			
	    	   session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
	    	
	    	   JSONObject resultjsonobject=new JSONObject(request.getParameter("getUploadedFileData"));
	    	  
	    	   String email="";
	    	   String group="";
	    	   String filenameNodeSling="";
	    	   String filedata="";
	    	   String fileExtnsion="";
	    	   String nodeNameString="";
	    	   
	    	    Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				
				JSONObject js = new JSONObject();
	    	   
	    	   if(resultjsonobject.has("Email")){
	    		   email=resultjsonobject.getString("Email");
	    	   }if(resultjsonobject.has("group")) {
				   group = resultjsonobject.getString("group");
				}if(resultjsonobject.has("filename")) {
					filenameNodeSling = resultjsonobject.getString("filename");
					
					if(filenameNodeSling.lastIndexOf(".")!=-1){
						 fileExtnsion = filenameNodeSling.substring(filenameNodeSling.lastIndexOf(".") + 1);
						 
						nodeNameString= filenameNodeSling.substring(0,filenameNodeSling.lastIndexOf("."));
					}
					
				}if(resultjsonobject.has("filedata")) {
					filedata = resultjsonobject.getString("filedata");
				}
//				 out.println("filedata_blank: "+filedata);
	    	   GetAllMethodsHere GAMH =new GetAllMethodsHere(email, session, response);
	    	   Node userEmailNode= GAMH.freeTrialCheck();
	    	   
	    	   if( userEmailNode!=null ){
	    		   
	    		   if( !GetAllMethodsHere.isNullString(filenameNodeSling) ){
	    		   Node groupNode=null;
	    		   Node DocumentTracking=null;
	    		   Node fileNameNode=null;
	    		   
	    		   if (userEmailNode.getPath().toString().contains("freetrial")) {
	    			   
	    			   if(userEmailNode.hasNode("DocumentTracking")){
    					   DocumentTracking= userEmailNode.getNode("DocumentTracking");
    				   }else{
    					   DocumentTracking= userEmailNode.addNode("DocumentTracking");
    					   DocumentTracking.setProperty("documentTrackingQuantity", 0);
    				   }
	    			   
	    			   if(DocumentTracking!=null){
	    				   boolean fileNameCheck= GAMH.checkFileNameFromSling(out, DocumentTracking, nodeNameString, session);
    					   
    					   if(fileNameCheck==false){
    						   
    						   if(DocumentTracking.hasNode(nodeNameString)){
 	    						  fileNameNode= DocumentTracking.getNode(nodeNameString);
 	    					   } // filename check uploaded
 	    					   else{
 	    						   fileNameNode= DocumentTracking.addNode(nodeNameString);
 	    					   }
    						   
    						   if(fileNameNode!=null){
    							   fileNameNode.setProperty("documentName", nodeNameString);
	    						   fileNameNode.setProperty("fileExtension", fileExtnsion);
	    						   fileNameNode.setProperty("group", group);
	    						   fileNameNode.setProperty("userEmail", email);
	    						   fileNameNode.setProperty("documentUploadedDate", c);
	    						   fileNameNode.setProperty("filenameplusextension", filenameNodeSling);
	    						   
	    						   // generate slingFileUrl 
	    						   
	    						   byte[] bytes = Base64.decode(filedata);
	    						  
	    						   InputStream myInputStream = new ByteArrayInputStream(bytes);
	    						   
	    						   long lsct = DocumentTracking.getProperty("documentTrackingQuantity").getLong();
	    						   DocumentTracking.setProperty("documentTrackingQuantity", lsct + 1);
	    						   
	    						   Node subfileNode = fileNameNode.addNode(filenameNodeSling, "nt:file");
	    						   Node jcrNode = subfileNode.addNode("jcr:content", "nt:resource");
	    						   jcrNode.setProperty("jcr:data", myInputStream);
	    						   jcrNode.setProperty("jcr:mimeType","application/octet-stream");
	    						   
	    						   //fileNameNode.setProperty("fileUrl",request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+ fileNameNode.getPath()+"/"+subfileNode.getName());
	    						  String url =   "https://" + request.getServerName() + ":8083"
											 + request.getContextPath()
											+ DocumentTracking.getPath()+"/"+nodeNameString+"/" + filenameNodeSling;
	    						  fileNameNode.setProperty("fileUrl",url);
	    						   js.put("status", "success");
	    						   out.println(js);
    						   }
    						   
    					   }
    					   else {
	   							js.put("status", "error");
	   							js.put("message", "filename Equals, please try with different file Name");
	   							out.println(js);
	   						}
	    					   session.save();
	    			   }
	    			   
	    		   } // freetrial check here
	    		   else{
	    			   
	    			   if(userEmailNode.hasNode(group)){
	    				   groupNode= userEmailNode.getNode(group);
	    				   
	    				   if(groupNode.hasNode("DocumentTracking")){
	    					   DocumentTracking= groupNode.getNode("DocumentTracking");
	    				   }else{
	    					   DocumentTracking= groupNode.addNode("DocumentTracking");
	    					   userEmailNode.setProperty("documentTrackingQuantity", 0);
	    				   }
	    				   
	    				   if(DocumentTracking!=null){
	    					   
	    					   boolean fileNameCheck= GAMH.checkFileNameFromSling(out, DocumentTracking, nodeNameString, session);
	    					   
	    					   if(fileNameCheck==false){
	    					   
	    					   if(DocumentTracking.hasNode(nodeNameString)){
	    						  fileNameNode= DocumentTracking.getNode(nodeNameString);
	    					   } // filename check uploaded
	    					   else{
	    						   fileNameNode= DocumentTracking.addNode(nodeNameString);
	    					   }
	    					   
	    					   if(fileNameNode!=null){
	    						   
	    						   fileNameNode.setProperty("documentName", nodeNameString);
	    						   fileNameNode.setProperty("fileExtension", fileExtnsion);
	    						   fileNameNode.setProperty("group", group);
	    						   fileNameNode.setProperty("userEmail", email);
	    						   fileNameNode.setProperty("documentUploadedDate", c);
	    						   fileNameNode.setProperty("filenameplusextension", filenameNodeSling);
	    						   
	    						   // generate slingFileUrl 
	    						   
	    						   byte[] bytes = Base64.decode(filedata);
//	    						   out.println("filedata_servlet: "+filedata);
	    						   InputStream myInputStream = new ByteArrayInputStream(bytes);
	    						   
	    						   long lsct = userEmailNode.getProperty("documentTrackingQuantity").getLong();
	    						   userEmailNode.setProperty("documentTrackingQuantity", lsct + 1);
	    						   
	    						   Node subfileNode = fileNameNode.addNode(filenameNodeSling, "nt:file");
	    						   Node jcrNode = subfileNode.addNode("jcr:content", "nt:resource");
	    						   jcrNode.setProperty("jcr:data", myInputStream);
//	    						   jcrNode.setProperty("jcr:mimeType", "attach");
//	    						   jcrNode.setProperty("jcr:mimeType","application/pdf");
	    						   jcrNode.setProperty("jcr:mimeType","application/octet-stream");
	    						   
	    						   //fileNameNode.setProperty("fileUrl",request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+ fileNameNode.getPath()+"/"+subfileNode.getName());
	    						  String url =   "https://" + request.getServerName() + ":8083"
											 + request.getContextPath()
											+ DocumentTracking.getPath()+"/"+nodeNameString+"/" + filenameNodeSling;
	    						  fileNameNode.setProperty("fileUrl",url);
	    						   js.put("status", "success");
	    						   out.println(js);
	    					   } // fileNameNode null check here
	    					   
	    				   }// check filenameQuery here
	    					   else {
	   							js.put("status", "error");
	   							js.put("message", "filename Equals, please try with different file Name");
	   							out.println(js);
	   						}
	    					   session.save();
	    				   } // DocumentTracking null check here
	    				   
	    			   } // group check
	    			   
	    		   } // shopping end here
	    		   
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
