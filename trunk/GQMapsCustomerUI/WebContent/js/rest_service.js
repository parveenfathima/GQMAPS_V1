$(document).ready(function() 
{
	$.jStorage.set("jsUrl", "http://192.168.1.95:8080/GQMapsRegistrationServices/gqm-gk/");
	$.jStorage.set("jsDBUrl", "http://192.168.1.95:8080/GQMapsCustomerServices/gqm-gqedp/");
});

function logout()
{
	$.jStorage.flush();
}

/*

=================================================================================================================================================================	
asset_edit.html services

http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/mapsDomainServices/getMapsDomainData
http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/getAssetServices/getAssetData // to be replaced
http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/AssetEditServices/getAssetData

http://localhost:8080/GQMapsCustomerServices1/gqm-gqedp/updateAssetServices/updateAssetData // to be replaced

http://localhost:8080/GQMapsCustomerServices1/gqm-gqedp/AssetEditServices/updateAssetData
{
            "assetId": "C-8c89a539be66",
            "name": "GQCHNSW01",
            "descr": "cisco ios s",
            "ipAddr": "192.168.1.90",
            "contact": "ggg",
            "location": "ggg",
            "ctlgId": "default_computer",
            "srvrAppId": "1",
            "assetUsg": "assetUsg",
            "impLvl": "1",
            "ownership": "Own",
            "dcEnt": "dc",
            "active": "n",
            "inactiveDttm": "2013-08-06 12:56:09",
            "typeId": "desktop"
            }

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

http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/DashboardServices/getdashboard
=================================================================================================================================================================					
				
goal.html

http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/goalServices/goal?goalId=pue&entpId=servion	//get			
http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/goalSnapshot/goal?flag=final  //put
http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/DashboardServices/getdashboard?entpId=talk  //get g & tasks

				
http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/procedure/getproc	

				*/