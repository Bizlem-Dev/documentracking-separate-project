
var Email= document.getElementById("email").value;

 $(function() {
	 getGroupName('working-group-DropdownClass');
	 getTrackedData();
	 
//	 $( "#datepicker" ).datepicker();
	 
	 $( "#datepicker" ).datepicker({
	      showOn: "button",
	      buttonImage: "/portal/apps/DocumentTrackingCSSAndJs/images/tenor.gif",
	      buttonImageOnly: true,
	      buttonText: "Select date"
	   });
	 
	 /*var includeData=location.href;
	 if( includeData.includes('/content/services/') ){
		 hitUrlMove(includeData);
		 
	 }*/
	 
	 
	 
	 
    });
 
 var group;
// var Email="nilesh@gmail.com";
 
 function getGroupName(selectclass){
	
		$.ajax({ 
		type: 'GET',
		url: '/portal/servlet/service/getGroupUI?email='+Email,
		async:false,
		success: function (dataa) {
		console.log(dataa);
		var json = JSON.parse(dataa);
		console.log("group json: "+JSON.stringify(json));
		
		var x = document.getElementsByClassName(selectclass);

		for(var i=0; i<x.length; i++){
		x[i].innerHTML="";
		
		if(json.length>0){
		for(var j=0; j<json.length; j++){
			
			if(json[j].hasOwnProperty("groupname")){
				var key =json[j].groupname;
				
				x[i].options[x[i].options.length] = new Option(key, key);
			}
		
		} // for close
		
		}else{
		x[i].options[x[i].options.length] = new Option("no group", "no group");
		}

		}
		group =document.getElementById("working-group-Dropdown-idDrop").value;
		console.log("group inside "+group);
		
		}
		});
		}
 
 
 
 function excelFileUpload(){	
	 var uploadExcel={};
 	var fileName=document.getElementById("fileUploadDocuments").value;
 	var cleanName=fileName.split('\\').pop();
// 	console.log("filename: "+fileName);
 	
 	var f=document.getElementById("fileUploadDocuments").files;
 	var isJsonValid=IsJsonString(JSON.stringify(f));
		if(isJsonValid){
			
			if (window.FileReader && window.Blob) {
			    // All the File APIs are supported.
//				console.log("f: "+JSON.stringify(f) +f.length);
			if(f.length>0){
				var reader=new FileReader();
				console.log("cleanName: "+cleanName);
			 	
			 	var str="File Name";
			 	str=str.fontcolor("black");
			 	document.getElementById("filenameId").innerHTML=str+" : "+cleanName;
				
 	reader.readAsDataURL(f[0]);
// 	console.log("f: "+f);
 	
 	reader.onloadend=function(){
// 		console.log(reader.result);
 		var filename=cleanName;
 		var filedata=reader.result;
 		
 		var fd= filedata.substr(0, filedata.indexOf(",")+1);
 		var fdata= filedata.replace(fd, "");
 			uploadExcel.Email=encodeURI(Email);
 			uploadExcel.filename=filename;
 			uploadExcel.filedata=encodeURI(fdata);
 			uploadExcel.group=group;
 			
 			var json = JSON.stringify(uploadExcel);
// 			console.log("json: "+json);

 			$.ajax({

 				url : "/portal/servlet/service/FileUploadedSaveData",
 				type : 'POST',
 				async:false,
 				data : {
 					'getUploadedFileData' : json
 				},

 				success : function(dataRes) {
 					console.log("Data Saved! " +dataRes);
 					
 					var isJsonValid=IsJsonString(dataRes);
 					if(isJsonValid){
 					
 					var jsonStr = dataRes;
 					jsonStr = JSON.parse(jsonStr);
 					
 					if(jsonStr.hasOwnProperty("status")){
 						var status=jsonStr.status;
 						var message=jsonStr.message;
 						
 						if(status=="success"){
 							
 							getTrackedData();
 							
 							 var successMsg = "Document SuccessFully Uploaded!"
  								$('#successUploadFileId').css('color','green');
  								document.getElementById("successUploadFileId").innerHTML = successMsg;
 							
 	 					} // json status success
 						else{
 							$('#successUploadFileId').css('color','red');
 							document.getElementById("successUploadFileId").innerHTML = message;
 						}
 					}
 					
 					} // is json valid check
 					
 				} // success function close
 				
 				

 			}); // function on click
			
 			
 		}// json valid check
			}// length check
		}  // blob check
		}
 }
 
 $('body').on('change','#fileUploadDocuments', function (e) {
		
	 document.getElementById("filenameId").innerHTML="";
	 document.getElementById("successUploadFileId").innerHTML ="";
	 
	 excelFileUpload();
		 
		 
  });
 
 function IsJsonString(str) {
	    try {
	        JSON.parse(str);
	    } catch (e) {
	        return false;
	    }
	    return true;
	}
 function custom_sort(a, b) {
	    return new Date(a.GenerationDate).getTime() - new Date(b.GenerationDate).getTime();
	}
 
 function getTrackedData(){
	 
	 $.ajax({

			url : "/portal/servlet/service/GetUploadedData?email="+Email,
			type : 'GET',
			async:false,
			success : function(dataRes) {
				console.log("GetUploadedData " +dataRes);
				
				var isJsonValid=IsJsonString(dataRes);
				if(isJsonValid){
				
				var jsonStr = dataRes;
				jsonStr = JSON.parse(jsonStr);
				
				if(jsonStr.hasOwnProperty("documentTrackingData")){
					var documentTrackingData=jsonStr.documentTrackingData;
					 documentTrackingData.sort(custom_sort);
					
					 var bodyDoc="";
					 
					if( documentTrackingData.length>0 ){
						for(var i=documentTrackingData.length-1;i>=0;i--){
							var insidejSonObj=documentTrackingData[i];
							
							var fileUrl="";
							var documentUploadedDate="";
							var documentName="";
							var fileExtension="";
							var noOfViewsDocument="";
							var documentStatus="";
							var lastViewsByDocument="";
							var uniqueView="";
							var owner="";
							var nodeName="";
							var urlTemp="";
							
							if(insidejSonObj.hasOwnProperty("documentName")){
								documentName=insidejSonObj.documentName;
							}if(insidejSonObj.hasOwnProperty("fileExtension")){
								fileExtension=insidejSonObj.fileExtension;
							}if(insidejSonObj.hasOwnProperty("noOfViewsDocument")){
								noOfViewsDocument=insidejSonObj.noOfViewsDocument;
							}if(insidejSonObj.hasOwnProperty("documentStatus")){
								documentStatus=insidejSonObj.documentStatus;
							}if(insidejSonObj.hasOwnProperty("lastViewsByDocument")){
								lastViewsByDocument=insidejSonObj.lastViewsByDocument;
							}if(insidejSonObj.hasOwnProperty("uniqueView")){
								uniqueView=insidejSonObj.uniqueView;
							}
							
							if(insidejSonObj.hasOwnProperty("fileUrl")){   
								fileUrl=insidejSonObj.fileUrl;
							}if(insidejSonObj.hasOwnProperty("owner")){
								owner=insidejSonObj.owner;
							}if(insidejSonObj.hasOwnProperty("nodeName")){
								nodeName=insidejSonObj.nodeName;
							}if(insidejSonObj.hasOwnProperty("urlTemp")){
								urlTemp=insidejSonObj.urlTemp;
							}
							
							if(insidejSonObj.hasOwnProperty("documentUploadedDate")){
								documentUploadedDate=insidejSonObj.documentUploadedDate;
								
								if(documentUploadedDate.lastIndexOf(".")!=-1){
									documentUploadedDate=documentUploadedDate.substring(0, documentUploadedDate.lastIndexOf("."));
									
									if(documentUploadedDate.includes("T1")){
										var beforeData=documentUploadedDate.substring(0,documentUploadedDate.indexOf("T1"));
										var afterData=documentUploadedDate.substring(documentUploadedDate.indexOf("T1")+1);
										
										documentUploadedDate=beforeData+" "+afterData;
									}
									
								}
								
							} //documentUploadedDate check
							
							bodyDoc=bodyDoc+'<tr><td>'+documentName+'</td><td>'+lastViewsByDocument+'</td><td>'+noOfViewsDocument+'</td><td>'+documentUploadedDate+'</td><td>'+owner+'</td><td class="action"><a class="icon-a" onclick="onclickleftslide(this);" nodeName="'+nodeName+'"><i class="fa fa-link link" aria-hidden="true"></i></a><div class="dropdown"><a class="icon-a drop-box dropdown-toggle" data-toggle="dropdown"><i class="fa fa-ellipsis-v" aria-hidden="true"></i></a><ul class="dropdown-menu"><li><a href="javascript:void(0)" class="copyLinkData" onclick="copyToClipBoard(this)" id="copyLinkData'+i+'" file-url="'+urlTemp+'"><i class="fa fa-clone" aria-hidden="true"></i> Copy to clipboard</a></li><li><a href="javascript:void(0)" class="sweet-success-cancel deleteFilenameNode" onclick="deleteDocumentNameNode(this)" id="deleteFilenameNode'+i+'" file-name="'+documentName+'"><i class="fa fa-trash-o" aria-hidden="true"></i> Delete</a></li></ul></div></td></tr>';
//							bodyDoc=bodyDoc+'<tr><td>'+documentName+'</td><td>'+lastViewsByDocument+'</td><td>'+noOfViewsDocument+'</td><td>'+documentUploadedDate+'</td><td>'+owner+'</td><td class="action"><a class="icon-a" onclick="onclickleftslide(this);" nodeName="'+nodeName+'"><i class="fa fa-link link" aria-hidden="true"></i></a><div class="dropdown"><a class="icon-a drop-box dropdown-toggle" data-toggle="dropdown"><i class="fa fa-ellipsis-v" aria-hidden="true"></i></a><ul class="dropdown-menu"><li><a href="javascript:void(0)" class="copyLinkData" onclick="copyToClipBoard(this)" id="copyLinkData'+i+'" file-url="'+fileUrl+'"><i class="fa fa-clone" aria-hidden="true"></i> Copy to clipboard</a></li><li><a href="javascript:void(0)" class="sweet-success-cancel deleteFilenameNode" onclick="deleteDocumentNameNode(this)" id="deleteFilenameNode'+i+'" file-name="'+documentName+'"><i class="fa fa-trash-o" aria-hidden="true"></i> Delete</a></li></ul></div></td></tr>';
							
						} // for close
						
						document.getElementById('documentTrackInId').innerHTML=bodyDoc;
						
					} // documentTrackingData length check
					else{
						bodyDoc='<p align="center">No Data Available</p>';
						document.getElementById('documentTrackInId').innerHTML=bodyDoc;
					}
					
				}
				
				} // is json valid check
				else{
					document.getElementById('documentTrackInId').innerHTML='<p align="center">No Data Available</p>';
				}
			} // success function close

		}); // function on click
	 
 }
 
 function copyToClipBoard(param){
	 
	 var $this = $(param);
	 var copyTextStr = $this.attr("file-url");
		  
		  const el = document.createElement('textarea');
		  el.value = copyTextStr;
		  document.body.appendChild(el);
		  el.select();
		  document.execCommand('copy');
		  document.body.removeChild(el);
 }
 
 /*function deleteDocumentNameNode(param){

		var $this = $(param);
		var fileName = $this.attr("file-name");
		console.log("fileName_delete: "+fileName);
		}*/
 
 function deleteDocumentNameNode(param) {
	 
	 var $this = $(param);
		var fileName = $this.attr("file-name");
		console.log("fileName_delete: "+fileName);
	 
	    swal({
	        title: "Are you sure?",
	        text: "You will not be able to recover this Document!",
	        type: "warning",
	        showCancelButton: true,
	        confirmButtonColor: "#DD6B55",
	        confirmButtonText: "Yes, delete it!",
	        cancelButtonText:"No, cancel it !!",
	        closeOnConfirm: false,
//	        timer: 3000,
	        closeOnCancel:!1
	    }, function (isConfirm) {
//	        if (!isConfirm) return;
	    	if(isConfirm){
	    		 $.ajax({
	 	            url: "/portal/servlet/service/DeleteDocument?email="+Email+"&group="+group+"&fileName="+fileName,
	 	            type: "GET",
	 	            dataType: "html",
	 	            success: function (data) {
	 	            	
	 	            	if( !isEmpty(data) ){
	 	            		swal("Done!", "Document was succesfully deleted!", "success");
	 	            		
	 	            		/* window.setTimeout(function(){ } ,3000);
	                         location.reload();*/
	 	            		getTrackedData();
	 	            		
	 	            		
	 	            	}
	 	                
	 	            },
	 	            error: function (xhr, ajaxOptions, thrownError) {
	 	                swal("Error deleting!", "Please try again", "error");
	 	            }
	 	        });
	    	}else{
//	    		if (!isConfirm) return;
	    		swal("Cancelled !!","your Document is safe !!","error");
                $('.dropdown-menu-box').hide();
	    	}
	       
	    });
	}
 
 function isEmpty(str) {
	    return (!str || 0 === str.length);
	}

 $(".removeData").click(function(){
	 document.getElementById("filenameId").innerHTML="";
	 document.getElementById("successUploadFileId").innerHTML ="";
	});
 $(".closeRemoveData").click(function(){
	 document.getElementById("filenameId").innerHTML="";
	 document.getElementById("successUploadFileId").innerHTML ="";
	});
 
 
 function onclickleftslide(param){
	 $(".left-section-div").show();
	 
	 var $this = $(param);
	 var nodeName = $this.attr("nodeName");
	 console.log("nodeNameoutside: "+nodeName);
	 
	 $(".create-link").click(function(){
		 setAlertAnyOptionIfNeeded(param);
	 });
	 
 }
 
 function setAlertAnyOptionIfNeeded(param){
	
	 var $this = $(param);
	 var nodeName = $this.attr("nodeName");
	 console.log("nodeNameinside: "+nodeName);
	 
	 var jsonData={};
	 jsonData.Email=Email;
	 jsonData.group=group;
	 jsonData.nodeName=nodeName;
	 
	 var SED = document.getElementById("SED");
	 if(SED.checked){
		 
		 $("#datepick").show();
		 
		 var datepicker = document.getElementById("datepicker");
		 console.log("datepicker: "+datepicker.value);
		 
		 jsonData.expiryDate="expiryDate";
		 jsonData.date=datepicker.value;
		 
		 checkData(jsonData);
		 
	 }else{
		 
		 var SU = document.getElementById("SU");
		 if(SU.checked){
			 
			 console.log("SU: "+"Checked");
			 jsonData.singleUse="singleUse";
			 checkData(jsonData);
			 
		 }else{
			 $("#datepick").hide();
		 }
		 
	 }
	 
	
 }
 
 $("#SED").click(function(){
	 var SED = document.getElementById("SED");
	 if(SED.checked){
		 $("#datepick").show();
	 }else{
		 $("#datepick").hide();
	 }
	});
 
 function checkData(jsonData){
	 
	 $.ajax({
		
		 type: "POST",
	        url: "/portal/servlet/service/CheckExpiryAndSingleUseSaveData",
	        async: false,
	        data:JSON.stringify(jsonData),
	        contentType: "application/json",
	        success: function (data) {
	        	console.log("data: "+data);
	        }
		 
	 });
	 
 }
 
 
 
 /*$(".sweet-success-cancel").click(function(e){
     var obj = $(this);
     e.preventDefault();
     swal({
         title:"Are you sure to delete ?",
         text:"You will not be able to recover this imaginary file !!",
         type:"warning",
         showCancelButton:!0,
         confirmButtonColor:"#DD6B55",
         confirmButtonText:"Yes, delete it !!",
         cancelButtonText:"No, cancel it !!",
         closeOnConfirm:!1,
         closeOnCancel:!1
     },
     function(e){
         if (e){
                obj.parents('tr').remove();
                 
                 swal("Deleted !!","Hey, your imaginary file has been deleted !!","success");
             } else {
                 swal("Cancelled !!","Hey, your imaginary file is safe !!","error");
                 $('.dropdown-menu-box').hide();
             }
     });
 });*/
 