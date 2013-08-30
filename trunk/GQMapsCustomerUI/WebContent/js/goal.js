var vType = "GET";	
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

$(document).ready(function() 
{
	//$("#pGoalName").text("Goal: " + $.jStorage.get("jsGoalName"));
	loadTasks();
});


function loadTasks()
{
	//vUrl = $.jStorage.get("jsUrl") + "meterrun/getMeterRun?entpId=" + $.jStorage.get("jsEntpId");	
	vUrl = "http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/goalServices/goal?goalId=pue&entpId=talk";	
	//loadDashboard(vType, vUrl, "MeterCount");

	var vTaskList = "";
	$.ajax
	({
		type:vType,
		contentType: "application/json",
		url:vUrl,
		async:false,
		dataType: "json",
		success:function(json)
		{
			
			$.each(json[0] ["ChartData"], function(i, n){			
				vTaskList = vTaskList + "<tr bgcolor='#00CCFF'>";
                vTaskList = vTaskList + "<td>";
                vTaskList = vTaskList + "<div><h6>" + json[0]["ChartData"][i]["descr"] + "</h6></div>";
                vTaskList = vTaskList + "<div id='divChart" + i + "'> </div>";
				

				if(json[0]["ChartData"][i]["charttype"] === "pie")
					showPieChart(json[0]["ChartData"][i]["Data"], "divChart"+i);
				else if(json[0]["ChartData"][i]["charttype"] === "bar")
					showBarChart(json[0]["ChartData"][i]["Data"], "divChart"+i);
				else if(json[0]["ChartData"][i]["charttype"] === "line")
					showLineChart(json[0]["ChartData"][i]["Data"], "divChart"+i);					
				else if(json[0]["ChartData"][i]["charttype"] === "plain")
					showPlainText(json[0]["ChartData"][i]["data"], "divChart"+i);	
	
				
                vTaskList = vTaskList + "</td>";
                vTaskList = vTaskList + "<td><a href='asset_list.html'>Asset List</a></td>";
                vTaskList = vTaskList + "<td><input type = 'text' id = 'txtUserNotes"+ i + "'/> </td>";
                vTaskList = vTaskList + "<td><input type = 'text' id = 'txtCostBenefit"+ i + "'/> </td>";
				vTaskList = vTaskList + "<td><input type = 'checkbox' id = 'chkApply"+ i + "' name = 'chkApply'"+ i + "/> </td>";
                vTaskList = vTaskList + "</tr>";
        	});		
			
			alert(vTaskList);		
			$("#tblTaskList").append(vTaskList);		
				
		},
		error:function(json)
		{
			alert("Error from loading goal data: " + json.status + " " + json.responseText);
		} 
	});		
}


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
	$("#" + divID).appendChild(plainText);
}