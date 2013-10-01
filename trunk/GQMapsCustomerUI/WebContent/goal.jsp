<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="com.gq.meter.xchange.object.GoalSnpsht"%>
<%@page import="com.gq.meter.xchange.object.Goal"%>
<%@page import="com.gq.meter.xchange.object.TaskTmplt"%>
<%@page import="com.gq.cust.GoalHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title></title>
	<style type="text/css">

			.taskTempltDiv {
			  position: absolute;
			  left: 5px;
			  padding: 0px;
			  width: 150px;
			}
			
			.goalSnapshtDiv{
			
			  margin-left: 200px;
			  padding: 0px;
			  margin-right: 15px;
			}

			.bluec
			{
				color:#0000aa;
			}

			.greenc
			{
				color:#00aa00;
			}

	</style>


	
<script>
 

		function processForm(obj) {
			var cb = document.getElementsByTagName('input');
			var retStr = "";
			var cbCount = 0;
			var cbCheckedCount = 0;
			
			//alert("# elements in form = " + cb.length);
			
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
		
		function DetectChanges(cid)
		{
			var cb = document.getElementsByTagName('input');
			
			var retStr = "";
			var hfval = "";
			//alert("# elements in form = " + cb.length);
			
			for ( var i = 0; i < cb.length; i++) {
				if (cb[i].getAttribute('type') == "text") {
					
					//alert ( 'value of text box is ' + cb[i].value);
					retStr = retStr + cb[i].value.trim();
									
				} 
			}
			//alert("num check boxes  = " + cbCount + " , checked = "+ cbCheckedCount);
			//alert ( 'combined value of text box is ' + retStr);
			if ( cid == 'ol'){
				document.getElementById('defval').value = retStr;
				
			}
			else{
				hfval = document.getElementById('defval').value;	
				if ( retStr == hfval ) {
					alert('form didnt change');
				}
				else {
					alert('form changed y krupa');
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
		GoalSnpsht a = new GoalSnpsht();
		GoalHelper gh = new GoalHelper();
		List<GoalSnpsht> goalsnapshotList = gh.getGoalSnapshot();
		Goal g = gh.getGoal();
		TaskTmplt b = new TaskTmplt();
		List<TaskTmplt> taskTmpltList = gh.getTaskTmplt();
	%>
	<h1 align="center"><%=g.getGoalDescr()%></h1>
	
		<form  id="gspform">
	
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
					<td><%=goalsnapshotList.get(i).getEndDate()%></td>
					<td><%=goalsnapshotList.get(i).getUserNotes()%></td>
					<td><%=goalsnapshotList.get(i).getCostBenefit()%></td>
				

					<%
						}
					%>
					
				</tr>
				
			</table>
		</div>
	</form>
	
	
	<form id="tskform">
	
		<div id="taskTempltDiv" >
			  <table id="tblTasktmplt" border ="1">
			 	<tr bgcolor="green" style="color: white;">
			 		<th>Task</th>
			 		<th>UserNotes</th>
			 		<th>Benefit</th>
			 		<th>SystemNotes</th>
			 		<th>Apply</th>
			 	</tr> 
			 	
			 		<%
						for (int i = 0; i < taskTmpltList.size(); i++) {
					%>
					<%
					  	if ( i % 2 == 0) {
					%>
					
			 			<tr bgcolor="gray">
			 	
			 		<% } 
			 			else {
			 		%>
			 			<tr bgcolor="beige">
			 	
			 		<% } 
			 		%>
			 			 	
			 		<td><%=taskTmpltList.get(i).getDescr()%></td>
			 		<td><input type="text" name="usernotes" id="usrnts" value=" "></td>
			 		<td><input type="text" name="benefit"></td>
			 		<td><input type="text" name="systemnotes"></td>
			 		<td><input type="checkbox" id = "chkApply"  ></td>
			 		
			 		<% } %>
			 		
			 	</tr>
			  </table>
			  <input type="button" id = "save" value="Save&Exit" onclick="DetectChanges('se')"> 
			  <input type="button" id = "finalize" value="Finalize" onclick="processForm(this) ">
			  <input type="text" name="Comments" value=" " >
			  <input type="hidden" id = "defval" name="defaultVal" value=""> 
		</div>
			
	</form>
	
	
<script >window.onload = DetectChanges('ol');
</script>
</body>

</html>


<!-- multiple rows of goal template table with desc from gt table 
		for each row
			show the chart based on task asst row entitiy for the g.template table ( button for server list)
		end for
	
	rows from goal snapshot table 
	
	2 buttons - save & continue , finalize
	 -->
