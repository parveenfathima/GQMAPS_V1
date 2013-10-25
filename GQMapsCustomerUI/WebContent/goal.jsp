
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
<script src="js/goal.js"> </script>
<script src = "js/jstorage.js"> </script>   
   
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
		
		// function to find whether the form is edited by user
		function DetectChanges(cid){
			var cb = document.getElementsByTagName('input');			
			var retStr = "";
			var hfval = "";
			
			for ( var i = 0; i < cb.length; i++) {
				if (cb[i].getAttribute('type') == "text") {					
					retStr = retStr + cb[i].value.trim();									
				} 
			}
			
			if ( cid == 'ol'){
				document.getElementById('defval').value = retStr;
				
			}
			else{
				hfval = document.getElementById('defval').value;	
				if ( retStr == hfval ) {
					alert('form didnt change');
				}
				else {
					alert('form changed');
				}
			}
			return false;
		}
				
		String.prototype.trim = function() {
		    return this.replace(/^\s+|\s+$/g, "");
		};
		
	</script>

</head>

<body>

	<%
		String enterpriseId = request.getParameter("entpId");
		String goalId = request.getParameter("goalId");
		String goalInputs = request.getParameter("goalInputs");
		
		System.out.println("inside goal.jsp e <" + enterpriseId +"> , g <" + goalId + "> , i <"+ goalInputs +">");
		
		GoalHelper gh = new GoalHelper(enterpriseId, goalId , goalInputs );
		//GoalHelper gh = new GoalHelper(enterpriseId, goalId , "__date__=10/16/2013 12:0:0~__asset_id__=C-000c293c937a" );
		
		Goal g = gh.getGoal();
		List<GoalSnpsht> goalsnapshotList = gh.getGoalSnapshot();
		List<TemplateTaskDetails> goalTaskTmpltChkList = gh
				.getTemplateTaskDetails();
	%>

	<h1 align="center"><%=g.getDescr()%></h1>
	
	<!-- form to display goal snapshot history-->

	<form id="gspform">

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

	<!-- form to display task template -->

	<table id="tblTasktmplt" border="1">
		<tr bgcolor="green" style="color: white;">
			<th>Task</th>
			<th>UserNotes</th>
			<th>Benefit</th>
			<th>SystemNotes</th>
			<th>Apply</th>
		</tr>
		
		<!-- Showing color change for alternative rows  -->
		<%
			int i = 0;
			for (i = 0; i < goalTaskTmpltChkList.size(); i++) {
				%>
				<%
					if (i % 2 == 0) {
				%>
		
				<tr bgcolor="gray">
		
					<%
						} else {
					%>
				
			    <tr bgcolor="beige">
		
						<%
							}
						%>
			
					<td><%=goalTaskTmpltChkList.get(i).getDescr()%></td>
					
					<td><input type="text" name="usernotes" id="usrnts" onclick="serverList();" 
							 value=<%=goalTaskTmpltChkList.get(i).getUsr_notes()%>></td>
							
					<td><input type="text" name="benefit" onclick="alert('Click!');"
							value=<%=goalTaskTmpltChkList.get(i).getCost_benefit()%>></td>
							
					<td><input type="text" name="systemnotes"
							value=<%=goalTaskTmpltChkList.get(i).getSys_notes()%>></td>
						
					<!-- Checking applied date is null -->		
					<%
						if (goalTaskTmpltChkList.get(i).getApply_date() == null) {
					%>
					<td><input type="checkbox" id="chkApply"></td>
					<%
						} else {
					%>
					<td><input type="checkbox" id="chkApply" checked="checked"></td>
					<%
						} // end apply date chek box logic
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

		<input type="button" id="save" value="Save&Exit"
			onclick="DetectChanges('se')">
		<input type="button" id="finalize" value="Finalize"
			onclick="processForm(this) ">
		<input type="text" name="Comments" value=" ">
		<input type="hidden" id="defval" name="defaultVal" value="">

	<script>
//	  alert("inside ol script");
	  window.onload = showGoalGraphs();	 
	</script>

</body>

</html>



