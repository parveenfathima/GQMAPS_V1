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
		if (divId == 'div_blacklistedSW'){
			chart.draw(data, barChartOptionsForBL);
		}
		else {
			chart.draw(data, barChartOptions);
		}	
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
			
			if ( divId == 'div_installedSW' ) {
				chart.draw(data, pieChartOptionsForIS);
			}
			
			else {
				chart.draw(data, pieChartOptions);
			}
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