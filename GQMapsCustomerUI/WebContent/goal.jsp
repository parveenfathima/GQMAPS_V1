
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="com.gq.meter.xchange.object.*"%>
<%@page import="com.gq.cust.GoalHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

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

	<script> 		
		
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


	function goalSubmit(actionName) {

		// if a check box is checked , set the corresponding hidden box to 1
		document.getElementById('actionName').value = actionName;

		// this is for the checkbox
		var onOff;

		var fields = document.ttform.elements["hd_chkApply"];
	    
		$('.input-row').each(function(index, row) {
	    	//alert('cb index = ' + index);
		    var innerObject = {};
		    $(':checkbox', row).each(function(index, checkbox) {
		    	onOff = checkbox.checked ? 'on' : 'off';
			    //alert('cb status  val = '+ onOff);
		    });
		    fields[index].value = onOff;
		   
		});

		if ( actionName == 'finalize') {
			// finalize 
			// calculate cost benefit by adding all the entered cost benefits
			var benefitsList = document.getElementsByName('cost_benefit');
			var totalBenefit = 0;
			
		    for (i = 0; i < benefitsList.length; i++){
		    	totalBenefit = totalBenefit + parseFloat(benefitsList[i].value);
		    }
		    alert("totalBenefit " + totalBenefit);
		    document.getElementById('gs_cost_benefit').value = totalBenefit;
		}
		else {
			// save
			//alert('our checkbox status = '+ JSON.stringify(jsonObject));
		}
			
		alert ( 'gspform on submit = ' + JSON.stringify($('form').serializeObject()) );
		
       return false;
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
		alert("num check boxes  = " + cbCount + " , checked = "+ cbCheckedCount);
		return false;
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
		
		System.out.println("inside goal.jsp e <" + enterpriseId +"> , g <" + goalId + "> , i <"+ goalInputs +">");
		
		GoalHelper gh = new GoalHelper(enterpriseId, goalId , goalInputs );
		//GoalHelper gh = new GoalHelper(enterpriseId, goalId , "__date__=10/16/2013 12:0:0~__asset_id__=C-000c293c937a" );
		
		Goal g = gh.getGoal();
		List<GoalSnpsht> goalsnapshotList = gh.getGoalSnapshot();
		List<TemplateTaskDetails> goalTaskTmpltChkList = gh.getTemplateTaskDetails();
	%>

	<h1 align="center"><%=g.getDescr()%></h1>
	
	<!-- form to display goal snapshot history-->
<%
if(g.getTimeBound().equals("y")){ %>
	<form id='gsform' method="post" >

		<div id="goalSnapshtDiv">
			<table id="tblGoalsnapsht" border="1">
				<tr bgcolor="green" style="color: white;">
					<th>Snapshot Id</th>
					<th>Start Date</th>
					<th>End Date</th>
					<th>Notes</th>
					<th>Realized Benefit</th>
				</tr>

				<%
					for (int i = 0; i < goalsnapshotList.size(); i++) {
				%>
				<tr>
					<td><%=goalsnapshotList.get(i).getSnpshtId()%></td>
					<td><%=goalsnapshotList.get(i).getStartDate()%></td>
					<td><%=goalsnapshotList.get(i).getNotes()%></td>
					<td><%=goalsnapshotList.get(i).getEndDate()%></td>
					<td><%=goalsnapshotList.get(i).getCostBenefit()%></td>

					<%
						}
					%>

				</tr>

			</table>
		</div>
	
	</form>
<%} %>
	<!-- form to display task template -->
<%
if(g.getTimeBound().equals("y")){ %>
<h1 align="center"> Tasks</h1>
<a id = "serverAsset" href = "#" onclick="serverList();">Asset List</a> <% } %>
<form name="ttform" action="goal.jsp" method="POST">
	<table id="tblTasktmplt" border="1">
		<tr bgcolor="green" style="color: white;">
			<%
					if(g.getTimeBound().equals("y")){ %>
			<th>Task</th>
			<th>UserNotes</th>
			<th>Benefit</th>
			<th>SystemNotes</th>
			<th>Apply</th>
			<% } %>
		</tr>
		
		<!-- Showing color change for alternative rows  -->
		<%
			int i = 0;
			for (i = 0; i < goalTaskTmpltChkList.size(); i++) {
				%>

				<tr bgcolor="pink" class="input-row">
		
					<td><%=goalTaskTmpltChkList.get(i).getDescr()%>
					<!--  we will just store the task id in a hidden field as well -->
					<input type="hidden" name="taskid" 
							 value=<%=goalTaskTmpltChkList.get(i).getTask_id() %>>
					</td>
					<%
					if(g.getTimeBound().equals("y")){ %>
					<td><input type="text" name="usernotes" id="usrnts" 
							 value=<%=goalTaskTmpltChkList.get(i).getUsr_notes()%>></td>
							
					<td><input type="text" name="cost_benefit" id ="cost_benefit" 
							value=<%=goalTaskTmpltChkList.get(i).getCost_benefit()%>></td>
							
					<td><input type="text" name="systemnotes" id ="sysnotes"
							value=<%=goalTaskTmpltChkList.get(i).getSys_notes()%>></td>
						
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
   <% if(g.getTimeBound().equals("y")){ %>
		<input type="button" id="save" value="Save&Exit" onclick="goalSubmit('save')">
		<input type="button" id="finalize" value="Finalize" onclick="goalSubmit('finalize') ">
			
		<!--  the following entries are for providing the goal snapshot entry -->
		<input type="text" name="gs_notes" value="please enter a snapshot note">
		<input type="hidden" name="gs_cost_benefit" id="gs_cost_benefit" >
		<input type="hidden" name="gs_id" value="<%=goalTaskTmpltChkList.get(0).getSnpsht_id() %>">
		<input type="hidden" name="gs_entpid" value="<%=enterpriseId %>">
		<input type="hidden" name="gs_goalid" value="<%=goalId %>">
		
		<!-- mention what action is this -->
		<input type="hidden" name="actionName" id="actionName" >
		<% 
		} 
		%>
		
		
</form>

	<script>
	  	window.onload = showGoalGraphs();	 
	</script>

</body>

</html>
