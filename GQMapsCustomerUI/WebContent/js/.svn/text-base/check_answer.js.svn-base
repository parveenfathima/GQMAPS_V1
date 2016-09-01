// validating the answer to be in one word and less than 10 characters
function validateAns(answer, quesNo, section)
{
		if(answer === "" || answer.length === 0 || answer.length > 9)
		{
			setAnsAlert(quesNo, section);
			return false;
		}
		else if(checkSpecialChar(answer))
		{
			setAnsAlert(quesNo, section);
			return false;			
		}
		else
		{
			var pattern = new RegExp(' ');
			var value = pattern.exec(answer);
		
			// Check for white space
			if (value != null) 
			{
				setFocus(quesNo, section);
				return false;
			}
			else
			{
				return true;
			}
		}
}

//function to set the appropriate alert messages if the answer contains space 
function setFocus(quesNo, section)
{
	if(quesNo == "1" && section === "oldQA")
	{
		alert("Answer 1 should be in one word without special characters and less than 10 characters");
		$('#txtAns1').focus();
		$('#txtAns1').select();		
	}
	else if(quesNo == "2" && section === "oldQA")
	{
		alert("Answer 2 should be in one word without special characters and less than 10 characters");
		$('#txtAns2').focus();
		$('#txtAns2').select();		
	}
	else if(quesNo == "1" && section === "newQA")
	{
		alert("New Answer 1 should be in one word without special characters and less than 10 characters");
		$('#txtChangeAns1').focus();
		$('#txtChangeAns1').select();		
	}
	else if(quesNo == "2" && section === "newQA")
	{
		alert("New Answer 2 should be in one word without special characters and less than 10 characters");
		$('#txtChangeAns2').focus();
		$('#txtChangeAns2').select();		
	}
}

//function to set the alert messages if they are not valid that is if the answer is null or > 9 characters
function setAnsAlert(quesNo, section)
{
	if(quesNo == "1" && section == "oldQA")
	{
		alert("Enter valid Answer 1 in one word with maximum of 10 characters and without special characters");
		$('#txtAns1').focus();
		$('#txtAns1').select();		
	}
	else if(quesNo == "2" && section == "oldQA")
	{
		alert("Enter a valid Answer 2 in one word with maximum of 10 characters and without special characters");
		$('#txtAns2').focus();
		$('#txtAns2').select();		
	}
	else if(quesNo == "1" && section == "newQA")
	{
		alert("Enter a valid Answer 1 in one word with maximum of 10 characters and without special characters");
		$('#txtChangeAns1').focus();
		$('#txtChangeAns1').select();		
	}
	else if(quesNo == "2" && section == "newQA")
	{
		alert("Enter a valid Answer 2  in one word with maximum of 10 characters and without special characters");
		$('#txtChangeAns2').focus();
		$('#txtChangeAns2').select();		
	}
}

//function to display the appropriate alert messages when the user does not select the security questions
function validateQues(question, quesNo, section)
{
		if(question === "" && quesNo == "1")
		{
			alert("Please choose distinct security question");
			
			if(section === "oldQA")
				$('#cmbQues1').focus();
			else
				$('#cmbQues2').focus();
				
			return false;
		}
		else if(question === "" && quesNo == "2")
		{
			alert("Please choose distinct security question");
			
			if(section = "newQA")
				$('#cmbChangeQues1').focus();
			else
				$('#cmbChangeQues2').focus();
				
			return false;
		}
		else
		{
			return true;
		}
}