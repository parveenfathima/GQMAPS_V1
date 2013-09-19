$.jStorage.set("jsMeters", "");

var vType = "";	
var vUrl = "";

// Set pie chart options
var pieChartOptions = {'title':'',
			   'is3D': true,
			   'width':230,
			   'height':180};

// Set bar chart options			   
var barChartOptions = {'title':'',
   'width':200,
   'height':180};

// Ajax call to get all the dashboard services
$(document).ready(function() 
{
	if($.jStorage.get("jsUserId") === "" || $.jStorage.get("jsUserId") === null || $.jStorage.get("jsPwd") === "" || $.jStorage.get("jsPwd") === null)
	{
		window.location.href = "login.html";
	}
	else
	{
		$('#btnConfAssets').click(function () { 
			var id = $.jStorage.get("jsEntpId");
			document.frmAssetEdit.setEntp.value = id;
		 });
		
		$("#btnConfAssets").bind("click", goToAssetEdit);
		
		$("#entpName").text("Customer Dashboard for " + $.jStorage.get("jsEName"));
		vType = "GET";						
		
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
