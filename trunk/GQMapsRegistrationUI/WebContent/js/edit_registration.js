// JavaScript Document

//datepicker 
$(document).ready(function() 
{
	$("#txtNewExpiry").datepicker({
		minDate: 0
	});
	
	 $("#accordion").hide();
	
});

function buttonFormatter(row, cell, value, columnDef, dataContext)
{  
	var button = "<input type='button' class='btnEdit' id='create-user' value = 'Edit' />";
    return button;
}

function callbackmethod(row) 
{
//	alert(data[row].eid);
	alert(data[row].ename);
}


function btnMeterFormatter(row, cell, value, columnDef, dataContext)
{  
	var button = "<input type='button' class='btnMeter' id='btnMeter' value = 'Meter' onclick='javascript:callbackmethod2("+row+");' />";
    return button;
}

function callbackmethod2(row) 
{
//	alert(data[row].eid);
	alert(data[row].ename);
}

function btnValidityFormatter(row, cell, value, columnDef, dataContext)
{  
	var button = "<input type='button' class='btnValidity' id='btnValidity' value = 'Validity' onclick='javascript:callbackmethod3("+row+");' />";
    return button;
}

function callbackmethod3(row) 
{
//	alert(data[row].eid);
	alert(data[row].ename);
	window.location.href = "login.html";
}

//dynamic grid for listing the enterprises
var gridNewEntList, gridRegEntList;
var data = [];
var columns = [
  {id: "action", name: "Action", field: "action", formatter: buttonFormatter, width:50},
  {id: "meter", name: "Meter", field: "meter", formatter: btnMeterFormatter, width:70},
  {id: "validity", name: "Validity", field: "validity", formatter: btnValidityFormatter, width:70},  
  {id: "eid", name: "EID", field: "eid"},
  {id: "ename", name: "EName", field: "ename"},
  {id: "uid", name: "UID", field: "uid"},
  {id: "pwd", name: "Password", field: "pwd"}

];
var countCol = 0;
var options = {
  enableCellNavigation: true,
  enableColumnReorder: false
};

$(function () {

  for (var i = 0; i < 10; i++) {
	data[i] = {
	  action: "button",
	  meter: "button",
	  validity: "button",
	  eid: "E" + i,
	  ename: "NE02/04/2013",
	  uid: "U" + i,
	  pwd: "********"
	};
  }
  
  var dataView = new Slick.Data.DataView();
  
  gridNewEntList = new Slick.Grid("#grdNewEList", data, columns, options);
  gridRegEntList = new Slick.Grid("#grdRegEList", data, columns, options);
   
});


///dynamic grid for listing the meters of the selected enterprise

var gridMeterList;
var meterColumns = [
  {id: "mid", name: "Meter ID", field: "mid"},
  {id: "protocol", name: "Protocol", field: "protocol"},
  {id: "phone", name: "Phone", field: "phone"},
  {id: "address", name: "Address", field: "address"},
  {id: "description", name: "Description", field: "description", width: 230}
];

$(function () {
  var meterData = [];
  for (var i = 0; i < 1; i++) 
  {
	meterData[i] = {
	  mid: "1",
	  protocol: "Computer",
	  phone: "22222222",
	  address: "Chennai",
	  description: "Purchase of computer meter"
	};
  }
  gridMeterList = new Slick.Grid("#grdMeterList", meterData, meterColumns, options);
 
})

//entAccordion





//dialog

/* $(function() {
$( "#dialog-form" ).dialog();
}); */


$( "#submit" ).button().click(function() {
$( "#dialog-form" ).dialog( "open" );
});
		