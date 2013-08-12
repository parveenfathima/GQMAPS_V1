
$.jStorage.set("jsMeters", "");
var vType = "GET";	
var vUrl = "";

$(document).ready(function() 
{
	$("#pGoalName").text("Goal: " + $.jStorage.get("jsGoalName"));
	loadTasks($.jStorage.get("jsGoalId"));
});


function loadTasks(goalId)
{
	
}

//loads the no. of meters for an enterprise
function loadMeterCount()
{
	vUrl = $.jStorage.get("jsUrl") + "meterrun/getMeterRun?entpId=" + $.jStorage.get("jsEntpId");	
	//loadDashboard(vType, vUrl, "MeterCount");

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
				
		},
		error:function(json)
		{
			alert("Error from loading meter count: " + json.status + " " + json.responseText);
		} 
	});		
		
}
