$(document).ready(function() 
{
   $.jStorage.set("jsUrl", "/GQMapsRegistrationServices/");
   $.jStorage.set("jsDBUrl", "/GQMapsCustomerServices/");
	//$.jStorage.set("jsUrl", "http://54.163.170.159:8080/GQMapsRegistrationServices/");
	//$.jStorage.set("jsDBUrl", "http://54.163.170.159:8080/GQMapsCustomerServices/");
});

function logout()
{
	$.jStorage.flush();
}

/*

=================================================================================================================================================================	
asset_edit.html services

http://localhost:8080/GQMapsCustomerServices/mapsDomainServices/getMapsDomainData?enterpriseId=intel
http://localhost:8080/GQMapsCustomerServices/AssetEditServices/getAssetData?enterpriseId=aps
http://localhost:8080/GQMapsCustomerServices/AssetEditServices/updateAssetData?enterpriseId=aps

http://localhost:8080/GQMapsCustomerServices/mapsDomainServices/getMapsDomainData?enterpriseId=intel
http://localhost:8080/GQMapsCustomerServices/AssetEditServices/getAssetData?enterpriseId=intel
http://localhost:8080/GQMapsCustomerServices/AssetEditServices/updateAssetData?enterpriseId=intel

 [
{
  "assetId":"C-00e01c3c16e4",
  "ipAddr":"192.168.1.80",
  "ctlgId":"default_desktop",
  "srvrAppId":"2",
  "assetUsg":"assetUsage",
  "impLvl":"2",
  "ownership":"Own",
  "dcEnt":"DC",
  "active":"y",
  "typeId":"desktop"
 },
{
  "assetId":"C-000c29361417",
  "ipAddr":"192.168.1.23",
  "ctlgId":"default_desktop",
  "srvrAppId":"2",
  "assetUsg":"assetUsage",
  "impLvl":"2",
  "ownership":"Own",
  "dcEnt":"DC",
  "active":"n",
  "typeId":"desktop"
 }
]

				var jsonLineData = {"cols":[{"id":"DateTime","label":"col0","type":"datetime","pattern":""},{"id":"CPULoad","label":"col1","type":"number","pattern":""}],"rows":[{"c":[{"v":new Date(2013,8,1,10,4,50)},{"v":2.0}]},{"c":[{"v":new Date(2013,8,1,10,56,57)},{"v":6.0}]},{"c":[{"v":new Date(2013,8,1,11,0,24)},{"v":3.0}]},{"c":[{"v":new Date(2013,8,1,11,3,25)},{"v":3.0}]},{"c":[{"v":new Date(2013,8,1,12,32,0)},{"v":1.0}]},{"c":[{"v":new Date(2013,8,1,12,32,50)},{"v":1.0}]},{"c":[{"v":new Date(2013,8,1,13,44,33)},{"v":2.0}]},{"c":[{"v":new Date(2013,8,1,14,6,52)},{"v":3.0}]},{"c":[{"v":new Date(2013,8,1,14,9,43)},{"v":10.0}]}]}	
=================================================================================================================================================================				

login.html services

GET
enterprise/getRegistration

=================================================================================================================================================================				
				
edit_registration.html services

GET
enterprise/updateRegistration
enterpriseMeters/addEntMeters
gatekeeper/addEntAudit
general/getEntpSummaryList
enterpriseMeters/getEntMeters
"enterpriseMeters/getEntMeters/?entpId=" + arrEntp[i].getEId();
enterprise/getRegistration				


POST
enterpriseMeters/addEntMeters
gatekeeper/addEntAudit


PUT
enterprise/updateRegistration
					
=================================================================================================================================================================				
				
dashboard_full.html services

http://localhost:8080/GQMapsCustomerServices/DashboardServices/getdashboard
=================================================================================================================================================================					
				
goal.html

http://localhost:8080/GQMapsCustomerServices/goalServices/goal?goalId=pue&entpId=servion	//get			
http://localhost:8080/GQMapsCustomerServices/goalSnapshot/goal?flag=final  //put
http://localhost:8080/GQMapsCustomerServices/DashboardServices/getdashboard?entpId=aps  //get g & tasks

				
http://localhost:8080/GQMapsCustomerServices/procedure/getproc?entpId=aps 	

http://localhost:8080/GQMapsCustomerServices/goalmaster/goals

				*/