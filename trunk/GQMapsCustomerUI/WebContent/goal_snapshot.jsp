
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
	<script src="js/goal.jsp"> </script>
	<script src = "js/jstorage.js"> </script>   
	<script src = "js/dashboard_full.js"> </script>  
	<script src="js/goal.js"></script>
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
		String snapShotId = request.getParameter("snapShotId");

		GoalHelper gh = new GoalHelper( snapShotId );

		Goal g = gh.getGoal();
		List<GoalSnpsht> goalsnapshotList = gh.getGoalSnapshot();
		List<TemplateTaskDetails> goalTaskTmpltChkList = gh.getTemplateTaskDetails();
		Date startDate=null;
		Date endDate=null;
		int year,month,date;
	%>
		<!-- form to display goal snapshot history-->
		<h1 align="center"><%=g.getDescr()%></h1>
<%
if(g.getTimeBound().equals("y")){ %>
	<form id='gsform' method="post" >

		<div id="goalSnapshtDiv">
			<table id="tblGoalsnapsht" border="1">
				<tr bgcolor="green" style="color: white;">
					<th>Snapshot Id</th>
					<th>Start Date</th>
					<th>Notes</th>
					<th>End Date</th>
					<th>Realized Benefit</th>
				</tr>

				<%
					for (int i = 0; i < goalsnapshotList.size(); i++) {
						//String goalSPLink = "goal_snapshot.jsp?snapShotId=" + goalsnapshotList.get(i).getSnpshtId() ;
				%>
				<tr>
					<td><%=goalsnapshotList.get(i).getSnpshtId()%></td>
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
	<!-- form to display task template -->
<form name="ttform">
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
					<td><label for ="usernotes" id="usrnts"><%=goalTaskTmpltChkList.get(i).getUsr_notes()%> </label></td>
							
					<td><label for ="cost_benefit" id ="cost_benefit"><%=goalTaskTmpltChkList.get(i).getCost_benefit()%> </label></td>
							
					<td><label for ="systemnotes" id ="sysnotes"> <%=goalTaskTmpltChkList.get(i).getSys_notes()%></label></td>
						
					<!-- Checking applied date is null -->		
					<%
					
						if (goalTaskTmpltChkList.get(i).getApply_date() == null ) {
					%>
					<td> <label for ="chkApply" id= "chkapply" ></label></td>
					<%
						} else {
					%>
					<td>
						<label for ="chkApply" id= "chkapply"> <%=goalTaskTmpltChkList.get(i).getApply_date()%> </label></td>
					<%
						} 
					}
					}// end apply date chek box logic 
					%>
				</tr> 
				
		

	</table>
	<br>
	<input type="button" value="Take me to Dashboard" onclick="gotoDashboard()">
		
</form>
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

