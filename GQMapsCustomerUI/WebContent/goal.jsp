<%@page import = "com.gq.meter.xchange.object.*"%>
<%@page import = "com.gq.cust.GoalHelper"%>
<%@page import = "java.util.ArrayList"%>
<%@page import = "java.util.List"%>
<%@page import = "java.util.Date"%>

<html>

	<head>
	
		<title>Goal screens for GQMaps</title>
		
		<!--  Custom Styles -->
		<link href = "css/dashboard.css" rel = "stylesheet" type = "text/css" />
		<link href = "css/timeout.css" rel = "stylesheet" type = "text/css" />
		<link href = "css/gqmaps.css" rel = "stylesheet" type = "text/css" />
		
		<!-- Predefined Styles -->
		<link href = 'http://fonts.googleapis.com/css?family=Open+Sans:400,600,700' rel = 'stylesheet' type = 'text/css' />
		
		<!-- Predefined Script -->
		<script type = "text/javascript" src = "http://www.google.com/jsapi"  charset = "utf-8"> </script>
		<script type = "text/javascript" src = "http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"> </script>
		<script type = "text/javascript" src = "http://maps.googleapis.com/maps/api/js?key=AIzaSyBGIi2rSK9Qp2dY1EewX-JUsK0sjQsxAak&sensor=false"> 
		</script>
		
		<script type = "text/javascript">
			google.load("visualization", "1", {packages:["piechart", "corechart", "annotatedtimeline", "geomap"]});
		</script>
		
		<script type = "text/javascript" src = "js/charts/graph.js"> </script>
		<script type = "text/javascript" src = "js/charts/chart1.js"> </script>
		<script type = "text/javascript" src = "js/charts/chart2.js"> </script>
		<script type = "text/javascript" src = "js/charts/chart3.js"> </script>
		<script type = "text/javascript" src = "js/plotgraph.js"> </script>
		
		<script type = "text/javascript" src = "js/plugins/charts/excanvas.min.js"> </script>
		<script type = "text/javascript" src = "js/plugins/charts/jquery.flot.js"> </script>
		<script type = "text/javascript" src = "js/plugins/charts/jquery.flot.resize.js"> </script>
		<script type = "text/javascript" src = "js/plugins/charts/jquery.sparkline.min.js"> </script> 
		
		<!-- Custom Scripts -->
		<script src = "js/goal.js"> </script>
		<script src = "js/jstorage.js"> </script>
		<script src = "js/rest_service.js"> </script>
		<script src = "js/general.js"> </script>
		<script src = "js/dashboard_full.js"> </script>

		<script type="text/javascript">

		$.fn.serializeObject = function()
		{
			var o = {};
			var a = this.serializeArray();
			
			$.each(a, function() {
				if (o[this.name] !== undefined) {
					if (!o[this.name].push) {
						o[this.name] = [o[this.name]];
					}
					o[this.name].push(this.value || '');
				} 
				else {
					o[this.name] = this.value || '';
				}
			});
		return o;
		};

		//function to accept numbers only.
		function isNumberKey(evt)
		{
			var charCode = (evt.which) ? evt.which : event.keyCode;
			var cost_benefit=document.getElementById('cost_benefit');

			if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
				return false;
			}
		return true;
		}

		//function to length validation for cost benefit
		function isLengthCheck() 
		{
			var cost_benefit=document.getElementById('cost_benefit').value;

			if(cost_benefit.length >9) {
				return false;
			}
		}

		//funtion to validate the System notes,User Notes and 
		//Snapshot notes dont accept special characters except .,- and space.
		function validateSplChars(value) 
		{
			if(value === "" || value === null) {
				return true;
			}
			
			var splRegExp = /^[A-Za-z\/\s\.-]+$/;

			if(splRegExp.test(value)) {
				return true;
			}
			else {
				alert('Please enter asset usage details without special characters except .,- and space');
				return false;
			}
		}

		function goalSubmit(actionName) {

			// if a check box is checked , set the corresponding hidden box to 1
			document.getElementById('actionName').value = actionName;

			//this is for costbenefit textbox
			var cost_benefit_value;
			var fields1 = document.ttform.elements["cost_benefit"];

			$('.input-row').each(function(index, row) {

				var innerObject = {};
				$(':text', row).each(function(index, text) {
					if(text.value.length == 0) {
						cost_benefit_value = 0;
					}
					else {
						cost_benefit_value=text.value;
					}
				});
			fields1[index].value = cost_benefit_value;
			});

			// this is for the checkbox
			var onOff;
			var fields = document.ttform.elements["hd_chkApply"];

			$('.input-row').each(function(index, row) {

				var innerObject = {};
				$(':checkbox', row).each(function(index, checkbox) {
					onOff = checkbox.checked ? 'on' : 'off';
				});
			fields[index].value = onOff;
			});

			if ( actionName == 'finalize') { //to check if all the tasks are applied

				if( ! processForm() ) {
					alert(" Please apply all the tasks ");
					return false;
				}

				// calculate cost benefit by adding all the entered cost benefits
				var benefitsList = document.getElementsByName('cost_benefit');
				var totalBenefit = 0;

				for (i = 0; i < benefitsList.length; i++){
					totalBenefit = totalBenefit + parseFloat(benefitsList[i].value);
				}
				document.getElementById('gs_cost_benefit').value = totalBenefit;
			}
			else {}

			var formJson = JSON.stringify($('form').serializeObject());

			//we are here to construct a ajax call for invoking save and finalize operation. 
			var vType = "POST";
			var SUrl = $.jStorage.get("jsDBUrl")+"saveAndFinalize/submit";
			$.ajax
			({
				type:vType,
				contentType: "application/json",
				url:SUrl,
				data: JSON.stringify($('form').serializeObject()),
				async:false,
				dataType: "json",

				success:function(response) {

					window.location.href="dashboard_full.html";
				},

				error:function(json) {
					window.location.href="Error.jsp";
				}
			});
		}

		//function to find number of check boxes selected
		function processForm(obj) {

			var cb = document.getElementsByTagName('input');
			var retStr = "";
			var cbCount = 0;
			var cbCheckedCount = 0;

			for ( var i = 0; i < cb.length; i++) {
				if (cb[i].getAttribute('type') == "checkbox") {

					cbCount++;
					if (cb[i].checked == true) {
						cbCheckedCount++;
					}
				}
			}

			if(cbCount == cbCheckedCount){
				return true;
			}
			else {
				return false;
			}
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
			<%
				String enterpriseId = request.getParameter("entpId");
				String goalId = request.getParameter("goalId");
				String goalInputs = request.getParameter("goalInputs");
				
				GoalHelper gh = new GoalHelper(enterpriseId, goalId , goalInputs );
				Goal g = gh.getGoal();
				
				List<GoalSnpsht> goalsnapshotList = gh.getGoalSnapshot();
				List<TemplateTaskDetails> goalTaskTmpltChkList = gh.getTemplateTaskDetails();
				
				Date startDate = null;
				Date endDate = null;
				int year;
				int month;
				int date;
			%>
			
			<!-- Fixed top -->
			<div id = "top">
				<div class = "fixed">
					<a href = "#" title = "" class = "logo"></a>
					<img class = "logo-menu" src = "images/image001.jpg"></img>
				</div>
			</div>
			<!-- /fixed top -->
			
			<!-- Content container -->
			<div id = "container" style = "margin-left:auto; margin-right:auto; max-width:70%;">
			
				<!-- Content wrapper -->
				<div class = "wrapper">
				
					<br/><br/>
					
					<h5 class = "widget-name"><i class = "icon-columns"></i><%=g.getDescr()%></h5>
					
					<% if(g.getTimeBound().equals("y")){ %>
					
						<a id = "serverAsset" href = "#" onclick = "serverList();">
							<h6 align = "right"><u>Asset List</u></h6>
						</a>
						
						<br/><br/>
						
						<!-- form to display task template -->
						<form name = "ttform" action = "/GQMapsCustomerServices/saveAndFinalize/submit" method = "post">
						
							<!-- Table with checkboxes -->
							<!--use tr class= success, warning, error and info for colors inside the placeholder table-->
							<div class = "widget">
							
								<div class = "table-overflow">
								
									<table class = "table table-bordered table-checks" class = "table" id = "select-all">
									
										<thead>
										
											<tr style = "font-size:80%;">
												<!--title of the table-->
												<th> Tasks </th>
												<th> Graph </th>
											</tr>
											
										</thead>
										
										<tbody>
										
											<%
											int taskChkListCount = 0;
											for (taskChkListCount = 0; taskChkListCount < goalTaskTmpltChkList.size(); taskChkListCount++) {
											%>
										
											<input type = "hidden" name = "taskid" value = '<%=goalTaskTmpltChkList.get(taskChkListCount).getTask_id()%>'>
										
											<% if( (taskChkListCount % 2) == 0){ %>
											<tr class = "success input-row">
											<% } else { %>
											<tr class = "info input-row">
											<% } %>
											
											<%
											
											String usrNotes = goalTaskTmpltChkList.get(taskChkListCount).getUsr_notes();
											String sysNotes = goalTaskTmpltChkList.get(taskChkListCount).getSys_notes();
											
											if ( usrNotes == null ) {
												usrNotes = "";
											}
											if ( sysNotes == null ) {
												sysNotes = "";
											}
											
											%>
											
											<td style = "width:42%;"> 
												
												<span class = "label label-inverse" style = "line-height:20px;">
													<div style = "white-space:normal;">
														<%=goalTaskTmpltChkList.get(taskChkListCount).getDescr()%>
													</div> 
												</span> 
												
												<br/><br/><br/>
												
												<div style = "width:190px; position:relative;">
													
													<label class = "control-label" style = "font-size:80%;">
														<i class = "icon-list-alt"> </i> System Notes 
													</label>
													
													<div class = "controls">
														<textarea style = "resize:none;" rows = "1" cols = "19" maxlength = "198" name = "systemnotes" id = "sysnotes" onblur = "validateSplChars(this.value)"><%=sysNotes%></textarea>
													</div>
													
												</div>
												
												<div class = "controls" style = "position:relative; width:150px; 
												bottom:55px; left:200px; height:30px;">
												
													<label class = "control-label" 
													style = "font-size:80%; width:150px;">
														<i class = "icon-pencil"></i> Benefit 
													</label>
													
													<input class = "input-small" type = "text" 
													style = "height:25px;" placeholder = "Numbers only" 
													name = "cost_benefit" id = "cost_benefit" 
													onkeypress = "return isNumberKey(event)" maxlength = "11" 
													onkeyup = "return isLengthCheck()" 
													value ="<%=goalTaskTmpltChkList.get(taskChkListCount).getCost_benefit()%>" />
													
													<input type = "hidden" name = "hdcost_benefit"> 
												
												</div>
												
												<div style = "position:relative; top:-25px;">
												
													<label class = "control-label" style = "font-size:80%;">
														<i class = "icon-user"></i> User Notes 
													</label>
													
													<div class = "controls">
														<textarea rows = "1" cols = "19" maxlength = "198" name = "usernotes" id = "usrnts" onblur = "validateSplChars(this.value)"><%=usrNotes%></textarea>
													</div>
													
												</div>
												
												<!-- Checking applied date is null -->
												<%
													if (goalTaskTmpltChkList.get(taskChkListCount).getApply_date() == null ) {
												%>
												
												<div class = "controls" style = "position:relative; width:150px; 
												bottom:60px; left:200px;" >
													
													<label class = "control-label checkbox " 
													style = "font-size:80%;">
														<input type = "checkbox" id = "inlineCheckbox1" 
														class = "styled" name = "chkApply">
														<i class = "icon-ok-sign"></i> Apply 
													</label>
													
													<input type = "hidden" name = "hd_chkApply" >
													
												</div>
												
												<%
												} else {
												%>
												
												<div class = "controls" style = "position:relative; width:150px; 
												bottom:60px; left:200px;" >
													
													<label class = "control-label checkbox " 
													style = "font-size:80%;">
														<input type = "checkbox" id = "inlineCheckbox1" 
														class = "styled" name = "chkApply"  checked = "checked">
														<i class = "icon-ok-sign"></i> Apply
													</label>
													
													<input type = "hidden" name = "hd_chkApply">
												
												</div>
												<% } %>
											
											</td>
											
											<td>
											
												<div class = "well body">
												
												<!-- Paste Chart div here-->
												<%
													if ( goalTaskTmpltChkList.get(taskChkListCount).getChartType().equals("line")) {
												%>
														<input type = "hidden" id = "chartData_<%=taskChkListCount%>" 
														value = '<%=goalTaskTmpltChkList.get(taskChkListCount).getChartData().replaceFirst("string", "datetime")%>'>
														<div id = 'chartDiv_<%=taskChkListCount%>' 
														style = "width :500px; height:200px; color: #00008a">
														</div>
												<%
												} else {
												%>
														<input type = "hidden" id = "chartData_<%=taskChkListCount%>" 
														value = '<%=goalTaskTmpltChkList.get(taskChkListCount).getChartData()%>'>	
														<div id = 'chartDiv_<%=taskChkListCount%>' style = "color: #00008h" >
														</div>
												<%
												}
												%>
													<input type = "hidden" id = "chartType_<%=taskChkListCount%>"
													value = '<%=goalTaskTmpltChkList.get(taskChkListCount).getChartType()%>'>
													
												</div>
												<!-- /Chart Div -->
										
												</td>
											</tr>
										
											<%
											} // end for to display tasks
											%>
										
											<input type = "hidden" id = "taskCount" value = <%=taskChkListCount%>>
										</tbody>
									</table>
								</div>
								<!-- /table overflow -->
							</div>
							<!-- /widget -->
							
							<div class = "span6">
							
								<button id = "save" class = "btn btn-info" type = "button" style = "position:relative; top:10px; 
								left:10px;" onclick = "goalSubmit('save')"> Save and Exit </button>
								
								<input class = "input-large" id = "gs_notes" name = "gs_notes" maxlength = "50" type = "text" 
								placeholder = "Enter a Snapshot Note" style = "display: block; position:relative;  
								top:-22px; left:150px;" onblur = "validateSplChars(this.value)"/>
								
								<button id = "finalize" class = "btn btn-success" type = "button" style = "position:relative; 
								top:-54px; left:363px;" onclick = "goalSubmit('finalize')"> Finalize </button>
								
								<button class = "btn btn-warning" type = "button" style = "position:relative; top:-56px; 
								left:680px;" onclick = "gotoDashboard()"> Back to Dashboard </button>
								
								<!--  the following entries are for providing the goal snapshot entry -->

								<p class = "widget-name"></p>
								<input type = "hidden" name = "gs_cost_benefit" id = "gs_cost_benefit" >
								<input type = "hidden" name = "gs_id" value = "<%=goalTaskTmpltChkList.get(0).getSnpsht_id()%>">
								<input type = "hidden" name = "gs_entpid" value = "<%=enterpriseId %>">
								<input type = "hidden" name = "gs_goalid" value = "<%=goalId %>">
								
								<!-- mention what action is this either save or finalize-->
								<input type = "hidden" name = "actionName" id = "actionName" >
								
								<!-- a dummy field to hold the json string-->
								<input type = "hidden" name = "jsonFormData" id = "jsonFormData" >
							
							</div>
							
							<br/><br/>
						
						</form>
						<!--  form ends for timebound yes -->
					<% } %>
					<%
						if(g.getTimeBound().equals("n")){ 
					%>
							<div class = "widget">
							
								<div class = "table-overflow">
								
									<table class = "table table-bordered table-checks" class = "table" id = "select-all">
									
										<tbody>
										
										<%
										int taskChkListCount = 0;
										for (taskChkListCount = 0; taskChkListCount < goalTaskTmpltChkList.size(); taskChkListCount++) {
										%>
										
										<input type = "hidden" name = "taskid" value = '<%=goalTaskTmpltChkList.get(taskChkListCount).getTask_id()%>'>
										
										<% if ( (taskChkListCount % 2) == 0){ %>
											<tr class = "success input-row">
										<% } else { %>
											<tr class = "info input-row">
										<% } %>
											
											<td>
												
												<!-- Paste Chart div here-->
												<div class = "well body">
													
													<% 
														if ( goalTaskTmpltChkList.get(taskChkListCount).getChartType().equals("line")) {
													%>
												
													<input type = "hidden" id = "chartData_<%=taskChkListCount%>" 
													value = '<%=goalTaskTmpltChkList.get(taskChkListCount).getChartData().replaceFirst("string", "datetime")%>'>
												
													<span class = "label label-inverse" style = "line-height:20px;">
														<div style = "white-space:normal;">
															<%=goalTaskTmpltChkList.get(taskChkListCount).getDescr()%>
														</div>
													</span>
												
													<br/><br/>
												
													<div id = 'chartDiv_<%=taskChkListCount%>' style = "width :800px; 
													height:250px ;color: #00008a"></div>
												
													<% } else { %>
												
													<span class = "label label-inverse" style = "line-height:20px;">
														<div style = "white-space:normal;">
															<%=goalTaskTmpltChkList.get(taskChkListCount).getDescr()%>
														</div>
													</span>
												
													<br/><br/>
												
													<input type = "hidden" id = "chartData_<%=taskChkListCount%>" 
													value = '<%=goalTaskTmpltChkList.get(taskChkListCount).getChartData()%>'>
												
													<div id = 'chartDiv_<%=taskChkListCount%>' style = "color: #00008h" ></div>		
												
													<% } %>
												
													<input type = "hidden" id = "chartType_<%=taskChkListCount%>"
													value = '<%=goalTaskTmpltChkList.get(taskChkListCount).getChartType()%>'>
											
												</div>
											</td>
											</tr>
											
										<% } // end for to display tasks %>
										
										<input type = "hidden" id = "taskCount" value = <%=taskChkListCount%>>
										
										</tbody>
									</table>
								</div>
								<!-- /table-overflow -->
							</div>
							<!-- /widget -->
							
							<br/><br/>
							
							<table>
								<tr>
									<td>
										<button class = "btn btn-warning" type = "button" style = "position:relative; 
										top:-30px; left:760px;" onclick = "gotoDashboard()"> Back to Dashboard </button>
									</td>
								</tr>
							</table>
						<% } %>
						
						<!-- form to display goal snapshot history-->
						
						<%
						if( g.getTimeBound().equals("y") ){ %>
						
						<h4 style = "font-family: 'Open Sans', sans-serif; font-weight: 400; text-shadow: 0 1px 1px #fff; 
						margin: 0; color: #303030;" align = "center"> Goal History </h4>
						
						<br/><br/>
						
						<form id = 'gsform' method = "post" >
								
							<div class = "widget">
							
								<div class = "table-overflow">
								
									<table align = "center" style = "width:600px;" id = "tblGoalsnapsht" 
									class = "table table-bordered table-checks" >
										
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
										
										<% for (int goalSnapShotCount = 0; goalSnapShotCount < goalsnapshotList.size(); goalSnapShotCount++) {
											String goalSPLink = "goal_snapshot.jsp?snapShotId=" + goalsnapshotList.get(goalSnapShotCount).getSnpshtId();
										%>
											<tr style = "font-size:80%;">
												
												<td style = "width:20px;"> <a href='<%=goalSPLink%>'>
													<%=goalsnapshotList.get(goalSnapShotCount).getSnpshtId()%></a>
												</td>
												
												<% 
												startDate = new Date(goalsnapshotList.get(goalSnapShotCount).getStartDate().getTime());
												year = startDate.getYear() + 1900;
												month = startDate.getMonth() + 1;
												date = startDate.getDate();
												%>
												
												<td style = "width:50px;">
													<%=year%>-<%if( month<10 ) out.println("0"+month); else out.println(month);%>-
													<% if(date<10) out.println("0"+date); else out.println(date);%>
												</td>
												
												<td style = "width:50px;">
													<%=goalsnapshotList.get(goalSnapShotCount).getNotes()%>
												</td>
												
												<% 
												endDate = new Date(goalsnapshotList.get(goalSnapShotCount).getEndDate().getTime());
												year = endDate.getYear() + 1900;
												month = endDate.getMonth() + 1;
												date = endDate.getDate();
												%>
												
												<td style = "width:50px;">
													<%=year%>-<%if( month<10 )out.println("0"+month); else out.println(month);%>-
													<% if(date<10) out.println("0"+date); else out.println(date);%>
												</td>
												
												<td style = "width:50px;">
													<%=goalsnapshotList.get(goalSnapShotCount).getCostBenefit()%>
												</td>
											</tr>
										<% } %>
										</tbody>
									</table>
								</div>
							</div>
							<!-- /widget -->
						</form>
						<!-- /goal snapshot history form -->
						<%}%>
						
						<script>
							window.onload = showGoalGraphs();
						</script>
						
					</div>
					<!-- /Content wrapper -->
				</div>
				<!-- /Container -->
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