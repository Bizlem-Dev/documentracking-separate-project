package com.documentracking.servlet;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
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
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.documenttracking.classonly.BypassSSlCertificate;
import com.documenttracking.classonly.GetAllMethodsHere;
import com.sun.mail.iap.Response;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
 
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;


@SuppressWarnings("serial")
@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/GetUploadedData" }),
		@Property(name = "sling.servlet.extensions", value = { "addAccount", "account", "success", "ajax", "newBlog",
				"ajaxBlog", "searchBlog", "search", "following", "follower", "userContent", "userPost", "userDraft",
				"userQueue", "home", "menu", "likeBlog", "deleteBlog", "tagSearch", "followerSearch", "edit",
				"viewBlog", "tagPosts", "blogSearch", "deleteBlogId", "deleteAccount", "confirmAccount", "confirmBlog",
				"randomBlog" })

})
public class GetUploadedData extends SlingAllMethodsServlet {


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
	       
	       try {
			
	    	      session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
	    	
					 GetAllMethodsHere GAMH =new GetAllMethodsHere(email, session, response);
			    	 Node userEmailNode= GAMH.freeTrialCheck();
					
					if (userEmailNode != null) {
						if (userEmailNode.getPath().toString().contains("freetrial")) {

							if (userEmailNode.hasNode("DocumentTracking")) {
								Node DocumentTracking = userEmailNode.getNode("DocumentTracking");

								if (DocumentTracking.hasNodes()) {
									NodeIterator itr = DocumentTracking.getNodes();

									JSONObject tempnameObj = new JSONObject();
									JSONArray propertArray = new JSONArray();

									while (itr.hasNext()) {

										Node nextNodeOfTemplateInside = itr.nextNode();

												JSONObject getPropertyObj = new JSONObject();

												String documentName = "";
												String documentUploadedDate = "";
												String useremail = "";
												String group = "";
												String fileExtension="";
												String fileUrl="";
												String filenameplusextension="";
												
													getPropertyObj.put("nodeName", nextNodeOfTemplateInside.getPath());
												
												if (nextNodeOfTemplateInside.hasProperty("documentName")) {
													documentName = nextNodeOfTemplateInside.getProperty("documentName")
															.getString();
													getPropertyObj.put("documentName", documentName);
												}
												if (nextNodeOfTemplateInside.hasProperty("documentUploadedDate")) {
													documentUploadedDate = nextNodeOfTemplateInside.getProperty("documentUploadedDate")
															.getString();
													getPropertyObj.put("documentUploadedDate", documentUploadedDate);
												}
												if (nextNodeOfTemplateInside.hasProperty("userEmail")) {
													useremail = nextNodeOfTemplateInside.getProperty("userEmail")
															.getString();
													getPropertyObj.put("userEmail", useremail);
													
													if(useremail.lastIndexOf("@")!=-1){
														getPropertyObj.put("owner", useremail.substring(0,useremail.lastIndexOf("@")));
														//String domain = useremail.substring(useremail.lastIndexOf("@") +1);
													}
													
												}
												if (nextNodeOfTemplateInside.hasProperty("group")) {
													group = nextNodeOfTemplateInside.getProperty("group").getString();
													getPropertyObj.put("group", group);
												}
												if (nextNodeOfTemplateInside.hasProperty("fileExtension")) {
													fileExtension = nextNodeOfTemplateInside.getProperty("fileExtension")
															.getString();
													getPropertyObj.put("fileExtension", fileExtension);
												}if (nextNodeOfTemplateInside.hasProperty("fileUrl")) {
													fileUrl = nextNodeOfTemplateInside.getProperty("fileUrl")
															.getString();
													
//													String urlTemp=request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+"/portal/servlet/service/DocumentNameData?email="+email+"&group="+group+"&file="+nextNodeOfTemplateInside.getName().toString();
													String urlTemp=request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+"/portal/servlet/service/PdfJs?email="+email+"&group="+group+"&file="+nextNodeOfTemplateInside.getName().toString();
													
													getPropertyObj.put("urlTemp", urlTemp);
													getPropertyObj.put("fileUrl", fileUrl);
												}

												if (nextNodeOfTemplateInside.hasProperty("filenameplusextension")) {
													filenameplusextension = nextNodeOfTemplateInside.getProperty("filenameplusextension")
															.getString();
													getPropertyObj.put("filenameplusextension", filenameplusextension);
												}
												
												// document tracking code start here
												
												JSONArray documentTrack=callScriptDocument(nextNodeOfTemplateInside, session, response, documentUploadedDate,documentName,filenameplusextension);
												
												if(documentTrack.length()!=0 && documentTrack!=null){
													
													getPropertyObj.put("documentStatus", "open");
													getPropertyObj.put("noOfViewsDocument", documentTrack.length());
													
													JSONObject ipObj=documentTrack.getJSONObject(documentTrack.length()-1);
													if(ipObj.has("ip")){
														getPropertyObj.put("lastViewsByDocument", ipObj.getString("ip"));
													}
													
													HashMap<String,String> hashOut = new HashMap<String,String>();
													
													hashOut.clear();
											        for (int i=0;i<documentTrack.length();i++) {
											            JSONObject jo = new JSONObject(documentTrack.get(i).toString());
											            if(jo.has("ip")){
											            	hashOut.put(jo.getString("ip"), jo.getString("ip"));
											            }
											        }
													
													getPropertyObj.put("uniqueView",hashOut.size());
													
												} //documentTrack close here
												
												// end document tracking 
											
												getPropertyObj.put("type", nextNodeOfTemplateInside.getName());
												propertArray.put(getPropertyObj);

											} // while close itr2


									tempnameObj.put("documentTrackingData", propertArray);
									out.println(tempnameObj);
								} // has nodes check
							}

						} // check freetrial here
						else {
							if (userEmailNode.hasNodes()) {

								JSONObject tempnameObj = new JSONObject();
								JSONArray propertArray = new JSONArray();

								NodeIterator itruserEmailNode = userEmailNode.getNodes();
								while (itruserEmailNode.hasNext()) {
									Node nextNodeuserEmailNode = itruserEmailNode.nextNode();

									if (nextNodeuserEmailNode.hasNode("DocumentTracking")) {
										Node DocumentTracking = nextNodeuserEmailNode.getNode("DocumentTracking");

										if (DocumentTracking.hasNodes()) {
											NodeIterator itr = DocumentTracking.getNodes();

											while (itr.hasNext()) {

												Node nextNodeOfTemplateInside = itr.nextNode();

														JSONObject getPropertyObj = new JSONObject();

														String documentName = "";
														String documentUploadedDate = "";
														String useremail = "";
														String group = "";
														String fileExtension="";
														String fileUrl="";
														String filenameplusextension="";

														getPropertyObj.put("nodeName", nextNodeOfTemplateInside.getPath());
														
														if (nextNodeOfTemplateInside.hasProperty("documentName")) {
															documentName = nextNodeOfTemplateInside.getProperty("documentName")
																	.getString();
															getPropertyObj.put("documentName", documentName);
														}
														if (nextNodeOfTemplateInside.hasProperty("documentUploadedDate")) {
															documentUploadedDate = nextNodeOfTemplateInside.getProperty("documentUploadedDate")
																	.getString();
															getPropertyObj.put("documentUploadedDate", documentUploadedDate);
														}
														if (nextNodeOfTemplateInside.hasProperty("userEmail")) {
															useremail = nextNodeOfTemplateInside.getProperty("userEmail")
																	.getString();
															getPropertyObj.put("userEmail", useremail);
															
															if(useremail.lastIndexOf("@")!=-1){
																getPropertyObj.put("owner", useremail.substring(0,useremail.lastIndexOf("@")));
																//String domain = useremail.substring(useremail.lastIndexOf("@") +1);
															}
															
														}
														if (nextNodeOfTemplateInside.hasProperty("group")) {
															group = nextNodeOfTemplateInside.getProperty("group").getString();
															getPropertyObj.put("group", group);
														}
														if (nextNodeOfTemplateInside.hasProperty("fileExtension")) {
															fileExtension = nextNodeOfTemplateInside.getProperty("fileExtension")
																	.getString();
															getPropertyObj.put("fileExtension", fileExtension);
														}if (nextNodeOfTemplateInside.hasProperty("fileUrl")) {
															fileUrl = nextNodeOfTemplateInside.getProperty("fileUrl")
																	.getString();
															
															//String urlTemp=request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+"/portal/servlet/service/DocumentNameData?email="+email+"&group="+group+"&file="+nextNodeOfTemplateInside.getName().toString();
															String urlTemp=request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+"/portal/servlet/service/PdfJs?email="+email+"&group="+group+"&file="+nextNodeOfTemplateInside.getName().toString();
															getPropertyObj.put("urlTemp", urlTemp);
															
															getPropertyObj.put("fileUrl", fileUrl);
														}
 
														if (nextNodeOfTemplateInside.hasProperty("filenameplusextension")) {
															filenameplusextension = nextNodeOfTemplateInside.getProperty("filenameplusextension")
																	.getString();
															getPropertyObj.put("filenameplusextension", filenameplusextension);
														}
														
														// document tracking code start here
														
														JSONArray documentTrack=callScriptDocument(nextNodeOfTemplateInside, session, response, documentUploadedDate,documentName,filenameplusextension);
														//out.println(nextNodeOfTemplateInside+" ::  "+documentTrack);
														if(documentTrack.length()!=0 && documentTrack!=null){
															
															getPropertyObj.put("documentStatus", "open");
															getPropertyObj.put("noOfViewsDocument", documentTrack.length());
															
															JSONObject ipObj=documentTrack.getJSONObject(documentTrack.length()-1);
															if(ipObj.has("ip")){
																getPropertyObj.put("lastViewsByDocument", ipObj.getString("ip"));
															}
															
															HashMap<String,String> hashOut = new HashMap<String,String>();
															
															hashOut.clear();
													        for (int i=0;i<documentTrack.length();i++) {
													            JSONObject jo = new JSONObject(documentTrack.get(i).toString());
													            if(jo.has("ip")){
													            	hashOut.put(jo.getString("ip"), jo.getString("ip"));
													            }
													        }
															
															getPropertyObj.put("uniqueView",hashOut.size());
															
														} //documentTrack close here
															
														getPropertyObj.put("type", nextNodeOfTemplateInside.getName());

														propertArray.put(getPropertyObj);

													} // while close itr2

										} // has nodes check
									}

								} // while nextNodeuserEmailNode

								tempnameObj.put("documentTrackingData", propertArray);
								out.println(tempnameObj);

							} // userEmailNode check here
						}

					} // userEmailNode blank check

			} catch (Exception e) {
				e.printStackTrace(out);
			}
		
		
	}
	
	public JSONArray callScriptDocument(Node nextNodeOfTemplateInside, Session session, SlingHttpServletResponse rep, String GenerationDate,String documentName, String filenameplusextension){
		JSONArray arrayPropObj=new JSONArray();
		
		try {
			PrintWriter out=rep.getWriter();
			if(!GetAllMethodsHere.isNullString(documentName)){
				//out.println("documentNameapi: "+documentName);
				JSONObject sendInputMohitApi = new JSONObject();
				sendInputMohitApi.put("filename",filenameplusextension);
				sendInputMohitApi.put("projectName","sling");
				
				if (nextNodeOfTemplateInside.hasProperty("lastSyncDate")) {
					String lastSyncDate=nextNodeOfTemplateInside.getProperty("lastSyncDate").getString();
					 JSONArray docArrayObj=null;
					if(nextNodeOfTemplateInside.hasProperty("documentArray")){
					   String docArrayStr=nextNodeOfTemplateInside.getProperty("documentArray").getString();
					    docArrayObj=new JSONArray(docArrayStr);
					// out.println("docArrayObj: "+docArrayObj);
					}
					String afterData="";
					if(lastSyncDate.lastIndexOf(".")!=-1){
						lastSyncDate=lastSyncDate.substring(0, lastSyncDate.lastIndexOf("."));
						if(lastSyncDate.contains("T1")){
							String beforeData=lastSyncDate.substring(0,lastSyncDate.indexOf("T1"));
							 afterData=lastSyncDate.substring(lastSyncDate.indexOf("T1")+1);
							lastSyncDate=beforeData;
						}else if(lastSyncDate.contains("T2")){
							String beforeData=lastSyncDate.substring(0,lastSyncDate.indexOf("T2"));
							 afterData=lastSyncDate.substring(lastSyncDate.indexOf("T2")+1);
							lastSyncDate=beforeData;
						}
						if( !GetAllMethodsHere.isNullString(lastSyncDate) ){
							 Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(lastSyncDate);
							 List<Date> s = getDaysBetweenDates(date1, new Date());
							 if(!s.isEmpty() && s!=null){
								 for (int i=0;i<s.size();i++){
									    Date g = s.get(i);
										if( String.valueOf(date1.getDate()).equalsIgnoreCase(String.valueOf(g.getDate())) ){
											sendInputMohitApi.put("logfilename","localhost_access_log."+lastSyncDate + ".txt");
											sendInputMohitApi.put("timestamp",afterData);
											String syncResponse=getRemainingDocumentTimeResponse(sendInputMohitApi.toString());
//											out.println("syncResponse: "+syncResponse);
											if ( !GetAllMethodsHere.isNullString(syncResponse) ) {
												//out.println("insidenull: "+syncResponse);
												
												JSONObject resonseObj = new JSONObject(syncResponse);
												if (resonseObj.length() != 0 && resonseObj != null) {
													//out.println("resonseObj: "+resonseObj);
													String status = "";
													String hostname="";
													String dateTime="";
													
													JSONObject responseStrObj = new JSONObject(resonseObj.getString("outputdata"));
													if (responseStrObj.length() != 0 && responseStrObj != null) {
													//	out.println("responseStrObj: "+responseStrObj);
													
														if (responseStrObj.has("status")) {
															status = responseStrObj.getString("status");
															//out.println("status: "+status);
															
														}
														if (responseStrObj.has("hostname")) {
															 hostname = responseStrObj.getString("hostname");
															// out.println("hostname: "+hostname);
														}
														
														if (responseStrObj.has("dateTime")) {
															dateTime = responseStrObj.getString("dateTime");
														}
														
														if (status.equals("notOpen")) {

															if (nextNodeOfTemplateInside.hasProperty("documentArray")) {
																String documentArrayStr = nextNodeOfTemplateInside.getProperty("documentArray").getString();
																arrayPropObj=new JSONArray(documentArrayStr);
														}
															if(arrayPropObj.length()>0){
															Calendar c = Calendar.getInstance();
															c.setTime(new Date());
															nextNodeOfTemplateInside.setProperty("lastSyncDate", c);
															session.save();
															}
															
														} else {
															if (status.equals("open")) {
																
																String[] hostSplit = null;
																String dateTimeSplit[]=null;
																
																if( hostname.indexOf("#")!=-1 ){
																	hostSplit = hostname.split("#");
																}
																if( dateTime.indexOf("#")!=-1 ){
																	dateTimeSplit = dateTime.split("#");
																}
																
//																String hostSplit[] = hostname.split("#");
																//String dateTimeSplit[] = dateTime.split("#");
																
																//out.println("dateTimeSplit: "+dateTimeSplit);
																
																if( hostSplit!=null ){
																	for(int j=0;j<hostSplit.length;j++){
																		JSONObject ipdateObj=new JSONObject();
																		
																		String ip=hostSplit[j];
																		boolean bool=false;
																		String date="";
																		
																		if(dateTimeSplit!=null){
																		try {
																			 date=dateTimeSplit[j];
																			 bool=true;
																	      } catch(Exception e) {
																	    	  bool=false;
																	      }
																		}
																		
																		if( bool==true ){
																			ipdateObj.put("date",date);
																		}
																		
																		ipdateObj.put("ip",ip);
																		docArrayObj.put(ipdateObj);
																	}
																}
																
																//out.println("docArrayObjforoutside: "+docArrayObj);
																if(docArrayObj.length()>0){
																nextNodeOfTemplateInside.setProperty("documentArray",docArrayObj.toString());
																
																Calendar c = Calendar.getInstance();
																c.setTime(new Date());
																nextNodeOfTemplateInside.setProperty("lastSyncDate", c);
																session.save();
																
																String slingArrayStr=nextNodeOfTemplateInside.getProperty("documentArray").getString();
																//out.println("slingArrayStr: "+slingArrayStr);
																if( !GetAllMethodsHere.isNullString(slingArrayStr) ){
																	//out.println("slingArrayStrinside: "+slingArrayStr);
																	JSONArray docArrayObjSling=new JSONArray(slingArrayStr);
																	arrayPropObj=docArrayObjSling;
																	//rep.getWriter().println("arrayPropObj_if: "+arrayPropObj);
																}
																}
																
															} // status open check 
														} // else
														
													} // responseStrObj length check
															
													
												} // resonseObj blank check
												
											} // resonseStr blank check
											
										}else{
											SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
											 String nowDate= formatter.format(g);
											 sendInputMohitApi.put("logfilename","localhost_access_log."+nowDate + ".txt");
											 
											 String resonseStr = getDocumentTrackResponse(sendInputMohitApi.toString());
												
												String status = "";
												String hostname="";
												String dateTime="";
												
												if ( !GetAllMethodsHere.isNullString(resonseStr) ) {
													
													JSONObject resonseObj = new JSONObject(resonseStr);
													if (resonseObj.length() != 0 && resonseObj != null) {
														
														JSONObject responseStrObj = new JSONObject(resonseObj.getString("outputdata"));
														if (responseStrObj.length() != 0 && responseStrObj != null) {
															
														
															if (responseStrObj.has("status")) {
																status = responseStrObj.getString("status");
															}
															if (responseStrObj.has("hostname")) {
																 hostname = responseStrObj.getString("hostname");
															}
															
															if (responseStrObj.has("dateTime")) {
																dateTime = responseStrObj.getString("dateTime");
															}
															
														} // responseStrObj length check
																
														
													} // resonseObj blank check
													
												} // resonseStr blank check
												
												if (status.equals("notOpen")) {

													if (nextNodeOfTemplateInside.hasProperty("documentArray")) {
														String documentArrayStr = nextNodeOfTemplateInside.getProperty("documentArray").getString();
														arrayPropObj=new JSONArray(documentArrayStr);
												}
													if(arrayPropObj.length()>0){
													Calendar c = Calendar.getInstance();
													c.setTime(new Date());
													nextNodeOfTemplateInside.setProperty("lastSyncDate", c);
													session.save();
													}
													
												} else {
													if (status.equals("open")) {

														/*String hostSplit[] = hostname.split("#");
														String dateTimeSplit[] = dateTime.split("#");
														
														for(int j=0;j<hostSplit.length;j++){
															String ip=hostSplit[j];
															
															String date=dateTimeSplit[j];
															
															JSONObject ipdateObj=new JSONObject();
															ipdateObj.put("ip",ip);
															ipdateObj.put("date",date);
															arrayPropObj.put(ipdateObj);
														}*/
														
														String[] hostSplit = null;
														String dateTimeSplit[]=null;
														
														if( hostname.indexOf("#")!=-1 ){
															hostSplit = hostname.split("#");
														}
														if( dateTime.indexOf("#")!=-1 ){
															dateTimeSplit = dateTime.split("#");
														}
														
//														String hostSplit[] = hostname.split("#");
														//String dateTimeSplit[] = dateTime.split("#");
														
														//out.println("dateTimeSplit: "+dateTimeSplit);
														
														if( hostSplit!=null ){
															for(int j=0;j<hostSplit.length;j++){
																JSONObject ipdateObj=new JSONObject();
																
																String ip=hostSplit[j];
																boolean bool=false;
																String date="";
																
																if(dateTimeSplit!=null){
																try {
																	 date=dateTimeSplit[j];
																	 bool=true;
															      } catch(Exception e) {
															    	  bool=false;
															      }
																}
																
																if( bool==true ){
																	ipdateObj.put("date",date);
																}
																
																ipdateObj.put("ip",ip);
																docArrayObj.put(ipdateObj);
																
															}
														}
														
														
														if(arrayPropObj.length()>0){
														nextNodeOfTemplateInside.setProperty("documentArray",arrayPropObj.toString());
														
														Calendar c = Calendar.getInstance();
														c.setTime(new Date());
														nextNodeOfTemplateInside.setProperty("lastSyncDate", c);
														session.save();
														}
													} // status open check 
												} // else
												
										}
										
								 }
								 
							 } // list check date beetwween
							 
							 
						} // afterData check
						
						
					}
					
				}else{
					//out.println("else: ");
					if( !GetAllMethodsHere.isNullString(GenerationDate) ){
						//out.println("GenerationDateapi: "+GenerationDate);
						if(GenerationDate.lastIndexOf(".")!=-1){
							GenerationDate=GenerationDate.substring(0, GenerationDate.lastIndexOf("."));
							
							if(GenerationDate.contains("T1")){
								String beforeData=GenerationDate.substring(0,GenerationDate.indexOf("T1"));
								String afterData=GenerationDate.substring(GenerationDate.indexOf("T1")+1);
								 GenerationDate=beforeData;

							}else if(GenerationDate.contains("T2")){
								String beforeData=GenerationDate.substring(0,GenerationDate.indexOf("T2"));
								String afterData=GenerationDate.substring(GenerationDate.indexOf("T2")+1);
								 GenerationDate=beforeData;
							}
							//out.println("GenerationDateapiinal: "+GenerationDate);
						 Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(GenerationDate);
						// out.println("date1api: "+date1);
						 List<Date> s = getDaysBetweenDates(date1, new Date());
						 if(!s.isEmpty() && s!=null){
							 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
							 for (int i=0;i<s.size();i++){
								    Date g = s.get(i);
								   // out.println("gapi: "+g);
								    
									 String nextDate= formatter.format(g);
									 //out.println("nextDateapi: "+nextDate);
									 sendInputMohitApi.put("logfilename","localhost_access_log."+nextDate + ".txt");
									// out.println("sendInputMohitApi.toString(): "+sendInputMohitApi);
									 String syncResponse = getDocumentTrackResponse(sendInputMohitApi.toString());
									// out.println("syncResponseoutside: "+syncResponse);
										if ( !GetAllMethodsHere.isNullString(syncResponse) ) {
											//out.println("syncResponseinside: "+syncResponse);
											JSONObject resonseObj = new JSONObject(syncResponse);
											if (resonseObj.length() != 0 && resonseObj != null) {
												
												String status = "";
												String hostname="";
												String dateTime="";
												
												JSONObject responseStrObj = new JSONObject(resonseObj.getString("outputdata"));
												if (responseStrObj.length() != 0 && responseStrObj != null) {
													
												
													if (responseStrObj.has("status")) {
														status = responseStrObj.getString("status");
													}
													if (responseStrObj.has("hostname")) {
														 hostname = responseStrObj.getString("hostname");
													}
													
													if (responseStrObj.has("dateTime")) {
														dateTime = responseStrObj.getString("dateTime");
													}
													
													if (status.equals("notOpen")) {

														if (nextNodeOfTemplateInside.hasProperty("documentArray")) {
															String documentArrayStr = nextNodeOfTemplateInside.getProperty("documentArray").getString();
															arrayPropObj=new JSONArray(documentArrayStr);
															
													}
														if(arrayPropObj.length()>0){
														Calendar c = Calendar.getInstance();
														c.setTime(new Date());
														nextNodeOfTemplateInside.setProperty("lastSyncDate", c);
														session.save();
														}
														
													} else {
														if (status.equals("open")) {

															/*String hostSplit[] = hostname.split("#");
															String dateTimeSplit[] = dateTime.split("#");
															
															
															
															for(int j=0;j<hostSplit.length;j++){
																String ip=hostSplit[j];
																
																String date=dateTimeSplit[j];
																
																JSONObject ipdateObj=new JSONObject();
																ipdateObj.put("ip",ip);
																ipdateObj.put("date",date);
																arrayPropObj.put(ipdateObj);
															}*/
															
															String[] hostSplit = null;
															String dateTimeSplit[]=null;
															
															if( hostname.indexOf("#")!=-1 ){
																hostSplit = hostname.split("#");
															}
															if( dateTime.indexOf("#")!=-1 ){
																dateTimeSplit = dateTime.split("#");
															}
															
//															String hostSplit[] = hostname.split("#");
															//String dateTimeSplit[] = dateTime.split("#");
															
															//out.println("dateTimeSplit: "+dateTimeSplit);
															
															if( hostSplit!=null ){
																for(int j=0;j<hostSplit.length;j++){
																	JSONObject ipdateObj=new JSONObject();
																	
																	String ip=hostSplit[j];
																	boolean bool=false;
																	String date="";
																	
																	if(dateTimeSplit!=null){
																	try {
																		 date=dateTimeSplit[j];
																		 bool=true;
																      } catch(Exception e) {
																    	  bool=false;
																      }
																	}
																	
																	if( bool==true ){
																		ipdateObj.put("date",date);
																	}
																	
																	ipdateObj.put("ip",ip);
																	arrayPropObj.put(ipdateObj);
																	
																}
															}
															
															
														} // status open check 
													} // else
													
												} // responseStrObj length check
														
												
											} // resonseObj blank check
											
										} // resonseStr blank check
										
									
							 }// for else close nextDate check
							 if(arrayPropObj.length()>0){
							    nextNodeOfTemplateInside.setProperty("documentArray",arrayPropObj.toString());
								
								Calendar c = Calendar.getInstance();
								c.setTime(new Date());
								nextNodeOfTemplateInside.setProperty("lastSyncDate", c);
								session.save();
							 }
							 
						 } // list check date beetwween
						 
					}// lastindex check
						 
						 
					} // afterData check
					
				}
				
			} // documentUUId blank check
			
			
			
		} catch (Exception e) {
	      try {
			PrintWriter out=rep.getWriter();
			e.printStackTrace(out);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			//System.out.println("documentScriptCallCheck: "+e.getMessage());
		}
		return arrayPropObj;
	}
	
	public String getDocumentTrackResponse(String POST_PARAMS) {
		StringBuffer response = null;
		try {

			BypassSSlCertificate.ignoreHttps("http://bluealgo.com:8087/apirest/doctojpeg/postpdfprevTrackApi");
			
			URL obj = new URL("http://bluealgo.com:8087/apirest/doctojpeg/postpdfprevTrackApi");
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("Content-Type", "application/json");
			postConnection.setDoOutput(true);
			OutputStream os = postConnection.getOutputStream();
			os.write(POST_PARAMS.getBytes());
			os.flush();
			os.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
			String inputLine;
			response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);

			}

			in.close();

		} catch (Exception e) {
			return e.getMessage();
		}

		return response.toString();
	}
	
	public  <T> ArrayList<T> removeDuplicates(ArrayList<T> list) 
    { 
  
        // Create a new ArrayList 
        ArrayList<T> newList = new ArrayList<T>(); 
  
        // Traverse through the first list 
        for (T element : list) { 
  
            // If this element is not present in newList 
            // then add it 
            if (!newList.contains(element)) { 
  
                newList.add(element); 
            } 
        } 
  
        // return the new list 
        return newList; 
    } 

	public  String getRemainingDocumentTimeResponse(String POST_PARAMS) {
		StringBuffer response = null;
		
		try {
			BypassSSlCertificate.ignoreHttps("http://bluealgo.com:8087/apirest/doctojpeg/pretimepdfTrackApi");
			
			URL obj = new URL("http://bluealgo.com:8087/apirest/doctojpeg/pretimepdfTrackApi");
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("Content-Type", "application/json");
			postConnection.setDoOutput(true);
			OutputStream os = postConnection.getOutputStream();
			os.write(POST_PARAMS.getBytes());
			os.flush();
			os.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
			String inputLine;
			response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);

			}

			in.close();

		} catch (Exception e) {
			return e.getMessage();
		}

		return response.toString();
	}
	
	public  List<Date> getDaysBetweenDates(Date startdate, Date enddate)
	{
	    List<Date> dates = new ArrayList<Date>();
	    Calendar calendar = new GregorianCalendar();
	    calendar.setTime(startdate);

	    while (calendar.getTime().before(enddate))
	    {
	        Date result = calendar.getTime();
	        dates.add(result);
	        calendar.add(Calendar.DATE, 1);
	    }
	    return dates;
	}

}
