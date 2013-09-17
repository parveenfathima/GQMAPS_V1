$(document).ready(function() 
{
	$.jStorage.set("jsUrl", "http://192.168.1.95:8080/GQMapsRegistrationServices/");
	$.jStorage.set("jsDBUrl", "http://192.168.1.95:8080/GQMapsCustomerServices/");
	//$.jStorage.set("jsUrl", "http://localhost:8080/GQMapsRegistrationServices/");
	//$.jStorage.set("jsDBUrl", "http://localhost:8080/GQMapsCustomerServices/");
});

function logout()
{
	$.jStorage.flush();
	$.jStorage.set("jsUserId", "");
	$.jStorage.set("jsPwd", "");	
}



