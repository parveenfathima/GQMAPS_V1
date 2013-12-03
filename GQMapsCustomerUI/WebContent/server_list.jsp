
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="com.gq.meter.object.Asset"%>
<%@page import="com.gq.meter.object.DevCtlg"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.gq.cust.AssetHelper"%>

<!DOCTYPE>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link type="text/css"
	href="jquery-ui-1.10.2.custom/css/blitzer/jquery-ui-1.10.2.custom.css"
	rel="stylesheet" />
<link type="text/css" href="css/gqmaps.css" rel="stylesheet" />

<script src="jquery-ui-1.10.2.custom/js/jquery-1.9.1.js"></script>
<script src="jquery-ui-1.10.2.custom/js/jquery-ui-1.10.2.custom.js"></script>
<script src = "js/jstorage.js"> </script>   
<script src = "js/dashboard_full.js"> </script>
<script> 

	function handleClick(){
		
		var assetTable = document.getElementById("tblComputerList");
		var chk_arr =  document.getElementsByName("chkApply");
		var total = 0;
		var ipaddresses = '';
		
		for (var i = 1 ; i < assetTable.rows.length ; i++) {
			row = assetTable.rows[i];

 		    if ( row.getElementsByTagName('td')[0].getElementsByTagName('input')[0].checked ) {
	    
 		     	total = total+ parseInt(row.cells[3].innerHTML);
 		     	ipaddresses = ipaddresses + ',' + row.cells[2].innerHTML;
 		  	} 
		}//for ends

		document.getElementById('consolidateResult').innerHTML 
					= "Total value = " + total + ' , Ip Addr(s) ' + ipaddresses;
	} 	
	
</script>
<!--timeout css -->
<style type="text/css">
#idletimeout {
	background: #4E4E4E;
	border: 3px solid #4E4E4E;
	color: #fff;
	font-family: arial, sans-serif;
	text-align: center;
	font-size: 15px;
	padding: 10px;
	position: relative;
	top: 0px;
	left: 0;
	right: 0;
	z-index: 100000;
	display: none;
}

#idletimeout a {
	color: #fff;
	font-weight: bold
}

#idletimeout span {
	font-weight: bold
}
</style>

</head>

<body>
<div id="idletimeout" style="top: 150px; margin-left: 215px; margin-right: 200px; ">
        Logging off in <span><!-- countdown place holder --></span>&nbsp;seconds due to inactivity.
        <a id="idletimeout-resume" href="#">Click here to continue</a>.
    </div>  
	<h1>List of Servers</h1>
	<br> <h5> Please choose the asset and click on submit, copy the result and paste in the places wherever required</h5>
<% 	

 		int i = 0;
 		int j = 0;
 		int cost=0;
		String ctlgDesc = "";
		
		String enterpriseId = request.getParameter("entpId") ;
		// Asset list
		List<Asset> assetListDB = AssetHelper.getAssetList(enterpriseId);
		
		DevCtlg d =new DevCtlg();
 		List<DevCtlg> dcListDB = AssetHelper.getDevCtlgList(enterpriseId);
 					
%>

<form>
<div id="myDiv">
	<table id="tblComputerList" border="0">
		<tr bgcolor="green" style="color: white;">
			<th>Apply</th>
			<th>Asset ID</th>
			<th>IP Address</th>
			<th>Cost</th>
			<th>Descr</th>		
			
		</tr>
		
	<%
		for (i = 0; i < assetListDB.size(); i++) {
				  
			for (j = 0;j < dcListDB.size(); j++) {
				if ( dcListDB.get(j).getCtlgId().equals( assetListDB.get(i).getCtlgId() ) ) {
					if ( dcListDB.get(j).getMonthlyRent() == null ) {
						cost = 0;
					}
					else {
						cost = dcListDB.get(j).getMonthlyRent();	
					}
					 
					ctlgDesc = dcListDB.get(j).getDescr();
				}
			}
			 
		%>
		
		<tr>
			<td><input type="checkbox" id = "chkApply" ></td>
			<td><%=assetListDB.get(i).getAssetId()%></td>
			<td><%=assetListDB.get(i).getIpAddr()%></td>
			<td><%= cost %></td>
			<td><%= ctlgDesc %> </td>

 <% 
	}		
%>
</tr>
	</table>
</div>
		<input type="button" value="Submit" onclick="handleClick()"> 
		<label id="consolidateResult"> </label>
	</form>

</body>

</html>

<!-- Mask to cover the whole screen -->
<div id="mask"></div>

<script src="js/jquery.idletimer.js" type="text/javascript"></script>
<script src="js/jquery.idletimeout.js" type="text/javascript"></script>

<!--jQuery plugin to set session timeout -->
<script type="text/javascript">
    $.idleTimeout('#idletimeout', '#idletimeout a', {
		idleAfter : $.jStorage.get("jsTimeout"),
        onTimeout : function() {
            $(this).slideUp();
            window.location = "login.html";
        },
        onIdle : function() {
            var maskHeight = $(document).height();
            var maskWidth = $(window).width();
            $('#mask').css({'width':maskWidth,'height':maskHeight});
            $('#mask').fadeIn(1000);	
            $('#mask').fadeTo("slow",0.8);	
            $(this).slideDown(); // show the warning bar
        },
        onCountdown : function(counter) {
            $(this).find("span").html(counter); // update the counter
        },
        onResume : function() {
            $(this).slideUp(); // hide the warning bar
            $('#mask').hide();
            $('.window').hide();
        }
    });
</script>			
