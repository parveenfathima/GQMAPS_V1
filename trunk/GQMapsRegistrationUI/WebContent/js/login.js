//binding the click event to the submit button
$(document).ready(function() 
{
	$("#submit").bind("click", userLogin);
});

// login validation for admin user
function userLogin() 
{
	var user = $.trim($('#txtUserId').val());
	var pwd = $.trim($('#pwdPassword').val());
	var isValid = 0;
	
	$.jStorage.set("jsUserId", "");
	$.jStorage.set("jsPwd", "");

	if (user.length === 0) 
	{
		alert("Enter Username");
		$('#txtUserId').focus();
	} 
	else if (pwd.length === 0) 
	{
		alert("Enter Password");
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
				var vFwdStore = "";	
				var vActive = "n";		
				if(vRecLen > 0)
				{
					$.each(json, function(i,n)
					{								
						if( user === "admin" && user === $.trim(n["userId"]) && pwd === $.trim(n["passwd"]))
						{
							isValid = 1;
							$.jStorage.set("jsUserId", user);
							$.jStorage.set("jsPwd", pwd);		
							$.jStorage.set("jsEntpId", n["enterpriseId"]);	
							$.jStorage.set("jsEName", n["eName"]);
							return false;
						}
					  
					});		
					
					if(isValid === 1)
					{		
							window.location.href = "edit_registration.html";	
					}
					else
					{
						alert("Invalid user credential!");
						$.jStorage.set("jsUserId", "");
						$.jStorage.set("jsPwd", "");	
						$('#txtUserId').val("");
						$('#pwdPassword').val("");	
						$('#txtUserId').focus();
					}
				}
				
			}, //end of success
			error : function(json) 
			{
				alert("Error from loading user credentials!");
			}
		});
	}
}

//change password validation. User id input is a must to navigate to the change_password page
function checkForValue() 
{
	var user = $.trim($('#txtUserId').val());

	if (user.length === 0 || user === null) 
	{
		alert("Please enter your userid");
		$("#txtUserId").focus();
	} 
	else 
	{
		$.jStorage.set("jsUserID", user);
		window.location.href = "change_password.html";
	}
}