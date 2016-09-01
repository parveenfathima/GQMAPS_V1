<%@page import = "com.gq.meter.object.Asset"%>
<%@page import = "com.gq.meter.object.DevCtlg"%>
<%@page import = "java.util.ArrayList"%>
<%@page import = "java.util.List"%>
<%@page import = "com.gq.cust.AssetHelper"%>

<!doctype html>  
<html lang = "en">
	
	<head>
	
		<meta http-equiv = "Content-Type" content = "text/html; charset=ISO-8859-1">
		<title> Asset List </title>
		
		<!--  Custom Styles -->
		<link href = "css/dashboard.css" rel = "stylesheet" type = "text/css" />
		<link href = "css/timeout.css" rel = "stylesheet" type = "text/css" />
		
		<!-- Predefined Styles -->
		<link href = 'http://fonts.googleapis.com/css?family=Open+Sans:400,600,700' rel = 'stylesheet' type = 'text/css' />
		
		<!-- Predefined Script -->
		<script type = "text/javascript" src = "http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"> </script>
		
		<!-- Custom Scripts -->
		<script src = "js/jstorage.js"> </script>
		<script src = "js/dashboard_full.js"> </script>
		<script src = "js/rest_service.js"> </script>
		<script src = "js/general.js"> </script>
		
		<script>

		function handleClick(){

			var assetTable = document.getElementById("tblComputerList");
			var chk_arr =  document.getElementsByName("chkApply");
			var total = 0;
			var ipaddresses = '';
			var assetIds = '';

			for (var i = 1 ; i < assetTable.rows.length ; i++) {

				row = assetTable.rows[i];
				
				if ( row.getElementsByTagName('td')[0].getElementsByTagName('input')[0].checked ) {
					total = total+ parseInt(row.cells[4].innerHTML);
					ipaddresses = ipaddresses + ',' + row.cells[3].innerHTML;
					assetIds = assetIds + ',' + row.cells[1].innerHTML;
				} 
			}//for ends
			document.getElementById('consolidateResult').innerHTML 
					= "Total Benefits = " + total + ' , Ip Addr(s) ' + ipaddresses + ' , Asset Id(s) ' + assetIds;
		}
		
		</script>
		
	</head>
	
	<body id = "table-body">
	
		<!-- Timeout Div -->
		<div id = "idletimeout">
			Logging off in <span> countdown place holder </span>&nbsp;seconds due to inactivity.
			<a id = "idletimeout-resume" href = "#"> Click here to continue </a>.
		</div>
		
		<div id = "wrap">
			
			<!-- Fixed top -->
			<div id = "top">
				<div class = "fixed">
					<a href = "#" title = "" class = "logo"></a>
					<img class = "logo-menu" src = "images/image001.jpg"></img>
				</div>
			</div>
			<!-- /fixed top -->
			
			<form>
				
				<!-- Content container -->
				<div id = "container">
				
					<!-- Content -->
					<div id = "content">
					
						<!-- Content wrapper -->
						<div class = "wrapper">
						
							<br/><br/>
						
							<h5 class = "widget-name">
								<i class = "icon-columns"></i> 
								Server List 
							</h5>
							
							<p> If you are choosing one asset to be monitored, Copy asset id and paste 
								to the form manually. If you want to calculate benefits choose multiple assets.
							</p>
							
							<!-- widget -->								
							<div class = "widget">
								
								<!-- table overflow -->
								<div class = "table-overflow">
								
									<table id = "tblComputerList" class = "table table-bordered table-checks" >
										
										<thead>
										
											<tr>
												<th style = "font-size:80%;"> Apply </th>
												<th style = "font-size:80%;"> Asset ID </th>
												<th style = "font-size:80%;"> Usage Dtls </th>
												<th style = "font-size:80%;"> IP Address </th>
												<th style = "font-size:80%;"> Cost </th>
												<th style = "font-size:80%;"> Description </th>
											</tr>
										
										</thead>
										
										<% 	
											int i = 0;
											int j = 0;
											int cost = 0;
											String ctlgDesc = "";
											String assetUsg = "";
											String enterpriseId = request.getParameter("entpId") ;
											// Asset list
											List<Asset> assetListDB = AssetHelper.getAssetList(enterpriseId);
											DevCtlg d = new DevCtlg();
											List<DevCtlg> dcListDB = AssetHelper.getDevCtlgList(enterpriseId);
										%>
										
										<tbody>
										
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
											
												<td>
													<input type = "checkbox" id = "chkApply" class = "styled">
												</td>
												
												<td style = "font-size:80%;">
													<%=assetListDB.get(i).getAssetId()%>
												</td>
												
												<%
													if( (assetListDB.get(i).getAssetUsg()) == null) {
														assetUsg = "Please enter asset usage details";
													}
													else {
														assetUsg = assetListDB.get(i).getAssetUsg();
													}
												%>
												
												<td style = "font-size:80%;">
													<%=assetUsg%>
												</td>
												
												<td style = "font-size:80%;">
													<%=assetListDB.get(i).getIpAddr()%>
												</td>
												
												<td style = "font-size:80%;">
													<%=cost%>
												</td>
												
												<td style = "font-size:80%;">
													<%=ctlgDesc%> 
												</td>
											
											</tr>
												
												<% 
													}
												%>
												
										</tbody>
										
									</table>
									
								</div>
								<!-- /table overflow -->
							
							</div>
							<!-- /widget -->
							
							<div class = "span6 well body " style = "height:70px;">
								
								<button class = "btn btn-success" type = "button" style = "position:relative; top:-10px; 
								left:0px;" onclick = "handleClick()"> Submit </button>
								<label id = "consolidateResult" class = "control-label" style = "position:relative; 
								top:-45px; left:80px; width:1000px; line-height:20px;"> </label>
							
							</div>
						
						</div>
						<!-- /content wrapper -->
						
					</div>
					<!-- /content -->
				
				</div>
				<!-- /container -->
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
		
	
		
		
	
