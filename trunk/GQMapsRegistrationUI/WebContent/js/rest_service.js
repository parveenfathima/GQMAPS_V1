$(document).ready(function() 
{
	$.jStorage.set("jsUrl", "/GQMapsRegistrationServices/");
	$.jStorage.set("jsDBUrl", "/GQMapsCustomerServices/");
	//$.jStorage.set("jsUrl", "http://localhost:8080/GQMapsRegistrationServices/");
	//$.jStorage.set("jsDBUrl", "http://localhost:8080/GQMapsCustomerServices/");
});

function logout()
{
	$.jStorage.flush();
	$.jStorage.set("jsUserId", "");
	$.jStorage.set("jsPwd", "");	
}



