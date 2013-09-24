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

<script src="js/jstorage.js"> </script>
<script src="js/rest_service.js"> </script>
<script src="js/asset_edit.js"> </script>

<script type="text/javascript">

//array to store the db values to compare with the page values
var assetsDB = { assetDataDB: [] };

//array to store the difference between the page data and db data
var assetsForUpdate = { assetUpdateData: [] };

function updateAsset(count)
{
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

    //array to store the page values on Save button click event
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
	
 	getCurrentAssets();
 			
 	$.each(assets.assetData, function(i, n)
 	{
 			
		if(assets.assetData[i].assetId != assetsDB.assetDataDB[i].assetId ||
			assets.assetData[i].ipAddr != assetsDB.assetDataDB[i].ipAddr ||
			assets.assetData[i].ctlgId != assetsDB.assetDataDB[i].ctlgId ||
			 assets.assetData[i].srvrAppId != assetsDB.assetDataDB[i].srvrAppId ||
			 assets.assetData[i].assetUsg != assetsDB.assetDataDB[i].assetUsg ||				   
			 assets.assetData[i].impLvl != assetsDB.assetDataDB[i].impLvl ||
			 assets.assetData[i].ownership != assetsDB.assetDataDB[i].ownership ||
			 assets.assetData[i].dcEnt != assetsDB.assetDataDB[i].dcEnt ||
			 assets.assetData[i].active != assetsDB.assetDataDB[i].active ||
			 assets.assetData[i].typeId != assetsDB.assetDataDB[i].typeId)
		 {
		 	assetsForUpdate.assetUpdateData.push(assets.assetData[i]);
		 }
		
 	});
 			
	if(assetsForUpdate.assetUpdateData.length >= 1)
	{
		var vType = "PUT";
		var vUrl = $.jStorage.get("jsDBUrl") + "AssetEditServices/updateAssetData?enterpriseId=" + $.jStorage.get("jsEntpId");
		alert(JSON.stringify(assetsForUpdate.assetUpdateData));		
		$.ajax
		({
			type:vType,
			contentType: "application/json",
			url:vUrl,
			data: JSON.stringify(assetsForUpdate.assetUpdateData),
			async:false,
			dataType: "json",
			success:function(response)
			{
	 			alert("Asset details were updated successfully!");
			},
			error:function(json)
			{
				alert("Error from updating asset details: " + json.status + " " + json.responseText);
			} 
		});	
	}
	else
	{
		alert("No changes were made!");
	}
	
	window.location.href = "dashboard_full.html";
}

function getCurrentAssets()
{
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
            
	<form id="frmAsset" name="frmAsset">
		<table>
			<tr>
				<td><label><h1>
							Asset Edit for&nbsp;
							<%=request.getParameter("setEntp")%></h1>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
				</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="dashboard_full.html"> Back</a>
				</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="login.html"
					onClick="logout();">Logout</a></td>
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
			System.out
					.println("\n Enterprise User id is -----------------------------------------: "
							+ entp);
			System.out
					.println("\n Enterprise User id is -----------------------------------------: "
							+ request.getParameter("setEntp"));

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
				<th style="width: 150px;">Asset ID</th>
				<th>IP Address</th>
				<th>Catalog</th>
				<th>Server Type</th>
				<th>Asset Usage</th>
				<th>Asset Importance</th>
				<th>Ownership</th>
				<th>Location</th>
				<th style="width: 100px;">Comp. Type</th>
			</tr>
			<%
				for (i = 0; i < assetListDB.size(); i++) {
						if (assetListDB.get(i).getProtocolId().equals("computer")) {
			%>
			<tr>
				<td><%=j = j + 1%></td>
				<td><select name=<%="cmbStatus" + i%> id=<%="cmbStatus" + i%>>
						<%
							for (int status = 0; status < aryStatus.length; status++) {
														if (assetListDB.get(i).getActive() == aryStatus[status]) {
						%>
						<option value="<%=(char) aryStatus[status]%>" selected="selected"><%=(char) aryStatus[status]%></option>
						<%
							} else {
						%>
						<option value="<%=(char) aryStatus[status]%>"><%=(char) aryStatus[status]%></option>
						<%
							}
													}
						%>
				</select></td>

				<td style="width: 150px;"><span id=<%="assetID" + i%>
					name=<%="assetID" + i%>> <%=assetListDB.get(i).getAssetId()%>
				</span></td>
				<td><span id=<%="ipAddr" + i%> name=<%="ipAddr" + i%>> <%=assetListDB.get(i).getIpAddr()%>
				</span></td>
				<td><select name=<%="cmbCatalog" + i%> id=<%="cmbCatalog" + i%>
					style="width: 150px">
						<%
							for (int catalog = 0; catalog < compCatalogListDB.size(); catalog++) {
														if (assetListDB.get(i).getCtlgId() == compCatalogListDB
																.get(catalog).getId()) {
						%>
						<option value="<%=compCatalogListDB.get(catalog).getId()%>"
							selected="selected"><%=compCatalogListDB.get(catalog).getDesc()%></option>
						<%
							} else {
						%>
						<option value="<%=compCatalogListDB.get(catalog).getId()%>"><%=compCatalogListDB.get(catalog).getDesc()%></option>
						<%
							}
													}
						%>
				</select></td>
				<td><select name=<%="cmbSrvrApp" + i%> id=<%="cmbSrvrApp" + i%>
					style="width: 100px;">
						<%
							for (int srvr = 0; srvr < srvrAppListDB.size(); srvr++) {
														if (assetListDB.get(i).getSrvrAppId() == Byte
																.parseByte(srvrAppListDB.get(srvr).getId())) {
						%>
						<option value="<%=srvrAppListDB.get(srvr).getId()%>"
							selected="selected"><%=srvrAppListDB.get(srvr).getDesc()%></option>
						<%
							} else {
						%>
						<option value="<%=srvrAppListDB.get(srvr).getId()%>"><%=srvrAppListDB.get(srvr).getDesc()%></option>
						<%
							}
													}
						%>
				</select></td>
				<td><input type="text" id=<%="txtAssetUsage" + i%>
					name=<%="txtAssetUsage" + i%>
					value="<%=assetListDB.get(i).getAssetUsg()%>" width="50px" /></td>
				<td><select name=<%="cmbImpLevel" + i%>
					id=<%="cmbImpLevel" + i%> style="width: 100px;">
						<%
							for (int impl = 0; impl < assetImpLevelListDB.size(); impl++) {
														if ((byte) assetListDB.get(i).getImpLvl() == Byte
																.valueOf(assetImpLevelListDB.get(impl).getId())) {
						%>
						<option value="<%=assetImpLevelListDB.get(impl).getId()%>"
							selected="selected"><%=assetImpLevelListDB.get(impl).getDesc()%></option>
						<%
							} else {
						%>
						<option value="<%=assetImpLevelListDB.get(impl).getId()%>"><%=assetImpLevelListDB.get(impl).getDesc()%></option>
						<%
							}
													}
						%>
				</select></td>
				<td><select name=<%="cmbOwnership" + i%>
					id=<%="cmbOwnership" + i%>>
						<%
							for (int ownership = 0; ownership < aryOwnership.length; ownership++) {
														if (assetListDB.get(i).getOwnership()
																.equals(aryOwnership[ownership])) {
						%>
						<option value="<%=aryOwnership[ownership]%>" selected="selected">
							<%=aryOwnership[ownership]%></option>
						<%
							} else {
						%>
						<option value="<%=aryOwnership[ownership]%>"><%=aryOwnership[ownership]%></option>
						<%
							}
													}
						%>
				</select></td>
				<td><select name=<%="cmbLocation" + i%>
					id=<%="cmbLocation" + i%>>
						<%
							for (int loc = 0; loc < aryLocation.length; loc++) {
														if (assetListDB.get(i).getLocation() == aryLocation[loc]) {
						%>
						<option value="<%=aryLocation[loc]%>" selected="selected"><%=aryLocation[loc]%></option>
						<%
							} else {
						%>
						<option value="<%=aryLocation[loc]%>"><%=aryLocation[loc]%></option>
						<%
							}
													}
						%>
				</select></td>
				<td><select name=<%="cmbCompType" + i%>
					id=<%="cmbCompType" + i%>>
						<%
							for (int comp = 0; comp < compTypeListDB.size(); comp++) {
														if (assetListDB.get(i).getTypeId()
																.equals(compTypeListDB.get(comp).getId())) {
						%>
						<option value="<%=compTypeListDB.get(comp).getId()%>"
							selected="selected"><%=compTypeListDB.get(comp).getDesc()%></option>
						<%
							} else {
						%>
						<option value="<%=compTypeListDB.get(comp).getId()%>"><%=compTypeListDB.get(comp).getDesc()%></option>
						<%
							}
													}
						%>
				</select></td>
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
				<th style="width: 150px;">Asset ID</th>
				<th style="width: 130px;">IP Address</th>
				<th style="width: 150px;">Catalog</th>
				<th style="width: 100px; display: none;">Server Type</th>
				<th style="width: 100px;">Asset Usage</th>
				<th>Asset Importance</th>
				<th>Ownership</th>
				<th>Location</th>
				<th style="width: 100px; display: none;">Comp. Type</th>
			</tr>
			<%
				for (i = 0; i < assetListDB.size(); i++) {
						if (assetListDB.get(i).getProtocolId().equals("printer")) {
			%>
			<tr>
				<td><%=k = k + 1%></td>
				<td><select name=<%="cmbStatus" + i%> id=<%="cmbStatus" + i%>>
						<%
							for (int status = 0; status < aryStatus.length; status++) {
														if (assetListDB.get(i).getActive() == aryStatus[status]) {
						%>
						<option value="<%=(char) aryStatus[status]%>" selected="selected"><%=(char) aryStatus[status]%></option>
						<%
							} else {
						%>
						<option value="<%=(char) aryStatus[status]%>"><%=(char) aryStatus[status]%></option>
						<%
							}
													}
						%>
				</select></td>
				<td style="width: 150px;"><span id=<%="assetID" + i%>
					name=<%="assetID" + i%>> <%=assetListDB.get(i).getAssetId()%>
				</span></td>
				<td style="width: 130px;"><span id=<%="ipAddr" + i%>
					name=<%="ipAddr" + i%>> <%=assetListDB.get(i).getIpAddr()%>
				</span></td>
				<td style="width: 150px;"><select name=<%="cmbCatalog" + i%>
					id=<%="cmbCatalog" + i%>>
						<%
							for (int catalog = 0; catalog < printerCatalogListDB.size(); catalog++) {
														if (assetListDB.get(i).getCtlgId() == printerCatalogListDB
																.get(catalog).getId()) {
						%>
						<option value="<%=printerCatalogListDB.get(catalog).getId()%>"
							selected="selected"><%=printerCatalogListDB.get(catalog)
									.getDesc()%></option>
						<%
							} else {
						%>
						<option value="<%=printerCatalogListDB.get(catalog).getId()%>"><%=printerCatalogListDB.get(catalog)
									.getDesc()%></option>
						<%
							}
													}
						%>
				</select></td>
				<td style="width: 100px; display: none;"><input type="text"
					id=<%="cmbSrvrApp" + i%> name=<%="cmbSrvrApp" + i%>
					disabled="disabled" value="null" style="width: 100px;" /></td>

				<td style="width: 100px;"><input type="text"
					id=<%="txtAssetUsage" + i%> name=<%="txtAssetUsage" + i%>
					value="<%=assetListDB.get(i).getAssetUsg()%>" width="100px" /></td>


				<td><select name=<%="cmbImpLevel" + i%>
					id=<%="cmbImpLevel" + i%>>
						<%
							for (int impl = 0; impl < assetImpLevelListDB.size(); impl++) {
														if ((byte) assetListDB.get(i).getImpLvl() == Byte
																.valueOf(assetImpLevelListDB.get(impl).getId())) {
						%>
						<option value="<%=assetImpLevelListDB.get(impl).getId()%>"
							selected="selected"><%=assetImpLevelListDB.get(impl).getDesc()%></option>
						<%
							} else {
						%>
						<option value="<%=assetImpLevelListDB.get(impl).getId()%>"><%=assetImpLevelListDB.get(impl).getDesc()%></option>
						<%
							}
													}
						%>
				</select></td>
				<td><select name=<%="cmbOwnership" + i%>
					id=<%="cmbOwnership" + i%>>
						<%
							for (int ownership = 0; ownership < aryOwnership.length; ownership++) {
														if (assetListDB.get(i).getOwnership()
																.equals(aryOwnership[ownership])) {
						%>
						<option value="<%=aryOwnership[ownership]%>" selected="selected">
							<%=aryOwnership[ownership]%></option>
						<%
							} else {
						%>
						<option value="<%=aryOwnership[ownership]%>"><%=aryOwnership[ownership]%></option>
						<%
							}
													}
						%>
				</select></td>
				<td><select name=<%="cmbLocation" + i%>
					id=<%="cmbLocation" + i%>>
						<%
							for (int loc = 0; loc < aryLocation.length; loc++) {
														if (assetListDB.get(i).getLocation() == aryLocation[loc]) {
						%>
						<option value="<%=aryLocation[loc]%>" selected="selected"><%=aryLocation[loc]%></option>
						<%
							} else {
						%>
						<option value="<%=aryLocation[loc]%>"><%=aryLocation[loc]%></option>
						<%
							}
													}
						%>
				</select></td>
				<td style="width: 100px; display: none;"><input type="text"
					name=<%="cmbCompType" + i%> id=<%="cmbCompType" + i%>
					disabled="disabled" value="null" style="width: 100px;" /></td>
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
				<th style="width: 100px;">IP Address</th>
				<th style="width: 150px;">Catalog</th>
				<th style="display: none;">Server Type</th>
				<th style="width: 100px; display: none;">Asset Usage</th>
				<th style="display: none;">Asset Importance</th>
				<th>Ownership</th>
				<th>Location</th>
				<th style="width: 100px; display: none;">Comp. Type</th>
			</tr>
			<%
				for (i = 0; i < assetListDB.size(); i++) {
						if (assetListDB.get(i).getProtocolId().equals("nsrg")) {
			%>
			<tr>
				<td><%=l = l + 1%></td>
				<td><select name=<%="cmbStatus" + i%> id=<%="cmbStatus" + i%>>
						<%
							for (int status = 0; status < aryStatus.length; status++) {
														if (assetListDB.get(i).getActive() == aryStatus[status]) {
						%>
						<option value="<%=(char) aryStatus[status]%>" selected="selected"><%=(char) aryStatus[status]%></option>
						<%
							} else {
						%>
						<option value="<%=(char) aryStatus[status]%>"><%=(char) aryStatus[status]%></option>
						<%
							}
													}
						%>
				</select></td>
				<td style="width: 150px;"><span id=<%="assetID" + i%>
					name=<%="assetID" + i%>><%=assetListDB.get(i).getAssetId()%>
				</span></td>
				<td style="width: 150px;"><span id=<%="ipAddr" + i%>
					name=<%="ipAddr" + i%>><%=assetListDB.get(i).getIpAddr()%> </span></td>
				<td><select name=<%="cmbCatalog" + i%> id=<%="cmbCatalog" + i%>
					style="width: 150px;">
						<%
							for (int catalog = 0; catalog < nsrgCatalogListDB.size(); catalog++) {
														if (assetListDB.get(i).getCtlgId() == nsrgCatalogListDB
																.get(catalog).getId()) {
						%>
						<option value="<%=nsrgCatalogListDB.get(catalog).getId()%>"
							selected="selected"><%=nsrgCatalogListDB.get(catalog).getDesc()%></option>
						<%
							} else {
						%>
						<option value="<%=nsrgCatalogListDB.get(catalog).getId()%>"><%=nsrgCatalogListDB.get(catalog).getDesc()%></option>
						<%
							}
													}
						%>
				</select></td>
				<td style="display: none;"><input type="text"
					id=<%="cmbSrvrApp" + i%> name=<%="cmbSrvrApp" + i%>
					disabled="disabled" value="null" hidden="hidden"
					style="width: 100px;" /></td>
				<td style="width: 100px; display: none;"><input type="text"
					id=<%="txtAssetUsage" + i%> name=<%="txtAssetUsage" + i%>
					disabled="disabled" value="null" hidden="hidden"
					style="width: 100px;" /></td>
				<td style="display: none;"><input type="text"
					id=<%="cmbImpLevel" + i%> name=<%="cmbImpLevel" + i%>
					disabled="disabled" value="null" hidden="hidden"
					style="width: 100px;" /></td>
				<td><select name=<%="cmbOwnership" + i%>
					id=<%="cmbOwnership" + i%> style="width: 50px">
						<%
							for (int ownership = 0; ownership < aryOwnership.length; ownership++) {
														if (assetListDB.get(i).getOwnership()
																.equals(aryOwnership[ownership])) {
						%>
						<option value="<%=aryOwnership[ownership]%>" selected="selected">
							<%=aryOwnership[ownership]%></option>
						<%
							} else {
						%>
						<option value="<%=aryOwnership[ownership]%>"><%=aryOwnership[ownership]%></option>
						<%
							}
													}
						%>
				</select></td>
				<td><select name=<%="cmbLocation" + i%>
					id=<%="cmbLocation" + i%>>
						<%
							for (int loc = 0; loc < aryLocation.length; loc++) {
														if (assetListDB.get(i).getLocation() == aryLocation[loc]) {
						%>
						<option value="<%=aryLocation[loc]%>" selected="selected"><%=aryLocation[loc]%></option>
						<%
							} else {
						%>
						<option value="<%=aryLocation[loc]%>"><%=aryLocation[loc]%></option>
						<%
							}
													}
						%>
				</select></td>
				<td style="display: none;"><input type="text"
					name=<%="cmbCompType" + i%> id=<%="cmbCompType" + i%>
					disabled="disabled" value="null" style="width: 100px;" /></td>
			</tr>
			<%
				}
					}
			%>

		</table>

		<br> <br> <input type="button" id="submit" name="submit"
			value="Save" onClick="updateAsset(<%=assetListDB.size()%>);" />
	</form>
</body>
</html>


</div>
<!-- Mask to cover the whole screen -->
<div id="mask"></div>

<script src="js/jquery.idletimer.js" type="text/javascript"></script>
<script src="js/jquery.idletimeout.js" type="text/javascript"></script>
<!--jQuery plugin to set session timeout -->
<script type="text/javascript">
    $.idleTimeout('#idletimeout', '#idletimeout a', {
        //idleAfter : 20,
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
