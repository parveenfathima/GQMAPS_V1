$(document).ready(function() 
{
	if(($.jStorage.get("jsUserId") === "" || $.jStorage.get("jsUserId") === null || $.jStorage.get("jsPwd") === "" 
		|| $.jStorage.get("jsPwd") === null)&& ($.jStorage.get("jsEntpId")=== "" || $.jStorage.get("jsEntpId")=== null ))
	{
		window.location.href = "login.html";
	}
});