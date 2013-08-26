
$(document).ready(function() 
{
	alert($.jStorage.get("jsEName"));
	
	loadDomainMasters();
	
});

function loadDomainMasters()
{

	var vType = "GET";		
					
	//var vUrl = $.jStorage.get("jsDBUrl") + "";	
	//var vUrl = "http://localhost:8080/GQMapsCustomerServicesAssetEdit/gqm-gqedp/mapsDomainServices/getMapsDomainData";
	
	var vUrl = "http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/getAssetServices/getAssetData";

	var vValues = "";
	var vAssetValues = "";
	
	$.ajax
	({
		type:vType,
		contentType: "application/json",
		url:vUrl,
		async:false,
		dataType: "json",
		success:function(json)
		{
					alert("inside asset details");		
				$.each(json, function(i, n)
				{/*
					
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
					
					vAssetValues = vAssetValues + "</tr>"; */
					
					
	
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
					</tr>   
 						

					
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
					$("#cmbCompType" + i).append(vValues);		  */
			
				});							

		},
		error:function(json)
		{
			alert("Error from loading asset data: " + json.status + " " + json.responseText);
		} 
	}); 
}



