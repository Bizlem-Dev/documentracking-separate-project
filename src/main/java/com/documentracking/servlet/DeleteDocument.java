package com.documentracking.servlet;

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
import org.apache.sling.jcr.api.SlingRepository;

import com.documenttracking.classonly.GetAllMethodsHere;

@SuppressWarnings("serial")
@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/DeleteDocument" }),
		@Property(name = "sling.servlet.extensions", value = { "addAccount", "account", "success", "ajax", "newBlog",
				"ajaxBlog", "searchBlog", "search", "following", "follower", "userContent", "userPost", "userDraft",
				"userQueue", "home", "menu", "likeBlog", "deleteBlog", "tagSearch", "followerSearch", "edit",
				"viewBlog", "tagPosts", "blogSearch", "deleteBlogId", "deleteAccount", "confirmAccount", "confirmBlog",
				"randomBlog" })

})
public class DeleteDocument extends SlingAllMethodsServlet {


	@Reference
	private SlingRepository repo;

	Session session = null;

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
	    	   
	}

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out=response.getWriter();
		
		String email = request.getParameter("email");
		String group = request.getParameter("group");
		String fileName = request.getParameter("fileName");
	       
	       try {
			
	    	      session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
	    	
					 GetAllMethodsHere GAMH =new GetAllMethodsHere(email, session, response);
			    	 Node userEmailNode= GAMH.freeTrialCheck();
					
					if (userEmailNode != null) {
						if (userEmailNode.getPath().toString().contains("freetrial")) {

							if (userEmailNode.hasNode("DocumentTracking")) {
								Node DocumentTracking = userEmailNode.getNode("DocumentTracking");

								if (DocumentTracking.hasNode(fileName)) {
									DocumentTracking.getNode(fileName).remove();
									session.save();
									out.println("Document Removed");
								} // has nodes check
							}

						} // check freetrial here
						else {
							if (userEmailNode.hasNode(group)) {
								Node groupNode=userEmailNode.getNode(group);
								
								if(groupNode.hasNode("DocumentTracking")){
									Node DocumentTracking = groupNode.getNode("DocumentTracking");
									
									if (DocumentTracking.hasNode(fileName)) {
										DocumentTracking.getNode(fileName).remove();
										session.save();
										out.println("Document Removed");
									} // has nodes check
									
								}
								
							} // userEmailNode check here
						}

					} // userEmailNode blank check

			} catch (Exception e) {
				e.printStackTrace(out);
			}
		
		
	}
	
	

}
