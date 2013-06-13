
var arrEntp = new Array(enterprise()); // global array to store all the enterprises from the rest service
var gArrayIndex = ""; // global enterprise id.
var gRegStatus = "";
var gMeterId = 0;
var gUserId = 0;
var arrIndex;

$.jStorage.set("jsLat", "0.00"); 
$.jStorage.set("jsLong", "0.00");

$(document).ready(function() 
{
	listEnterprise();
	
	$( "#txtNewExpiry").bind("focusin", showDate);
	
	$("#txtPhone").keypress(function (e) 
	{
	// if the letter is not digit then display error and don't type anything
	 if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) 
	 {
		//display error message
		alert("Enter numbers only");
		$("#txtPhone").focus();
		return false;
	 }
   }); 
});


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
	
	var isActive = $.trim(arrEntp[i].getActive());
	
	if(isActive === 'y')
		$('input:checkbox[name=chkActive]').attr('checked',true);
	else
		$('input:checkbox[name=chkActive]').attr('checked',false);
		
	
	var isRegistered = $.trim(arrEntp[i].getRegStatus());
	
	if(isRegistered === 'y')
	{
		$('input:checkbox[name=chkRegCompl]').attr('checked',true);
		$("#txtEID").attr("disabled", "disabled");
		$("#chkRegCompl").attr("disabled", "disabled");		
	}
	else
	{
		$('input:checkbox[name=chkRegCompl]').attr('checked',false);
		$(txtEID).removeAttr("disabled");
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
	
	var regStatus = "n";
	var active = "n";
	
	if(isRegistered)
		var regStatus  = "y";

	var isActive = $('#chkActive').is(':checked');
	
	if(isActive)
		var active = "y";

	
	var formEntp = new enterprise(dbEntp.getSId(),eid, ename, uid, pwd, output, url, empCount, eSqft, eAsset, dSqft, dAsset, dcUsed, dcTemp, comments, active, regStatus);
	
	if(!validatePwd(formEntp.getPwd()))
	{
		$("#txtPwd").select();
		return false;
	}										 
	else if(compareObject(dbEntp, formEntp))
	{
		alert("No changes found");
		$( "#dlgGeneral" ).dialog( "close" );
	}
	else if(formEntp.getEId().length === 0 || formEntp.getUId().length === 0 || formEntp.getPwd().length === 0)
	{
		alert("Enter all the mandatory values");
		$("#txtEID").focus();
		return false;
	}
	else
	{
		var vType = "PUT";
		//var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/enterprise/updateRegistration";		
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
		vQuery = vQuery + 	formEntp.getPwd() + '", "storeFwd":"' + formEntp.getOutput() + '", "fwdUrl":"' + formEntp.getUrl() + '", "noOfEmpl" : "' + formEntp.getEmpCount(); 
		vQuery = vQuery + '", "entSqft":"' + formEntp.getESqft();
		vQuery = vQuery + '", "entAssetCount":"'+ formEntp.getEAsset() + '", "dcSqft": "' + formEntp.getDSqft() + '", "dcAssetCount":"'+ formEntp.getDAsset();
		vQuery = vQuery + '", "dcUsePctg":"'+ formEntp.getDcUsed() + '", "dcTemp":"'+ formEntp.getDcTemp()+ '", "regCmplt":"' + formEntp.getRegStatus();
		vQuery = vQuery + '", "active":"' + formEntp.getActive()+ '"}';	
		
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
				alert("Enterprise general details are saved successfully!");
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
	$( "#dlgMeter" ).dialog( "open" );

	//loading the protocols from db in the dropdown in general.js
	loadProtocol();
	$("#tblMeterList").val("");
	loadMeters(index);
}

function saveMeter()
{
	addEntpMeter(arrEntp[gArrayIndex]);
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
		alert("Enter mandatory values to submit the details");
		return false;
	}
	else if($("#cmbProtocol").val() === "" )
	{
		alert("Select protocol");
		$("#cmbProtocol").focus();
	}
	else if(vPhone.length != 10)
	{
		alert("Enter a valid phone number");
		$("#txtPhone").select();	
		return false;	
	}	
	else 
	{	
		getGeoLocation(vAddress);	
		
		validateMeterId(vMId);
		
		if(gMeterId === 1)
		{
			alert("Enter a unique meter ID!");
			$("#txtMeterID").select();
			gMeterId = 0;
			return false;
		}
	
		var vType = "POST";
		//var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/enterpriseMeters/addEntMeters";						
		var vUrl = $.jStorage.get("jsUrl") + "enterpriseMeters/addEntMeters";
				
		var vQuery = "";
		vQuery = vQuery + '{"meterId":"' + vMId + '","protocolId":"' + vProtocol + '", "enterpriseId":"' + vEId + '", "descr":"' + vDesc + '", "address":"' + vAddress;
		vQuery = vQuery + '","phone":"' + vPhone + '", "creDttm":"' + vDtTime + '", "latitude":' + $.jStorage.get("jsLat")  + ', "longitude":' + $.jStorage.get("jsLong")  + '}';																		
				
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
				$.jStorage.set("jsLat", "0.00"); 
				$.jStorage.set("jsLong", "0.00");			
				
				alert("Meter details saved successfully!");
				//$('#frmMeter').get(0).reset();
				gArrayIndex = "";
				window.location.href = "edit_registration.html";
			},
			error:function(json)
			{
				resetVariables();	
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
	//$('#txtNewExpiry').blur(); 	
}

function saveValidity()
{
	updateGateKpr(arrEntp[gArrayIndex]);
}

function updateGateKpr(dbEntp)
{
	var dateObject = $("#datepicker").datepicker("getDate");
		
	var vExpDate = $.trim($("#txtNewExpiry").val());
	
	var vComments = $.trim($("#taValComments").val());
	
	if (vExpDate === "" || vExpDate.length === 0)
	{
		alert("Select expiry date");
		$('#txtNewExpiry').blur(); 	
	}
	else if(vComments === "" || vComments.length === 0)
	{
		alert("Enter comments");
		$("#taValComments").focus();
	}
	else
	{
		var vType = "POST";
		//var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/gatekeeper/addEntAudit";
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
				//$( "#dlgValidity" ).dialog( "close" );
				window.location.href = "edit_registration.html";
				//$("#validity")[0].reset();
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
		  width: 475,
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
		//var vUrl = 'http://localhost:8080/GQMapsRegistrationServices/gqm-gk/enterprise/getRegistration';
		var vUrl = $.jStorage.get("jsUrl") + "enterprise/getRegistration";
		
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
							arrEntp[i] = new enterprise($.trim(n["sid"]), $.trim(n["enterpriseId"]), $.trim(n["eName"]), $.trim(n["userId"]), $.trim(n["passwd"]), $.trim(n["storeFwd"]), $.trim(n["fwdUrl"]), $.trim(n["noOfEmpl"]), $.trim(n["entSqft"]), $.trim(n["entAssetCount"]), $.trim(n["dcSqft"]), $.trim(n["dcAssetCount"]), $.trim(n["dcUsePctg"]), $.trim(n["dcTemp"]), $.trim(n["comments"]), $.trim(n["active"]), $.trim(n["regCmplt"]));											
														
							if($.trim(n["regCmplt"]) === 'n')
								vEntpList += '<tr style="height:25px; color: #FF0000">';
							else
								vEntpList += '<tr>';
								
							vEntpList += '<td> <input type = "button" class = "button-font-a" id = "btnGeneral" title = "General" onClick = "openGeneralDialog(' + i + ')"/></td>';
							vEntpList += '<td> <input type = "button" class = "button-font-a" id = "btnMeter" title = "Add Meter" onClick = "openMeterDialog(' + i + ')"/></td>';
							vEntpList += '<td> <input type = "button" class = "button-font-a" id = "btnValidity" title = "Add Validity" onClick = "openValidityDialog(' + i + ')"/></td>';                                            
							vEntpList += '<td class = "td-font-b" style = "padding-left: 20px;">' +  arrEntp[i].getEId() + '</td>';
							vEntpList += '<td class = "td-font-b" style = "padding-left: 20px;">' +  arrEntp[i].getEName() + '</td>';
							vEntpList += '<td class = "td-font-b" style = "padding-left: 20px;">' +  arrEntp[i].getUId()  + '</td>';
							vEntpList += '<td class = "td-font-b" style = "padding-left: 20px;">' +  arrEntp[i].getPwd() + '</td>';
							vEntpList += '<td class = "td-font-b" style = "padding-left: 20px;">' +  arrEntp[i].getRegStatus() + '</td>';
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
function enterprise(sid, eid, ename, uid, pwd, output, url, eEmpCount, eSqft, eAsset, dSqft, dAsset, dcUsed, dcTemp, comments, active, regStatus)   
{
	this.sid = sid;
	this.eid = eid;
	this.ename = ename;
	this.uid = uid;
	this.pwd = pwd;
	this.output = output;
	this.eEmpCount = eEmpCount
	this.url = url;
	this.eSqft = eSqft;
	this.eAsset = eAsset;
	this.dSqft = dSqft;
	this.dAsset = dAsset;
	this.dcUsed = dcUsed;
	this.dcTemp = dcTemp;
	this.comments = comments;
	this.active = active;
	this.regStatus = regStatus;
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
	return this.eEmpCount;
}

enterprise.prototype.setEmpCount = function(eEmpCount)
{
	this.eEmpCount = eEmpCount;
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

enterprise.prototype.getActive = function()
{
	return this.active;
}

enterprise.prototype.setActive = function(active)
{
	this.active = active;
}


enterprise.prototype.getRegStatus = function()
{
	return this.regStatus;
}

enterprise.prototype.setRegStatus = function(url)
{
	this.url = regStatus;
}

enterprise.prototype.toString = function()  
{
	sid, eid, ename, uid, pwd, output, url, eSqft, eAsset, dSqft, dAsset, dcUsed, dcTemp, active, regStatus
	return(this.eid + ", " + this.ename + ", " + this.uid + ", " + this.pwd + ", " + this.output + ", " + this.url + ", " + this.eEmpCount + ", "+ this.eSqft + ", " + this.eAsset + ", " + this.dSqft + ", " + this.dAsset + ", " + this.dcUsed + ", " + this.dcTemp + ", " + this.comments + ", " +this.active + ", " + this.regStatus);
} // end of subject object	


//-------------------------------------------------/Enterprise object---------------------------------------------------


function convertToTwoDigit(no)
{
	return (no >=0 && no <10 ? ("0"+ no) : no)
}

//check for unique meter id 
function validateMeterId(meterId)
{
	  var vType = "GET";
	  //var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/enterpriseMeters/getEntMeters";	
	  var vUrl = $.jStorage.get("jsUrl") + "enterpriseMeters/getEntMeters";	
	  
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
	  //gLongitude = null;
	  //gLatitude = null;	
	  gRegStatus = "";
	  gMeterId = 0;
}

//loads meter details from the db
function loadMeters(i)
{
	var vType = "GET";	
	//var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/enterpriseMeters/getEntMeters";		
	var vUrl = $.jStorage.get("jsUrl") + "enterpriseMeters/getEntMeters/?entpId=" + arrEntp[i].getEId();
	
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

// funtion that returns the longitue and latitude of an address passed.
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
			$.jStorage.set("jsLat", "0.00"); 
			$.jStorage.set("jsLong", "0.00");
			alert("Request failed.");
		}
	});
}

//check for unique user id 
function validateUserId(userId)
{
	  var vType = "GET";
	  //var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/enterprise/getRegistration";	
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