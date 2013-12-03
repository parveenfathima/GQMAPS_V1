
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

	<script src="jquery-ui-1.10.2.custom/js/jquery-1.9.1.js"></script>
	<script src="jquery-ui-1.10.2.custom/js/jquery-ui-1.10.2.custom.js"></script>
	<script src="js/goal.js"> </script>
	<script src = "js/jstorage.js"> </script>   
	<script src = "js/dashboard_full.js"> </script>  
	   
	<script src = "js/rest_service.js"> </script> 

	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<title>Goal screens for GQMaps</title>

	<style type="text/css">
	
		.taskTempltDiv {
			position: absolute;
			left: 5px;
			padding: 0px;
			width: 150px;
		}
		
		.goalSnapshtDiv {
			margin-left: 200px;
			padding: 0px;
			margin-right: 15px;
		}
		
		.bluec {
			color: #0000aa;
		}
		
		.greenc {
			color: #00aa00;
		}
	</style>

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

<body>

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

	<h1 align="center"><%=g.getDescr()%></h1>
	
	
	<!-- form to display task template -->
<%
if(g.getTimeBound().equals("y")){ %>
<h1 align="center"> Tasks</h1>
<a id = "serverAsset" href = "#" onclick="serverList();">Asset List</a> <% } %>

<form name="ttform" action="/GQMapsCustomerServices/saveAndFinalize/submit" method="post">
	<table id="tblTasktmplt" border="1">
		<tr bgcolor="green" style="color: white;">
			<%
					if(g.getTimeBound().equals("y")){ %>
			<th>Task</th>
			<th>UserNotes</th>
			<th>Benefit  (Only Numeric)</th>
			<th>SystemNotes</th>
			<th>Apply</th>
			<% } %>
		</tr>
		
		
		<!-- Showing color change for alternative rows  -->
		<%
			int i = 0;
			for (i = 0; i < goalTaskTmpltChkList.size(); i++) {
				%>

				<tr bgcolor="#FFFFCC" class="input-row">
		
					<td><%=goalTaskTmpltChkList.get(i).getDescr()%>
					<!--  we will just store the task id in a hidden field as well -->
					<input type="hidden" name="taskid" 
							 value=<%=goalTaskTmpltChkList.get(i).getTask_id() %>>
					</td>
					<%
					if(g.getTimeBound().equals("y")){  
					
					String usrNotes = goalTaskTmpltChkList.get(i).getUsr_notes();
					if ( usrNotes == null ) {
						usrNotes = "";
					}
					%>
					<td><textarea rows="3" cols="30" maxlength="198" name="usernotes" id="usrnts"><%=usrNotes%></textarea></td>
							
					<td><input type="text" name="cost_benefit" id ="cost_benefit" onkeypress="return isNumberKey(event)" 
					maxlength="11" onkeyup="return isLengthCheck()" value=<%=goalTaskTmpltChkList.get(i).getCost_benefit()%>>
					<input type="hidden" name="hdcost_benefit">
					</td>
							
					<%
				
					String sysNotes = goalTaskTmpltChkList.get(i).getSys_notes();
					if ( sysNotes == null ) {
						sysNotes = "";
					}
					%>
					<td><textarea rows="3" cols="30" maxlength="198" name="systemnotes" id ="sysnotes"><%=sysNotes%></textarea></td>
						
					<!-- Checking applied date is null -->		
					<%
					
						if (goalTaskTmpltChkList.get(i).getApply_date() == null ) {
					%>
					<td>
						<input type="checkbox" name="chkApply" >
						<input type="hidden" name="hd_chkApply" >
					</td>
					<%
						} else {
					%>
					<td>
						<input type="checkbox"  name="chkApply"  checked="checked">
						<input type="hidden" name="hd_chkApply" >
						
					</td>
					<%
						} 
							}// end apply date chek box logic 
					%>
				</tr> 
				
				<!--  this tr is for graph -->
				<tr>
					<td>
					<%
					if ( goalTaskTmpltChkList.get(i).getChartType().equals("line")) {
						%>
						<input type="hidden" id="chartData_<%=i%>" 
							value='<%=goalTaskTmpltChkList.get(i).getChartData().replaceFirst("string", "datetime")%>'>		
							<div id='chartDiv_<%=i%>' style = "width :400px; height:200px ;color: #00008a"></div> 					
						<%	
					}
					else {
						%>
							<input type="hidden" id="chartData_<%=i%>" 
									value='<%=goalTaskTmpltChkList.get(i).getChartData()%>'>							
							<div id='chartDiv_<%=i%>' style = "color: #00008h" ></div>		
						<%
					}
					%>
					
					<input type="hidden" id="chartType_<%=i%>"
							value='<%=goalTaskTmpltChkList.get(i).getChartType()%>'></td>
				</tr>
	
			<%
				} // end for to display tasks
			%>

			<!-- this used in goal.jsp for-loop -->
			
			<tr>
				<td><input type="hidden" id="taskCount" value=<%=i%>>	</td>
			</tr>

	</table>
	<br>
	<%
					if(g.getTimeBound().equals("n")){
	%>
	<table>
	<td><input type=button value="Take me to Dashboard" onclick="gotoDashboard()"/></td>
	</table>
	<% } %>
	<br>
   <% if(g.getTimeBound().equals("y")){ %>
   <table>
   <tr>
		<td><input type="button" id="save" value="Save&Exit" onclick="goalSubmit('save')"></td>
		<td><input type="button" id="finalize" value="Finalize" onclick="goalSubmit('finalize') "></td>
			
		<!--  the following entries are for providing the goal snapshot entry -->
		<td><input type="text" name="gs_notes" value="please enter a snapshot note" maxlength="50"></td>
		<td width="650"/>
		<td><input type="button" value="Take me to DASHBOARD" onclick="gotoDashboard()"></td>
		</tr>
		</table>
		<input type="hidden" name="gs_cost_benefit" id="gs_cost_benefit" >
		<input type="hidden" name="gs_id" value="<%=goalTaskTmpltChkList.get(0).getSnpsht_id() %>">
		<input type="hidden" name="gs_entpid" value="<%=enterpriseId %>">
		<input type="hidden" name="gs_goalid" value="<%=goalId %>">
		
		<!-- mention what action is this -->
		<input type="hidden" name="actionName" id="actionName" >

		<!-- a dummy field to hold the json string
		
 -->
	<input type="hidden" name="jsonFormData" id="jsonFormData" >
		<% 
		} 
		%>
		
		
</form>

<!-- form to display goal snapshot history-->
<%
if(g.getTimeBound().equals("y")){ %>
<h1 align="center">Goal History</h1>
	<form id='gsform' method="post" >

		<div id="goalSnapshtDiv">
			<table width="800" id="tblGoalsnapsht" border="1">
				<tr bgcolor="green" style="color: white;">
					<th>Snapshot Id</th>
					<th>Start Date</th>
					<th>Notes</th>
					<th>End Date</th>
					<th>Realized Benefit</th>
				</tr>

				<%
					for (i = 0; i < goalsnapshotList.size(); i++) {
						String goalSPLink = "goal_snapshot.jsp?snapShotId=" + goalsnapshotList.get(i).getSnpshtId() ;
				%>
				<tr>
					<td><a href='<%=goalSPLink%>'>
										<%=goalsnapshotList.get(i).getSnpshtId()%></a></td>
					<% startDate=new Date(goalsnapshotList.get(i).getStartDate().getTime());
					year=startDate.getYear()+1900;
					month=startDate.getMonth()+1;
					date=startDate.getDate();
					%>
					<td><%=year%>-<%=month%>-<%=date%></td>
					<td><%=goalsnapshotList.get(i).getNotes()%></td>
					<% endDate=new Date(goalsnapshotList.get(i).getEndDate().getTime());
					year=endDate.getYear()+1900;
					month=endDate.getMonth()+1;
					date=endDate.getDate();
					%>
					<td><%=year%>-<%=month%>-<%=date%></td>
					<td><%=goalsnapshotList.get(i).getCostBenefit()%></td>

					<%
						}
					%>

				</tr>

			</table>
		</div>
	
	</form>
<%} %>
	<script>
	  	window.onload = showGoalGraphs();	
	  	
	</script>

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




