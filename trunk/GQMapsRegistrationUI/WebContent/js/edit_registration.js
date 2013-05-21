
var arrEntp = new Array(enterprise()); // global array to store all the enterprises from the rest service
var gArrayIndex = ""; // global enterprise id.
var gRegStatus = "";
var gMeterId = 0;
var arrIndex;

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
	$("#hBasic").text(arrEntp[i].getEName());
	
	$("#txtEID").val(arrEntp[i].getEId());
	$("#txtEName").val(arrEntp[i].getEName());
	$("#txtUID").val(arrEntp[i].getUId());
	$("#txtPwd").val(arrEntp[i].getPwd());
	$("#cmbSaveFwd").val(arrEntp[i].getOutput());
	$("#txtUrl").val(arrEntp[i].getUrl());
	$("#txtESqft").val(arrEntp[i].getESqft());
	$("#txtEAsset").val(arrEntp[i].getEAsset());
	$("#txtDCSqft").val(arrEntp[i].getDSqft());
	$("#txtDCAsset").val(arrEntp[i].getDAsset());
	$("#txtDCUsed").val(arrEntp[i].getDcUsed());
	$("#txtDCTemp").val(arrEntp[i].getDcTemp());
	$("cmbActive").val(arrEntp[i].getActive());				
	$("#cmbRegCompl").val(arrEntp[i].getRegStatus());
			
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
		alert(dbEntp.toString());
		
		var eid = $.trim($("#txtEID").val());
		var ename = $.trim($("#txtEName").val());
		var uid = $.trim($("#txtUID").val());
		var pwd = $.trim($("#txtPwd").val());
		var output = $.trim($("#cmbSaveFwd").val());
		var url = $.trim($("#txtUrl").val());
		var eSqft = $.trim($("#txtESqft").val());
		var eAsset = $.trim($("#txtEAsset").val());
		var dSqft = $.trim($("#txtDCSqft").val());
		var dAsset = $.trim($("#txtDCAsset").val());
		var dcUsed = $.trim($("#txtDCUsed").val());
		var dcTemp = $.trim($("#txtDCTemp").val());
		var active = $.trim($("#cmbRegCompl").val());
		var regStatus = $.trim($("#cmbActive").val());
		
		
		var formEntp = new enterprise(dbEntp.getSId(),eid, ename, uid, pwd, output, url, eSqft, eAsset, dSqft, dAsset, dcUsed, dcTemp, active, regStatus);
			
		if(compareObject(dbEntp, formEntp))
		{
			alert("No changes");
		}
		else
		{
			alert("Changes found");
			
			//TODO call to ajax
			
			var vType = "POST";
			var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/enterprise/updateRegistration";						
	
			var vQuery = "";
			
			vQuery = vQuery + '{"sid":"' + dbEntp.getSId() + '", "enterpriseId":"' + formEntp.getEId() + '", "userId":"' + formEntp.getUId() + '", "passwd":"' ;
			vQuery = vQuery + 	formEntp.getPwd() + '", "storeFwd":"' + formEntp.getOutput() + '", "fwdUrl":"' + formEntp.getUrl() + '"entSqft":"' + formEntp.getESqft();
			vQuery = vQuery + '", "entAssetCount":"'+ formEntp.getEAsset() + '", "dcSqft": "' + formEntp.getDSqft() + '", "dcAssetCount":"'+ formEntp.getDAsset();
			vQuery = vQuery + '", "dc_use_pctg":"'+ formEntp.getDcUsed() + '", "dc_temp":"'+ formEntp.getDcTemp()+ '", "regCmplt":"' + formEntp.getRegStatus();
			vQuery = vQuery + '", "active":"' + formEntp.getActive()+ '"}';	
			
			alert(formEntp.toString());	
			
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
					//$( "#dlgGeneral" ).dialog( "close" );
					listEnterprise();
					gArrayIndex = "";
				},
				error:function(json)
				{
					alert("Error: " + json.status + " " + json.responseText);
				}
			});	

		}
}


// compare the enterprise values before and after the change. If changed, then only the ajax will be called to update.
function compareObject(obj1, obj2)
{
	for(var parameters in obj1)
	{
		if(obj1[parameters] !== obj2[parameters])
		{
			return false;
		}
	}
	
	for(var parameters in obj2)
	{
		if(obj1[parameters] !== obj2[parameters]){
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
function openMeterDialog(eId)
{
	gArrayIndex = eId;
	$( "#dlgMeter" ).dialog( "open" );

	//loading the protocols from db in the dropdown in general.js
	loadProtocol();
	loadMeters();
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
		var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/enterpriseMeters/addEntMeters";						
				
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
				gLongitude = null;
				gLatitude = null;	
				gMeterId = 0;	
				$.jStorage.set("jsLat", ""); 
				$.jStorage.set("jsLong", "");			
				
				alert("Meter details saved successfully!");
				$('#frmMeter').get(0).reset();
				loadMeters();
			},
			error:function(json)
			{
				resetVariables();	
				alert("Error: " + json.status + " " + json.responseText);
			}
		});	
	}
}

// opening the meter dialog for the selected enterprise to add the validity details. 
function openValidityDialog(eId)
{
	gArrayIndex = eId;
	$( "#dlgValidity" ).dialog( "open" );
	$('#txtNewExpiry').blur(); 	
}

function saveValidity()
{
	updateGateKpr(arrEntp[gArrayIndex]);
}

function updateGateKpr(dbEntp)
{
	var dateObject = $("#datepicker").datepicker("getDate");
	
	var vExpDate = $.trim($("#txtNewExpiry").val());
	var vCount = $.trim($("#txtNewScanCount").val());
	var vComments = $.trim($("#taComments").val());
	
	if(vComments === "" || vComments.length === 0)
	{
		vComments = null; 	
	}
	
	var vCondition = $("#cmbCondition").val();		
	
	if(vExpDate === "" || vExpDate.length === 0 || vCount === "" || vCount.length === 0)
	{
		alert("Enter the mandatory values to save/update the validity details");
		return false;
	}
	else
	{
		var vType = "POST";
		var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/gatekeeper/addEntAudit";						

		
		var vEDt = new Date(vExpDate);
		vExpDate= vEDt.getFullYear() + "-" + convertToTwoDigit(vEDt.getMonth()) + "-" + convertToTwoDigit(vEDt.getDate() + " " + convertToTwoDigit(vEDt.getHours()) + ":" 
										 + convertToTwoDigit(vEDt.getMinutes()) + ":" + convertToTwoDigit(vEDt.getSeconds()));

		var dt = new Date();
		var vCDtTime = dt.getFullYear() + "-" + convertToTwoDigit(dt.getMonth()) + "-" + convertToTwoDigit(dt.getDate()) + " " + convertToTwoDigit(dt.getHours()) + ":" 
										 + convertToTwoDigit(dt.getMinutes()) + ":" + convertToTwoDigit(dt.getSeconds());

		var vQuery = '{"enterpriseId" : "' + dbEntp.getEId() + '", "comment" : "' + vComments + '","scanPurchased" : "' + vCount + '", "expDttm" : "' + vExpDate +'","creDttm" : "' + vCDtTime + '"}';											 
										 
				
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
				//$("#validity")[0].reset();
				//TODO ajax call for gatekpr to be done as the service is not giving proper result which has got some issues in saving the data. need to be checked.
				gArrayIndex = ""
				
			},
			error:function(json)
			{
				alert("Error: " + json.status + " " + json.responseText);
			}
		});	

	}
		
}

//General form dialog configuration
$(function() {
	  $( "#dlgGeneral" ).dialog({
		  autoOpen: false,
		  height: 870,
		  width: 400,
		  modal: true,
		  position: "center"
	  });		  
});

//Meter form dialog configuration
$(function() {
	  $( "#dlgMeter" ).dialog({
		  autoOpen: false,
		  height: 502,
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
		var vUrl = 'http://localhost:8080/GQMapsRegistrationServices/gqm-gk/enterprise/getRegistration';
		
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
							arrEntp[i] = new enterprise(n["sid"], n["enterpriseId"], n["eName"], n["userId"], n["passwd"], n["storeFwd"], n["fwdUrl"], n["entSqft"], n["entAssetCount"], n["dcSqft"], n["dcAssetCount"], n["dc_use_pctg"], n["dc_temp"], n["active"], n["regCmplt"]);											
							
							if(gRegStatus === 'n')
								vEntpList += '<tr style="height:25px; color: #FF0000">';
							else
								vEntpList += '<tr>';
								
							vEntpList += '<td> <input type = "button" class = "button-font-a" id = "btnGeneral" onClick = "openGeneralDialog(' + i + ')"/></td>';
							vEntpList += '<td> <input type = "button" class = "button-font-a" id = "btnMeter" onClick = "openMeterDialog(' + i + ')"/></td>';
							vEntpList += '<td> <input type = "button" class = "button-font-a" id = "btnValidity" onClick = "openValidityDialog(' + i + ')"/></td>';                                            
							vEntpList += '<td class = "td-font-b">' +  arrEntp[i].getEId() + '</td>';
							vEntpList += '<td class = "td-font-b">' +  arrEntp[i].getEName() + '</td>';
							vEntpList += '<td class = "td-font-b">' +  arrEntp[i].getUId()  + '</td>';
							vEntpList += '<td class = "td-font-b">' +  arrEntp[i].getPwd() + '</td>';
							vEntpList += '<td class = "td-font-b">' +  arrEntp[i].getActive() + '</td>';
							vEntpList += '<td class = "td-font-b">' +  arrEntp[i].getRegStatus() + '</td>';
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
				alert("Error: " + json.status + " " + json.responseText);
			}
		});			  		
}

// enterprise object's constructor, get and set methods		
function enterprise(sid, eid, ename, uid, pwd, output, url, eSqft, eAsset, dSqft, dAsset, dcUsed, dcTemp, active, regStatus)   
{
	this.sid = sid;
	this.eid = eid;
	this.ename = ename;
	this.uid = uid;
	this.pwd = pwd;
	this.output = output;
	this.url = url;
	this.eSqft = eSqft;
	this.eAsset = eAsset;
	this.dSqft = dSqft;
	this.dAsset = dAsset;
	this.dcUsed = dcUsed;
	this.dcTemp = dcTemp;
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
	return(this.eid + ", " + this.ename + ", " + this.uid + ", " + this.pwd + ", " + this.output + ", " + this.url + ", " + this.eSqft + ", " + this.eAsset + ", " + this.dSqft + ", " + this.dAsset + ", " + this.dcUsed + ", " + this.dcTemp + ", " + this.active + ", " + this.regStatus);
} // end of subject object	




function convertToTwoDigit(no)
{
	return (no >=0 && no <10 ? ("0"+ no) : no)
}

//check for unique meter id 
function validateMeterId(meterId)
{
	  var vType = "GET";
	  var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/enterpriseMeters/getEntMeters";		
	  
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
			  alert("Error: " + json.status + " " + json.responseText);
		  } 
	  });	
			
}

function resetVariables()
{
	  gArrayIndex = "";
	  gLongitude = null;
	  gLatitude = null;	
	  gRegStatus = "";
	  gMeterId = 0;
}

//loads meter details from the db
function loadMeters()
{
	var vType = "GET";	
	var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/enterpriseMeters/getEntMeters";		
	
	$.ajax
	({
		type:vType,
		contentType: "application/json",
		url:vUrl,
		async:false,
		dataType: "json",
		success:function(json)
		{				
			var vValues = "";
			$.each(json, function(i,n)
			{
				vValues = vValues + '<tr>';                                        
				vValues = vValues + '<td width="80px" style = "padding-left:10px">' + n["meterId"] + '</td>';
				vValues = vValues + '<td width="120px" style = "padding-left:20px">' + n["protocolId"] + '</td>';
				vValues = vValues + '</tr>';
			});
			$("#tblMeterList").append(vValues);  
		},
		error:function(json)
		{
			alert("Error: " + json.status + " " + json.responseText);
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
			$.jStorage.set("jsLat", ""); 
			$.jStorage.set("jsLong", "");
			alert("Request failed.");
		}
	});
}
