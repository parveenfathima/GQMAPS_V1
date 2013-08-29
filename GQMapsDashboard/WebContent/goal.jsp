<!doctype html>
<html>
    <head>
         <meta charset="utf-8">
         <title>Goals</title>
        
         <link type="text/css" href = "jquery-ui-1.10.2.custom/css/blitzer/jquery-ui-1.10.2.custom.css" rel="stylesheet" />
         <link type="text/css" href = "css/gqmaps.css" rel="stylesheet" />
         
         <script type='text/javascript' src='https://www.google.com/jsapi'></script>

         <script src = "jquery-ui-1.10.2.custom/js/jquery-1.9.1.js" ></script>
         <script src = "jquery-ui-1.10.2.custom/js/jquery-ui-1.10.2.custom.js" ></script>           
             
         <script src = "js/jstorage.js"> </script>
         <script src = "js/rest_service.js"> </script>  
         <script src = "js/goal.js"> </script>   
 
         
         
         <script type='text/javascript'>
		 
			var jsonPieData = "";
			var vType = "GET";								
			
			//var vUrl = $.jStorage.get("jsDBUrl") + "";	
			var vUrl = "http://localhost:8080/GQMapsCustomerServices/gqm-gqedp/Piechart/getPiechart";
			
			$.ajax
			({
				type:vType,
				contentType: "application/json",
				url:vUrl,
				async:false,
				dataType: "json",
				success:function(json)
				{
					alert("inside success");
					//drawGooglePieBarChart(json["data"], json["name"], json["title"]);
					
					//ajax return the DataTable object
					
				   // Load the Visualization API and the piechart package.
					google.load('visualization', '1.0', {'packages':['corechart']});
			  
					// Set a callback to run when the Google Visualization API is loaded.
					google.setOnLoadCallback(drawChart);
			  
					// Callback that creates and populates a data table,
					// instantiates the pie chart, passes in the data and
					// draws it.
					function drawChart() 
					{
	
						var data = new google.visualization.DataTable(json["data"]);
						
						// Set chart options
						var options = {'title':json["title"],
									   'is3D': true,
									   'width':225,
									   'height':100};
				
						// Instantiate and draw our chart, passing in some options.
						var chart = new google.visualization.PieChart(document.getElementById('divChart'));
						chart.draw(data, options);
					 }		
					 			                  
				},
				error:function(json)
				{
					alert("Error from loading chart data: " + json.status + " " + json.responseText);
				} 
			});
		 
		 </script>       

    </head>
    
    <body class="body" >
        <div class="main-div"> <!--main div-->
            <div class="header"><!--header -->
            	<img src="theme/images/gquotient_logo.png"  style = "padding-top:25px; padding-left:25px" width="161" height="53" align="left"/>
                <p id = "pGoalName" class="hesder-font" style = "padding-top:35px;padding-left:300px "></p> 
                <p align="right" style="padding-right:10px"> <a href="login.jsp" onClick="logout();">Back to Home Page</a></p>
            </div><!--/header -->

            <div class="content-div"><!--content div-->

                    <div class="plain-div"> <!--plain div -->
                    <h4>Task List </h4>
                    <div id="divTaskList">
                    	<table>
                        	<tr><th> Task </th><th> User Notes </th><th> Benefit </th> <th> System Notes </th> <th> Apply </th></tr>
                            <tr bgcolor="#00CCFF">
                            	<td>Decommision</td>
                                <td>3 Servers</td>
                                <td>20,000.00</td>
                                <td>SSSSSSSS </td>
                                <td>
                                    <input type = "checkbox" id = "chkApply"+i name = "chkApply"+i>                                                                
                                </td>
                        	</tr>
                            <tr>
                            	<td><div id="divChart" style = "width: 225px;height: 100px;background-color:#999"></div></td>
                            </tr>
                            
                            <tr bgcolor="#00CCFF">
                            	<td>Decommision</td>
                                <td>3 Servers</td>
                                <td>20,000.00</td>
                                <td>SSSSSSSS </td>
                                <td>
                                    <input type = "checkbox" id = "chkApply"+i name = "chkApply"+i>                                                                
                                </td>
                        	</tr>
                            <tr>
                            	<td><div id="divChart" style = "width: 225px;height: 100px;background-color:#999"></div></td>
                            </tr>                            
                        </table>
                    
                    </div>
                    
                    <h4>Goal Application History </h4>
                    <div id="divHistory">
                    	<table>
                        	<tr><th> Snapshot Id </th><th> Start Date </th><th> End Date </th> <th> Notes </th> <th> Realized Benefit </th></tr>
                            <tr>
                            	<td>1</td>
                                <td>01/02/2013</td>
                                <td>01/05/2013</td>
                                <td>Notes </td>
                                <td> 5000.00 </td>
                        	</tr>
                         </table>
                         <table>  
                            <tr>
                                 <td>
                                        <label for="taComments" class = "label-font">Comments</label>
                                 </td>
                                 <td>   
                                        <textarea name="taComments" id="taComments" rows = "3" style="overflow:auto;resize:none; width:200px" > </textarea>
                                 </td>
                            </tr>
                          </table>    
                              
                          <input type="button" id = "btnSave" value="Save&Exit" style="float:left; margin-left:175px; margin-right: 20px" />
                          <input type="button"  id = "btnFinalize" value="Finalize" />                          
                    
                    </div>     
                          
                    </div><!--/plain div -->
           
            </div><!--/content div -->

                <div class = "footer">
                    <font class="Copyrights-font">© Copyright 2012 Gquotient</font>
                </div>

        
        </div> <!--/main div -->
        
    </body>
    
</html>