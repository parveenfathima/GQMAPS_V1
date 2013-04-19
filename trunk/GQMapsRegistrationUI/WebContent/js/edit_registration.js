
var arrEntp = new Array(enterprise()); // global array to store all the enterprises from the rest service
var gEId = ""; // global enterprise id.
var gRegStatus = "";

$(document).ready(function() 
{
	$( "#txtNewExpiry").bind("focusin", showDate);
});

// saving the enterprise general information dialog
function saveGeneral()
{
	updateEntp(arrEntp[gEId]);   
}

// updating only the changed object
function updateEntp(dbEntp)
{
		var formEntp = new enterprise($.trim($("#txtEID").val()), $.trim($("#txtEName").val()), $.trim($("#txtUID").val()), $.trim($("#txtPwd").val()), $.trim($("#cmbSaveFwd").val()), $.trim($("#txtUrl").val()), dbEntp.getRegStatus());
		
		if(compareObject(dbEntp, formEntp))
		{
			alert("No changes");
		}
		else
		{
			alert("Changes found");
			
			//TODO call to ajax
			
			// gEId = ""
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
	$("#txtNewExpiry").datepicker({
		minDate: 0
	});
}

// opening the general dialog with the selected enterprise's basic information to edit by the admin user. 
function openGeneralDialog(i)
{
	gEId = i;
	$( "#dlgGeneral" ).dialog( "open" );
	$("#hBasic").text(arrEntp[i].getEId() + " - " + arrEntp[i].getEName());
	$("#txtEID").val(arrEntp[i].getEId());
	$("#txtEName").val(arrEntp[i].getEName());
	$("#txtUID").val(arrEntp[i].getUId());
	$("#txtPwd").val(arrEntp[i].getPwd());
	$("#cmbSaveFwd").val(arrEntp[i].getOutput());
	$("#txtUrl").val(arrEntp[i].getUrl());
	
	$("#txtEID").focus();
	
}

// opening the meter dialog for the selected enterprise to add the meter details. 
function openMeterDialog(eId)
{
	$( "#dlgMeter" ).dialog( "open" );
	$("#txtMeterID").text(eId);
}

// opening the meter dialog for the selected enterprise to add the validity details. 
function openValidityDialog(eId)
{
	$( "#dlgValidity" ).dialog( "open" );
	$('#txtNewExpiry').blur(); 
}

//General form dialog configuration
$(function() {
	  $( "#dlgGeneral" ).dialog({
		  autoOpen: false,
		  height: 435,
		  width: 300,
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
		var vUrl = 'http://localhost:8080/GQMapsRegistrationServices/gqm-gk/enterprise/getregistration';
		
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
							var vEId = n["enterpriseId"];
							gRegStatus = n["regCmplt"];
							
							arrEntp[i] = new enterprise(n["enterpriseId"], n["EName"], n["userId"], n["passwd"], n["storeFwd"], n["fwdUrl"], n["regCmplt"]);											
							
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
function enterprise(eid, ename, uid, pwd, output, url, regStatus)   
{
	this.eid = eid;
	this.ename = ename;
	this.uid = uid;
	this.pwd = pwd;
	this.output = output;
	this.url = url;
	this.regStatus = regStatus;
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
	return(this.eid + " " + this.ename + " " + this.uid + " " + this.pwd + " " + this.output + " " + this.url + " " + this.regStatus);
} // end of subject object	