<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="com.gq.meter.xchange.object.*"%>
<%@page import="com.gq.cust.GoalHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>

<html>
	<head>
		<script type="text/javascript" src = "http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.js" charset="utf-8"></script>
		
		<script type="text/javascript" src="http://www.google.com/jsapi"  charset="utf-8"></script>
		<script type="text/javascript">
		            google.load("visualization", "1", {packages:["piechart", "corechart", "annotatedtimeline", "geomap"]});
		</script>
		
		<link type="text/css" href="jquery-ui-1.10.2.custom/css/blitzer/jquery-ui-1.10.2.custom.css" rel="stylesheet" />
		<link type="text/css" href="css/gqmaps.css" rel="stylesheet" />
		<link href="css/dashboard.css" rel="stylesheet" type="text/css" />
		<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,600,700' rel='stylesheet' type='text/css'>
		<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
		
		<script type="text/javascript" src="js/plugins/tables/jquery.dataTables.min.js"></script>
		
		<!--/custom scripts -->
		<script src="js/goal.jsp"> </script>
		<script src = "js/jstorage.js"> </script>   
		<script src = "js/dashboard_full.js"> </script>  
		<script src="js/goal.js"></script>
		<script src = "js/rest_service.js"> </script> 
		
		
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
			<img class="mergedimg" src="images/grey.png"> 
			
  			<div id="idletimeout" style="top: 150px; margin-left: 215px; margin-right: 200px; ">
        		Logging off in <span><!-- countdown place holder --></span>&nbsp;seconds due to inactivity.
        		<a id="idletimeout-resume" href="#">Click here to continue</a>.
    		</div>  
			
			<%
				String snapShotId = request.getParameter("snapShotId");
		
				GoalHelper gh = new GoalHelper( snapShotId );
		
				Goal g = gh.getGoal();
				List<GoalSnpsht> goalsnapshotList = gh.getGoalSnapshot();
				List<TemplateTaskDetails> goalTaskTmpltChkList = gh.getTemplateTaskDetails();
				Date startDate=null;
				Date endDate=null;
				Date applyDate=null;
				int year,month,date;
			%>
			
			<!-- Fixed top -->
			<div id="top">
				<div class="fixed">
					<a href="#" title="" class="logo"></a>
					<img class="logo-menu" src="images/image001.jpg"></img>
				</div>
			</div>
			<!-- /fixed top -->
			
			<!-- Content container -->
			<div id="container" style="margin-left:auto; margin-right:auto; max-width:70%;">
		
				<!-- Content wrapper -->
		    	<div class="wrapper">
					<br/><br/>
       				<h5 class="widget-name"><i class="icon-columns"></i><%=g.getDescr()%></h5>
       				
       				 <!-- form to display goal snapshot history-->
					 <%
						if(g.getTimeBound().equals("y")){ %>
						<br><br>
						<form id='gsform' method="post" >
							
							<div class="widget">
                				
                				<div class="table-overflow">
                        			
                        			<table align="center" style="width:600px;" id="tblGoalsnapsht" class="table table-bordered table-checks" >
                          			<thead>
                              			<tr style="font-size:80%;">
                                 			<th>Snapshot Id</th>
	                                  		<th>Start Date</th>
											<th>Notes</th>
											<th>End Date</th>
											<th>Realized Benefit</th>
                                  		</tr>
                          			</thead>
									<tbody>
										<%
											for (int i = 0; i < goalsnapshotList.size(); i++) {		
										%>
										
										<tr style="font-size:80%;">
										
											<td style="width:20px;"><%=goalsnapshotList.get(i).getSnpshtId()%></td>
											
											<% startDate=new Date(goalsnapshotList.get(i).getStartDate().getTime());
											year=startDate.getYear()+1900;
											month=startDate.getMonth()+1;
											date=startDate.getDate();
											%>
											
											<td style="width:50px;"><%=year%>-<%if(month<10)out.println("0"+month);else out.println(month);%>-<%if(date<10)out.println("0"+date);else out.println(date);%></td>
											<td style="width:50px;"><%=goalsnapshotList.get(i).getNotes()%></td>
											
											<% endDate=new Date(goalsnapshotList.get(i).getEndDate().getTime());
											year=endDate.getYear()+1900;
											month=endDate.getMonth()+1;
											date=endDate.getDate();
											%>
											
											<td style="width:80px;"><%=year%>-<%if(month<10)out.println("0"+month);else out.println(month);%>-<%if(date<10)out.println("0"+date);else out.println(date);%></td>
											<td style="width:50px;"><%=goalsnapshotList.get(i).getCostBenefit()%></td>
										
										</tr>
										<%
											}
										%>
									</tbody>			
									</table>
								</div>
								<!-- table div ends -->
								
							</div>
							<!-- widget div ends -->
							
						</form>
					<%}%>
					
						<!-- form to display task template -->
						<%
						if(g.getTimeBound().equals("y")){ %>
						<br><br>
						<form name="ttform">
						
							<div class="widget">
                				
                				<div class="table-overflow">
                        			
                        			<table align="center" style="width:800px;" id="tblTasktmplt" class="table table-bordered table-checks" >
                          			<thead>
									<tr style="font-size:80%;">
                                 		<th>Task</th>
										<th>UserNotes</th>
										<th>Benefit</th>
										<th>SystemNotes</th>
										<th>Apply</th>
                                  	</tr>
                          			</thead>
									<tbody>
										<%
										int i = 0;
										for (i = 0; i < goalTaskTmpltChkList.size(); i++) {
										%>
										
										<tr style="font-size:80%;">
										
											<td style="width:350px;"><%=goalTaskTmpltChkList.get(i).getDescr()%>
											<!--  we will just store the task id in a hidden field as well -->
											<input type="hidden" name="taskid" 
							 					value=<%=goalTaskTmpltChkList.get(i).getTask_id() %>>
											</td>
											
											<td><%=goalTaskTmpltChkList.get(i).getUsr_notes()%></td>
											<td><%=goalTaskTmpltChkList.get(i).getCost_benefit()%></td>
											<td><%=goalTaskTmpltChkList.get(i).getSys_notes()%></td>
											
											<!-- Checking applied date is null -->		
											<%
					
												if (goalTaskTmpltChkList.get(i).getApply_date() == null ) {
											%>
												<td></td>
											<%
												} 
												else {
											%>
											<td>
											<% 
												endDate=new Date(goalTaskTmpltChkList.get(i).getApply_date().getTime());
												year=endDate.getYear()+1900;
												month=endDate.getMonth()+1;
												date=endDate.getDate();
											%> 
											<%=year%>-<%if(month<10)out.println("0"+month);else out.println(month);%>-<%if(date<10)out.println("0"+date);else out.println(date);%> 
											</td>
										</tr>
										<%		}
										}
										%>
									</tbody>			
									</table>
								</div>
								<!-- table div ends -->
								
							</div>
							<!-- widget div ends -->
							<br/>
							<table>
							<tr><td><button class="btn btn-warning" type="button" style="position:relative; top:-20px; left:720px;" onclick="gotoDashboard()"> Back to Dashboard </button>
							</table>
							
						</form>
					<%}%>
						
					 
       			</div>
       			<!-- Content wrapper ends -->
       			
			</div>
			<!-- Content container ends -->
			
		</div>
		<!-- div wrap ends -->
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

