
//binding the click event to the submit button
$(document).ready(function()
{
		$("#submit").bind("click", validateForm);
		
		$("#txtPhone, #txtESqft, #txtEAsset, #txtDCSqft, #txtDCAsset, #txtDCUsed, #txtDCTemp").keypress(function (e) {
		// if the letter is not digit then display error and don't type anything
		 if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) 
		 {
			//display error message
			alert("Please enter numbers only");
			return false;
		 }
	   }); 
	   
	   loadSecQuestions();
	   loadBusLine();

});

function validateForm()
{
		//declaring and initializing form variables

		var vEName = $.trim($('#txtEName').val()); //mandatory field
		var vBusCat = $('#cmbBusCategory option:selected').val(); //mandatory field
		var vPhone = $.trim($('#txtPhone').val()); //mandatory field
		var vEmail = $.trim($('#txtEmail').val()); //mandatory field
		
		var vSaveFwd = $('#cmbSaveFwd').val(); 
		vSaveFwd = "C";
		var vStoreFwd = $.trim($('#txtUrl').val());
		var vQues1 = $('#cmbQues1 option:selected').val(); //mandatory field
		var vAns1 = $.trim($('#txtAns1').val()); //mandatory field
		var vQues2 = $('#cmbQues2 option:selected').val(); //mandatory field
		var vAns2 = $.trim($('#txtAns2').val()); //mandatory field

		var vEmp = $.trim($('#cmbEmpCount').val());
	
		var vESqft = $.trim($('#txtESqft').val());		
		var vEAsset = $.trim($('#txtEAsset').val());
		var vDCSqft = $.trim($('#txtDCSqft').val());		
		var vDCAsset = $.trim($('#txtDCAsset').val());	
		var vDCUsed = $.trim($('#txtDCUsed').val());	
		var vDCTemp = $.trim($('#txtDCTemp').val());	 

		var vComments = $.trim($('#taComments').val()); 
		
		//checking the mandatory fields for null to alert user to enter the appropriate values
		
		if(vEName.length === 0)
		{
			alert("Please enter Enterprise Name");	
			$('#txtEName').select();
			return false;
		}
		else if(checkSpecialCharWithSpace(vEName))
		{
			alert("Please enter Enterprise Name without special characters");	
			$('#txtEName').select();
			return false;			
		}
		else if(vBusCat === "")
		{
			alert("Select your business line");
			$('#cmbBusCategory').focus();
			return false;
		}
		else if(vPhone.length == 0 )
		{
			alert("Please enter the mobile number");	
			$('#txtPhone').select();	
			return false;			
		}
		else if($.trim($('#txtPhone').val()).length < 10 || $.trim($('#txtPhone').val()).length > 15)
		{
			alert("Please enter a valid mobile number");
			$('#txtPhone').select();
			return false;			
		}
		
		else if(vEmail.length === 0)
		{
			alert("Please enter the email Id");
			$('#txtEmail').select();
			return false;					
		}
		else if(!validateEMail(vEmail))
		{
			alert("Please enter a valid email Id");
			$('#txtEmail').select();	
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
			alert("Enter a valid Answer 1 with less than 10 characters and without special characters ");
			$('#txtAns1').focus();
			$('#txtAns1').select();			
			return false;
		}
		else if(!validateAns(vAns1, "1", "oldQA"))
		{
			return false;
		}
		else if(vQues2 === "")
		{
			alert("Select your Security Question 2");
			$('#cmbQues2').focus();
			return false;
		}
		else if(vQues2 === vQues1)
		{
			alert("Please choose distinct security questions");
			$('#cmbQues2').focus();
			return false;			
		}
		else if(vAns2 === "" || vAns2.length === 0 || vAns2.length > 9)
		{
			alert("Enter a valid Answer 2 with less than 10 characters and without special characters ");
			$('#txtAns2').focus();
			$('#txtAns2').select();
			return false;
		}
		else if(!validateAns(vAns2, "2", "oldQA"))
		{
			return false;
		}		
		else if(vESqft.toString().length > 9)
		{
			alert("Please enter valid IT usable area");
			$('#txtESqft').select();
			return false;
		}
		else if(vEAsset.toString().length > 9)
		{
			alert("Please enter valid total number of IT assets");
			$('#txtEAsset').select();
			return false;
		}
		else if(vDCSqft.toString().length > 9)
		{
			alert("Please enter valid datacenter usable area");
			$('#txtDCSqft').select();
			return false;
		}	
		else if(vDCAsset.toString().length > 9)
		{
			alert("Please enter valid datacenter assets");
			$('#txtDCAsset').select();
			return false;
		}	
		else if(vDCUsed > 100)
		{
			alert("Please enter valid datacenter occupancy rate");
			$('#txtDCUsed').select();
			return false;
		}				
		else if(vDCTemp > 99)
		{
			alert("Please enter valid datacenter temperature");
			$('#txtDCTemp').select();
			return false;
		}					
		else 
		{	
			var vQuery = "";
			var vEId = generateEntpID("eid");
			var vUId = generateEntpID("uid");
			
			var vDateTime = getDtTime();
			var vPassword = "Gq@123";	

			if(vEmp === "")	vEmp = 0;
			if(vESqft === "")	vESqft = 0;
			if(vEAsset  === "") vEAsset  = 0;
			if(vDCSqft  === "")	vDCSqft  = 0;
			if(vDCAsset === "") vDCAsset = 0;
			if(vDCUsed  === "") vDCUsed  = 0;
			if(vDCTemp   === "") 
			{
				vDCTemp   = 0;
			}
					
			if(vStoreFwd === "") vStoreFwd = " ";
			if(vComments === "") vComments = " ";							 
		
			var vType = "POST";			
			var vUrl = $.jStorage.get("jsUrl") + "enterprise/addRegistration";	
			
			vQuery = '{"enterpriseId":"' + vEId + '", "blCd":"' + vBusCat + '", "eName":"' + vEName + '", "phone":"' + vPhone + '", "email":"' + vEmail;
			vQuery = vQuery + '", "userId": "' + vUId + '", "passwd": "' + vPassword + '", "secQtn1":"' + vQues1 + '", "ans1":"' + vAns1;
			vQuery = vQuery + '", "secQtn2":"' + vQues2 + '", "ans2":"' + vAns2 + '", "storeFwd":"' + vSaveFwd ;
			vQuery = vQuery + '", "fwdUrl":"' + vStoreFwd + '", "noOfEmpl":"' + vEmp + '", "entSqft":"' + vESqft + '", "entAssetCount" : "' + vEAsset ;
			vQuery = vQuery + '", "dcSqft":"' + vDCSqft + '", "dcAssetCount": "' + vDCAsset + '", "dcUsePctg" : "' + vDCUsed + '", "dcTemp" : "' + vDCTemp;
			vQuery = vQuery + '", "regCmplt" : "n", "active": "n", "comments":"' + vComments + '","creDttm":"' + vDateTime + '"}';
			
			var msg = "";
			$.ajax
			({
				type:vType,
				contentType: "application/json",
				url:vUrl,
				async:false,
				data:vQuery,
				dataType: "json",
				success:function(json)
				{
					$('#frmAddRegn').unbind('submit');
					$('#submit').attr('disabled','disabled');
					alert("Thank you for registering with GQuotient energy optimization services, an email has been sent to the registered email address with the next steps!");
					$('#frmAddRegn').submit();
					window.location.href = "login.html";
				},
				failure:function(json)
				{
					alert("Error from registering the enterprise: " + json.status + " " + json.responseText);
				} 			
			});				
		}
}

//Generating the enterprise id in "NEddhhmmss" format from the current date format of - Thu Apr 04 2013 16:26:45 GMT+0530 (India Standard Time)
//  											                             index is - 012345678901234567890123

function generateEntpID(type)
{
		var dt = new Date();
		var dtString = "";
		if(type == 'eid')
		{
			//TODO use getMonth, getHours, etc. methods to form the enterprise id
			dtString = "NE" + convertToTwoDigit(dt.getDate()) + convertToTwoDigit(dt.getHours()) + convertToTwoDigit(dt.getMinutes()) + convertToTwoDigit(dt.getSeconds());
			return dtString;
		}
		else
		{
			dtString = "USER" + convertToTwoDigit(dt.getDate()) + convertToTwoDigit(dt.getHours()) + convertToTwoDigit(dt.getMinutes()) + convertToTwoDigit(dt.getSeconds());
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

//converting single digit no to two digits, that is preceeding with 0 if single digit. Used for date and time formats
function convertToTwoDigit(no)
{
		return (no >=0 && no <10 ? ("0"+ no) : no)
}

//function disable submit button on submission of the form.
$('#frmAddRegn').submit(function(){
    $('input[type=submit]', this).attr('disabled', 'disabled');
});