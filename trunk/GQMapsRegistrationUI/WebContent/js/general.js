$.jStorage.set("jsQuestions", "");

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
			var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/secQuest/getSecQuestions";						
					
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
					
					//the same values can be loaded in the change security questions by storing it in the jStorage variable without calling the ajax once again. This is only in the change password page.
					$.jStorage.set("jsQuestions", vValues);
					
                    $("#cmbQues1").append(vValues);   
					$("#cmbQues2").append(vValues);
					$("#cmbQues1").val("");  
					$("#cmbQues2").val("");  					                          
				},
				error:function(json)
				{
					alert("Error: " + json.status + " " + json.responseText);
				} 
			});			
}



//loads all the business lines from the db
function loadBusLine()
{
		var vType = "GET";
		var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/busnLine/getBusnLine";						
				
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
				alert("Error: " + json.status + " " + json.responseText);
			} 
		});	
}


//loads all the protocols from the db
function loadProtocol()
{
			var vType = "GET";
			//var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/secQuest/getSecQuestions";						
			var vUrl = "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/protocol/getProtocols";		
			
			var items = document.getElementById("cmbProtocol").options.length;
			
			//loading the protocols only once since the dialog can be opened many times	
			
			if(items === 1)		
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
						alert("Error: " + json.status + " " + json.responseText);
					} 
				});	
			}
}

