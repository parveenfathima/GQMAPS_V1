
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
	
	<link type="text/css"
	href="jquery-ui-1.10.2.custom/css/blitzer/jquery-ui-1.10.2.custom.css"
	rel="stylesheet" />
	<link type="text/css" href="css/gqmaps.css" rel="stylesheet" />
	<link href="css/dashboard.css" rel="stylesheet" type="text/css" />
	<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,600,700' rel='stylesheet' type='text/css'>
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />

	
	<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=AIzaSyBGIi2rSK9Qp2dY1EewX-JUsK0sjQsxAak&sensor=false"></script>
  

	<script type="text/javascript" src="js/DateTimePickerJS/DateTimePicker.js"></script>  
	<script type="text/javascript" src="js/plugins/tables/jquery.dataTables.min.js"></script>
 
    <!--/custom scripts -->  
	<script src="js/goal.js"> </script>
	<script src = "js/jstorage.js"> </script>   
	<script src = "js/dashboard_full.js"> </script>
	<script src = "js/general.js"> </script> 
	<script src = "js/rest_service.js"> </script> 
	
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

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
	
	

	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<title>Goal screens for GQMaps</title>

	

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
	        } else {
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

       if (charCode != 46 && charCode > 31 
         && (charCode < 48 || charCode > 57)) {
          return false;
       }
       return true;
    }

	//function to length validation for cost benefit
	function isLengthCheck() {

		var cost_benefit=document.getElementById('cost_benefit').value;
		if(cost_benefit.length >9) {
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
			    	cost_benefit_value=0;
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

		if ( actionName == 'finalize') {
			//to check if all the tasks are applied
			
			if( ! processForm() ) {
				alert(" Please apply all the tasks ");
				return false;
			}
			
			
			// finalize 
			// calculate cost benefit by adding all the entered cost benefits
			var benefitsList = document.getElementsByName('cost_benefit');
			var totalBenefit = 0;
			
		    for (i = 0; i < benefitsList.length; i++){
		    	totalBenefit = totalBenefit + parseFloat(benefitsList[i].value);
		    }
		    document.getElementById('gs_cost_benefit').value = totalBenefit;
		}
		else {
			
		}
			
 	var formJson=JSON.stringify($('form').serializeObject());
 
		//we are here to construct a ajax call for invoking save and finalize operation. 
		  var vType = "POST";
		  var SUrl=$.jStorage.get("jsDBUrl")+"saveAndFinalize/submit";
		  $.ajax
			({
				type:vType,
				contentType: "application/json",
				url:SUrl,
				data: JSON.stringify($('form').serializeObject()),
				async:false,
				dataType: "json",
				
				success:function(response)
				{
		 			window.location.href="dashboard_full.html";
				},
				error:function(json)
				{
					window.location.href="Error.jsp";
				} 
			});	

		 	  
	 }
	
//---------------------
		
			
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

		else
		{ 
			return false;
		}
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
			<img class="mergedimg" src="images/grey.png"> 
  			<div id="idletimeout" style="top: 150px; margin-left: 215px; margin-right: 200px; ">
        		Logging off in <span><!-- countdown place holder --></span>&nbsp;seconds due to inactivity.
        		<a id="idletimeout-resume" href="#">Click here to continue</a>.
    		</div>  
    
    		<%
				String enterpriseId = request.getParameter("entpId");
				String goalId = request.getParameter("goalId");
				String goalInputs = request.getParameter("goalInputs");
				
				GoalHelper gh = new GoalHelper(enterpriseId, goalId , goalInputs );
		
				Goal g = gh.getGoal();
				List<GoalSnpsht> goalsnapshotList = gh.getGoalSnapshot();
				List<TemplateTaskDetails> goalTaskTmpltChkList = gh.getTemplateTaskDetails();
				Date startDate=null;
				Date endDate=null;
				int year,month,date;
			%>
	
			<!-- Fixed top -->
			<div id="top">
				<div class="fixed">
					<a href="#" title="" class="logo"></a>
				</div>
			</div>
			<!-- /fixed top -->
	
	
			<!-- form to display task template -->
			<form name="ttform" action="/GQMapsCustomerServices/saveAndFinalize/submit" method="post">
	
			<!-- Content container -->
			<div id="container" style="margin-left:auto; margin-right:auto; max-width:70%;">
		
		    	<!-- Content wrapper -->
		    	<div class="wrapper">
	                <br/>
	                <br/>
        
			    	<h5 class="widget-name"><i class="icon-columns"></i><%=g.getDescr()%></h5>
			    
				    <!-- Table with checkboxes -->
	                <!--use tr class= success, warning, error and info for colors inside the placeholder table-->
	                <div class="widget">
                	
                    	<div class="table-overflow">
                    
                        	<table class="table table-bordered table-checks" class="table" id="select-all">
                        
                          <thead>
						  		<%if(g.getTimeBound().equals("y")){ %>
                              <tr style="font-size:80%;">
                              	<!--title of the table-->
                                  <th>Tasks</th>
                                  <th>Graph</th>
                              </tr>
                              <%}%>
                          </thead>
                          <tbody>
                          	 <%
								int i = 0;
								for (i = 0; i < goalTaskTmpltChkList.size(); i++) {
						  		%>
						  		<input type="hidden" name="taskid" value='<%=goalTaskTmpltChkList.get(i).getTask_id()%>'>
						  		<%if(i%2==0){ %>
                              	<tr class="success input-row">
                              	<%}else { %>
                              	<tr class="info input-row">
                              	<%} %>
                                  <%
                                  	if(g.getTimeBound().equals("y")){ 
										String usrNotes = goalTaskTmpltChkList.get(i).getUsr_notes();
										if ( usrNotes == null ) {
											usrNotes = "";
										}
										String sysNotes = goalTaskTmpltChkList.get(i).getSys_notes();
										if ( sysNotes == null ) {
											sysNotes = "";
										}
								
								%>
                                  <td style="width:42%;"> <span class="label label-inverse" style="line-height:20px;"><div style="white-space:normal;"><%=goalTaskTmpltChkList.get(i).getDescr()%></div> </span> <br/><br/><br/>
                                        
                                        	<div style="width:190px; position:relative;">		                  
                                        		<label class="control-label" style="font-size:80%;"><i class="icon-list-alt"></i>System Notes</label>
        						                    <div class="controls">
        						                    <textarea style="resize:none;" rows="1" cols="19" maxlength="198" name="systemnotes" id ="sysnotes"><%=sysNotes%></textarea></div>
        						             </div>
                                      				
                                      	
                                         	<div class="controls" style="position:relative; width:150px; bottom:55px;left:200px; height:30px;"><label class="control-label" style="font-size:80%;width:150px;"><i class="icon-pencil"></i>Benefit</label><input class="input-small" type="text" style="height:25px;" placeholder="Numbers only" name="cost_benefit" id ="cost_benefit" onkeypress="return isNumberKey(event)" maxlength="11" onkeyup="return isLengthCheck()" value="<%=goalTaskTmpltChkList.get(i).getCost_benefit()%>" />
													<input type="hidden" name="hdcost_benefit"> 
										 	</div>
              
                                        	<div style="position:relative; top:-25px;">           
		                                        <label class="control-label" style="font-size:80%;"><i class="icon-user"></i>User Notes</label>
		                                        <div class="controls"><textarea rows="1" cols="19" maxlength="198" name="usernotes" id="usrnts"><%=usrNotes%></textarea></div>
                                        	</div>

                                         	<!-- Checking applied date is null -->		
											<%
													if (goalTaskTmpltChkList.get(i).getApply_date() == null ) {
											%>
										 
										 	<div class="controls" style="position:relative;width:150px; bottom:60px; left:200px;" >
												<label class="control-label checkbox " style="font-size:80%;"><input type="checkbox" id="inlineCheckbox1" class="styled" name="chkApply"><i class="icon-ok-sign"></i>Apply</label>
												<input type="hidden" name="hd_chkApply" >
									    	</div>
										
											<%
												} else {
											%>
											<div class="controls" style="position:relative;width:150px; bottom:60px; left:200px;" >
												<label class="control-label checkbox " style="font-size:80%;"><input type="checkbox" id="inlineCheckbox1" class="styled" name="chkApply"  checked="checked"><i class="icon-ok-sign"></i>Apply</label>
												<input type="hidden" name="hd_chkApply" >
									    	</div>
											<%
												} %>
								  </td>
											<% 
												}// end apply date chek box logic 
											%>
    										
                                  <td>
 								      <div class="well body">
  				            	
  				            			             <!-- Paste Chart div here-->
  				            			             
  				            			             <%
  				            			             if(g.getTimeBound().equals("y")) {
														if ( goalTaskTmpltChkList.get(i).getChartType().equals("line")) {
															%>
															
															<input type="hidden" id="chartData_<%=i%>" 
															value='<%=goalTaskTmpltChkList.get(i).getChartData().replaceFirst("string", "datetime")%>'>		
															<div id='chartDiv_<%=i%>' style = "width :500px; height:200px ;color: #00008a"></div> 					
															<%	
															}
														else {
															%>
															
															<input type="hidden" id="chartData_<%=i%>" 
															value='<%=goalTaskTmpltChkList.get(i).getChartData()%>'>	
															<div id='chartDiv_<%=i%>' style = "color: #00008h" ></div>		
															<%
															}
  				            			             }
  				            			             
  				            			             if(g.getTimeBound().equals("n")) {
  				            			            	if ( goalTaskTmpltChkList.get(i).getChartType().equals("line")) {
															%>
															
															<input type="hidden" id="chartData_<%=i%>" 
															value='<%=goalTaskTmpltChkList.get(i).getChartData().replaceFirst("string", "datetime")%>'>
															<span class="label label-inverse" style="line-height:20px;"><div style="white-space:normal;"><%=goalTaskTmpltChkList.get(i).getDescr()%></div> </span><br/><br/>	
															<div id='chartDiv_<%=i%>' style = "width :770px; height:200px ;color: #00008a"></div> 					
															<%	
															}
														else {
															%>
															<span class="label label-inverse" style="line-height:20px;"><div style="white-space:normal;"><%=goalTaskTmpltChkList.get(i).getDescr()%></div> </span><br/><br/> 
															<input type="hidden" id="chartData_<%=i%>" 
															value='<%=goalTaskTmpltChkList.get(i).getChartData()%>'>	
															<div id='chartDiv_<%=i%>' style = "color: #00008h" ></div>		
															<%
															}
  				            			             }
															%>
															<input type="hidden" id="chartType_<%=i%>"
															value='<%=goalTaskTmpltChkList.get(i).getChartType()%>'>
                                     	 </div>
				            	  	</td>
                              	</tr>
                              	
                              	<%
										} // end for to display tasks
						      	%>
								<!-- this used in goal.jsp for-loop -->
			
								<input type="hidden" id="taskCount" value=<%=i%>>	
								
                         </tbody>
                     </table>
                 </div>
           </div>
			    <!-- /table with checkboxes -->

			    <% if(g.getTimeBound().equals("y")){ %>
			       <div class="span6">
            
            		<button id="save" class="btn btn-info" type="button" style="position:relative; top:10px; left:10px;" onclick="goalSubmit('save')">Save and Exit</button>
            		<input class="input-large" name="gs_notes" maxlength="50" type="text" placeholder="Enter a Snapshot Note" style="display: block; position:relative;  top:-22px; left:150px;" />
            		<button id="finalize" class="btn btn-success" type="button" style="position:relative; top:-54px; left:363px;" onclick="goalSubmit('finalize')">Finalize</button>
            		
            		<!--  the following entries are for providing the goal snapshot entry -->
            		
            		<button class="btn btn-warning" type="button" style="position:relative; top:-56px; left:680px;" onclick="gotoDashboard()"> Back to Dashboard </button>
					<input type="hidden" name="gs_cost_benefit" id="gs_cost_benefit" >
					<input type="hidden" name="gs_id" value="<%=goalTaskTmpltChkList.get(0).getSnpsht_id() %>">
					<input type="hidden" name="gs_entpid" value="<%=enterpriseId %>">
					<input type="hidden" name="gs_goalid" value="<%=goalId %>">
		
					<!-- mention what action is this -->
					<input type="hidden" name="actionName" id="actionName" >

					<!-- a dummy field to hold the json string-->
					<input type="hidden" name="jsonFormData" id="jsonFormData" >
					<p class="widget-name"></p>
					</div>   
					
				<% 
					} 
				%>    
			
	<br>
	<%
					if(g.getTimeBound().equals("n")){
	%>
	<table>
	<tr><td><button class="btn btn-warning" type="button" style="position:relative; top:-30px; left:760px;" onclick="gotoDashboard()"> Back to Dashboard </button>
	</table>
	<% } %>
		</div>
		<!-- Content Wrapper ends -->
	</div>
	<!-- Content Container ends  -->
	
  </form>
  
  <!-- form to display goal snapshot history-->
<%
if(g.getTimeBound().equals("y")){ %>

 <h4 style="font-family: 'Open Sans', sans-serif; font-weight: 400; text-shadow: 0 1px 1px #fff; margin: 0; color: #303030;" align="center">Goal History</h4>
<br>
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
				<%
					for (i = 0; i < goalsnapshotList.size(); i++) {
						String goalSPLink = "goal_snapshot.jsp?snapShotId=" + goalsnapshotList.get(i).getSnpshtId() ;
				%>
				<tr style="font-size:80%;">
					<td style="width:20px;"><a href='<%=goalSPLink%>'>
										<%=goalsnapshotList.get(i).getSnpshtId()%></a></td>
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
					<td style="width:50px;"><%=year%>-<%if(month<10)out.println("0"+month);else out.println(month);%>-<%if(date<10)out.println("0"+date);else out.println(date);%></td>
					<td style="width:50px;"><%=goalsnapshotList.get(i).getCostBenefit()%></td>
					</tr>
					<%
						}
					%>				

			</table>
		</div>
	</div>
	</form>
	<%}%> 
	<script>
	  	window.onload = showGoalGraphs();	
	</script>
	
</div>
</body>
</html>