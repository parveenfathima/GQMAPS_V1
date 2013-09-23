var arrEntp = new Array(enterprise()); // global array to store all the enterprises from the rest service
var gArrayIndex = ""; // global enterprise id.
var gRegStatus = "";
var gMeterId = 0;
var gUserId = 0;
var arrIndex;

$.jStorage.set("jsLat", "0.0"); 
$.jStorage.set("jsLong", "0.0");

$(document).ready(function() 
{
	if($.jStorage.get("jsUserId") === "" || $.jStorage.get("jsUserId") === null || $.jStorage.get("jsPwd") === "" || $.jStorage.get("jsPwd") === null)
	{
		window.location.href = "login.html";
	}
	else
	{
		$("#datepicker").keypress( function(e) {
		  
		// if the letter is not digit then display error and don't type anything
		 if (e.which != 8 && e.which != 0 && (e.which >= 48 || e.which <= 57)) 
		 {
			//display error message
			alert("Enter numbers only");
			$("#txtPhone").focus();
			return false;
		 }
		});
		  
		  
		$("#txtPhone, #txtESqft, #txtEAsset, #txtDCSqft, #txtDCAsset, #txtDCUsed, #txtDCTemp").keypress(function (e) {
		// if the letter is not digit then display error and don't type anything
		 if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) 
		 {
			//display error message
			alert("Please enter numbers only");
			return false;
		 }
	   }); 
		
		listEnterprise();
		
		$( "#txtNewExpiry").bind("focusin", showDate);
		
		$("#txtPhone").keypress(function (e) 
		{
		// if the letter is not digit then display error and don't type anything
		
		 if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) 
		 {
			//display error message
			alert("Please enter numbers only");
			$("#txtPhone").focus();
			return false;
		 }
	   }); 
	   
	   $( "#btnCancel").bind("click", cancelEdit);
	}
	
});

function cancelEdit()
{
	$( "#dlgGeneral" ).dialog( "close" );
}

// opening the general dialog with the selected enterprise's basic information to edit by the admin user. 
function openGeneralDialog(i)
{
	gArrayIndex = i;

	$( "#dlgGeneral" ).dialog( "open" );
	$("#hBasic").text("General details of " + arrEntp[i].getEName());
	
	$("#txtEID").val(arrEntp[i].getEId());
	$("#txtEName").val(arrEntp[i].getEName());
	$("#txtUID").val(arrEntp[i].getUId());
	$("#txtPwd").val(arrEntp[i].getPwd());
	$("#cmbSaveFwd").val(arrEntp[i].getOutput());
	$("#cmbEmpCount").val(arrEntp[i].getEmpCount());
	$("#txtUrl").val(arrEntp[i].getUrl());
	$("#txtESqft").val(arrEntp[i].getESqft());
	$("#txtEAsset").val(arrEntp[i].getEAsset());
	$("#txtDCSqft").val(arrEntp[i].getDSqft());
	$("#txtDCAsset").val(arrEntp[i].getDAsset());
	$("#txtDCUsed").val(arrEntp[i].getDcUsed());
	$("#txtDCTemp").val(arrEntp[i].getDcTemp());

	$("#taComments").val(arrEntp[i].getComments());

	//if no meter, then we can edit the enterprise id. otherwise, no
	var mCount = hasMeter($("#txtEID").val());
	
	if(mCount > 0)
	{
		$("#txtEID").attr("disabled", "disabled");
	}
	/*else if(!checkSpecialCharWithSpace(arrEntp[i].getEId()) && !checkForNoInString(arrEntp[i].getEId()))
	
	{
		$("#txtEID").attr('disabled', 'disabled');
	}*/
	else
	{
		$(txtEID).removeAttr("disabled");
	}
	
	var isRegistered = $.trim(arrEntp[i].getRegStatus());
	
	if(isRegistered === 'y')
	{
		$('input:checkbox[name=chkRegCompl]').attr('checked',true);
		$("#chkRegCompl").attr("disabled", "disabled");		
	}
	else
	{
		$('input:checkbox[name=chkRegCompl]').attr('checked',false);
		$("#chkRegCompl").removeAttr("disabled");
	}

	$("#txtEID").focus();	
}

// saving the enterprise general information dialog
function saveGeneral()
{
	updateEntp(arrEntp[gArrayIndex]);   
}

// updating only the changed enterprise
function updateEntp(dbEntp)
{		
	var eid = $.trim($("#txtEID").val());
	var ename = $.trim($("#txtEName").val());
	var uid = $.trim($("#txtUID").val());
	var pwd = $.trim($("#txtPwd").val());
	var output = $.trim($("#cmbSaveFwd").val());

	var empCount = $.trim($("#cmbEmpCount").val());	
	
	var url = $.trim($("#txtUrl").val());
	var eSqft = $.trim($("#txtESqft").val());
	var eAsset = $.trim($("#txtEAsset").val());
	var dSqft = $.trim($("#txtDCSqft").val());
	var dAsset = $.trim($("#txtDCAsset").val());
	var dcUsed = $.trim($("#txtDCUsed").val());
	var dcTemp = $.trim($("#txtDCTemp").val());
	var comments = $.trim($("#taComments").val());

	var isRegistered = $('#chkRegCompl').is(':checked');
	//var isActive = $('#chkActive').is(':checked');
		
	var regStatus = "n";
	//var active = "n";
	
	if(isRegistered)
		regStatus  = "y";

	var formEntp = new enterprise(dbEntp.getSId(), eid, ename, uid, pwd, output, url, empCount, eSqft, eAsset, dSqft, dAsset, dcUsed, dcTemp, comments, regStatus, dbEntp.getMCount(), dbEntp.getExpDate());
                                            //sid, eid, ename, uid, pwd, output, url, empCount, eSqft, eAsset, dSqft, dAsset, dcUsed, dcTemp,  comments, regStatus, mcount, expDate)  
											
 	var mCount = hasMeter(eid); //check whether the enterprise has got meter or not to complete the registration
	
	if(mCount === 0 && regStatus === "y")
	{
		alert("The enterprise has " + mCount + " meter(s), add at least one meter to complete the registration");
		$(txtEID).removeAttr("disabled");
		$('input:checkbox[name=chkRegCompl]').attr('checked',false);	
		return false;		
	}
	else if(formEntp.getEId().length === 0 || formEntp.getUId().length === 0 || formEntp.getPwd().length === 0 || formEntp.getEName().length === 0)
	{
		alert("Please enter all the mandatory values");
		$("#txtEID").focus();
		return false;
	}
	else if(compareObject(dbEntp, formEntp))
	{
		alert("No changes found");
		$( "#dlgGeneral" ).dialog( "close" );
	}
	else if($('#txtEID').is(':disabled') === false && !validateEntpID(dbEntp.getEId(), formEntp.getEId())) 
	{ 
		$("#txtEID").select();
		return false;		
	}
	else if(checkSpecialChar(formEntp.getEName()))
	{
		alert("Please enter Enterprise Name without special characters");	
		$('#txtEName').select();
		return false;			
	}	
	else if(checkSpecialCharWithSpace(formEntp.getUId()))
	{
		alert("Please enter User ID without special characters/space");	
		$("#txtUID").select();
		return false;		
	}
	else if(!validatePwd(formEntp.getPwd()))
	{
		$("#txtPwd").select();
		return false;
	}	
	else if(eSqft.toString().length > 9)
	{
		alert("Please enter valid IT usable area");
		$('#txtESqft').select();
		return false;
	}
	else if(eAsset.toString().length > 9)
	{
		alert("Please enter valid total number of IT assets");
		$('#txtEAsset').select();
		return false;
	}
	else if(dSqft.toString().length > 9)
	{
		alert("Please enter valid datacenter usable area");
		$('#txtDCSqft').select();
		return false;
	}	
	else if(dAsset.toString().length > 9)
	{
		alert("Please enter valid datacenter assets");
		$('#txtDCAsset').select();
		return false;
	}	
	else if(dcUsed  > 100)
	{
		alert("Please enter valid datacenter occupancy rate");
		$('#txtDCUsed').select();
		return false;
	}				
	else if(dcTemp > 99)
	{
		alert("Please enter valid datacenter temperature");
		$('#txtDCTemp').focus();
		return false;
	}
	else
	{
		var vType = "PUT";
		var vUrl = $.jStorage.get("jsUrl") + "enterprise/updateRegistration";
		
		if(formEntp.getUrl() === "")	formEntp.setUrl(" ");
		if(formEntp.getESqft()  === "") formEntp.setESqft("0");
		if(formEntp.getEAsset()  === "")	formEntp.setEAsset("0");
		if(formEntp.getDSqft() === "") formEntp.setDSqft("0");
		if(formEntp.getDAsset()  === "") formEntp.setDAsset("0");
		if(formEntp.getDcUsed()   === "") formEntp.setDcUsed("0");
		if(formEntp.getDcTemp() === "") formEntp.setDcTemp("0");	

		var vQuery = "";
		
		vQuery = vQuery + '{"sid":"' + dbEntp.getSId() + '", "enterpriseId":"' + formEntp.getEId() + '", "userId":"' + formEntp.getUId() + '", "passwd":"' ;
		vQuery = vQuery + 	formEntp.getPwd() + '", "noOfEmpl" : "' + formEntp.getEmpCount(); 
		vQuery = vQuery + '", "entSqft":"' + formEntp.getESqft();
		vQuery = vQuery + '", "entAssetCount":"'+ formEntp.getEAsset() + '", "dcSqft": "' + formEntp.getDSqft() + '", "dcAssetCount":"'+ formEntp.getDAsset();
		vQuery = vQuery + '", "dcUsePctg":"'+ formEntp.getDcUsed() + '", "dcTemp":"'+ formEntp.getDcTemp()+ '", "regCmplt":"' + formEntp.getRegStatus() + '"}';	
		
		var msg = "";
		
		$.ajax
		({
			type:vType,
			contentType: "application/json",
			url:vUrl,
			async:false,
			data:vQuery,
			dataType: "json",
			success:function(json) 
			{				
			
				//sending activation email
				msg = "Updated Successfully";
				
				var url = $.jStorage.get("jsUrl") + "general/activationEmail?sId=" + dbEntp.getSId();	
				var type = "GET";				
				
				if(dbEntp.getRegStatus() === "n" && formEntp.getRegStatus() === "y")
				{
						$.ajax
						({
							type:type,
							contentType: "application/json",
							url:url,
							async:false,
							dataType: "json",
							success:function(json) 
							{											
								msg = "Updated successfully and an email has been sent to the enterprise with the username and password to login GQMaps!";
							},
							error:function(json)
							{
								msg = "Updated successfully but an error occured while sending the email to the client";
								alert("Error from sending activation email: " + json.status + " " + json.responseText);
							}
						});
				}
				// end of activation email
				alert(msg);
				$( "#dlgGeneral" ).dialog( "close" );
				window.location.href = "edit_registration.html";
				gArrayIndex = "";
			},
			error:function(json)
			{
				alert("Error from updating enterprise details: " + json.status + " " + json.responseText);
			}
		});	
	}
}

//function to validate the enterprise id while editing the general information which needs to be unique
function validateEntpID(dbEId, eid)
{
		if(eid.length > 10)
		{
			alert("Please enter the Enterprise ID with the maximum length of 10 characters");
			$("#txtEID").select();
			return false;
		}
		else if(checkSpecialCharWithSpace(eid) || checkForNoInString(eid))
		{
			alert("Please enter Enterprise ID without special characters/numbers/space");	
			$("#txtEID").select();
			return false;			
		}
		else if(dbEId != eid && !checkUniqueEntpID(eid))
		{
			alert("Please enter unique Enterprise ID");
			$("#txtEID").select();
			return false;
		}	
		else
			return true;
}


// compare the enterprise values before and after the change. If changed, then only the ajax will be called to update.
function compareObject(obj1, obj2)
{
	for(var p in obj1)
	{
		if(obj1[p] !== obj2[p])
		{
			return false;
		}
	}
	
	for(var p in obj2)
	{
		if(obj1[p] !== obj2[p]){
			return false;
		}
	}
	
	return true;
}

// binding the datepicker with the field
function showDate()
{
	$("#txtNewExpiry").datepicker({ dateFormat: 'yy-mm-dd' });
}

// opening the meter dialog for the selected enterprise to add the meter details. 
function openMeterDialog(index)
{
	gArrayIndex = index;
	
	clearMeterDetails();
	
	$( "#dlgMeter" ).dialog( "open" );

	//loading the protocols from db in the dropdown in general.js
	loadProtocol();
	$("#tblMeterList").val("");
	loadMeters(index);
}

//function to save the newly added meter details
function saveMeter()
{
	addEntpMeter(arrEntp[gArrayIndex]);
}

function clearMeterDetails()
{
	$("#txtMeterID").val("");
	$("#cmbProtocol").val("");
	$("#txtPhone").val("");
	$.trim($("#taAddress").val(""));
	$.trim($("#txtDesc").val(""));
}

function addEntpMeter(dbEntp)
{
	var vMId = $("#txtMeterID").val();
	var vProtocol = $("#cmbProtocol").val();
	var vEId = dbEntp.getEId();
	var vPhone = $("#txtPhone").val();
	var vAddress = $.trim($("#taAddress").val());
	var vDesc = $.trim($("#txtDesc").val());
	
	if(vDesc.length === 0 || vDesc === "")
	{
		vDesc = null;
	}
	
	var vDtTime = getDtTime();
	
	if(vMId.length === 0 || vMId == "" || vPhone.length === 0 || vPhone === "" || vAddress.length === 0 || vAddress === "")
	{
		alert("Please enter mandatory values to submit the details");
		return false;
	}
	else if(vPhone.length < 10 || vPhone.length  > 15)
	{
		alert("Please enter a valid mobile number");	
		$("#txtPhone").select();
		return false;		
	}
	else if(checkSpecialCharWithSpace(vMId))
	{
		alert("Please enter Meter ID without special characters/space");	
		$("#txtMeterID").select();
		return false;		
	}
	else if($("#cmbProtocol").val() === "" )
	{
		alert("Select protocol");
		$("#cmbProtocol").focus();
	}	
	else 
	{	
		getGeoLocation(vAddress);	
		
		validateMeterId(vMId, vEId);
		
		if(gMeterId === 1)
		{
			alert("Enter unique meter ID!");
			$("#txtMeterID").select();
			gMeterId = 0;
			return false;
		}
	
		var vType = "POST";						
		var vUrl = $.jStorage.get("jsUrl") + "enterpriseMeters/addEntMeters?entpId=" + vEId;
				
		var vQuery = "";
		vQuery = vQuery + '{"meterId":"' + vMId + '","protocolId":"' + vProtocol + '", "enterpriseId":"' + vEId + '", "descr":"' + vDesc + '", "address":"' + vAddress;
		vQuery = vQuery + '","phone":"' + vPhone + '", "creDttm":"' + vDtTime + '", "latitude":"' + $.jStorage.get("jsLat");
		vQuery = vQuery + '", "longitude":"' + $.jStorage.get("jsLong")  + '"}';																		
		
		$.ajax
		({
			type:vType,
			contentType: "application/json",
			url:vUrl,
			async:false,
			data:vQuery,
			dataType: "json",
			success:function(json) 
			{
				gMeterId = 0;	
				$.jStorage.set("jsLat", "0.0"); 
				$.jStorage.set("jsLong", "0.0");			
				
				alert("Meter details saved successfully!");
				clearMeterDetails();
				window.location.href = "edit_registration.html";
				gArrayIndex = "";
			},
			error:function(json)
			{
				resetVariables();	
				clearMeterDetails();
				alert("Error from adding meter details: " + json.status + " " + json.responseText);
				
			}
		});	
	}
}

// opening the meter dialog for the selected enterprise to add the validity details. 
function openValidityDialog(index)
{
	gArrayIndex = index;
	$( "#dlgValidity" ).dialog( "open" );
	$("#hValidity").text("Add validity details for " + arrEntp[gArrayIndex].getEName());	
}

// saving the gatekeeper and gatekeeper audit values
function saveValidity()
{
	updateGateKpr(arrEntp[gArrayIndex]);
}

function updateGateKpr(dbEntp)
{
	
	var dateObject = $("#datepicker").datepicker("expDate");
		
	var vExpDate = $.trim($("#txtNewExpiry").val());
	
	var vComments = $.trim($("#taValComments").val());
	
	var now = new Date(); 
	var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
	
	var date = vExpDate.split("-");
	var dtStr = date[1] + "/" + date[0] + "/" + date[2];
		
	var vEDate = new Date(dtStr);
	
	var vEId = dbEntp.getEId();
	var mCount = hasMeter(vEId); //check whether the enterprise has got meter or not to update the expiry date
	
	if(mCount === 0)
	{
		alert("The enterprise has " + mCount + " meter(s), add at least one meter details to update the meter expiry date");
		$('#txtNewExpiry').val("");
		$('#taValComments').val("");
		$( "#dlgValidity" ).dialog( "close" );
		return false;		
	}
	else if (vExpDate === "" || vExpDate.length === 0)
	{
		alert("Select expiry date");
		$('#txtNewExpiry').blur(); 	
		return false;
	}
	else if(vEDate < today)
	{
		alert("Enter a valid date which should be greater than the current date");
		$('#txtNewExpiry').val("");
		$('#txtNewExpiry').focus();
		return false;
	}
	else if(vComments === "" || vComments.length === 0)
	{
		alert("Enter comments");
		$("#taValComments").focus();
		return false;
	}
	else
	{
		var vType = "POST";
		var vUrl = $.jStorage.get("jsUrl") + "gatekeeper/addEntAudit";						

		var date = vExpDate.split("-");
		var vEDt = new Date(date[2], date[1] - 1, date[0]);
		
		var dt = new Date();
		var vCDtTime = dt.getFullYear() + "-" + convertToTwoDigit(dt.getMonth()+1) + "-" + convertToTwoDigit(dt.getDate()) + " " + convertToTwoDigit(dt.getHours()) + ":" 
										 + convertToTwoDigit(dt.getMinutes()) + ":" + convertToTwoDigit(dt.getSeconds());
	
		var vExpDt = vEDt.getFullYear() + "-" + convertToTwoDigit(vEDt.getMonth()+1) + "-" + convertToTwoDigit(vEDt.getDate()) + " " + convertToTwoDigit(dt.getHours()) + ":" 
										 + convertToTwoDigit(dt.getMinutes()) + ":" + convertToTwoDigit(dt.getSeconds());
		
		var vQuery = '{"enterpriseId" : "' + dbEntp.getEId() + '", "comment" : "' + vComments + '", "expDttm" : "' + vExpDt +'","creDttm" : "' + vCDtTime + '"}';																					
			
		$.ajax
		({
			
			type:vType,
			contentType: "application/json",
			url:vUrl,
			async:false,
			data:vQuery,
			dataType: "json",
			success:function(json) 
			{
				alert("Validity details saved successfully!");
				window.location.href = "edit_registration.html";
				gArrayIndex = "";
				
			},
			error:function(json)
			{
				alert("Error from adding gate keeper/audit details: " + json.status + " " + json.responseText);
			}
		});	

	}
		
}

//General form dialog configuration
$(function() {
	  $( "#dlgGeneral" ).dialog({
		  autoOpen: false,
		  height: 475,
		  width: 500,
		  modal: true,
		  position: "center"
	  });		  
});

//Meter form dialog configuration
$(function() {
	  $( "#dlgMeter" ).dialog({
		  autoOpen: false,
		  height: 465,
		  width: 300,
		  modal: true,
		  position: "center"
	  });		  
});

//Validity form dialog configuration
$(function() {
	  $( "#dlgValidity" ).dialog({
		  autoOpen: false,
		  height: 365,
		  width: 300,
		  modal: true,
		  position: "center"
	  });
});

// dynamic grid listing all the enterprises
function listEnterprise()
{			
		var vUrl = $.jStorage.get("jsUrl") + "general/getEntpSummaryList";
		
		$.ajax({
			type : "GET",
			beforeSend: function(xhr) 
			{ 
			  xhr.setRequestHeader("Content-Type", "application/json");
			},			
			url : vUrl,
			dataType : "json",
			success : function(json) 
			{			
				var vRecLen = json.length;
				
				if(vRecLen != 0) // listing the enterprise
				{					
						var vEntpList = "";	
						var vEId = "";	

						$.each(json, function(i,n)
						{				
									
						
							arrEntp[i] = new enterprise($.trim(n["sid"]), $.trim(n["enterpriseId"]), $.trim(n["eName"]), $.trim(n["userId"]), $.trim(n["passwd"]), $.trim(n["storeFwd"]), $.trim(n["fwdUrl"]), $.trim(n["noOfEmpl"]), $.trim(n["entSqft"]), $.trim(n["entAssetCount"]), $.trim(n["dcSqft"]), $.trim(n["dcAssetCount"]), $.trim(n["dcUsePctg"]), $.trim(n["dcTemp"]), $.trim(n["comments"]), $.trim(n["regCmplt"]), $.trim(n["mCount"]), n["expDttm"]);	
							
							if(arrEntp[i].getRegStatus() === 'n')
								vEntpList += '<tr style="height:25px; color: #FF0000">';
							else
								vEntpList += '<tr>';
								
								
								vEntpList += '<td> <input type = "button" class = "button-font-a" id = "btnGeneral" title = "General" onClick = "openGeneralDialog(' + i + ')"/></td>';
				
								
								//check for a valid enterprise id to add meter and validity details
								if(checkSpecialCharWithSpace(arrEntp[i].getEId()) || checkForNoInString(arrEntp[i].getEId()))
								{
									vEntpList += '<td> <input type = "button" class = "button-font-a" id = "btnMeter" title = "Edit Enterprise ID to add meters" onClick = "openMeterDialog(' + i + ')" disabled = "disbaled" /></td>';
									vEntpList += '<td> <input type = "button" class = "button-font-a" id = "btnValidity" title = "Edit Enterprise ID to add validity details" onClick = "openValidityDialog(' + i + ')"  disabled = "disbaled" /></td>';  									
								}	
								else
								{
									vEntpList += '<td> <input type = "button" class = "button-font-a" id = "btnMeter" title = "Add Meter" onClick = "openMeterDialog(' + i + ')"/></td>';
									vEntpList += '<td> <input type = "button" class = "button-font-a" id = "btnValidity" title = "Add Validity" onClick = "openValidityDialog(' + i + ')"/></td>';  									
								}
                                          
								vEntpList += '<td width="100" style = "padding-left: 34px;" class = "gen-form-font">' +  arrEntp[i].getEId() + '</td>';
								vEntpList += '<td width="100" style = "padding-left: 40px;" class = "gen-form-font">' +  arrEntp[i].getEName() + '</td>';
								vEntpList += '<td width="100"> </td>';
								vEntpList += '<td width="80"  style = "padding-right: 20px;" class = "gen-form-font">' +  arrEntp[i].getMCount()  + '</td>';
								vEntpList += '<td width="160" class = "gen-form-font">' +  arrEntp[i].getExpDate() + '</td>';
								vEntpList += '</tr>';					
																				
						});	
						
						$("#tblEList").append(vEntpList);		
									
				} // end of if(vRecLen != 0 && vUserID != "admin")
				else
				{
						alert("No records found");	
						$.jStorage.set("jsUserId", "");	
						window.location.href = "login.html";	
				} // end of if(vRecLen != 0)
				
			}, //end of success
			error : function(json) 
			{
				alert("Error from listing enterprise details: " + json.status + " " + json.responseText);
			}
		});			  		
}

//-------------------------------------------------Enterprise object---------------------------------------------------

// enterprise object's constructor, get and set methods		
function enterprise(sid, eid, ename, uid, pwd, output, url, empCount, eSqft, eAsset, dSqft, dAsset, dcUsed, dcTemp,  comments, regStatus, mcount, expDate)   
{
	this.sid = sid;
	this.eid = eid;
	this.ename = ename;
	this.uid = uid;
	this.pwd = pwd;
	this.output = output;
	this.empCount = empCount
	this.url = url;
	this.eSqft = eSqft;
	this.eAsset = eAsset;
	this.dSqft = dSqft;
	this.dAsset = dAsset;
	this.dcUsed = dcUsed;
	this.dcTemp = dcTemp;
	this.comments = comments;
	//this.active = active;
	this.regStatus = regStatus;
	this.mcount = mcount;
	this.expDate = expDate;
}

enterprise.prototype.getSId = function()
{
	return this.sid;	
}

enterprise.prototype.setSId = function(sid)
{
	this.sid = sid;
}

enterprise.prototype.getEId = function()
{
	return this.eid;	
}

enterprise.prototype.setEId = function(eid)
{
	this.eid = eid;
}

enterprise.prototype.getEName = function()
{
	return this.ename;
}

enterprise.prototype.setEName = function(ename)
{
	this.ename = ename;
}

enterprise.prototype.getUId = function()
{
	return this.uid;
}

enterprise.prototype.setUId = function(uid)
{
	this.uid = uid;
}

enterprise.prototype.getPwd = function()
{
	return this.pwd;
}

enterprise.prototype.setPwd = function(pwd)
{
	this.pwd = pwd;
}

enterprise.prototype.getOutput = function()
{
	return this.output;
}

enterprise.prototype.setOutput = function(output)
{
	this.output = output;
}

enterprise.prototype.getUrl = function()
{
	return this.url;
}

enterprise.prototype.setUrl = function(url)
{
	this.url = url;
}

enterprise.prototype.getEmpCount = function()
{
	return this.empCount;
}

enterprise.prototype.setEmpCount = function(empCount)
{
	this.empCount = empCount;
}



enterprise.prototype.getESqft = function()
{
	return this.eSqft;
}

enterprise.prototype.setESqft = function(eSqft)
{
	this.eSqft = eSqft;
}

enterprise.prototype.getEAsset = function()
{
	return this.eAsset;
}

enterprise.prototype.setEAsset = function(eAsset)
{
	this.eAsset = eAsset;
}

enterprise.prototype.getDSqft = function()
{
	return this.dSqft;
}

enterprise.prototype.setDSqft = function(dSqft)
{
	this.dSqft = dSqft;
}

enterprise.prototype.getDAsset = function()
{
	return this.dAsset;
}

enterprise.prototype.setDAsset = function(dAsset)
{
	this.dAsset = dAsset;
}

enterprise.prototype.getDcUsed = function()
{
	return this.dcUsed;
}

enterprise.prototype.setDcUsed = function(dcUsed)
{
	this.dcUsed = dcUsed;
}

enterprise.prototype.getDcTemp = function()
{
	return this.dcTemp;
}

enterprise.prototype.setDcTemp = function(dcTemp)
{
	this.dcTemp = dcTemp;
}

enterprise.prototype.getComments = function()
{
	return this.comments;
}

enterprise.prototype.setComments = function(comments)
{
	this.comments = comments;
}

enterprise.prototype.getRegStatus = function()
{
	return this.regStatus;
}

enterprise.prototype.setRegStatus = function(url)
{
	this.url = regStatus;
}

enterprise.prototype.getMCount = function()
{
	return this.mcount;
}

enterprise.prototype.setMCount = function(mcount)
{
	this.mcount = mcount;
}

enterprise.prototype.getExpDate = function()
{
	return this.expDate;
}

enterprise.prototype.setExpDate = function(expDate)
{
	
	this.expDate = expDate;
}

enterprise.prototype.toString = function()  
{

	return(this.eid + ", " + this.ename + ", " + this.uid + ", " + this.pwd + ", " + this.output + ", " + this.url + ", " + this.empCount + ", "+ this.eSqft + ", " + this.eAsset + ", " + this.dSqft + ", " + this.dAsset + ", " + this.dcUsed + ", " + this.dcTemp + ", " + this.comments + ", " + this.regStatus + ", " + this.mcount + ", " + this.expDate);
} // end of subject object	


//-------------------------------------------------/Enterprise object---------------------------------------------------


function convertToTwoDigit(no)
{
	return (no >=0 && no <10 ? ("0"+ no) : no)
}

//check for unique meter id 
function validateMeterId(meterId, eid)
{
	  var vType = "GET";
	  var vUrl = $.jStorage.get("jsUrl") + "enterpriseMeters/getAllMeters";	
	  
	  $.ajax
	  ({
		  type:vType,
		  contentType: "application/json",
		  url:vUrl,
		  async:false,
		  dataType: "json",
		  success:function(json)
		  {				
				$.each(json, function(i,n)
				{
					if(n["meterId"] === meterId)
					{
						gMeterId = 1;
					}
				});	
				
		  },
		  error:function(json)
		  {
			  alert("Error while validating unique meter id: " + json.status + " " + json.responseText);
		  } 
	  });	
			
}

function resetVariables()
{
	  gArrayIndex = "";	
	  gRegStatus = "";
	  gMeterId = 0;
}

//loads meter details from the db
function loadMeters(i)
{
	var vType = "GET";	
	var vUrl = $.jStorage.get("jsUrl") + "enterpriseMeters/getEntMeters?entpId=" + arrEntp[i].getEId();
	
	var vValues = "";
	$("#tblMeterList").text("");
	$("#hMeter").text("Add Meter for " + arrEntp[i].getEName());
	$.ajax
	({
		type:vType,
		contentType: "application/json",
		url:vUrl,
		async:false,
		dataType: "json",
		success:function(json)
		{				
				
				if(json.length > 0 )
				{
					$.each(json, function(i,n)
					{
						vValues = vValues + '<tr>';                                        
						vValues = vValues + '<td width="80px" style = "padding-left:10px">' + n["meterId"] + '</td>';
						vValues = vValues + '<td width="120px" style = "padding-left:20px">' + n["protocolId"] + '</td>';
						vValues = vValues + '</tr>';
					});
				}
				$("#tblMeterList").append(vValues);  
			
		},
		error:function(json)
		{
			alert("Error from laoding meter details: " + json.status + " " + json.responseText);
		} 
	});	
			
}

//validating whether the enterprise has purchased meters or not
function hasMeter(eid)
{
		var vType = "GET";		
		var vUrl = $.jStorage.get("jsUrl") + "enterpriseMeters/getEntMeters?entpId=" + eid;
		var mCount = 0;
		$.ajax
		({
			type:vType,
			contentType: "application/json",
			url:vUrl,
			async:false,
			dataType: "json",
			success:function(json)
			{				
				mCount = json.length;		
			},
			error:function(json)
			{
				alert("Error from laoding meter details: " + json.status + " " + json.responseText);
			} 
		});	
		
		return mCount;
}

//check for unique enterprise ID while saving the edited enterprise ID
function checkUniqueEntpID(eid)
{
		var vType = "GET";		
		var vUrl = $.jStorage.get("jsUrl") + "enterprise/getEnterprise?entpId=" + eid;
		var count = 0;
		$.ajax
		({
			type:vType,
			contentType: "application/json",
			url:vUrl,
			async:false,
			dataType: "json",
			success:function(json)
			{				
				count = json.length;	
			},
			error:function(json)
			{
				alert("Error from laoding enterprise details: " + json.status + " " + json.responseText);
			} 
		});	
		
		if(count === 0)
		{
			return true;
		}
		else
		{
			return false;
		}
}

// funtion that returns the longitude and latitude of an address passed.
function getGeoLocation(address) {
	var geocoder = new google.maps.Geocoder();
	//var address = document.getElementById("taAddress").value;
	
	geocoder.geocode({ 'address': address }, function (results, status) {
		
		if (status === google.maps.GeocoderStatus.OK) 
		{
			$.jStorage.set("jsLat", results[0].geometry.location.lat());
			$.jStorage.set("jsLong", results[0].geometry.location.lng());
			
		} else 
		{
			$.jStorage.set("jsLat", "0.0"); 
			$.jStorage.set("jsLong", "0.0");
			alert("Not a proper address to fetch latitude and longitude from google maps.");
		}
	});
}

//check for unique user id 
function validateUserId(userId)
{
	  var vType = "GET";	
	  var vUrl = $.jStorage.get("jsUrl") + "enterprise/getRegistration";	
	  
	  $.ajax
	  ({
		  type:vType,
		  contentType: "application/json",
		  url:vUrl,
		  async:false,
		  dataType: "json",
		  success:function(json)
		  {				
				$.each(json, function(i,n)
				{
					if(n["userId"] === userId)
					{
						gUserId = 1;
					}
				});	
		  },
		  error:function(json)
		  {
			  alert("Error from validating the user id: " + json.status + " " + json.responseText);
		  } 
	  });	
			
}