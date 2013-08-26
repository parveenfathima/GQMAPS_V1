$.jStorage.set("jsMeters", "");

var vType = "";	
var vUrl = "";

// Set pie chart options
var pieChartOptions = {'title':'',
			   'is3D': true,
			   'width':350,
			   'height':200};

// Set bar chart options			   
var barChartOptions = {'title':'',
   'width':300,
   'height':200};

// Ajax call to get all the dashboard services
$(document).ready(function() 
{
	alert($.jStorage.get("jsEntpId"));
	$("#entpName").text("Customer Dashboard for " + $.jStorage.get("jsEName"));
	vType = "GET";						
	
	//var vUrl = $.jStorage.get("jsDBUrl") + "DashboardServices/getdashboard?entpId=maps";	
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

			//alert(json);

			//alert(json[0]);			

			$.each(json, function(i, v){
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
			alert("Error from loading google charts: " + json.status + " " + json.responseText);	
				
			var obj = eval('(' + json.responseText + ')');
			
			alert(obj);
			
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
	});	
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
	if(plainText <=0)
		$("#" + divID).text("Meter expired, contact support!");
	else if(plainText >= 1 && plainText <= 10)
		$("#" + divID).text("Meter will get expired in " + plainText + " days!");
	else
		$("#" + divID).text("No alerts!");
}

//function to navigate to the asset_edit.jsp
function goToAssetEdit()
{
	$("#btnConfAssets").bind("click", function()
	{
		window.location.href = "asset_edit.html";
	});
}



function gotoAssetList()
{
	window.location.href = "asset_list.html";
}