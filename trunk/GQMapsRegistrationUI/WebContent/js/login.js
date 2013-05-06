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
	var isValid = 1;
	
	$.jStorage.set("jsUserId", "");
	$.jStorage.set("jsPwd", "");

	if (user.length === 0) 
	{
		alert("Enter Username");
		$('#txtUserId').focus();
		isValid = 0;
	} else if (pwd.length === 0) 
	{
		alert("Enter Password");
		$('#pwdPassword').focus();
		isValid = 0;
	} else if ((user == 'admin') && (pwd == 'admin')) 
	{
		window.location.href = "edit_registration.html";
		$.jStorage.set("jsUserID", user);
	} 
	else 
	{
		var vUrl = 'http://localhost:8080/GQMapsRegistrationServices/gqm-gk/enterprise/getRegistration';
		//var vUrl = 'http://localhost:8080/GQMapsRegistrationServices/gqm-gk/enterprise/authenticate';

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
				
				$('#productInfo').append(json);
				
				if(vRecLen != 0)
				{
					$('#productInfo').append(json[0]["userId"] + " " + json[0]["passwd"] +"\n" + json[1]["userId"] + " " + json[1]["passwd"] +"\n" 
					+ json[2]["userId"] + " " + json[2]["passwd"]);
					
					if(user === "admin" && pwd ==="admin")
					{
						isValid = 1;
						$.jStorage.set("jsUserId", user);
						$.jStorage.set("jsPwd", pwd);						
						window.location.href = "edit_registration.html";
					}
					else
					{
						$.each(json, function(i,n)
						{											
							if( user === $.trim(n["userId"]) && pwd === $.trim(n["passwd"]))
							{
								isValid = 1;
								$.jStorage.set("jsUserId", user);
								$.jStorage.set("jsPwd", pwd);								
								window.location.href = "add_registration.html";						
							}
							else
							{
								isValid = 0;
							}
						  
						});
						
						if(isValid === 0)
						{
							alert("Invalid User");	
							$.jStorage.set("jsUserId", "");
							$.jStorage.set("jsPwd", "");	
							$('#txtUserId').val("");
							$('#pwdPassword').val("");	
							$('#txtUserId').focus();												
						}
					}					
				}
				
			}, //end of success
			error : function(json) 
			{
				alert("Invalid User: " + json.status + " " + json.responseText);
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
		alert("Enter your user id to change password");
			
		window.location.href = "login.html"
		$("#txtUserId").focus();
	} 
	else 
	{
		$.jStorage.set("jsUserID", user);
		window.location.href = "change_password.html";
	}
}