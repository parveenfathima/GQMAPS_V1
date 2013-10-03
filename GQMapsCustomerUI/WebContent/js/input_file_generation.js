var gCSArray = new Array();
var i = 0; 

$(document).ready(function() 
{
	loadMeters("enter");
	checkForIPAdd();
	
	$('#cmbMeters').bind("change", function()
	{
		setProtocol();
	});
	
	$('#chkFull').bind("change", function()
	{
		setValues();
	});
	
	$('#submit').bind("click", function()
	{
		var vCString = $.trim($("#txtComString").val());
		if(vCString === "" || vCString.indexOf(' ')>=0)
		{
			alert("Enter a valid Community String");
			$("#txtComString").focus();
		}
		else if(checkForIPAdd() === "l")
		{
			alert("Enter a valid IP address");
			$("#txtIPLB").select();
			//return false;
		}
		else if(checkForIPAdd() === "u")
		{
			alert("Enter a valid IP address");
			$("#txtIPUB").select();
			//return false;
		}	
		else if(checkForIPAdd())
		{
			alert("valid");
			return true;
		
		}
	});
	
	$('#btnAdd').bind("click", function()
	{
		var vCString = $.trim($("#txtComString").val());
		if(vCString === "" || vCString.indexOf(' ')>=0)
		{
			alert("Enter a valid Community String");
			$("#txtComString").focus();
		}
		else if(checkForIPAdd() === "l")
		{
			alert("Enter a valid IP address");
			$("#txtIPLB").select();
			//return false;
		}
		else if(checkForIPAdd() === "u")
		{
			alert("Enter a valid IP address");
			$("#txtIPUB").select();
			//return false;
		}	
		else if(checkForIPAdd())
		{
			alert("valid");
			
			var vRows = "";
			vRows = vRows + '<tr><td>'+ $("#txtComString").val() + '</td><td>' + $("#txtIPLB").val() + '</td><td>' + $("#txtIPUB").val() + '</td></tr>';
			
			$("#tblCString").append(vRows);
			
			gCSArray
		}
	});	
	
});



//loads meter details from the db
function loadMeters(i)
{
	var vType = "GET";	
		
	var vUrl = $.jStorage.get("jsUrl") + "enterpriseMeters/getEntMeters/?entpId=" + i;
	
	//var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/enterpriseMeters/getEntMeters/?entpId=" + i;
	
	var vValues = "";
	var vProtocol = "";
	
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
				vValues = vValues + '<option value = "'+ json[i]["meterId"] + '" >' + json[i]["meterId"] + '</option>';	
			});				
				
			vProtocol = json[0]["protocolId"];
			$("#txtSwitch").val(vProtocol);
			$("#cmbMeters").append(vValues);
		},
		error:function(json)
		{
			alert("Error from laoding meter details: " + json.status + " " + json.responseText);
		} 
	});	
			
}

//function to check for a valid ip address
function checkForIPAdd()
{
	var vLB = $.trim($("#txtIPLB").val());
	var vUB = $.trim($("#txtIPUB").val());	
	if(vLB === "")
	{
		return "l";
	}
	else if(vUB === "")
	{
		return "u";
	}
	else
	{
		
		//var vIPString = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
		
		//var vIPString = "/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/";
		
		//var vIPString = "/^[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}$/";
		
		
		var splitLB = vLB.split('.');
		var splitUB = vUB.split('.');
		
		if (splitLB.length != 4)
		{
			return "l";
		}
		else if(splitLB.length === 4)
		{
			for (var i=0; i<splitLB.length; i++) 
			{
				var s = splitLB[i];
				if (s.length==0 || isNaN(s) || s<0 || s>255)
					return "l";
			}
		}
		else if(splitUB.length != 4)
			return "u";
		else if(splitUB === 4)
		{
			for (var i=0; i<splitUB.length; i++) 
			{
				var s = splitUB[i];
				if (s.length==0 || isNaN(s) || s<0 || s>255)
					return "u";
			}
		}
		else
		{
			//alert("true");	
			return true;
		}
	}
}

function setProtocol()
{
	var mId = $('#cmbMeters option:selected').text();
	
	var vType = "GET";	
		
	var vUrl = $.jStorage.get("jsUrl") + "enterpriseMeters/getMeter?meterId=" + mId;
	var vProtocol = "";
	
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
				vProtocol = json[0]["protocolId"];
				$("#txtSwitch").val(vProtocol);
			} 			
		},
		error:function(json)
		{
			alert("Error from laoding meter details for the entered meter id: " + json.status + " " + json.responseText);
		} 
	});	
}

function setValues()
{
	var vChkFull = $("#chkFull").is(':checked');
	if(vChkFull)
	{
		$("#chkCompSS").attr('checked', false);
		$("#chkCD").attr('checked', false);
		$("#chkIS").attr('checked', false);
		$("#chkProc").attr('checked', false);
		
		$("#chkCompSS").attr('disabled', true);
		$("#chkCD").attr('disabled', true);
		$("#chkIS").attr('disabled', true);
		$("#chkProc").attr('disabled', true);
	}
	else
	{
		$("#chkCompSS").attr('checked', true);
		$("#chkCD").attr('checked', true);
		$("#chkIS").attr('checked', true);
		$("#chkProc").attr('checked', true);	
		
		$("#chkCompSS").attr('disabled', false);
		$("#chkCD").attr('disabled', false);
		$("#chkIS").attr('disabled', false);
		$("#chkProc").attr('disabled', false);			
	}
}