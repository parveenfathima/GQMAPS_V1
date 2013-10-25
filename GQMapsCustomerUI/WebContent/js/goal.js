// Set pie chart options
var pieChartOptions = {'title':'',
			   'is3D': true,
			   'width':350,
			   'height':200};

// Set bar chart options			   
var barChartOptions = {'title':'',
   'width':300,
   'height':200};


function showGoalGraphs(){
	//alert("inside show goal graph function , # of tasks = " + document.getElementById('taskCount').value);

	for (var j=0 ; j < document.getElementById('taskCount').value;j++){

		var cdData = 'chartData_'+ j;
		var cdDiv = 'chartDiv_'+ j;
		var cdType = 'chartType_'+ j;
		
//		alert( 'chartData_'+ j+ ' = ' + document.getElementById(cdData).value
//			+ '  ,  chartDiv_'+ j+ ' = ' + document.getElementById(cdDiv).id
//			+ '  ,  chartType_'+ j+ ' = <' + document.getElementById(cdType).value +'>'
//		);
		
		if (document.getElementById(cdType).value == "pie"){
			showGoalPieChart( document.getElementById(cdData).value  , document.getElementById(cdDiv).id );
		}
		else  if ( document.getElementById(cdType).value == "bar"){
			showGoalBarChart ( document.getElementById(cdData).value  , document.getElementById(cdDiv).id );
		}
		else  if ( document.getElementById(cdType).value == "line"){
			showGoalATLChart( document.getElementById(cdData).value  , document.getElementById(cdDiv).id );
		}
		else  if ( document.getElementById(cdType).value == "plain"){
			showGoalPlainChart(document.getElementById(cdData).value , document.getElementById(cdDiv).id);
		}
		else {
			
		}
		
	}
}

function showGoalBarChart(barData, divId)
{
	//alert("inside bar chart");
	var jsarray = JSON.parse(barData);		
	//alert("jsarray" + jsarray);
		var data = new google.visualization.DataTable(jsarray);
	//alert("data" + data);
		var chart = new google.visualization.BarChart(document.getElementById(divId));
		chart.draw(data, barChartOptions);
	
}


function showGoalPieChart(pieData,divId)
{	 
	//alert("inside pie chart");
	var jsarray = JSON.parse(pieData);
	var data = new google.visualization.DataTable( jsarray );
	
	var chart = new google.visualization.PieChart(document.getElementById(divId));
	chart.draw(data, pieChartOptions);
}

function dateReviver(key, value) {
	//alert("dateReviver key = "+ key +" , val = "+ value);
    var a;
    if (typeof value === 'string') {
    	
        a = /^(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2}(?:\.\d*)?)$/.exec(value);
        if (a) {
            return new Date(Date.UTC(+a[1], +a[2] - 1, +a[3], +a[4],
                            +a[5], +a[6]));
        }
    }
    return value;
	
}
	

function showGoalATLChart(lineData, divId){	
	
//	alert(" Line data " + lineData );
	
//	if(lineData.charAt(0) === '"')
//		lineData = lineData.substr(1);
	
//	alert(" Line data 2 " + lineData );
	
	lineData = lineData.substr(1,lineData.length-2);
//	alert(" Line data 3 " + lineData );

	lineData = lineData.replace(/\\/g,"");
//	alert('string sl c re = ' + lineData );
	var jsarray = JSON.parse(lineData,dateReviver);
	
//	alert("showGoalATLChart "+ JSON.stringify(jsarray));
	
	var data = new google.visualization.DataTable(jsarray);
//	alert("showGoalATLChart 2 data " + data );
	
	var annotatedtimeline = new google.visualization.AnnotatedTimeLine(document.getElementById(divId));
		//alert("data" + annotatedtimeline);
	annotatedtimeline.draw(data, {'displayAnnotations': true,
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
		});
	

}

//function to display text in the specified div location
function showGoalPlainChart(plainText, divId)
{
	//alert( 'showPlainChart , chartData = ' + plainText 	+ '  ,  chartDiv_ = ' + divId );
	document.getElementById(divId).innerHTML = plainText;
	 
}