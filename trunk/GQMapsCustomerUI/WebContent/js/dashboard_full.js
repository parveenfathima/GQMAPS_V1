$.jStorage.set("jsMeters", "");
$.jStorage.set("jsAstCount", 0);
$.jStorage.set("goalId", "");

var vType = "";
var vUrl = "";
var assetsDB = { assetDataDB: [] };

// Set pie chart options
var pieChartOptions = {
	'title' : '',
	'is3D' : true,
	legend : {
		position : 'top'
	},
	hAxis : {
		'title' : '',
		textStyle : {
			color : '#005500',
			fontSize : '12',
			paddingRight : '10',
			marginRight : '10'
		}
	}
};

//Set pie chart options
var pieChartOptionsForIS = {
	'title' : '',
	'is3D' : true,chartArea: {left:0,width:"79%"},
	legend : {
		position : 'right'
	},
	hAxis : {
		'title' : '',
		textStyle : {
			color : '#005500',
			fontSize : '12'
		}
	}
};

// Set bar chart options
var barChartOptions = {
		legend : 'none','chartArea':{left:"23%",top:20,width:"70%"}, 
		hAxis : {
			'title' : '',
			textStyle : {
				fontSize : '12'
							}
		}
	};

var barChartOptionsForBL = {
		legend : 'none','chartArea':{left:"50%",top:20,width:"70%"},colors:['#990066'],
		hAxis : {
			'title' : '',
			textStyle : {
				color : '#005500',
				fontSize : '12'
							}
		}
	};

//var barChartOptions = {'title':title,'width':w,'height':h,'chartArea':{left:0,top:10,width:"100%"}};

// Goal input dialog configuration
var optGoalInput = {
		  autoOpen: false,
		  height: 300,
		  width: 300,
		  modal: true ,
		  position: "center",
		  close: function () {
			
			  $("#addElements").children(this).remove();
			  $(this).dialog('close');
			  }
	};
	
// Asset list dialog configuration	
var optAssetList = {
		  autoOpen: false,
		  height: 400,
		  width: 300,
		  modal: true ,
		  position: "center",
		  close: function () {
					
				$("#trAssetIdHead").remove();			
				for(i=0; i<$.jStorage.get("jsAstCount"); i++)
				{			
					$("#trAssetId"+ i).remove();
				}						
				
				$(this).dialog('close');
			  }
	};	
	  
// Ajax call to get all the dashboard services
$(document).ready(function() 
{
	if(($.jStorage.get("jsUserId") === "" || $.jStorage.get("jsUserId") === null || $.jStorage.get("jsPwd") === "" 
		|| $.jStorage.get("jsPwd") === null)&& ($.jStorage.get("jsEntpId")=== "" || $.jStorage.get("jsEntpId")=== null ))
	{
		window.location.href = "login.html";
	}
	else
	{
		//getting asset details of the enterprise
		getCurrentAssets();
		$.jStorage.set("jsAstCount", assetsDB.assetDataDB.length);
	
		$('#btnConfAssets').click(function () { 
			var id = $.jStorage.get("jsEntpId");
			document.frmAssetEdit.setEntp.value = id;
		 });
		
		
		$("#btnShowAssets").bind("click", addText);	
		$("#btnConfAssets").bind("click", goToAssetEdit);	
		$("#serverAsset").bind("click", serverList);
		$("#btnAsset").bind("click", submitGoalInput);
		
		
		$("#entpName").text( " Hello " + $.jStorage.get("jsEName"));
		
		var vType = "GET";						
		// call to the dashboard services are made
		var vUrl = $.jStorage.get("jsDBUrl") + "DashboardServices/getdashboard?entpId=" + $.jStorage.get("jsEntpId");

		$.ajax
		({
			type:vType,
			contentType: "application/json",
			url:vUrl,
			async:false,
			dataType: "json",
			success:function(json)
			{		
				    $.each(json, function(i, v)
					{
						
						if(json[i]["charttype"] == "plain"){
							showPlainText(json[i]["data"], json[i]["divId"]);
						}
						else {
							var rowLength =  json[i].data.rows.length;
							if(json[i]["charttype"] == "pie" )
								showPieChart(json[i]["data"], json[i]["divId"],rowLength);			            	  
							else if(json[i]["charttype"] == "bar")
								showBarChart(json[i]["data"], json[i]["divId"],rowLength);
							else if(json[i]["charttype"] == "line")
								showLineChart(json[i]["data"], json[i]["divId"],rowLength);	
							}
					}
					);	 	                           				
				
		
			},
			error : function(json) {
				var obj = eval('(' + json.responseText + ')');	
				
				$.each(obj, function(i, v){
					
					if(obj[i]["charttype"] == "plain"){
						showPlainText(obj[i]["data"], obj[i]["divId"]);
					}
					else {
						var rowLength =  obj[i].data.rows.length;
						if(obj[i]["charttype"] == "pie" )
							showPieChart(obj[i]["data"], obj[i]["divId"],rowLength);			            	  
						else if(obj[i]["charttype"] == "bar")
							showBarChart(obj[i]["data"], obj[i]["divId"],rowLength);
						else if(obj[i]["charttype"] == "line")
							showLineChart(obj[i]["data"], obj[i]["divId"],rowLength);	
						}
				});	 
			}
		});	//end of ajax
		
		getPUE();
		loadGoals();
	}	
});


//function to display text in the specified div location
function showPlainText(plainText, divID)
{
		$("#" + divID).text(plainText);
}

//function to navigate to the asset_edit.jsp
function goToAssetEdit()
{
	document.frmAssetEdit.setEntp.value = $.jStorage.get("jsEntpId");
}

//function to display PUE 
function getPUE()
{
	var vType = "GET";
	var vUrl = $.jStorage.get("jsDBUrl") + "procedure/getproc?entpId=" + $.jStorage.get("jsEntpId");

	$.ajax
	({
		type:vType,
		contentType: "application/json",
		url:vUrl,
		async:false,
		dataType: "json",
		success:function(json)
		{		
			$("#div_pue").text(json);			
		},
		error:function(json)
		{
			alert("Error loading PUE data!");			
 
		}	 
	});	//end of ajax	
}

//function to load goals to the dashboard dropdown
function loadGoals()
{
	var vType = "GET";
	var vUrl = $.jStorage.get("jsDBUrl") + "goalmaster/goals";
	
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
		
			$.each(json[0]["goalData"], function(i,n)
			{
				//vValues = vValues + '<option value = "'+ json[0]["goalData"][i]["goalId"] + '" >' 
					//+ json[0]["goalData"][i]["goalDescr"] + '</option>';	
				
				vValues = vValues + '<li><a href="#" onClick="checkGoalInput(\''+ json[0]["goalData"][i]["goalId"] +"')\">"
						 + json[0]["goalData"][i]["goalDescr"] + "</a></li>";
				

                
			});
			
			$("#cmbGoals").append(vValues); 			
					
		},
		error:function(json)
		{
			alert("Error from loading goals list!");			
		}	 
	});	//end of ajax		
}

function serverList()
{
  
	window.open("server_list.jsp?entpId="+ $.jStorage.get("jsEntpId") ,"serverlist","right=2000,top=20,toolbar=no, " +
				"status=no,location=no,	menubar=no, scrollbars=yes, resizable=no, width=400, height=400");
  
}


//opening goal input dialog based on the goal id in goal_input table
function checkGoalInput(goalId)
{
	$.jStorage.set("goalId", goalId);
	var vType = "GET";
	var vUrl = $.jStorage.get("jsDBUrl") + "GoalInputServices/getGoalInput?goalId=" + goalId;
						
	$.ajax
	({
		type:vType,
		contentType: "application/json",
		url:vUrl,
		async:false,
		dataType: "json",
		success:function(json)
		{		
			var len = json.length;
			var vValues = "";
			if(len>0)
			{		
				
				$.each(json, function(i, v)
				{
					vValues = vValues + ' <label for = "' + json[i]["descr"] + '" > ' + json[i]["descr"] + ' </label> ';
					
	 				if(json[i]["dtvalue"] === "date")
						vValues = vValues + ' <input type="text" name="' + json[i]["descr"] + '" id="' + json[i]["descr"] + '" disabled="disabled"  /> ';	   						
					else					
						vValues = vValues + ' <input type="text" name="' + json[i]["descr"] + '" id="' + json[i]["descr"] + '"  /> ';
				});	 

				$("#addElements").append(vValues);	
				
				$.each(json, function(i, v)
				{
	 				if(json[i]["dtvalue"] === "date")
					{
	 					alert('hai');
						$("#"+json[i]["descr"]).datepicker({ dateFormat: 'yy-mm-dd' ,
							showOn: "button", buttonImage: "images/calendar.gif", buttonImageOnly: true }) ; 						
					} 
				});	 				
	 
				$("#dlgGoalInput").dialog(optGoalInput).dialog('open');		
				
			}
			else
			{
				//TODO navigate to goal.jsp page
				window.location.href = "goal.jsp?goalId="+$.jStorage.get("goalId")+"&entpId="+$.jStorage.get("jsEntpId");
			}
		
		},
		error:function(json)
		{
			alert("Error from loading goals list!");			
		}	 
	});	//end of ajax			
}


// opening the asset list dialog to copy the selected assets
//function openAssetList()
//{
//	var vValues = "";
//	
//	$("#txtSelAssets").val("");
//	
//	vValues = vValues + '<table><thead><tr id = "trAssetIdHead"><th>Action</th><th>Asset ID</th></tr></thead><tbody>'
//	$.each(assetsDB.assetDataDB, function(i, n)
//	{
//		vValues = vValues + '<tr id = "trAssetId' + i + '"><td><input type = "checkbox"  id = "chkAssetId' + i + '" value = "' +  assetsDB.assetDataDB[i].assetId  + '"/></td>';
//		vValues = vValues + '<td>' + assetsDB.assetDataDB[i].assetId  + '</td></tr>';							
//	});		
//	
//	vValues = vValues + '</tbody></table>';   
//	
//	$("#spnAssetList").append(vValues);
//	$("#dlgAssetList").dialog(optAssetList).dialog('open');	
//				
//}

//concatenating asset IDs on selection to copy past from asset list dialog box
function addText()
{
	$("#txtSelAssets").val("");
	var i =0;
	var txt = "";
	var len = assetsDB.assetDataDB.length;

	for(i=0; i<len; i++)
	{
		if($("#chkAssetId"+i).is(':checked'))
		{
			txt = txt + $("#chkAssetId"+i).val() + ',';
		}
	}
	
	//truncating the last comma
	if($.trim(txt) != "" && txt.length > 2)
		$("#txtSelAssets").val(txt.substring(0, txt.length-1));
}

// submiting the goal input values to task page
function submitGoalInput()
{
	var vType = "GET";
	var vUrl = $.jStorage.get("jsDBUrl") + "GoalInputServices/getGoalInput?goalId=" + 	$.jStorage.get("goalId");
	
	var vGoalInputs = "";
	
	var aryGoalInputs = [];

	$.ajax
	({
		type:vType,
		contentType: "application/json",
		url:vUrl,
		async:false,
		dataType: "json",
		success:function(json)
		{		
			var len = json.length;
			if(len>0)
			{		
				$.each(json, function(i, v)
				{
					if($.trim($("#"+json[i]["descr"]).val()) === "")
					{
						alert("Enter all input values");
						$("#"+json[i]["descr"]).select();
						return false;
					}
					else
					{
						aryGoalInputs.push({
							text: json[i]["colHoldr"],
							value: $("#"+json[i]["descr"]).val()
            				});
						
						if ( vGoalInputs == "" ) {
							vGoalInputs = vGoalInputs + json[i]["colHoldr"] + "=" + $("#"+json[i]["descr"]).val();
						}
						else {
							vGoalInputs = vGoalInputs + "~"+ json[i]["colHoldr"] + "=" + $("#"+json[i]["descr"]).val();	
						}
						
					}
				});	 
								
				$("#dlgGoalInput").dialog(optGoalInput).dialog('close');		
				window.location.href = "goal.jsp?goalId="+$.jStorage.get("goalId")+"&entpId="+$.jStorage.get("jsEntpId")+"&goalInputs="+vGoalInputs;
				
			}	
		},
		error:function(json)
		{
			alert("Error from loading goals list!");			
		}	 
	});	//end of ajax		
}



//function setGoal(e)
//{
//	window
//	.open(
//			'goal.jsp',
//			"GoalTasks",
//			"right=2000,top=20,toolbar=no, "
//					+ "status=no,location=no,	menubar=no, scrollbars=yes, resizable=no, width=900, height=900");
//	}
