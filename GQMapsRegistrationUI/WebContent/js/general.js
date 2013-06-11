


$(document).ready(function() 
{
	$.jStorage.set("jsQuestions", "");
});

function logout()
{
		$.jStorage.flush();
}

function convertToTwoDigit(no)
{
		return (no >=0 && no <10 ? ("0"+ no) : no)
}

function getDtTime()
{
		var dt = new Date();
		return (dt.getFullYear() + "-" + convertToTwoDigit(dt.getMonth()+1) + "-" + convertToTwoDigit(dt.getDate()) + " " + convertToTwoDigit(dt.getHours()) + ":" 
											 + convertToTwoDigit(dt.getMinutes()) + ":" + convertToTwoDigit(dt.getSeconds()));
}

//loads all the security questions from the db used for change password and add registration pages.
function loadSecQuestions()
{
			
			var vType = "GET";
			//var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/secQuest/getSecQuestions";
			//var vUrl = "http://192.168.1.95:8080/GQMapsRegistrationServices/gqm-gk/secQuest/getSecQuestions";									
			
			var vUrl = $.jStorage.get("jsUrl") + "secQuest/getSecQuestions";	
			
			
					
			$.ajax
			({
				type:vType,
				contentType: "application/json",
				url:vUrl,
				async:false,
				dataType: "json",
				success:function(json)
				{
					var vValues = "";
					$.each(json, function(i,n)
					{
						vValues = vValues + '<option value = "'+ json[i]["questionId"] + '" >' + json[i]["question"] + '</option>';
					});
					
					$.jStorage.set("jsQuestions", vValues);
					
                    $("#cmbQues1").append(vValues);   
					$("#cmbQues2").append(vValues);
					$("#cmbChangeQues1").append(vValues);
					$("#cmbChangeQues2").append(vValues); 					                          
				},
				error:function(json)
				{
					alert("Error from loading security questions: " + json.status + " " + json.responseText);
				} 
			});			
}



//loads all the business lines from the db
function loadBusLine()
{
		var vType = "GET";
		//var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/busnLine/getBusnLine";		
		//var vUrl = "http://192.168.1.95:8080/GQMapsRegistrationServices/gqm-gk/busnLine/getBusnLine";								
		var vUrl = $.jStorage.get("jsUrl") + "busnLine/getBusnLine";
				
		$.ajax
		({
			type:vType,
			contentType: "application/json",
			url:vUrl,
			async:false,
			dataType: "json",
			success:function(json)
			{
				var vValues = "";
				$.each(json, function(i,n)
				{
					vValues = vValues + '<option value = "'+ json[i]["blCd"] + '" >' + json[i]["descr"] + '</option>';
					
				});
				
				$("#cmbBusCategory").append(vValues);  
			},
			error:function(json)
			{
				alert("Error from loading business lines: " + json.status + " " + json.responseText);
			} 
		});	
}


//loads all the protocols from the db
function loadProtocol()
{
			var vType = "GET";
			//var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/secQuest/getSecQuestions";						
			//var vUrl = "http://192.168.1.95:8080/GQMapsRegistrationServices/gqm-gk/protocol/getProtocols";	
			var vUrl = $.jStorage.get("jsUrl") + "protocol/getProtocols";	
			
			var items = document.getElementById("cmbProtocol").options.length;
			
			//loading the protocols only once since the dialog can be opened many times	
			
			if(items === 0)		
			{
				$.ajax
				({
					type:vType,
					contentType: "application/json",
					url:vUrl,
					async:false,
					dataType: "json",
					success:function(json)
					{				
						var vValues = "";
						$.each(json, function(i,n)
						{
								vValues = vValues + '<option value = "'+ json[i]["protocolId"] + '" >' + json[i]["protocolId"] + '</option>';
						});
						
						$("#cmbProtocol").append(vValues); 
						 
	
												  
					},
					error:function(json)
					{
						alert("Error from loading protocols: " + json.status + " " + json.responseText);
					} 
				});	
			}
}

//-------------------------------------PASSWORD FIELD related functions------------------------------

// validating the password to contain max of 6-12 chars with at least 1 special, number, uppercase and lowercase characters...
function validatePwd(password)
{
		if(password.length < 6 || password.length >12)   //check for the password length to be between 6-12 characters
		{
			alert("Enter a valid password");
			return false;
		}
		else if(!checkSpecialChar(password))   //check whether the password contains at least one special character
		{
			alert('Password should contain at least one special character');
			return false;
		}
		else if(!checkForNoInString(password))   //check whether the password contains at least one number
		{
			alert('Password should contain at least one number');
			return false;
		}
		else if(!checkForUCase(password))   //check whether the password contains at least one uppercase character
		{
			alert('Password should contain at least one uppercase character');
			return false;
		}		
		else if(!checkForLCase(password))   //check whether the password contains at least one lowercase
		{
			alert('Password should contain at least one lowercase character');
			return false;
		}
		else
		{
			return true;    
		}
}
 
//function to check for a string containing a special character
function checkSpecialChar(string)
{
		var vPwdFlag = 0; 
		for (var i = 0; i < string.length; i++) 
		{
			var specialChars = "!@#$%^&*()+=-[]\\\';,./{}|\":<>?~_";
			if (specialChars.indexOf(string.charAt(i)) != -1) 
			{
				vPwdFlag = 1;
				break;
			}
		}	

		if(vPwdFlag == 0)
			return false;
		else if(vPwdFlag == 1)
			return true;
}


//function to check for a string containing a number
function checkForNoInString(string)
{
		var numbers = string.match(/\d+/g);
		
		if (numbers == null) 
		{
			return false;
		}
		return true;
}


//function to check for a string containing an uppercase character
function checkForUCase(string)
{
	var vUpperCaseStr = new RegExp('[A-Z]');
	
	if(!string.match(vUpperCaseStr))
		return false;
		
	return true;
}


//function to check for a string containing a lowercase character
function checkForLCase(string)
{
	var vLowerCaseStr = new RegExp('[a-z]');
	
	if(!string.match(vLowerCaseStr))
		return false;
		
	return true;
}


//function to set focus on the New Password field
function setFocusNewPwd()
{
		$('#txtNewPwd').focus();
		$('#txtNewPwd').select();
		return false;
}

function setFocusEditPwd()
{
		$('#txtPwd').select();
		return false;
}

//-------------------------------------/PASSWORD FIELD related functions------------------------------