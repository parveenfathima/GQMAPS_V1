// JavaScript Document


function buttonFormatter(row,cell,value,columnDef,dataContext)
{  
   	 // var button = "<input type='button' class='btnEdit' id='btnEdit' value = 'Edit' onclick='javascript:callbackmethod()' />";
    //var button = "<input type='button' class='btnEdit' id='btnEdit" + countCol++ +"' onclick='javascript:callbackmethod("+countCol+")' value = 'Edit' />";
	var button = "<input type='button' class='btnEdit' id='btnEdit' value = 'Edit' onclick='javascript:callbackmethod("+row+");' />";
    return button;
}

/*$('.btnEdit').live('click', function( ){
	var EID = columns.push({  id: "eid", name: "EID", field: "eid" });
	alert(EID);
	});
*/	
function callbackmethod(row) 
{

	alert(row);
	//var selectedIndexes = grid.getSelectedRows();
	
	
}

/*
$("#btnEdit").click(function() {

               var selectedData = [],
            selectedIndexes;
            var m, el;
            var selectedIndexes = grid.getSelectedRows();
               // alert(selectedIndexes);
            jQuery.each(selectedIndexes, function (index, value) {
                m = grid.getData()[value];
                // var keys = Object.keys(m);
                //var el = m["admin_subscriber_id"];
                //  selectedData.push( el );
				alert(m);
            });
            alert(selectedData);
        })

*/



//dynamic grid for listing the enterprises
var gridNewEntList, gridRegEntList;
 var data = [];
var columns = [
  {id: "action", name: "Action", field: "action", formatter: buttonFormatter},
  {id: "eid", name: "EID", field: "eid"},
  {id: "ename", name: "EName", field: "ename"},
  {id: "uid", name: "UID", field: "uid"},
  {id: "pwd", name: "Password", field: "pwd"},
  {id: "savefwd", name: "SaveForward", field: "savefwd"},
  {id: "url", name: "URL", field: "url"}  
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
	  eid: "E" + i,
	  ename: "NE02/04/2013",
	  uid: "U" + i,
	  pwd: "********",
	  savefwd: "Forward",
	  url: "www.forwardurl.com"
	};
  }
  
  var dataView = new Slick.Data.DataView();
  
  gridNewEntList = new Slick.Grid("#grdNewEList", dataView, columns, options);
  gridRegEntList = new Slick.Grid("#grdRegEList", data, columns, options);
  
   
    gridNewEntList.onAddNewRow.subscribe(function (e, args) {
      var item = args.item;
	  alert(item + "   " +  grid.invalidateRow(data.length));
     // data.push(item);
     // grid.updateRowCount();
     // grid.render();
    });
   
})


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
  for (var i = 0; i < 1; i++) {
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

$(function() {
$( "#acdEditEntp" ).accordion();
});


//datepicker 
$(document).ready(function() 
{
	$("#txtNewExpiry").datepicker({
		minDate: 0
	});
});


