
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
<script> 

	function handleClick(){
		
		var assetTable = document.getElementById("tblComputerList");
		var chk_arr =  document.getElementsByName("chkApply");
		var total = 0;
		var ipaddresses = '';
		//alert('row count including header = ' + assetTable.rows.length );
		
		for (var i = 1 ; i < assetTable.rows.length ; i++) {
			row = assetTable.rows[i];

 		    if ( row.getElementsByTagName('td')[0].getElementsByTagName('input')[0].checked ) {
//  		    	alert(  "ip = " +row.cells[2].innerHTML 
//  		    			+ ", cost = " + row.cells[3].innerHTML);
	    
 		     	total = total+ parseInt(row.cells[3].innerHTML);
 		     	ipaddresses = ipaddresses + ',' + row.cells[2].innerHTML;
 		  	} 
		}//for ends
		//alert("total = " + total + ' , ip addr ' + ipaddresses);
		document.getElementById('consolidateResult').innerHTML 
					= "Total value = " + total + ' , Ip Addr(s) ' + ipaddresses;
	} 	
	
</script>

</head>

<body>
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
