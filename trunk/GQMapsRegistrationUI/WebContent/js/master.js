
//loads all the security questions from the db
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

