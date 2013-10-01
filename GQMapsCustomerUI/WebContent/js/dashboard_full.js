$.jStorage.set("jsMeters", "");
$.jStorage.set("jsAstCount", 0);
$.jStorage.set("goalId", "");


var vType = "";	
var vUrl = "";
var assetsDB = { assetDataDB: [] };
// Set pie chart options
var pieChartOptions = {'title':'',
			   'is3D': true,
			   'width':230,
			   'height':180};

// Set bar chart options			   
var barChartOptions = {'title':'',
   'width':200,
   'height':180};

// Goal input dialog configuration
var optGoalInput = {
		  autoOpen: false,
		  height: "auto",
		  width: "auto",
		  modal: true ,
		  position: "center",
		  close: function () {
			
			  $("#addElements").children(this).remove();
			  $(this).dialog('close');
			  }
	};
	
var myPos = [ $(window).width() / 2, 50 ];
	
// Asset list dialog configuration	
var optAssetList = {
		  autoOpen: false,
		  height: "auto",
		  width: "auto",
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
	if($.jStorage.get("jsUserId") === "" || $.jStorage.get("jsUserId") === null || $.jStorage.get("jsPwd") === "" || $.jStorage.get("jsPwd") === null)
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
		$("#openAsset").bind("click", openAssetList);
		$("#btnAsset").bind("click", submitGoalInput);
		
		
		$("#entpName").text("Customer Dashboard for " + $.jStorage.get("jsEName"));
		
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
					if(json[i]["charttype"] === "pie")
						showPieChart(json[i]["data"], json[i]["divId"]);
					else if(json[i]["charttype"] === "bar")
						showBarChart(json[i]["data"], json[i]["divId"]);
					else if(json[i]["charttype"] === "line")
						showLineChart(json[i]["data"], json[i]["divId"]);					
					else if(json[i]["charttype"] === "plain")
						showPlainText(json[i]["data"], json[i]["divId"]);
				});	 	                           
				
				
			},
			error:function(json)
			{
				var obj = eval('(' + json.responseText + ')');	
				
				$.each(obj, function(i, v){
					if(obj[i]["charttype"] === "pie")
						showPieChart(obj[i]["data"], obj[i]["divId"]);
					else if(obj[i]["charttype"] === "bar") 
						showBarChart(obj[i]["data"], obj[i]["divId"]);
					else if(obj[i]["charttype"] === "line")
						showLineChart(obj[i]["data"], obj[i]["divId"]);										 
					else if(obj[i]["charttype"] === "plain")
						showPlainText(obj[i]["data"], obj[i]["divId"]);	 
				});	 
			}	 
		});	//end of ajax
		
		getPUE();
		loadGoals();
	}
	
});

//function to diplay bar chart in the specified div location
function showBarChart(barData, divId)
{					
	  // Set a callback to run when the Google Visualization API is loaded.
	  google.setOnLoadCallback(drawChart);
	
	  var data = new google.visualization.DataTable(barData);
	  // Callback that creates and populates a data table,
	  // instantiates the pie chart, passes in the data and
	  // draws it.
	  
	  function drawChart() 
	  {
		// Instantiate and draw our chart, passing in some options.
		var chart = new google.visualization.BarChart(document.getElementById(divId));
		chart.draw(data, barChartOptions);
	  }
}

//function to diplay pie chart in the specified div location
function showPieChart(pieData, divId)
{					
	  google.setOnLoadCallback(drawChart);
	  var data = new google.visualization.DataTable(pieData);
  
	  function drawChart() 
	  {
			var chart = new google.visualization.PieChart(document.getElementById(divId));
			chart.draw(data, pieChartOptions);
	  }
}

//function to diplay line chart in the specified div location
function showLineChart(lineData, divId)
{					  
	  function drawATLChart() 
	  {
		  var data = new google.visualization.DataTable(lineData);

		  var annotatedtimeline = new google.visualization.AnnotatedTimeLine(
		  document.getElementById(divId));
		  
		  annotatedtimeline.draw(data, 
					{'displayAnnotations': true,
					lineWidth: 1,
					'fill': 0,
					title:'', 
					titleY: '',
					displayLegendValues:true,
					displayZoomButtons:false,
					legendPosition:'newRow',
					dateFormat:'EEE, MMM d,yy HH:mm',
					'colors': ['green', 'red', 'orange'],
					'displayRangeSelector' : false
					}
					);
		  }
		  
		  google.setOnLoadCallback(drawATLChart);		
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
			//alert("inside PUE success");	
			$("#div_pue").text("Current month PUE is " + json);			
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
				vValues = vValues + '<option value = "'+ json[0]["goalData"][i]["goalId"] + '" >' + json[0]["goalData"][i]["goalDescr"] + '</option>';	
			});
			
			$("#cmbGoals").append(vValues); 			
					
		},
		error:function(json)
		{
			alert("Error from loading goals list!");			
		}	 
	});	//end of ajax		
}

function gotoAssetList(e)
{
	//window.location.href = "server_list.jsp";
	window.open('server_list.jsp',"serverlist","right=2000,top=20,toolbar=no, " +
				"status=no,location=no,	menubar=no, scrollbars=yes, resizable=no, width=400, height=400");
	//ServerLIst.document.write('My PDF File Title');
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
			//alert("length of json is : " + json.length+ "   inside checkGoalInput for the goal:   " + goalId); 
			var len = json.length;
			var vValues = "";
			if(len>0)
			{		
				
				$.each(json, function(i, v)
				{
					vValues = vValues + ' <label for = "' + json[i]["descr"] + '" > ' + json[i]["descr"] + ' </label> ';
					
					if(json[i]["dtvalue"] === "string")
					{
						vValues = vValues + ' <input type="text" name="' + json[i]["descr"] + '" id="' + json[i]["descr"] + '"  /> ';
					}
					else if(json[i]["dtvalue"] === "date")
					{
						vValues = vValues + ' <input type="text" name="' + json[i]["descr"] + '" id="' + json[i]["descr"] + '"  /> ';						
					}

				});	 

				$("#addElements").append(vValues);	
				$("#dlgGoalInput").dialog(optGoalInput).dialog('open');		
				
			}
			else
			{
				//TODO navigate to task asset page
				window.location.href = "login.html";
			}
		
		},
		error:function(json)
		{
			alert("Error from loading goals list!");			
		}	 
	});	//end of ajax			
}

// opening the asset list dialog to copy the selected assets
function openAssetList()
{
	var vValues = "";
	
	$("#txtSelAssets").val("");
	
	vValues = vValues + '<table><thead><tr id = "trAssetIdHead"><th>Action</th><th>Asset ID</th></tr></thead><tbody>'
	$.each(assetsDB.assetDataDB, function(i, n)
	{
		vValues = vValues + '<tr id = "trAssetId' + i + '"><td><input type = "checkbox"  id = "chkAssetId' + i + '" value = "' +  assetsDB.assetDataDB[i].assetId  + '"/></td>';
		vValues = vValues + '<td  >' + assetsDB.assetDataDB[i].assetId  + '</td> </tr>';							
	});		
	
	vValues = vValues + '</tbody></table>';   
	
	$("#spnAssetList").append(vValues);
	
	$("#dlgAssetList").dialog(optAssetList).dialog('open');	
				
}

//concatenating asset IDs on selection to copy past from asset list dialog box
function addText()
{
	$("#txtSelAssets").val("");
	var i =0;
	var txt = ""
	var len = assetsDB.assetDataDB.length;

	for(i=0; i<len; i++)
	{
		if($("#chkAssetId"+i).is(':checked'))
		{
			txt = txt + $("#chkAssetId"+i).val() + ',';
		}
	}
	
	if($.trim(txt) != "" && txt.length > 2)
		$("#txtSelAssets").val(txt.substring(0, txt.length-1));
}

// submiting the goal input values to task page
function submitGoalInput()
{
	var vType = "GET";
	var vUrl = $.jStorage.get("jsDBUrl") + "GoalInputServices/getGoalInput?goalId=" + 	$.jStorage.get("goalId");
	
	var vValues = "";
	
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
					//alert($("#"+json[i]["descr"]).val());
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
					}
				});	 
				
				alert("final text length to be submitted is :" + aryGoalInputs.length + "  " + JSON.stringify(aryGoalInputs));
			}	
		},
		error:function(json)
		{
			alert("Error from loading goals list!");			
		}	 
	});	//end of ajax		
}

