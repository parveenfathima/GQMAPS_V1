$(document).ready(function() 
{
	
});

function drawGooglePieBarChart(data, name, title)
{
	  google.load('visualization', '1.0', {'packages':['corechart']});

	  google.setOnLoadCallback(drawChart);
	  
	  function drawChart() 
	  {
		  	var chartOptions = null;
			var chart = null;
			
			if(name === "pie")
			{
				chartOptions = {'title':title,
							   'is3D': true,
							   'width':350,
							   'height':200};
							   	
				chart = new google.visualization.PieChart(document.getElementById('pie-chart_div'));							   			
			}
			else if(name === "bar")
			{
				chartOptions = {'title':'Power consumption by asset type',
							   'width':300,
							   'height':200};
							   		
				chart = new google.visualization.BarChart(document.getElementById('bar-chart_div'));							   		
			}

			chart.draw(data, chartOptions);						   
	   }	
		
}

function drawGoogleLineChart(data, name, title)
{
		google.load('visualization', '1', {packages: ['annotatedtimeline']});
		
		function drawATLChart() 
		{
		
			var annotatedtimeline = new google.visualization.AnnotatedTimeLine(
			document.getElementById('ATLChart_Div'));
			
			annotatedtimeline.draw(data, 
					  {'displayAnnotations': true,
					  lineWidth: 1,
					  'fill': 0,
					  title: title, 
					  titleY: 'Dollars (Millions)',
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

function loadPieChart()
{
			var jsonPieData = "";
			var vType = "GET";								
			
			//var vUrl = $.jStorage.get("jsDBUrl") + "";	
			var vUrl = "http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/Piechart/getPiechart";
			
			$.ajax
			({
				type:vType,
				contentType: "application/json",
				url:vUrl,
				async:false,
				dataType: "json",
				success:function(json)
				{
					alert("inside success");
					//drawGooglePieBarChart(json["data"], json["name"], json["title"]);
					
					//ajax return the DataTable object
					
				   // Load the Visualization API and the piechart package.
					google.load('visualization', '1.0', {'packages':['corechart']});
			  
					// Set a callback to run when the Google Visualization API is loaded.
					google.setOnLoadCallback(drawChart);
			  
					// Callback that creates and populates a data table,
					// instantiates the pie chart, passes in the data and
					// draws it.
					function drawChart() 
					{
	
						var data = new google.visualization.DataTable(json["data"]);
						
						// Set chart options
						var options = {'title':json["title"],
									   'is3D': true,
									   'width':350,
									   'height':200};
				
						// Instantiate and draw our chart, passing in some options.
						var chart = new google.visualization.PieChart(document.getElementById('divChart'));
						chart.draw(data, options);
					 }		
					 			                  
				},
				error:function(json)
				{
					alert("Error from loading chart data: " + json.status + " " + json.responseText);
				} 
			});	 
}
