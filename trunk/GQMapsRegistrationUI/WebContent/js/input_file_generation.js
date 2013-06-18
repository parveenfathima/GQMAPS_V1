


$(document).ready(function() 
{
	loadMeters("enter");
	checkForIPAdd();
});


//loads meter details from the db
function loadMeters(i)
{
	var vType = "GET";	
		
	var vUrl = $.jStorage.get("jsUrl") + "enterpriseMeters/getEntMeters/?entpId=" + i;
	
	//var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/enterpriseMeters/getEntMeters/?entpId=" + i;
	
	var vValues = "";
	
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
						vValues = vValues + '<option value = "'+ json[i]["meterId"] + '" >' + json[i]["meterId"] + '</option>';
					});

				}
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
	var string = "1.1.1.1";
	var vUpperCaseStr = new RegExp('/^[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}$/');
	
	if(!string.match(vUpperCaseStr))
		alert("false");
		//return false;
	
	//return true;
	alert("true");
}


