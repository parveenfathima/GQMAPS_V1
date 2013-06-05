$.jStorage.set("jsQuestions", "");
//$.jStorage.set("jsUrl", "http://192.168.1.95:8080/GQMapsRegistrationServices/gqm-gk/");
$.jStorage.set("jsUrl", "http://localhost:8080/GQMapsRegistrationServices/gqm-gk/");

$(document).ready(function() 
{

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

