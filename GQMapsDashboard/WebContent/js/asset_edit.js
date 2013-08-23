
$(document).ready(function() 
{
	alert($.jStorage.get("jsEName"));
	
	loadDomainMasters();
	
});

function loadDomainMasters()
{
	
	var assetJson = 
{
    "assetDetails": [
        {
            "assetId": "C-000c293c937a",
            "protocolId": "computer",
            "name": "SRV1004",
            "descr": "hardware: intel64 family 6 model 44 stepping 2 at/at compatible - software: windows version 6.0 (build 6002 multiprocessor free)",
            "contact": null,
            "location": null,
            "ctlgId": null,
           // "srvrAppId": 1,
            "assetUsg": "assetUsg",
            "impLvl": null,
            "ownership": null,
            "dcEnt": null,
            "active": "y",
            "inactiveDttm": null,
            "typeId": null,
            "assetStrength": null,
            "area": null
        },
        {
            "assetId": "P-101f74489436",
            "protocolId": "printer",
            "name": "GQCHNP01",
            "descr": "hp ethernet multi-environment,sn:cnc9c8bd2x,fn:p9336qh,svcid:22031,pid:hp laserjet m1536dnf mfp",
            "contact": null,
            "location": null,
            "ctlgId": null,
            "srvrAppId": 1,
            "assetUsg": "assetUsg",
            "impLvl": null,
            "ownership": null,
            "dcEnt": null,
            "active": "n",
            "inactiveDttm": null,
            "typeId": null,
            "assetStrength": null,
            "area": null
        },
        {
            "assetId": "P-101f74489436",
            "protocolId": "nsrg",
            "name": "GQCHNSW01",
            "descr": "cisco ios software, c2960 software (c2960-lanbase-m), version 12.2(35)se5, release software (fc1) copyright (c) 1986-2007 by cisco systems, inc. compiled thu 19-jul-07 20:06 by nachen",
            "contact": null,
            "location": null,
            "ctlgId": null,
            "srvrAppId": null,
            "assetUsg": "assetUsg",
            "impLvl": null,
            "ownership": null,
            "dcEnt": null,
            "active": "y",
            "inactiveDttm": null,
            "typeId": null,
            "assetStrength": null,
            "area": null
        }
    ]
};

	
	var vType = "GET";		
					
	//var vUrl = $.jStorage.get("jsDBUrl") + "";	
	var vUrl = "http://localhost:8080/GQMapsCustomerServicesAssetEdit/gqm-gqedp/mapsDomainServices/getMapsDomainData";
	
	http://localhost:28080/GQMapsCustomerServices/gqm-gqedp/getAssetServices/getAssetData

	var vValues = "";
	var vAssetValues = "";

	alert(assetJson.assetDetails.length);
	alert(assetJson.assetDetails[0]["srvrAppId"]);
	
	var jsonobj = {"name":"George", "age":29, "friends":["John", "Sarah", "Albert"]};
	alert(jsonobj);
	var jsonstr = JSON.stringify(jsonobj);
	alert(jsonstr);
	
	
	$.ajax
	({
		type:vType,
		contentType: "application/json",
		url:vUrl,
		async:false,
		dataType: "json",
		success:function(json)
		{
			
				//var obj = jQuery.parseJSON('{"name":"John"}');
				//alert( obj.name === "John" );
				alert(json);
				alert(JSON.stringify(json));
				var george=JSON.parse(json);
				alert(george.devCtlgResult[0].ctlgId);
				var obj1 = eval ('(' + json + ')');
				alert(obj1.devCtlgResult[0].ctlgId);
				
				$.each(assetJson.assetDetails, function(i, n)
				{
					
					vAssetValues = vAssetValues + " <tr>";
					
					vAssetValues = vAssetValues + " <td> <span id = 'sno' + i> 1 </span> </td>";
					
					vAssetValues = vAssetValues + " <td> <select id = 'cmbAssetActive'" + i + " name = 'cmbAssetActive'> <option value = 'yes'>Yes</option>";
					vAssetValues = vAssetValues + " <option value = 'no'> No </option> </select> </td>";
					
					vAssetValues = vAssetValues + " <td> <span id = 'assetId' + i> C - 123 </span> </td>"; 					
					vAssetValues = vAssetValues + " <td> <span id = 'ipAdd' + i> 192.168.1.95 </span> </td>";
					
					vAssetValues = vAssetValues + " <td> <select id = 'cmbCatalog'" + i + " name = 'cmbCatalog'> </select> </td>";
					
					vAssetValues = vAssetValues + "	<td> <select id = 'cmbServerType'" + i + " name = 'cmbServerType'> </select> </td> ";
					vAssetValues = vAssetValues + " <td> Verde Server </td> ";
					
					vAssetValues = vAssetValues + " <td> <select id = 'cmbAssetImp'" + i + " name = 'cmbAssetImp'> </select> </td>"; 
					
					vAssetValues = vAssetValues + " <td> <select id = 'cmbOwnership' " + i + " name = 'cmbOwnership'> <option value = 'own'>Own</option>";
					vAssetValues = vAssetValues + " <option value = 'lease'>Lease</option> </select> </td> ";	
																			
					vAssetValues = vAssetValues + " <td> <select id = 'cmbLocation' " + i + " name = 'cmbLocation'> <option value = 'dc'>DC</option> ";
					vAssetValues = vAssetValues + " <option value = 'enterprise'>Enterprise</option> </select> </td>";
					
					vAssetValues = vAssetValues + " <td>  <select id = 'cmbCompType'" + i + " name = 'cmbCompType'> </select> </td> ";
					
					vAssetValues = vAssetValues + "</tr>";
	
					/*
					<tr>
						<td> 1 </td>                                     
						<td> 
							<select id = "cmbAssetActive" name = "cmbAssetActive">
								<option value = "yes">Yes</option>
								<option value = "no"> No </option>
							</select> 
						</td>
					   
						<td> 192.168.1.95 </td>
						<td> 
							<select id = "cmbCatalog" name = "cmbCatalog">

							
							</select> 
						</td>    
						<td> 
							<select id = "cmbServerType" name = "cmbServerType">

							
							</select> 
						</td>  
						<td> Verde Server </td>
						
						<td> 
							<select id = "cmbAssetImp" name = "cmbAssetImp">

							
							</select> 
						</td>  
						<td> 
							<select id = "cmbOwnership" name = "cmbOwnership">
								<option value = "own">Own</option>
								<option value = "lease">Lease</option>
							</select> 
						</td>    
						<td> 
							<select id = "cmbLocation" name = "cmbLocation">
								<option value = "dc">DC</option>
								<option value = "enterprise">Enterprise</option>
							</select> 
						</td>                                                                                                                   	                        <td> 
							<select id = "cmbCompType" name = "cmbCompType">

							</select> 
						</td>                                          
					</tr>   */
 						

					
					//loading Catalog IDs
					$.each(json.devCtlgResult, function(j,n)
					{
						vValues = vValues + '<option value = "'+ json.devCtlgResult[j]["ctlgId"] + '" >' + json.devCtlgResult[j]["descr"] + '</option>';
						
					});
		
					$("#cmbCatalog" + i).append(vValues);  	
					
					vValues = "";
					
					//loading Server types
					$.each(json.srvrAppType, function(j,n)
					{
						vValues = vValues + '<option value = "'+ json.srvrAppType[j]["srvrAppId"] + '" >' + json.srvrAppType[j]["descr"] + '</option>';
						
					});
					
		
					$("#cmbServerType" + i).append(vValues);	
					
					vValues = "";
					
		
					$.each(json.assetImp, function(i,n)
					{
						vValues = vValues + '<option value = "'+ json.assetImp[j]["impLvl"] + '" >' + json.assetImp[j]["descr"] + '</option>';
						
					});
					$("#cmbAssetImp" + i).append(vValues);	
					
					vValues = "";
					
					//loading Computer types
					$.each(json.compType, function(j,n)
					{
						vValues = vValues + '<option value = "'+ json.compType[j]["typeId"] + '" >' + json.compType[j]["descr"] + '</option>';
						
					});
					$("#cmbCompType" + i).append(vValues);		
			
				});							

		},
		error:function(json)
		{
			alert("Error from loading domain masters: " + json.status + " " + json.responseText);
		} 
	});
}

function showPieChart()
{
	/*
		for (var key in json) 
		{
			alert(' Key=' + key + ' value=' + json[key]);
		}
		
		for( var i = 0; i < json.DevCtlgResult.length; i++)
		 {
			alert(json.DevCtlgResult[i]["ctlgId"]);
		 }
			  
		 */	
}


