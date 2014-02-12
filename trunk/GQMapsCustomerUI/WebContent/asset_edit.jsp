<%@page
	import="com.gq.AssetData, com.gq.ui.object.DomainData, java.util.*,com.gq.meter.object.*, com.google.gson.*;"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Configure Assets</title>
<link type="text/css"
	href="jquery-ui-1.10.2.custom/css/blitzer/jquery-ui-1.10.2.custom.css"
	rel="stylesheet" />
<link type="text/css" href="css/gqmaps.css" rel="stylesheet" />
<link href = "css/timeout.css" rel = "stylesheet" type = "text/css" />
<link href="css/dashboard.css" rel="stylesheet" type="text/css" />
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,600,700' rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />

<script src="jquery-ui-1.10.2.custom/js/jquery-1.9.1.js"></script>
<script src="jquery-ui-1.10.2.custom/js/jquery-ui-1.10.2.custom.js"></script>
<script type="text/javascript" src = "http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.js" charset="utf-8"></script>
<script type="text/javascript" src = "http://www.google.com/jsapi" charset="utf-8"></script>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=AIzaSyBGIi2rSK9Qp2dY1EewX-JUsK0sjQsxAak&sensor=false"></script>
  

<script type="text/javascript" src="js/DateTimePickerJS/DateTimePicker.js"></script>  
<script type="text/javascript" src="js/plugins/tables/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js"></script> 

<script src="js/jstorage.js"> </script>
<script src="js/rest_service.js"> </script>
<script src = "js/general.js"> </script>  
<script src="js/asset_edit.js"></script>  

<script type="text/javascript" src="js/charts/graph.js"></script>
<script type="text/javascript" src="js/charts/chart1.js"></script>
<script type="text/javascript" src="js/charts/chart2.js"></script>
<script type="text/javascript" src="js/charts/chart3.js"></script>
<script type="text/javascript" src="js/plotgraph.js"></script> 


<script type="text/javascript" src="js/plugins/charts/excanvas.min.js"></script>
<script type="text/javascript" src="js/plugins/charts/jquery.flot.js"></script>
<script type="text/javascript" src="js/plugins/charts/jquery.flot.resize.js"></script>
<script type="text/javascript" src="js/plugins/charts/jquery.sparkline.min.js"></script>

<script type="text/javascript" src="js/plugins/ui/jquery.easytabs.min.js"></script>
<script type="text/javascript" src="js/plugins/ui/jquery.collapsible.min.js"></script>
<script type="text/javascript" src="js/plugins/ui/jquery.mousewheel.js"></script>
<script type="text/javascript" src="js/plugins/ui/prettify.js"></script>
<script type="text/javascript" src="js/plugins/ui/jquery.bootbox.min.js"></script>
<script type="text/javascript" src="js/plugins/ui/jquery.colorpicker.js"></script>
<script type="text/javascript" src="js/plugins/ui/jquery.timepicker.min.js"></script>
<script type="text/javascript" src="js/plugins/ui/jquery.jgrowl.js"></script>
<script type="text/javascript" src="js/plugins/ui/jquery.fancybox.js"></script>
<script type="text/javascript" src="js/plugins/ui/jquery.fullcalendar.min.js"></script>
<script type="text/javascript" src="js/plugins/ui/jquery.elfinder.js"></script>

<script type="text/javascript" src="js/plugins/uploader/plupload.js"></script>
<script type="text/javascript" src="js/plugins/uploader/plupload.html4.js"></script>
<script type="text/javascript" src="js/plugins/uploader/plupload.html5.js"></script>
<script type="text/javascript" src="js/plugins/uploader/jquery.plupload.queue.js"></script>

<script type="text/javascript" src="js/plugins/forms/jquery.uniform.min.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.autosize.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.inputlimiter.min.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.tagsinput.min.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.inputmask.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.select2.min.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.listbox.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.validation.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.validationEngine-en.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.form.wizard.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.form.js"></script>

<script type="text/javascript" src="js/plugins/tables/jquery.dataTables.min.js"></script>

<script type="text/javascript" src="js/files/bootstrap.min.js"></script>

<script type="text/javascript" src="js/files/functions.js"></script>

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

		var assetRegExp = /^[A-Za-z0-9\/\s\.-]+$/;

		if(vUsage === "" || vUsage === null) 
		{
			alert('Unnamed Asset');
			$("#txtAssetUsage"+j).select();
			return false;
		}
		else if (!assetRegExp.test(vUsage)) 
		{
			alert('Please enter asset usage details without special characters except .,- and space '+'  '+ vUsage);
				return false;
		}
		
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
			"type_id": vCompType        
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
			 assets.assetData[i].type_id != assetsDB.assetDataDB[i].type_id)
		 {
		 	assetsForUpdate.assetUpdateData.push(assets.assetData[i]);
		 }
		
 	});
 			
	if(assetsForUpdate.assetUpdateData.length >= 1)
	{
		var vType = "PUT";
		var vUrl = $.jStorage.get("jsDBUrl") + "AssetEditServices/updateAssetData?enterpriseId=" + $.jStorage.get("jsEntpId");
				
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
					"type_id": response["assetResult"][i]["type_id"]        
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
</head>
<body id="table-body">

<!-- Timeout Div -->
		<div id = "idletimeout">
        	Logging off in <span> countdown place holder </span>&nbsp;seconds due to inactivity.
        	<a id = "idletimeout-resume" href = "#"> Click here to continue </a>.
    	</div>   
  <div id="wrap">
   
	<!-- Fixed top -->
	<div id="top">
		<div class="fixed">
			<a href="#" title="" class="logo"></a>
			<img class="logo-menu" src="images/image001.jpg"></img>
		</div>
	</div>
	<!-- /fixed top -->
    
    <%
			int i = 0;
			int j = 0;
			int k = 0;
			int l = 0;
			String entp = "";
			char aryStatus[] = { 'y', 'n' };
			String activeStatus[]={"Yes","No"};
			String aryOwnership[] = { "Own", "Lease" };
			String aryLocation[] = { "dc", "ep" };
			String activeLocation[]={"Datacenter","Enterprise"};

			Asset a = new Asset();
			AssetData assetData = new AssetData();

			// Asset list
			List<Asset> assetListDB = new ArrayList<Asset>();

			// All domain data list
			List<DomainData> domainDataListDB = new ArrayList<DomainData>();

			//getting the user id of the enterprise
			entp = request.getParameter("setEntp");
			
			try
			{
				assetListDB = assetData.getAssetList(entp);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				domainDataListDB = assetData.getDomainDataList(entp);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
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
            
	<form id="frmAsset" name="frmAsset">
	
	<!-- Content container -->
	<div id="container">

		    <!-- Content wrapper -->
		    <div class="wrapper">
		    	<br/>
		    	<br/>

			    <h5 class="widget-name"><i class="icon-file"></i>Asset Edit for <%=entp%></h5>
			    <p>Computer Asset list</p>
	                	
                <!-- Table with checkboxes -->
                <div class="widget">
                	
                    <div class="table-overflow">
                        <table id="tblComputerList" class="table table-bordered table-checks" >
                          <thead>
                              <tr style="font-size:80%;">
                                  <th style="width:20px;">S.No</th>
                                  <th>Asset Active</th>
                                  <th>Asset Id</th>
                                  <th>IP Address</th>
                                  <th>Catalog</th>
                                  <th>Server Type</th>
                                  <th>Asset Usage</th>
                                  <th>Asset Importance</th>
                                  <th>Ownership</th>
                                  <th>Location</th>
                                  <th>Comp. type</th>
                                  
                              </tr>
                          </thead>
                          <tbody>
                          <%
				for (i = 0; i < assetListDB.size(); i++) {
						if (assetListDB.get(i).getProtocolId().equals("computer")) {
			%>
                              <tr style="font-size:80%;">
                                  <td style="width:20px;"><%=j = j + 1%></td>
                                  <td style="width:50px;">
                                    
                                  	
                                  	<select name=<%="cmbStatus" + i%> id=<%="cmbStatus" + i%> class="styled" style="width:50px; height:20px;" >
                                  	<%
							for (int status = 0; status < aryStatus.length; status++) {
														if (assetListDB.get(i).getActive() == aryStatus[status]) {
						%>
                                        <option value="<%=(char) aryStatus[status]%>" selected="selected"><%=activeStatus[status]%></option>
						<%
							} else {
						%>
						<option value="<%=(char) aryStatus[status]%>"><%=activeStatus[status]%></option>
						<%
							}
													}
						%>
                                    </select>

                                
                                  </td>
                                  <td style="width:100px; ">
                                  <span id=<%="assetID" + i%>
									name=<%="assetID" + i%>> <%=assetListDB.get(i).getAssetId()%>
									</span></td>
									<td style="width:90px; ">
                                    <span id=<%="ipAddr" + i%> name=<%="ipAddr" + i%>> <%=assetListDB.get(i).getIpAddr()%>
									</span>
                                  </td>
                                  <td style="width:70px;height:20px;">
                                   
                                    
                                    <select name=<%="cmbCatalog" + i%> id=<%="cmbCatalog" + i%> class="styled" style="width:110px;height:20px;" >
                                        <%
							for (int catalog = 0; catalog < compCatalogListDB.size(); catalog++) {
														if (assetListDB.get(i).getCtlgId().equals(compCatalogListDB
																.get(catalog).getId())) {
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
                                    </select>                                  

                                  </td>
                                  <td style="width:80px;height:20px;">
                                    
                                    
                                    <select name=<%="cmbSrvrApp" + i%> id=<%="cmbSrvrApp" + i%> class="styled" style="width:110px;height:20px;" >
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
                                    </select>

                                  </td>

                                  <td style="width:100px;height:20px;">
                                    <input type="text" id=<%="txtAssetUsage" + i%> name=<%="txtAssetUsage" + i%>
									value="<%=assetListDB.get(i).getAssetUsg()%>" maxlength="50"  class="input-small" placeholder="Asset Usage">

                                  </td>

                                  <td style="width:60px;height:20px;">
                                    
                                    
                                    <select name=<%="cmbImpLevel" + i%>
									id=<%="cmbImpLevel" + i%> class="styled" style="width:100px;height:20px;" >
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
                                    </select>

                                  </td>

                                  <td style="width:80px;height:20px;">
                                   
                                    
                                    <select name=<%="cmbOwnership" + i%>
									id=<%="cmbOwnership" + i%> class="styled" style="width:70px;height:20px;" >
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
                                    </select>

                                  </td>

                                  <td style="width:60px;height:20px;">
                                   
                                    
                                    <select name=<%="cmbLocation" + i%>
									id=<%="cmbLocation" + i%> class="styled" style="width:70px;height:20px;" >
                                        <%
							for (int loc = 0; loc < aryLocation.length; loc++) {
														if (assetListDB.get(i).getDcEnt().equals(aryLocation[loc])) {
						%>
						<option value="<%=aryLocation[loc]%>" selected="selected"><%=activeLocation[loc]%></option>
						<%
							} else {
						%>
						<option value="<%=aryLocation[loc]%>"><%=activeLocation[loc]%></option>
						<%
							}
													}
						%>
                                    </select>

                                  </td>

                                  <td style="width:70px;height:20px;">
                                    
                                    
                                    <select name=<%="cmbCompType" + i%>
									id=<%="cmbCompType" + i%> class="styled" style="width:70px;height:20px;" >
                                        <%
							for (int comp = 0; comp < compTypeListDB.size(); comp++) {
														if (assetListDB.get(i).getType_id()
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
                                    </select>

                                  </td>
                                  
                              </tr>
                             
                          </tbody>
                           <%
				}
					}
			%>
                        </table>
                    </div>
                </div>
                <!-- /table with checkboxes -->
                
                <p>Printer Asset list</p>
	                	
                <!-- Table with checkboxes -->
                <div class="widget">
                	
                    <div class="table-overflow">
                        <table style="width:1000px;" id="tblPrinterList" class="table table-bordered table-checks" >
                          <thead>
                              <tr style="font-size:80%;">
                                  <th style="width:20px;">S.No</th>
                                  <th>Asset Active</th>
                                  <th>Asset Id</th>
                                  <th>IP Address</th>
                                  <th>Catalog</th>
                                  <th>Asset Usage</th>
                                  <th>Asset Importance</th>
                                  <th>Ownership</th>
                                  <th>Location</th>
                                  
                                  
                              </tr>
                          </thead>
                          <tbody>
                			<%
				for (i = 0; i < assetListDB.size(); i++) {
						if (assetListDB.get(i).getProtocolId().equals("printer")) {
			%>
                <tr style="font-size:80%;">
                                  <td style="width:20px;"><%=k = k + 1%></td>
                                  <td style="width:50px;">
                                    
                                  	
                                  	<select name=<%="cmbStatus" + i%> id=<%="cmbStatus" + i%> class="styled" style="width:50px; height:20px;" >
                                  	<%
							for (int status = 0; status < aryStatus.length; status++) {
														if (assetListDB.get(i).getActive() == aryStatus[status]) {
						%>
						<option value="<%=(char) aryStatus[status]%>" selected="selected"><%=activeStatus[status]%></option>
						<%
							} else {
						%>
						<option value="<%=(char) aryStatus[status]%>"><%=activeStatus[status]%></option>
						<%
							}
													}
						%>
                                    </select>

                                  </td>
                                  <td style="width:120px; ">
                                  <span id=<%="assetID" + i%>
									name=<%="assetID" + i%>> <%=assetListDB.get(i).getAssetId()%>
									</span></td>
									<td style="width:110px; ">
                                    <span id=<%="ipAddr" + i%> name=<%="ipAddr" + i%>> <%=assetListDB.get(i).getIpAddr()%>
									</span>
                                  </td>
                                  <td style="width:90px;height:20px;">
                                   
                                    
                                    <select  name=<%="cmbCatalog" + i%> id=<%="cmbCatalog" + i%> class="styled" style="width:110px;height:20px;" >
                                        <%
							for (int catalog = 0; catalog < printerCatalogListDB.size(); catalog++) {
														if (assetListDB.get(i).getCtlgId().equals(printerCatalogListDB
																.get(catalog).getId())) {
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
                                    </select>                                  

                                  </td>
                                  <td style="width: 60px; display: none;"><input type="text"
					id=<%="cmbSrvrApp" + i%> name=<%="cmbSrvrApp" + i%>
					disabled="disabled" hidden="hidden" value="null" style="width: 100px;" /></td>
                <td style="width:100px;height:20px;">
                                    <input type="text" id=<%="txtAssetUsage" + i%> name=<%="txtAssetUsage" + i%>
									value="<%=assetListDB.get(i).getAssetUsg()%>" maxlength="50"  class="input-small" placeholder="Asset Usage">

                                  </td>

                                  <td style="width:100px;height:20px;">
                                    
                                    
                                    <select name=<%="cmbImpLevel" + i%>
					id=<%="cmbImpLevel" + i%> class="styled" style="width:100px;height:20px;" >
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
                                    </select>

                                  </td>

                                  <td style="width:60px;height:20px;">
                                   
                                    
                                    <select name=<%="cmbOwnership" + i%>
									id=<%="cmbOwnership" + i%> class="styled" style="width:60px;height:20px;" >
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
                                    </select>

                                  </td>

                                  <td style="width:60px;height:20px;">
                                   
                                    
                                    <select name=<%="cmbLocation" + i%>
									id=<%="cmbLocation" + i%> class="styled" style="width:60px;height:20px;" >
                                       <%
							for (int loc = 0; loc < aryLocation.length; loc++) {
														if (assetListDB.get(i).getDcEnt().equals(aryLocation[loc])) {
						%>
						<option value="<%=aryLocation[loc]%>" selected="selected"><%=activeLocation[loc]%></option>
						<%
							} else {
						%>
						<option value="<%=aryLocation[loc]%>"><%=activeLocation[loc]%></option>
						<%
							}
													}
						%>
                                    </select>

                                  </td>
                                  <td style="width: 100px; display: none;"><input type="text"
					name=<%="cmbCompType" + i%> id=<%="cmbCompType" + i%>
					disabled="disabled" hidden="hidden" value="null" style="width: 100px;" /></td>
                   </tr>
                             
                          </tbody>
                           <%
				}
					}
			%>
                        </table>
                    </div>
                </div>
                <!-- /table with checkboxes -->
                
                
                
                
                <p>Network, Switch, Router and Gateway List</p>
	                	
                <!-- Table with checkboxes -->
                <div class="widget">
                	
                    <div class="table-overflow">
                        <table id="tblNsrg" class="table table-bordered table-checks" style="width:800px;" >
                          <thead>
                              <tr style="font-size:80%;">
                                  <th style="width:20px;">S.No</th>
                                  <th>Asset Active</th>
                                  <th>Asset Id</th>
                                  <th>IP Address</th>
                                  <th>Catalog</th>
                                  <th>Ownership</th>
                                  <th>Location</th>
                              </tr>
                          </thead>
                          <tbody>
                		<tbody>
                			<%
				for (i = 0; i < assetListDB.size(); i++) {
						if (assetListDB.get(i).getProtocolId().equals("nsrg")) {
			%>
                <tr style="font-size:80%;">
                                  <td style="width:20px;"><%=l = l + 1%></td>
                                  <td style="width:50px;">
                                    
                                  	
                                  	<select name=<%="cmbStatus" + i%> id=<%="cmbStatus" + i%> class="styled" style="width:50px; height:20px;" >
                                  	<%
							for (int status = 0; status < aryStatus.length; status++) {
														if (assetListDB.get(i).getActive() == aryStatus[status]) {
						%>
						<option value="<%=(char) aryStatus[status]%>" selected="selected"><%=activeStatus[status]%></option>
						<%
							} else {
						%>
						<option value="<%=(char) aryStatus[status]%>"><%=activeStatus[status]%></option>
						<%
							}
													}
						%>
                                    </select>

                                
                                  </td>
                                  <td style="width:110px; ">
                                  <span id=<%="assetID" + i%>
									name=<%="assetID" + i%>> <%=assetListDB.get(i).getAssetId()%>
									</span></td>
									<td style="width:90px; ">
                                    <span id=<%="ipAddr" + i%> name=<%="ipAddr" + i%>> <%=assetListDB.get(i).getIpAddr()%>
									</span>
                                  </td>
                                  <td style="width:100px;height:20px;">
                                   
                                    
                                    <select  name=<%="cmbCatalog" + i%> id=<%="cmbCatalog" + i%> class="styled" style="width:110px;height:20px;" >
                                      <%
							for (int catalog = 0; catalog < nsrgCatalogListDB.size(); catalog++) {
														if (assetListDB.get(i).getCtlgId().equals(nsrgCatalogListDB
																.get(catalog).getId())) {
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
                                    </select>                                  

                                  </td>
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
                		<td style="width:70px;height:20px;">
                                   
                                    
                                    <select name=<%="cmbOwnership" + i%>
									id=<%="cmbOwnership" + i%> class="styled" style="width:70px;height:20px;" >
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
                                    </select>

                                  </td>

                                  <td style="width:70px;height:20px;">
                                   
                                    
                                    <select name=<%="cmbLocation" + i%>
									id=<%="cmbLocation" + i%> class="styled" style="width:70px;height:20px;" >
                                    <%
							for (int loc = 0; loc < aryLocation.length; loc++) {
														if (assetListDB.get(i).getDcEnt().equals(aryLocation[loc])) {
						%>
						<option value="<%=aryLocation[loc]%>" selected="selected"><%=activeLocation[loc]%></option>
						<%
							} else {
						%>
						<option value="<%=aryLocation[loc]%>"><%=activeLocation[loc]%></option>
						<%
							}
													}
						%>
                                    </select>

                                  </td>
                	<td style="display: none;"><input type="text"
					name=<%="cmbCompType" + i%> id=<%="cmbCompType" + i%>
					disabled="disabled" value="null" style="width: 100px;" /></td>
                	 </tr>
                             
                          </tbody>
                           <%
				}
					}
			%>
                        </table>
                    </div>
                </div>
                <!-- /table with checkboxes -->

                <div class="span6 well body " style="height:50px;">
            
		            <button class="btn btn-success" type="button" id="submit" name="submit" style="position:relative; top:-10px; left:0px;" onClick="updateAsset(<%=assetListDB.size()%>);">Submit</button>
		            <button class="btn btn-warning" type="button" style="position:relative; top:-10px; left:10px;" onClick="gotoDashboard()"> Back to Dashboard </button>

          		</div>   
	                        
		    </div>
		    <!-- /content wrapper -->
	</div>
	<!-- /content container -->


	</form>
	</div>
	<!-- /wrap -->
</body>
</html>
	
<!-- Mask to cover the whole screen -->
	<div id = "mask"> </div>
		 	
	<script src = "js/jquery.idletimer.js" type = "text/javascript" > </script>
	<script src = "js/jquery.idletimeout.js" type = "text/javascript" > </script>
        
	<script type = "text/javascript" >
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