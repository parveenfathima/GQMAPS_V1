
//binding the click event to the submit button
$(document).ready(function()
{
		$("#submit").bind("click", validateForm);
		
		// called when key is pressed in txtPhone textbox
		$("#txtPhone").keypress(function (e) {
		// if the letter is not digit then display error and don't type anything
		 if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) 
		 {
			//display error message
			alert("Enter numbers only");
			return false;
		 }
	   }); 

	      $('#dialog').dialog({ autoOpen: false });
	   $("#btnOpenMe").bind("click", openDialog);
   
});

function openDialog()
{
		$("#dialog").dialog('open');
		return false;
}

function validateForm()
{
		//declaring and initializing form variables
		
		var vEName = $.trim($('#txtEName').val()); //mandatory field
		var vBusCat = $('#cmbBusCategory option:selected').val(); //mandatory field
		var vPhone = $.trim($('#txtPhone').val()); //mandatory field
		var vEmail = $.trim($('#txtEmail').val()); //mandatory field
		var vUsername = $.trim($('#txtUsername').val()); //mandatory field
		var vSaveFwd = $('#cmbSaveFwd').val(); //mandatory field
		var vUrl = $.trim($('#txtUrl').val());
		var vQues1 = $('#cmbQues1').val(); //mandatory field
		var vAns1 = $.trim($('#txtAns1').val()); //mandatory field
		var vQues2 = $('#cmbQues2').val(); //mandatory field
		var vAns2 = $.trim($('#txtAns2').val()); //mandatory field
		var vComments = $.trim($('#taComments').val()); 
		var vEmp = $.trim($('#txtEmp').val());
		var vAsset = $.trim($('#txtAsset').val());
		var vSqft = $.trim($('#txtSqft').val());
		
		//checking the mandatory fields for null to alert user to enter the appropriate values
		
		if(vEName.length == 0)
		{
			alert("Enter Enterprise Name");	
			$('#txtEName').select();
			return false;
		}
		else if(vPhone.length == 0 )
		{
			alert("Enter Phone number");	
			$('#txtPhone').select();	
			return false;			
		}
		else if($.trim($('#txtPhone').val()).length < 8 || $.trim($('#txtPhone').val()).length > 10)
		{
			alert("Enter a valid phone number");
			$('#txtPhone').select();
			return false;			
		}
		else if(vEmail.length == 0)
		{
			alert("Enter E-mail ID");
			$('#txtEmail').select();
			return false;					
		}
		else if(!validateEMail(vEmail))
		{
			alert("Enter a valid E-mail ID");
			$('#txtEmail').select();	
			return false;			
		}
		else if(vUsername.length == 0)
		{
			alert("Enter Username");
			$('#txtUsername').select();		
			return false;			
		}
		else if(vQues1 == "")
		{
			alert("Select your Security Question 1");
			$('#cmbQues1').focus();
			return false;
		}
		else if(vAns1 == "" || vAns1.length == 0 || vAns1.length > 9)
		{
			alert("Enter a valid Answer 1 with less than 10 characters");
			$('#txtAns1').focus();
			$('#txtAns1').select();			
			return false;
		}
		else if(!validateAns(vAns1, "1", "oldQA"))
		{
			return false;
		}
		else if(vQues2 == "")
		{
			alert("Select your Security Question 2");
			$('#cmbQues2').focus();
			return false;
		}
		else if(vAns2 == "" || vAns2.length == 0 || vAns2.length > 9)
		{
			alert("Enter a valid Answer 2 with less than 10 characters");
			$('#txtAns2').focus();
			$('#txtAns2').select();
			return false;
		}
		else if(!validateAns(vAns2, "2", "oldQA"))
		{
			return false;
		}
		
		else 
		{
			//TODO the form submission code goes here
			alert("Registered successfully. Your login credentials will be e-mailed to you to avail the services. Thank you!");
			
			var vEId = generateEntpID("eid");
			var vUId = generateEntpID("uid")
			alert(generateEntpID("eid"));
			alert(generateEntpID("uid"));
			//$('#frmAddRegn').get(0).reset();
			window.location.href = "login.html";
		}
}

//Generating the enterprise id in "NEddhhmmss" format from the current date format of - Thu Apr 04 2013 16:26:45 GMT+0530 (India Standard Time)
//  											                             index is - 012345678901234567890123


function generateEntpID(type)
{
	var dt = new Date(date);
	var dtString = "";
	if(type == 'eid')
	{
		//TODO use getMonth, getHours, etc. methods to form the enterprise id
		dtString = "NE" +  dt.toString().substr(8,2)+ dt.toString().substr(16,2) + dt.toString().substr(19,2) + dt.toString().substr(22,2);
		return dtString;
	}
	else
	{
		dtString = "USER" +  dt.toString().substr(8,2)+ dt.toString().substr(16,2) + dt.toString().substr(19,2) + dt.toString().substr(22,2);
		return dtString;
	}
}

// function to validate email  
function validateEMail(email) 
{
		var emailString = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
		
		if (emailString.test(email)) 
		{
				return true;
		}
		else 
		{
				return false;
		}
}