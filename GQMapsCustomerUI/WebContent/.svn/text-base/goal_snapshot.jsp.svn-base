<%@page import = "com.gq.meter.xchange.object.*"%>
<%@page import = "com.gq.cust.GoalHelper"%>
<%@page import = "java.util.ArrayList"%>
<%@page import = "java.util.List"%>
<%@page import = "java.util.Date"%>

<html>
	<head>
		
		<title> GQMaps Goal Snapshot History </title>
		
		<!--  Custom Styles -->
		<link href = "css/dashboard.css" rel = "stylesheet" type = "text/css" />
		<link href = "css/timeout.css" rel = "stylesheet" type = "text/css" />
		<link href = "css/gqmaps.css" rel = "stylesheet" type = "text/css" />
		
		<!-- Predefined Styles -->
		<link href = 'http://fonts.googleapis.com/css?family=Open+Sans:400,600,700' rel = 'stylesheet' type = 'text/css' />
		
		<!-- Predefined Script -->
		<script type = "text/javascript" src = "http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"> </script>
		<script type = "text/javascript" src = "js/plugins/tables/jquery.dataTables.min.js"> </script>
       	
		<!--/custom scripts -->
		<script src = "js/jstorage.js"> </script>   
		<script src = "js/dashboard_full.js"> </script>  
		<script src = "js/rest_service.js"> </script> 
		
	</head>
	
	<body id = "table-body">
	
		<!-- Timeout Div -->
		<div id = "idletimeout">
			Logging off in <span> countdown place holder </span>&nbsp;seconds due to inactivity.
			<a id = "idletimeout-resume" href = "#"> Click here to continue </a>.
		</div> 
		
		<div id = "wrap">
			
			<%
				String snapShotId = request.getParameter("snapShotId");
		
				GoalHelper gh = new GoalHelper( snapShotId );
		
				Goal g = gh.getGoal();
				List<GoalSnpsht> goalsnapshotList = gh.getGoalSnapshot();
				List<TemplateTaskDetails> goalTaskTmpltChkList = gh.getTemplateTaskDetails();
				Date startDate = null;
				Date endDate = null;
				Date applyDate = null;
				int year,month,date;
			%>
			
			<!-- Fixed top -->
			<div id = "top">
				<div class = "fixed">
					<a href = "index.html" title = " " class = "logo"></a>
					<img class = "logo-menu" src = "images/image001.jpg"></img>
				</div>
			</div>
			<!-- /Fixed top -->
			
			<!-- Content container -->
			<div id = "container" style = "margin-left:auto; margin-right:auto; max-width:70%;">
			
				<!-- Content wrapper -->
				<div class = "wrapper">
				
					<br/><br/>
					
					<h5 class = "widget-name"> <i class = "icon-columns"></i> <%=g.getDescr()%> </h5>
					
					<!-- form to display goal snapshot history-->
					<%
						if(g.getTimeBound().equals("y")){ 
					%>
					
					<br/><br/>
					
					<form id = 'gsform' method = "post" >
					
					<div class = "widget">
					
						<div class = "table-overflow">
							<!--  table -->
							<table align = "center" style = "width:600px;" id = "tblGoalsnapsht" class = "table table-bordered table-checks" >
                        	
                          		<thead>
                          		
                              		<tr style = "font-size:80%;">
                                 		<th> Snapshot Id </th>
	                                  	<th> Start Date </th>
	                                  	<th> Notes </th>
	                                  	<th> End Date </th>
	                                  	<th> Realized Benefit </th>
                                  	</tr>
                                  	
                          		</thead>
                          		
                          		<tbody>
                          		<%
                          			for (int snapShotCount = 0; snapShotCount < goalsnapshotList.size(); snapShotCount++) {
                          		%>
                          		<tr style = "font-size:80%;">
                          		
                          			<td style = "width:20px;"> <%=goalsnapshotList.get(snapShotCount).getSnpshtId()%> </td>
                          		
                          			<%
                          				startDate = new Date(goalsnapshotList.get(snapShotCount).getStartDate().getTime());
                          				year = startDate.getYear() + 1900;
                          				month = startDate.getMonth() + 1;
                          				date = startDate.getDate();
                          			%>
                          			
                          			<td style = "width:50px;">
                          				<%=year%>-<% if( month<10 ) out.println("0"+month); else out.println(month);%>-
                          				<% if( date<10 ) out.println("0"+date); else out.println(date);%>
                          			</td>
                          			
                          			<td style = "width:50px;">
                          				<%=goalsnapshotList.get(snapShotCount).getNotes()%>
                          			</td>
                          			
                          			<%
                          				endDate = new Date(goalsnapshotList.get(snapShotCount).getEndDate().getTime());
                          				year = endDate.getYear() + 1900;
                          				month = endDate.getMonth() + 1;
                          				date = endDate.getDate();
                          			%>
                          			
                          			<td style = "width:80px;">
                          				<%=year%>-<% if( month<10 ) out.println("0"+month); else out.println(month);%>-
                          				<% if( date<10 ) out.println("0"+date); else out.println(date);%>
                          			</td>
                          			
                          			<td style = "width:50px;">
                          				<%=goalsnapshotList.get(snapShotCount).getCostBenefit()%>
                          			</td>
                          			
                          		</tr>
                          		<%
                          			}
                          		%>
                          		</tbody>
                          		
                          	</table>
                          	<!-- /table -->
                          	
                        </div>
                        <!-- table div ends -->
                    </div>
                </form>
				<%
					}
				%>
				
				<!-- form to display task template -->
				<%
					if(g.getTimeBound().equals("y")){ 
				%>
					
				<br/><br/>
					
				<form name = "ttform">
					
					<div class = "widget">
							
						<div class = "table-overflow">
							
							<table align = "center" style = "width:800px;" id = "tblTasktmplt" class = "table table-bordered table-checks" >
								
								<thead>
									
									<tr style = "font-size:80%;">
											
										<th> Task </th>
										<th> UserNotes </th>
										<th> Benefit </th>
										<th> SystemNotes </th>
										<th> Apply </th>
											
									</tr>
										
								</thead>
								
								<tbody>
									
									<%
										int taskCount = 0;
										for (taskCount = 0; taskCount < goalTaskTmpltChkList.size(); taskCount++) {
									%>
									
										<tr style = "font-size:80%;">
										
											<td style = "width:350px;">
												<%=goalTaskTmpltChkList.get(taskCount).getDescr()%>
												<!--  we will just store the task id in a hidden field as well -->
												<input type = "hidden" name = "taskid" value = <%=goalTaskTmpltChkList.get(taskCount).getTask_id() %>>
											</td>
											
											<td> <%=goalTaskTmpltChkList.get(taskCount).getUsr_notes()%> </td>
											
											<td> <%=goalTaskTmpltChkList.get(taskCount).getCost_benefit()%> </td>
											
											<td> <%=goalTaskTmpltChkList.get(taskCount).getSys_notes()%> </td>
											
											<!-- Checking applied date is null -->
											<%
												if (goalTaskTmpltChkList.get(taskCount).getApply_date() == null ) {
											%>
											
											<td> </td>
											
											<%
												} 
												else {
													endDate = new Date(goalTaskTmpltChkList.get(taskCount).getApply_date().getTime());
													year = endDate.getYear() + 1900;
													month = endDate.getMonth() + 1;
													date = endDate.getDate();
											%>
											
											<td>
												<%=year%>-<% if( month<10 ) out.println("0"+month); else out.println(month);%>-
												<% if( date<10 ) out.println("0"+date);else out.println(date);%> 
											</td>
											
											<%
												}
											%>
											
										</tr>
										<% 
											}
										%>
										
									</tbody>
								
								</table>
								<!-- /task template table-->
							</div>
							<!-- table div ends -->
						</div>
						<!-- widget div ends -->
						
						<br/>
						
						<table>
						
							<tr>
								<td>
									<button class = "btn btn-warning" type = "button" style = "position:relative; top:-20px; left:720px;" 
									onclick = "gotoDashboard()"> Back to Dashboard </button>
								</td>
							</tr>
						</table>
					
					</form>
					<%
						}
					%>
				
				</div>
				<!-- Content wrapper ends -->
			
			</div>
			<!-- Content container ends -->
		
		</div>
		<!-- div wrap ends -->
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
