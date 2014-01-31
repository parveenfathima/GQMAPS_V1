	$.jStorage.set("jsUserId", "");
	$.jStorage.set("jsPwd", "");		
	$.jStorage.set("jsEntpId","");	
	$.jStorage.set("jsEName", "");


//binding the click event to the submit button
$(document).ready(function() 
{
	$("#submitLogin").bind("click", userLogin);
});

// login validation for both admin and customers
function userLogin() 
{
	var user = $.trim($('#txtUserId').val());
	var pwd = $.trim($('#pwdPassword').val());
	var isValid = 0;
	
	$.jStorage.set("jsUserId", "");
	$.jStorage.set("jsPwd", "");

	if (user.length === 0) 
	{
		alert("Please Enter Username");
		$('#txtUserId').focus();
	} 
	else if (pwd.length === 0) 
	{
		alert("Please Enter Password");
		$('#pwdPassword').focus();
	} 
	else 
	{
		var vUrl = $.jStorage.get("jsUrl") + "enterprise/getEnterpriseDetails?userId=" + user + "&passwd=" + pwd;
		
		$.ajax({
			type : "GET",
			contentType: "application/json",
			url : vUrl,
			dataType : "json",
			success : function(json) 
			{
				var vRecLen = json.length;

				var vActive = "n";		
				if(vRecLen > 0)
				{
					$.each(json, function(i,n)
					{								
						if( user != "admin" && user === $.trim(n["userId"]) && pwd === $.trim(n["passwd"]))
						{
							isValid = 1;
							$.jStorage.set("jsUserId", user);
							$.jStorage.set("jsPwd", pwd);		
							$.jStorage.set("jsEntpId", n["enterpriseId"]);	
							$.jStorage.set("jsEName", n["eName"]);
							$.jStorage.set("jsTimeout", 180);							
							return false;
						}
					});	
					if(isValid === 1)
					{		
						window.location.href = "dashboard_full.html";	
					}
				}
					
				if(vRecLen==0)
				{
					alert("Either User credentials are invalid or Your meters have expired.");
					$.jStorage.set("jsUserId", "");
					$.jStorage.set("jsPwd", "");	
					$('#txtUserId').val("");
					$('#pwdPassword').val("");	
					$('#txtUserId').focus();
				}
			}, //end of success
			error : function(json) 
			{
				alert("Error from loading user credentials!");
			}
		});
	}
}

//change password validation. User id input is must to navigate to the change_password page
function checkForValue() 
{
	var user = $.trim($('#txtUserId').val());

	if (user.length === 0) 
	{
		alert("Please enter your userid");
			
		window.location.href = "login.html";
		$("#txtUserId").focus();
	} 
	else 
	{
		$.jStorage.set("jsUserID", user);
		$.jStorage.set("jsTimeout", 180);		
		window.location.href = "change_password.html";
	}
}

//function to redirect new enterprise registration process
function addRegistration()
{
	$.jStorage.set("jsTimeout",180);
	window.location.href="add_registration.html";
}