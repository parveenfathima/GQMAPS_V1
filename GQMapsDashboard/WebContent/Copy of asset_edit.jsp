<%@page
	import="com.gq.AssetData, com.gq.ui.object.DomainData, java.util.*, com.gq.meter.object.Asset;"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


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
<script type="text/javascript">

</script>

</head>
<body>
	<h1>Asset Edit Page</h1>
	<%
		AssetData assetData = new AssetData();
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		Asset a = new Asset();

		List<Asset> assetListDB = new ArrayList<Asset>();
		assetListDB = assetData.getAssetList();
		System.out.println("Total assets: " + assetListDB.size());
		
		
	%>

	<h4>Computer Asset List</h4>
	<table id="tblComputerList" border="1">
		<tr bgcolor="green" style="color: white;">
			<th>S.#</th>
			<th>Status</th>
			<th>Asset ID</th>
			<th>IP Address</th>
			<th>Catalog</th>
			<th>Server Type</th>
			<th>Asset Usage</th>
			<th>Asset Importance</th>
			<th>Ownership</th>
			<th>Location</th>
		</tr>
		<%
			for (i = 0; i < assetListDB.size(); i++) {
				if (assetListDB.get(i).getProtocolId().equals("computer")) {
		%>
		<tr>
			<td><%=j = j + 1%></td>
			<td>
				<%
					if (assetListDB.get(i).getActive() == 'y') {
								
				%> 
						<select name="cmbStatus"+<%=i%> id="cmbStatus"+<%=i%> name="cmbStatus"+<%=i%> style="width: 50px">
							<option value="<%=assetListDB.get(i).getActive()%>" selected="selected"><%=assetListDB.get(i).getActive()%>	</option>
							<option value="n">n</option>
						</select> 
				<%
				 	} else if (assetListDB.get(i).getActive() == 'n') {
				 				System.out.println("true");
 				%> 
	 					<select name="cmbStatus"+<%=i%> id="cmbStatus"+<%=i%> name="cmbStatus"+<%=i%> style="width: 50px">
	 						<option value="y">y</option>
							<option value="<%=assetListDB.get(i).getActive()%>" selected="selected"> <%=assetListDB.get(i).getActive()%> </option>
						</select> 
				<%
 					}
 				%>
			</td>
			<td><%=assetListDB.get(i).getAssetId()%></td>
			<td><%=assetListDB.get(i).getIpAddr()%></td>
			<td><%=assetListDB.get(i).getCtlgId()%></td>
			<td><%=assetListDB.get(i).getSrvrAppId()%></td>
			<td><%=assetListDB.get(i).getAssetUsg()%></td>
			<td><%=assetListDB.get(i).getImpLvl()%></td>
			<td>
				<%
					if(assetListDB.get(i).getOwnership().equals("Own")) {
								
				%> 
						<select name="cmbStatus"+<%=i%> id="cmbStatus"+<%=i%> style="width: 50px">
							<option value="Own" selected="selected">Own	</option>
							<option value="Lease">Lease</option>
						</select> 
				<%
				 	} else if(assetListDB.get(i).getOwnership().equals("Lease")) {
 				%> 
	 					<select name="cmbStatus"+<%=i%> id="cmbStatus"+<%=i%> style="width: 50px">
	 						<option value="Own">Own</option>
							<option value="Lease" selected="selected"> Lease </option>
						</select> 
				
				<%} %>
			</td>
			<td>
				<%=assetListDB.get(i).getLocation()%>
			</td>
		</tr>
		<%
			}
		}
		%>
	</table>

	<h4>Printer Asset List</h4>
	<table id="tblPrinterList" border="1">
		<tr bgcolor="green" style="color: white;">
			<th>S.#</th>
			<th>Status</th>
			<th>Asset ID</th>
			<th>IP Address</th>
			<th>Catalog</th>
			<th>Server Type</th>
			<th>Asset Usage</th>
			<th>Asset Importance</th>
			<th>Ownership</th>
			<th>Location</th>
		</tr>
		<%
			for (i = 0; i < assetListDB.size(); i++) 
			{
				if (assetListDB.get(i).getProtocolId().equals("printer")) 
				{
		%>
		<tr>
			<td><%=k = k + 1%></td>
			<td>
				<%
					if (assetListDB.get(i).getActive() == 'y') {
								
				%> 
						<select name="cmbStatus"+<%=i%> id="cmbStatus"+<%=i%> name="cmbStatus"+<%=i%> style="width: 50px">
							<option value="<%=assetListDB.get(i).getActive()%>" selected="selected"><%=assetListDB.get(i).getActive()%>	</option>
							<option value="n">n</option>
						</select> 
				<%
				 	} else if (assetListDB.get(i).getActive() == 'n') {
				 				System.out.println("true");
 				%> 
	 					<select name="cmbStatus"+<%=i%> id="cmbStatus"+<%=i%> name="cmbStatus"+<%=i%> style="width: 50px">
	 						<option value="y">y</option>
							<option value="<%=assetListDB.get(i).getActive()%>" selected="selected"> <%=assetListDB.get(i).getActive()%> </option>
						</select> 
				<%
 					}
 				%>

			</td>
			<td>
				<%=assetListDB.get(i).getAssetId()%>
			</td>
			<td><%=assetListDB.get(i).getIpAddr()%></td>
			<td><%=assetListDB.get(i).getCtlgId()%></td>
			<td><%=assetListDB.get(i).getSrvrAppId()%></td>
			<td><%=assetListDB.get(i).getAssetUsg()%></td>
			<td><%=assetListDB.get(i).getImpLvl()%></td>
			<td>
				<%
					if(assetListDB.get(i).getOwnership().equals("Own")) {
								
				%> 
						<select name="cmbStatus"+<%=i%> id="cmbStatus"+<%=i%> style="width: 50px">
							<option value="Own" selected="selected">Own	</option>
							<option value="Lease">Lease</option>
						</select> 
				<%
				 	} else if(assetListDB.get(i).getOwnership().equals("Lease")) {
 				%> 
	 					<select name="cmbStatus"+<%=i%> id="cmbStatus"+<%=i%> style="width: 50px">
	 						<option value="Own">Own</option>
							<option value="Lease" selected="selected"> Lease </option>
						</select> 
				
				<%} %>
			
			</td>
			<td><%=assetListDB.get(i).getLocation()%></td>
		</tr>
		<%
			}
		}
		%>

	</table>

	<h4>NSRG Asset List</h4>
	<table id="tblNsrg" border="1">
		<tr bgcolor="green" style="color: white;">
			<th>S.#</th>
			<th>Status</th>
			<th>Asset ID</th>
			<th>IP Address</th>
			<th>Catalog</th>
			<th>Ownership</th>
			<th>Location</th>
		</tr>
		<%
			for (i = 0; i < assetListDB.size(); i++) {
				if (assetListDB.get(i).getProtocolId().equals("nsrg")) {
		%>
		<tr>
			<td><%=l = l + 1%></td>
			<td>
				<%
					if (assetListDB.get(i).getActive() == 'y') {
								
				%> 
						<select name="cmbStatus"+<%=i%> id="cmbStatus"+<%=i%> name="cmbStatus"+<%=i%> style="width: 50px">
							<option value="<%=assetListDB.get(i).getActive()%>" selected="selected"><%=assetListDB.get(i).getActive()%>	</option>
							<option value="n">n</option>
						</select> 
				<%
				 	} else if (assetListDB.get(i).getActive() == 'n') {
				 				System.out.println("true");
 				%> 
	 					<select name="cmbStatus"+<%=i%> id="cmbStatus"+<%=i%> name="cmbStatus"+<%=i%> style="width: 50px">
	 						<option value="y">y</option>
							<option value="<%=assetListDB.get(i).getActive()%>" selected="selected"> <%=assetListDB.get(i).getActive()%> </option>
						</select> 
				<%
 					}
 				%>
				
			</td>
			<td><%=assetListDB.get(i).getAssetId()%></td>
			<td><%=assetListDB.get(i).getIpAddr()%></td>
			<td><%=assetListDB.get(i).getCtlgId()%></td>
			<td>
				<%
					if(assetListDB.get(i).getOwnership().equals("Own")) {
								
				%> 
						<select name="cmbStatus"+<%=i%> id="cmbStatus"+<%=i%> style="width: 50px">
							<option value="Own" selected="selected">Own	</option>
							<option value="Lease">Lease</option>
						</select> 
				<%
				 	} else if(assetListDB.get(i).getOwnership().equals("Lease")) {
 				%> 
	 					<select name="cmbStatus"+<%=i%> id="cmbStatus"+<%=i%> style="width: 50px">
	 						<option value="Own">Own</option>
							<option value="Lease" selected="selected"> Lease </option>
						</select> 
				
				<%} %>			
			
			</td>
			<td><%=assetListDB.get(i).getLocation()%></td>
		</tr>
		<%
			}
		}
		%>

	</table>

	<br>
	<br>
	<input type="submit" id="submit" name="submit" value="Save" />
	<input type="reset" id="reset" name="reset" value="Reset" />

</body>
</html>