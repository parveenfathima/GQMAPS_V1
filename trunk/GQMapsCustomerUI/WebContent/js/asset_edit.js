$(document).ready(function() 
{
	if(($.jStorage.get("jsUserId") === "" || $.jStorage.get("jsUserId") === null || $.jStorage.get("jsPwd") === "" 
		|| $.jStorage.get("jsPwd") === null)&& ($.jStorage.get("jsEntpId")=== "" || $.jStorage.get("jsEntpId")=== null ))
	{
		window.location.href = "login.html";
	}
});

//function to redirect Dashboard screen    
function gotoDashboard() {

	window.location.href="dashboard_full.html";

		}