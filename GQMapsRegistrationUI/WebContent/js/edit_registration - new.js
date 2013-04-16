$(document).ready(function() 
{

	/*    
	$("#txtNewExpiry").focus(function() {
        $("#txtNewExpiry").datepicker("show");
    });  */
	
	$( "#btnGeneral").bind("click", openGeneralDialog);
	$( "#btnMeter").bind("click", openMeterDialog);
	$( "#btnValidity").bind("click", openValidityDialog);
	$( "#txtNewExpiry").bind("focusin", showDate);
});

function showDate()
{
	$("#txtNewExpiry").datepicker({
		minDate: 0
	});
}

function openGeneralDialog()
{
	$( "#dlgGeneral" ).dialog( "open" );
}

function openMeterDialog()
{
	$( "#dlgMeter" ).dialog( "open" );
}

function openValidityDialog()
{
	$( "#dlgValidity" ).dialog( "open" );
	$('#txtNewExpiry').blur(); 
}

//General form dialog creation
$(function() {
	  $( "#dlgGeneral" ).dialog({
		  autoOpen: false,
		  height: 435,
		  width: 300,
		  modal: true,
		  position: "center"
	  });		  
});

//Meter form dialog creation
$(function() {
	  $( "#dlgMeter" ).dialog({
		  autoOpen: false,
		  height: 502,
		  width: 300,
		  modal: true,
		  position: "center"
	  });		  
});

//Validity form dialog creation
$(function() {
	  $( "#dlgValidity" ).dialog({
		  autoOpen: false,
		  height: 365,
		  width: 300,
		  modal: true,
		  position: "center"
	  });
});