
$.jStorage.set("jsMeters", "");

var vType = "GET";	
var vUrl = "";

// Set chart options
var pieChartOptions = {'title':'',
			   'is3D': true,
			   'width':350,
			   'height':200};
			   
 var barChartOptions = {'title':'Power consumption by asset type',
	 'width':300,
	 'height':200};

$(document).ready(function() 
{
	$("#entpName").text("Customer Dashboard for " + $.jStorage.get("jsEName"));
	vType = "GET";						
	//var vUrl = $.jStorage.get("jsDBUrl") + "charts/getAllCharts";	
	
	var vUrl = "http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/DashboardServices/getdashboard"

	$.ajax
	({
		type:vType,
		contentType: "application/json",
		url:vUrl,
		async:false,
		dataType: "json",
		success:function(json)
		{
			alert("inside pie success function: " + json );
			
			showBarChart(json[0]["data"], json[0]["divId"]);
			
			showBarChart(json[1]["data"], json[1]["divId"]);
			showPieChart(json[2]["data"], json[2]["divId"]);
			showPieChart(json[3]["data"], json[3]["divId"]);
			showBarChart(json[4]["data"], json[4]["divId"]);		s									
								   				
		},
		error:function(json)
		{
			alert("Error from loading google charts: " + json.status + " " + json.responseText);
		} 
	});
 
	
	//getDashboard();
	
});


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

function showPieChart(pieData, divId)
{					
	  // Set a callback to run when the Google Visualization API is loaded.
	  google.setOnLoadCallback(drawChart);
	
	  var data = new google.visualization.DataTable(pieData);
	  // Callback that creates and populates a data table,
	  // instantiates the pie chart, passes in the data and
	  // draws it.
	  
	  function drawChart() 
	  {
		// Instantiate and draw our chart, passing in some options.
		var chart = new google.visualization.PieChart(document.getElementById(divId));
		chart.draw(data, pieChartOptions);
	  }
}
