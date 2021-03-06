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
	
	backgroundColor: '#F4F4F4',
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
	'is3D' : true,
	chartArea: {left:0,width:"79%"},
	backgroundColor: '#F4F4F4',
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
	backgroundColor: '#F4F4F4',
	legend : 'none','chartArea':{left:"23%",top:20,width:"70%"}, 
	hAxis : {
		'title' : '',
		textStyle : {
		fontSize : '12'
		}}
};

var barChartOptionsForBL = {
	backgroundColor: '#F4F4F4',
	legend : 'none','chartArea':{left:"50%",top:20,width:"70%"},colors:['#990066'],
	hAxis : {
		'title' : '',
		textStyle : {
		color : '#005500',
		fontSize : '12'
		}}
};

// Goal input dialog configuration
var optGoalInput = {
	autoOpen: false,
	height: 300,
	width: 300,
	modal: true ,
	position: "center",
	title: "Goal Input Dialog",

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
	position: "center" ,	  
	
	close: function () {
		$("#trAssetIdHead").remove();			
		for(i = 0; i < $.jStorage.get("jsAstCount"); i++)
		{			
			$("#trAssetId"+ i).remove();
		}						
		$(this).dialog('close');
	}
};	
	  
// Ajax call to get all the dashboard services
$(document).ready(function() 
{
	$("#div_topcpuLoad").height(200);
	if( ($.jStorage.get("jsUserId") === "" || $.jStorage.get("jsUserId") === null || $.jStorage.get("jsPwd") === "" 
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
		
		// call to the dashboard services are made
		var vType = "GET";						
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
				});	 	                           				
			},
			error : function(json) {
				
				var obj = eval('(' + json.responseText + ')');	
				$.each(obj, function(i, v)
				{
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

//function to store compute duration new value.
function computeDurationSet(vol) 
{
	document.querySelector('#volume').value = vol;
	var rangeValue = document.getElementById('slide').value;
	document.getElementById('computeValue').value = rangeValue;
}

//function to show compute duration div 
function showComDurDiv()
{
	document.getElementById('ComDurDiv').style.display = "block";
}

//function to highlight the label
function labelHighLight() {
	document.getElementById('fader').style.color = "blue";
}

//function to redirect a dashboard screen when dynamically changed compute duration
function computeDurationUpdate() 
{
	var rangeValue = document.getElementById('computeValue').value;
	var vType = "PUT";
	var vUrl = $.jStorage.get("jsDBUrl") + "updcmpdur?entpId=" + $.jStorage.get("jsEntpId")+"&value=" +rangeValue;
	
	$.ajax
	({
		type:vType,
		contentType: "application/json",
		url:vUrl,
		async:false,
		
		success:function()
		{	
			window.location.reload();
		},
		error:function(json)
		{
			alert("Error from fetching Updated compute duration data!");			
 
		}	 
	});	//end of ajax	
}

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
	var vUrl = $.jStorage.get("jsDBUrl") + "goalmaster/goals?entpId=" + $.jStorage.get("jsEntpId");
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
	window.open("server_list.jsp?entpId=" + $.jStorage.get("jsEntpId") ,"serverlist","right=2000,top=20,toolbar=no, " +
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
					vValues = vValues + '<tr><td width="200%"> <label for = "' + json[i]["descr"] + '" > ' + json[i]["descr"] + ' </label></td> ';
					
	 				if(json[i]["dtvalue"] === "date") 
						vValues = vValues + '<td> <input type="date" name="' + json[i]["descr"] + '"  id="' + json[i]["descr"] + '" /></td></tr> ';
					else					
						vValues = vValues + '<td> <input type="text" name="' + json[i]["descr"] + '" id="' + json[i]["descr"] + '"  /></td></tr> ';
				});	 
				
				$("#addElements").append(vValues);	
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
	var vUrl = $.jStorage.get("jsDBUrl") + "GoalInputServices/getGoalInput?goalId=" + $.jStorage.get("goalId");
	
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
				window.location.href = "goal.jsp?goalId=" + $.jStorage.get("goalId")+"&entpId=" + $.jStorage.get("jsEntpId") + "&goalInputs=" + vGoalInputs;
				
			}	
		},
		error:function(json)
		{
			alert("Error from loading goals list!");			
		}	 
	});	//end of ajax		
}

//function to redirect Dashboard screen    
function gotoDashboard() {
	window.location.replace("dashboard_full.html");
}
