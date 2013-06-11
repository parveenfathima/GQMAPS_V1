


$(document).ready(function() 
{
	//$.jStorage.set("jsUrl", "http://192.168.1.95:8080/GQMapsRegistrationServices/gqm-gk/");
	$.jStorage.set("jsUrl", "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/");
});

function logout()
{
		$.jStorage.flush();
}
