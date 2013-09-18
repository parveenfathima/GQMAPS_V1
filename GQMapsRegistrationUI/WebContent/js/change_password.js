var isValid = "";

//displaying the userid captured in login screen
$(window).load(function () 
{
		$('#tdUserID').append('<label for="cmbQues1" class = "label-font">' + $.jStorage.get("jsUserID") + '</label>');
		changeQA();
});


//binding the click event to the submit button
$(document).ready(function()
{
		$("#submit").bind("click", validateForm);
		$('#chkChageQA').bind("click", changeQA);
		
		//loading the security questions
		loadSecQuestions();

		isValid = 0; // 0 and 1 indicates invalid and valid user flags.
});

// function to validate the change_password form
function validateForm()
{
		//declaring and initializing form variables
	
		var vUserID = $.jStorage.get("jsUserID");
	
		var vQues1 = $('#cmbQues1').val(); 
		var vAns1 = $.trim($('#txtAns1').val()); 
		
		var vQues2 = $('#cmbQues2').val(); 
		var vAns2 = $.trim($('#txtAns2').val()); 
		
		var vNewPwd = $.trim($('#txtNewPwd').val()); 
		var vConfPwd = $.trim($('#txtConfPwd').val()); 
		
		//validating each field for not null and appropriate value selction
		if(!validateQues(vQues1, "1", "oldQA"))
		{
			return false;
		}
		else if(!validateAns(vAns1, "1", "oldQA"))
		{
			return false;
		}
		else if(!validateQues(vQues2, "2", "oldQA"))
		{
			return false;
		}
		else if(vQues2 === vQues1)
		{
			alert("Select unique security questions");
			$('#cmbQues2').focus();
			return false;			
		}
		else if(!validateAns(vAns2, "2", "oldQA"))
		{
			return false;
		}
		else if(!validatePwd(vNewPwd)) // calling it from change security question to set focus back to this page
		{
			$('#txtNewPwd').focus();
			return false;
		}
		else if(!validatePwd(vConfPwd))
		{
			$("#txtConfPwd").focus();
			return false;
		}		
		else if(comparePwd(vNewPwd, vConfPwd))
		{
			//user id validation	
			var vUrl = $.jStorage.get("jsUrl") + "enterprise/getRegistration";			
			
			var vQuery = "";

			$.ajax({
				type : "GET",
				url : vUrl,
				dataType : "json",
				success : function(json) 
				{			
					var vRecLen = json.length;
					
					if(vRecLen != 0 && vUserID != "admin") // validating the user who should not be an admin user to change their pwd and sec. questions
					{									
							$.each(json, function(i,n)
							{											
								if( vUserID === $.trim(n["userId"]) && vQues1 === $.trim(n["secQtn1"]) && vAns1 === $.trim(n["ans1"]) && vQues2 === $.trim(n["secQtn2"]) && vAns2 === $.trim(n["ans2"]))
								{
									isValid = 1; //valid user
									$.jStorage.set("jsUserId", vUserID);
									$.jStorage.set("jsSId", n["sid"]);									
								}	
							});
							
							if(isValid === 0) //invalid user
							{
								alert("Invalid User");	
								$.jStorage.set("jsUserId", "");	
								return false;						
							}
							else
							{
								//alert("Valid user");	
								//$.jStorage.set("jsUserId", vUserID);	
								
								// Check for Change Security Questions option
								var isChecked = $('#chkChageQA').is(':checked');
								var vSId = $.jStorage.get("jsSId");
								if(isChecked)
								{			
									if(!validateChangeQA())   //validating the fields in Change Security Questions for proper values to submi the form if the checkbox is checked
									{
										return false;
									}
									else
									{
									  
									  //alert("Form is ready to submit - includes change sec question : " + vSId + "ques val : " + vQues1 + "  " + vQues2 );
																	
									  var vCQues1 = $('#cmbChangeQues1').val();
									  var vCAns1 = $('#txtChangeAns1').val();
									  var vCQues2 = $('#cmbChangeQues2').val();
									  var vCAns2 = $('#txtChangeAns2').val();									  									  
									  
									  vQuery =  '{"sid":"' + vSId + '", "passwd":"' + vNewPwd + '", "secQtn1":"' + vCQues1 + '", "ans1":"' + vCAns1 + '", "secQtn2":"' + vCQues2 + '", "ans2":"' + vCAns2 + '"}';	
									}
								}
								else
								{
									//alert("Form is ready to submit - only new pwd : " + vSId);									
									vQuery =  '{"sid":"' + vSId + '", "passwd":"' + vNewPwd + '", "secQtn1":"0", "ans1":" ","secQtn2":"0", "ans2":" "}';	
						  
								}
								
								// TODO form submission code goes here
								
								$.ajax
								({
									type:"PUT",
									contentType: "application/json",
									url: $.jStorage.get("jsUrl") + "enterprise/updatePassword",
									async:false,
									data:vQuery,
									dataType: "json",
									success:function(json)
									{
										alert("Updated successfully!");
										//$("#frmAddRegn")[0].reset();  
										window.location.href = "login.html";
									},
									failure:function(json)
									{
										alert("Error: " + json.status + " " + json.responseText);
									} 
								});								
								return true;	
							}											
					} // end of if(vRecLen != 0 && vUserID != "admin")
					else
					{
							alert("Invalid User");	
							$.jStorage.set("jsUserId", "");	
							window.location.href = "login.html";	
					} // end of if(vRecLen != 0)
					
				}, //end of success
				error : function(json) 
				{
					alert("Error: " + json.status + " " + json.responseText);
				}
			});			  		
		}	
}

//comparing old and new password
function comparePwd(password, confPwd)
{
	  if(password != confPwd)
	  {
		  alert("Re-type the New Password in Confirm Password field");
		  $('#txtConfPwd').focus();
		  return false;
	  }
	  else
	  {
		  return true;    
	  }
}

//funciton to enable/disable the fields when the Change Security Questions checkbox is checked/unchecked
function changeQA()
{
		var isChecked = $('#chkChageQA').is(':checked');
		
		if(isChecked)  
		{
			clearChangeQA();
			$("#cmbChangeQues1").attr('disabled', false);
			$("#txtChangeAns1").attr('disabled', false);
			$("#cmbChangeQues2").attr('disabled', false);
			$("#txtChangeAns2").attr('disabled', false);
		}
		else   
		{
			clearChangeQA();
			$('#cmbChangeQues1').attr("disabled", true);
			$("#txtChangeAns1").attr("disabled", true);		
			$('#cmbChangeQues2').attr("disabled", true);
			$("#txtChangeAns2").attr("disabled", true);	
										
		}
}


//function to reset the fields when Change Security Questions checkbox is unchecked
function clearChangeQA()
{
		$('#cmbChangeQues1').val("");
		$('#txtChangeAns1').val("");
		$('#cmbChangeQues2').val("");
		$('#txtChangeAns2').val("");
}


//function to validate the fields in the Change Security Questions if the checkbox is checked
function validateChangeQA()
{
	
		var vCQues1 = $('#cmbChangeQues1').val();
		var vCAns1 = $('#txtChangeAns1').val();
		var vCQues2 = $('#cmbChangeQues2').val();
		var vCAns2 = $('#txtChangeAns2').val();
	
		if(!validateQues(vCQues1, "1", "newQA"))
		{
			return false;
		}
		else if(!validateAns(vCAns1, "1", "newQA"))
		{
			return false;
		}
		if(!validateQues(vCQues2, "2", "newQA"))
		{
			return false;
		}
		else if(vCQues2 === vCQues1)
		{
			alert("Select unique security questions");
			$('#cmbChangeQues2').focus();
			return false;			
		}
		else if(!validateAns(vCAns2, "2", "newQA"))
		{
			return false;
		}
		else
		{
			return true;
		}
}