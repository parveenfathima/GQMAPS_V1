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

	for (var j=0 ; j < document.getElementById('taskCount').value;j++){

		var cdData = 'chartData_'+ j;
		var cdDiv = 'chartDiv_'+ j;
		var cdType = 'chartType_'+ j;
				
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
		else  if ( document.getElementById(cdType).value == "html"){
			showGoalHTML(document.getElementById(cdData).value , document.getElementById(cdDiv).id);
		}
		else {
			
		}
		
	}
}

function showGoalBarChart(barData, divId)
{
		var jsarray = JSON.parse(barData);		
		var data = new google.visualization.DataTable(jsarray);
		var chart = new google.visualization.BarChart(document.getElementById(divId));
		chart.draw(data, barChartOptions);	
}

function showGoalPieChart(pieData,divId)
{	 
	var jsarray = JSON.parse(pieData);
	var data = new google.visualization.DataTable( jsarray );
	
	var chart = new google.visualization.PieChart(document.getElementById(divId));
	chart.draw(data, pieChartOptions);
}

function dateReviver(key, value) {
    var a;
    if (typeof value === 'string') {
    	
        a = /^(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2}(?:\.\d*)?)$/.exec(value);
        if (a) {
            return new Date(+a[1], +a[2] - 1, +a[3], +a[4],
                            +a[5], +a[6]);
        }
    }
    return value;
    
}
	

function showGoalATLChart(lineData, divId){	
	
	lineData = lineData.substr(1,lineData.length-2);
	lineData = lineData.replace(/\\/g,"");
	
	var jsarray = JSON.parse(lineData,dateReviver);
	var data = new google.visualization.DataTable(jsarray);
	var annotatedtimeline = new google.visualization.AnnotatedTimeLine(document.getElementById(divId));
	
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
	document.getElementById(divId).innerHTML = plainText;	 
}

// function to display html sent from service
function showGoalHTML(htmlText, divId)
{
	document.getElementById(divId).innerHTML = htmlText;	 
}


//function to redirect Dashboard screen    
function gotoDashboard() {

	window.location.href="dashboard_full.html";

}
