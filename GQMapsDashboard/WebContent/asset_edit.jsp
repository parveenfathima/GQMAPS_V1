<%@page
	import="com.gq.AssetData, com.gq.ui.object.DomainData, java.util.*,com.gq.meter.object.Asset;"%>

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

		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		
		char aryStatus[] = { 'y', 'n' };
		String aryOwnership[] = { "Own", "Lease" };
		String aryLocation[] = { "DC", "EP" };
		
		Asset a = new Asset();
		AssetData assetData = new AssetData();
		
		// Asset list
		List<Asset> assetListDB = new ArrayList<Asset>();
		assetListDB = assetData.getAssetList();
		System.out.println("Total assets: " + assetListDB.size());

		// All domain data list
		List<DomainData> domainDataListDB = new ArrayList<DomainData>();
		domainDataListDB = assetData.getDomainDataList();
		System.out.println("Total Domain Data: " + domainDataListDB.size());	
		
		//computer catalog domain list
		List<DomainData> compCatalogListDB = new ArrayList<DomainData>();
		compCatalogListDB = assetData.getCatalogList("computer");
		
		//printer catalog domain list
		List<DomainData> printerCatalogListDB = new ArrayList<DomainData>();
		printerCatalogListDB = assetData.getCatalogList("printer");		
		
		//nsrg catalog domain list
		List<DomainData> nsrgCatalogListDB = new ArrayList<DomainData>();
		nsrgCatalogListDB = assetData.getCatalogList("nsrg");		
		
		//server app domain list
		List<DomainData> srvrAppListDB = new ArrayList<DomainData>();
		srvrAppListDB = assetData.getSrvrAppType();
		
		//asset importance domain list
		List<DomainData> assetImpLevelListDB = new ArrayList<DomainData>();
		assetImpLevelListDB = assetData.getAssetImpLevel();
		
		//computer type list
		List<DomainData> compTypeListDB = new ArrayList<DomainData>();
		compTypeListDB = assetData.getCompType();
		
 /* 		for(int srvr = 0; srvr<compTypeListDB.size(); srvr++)
		System.out.println("\n " + compTypeListDB.get(srvr).getId() + " >>>>>>>> " + compTypeListDB.get(srvr).getDesc()); */
		
		
	%>

	<h4>Computer Asset List</h4>
	<table id="tblComputerList" border="1">
		<tr bgcolor="green" style="color: white;">
			<th>S.#</th>
			<th style="width: 50px;">Asset Active</th>
			<th>Asset ID</th>
			<th>IP Address</th>
			<th>Catalog</th>
			<th>Server Type</th>
			<th>Asset Usage</th>
			<th>Asset Importance</th>
			<th>Ownership</th>
			<th>Location</th>
			<th>Comp. Type</th>			
		</tr>
		<%
			for (i = 0; i < assetListDB.size(); i++) {
				if (assetListDB.get(i).getProtocolId().equals("computer")) {
		%>
		<tr>
			<td><%=j = j + 1%></td>
			<td>
				<select name="cmbStatus" +<%=i%> id="cmbStatus" +<%=i%> >
						<%
							for (int status = 0; status < aryStatus.length; status++) 
							{
								if (assetListDB.get(i).getActive()==aryStatus[status]) 
					 			{
						%> 	
									<option value="<%=(char)aryStatus[status]%>" selected = "selected"><%=(char)aryStatus[status]%></option>
			 			<%
			 					}
			 					else
			 					{
			 					%>
			 						<option value="<%=(char)aryStatus[status]%>" ><%=(char)aryStatus[status]%></option>
			 					<%
			 					}
							}
						%>
				</select> 
			</td>
			
			<td><%=assetListDB.get(i).getAssetId()%></td>
			<td><%=assetListDB.get(i).getIpAddr()%></td>
			<td>
				<select name="cmbCatalog" +<%=i%> id="cmbCatalog" +<%=i%> style="width: 150px">
						<%
							for (int catalog = 0; catalog < compCatalogListDB.size(); catalog++) 
							{
								if (assetListDB.get(i).getCtlgId()==compCatalogListDB.get(catalog).getId()) 
					 			{
						%> 	
									<option value="<%=compCatalogListDB.get(catalog).getId()%>" selected = "selected"><%=compCatalogListDB.get(catalog).getDesc()%></option>
			 			<%
			 					}
			 					else
			 					{
			 					%>
			 						<option value="<%=compCatalogListDB.get(catalog).getId()%>"><%=compCatalogListDB.get(catalog).getDesc()%></option>
			 					<%
			 					}
							}
						%>
				</select> 
			
			</td>
			<td>
				<select name="cmbSrvrApp"+<%=i%> id="cmbSrvrApp"+<%=i%> style="width: 100px;">
						<%
							for (int srvr = 0; srvr < srvrAppListDB.size(); srvr++) 
							{
								if (assetListDB.get(i).getSrvrAppId() == Byte.parseByte(srvrAppListDB.get(srvr).getId()))
					 			{
						%> 	
									<option value="<%=srvrAppListDB.get(srvr).getId()%>" selected = "selected"><%=srvrAppListDB.get(srvr).getDesc()%></option>
			 			<%
			 					}
			 					else
			 					{
			 			%>
			 						<option value="<%=srvrAppListDB.get(srvr).getId()%>" ><%=srvrAppListDB.get(srvr).getDesc()%></option>
			 			<%
			 					}
							}
						%>
				</select> 			
			
			</td>
			<td><%=assetListDB.get(i).getAssetUsg()%></td>
			<td>
				<select name="cmbImpLevel"+<%=i%> id="cmbImpLevel"+<%=i%> style="width: 100px;">
						<%
							for (int impl = 0; impl < assetImpLevelListDB.size(); impl++) 
							{
								if ((byte)assetListDB.get(i).getImpLvl() == Byte.valueOf(assetImpLevelListDB.get(impl).getId()))
					 			{
						%> 	
									<option value="<%=assetImpLevelListDB.get(impl).getId()%>" selected = "selected"><%=assetImpLevelListDB.get(impl).getDesc()%></option>
			 			<%
			 					}
			 					else
			 					{
					 			System.out.println("\n" + assetListDB.get(i).getSrvrAppId() + "   if  " + assetImpLevelListDB.get(impl).getId());			 					
			 			%>
			 						<option value="<%=assetImpLevelListDB.get(impl).getId()%>" ><%=assetImpLevelListDB.get(impl).getDesc()%></option>
			 			<%
			 					}
							}
						%>
				</select> 				
			</td>
			<td>
				<select name="cmbOwnership"+<%=i%> id="cmbOwnership"+<%=i%> >
						<%
							for (int ownership = 0; ownership < aryOwnership.length; ownership++) 
							{
								if (assetListDB.get(i).getOwnership().equals(aryOwnership[ownership])) 
					 			{
						%> 	
									<option value="<%=aryOwnership[ownership]%>" selected = "selected"> <%=aryOwnership[ownership]%></option>
			 			<%
			 					}
			 					else
			 					{
			 					%>
			 						<option value="<%=aryOwnership[ownership]%>"><%=aryOwnership[ownership]%></option>
			 					<%
			 					}
							}
						%>
				</select> 

			</td>
			<td>
				<select name="cmbLocation" +<%=i%> id="cmbLocation" +<%=i%> >
						<%
							for (int loc = 0; loc < aryLocation.length; loc++) 
							{
								if (assetListDB.get(i).getLocation()==aryLocation[loc]) 
					 			{
						%> 	
									<option value="<%=aryLocation[loc]%>" selected = "selected"><%=aryLocation[loc]%></option>
			 			<%
			 					}
			 					else
			 					{
			 					%>
			 						<option value="<%=aryLocation[loc]%>" ><%=aryLocation[loc]%></option>
			 					<%
			 					}
							}
						%>
				</select> 
			</td>
			<td>
				<select name="cmbCompType"+<%=i%> id="cmbCompType"+<%=i%> >
						<%
							for (int comp = 0; comp < compTypeListDB.size(); comp++) 
							{
							System.out.println("\n "  + assetListDB.get(i).getTypeId() + "  "  + compTypeListDB.get(comp).getId() );
								if (assetListDB.get(i).getTypeId().equals(compTypeListDB.get(comp).getId()))
					 			{
						%> 	
									<option value="<%=compTypeListDB.get(comp).getId()%>" selected = "selected"><%=compTypeListDB.get(comp).getDesc()%></option>
			 			<%
			 					}
			 					else
			 					{
			 			%>
			 						<option value="<%=compTypeListDB.get(comp).getId()%>" ><%=compTypeListDB.get(comp).getDesc()%></option>
			 			<%
			 					}
							}
						%>
				</select> 
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
			<th style="width: 50px;">Asset Active</th>
			<th>Asset ID</th>
			<th style="width: 120px;">IP Address</th>
			<th>Catalog</th>
			<th>Asset Usage</th>
			<th>Asset Importance</th>
			<th>Ownership</th>
			<th>Location</th>
		</tr>
		<%
			for (i = 0; i < assetListDB.size(); i++) {
				if (assetListDB.get(i).getProtocolId().equals("printer")) {
		%>
		<tr>
			<td><%=k = k + 1%></td>
		<td>
				<select name="cmbStatus" +<%=i%> id="cmbStatus" +<%=i%> >
						<%
							for (int status = 0; status < aryStatus.length; status++) 
							{
								if (assetListDB.get(i).getActive()==aryStatus[status]) 
					 			{
						%> 	
									<option value="<%=(char)aryStatus[status]%>" selected = "selected"><%=(char)aryStatus[status]%></option>
			 			<%
			 					}
			 					else
			 					{
			 					%>
			 						<option value="<%=(char)aryStatus[status]%>" ><%=(char)aryStatus[status]%></option>
			 					<%
			 					}
							}
						%>
				</select> 
				<%
				String statusName = "cmbStatus" + i;
				
				for (int status = 0; status < aryStatus.length; status++) {
					 
			 	}
			 	%>
			</td>
			<td><%=assetListDB.get(i).getAssetId()%></td>
			<td><%=assetListDB.get(i).getIpAddr()%></td>
			<td>
					<select name="cmbCatalog" +<%=i%> id="cmbCatalog" +<%=i%> >
						<%
							for (int catalog = 0; catalog < printerCatalogListDB.size(); catalog++) 
							{
								if (assetListDB.get(i).getCtlgId()==printerCatalogListDB.get(catalog).getId()) 
					 			{
						%> 	
									<option value="<%=printerCatalogListDB.get(catalog).getId()%>" selected = "selected"><%=printerCatalogListDB.get(catalog).getDesc()%></option>
			 			<%
			 					}
			 					else
			 					{
			 					%>
			 						<option value="<%=printerCatalogListDB.get(catalog).getId()%>"><%=printerCatalogListDB.get(catalog).getDesc()%></option>
			 					<%
			 					}
							}
						%>
				</select> 
			</td>
			<td><%=assetListDB.get(i).getAssetUsg()%></td>
			<td>
				<select name="cmbImpLevel"+<%=i%> id="cmbImpLevel"+<%=i%> >
						<%
							for (int impl = 0; impl < assetImpLevelListDB.size(); impl++) 
							{
								if ((byte)assetListDB.get(i).getImpLvl() == Byte.valueOf(assetImpLevelListDB.get(impl).getId()))
					 			{
						%> 	
									<option value="<%=assetImpLevelListDB.get(impl).getId()%>" selected = "selected"><%=assetImpLevelListDB.get(impl).getDesc()%></option>
			 			<%
			 					}
			 					else
			 					{
					 			System.out.println("\n" + assetListDB.get(i).getSrvrAppId() + "   if  " + assetImpLevelListDB.get(impl).getId());			 					
			 			%>
			 						<option value="<%=assetImpLevelListDB.get(impl).getId()%>" ><%=assetImpLevelListDB.get(impl).getDesc()%></option>
			 			<%
			 					}
							}
						%>
				</select> 
			</td>
			<td>
				<select name="cmbOwnership"+<%=i%> id="cmbOwnership"+<%=i%> >
						<%
							for (int ownership = 0; ownership < aryOwnership.length; ownership++) 
							{
								if (assetListDB.get(i).getOwnership().equals(aryOwnership[ownership])) 
					 			{
						%> 	
									<option value="<%=aryOwnership[ownership]%>" selected = "selected"> <%=aryOwnership[ownership]%></option>
			 			<%
			 					}
			 					else
			 					{
			 					%>
			 						<option value="<%=aryOwnership[ownership]%>"><%=aryOwnership[ownership]%></option>
			 					<%
			 					}
							}
						%>
				</select> 

			</td>
			<td>
				<select name="cmbLocation" +<%=i%> id="cmbLocation" +<%=i%> >
						<%
							for (int loc = 0; loc < aryLocation.length; loc++) 
							{
								if (assetListDB.get(i).getLocation()==aryLocation[loc]) 
					 			{
						%> 	
									<option value="<%=aryLocation[loc]%>" selected = "selected"><%=aryLocation[loc]%></option>
			 			<%
			 					}
			 					else
			 					{
			 					%>
			 						<option value="<%=aryLocation[loc]%>" ><%=aryLocation[loc]%></option>
			 					<%
			 					}
							}
						%>
				</select> 
				
			</td>
		</tr>
		<%
			}
		}
		%>

	</table>

	<h4>Network, Switch, Router and Gateway</h4>
	<table id="tblNsrg" border="1">
		<tr bgcolor="green" style="color: white;">
			<th>S.#</th>
			<th style="width: 50px;">Asset Active</th>
			<th style="width: 120px;">Asset ID</th>
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
				<select name="cmbStatus" +<%=i%> id="cmbStatus" +<%=i%>>
						<%
							for (int status = 0; status < aryStatus.length; status++) 
							{
								if (assetListDB.get(i).getActive()==aryStatus[status]) 
					 			{
						%> 	
									<option value="<%=(char)aryStatus[status]%>" selected = "selected"><%=(char)aryStatus[status]%></option>
			 			<%
			 					}
			 					else
			 					{
			 					%>
			 						<option value="<%=(char)aryStatus[status]%>" ><%=(char)aryStatus[status]%></option>
			 					<%
			 					}
							}
						%>
				</select> 
				<%
				String statusName = "cmbStatus" + i;
				
				for (int status = 0; status < aryStatus.length; status++) {
					 
			 	}
			 	%>
			</td>
			<td><%=assetListDB.get(i).getAssetId()%></td>
			<td><%=assetListDB.get(i).getIpAddr()%></td>
			<td>
				<select name="cmbCatalog" +<%=i%> id="cmbCatalog" +<%=i%> >
						<%
							for (int catalog = 0; catalog < nsrgCatalogListDB.size(); catalog++) 
							{
								if (assetListDB.get(i).getCtlgId()==nsrgCatalogListDB.get(catalog).getId()) 
					 			{
						%> 	
									<option value="<%=nsrgCatalogListDB.get(catalog).getId()%>" selected = "selected"><%=nsrgCatalogListDB.get(catalog).getDesc()%></option>
			 			<%
			 					}
			 					else
			 					{
			 					%>
			 						<option value="<%=nsrgCatalogListDB.get(catalog).getId()%>"><%=nsrgCatalogListDB.get(catalog).getDesc()%></option>
			 					<%
			 					}
							}
						%>
				</select> 		
			</td>
			<td>
					<select name="cmbOwnership"+<%=i%> id="cmbOwnership"+<%=i%>
					name="cmbOwnership"+<%=i%> style="width: 50px">
						<%
							for (int ownership = 0; ownership < aryOwnership.length; ownership++) 
							{
								if (assetListDB.get(i).getOwnership().equals(aryOwnership[ownership])) 
					 			{
						%> 	
									<option value="<%=aryOwnership[ownership]%>" selected = "selected"> <%=aryOwnership[ownership]%></option>
			 			<%
			 					}
			 					else
			 					{
			 					%>
			 						<option value="<%=aryOwnership[ownership]%>"><%=aryOwnership[ownership]%></option>
			 					<%
			 					}
							}
						%>
				</select> 
			</td>
			<td>
				<select name="cmbLocation" +<%=i%> id="cmbLocation" +<%=i%> >
						<%
							for (int loc = 0; loc < aryLocation.length; loc++) 
							{
								if (assetListDB.get(i).getLocation()==aryLocation[loc]) 
					 			{
						%> 	
									<option value="<%=aryLocation[loc]%>" selected = "selected"><%=aryLocation[loc]%></option>
			 			<%
			 					}
			 					else
			 					{
			 					%>
			 						<option value="<%=aryLocation[loc]%>" ><%=aryLocation[loc]%></option>
			 					<%
			 					}
							}
						%>
				</select> 

			</td>
		</tr>
		<%
			}
		}
		%>

	</table>

	<br>
	<br>
	<input type="submit" id="submit" name="submit" value="Save" />

</body>
</html>