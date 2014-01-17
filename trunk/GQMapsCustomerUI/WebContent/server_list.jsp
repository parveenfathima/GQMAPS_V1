
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="com.gq.meter.object.Asset"%>
<%@page import="com.gq.meter.object.DevCtlg"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.gq.cust.AssetHelper"%>

<!DOCTYPE>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link type="text/css"
	href="jquery-ui-1.10.2.custom/css/blitzer/jquery-ui-1.10.2.custom.css"
	rel="stylesheet" />
<link type="text/css" href="css/gqmaps.css" rel="stylesheet" />
<link href="css/dashboard.css" rel="stylesheet" type="text/css" />
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,600,700' rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />

<script type="text/javascript" src = "http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.js" charset="utf-8"></script>
<script type="text/javascript" src = "http://www.google.com/jsapi" charset="utf-8"></script>
<script src="jquery-ui-1.10.2.custom/js/jquery-1.9.1.js"></script>
<script src="jquery-ui-1.10.2.custom/js/jquery-ui-1.10.2.custom.js"></script>
<script src = "js/jstorage.js"> </script>             
<script src = "js/rest_service.js"> </script>              
<script src = "js/dashboard_full.js"> </script>   
<script src = "js/general.js"> </script>    
<script src = "js/chart.js"> </script> 





<script type="text/javascript" src="js/plugins/charts/jquery.sparkline.min.js"></script>

<script type="text/javascript" src="js/plugins/ui/jquery.easytabs.min.js"></script>
<script type="text/javascript" src="js/plugins/ui/jquery.collapsible.min.js"></script>
<script type="text/javascript" src="js/plugins/ui/jquery.mousewheel.js"></script>
<script type="text/javascript" src="js/plugins/ui/prettify.js"></script>
<script type="text/javascript" src="js/plugins/ui/jquery.bootbox.min.js"></script>
<script type="text/javascript" src="js/plugins/ui/jquery.jgrowl.js"></script>
<script type="text/javascript" src="js/plugins/ui/jquery.fancybox.js"></script>
<script type="text/javascript" src="js/plugins/ui/jquery.elfinder.js"></script>


<script type="text/javascript" src="js/plugins/forms/jquery.uniform.min.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.autosize.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.inputlimiter.min.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.tagsinput.min.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.inputmask.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.select2.min.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.listbox.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.validation.js"></script>
<script type="text/javascript" src="js/plugins/forms/jquery.validationEngine-en.js"></script>


<script type="text/javascript" src="js/plugins/tables/jquery.dataTables.min.js"></script>

<script type="text/javascript" src="js/files/bootstrap.min.js"></script>

<script type="text/javascript" src="js/files/functions.js"></script>


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

<body id="table-body">
<div id="wrap">
<form>
<div id="idletimeout" style="top: 150px; margin-left: 215px; margin-right: 200px; ">
        Logging off in <span><!-- countdown place holder --></span>&nbsp;seconds due to inactivity.
        <a id="idletimeout-resume" href="#">Click here to continue</a>.
    </div>  
	

	

	<!-- Fixed top -->
	<div id="top">
		<div class="fixed">
			<a href="#" title="" class="logo"></a>
			<img class="logo-menu" src="images/image001.jpg"></img>
		</div>
	</div>
	<!-- /fixed top -->


	<!-- Content container -->
	<div id="container">

		<!-- Content -->
		<div id="content">

		    <!-- Content wrapper -->
		    <div class="wrapper">
		    	<br/>
		    	<br/>

			    <h5 class="widget-name"><i class="icon-columns"></i>Server List</h5>
			    <p>If you are choosing one asset to be monitored, Copy asset id and paste to the form manually. If you want to calculate benefits choose multiple assets.</p>
	                	
                <!-- Table with checkboxes -->
                <div class="widget">
                	
                    <div class="table-overflow">
                        <table id="tblComputerList" class="table table-bordered table-checks" >
                          <thead>
                              <tr>
                                  <th style="font-size:80%;">Apply</th>
                                  <th style="font-size:80%;">Asset ID</th>
                                  <th style="font-size:80%;">Usage Dtls</th>
                                  <th style="font-size:80%;">IP Address</th>
                                  <th style="font-size:80%;">Cost</th>
                                  <th style="font-size:80%;">Description</th>
                                  
                              </tr>
                          </thead>
                           
<% 	

 		int i = 0;
 		int j = 0;
 		int cost=0;
		String ctlgDesc = "";
		String assetUsg="";
		String enterpriseId = request.getParameter("entpId") ;
		// Asset list
		List<Asset> assetListDB = AssetHelper.getAssetList(enterpriseId);
		
		DevCtlg d =new DevCtlg();
 		List<DevCtlg> dcListDB = AssetHelper.getDevCtlgList(enterpriseId);
 					
%>

		

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
		<tbody>
		<tr>
			<td><input type="checkbox" id = "chkApply" class="styled"  ></td>
			<td style="font-size:80%;"><%=assetListDB.get(i).getAssetId()%></td>
			<%
				if(assetListDB.get(i).getAssetUsg()==null) {
					assetUsg="";
				}
				else {
					assetUsg=assetListDB.get(i).getAssetUsg();
				}
			%>
			<td style="font-size:80%;"><%=assetUsg%></td>
			<td style="font-size:80%;"><%=assetListDB.get(i).getIpAddr()%></td>
			<td style="font-size:80%;"><%= cost %></td>
			<td style="font-size:80%;"><%= ctlgDesc %> </td>

 <% 
	}		
%>
</tr>


		</tbody>	
		
		</table>
                    </div>
                </div>
                <!-- /table with checkboxes -->

                <div class="span6 well body " style="height:70px;">
            
		            <button class="btn btn-success" type="button" style="position:relative; top:-10px; left:0px;" onclick="handleClick()">Submit</button>
		            <label id="consolidateResult" class="control-label" style="position:relative; top:-45px; left:80px; width:1000px; line-height:20px;"> </label>
		    </div>
		    <!-- /content wrapper -->

		</div>
		<!-- content -->

	</div>
	<!-- /content container -->

</form>
</div>
</body>
</html>


		
	
		
		
	
<!-- Mask to cover the whole screen -->
<div id="mask"></div>

<script src="js/jquery.idletimer.js" type="text/javascript"></script>
<script src="js/jquery.idletimeout.js" type="text/javascript"></script>

<!--jQuery plugin to set session timeout -->
<script type="text/javascript">
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
