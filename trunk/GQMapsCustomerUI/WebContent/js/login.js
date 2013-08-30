//binding the click event to the submit button
$(document).ready(function() 
{
	$("#submit").bind("click", userLogin);
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
		var vUrl = $.jStorage.get("jsUrl") + "enterprise/getRegistration";

		$.ajax({
			type : "GET",
			beforeSend: function(xhr) 
			{ 
			  xhr.setRequestHeader("Content-Type", "application/json");
			},
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
						if( user != "admin" && user === $.trim(n["userId"]) && pwd === $.trim(n["passwd"]))
						{
							isValid = 1;
							$.jStorage.set("jsUserId", user);
							$.jStorage.set("jsPwd", pwd);		
							$.jStorage.set("jsEntpId", n["enterpriseId"]);	
							$.jStorage.set("jsEName", n["eName"]);
							vFwdStore = n["storeFwd"];
						}
					  
					});		
					
					if(isValid === 1 && vFwdStore === "C" || vFwdStore === "c")
					{		
	
						window.location.href = "dashboard_full.html";	
		
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
				//alert("Invalid User Credentials" + json.status + " " + json.responseText);
				alert("Error: Invalid user credentials!");
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
			
		window.location.href = "login.html"
		$("#txtUserId").focus();
	} 
	else 
	{
		$.jStorage.set("jsUserID", user);
		window.location.href = "change_password.html";
	}
}