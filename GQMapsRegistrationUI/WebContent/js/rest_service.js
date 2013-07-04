


$(document).ready(function() 
{
	//$.jStorage.set("jsUrl", "http://192.168.1.95:8080/GQMapsRegistrationServices/gqm-gk/");
	//$.jStorage.set("jsDBUrl", "http://192.168.1.95:8080/GQMapsCustomerServices/gqm-gqedp/");
	$.jStorage.set("jsUrl", "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/");
	$.jStorage.set("jsDBUrl", "http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/");
	
});

function logout()
{
		$.jStorage.flush();
}
