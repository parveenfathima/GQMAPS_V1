<%@page
	import="com.gq.AssetData, com.gq.ui.object.DomainData, java.util.*,com.gq.meter.object.*, com.google.gson.*;"%>

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

<script src = "js/jstorage.js"> </script> 
<script src = "js/rest_service.js"> </script>  
<script src = "js/asset_edit.js"> </script>  

<script type="text/javascript">

var assetsDB = { assetDataDB: [] };
function updateAsset(count)
{
	//alert("Count: " + count);
	var vActive = "";
	var vAssetID = "";
	var vIP = ""; 
	var vCatalog = "";
	var vSrvrType = "";
	var vUsage = "";
	var vImpLevel = "";
	var vOwnership = "";
	var vLocation = "";
	var vCompType = "";
	
	var jsonString = "";

	var assets = { assetData: [] };
	
	var j = 0;
	for(j=0; j < count; j++)
	{

		vActive = $("#cmbStatus"+j).val();
		vAssetID = $.trim($("#assetID"+j).text());
		vIP =  $.trim($("#ipAddr"+j).text());
		vCatalog =  $("#cmbCatalog"+j).val();
		
		vSrvrType =  $("#cmbSrvrApp"+j).val(); // NA for printer and nsrg
		if(vSrvrType === null || vSrvrType === "null")
			vSrvrType = null;
		
		vUsage =  $.trim($("#txtAssetUsage"+j).val()); // NA for nsrg
		if(vUsage === "" || vUsage === "null" || vUsage === null)
			vUsage = null;
		
		vImpLevel =  $("#cmbImpLevel"+j).val(); // NA for nsrg
		if(vImpLevel === null || vImpLevel === "null")
			vImpLevel = null;		
		
		vOwnership =  $("#cmbOwnership"+j).val();
		vLocation =  $("#cmbLocation"+j).val();
		
		vCompType =  $("#cmbCompType"+j).val(); //NA for printer and nsrg
		if(vCompType === null || vCompType === "null")
			vCompType = null;		
		
		
		//alert(vActive +", " +vAssetID +", " +vIP+", " +vCatalog+", " +vSrvrType+", " +vUsage+", " +vImpLevel+", " +vOwnership+", " +vLocation+", " +vCompType);

	    assets.assetData.push({ 
	        
			"assetId": vAssetID,
			"ipAddr": vIP,
			"ctlgId": vCatalog,
			"srvrAppId": vSrvrType, 
			"assetUsg": vUsage,
			"impLvl": vImpLevel,
			"ownership": vOwnership,
			"dcEnt": vLocation,
			"active": vActive, 
			"typeId": vCompType        
	    });
	}

	var vType = "PUT";
	//alert("before update: "  + $.jStorage.get("jsEntpId"));
	var vUrl = $.jStorage.get("jsDBUrl") + "AssetEditServices/updateAssetData?enterpriseId=" + $.jStorage.get("jsEntpId");
			
	$.ajax
	({
		type:vType,
		contentType: "application/json",
		url:vUrl,
		data: JSON.stringify(assets.assetData),
		async:false,
		dataType: "json",
		success:function(response)
		{
 			getCurrentAssets();
 			
 			//alert("assetsDB: " + JSON.stringify(assetsDB.assetDataDB));
 			
 			alert("assetsDB length: " + assetsDB.assetDataDB.length + " " + "assets length: " + assets.assetData.length);
 			
 			$.each(assets.assetData, function(i, n){
 			
				//alert(assets.assetData[i].assetId); 	
				if(!compareObject(assets.assetData[i], assetsDB.assetDataDB[i]))
				{
					alert("changes found for " + i);
				}		
 			});
 			
 			alert("Asset details are saved successfully!");

 			//window.location.href = "dashboard_full.html";
		},
		error:function(json)
		{
			alert("Error from updating asset details: " + json.status + " " + json.responseText);
		} 
	});	

}

function getCurrentAssets()
{
	//alert("Count: " + count);
	var vActive = "";
	var vAssetID = "";
	var vIP = ""; 
	var vCatalog = "";
	var vSrvrType = "";
	var vUsage = "";
	var vImpLevel = "";
	var vOwnership = "";
	var vLocation = "";
	var vCompType = "";
	
	var vType = "GET";
	//alert("before getting current assets: "  + $.jStorage.get("jsEntpId"));
	var vUrl = $.jStorage.get("jsDBUrl") + "AssetEditServices/getAssetData?enterpriseId=" + $.jStorage.get("jsEntpId");
			
	$.ajax
	({
		type:vType,
		contentType: "application/json",
		url:vUrl,
		async:false,
		dataType: "json",
		success:function(response)
		{
 			//alert("Current asset details inside success!");
 			
 			$.each(response["assetResult"], function(i,n)
			{
				 assetsDB.assetDataDB.push({ 
	        
					"assetId": response["assetResult"][i]["assetId"],
					"ipAddr": response["assetResult"][i]["ipAddr"],
					"ctlgId": response["assetResult"][i]["ctlgId"],
					"srvrAppId": response["assetResult"][i]["srvrAppId"], 
					"assetUsg": response["assetResult"][i]["assetUsg"],
					"impLvl": response["assetResult"][i]["impLvl"],
					"ownership": response["assetResult"][i]["ownership"],
					"dcEnt": response["assetResult"][i]["dcEnt"],
					"active": response["assetResult"][i]["active"], 
					"typeId": response["assetResult"][i]["typeId"]        
	    		});
			
			});
			
		},
		error:function(json)
		{
			alert("Error from updating asset details: " + json.status + " " + json.responseText);
		} 
	});	
}

function compareObject(obj1, obj2)
{
	for(var p in obj1)
	{
		alert(obj1[p] + "   " + obj2[p]);
		if(obj1[p] !== obj2[p])
		{
			return false;
		}
	}
	
	for(var p in obj2)
	{
	alert(obj1[p] + "   " + obj2[p]);
		if(obj1[p] !== obj2[p]){
			return false;
		}
	}
	
	return true;
}
	
</script>

</head>
<body>

<form id = "frmAsset" name = "frmAsset">
<table>
<tr>
	<td><label><h1> Asset Edit for&nbsp; <%=request.getParameter("setEntp") %></h1>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label> </td>
	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="dashboard_full.html" > Back</a>
	</td>
	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="login.html" onClick="logout();">Logout</a></td>
</tr>
</table>
	<%
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		String entp = "";
		char aryStatus[] = { 'y', 'n' };
		String aryOwnership[] = { "Own", "Lease" };
		String aryLocation[] = { "DC", "EP" };
		
		Asset a = new Asset();
		AssetData assetData = new AssetData();
		
		// Asset list
		List<Asset> assetListDB = new ArrayList<Asset>();
		
		// All domain data list
		List<DomainData> domainDataListDB = new ArrayList<DomainData>();		
		
		//getting the user id of the enterprise
		entp = request.getParameter("setEntp");
		System.out.println("\n Enterprise User id is -----------------------------------------: " + entp);
		System.out.println("\n Enterprise User id is -----------------------------------------: " + request.getParameter("setEntp"));		
		
		assetListDB = assetData.getAssetList(entp);
		System.out.println("Total assets: " + assetListDB.size());

		domainDataListDB = assetData.getDomainDataList(entp);
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
	%>
	
	<h4>Computer Asset List</h4>
	
	<table id="tblComputerList" border="1">
		<tr bgcolor="green" style="color: white;">
			<th>S.#</th>
			<th style="width: 50px;">Asset Active</th>
			<th style = "width:150px;">Asset ID</th>
			<th>IP Address</th>
			<th>Catalog</th>
			<th>Server Type</th>
			<th>Asset Usage</th>
			<th>Asset Importance</th>
			<th>Ownership</th>
			<th>Location</th>
			<th style = "width: 100px;">Comp. Type</th>			
		</tr>
		<%
			for (i = 0; i < assetListDB.size(); i++) {
				if (assetListDB.get(i).getProtocolId().equals("computer")) {
		%>
		<tr>
			<td><%=j = j + 1%></td>
			<td>
				<select name=<%="cmbStatus"+i%> id=<%="cmbStatus"+i%> >
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
			
			<td style = "width:150px;"><span id = <%="assetID"+i%> name = <%="assetID"+i%>> <%=assetListDB.get(i).getAssetId()%> </span> </td>
			<td><span id = <%="ipAddr"+i%> name = <%="ipAddr"+i%>> <%=assetListDB.get(i).getIpAddr()%> </span> </td>
			<td>
				<select name=<%="cmbCatalog"+i%> id=<%="cmbCatalog"+i%> style="width: 150px">
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
				<select name=<%="cmbSrvrApp"+i%> id=<%="cmbSrvrApp"+i%> style="width: 100px;">
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
			<td>
				<input type="text" id = <%="txtAssetUsage"+i%> name = <%="txtAssetUsage"+i%> value = "<%=assetListDB.get(i).getAssetUsg()%>" width="50px" />
			</td>
			<td>
				<select name=<%="cmbImpLevel"+i%> id=<%="cmbImpLevel"+i%> style="width: 100px;">
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
			 			%>
			 						<option value="<%=assetImpLevelListDB.get(impl).getId()%>" ><%=assetImpLevelListDB.get(impl).getDesc()%></option>
			 			<%
			 					}
							}
						%>
				</select> 				
			</td>
			<td>
				<select name=<%="cmbOwnership"+i%> id=<%="cmbOwnership"+i%> >
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
				<select name=<%="cmbLocation"+i%> id=<%="cmbLocation"+i%> >
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
				<select name=<%="cmbCompType"+i%> id=<%="cmbCompType"+i%> >
						<%
							for (int comp = 0; comp < compTypeListDB.size(); comp++) 
							{
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
			<th style = "width:150px;">Asset ID</th>
			<th style = "width:130px;">IP Address</th>
			<th style = "width:150px;">Catalog</th>
			<th style = "width:100px;">Server Type</th>			
			<th style = "width:100px;">Asset Usage</th>
			<th>Asset Importance</th>
			<th>Ownership</th>
			<th>Location</th>
			<th style = "width:100px;">Comp. Type</th>					
		</tr>
		<%
			for (i = 0; i < assetListDB.size(); i++) {
				if (assetListDB.get(i).getProtocolId().equals("printer")) {
		%>
		<tr>
			<td><%=k = k + 1%></td>
		<td>
				<select name=<%="cmbStatus"+i%> id=<%="cmbStatus"+i%> >
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
			<td style = "width:150px;"> <span id = <%="assetID"+i%> name = <%="assetID"+i%>> <%=assetListDB.get(i).getAssetId()%> </span></td>
			<td style = "width:130px;"> <span id = <%="ipAddr"+i%> name = <%="ipAddr"+i%>> <%=assetListDB.get(i).getIpAddr()%> </span></td>
			<td style = "width:150px;">
					<select name=<%="cmbCatalog"+i%> id=<%="cmbCatalog"+i%> >
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
			<td style = "width:100px;">
				<input type="text" id = <%="cmbSrvrApp"+i%> name = <%="cmbSrvrApp"+i%> disabled = "disabled" value = "null" style = "width:100px;" />
			</td>
			<td style = "width:100px;">
				<input type="text" id = <%="txtAssetUsage"+i%> name = <%="txtAssetUsage"+i%> disabled = "disabled" value = "null" style = "width:100px;"/>
			</td>			
			<td>
				<select name=<%="cmbImpLevel"+i%> id=<%="cmbImpLevel"+i%> >
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
			 			%>
			 						<option value="<%=assetImpLevelListDB.get(impl).getId()%>" ><%=assetImpLevelListDB.get(impl).getDesc()%></option>
			 			<%
			 					}
							}
						%>
				</select> 
			</td>
			<td>
				<select name=<%="cmbOwnership"+i%> id=<%="cmbOwnership"+i%> >
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
				<select name=<%="cmbLocation"+i%> id=<%="cmbLocation"+i%> >
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
			<td style = "width:100px;">
				<input type="text" name=<%="cmbCompType"+i%> id=<%="cmbCompType"+i%> disabled = "disabled" value = "null" style = "width:100px;"/>
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
			<th style="width: 150px;">Asset ID</th>
			<th style = "width:100px;">IP Address</th>
			<th style = "width:150px;">Catalog</th>
			<th>Server Type</th>
			<th style = "width:100px;">Asset Usage</th>	
			<th>Asset Importance</th>
			<th>Ownership</th>
			<th>Location</th>
			<th style = "width:100px;" >Comp. Type</th>					
		</tr>
		<%
			for (i = 0; i < assetListDB.size(); i++) {
				if (assetListDB.get(i).getProtocolId().equals("nsrg")) {
		%>
		<tr>
			<td><%=l = l + 1%></td>
			<td>
				<select name=<%="cmbStatus"+i%> id=<%="cmbStatus"+i%>>
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
			<td style = "width:150px;"> <span id = <%="assetID"+i%> name = <%="assetID"+i%>><%=assetListDB.get(i).getAssetId()%> </span></td>
			<td style = "width:150px;"> <span id = <%="ipAddr"+i%> name = <%="ipAddr"+i%> ><%=assetListDB.get(i).getIpAddr()%> </span></td>
			<td>
				<select name=<%="cmbCatalog"+i%> id=<%="cmbCatalog"+i%> style = "width:150px;">
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
				<input type="text" id = <%="cmbSrvrApp"+i%> name = <%="cmbSrvrApp"+i%> disabled = "disabled" value = "null" hidden = "hidden" style = "width:100px;"/>
			</td>		
			<td style = "width:100px;">
				<input type="text" id = <%="txtAssetUsage"+i%> name = <%="txtAssetUsage"+i%> disabled = "disabled" value = "null" hidden = "hidden" style = "width:100px;" />
			</td>
			<td>
				<input type="text" id = <%="cmbImpLevel"+i%> name = <%="cmbImpLevel"+i%> disabled = "disabled" value = "null" hidden = "hidden" style = "width:100px;"/>
			</td>			
			<td>
					<select name=<%="cmbOwnership"+i%> id=<%="cmbOwnership"+i%> style="width: 50px">
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
				<select name=<%="cmbLocation"+i%> id=<%="cmbLocation"+i%> >
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
			<td >
				<input type="text" name=<%="cmbCompType"+i%> id=<%="cmbCompType"+i%>  disabled = "disabled" value = "null"   style = "width:100px;"/>
			</td>			
		</tr>
		<%
			}
		}
		%>

	</table>

	<br>
	<br>
	
	<input type="button" id="submit" name="submit" value="Save" onClick = "updateAsset(<%=assetListDB.size()%>);" />
</form>
</body>
</html>