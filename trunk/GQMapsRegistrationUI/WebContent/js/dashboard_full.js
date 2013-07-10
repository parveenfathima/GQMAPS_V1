
//$.jStorage.set("jsUrl", "http://192.168.1.95:8080/GQMapsRegistrationServices/gqm-gk/");
//$.jStorage.set("jsDBUrl", "http://192.168.1.95:8080/GQMapsCustomerServices/gqm-gqedp/");

//$.jStorage.set("jsUrl", "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/");
//$.jStorage.set("jsDBUrl", "http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/");

$.jStorage.set("jsMeters", "");

$(document).ready(function() 
{
	$("#entpName").text("Customer Dashboard for " + $.jStorage.get("jsEName"));
	
	
	loadMeterCount();
	loadMeterTypes();	
	loadExpDays();
	loadLastRunDetails();
	
	loadTotAssets();
	
});


//loads the no. of meters for an enterprise
function loadMeterCount()
{
			
	var vType = "GET";						
	
	var vUrl = $.jStorage.get("jsUrl") + "meterrun/getMeterRun?entpId=" + $.jStorage.get("jsEntpId");	
	
	$.ajax
	({
		type:vType,
		contentType: "application/json",
		url:vUrl,
		async:false,
		dataType: "json",
		success:function(json)
		{
			$("#mCount").text(json );	
			$.jStorage.set("jsMeters", json);
			
			//alert("meter count is: " + $.jStorage.get("jsMeters"));
				
		},
		error:function(json)
		{
			alert("Error from loading meter count: " + json.status + " " + json.responseText);
		} 
	});			
}

//load the meter types for an enterprise
function loadMeterTypes()
{
	var vType = "GET";						
	
	var vUrl = $.jStorage.get("jsUrl") + "enterpriseMeters/getProtocol?entpId=" + $.jStorage.get("jsEntpId");	
	
	$.ajax
	({
		type:vType,
		contentType: "application/json",
		url:vUrl,
		async:false,
		dataType: "json",
		success:function(json)
		{
		
			var vTypes = "";
			
			if($.jStorage.get("jsMeters") != 0)
			{
				//alert("load meter types: " + $.jStorage.get("jsMeters"));
				jQuery.each(json, function(i, v) 
				{
					vTypes = vTypes + "<tr><td class = 'td-font'><span class='span-color'>" + v["protocolId"] + "</span></td><td class = 'td-font'><span class='span-color'>" + v["pcount"] + "</span></td></tr>";

			   });
			}
			else
			{
				vTypes = vTypes + "N/A";
			}
			//alert("vTypes is: " + vTypes);
		   	$("#mTypes").append(vTypes);
			
		},
		error:function(json)
		{
			alert("Error from loading meter types: " + json.status + " " + json.responseText);
		} 
	});	

}

function loadExpDays()
{
	var vType = "GET";						
	
	var vUrl = $.jStorage.get("jsUrl") + "gatekeeper/getExpiryDays?entpId=" + $.jStorage.get("jsEntpId");	
	
	//alert("meter count inside loadexpdays: " + $.jStorage.get("jsMeters"));
	
	if($.jStorage.get("jsMeters") != 0)
	{
		$.ajax
		({
			type:vType,
			contentType: "application/json",
			url:vUrl,
			async:false,
			dataType: "json",
			success:function(json)
			{
					$("#mExpDays").text(json);	
			},
			error:function(json)
			{
				alert("Error from loading expiry days:  " + json.status + " " + json.responseText);
			} 
		});	
	}
	else
	{
		$("#mExpDays").text("0");
	}
	
}

function loadLastRunDetails()
{
	var vType = "GET";						
	
	var vUrl = $.jStorage.get("jsUrl") + "meterrun/getLastScan?entpId=" + $.jStorage.get("jsEntpId");	
	
	if($.jStorage.get("jsMeters") != 0)
	{
	
		$.ajax
		({
			type:vType,
			contentType: "application/json",
			url:vUrl,
			async:false,
			dataType: "json",
			success:function(json)
			{
			   $("#lRunTotAssets").text(json[0][2]);	
			   $("#lRunDt").text(json[0][1]);
						  
			},
			error:function(json)
			{
				alert("Error from loading last run details: " + json.status + " " + json.responseText);
			} 
		});	
	}
	else
	{
			   $("#lRunTotAssets").text(" Null ");	
			   $("#lRunDt").text(" Null ");
	}
}

function loadTotAssets()
{
	var vType = "GET";						
	
	var vUrl = $.jStorage.get("jsDBUrl") + "customerservices/getAssetCount";	
	//var vUrl = "http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/customerservices/getAssetCount";
	
	if($.jStorage.get("jsMeters") != 0)
	{
	
		$.ajax
		({
			type:vType,
			contentType: "application/json",
			url:vUrl,
			async:false,
			dataType: "json",
			success:function(json)
			{
			   	$("#totAssets").text(json[0]);		
			   	loadProtocolTypes();	
						  
			},
			error:function(json)
			{
				alert("Error from loading count of assets: " + json.status + " " + json.responseText);
			} 
		});	
	}
	else
	{
			   $("#totAssets").text(" Null ");	
	}
}


//load the protocol types 
function loadProtocolTypes()
{
	var vType = "GET";						
	
	var vUrl = $.jStorage.get("jsDBUrl") + "customerservices/getEntpProtocolCount";
	//var vUrl = "http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/customerservices/getEntpProtocolCount";
	
	$.ajax
	({
		type:vType,
		contentType: "application/json",
		url:vUrl,
		async:false,
		dataType: "json",
		success:function(json)
		{
			var pTypes = "";
			
			if($.jStorage.get("jsMeters") != 0)
			{
				//alert("load meter types: " + $.jStorage.get("jsMeters"));
				jQuery.each(json, function(i, v) 
				{
					pTypes = pTypes + "<tr><td class = 'td-font'><span class='span-color'>" + v["protocolId"] + "</span></td><td class = 'td-font'><span class='span-color'>" + v["pcount"] + "</span></td></tr>";
		
			   });
			}
			else
			{
				pTypes = pTypes + "N/A";
			}

		   	$("#pTypes").append(pTypes);
			
		},
		error:function(json)
		{
			alert("Error from loading meter types: " + json.status + " " + json.responseText);
		} 
	});	

}