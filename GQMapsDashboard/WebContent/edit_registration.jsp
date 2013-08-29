<!doctype html>
<html>
    <head>  
        <meta charset="utf-8">
        <title>Edit Enterprise</title>         
        
        <link rel="stylesheet" href="SlickGrid-master/slick.grid.css" type="text/css"/>
        <link rel="stylesheet" href="SlickGrid-master/css/smoothness/jquery-ui-1.8.16.custom.css" type="text/css"/>
<!--        <link rel="stylesheet" href="SlickGrid-master/examples/examples.css" type="text/css"/>  -->
        
        <!--<link rel="stylesheet" href="/resources/demos/style.css" /> -->
        
        <link type="text/css" href = "css/gqmaps.css" rel="stylesheet" />  
         
        <script src = "jquery-ui-1.10.2.custom/js/jquery-1.9.1.js" ></script>
                
        <script src="SlickGrid-master/lib/jquery-1.7.min.js"></script>
        <script src="SlickGrid-master/lib/jquery.event.drag-2.2.js"></script>
        
        <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=AIzaSyBGIi2rSK9Qp2dY1EewX-JUsK0sjQsxAak&sensor=false"></script> 
        <script src="jquery-ui-1.10.2.custom/js/jquery-ui-1.10.0.custom.js"></script>        
        <script src="SlickGrid-master/slick.core.js"></script>
        <script src="SlickGrid-master/slick.grid.js"></script>      
        <script src="SlickGrid-master/slick.dataview.js"></script>
        
		<script src = "js/jstorage.js"> </script>   
        <script src = "js/rest_service.js"> </script>            
        <script src = "js/general.js"> </script>      
        <script src = "js/edit_registration.js"> </script>            		 

	   <script type="text/javascript">
           $(function() {
                   $("#txtNewExpiry").datepicker({ dateFormat: "dd-mm-yy" }).val()
           });
       </script>
           
        <style>
			body { font-size: 62.5%; }
			label, input { display:block; }
			input.text { margin-bottom:12px; width:95%; padding: .4em; }
			fieldset { padding:0; border:0; margin-top:25px; }
			h1 { font-size: 1.2em; margin: .6em 0; }
			div#users-contain { width: 350px; margin: 20px 0; }
			div#users-contain table { margin: 1em 0; border-collapse: collapse; width: 100%; }
			div#users-contain table td, div#users-contain table th { border: 1px solid #eee; padding: .6em 10px; text-align: left; }
			.ui-dialog .ui-state-error { padding: .3em; }
			.validateTips { border: 1px solid transparent; padding: 0.3em; }
        </style>
			 
    </head>
    
    <body class="body">
                             
    	<div class="main-div"> <!--main div-->
                <div class="header"><!--header -->
                    <img src="theme/images/gquotient_logo.png"  style = "padding-top:25px; padding-left:25px" width="161" height="53" align="left"/>
                    <p class="hesder-font" style = "padding-top:35px;padding-left:300px ">Edit Enterprise</p> 
                    <p align="right" style="padding-right:10px"> <a href="login.jsp" onClick="logout();"><font size="+1"> Logout </font></a></p>
                </div><!--/header -->
                
                <div id="dlgGeneral">
                		<br>
                		<center><h1 id="hBasic"> </h1></center>
                        <font color="#FF0000"> * </font> indicates required fields
                        <form>
                            <fieldset>                            
           
           						<table>
                                	<tr>
                                    	<td style = "padding-left: 5px;"><label for="txtEID">Enterprise ID <font color="#FF0000"> * </font> </label></td>
                                        <td ><input type="text" name="txtEID" id="txtEID"  style = "width:100px;"/></td>
                                        <td style = "padding-left: 5px;"><label for="txtEName" >Enterprise Name <font color="#FF0000"> * </font> </label>          </td>
                                        <td><input type="text" name="txtEName" id="txtEName"  disabled="disabled" style = "width:100px;"></td>         
                                    </tr>
                                    <tr>
                                    	<td style = "padding-left: 5px;"><label for="txtUID" >User ID <font color="#FF0000"> * </font> </label>           </td>
                                        <td><input type="text" name="txtUID" id="txtUID" style = "width:100px;"/></td>
                                        <td style = "padding-left: 5px;"> <label for="txtPwd" >Password <font color="#FF0000"> * </font> </label>   </td>
                                        <td> <input type="text" name="txtPwd" id="txtPwd"  style = "width:100px;"/></td>
                                    </tr>
                                    
                                    <tr>
                                    	<td style = "padding-left: 5px;"> <label for="cmbSaveFwd" >Consume / Forward</label>                 </td>
                                        <td>                                    
                                        	<select name="cmbSaveFwd" id="cmbSaveFwd" style="width:100px" disabled = "disabled">
                                                <option value = "C">Consume</option>
                                                <option value = "F">Forward</option>
                                            </select>
                                        </td>
                                        <td style = "padding-left: 5px;"><label for="txtUrl">Forward URL</label></td>
                                        <td> <input type="text" name="txtUrl" id="txtUrl"  style = "width:100px;"  disabled = "disabled"/>  </td>
                                    </tr>

                                    <tr>
                                    	<td style = "padding-left: 5px;"><label for="txtESqft">Enterprise Sqft.</label></td>
                                        <td> <input type="text" name="txtESqft" id="txtESqft" style = "width:100px;"/></td>
                                        <td style = "padding-left: 5px;"><label for="txtEAsset" >Enterprise Asset Count</label></td>
                                        <td><input type="text" name="txtEAsset" id="txtEAsset" style = "width:100px;"/></td>
                                    </tr>
                                    <tr>
                                    	<td style = "padding-left: 5px;"><label for="txtDCSqft" >DC Sqft.</label></td>
                                        <td><input type="text" name="txtDCSqft" id="txtDCSqft" style = "width:100px;"/></td>
                                        <td style = "padding-left: 5px;"><label for="txtDCAsset" >DC Asset Count</label></td>
                                        <td><input type="text" name="txtDCAsset" id="txtDCAsset" style = "width:100px;"/></td>
                                    </tr>  
                                    <tr> 
                                        <td style = "padding-left: 5px;"><label for="txtDCUsed" >DC Used Percentage</label></td>
                                        <td><input type="text" name="txtDCUsed" id="txtDCUsed" style = "width:100px;"/></td>
                                        <td style = "padding-left: 5px;"><label for="txtDCTemp" >DC Temperature</label></td>
                                        <td><input type="text" name="txtDCTemp" id="txtDCTemp" style = "width:100px;"/></td>                                        
                                    </tr>
                                    <tr> 
                                    	<td style = "padding-left: 5px;"><label for="cmbEmpCount" >Total Employees</label></td>
                                        <td>
                                            <select name="cmbEmpCount" id="cmbEmpCount" style="width:100px">
                                                <option value = "1" selected = "selected">0-100</option>
                                                <option value = "2" >100-200</option>
                                                <option value = "3" >200-500</option>
                                                <option value = "4" >>500</option>
                                            </select>                                        
                                        </td>                                    
                                        <td style = "padding-left: 5px;"><label for="taComments" >Comments</label></td>
                                        <td><textarea name="taComments" id="taComments" disabled="disabled" rows = "2" style="overflow:auto;resize:none; width:100px"> </textarea></td>
                                        
                                    </tr>      
                                    <tr><td></td><td></td></tr>             
                                    <tr><td></td><td></td></tr>       
                                    <tr><td></td><td></td></tr>    
                                    <tr><td></td><td></td></tr>             
                                    <tr><td></td><td></td></tr>       
                                    <tr><td></td><td></td></tr>                      
                                    <tr> 
                                        <td style = "padding-left: 5px;"><label for="chkRegCompl"  style="font-size:12px; font-weight:bold; width:150px">Registration Completed</label></td>
                                        <td><input type = "checkbox" id = "chkRegCompl" name = "chkRegCompl"></td>
<!--                                        <td style = "padding-left: 5px;"><label for="chkActive" style="font-size:12px; font-weight:bold; ">Active Status</label></td>
                                        <td><input type = "checkbox" id = "chkActive" name = "chkActive" disabled = "disabled"></td>   -->                                      
                                    </tr>                                                                                                                                                                             
                                </table>
                                                                                                                                           
                                <br><br> 

                             
                                <input type="button" id = "sbtnGeneral" value="Submit" style="float:left; margin-left:175px; margin-right: 20px" onClick="saveGeneral();"/>
                                <input type="reset"  id = "btnCancel" value="Cancel" />
                               
                            </fieldset>
                        </form>
                </div>
                
                <div id="dlgMeter">
                		<center><h1 id = "hMeter"> </h1></center>
                        <font color="#FF0000"> * </font> indicates required fields
                        <form id = "frmMeter">
                            <fieldset>   
								<table>
                                	<tr>
                                    	<td><label for="txtMeterID" >Meter ID <font color="#FF0000"> * </font> </label></td>
                                        <td><input type="text" name="txtMeterID" id="txtMeterID" class="text ui-widget-content ui-corner-all"/></td>
                                    </tr> 
                                    <tr>
                                    	<td><label for="cmbProtocol" > Protocol <font color="#FF0000"> * </font> </label></td>
                                        <td>                                
                                        	<select name="cmbProtocol" id="cmbProtocol" style="width:100px">
                                			</select>
                                        </td>
                                    </tr>    
                                    <tr>
                                    	<td><label for="txtPhone" >Phone <font color="#FF0000"> * </font> </label></td>
                                        <td>                                
											<input type="text" name="txtPhone" id="txtPhone" class="text ui-widget-content ui-corner-all"/>
                                        </td>
                                    </tr>                                   
                                    <tr>
                                    	<td><label for="taAddress" >Address <font color="#FF0000"> * </font> </label></td>
                                        <td><textarea name="taAddress" id="taAddress" rows = "2" style="overflow:auto;resize:none;width:200px" > </textarea></td>
                                    </tr>
                                    <tr>
                                    	<td><label for="txtDesc">Description</label></td>
                                        <td><input type="text" name="txtDesc" id="txtDesc" class="text ui-widget-content ui-corner-all" /></td>
                                    </tr>                                
                                </table>
                                
                                <br>
                                
                            	<label>Meter List</label>    
                                                                
                                <div id="grdMain" style = "width:200px;height:50px; margin-left:10px;" class = "div-height" >
                                
                                    <div id="grdEListHead" style = "width:200px;height:20px; margin-left:50px; padding-bottom:0px;" class = "div-height"  >
                                        <table id = "tblMListHead" bordercolordark="#FF0000" style="border-radius: 1px 1px 1px 1px;">
                                            <tr bgcolor="#009900">
                                                <th width="80px"> Meter ID </th>
                                                <th width="120px"> Protocol </th>                                                                                     
                                            </tr>                     
                                        </table>
                                    </div>
                                    
                                    <div id="grdMListRows" style = "width:200px;height:43px; margin-left:50px; padding-top:0px; overflow-y:scroll; " class = "div-height" >
                                        <table id = "tblMeterList" bordercolordark="#FF0000" style="border-radius: 1px 1px 1px 1px;" border="1">                	                                                
                            			</table>                                      
                                    </div>
                                    
                                </div>     

								<br><br>
                                <input type="button" id = "sbtnMeter" value="Submit" style="float:left; margin-left:90px;" onClick="saveMeter();"/><input type="reset" value="Reset" /> 
                            </fieldset>
                        </form>
                </div>  
                
                <div id="dlgValidity">
                		<center><h1 id = "hValidity"> </h1></center>
                        <font color="#FF0000"> * </font> indicates required fields
                        <form id ="validity">
                            <fieldset>   
                                <label for="txtNewExpiry" >New Scan Expiry Date <font color="#FF0000"> * </font> </label> 
                                <input type="text" name="txtNewExpiry" id="txtNewExpiry" class="text ui-widget-content ui-corner-all"/>
                                                                
                                <label for="taValComments" >Purchase Comments <font color="#FF0000"> * </font> </label>  
                                <textarea name="taValComments" id="taValComments" rows = "2" style="overflow:auto;resize:none;width:200px"> </textarea>
								<br><br>
                                <input type="button" id = "sbtnValidity" value="Submit" style="float:left; margin-left:90px;" onClick="saveValidity();"/><input type="reset" value="Reset" /> 
                            </fieldset>
                        </form>
                </div>    
                
                
                <div id="dlgAssetSample">
                		<center><h1 id = "hMeter"> </h1></center>
                        <font color="#FF0000"> * </font> indicates required fields 
                                            
                </div>
                
                
                <div class="content-div"><!--content div--> 
                            
                        <br><br> 
                        <font class = "sectioncaption-font">Enterprise List</font>
                        
                        <div id="grdMain" style = "width:800px;height:115px; margin-left:50px;" class = "div-height"  >
	                        <div id="grdEListHead" style = "width:780px;height:30px; margin-left:50px; padding-bottom:0px;" class = "div-height"  >
                                <table id = "tblEListHead" bordercolordark="#FF0000" style="border-radius: 1px 1px 1px 1px;">
                                    <tr style="height:30px;" class = "innercaption-font" >
                                        <th colspan="3" width="100"> Action </th>
                                        <th width="100"> Entp. ID </th>
                                        <th width="100"> Entp. Name </th>
                                        <th width="100"> Meter Count </th>
                                        <th width="100"> Expiry Date </th>                                       
                                    </tr>
                                </table>
                             
                            </div>
                            <div id="grdEListRows" style = "width:780px;height:85px; margin-left:50px; padding-top:0px; overflow-y:scroll; " class = "div-height"  >
                            
                                <table id = "tblEList" bgcolor="#FFFFFF"="" bordercolordark="#FF0000" style="border-radius: 1px 1px 1px 1px;">

                                 </table>
                            </div>
                        </div>     

                        <p style = "margin-left:100px";><font color="#FF0000"> Red rows indicate incomplete registration </font></p>

				<!--/content div -->
                
                <div class = "footer" position = "fixed">
                    <font class="Copyrights-font">© Copyright 2012 Gquotient</font>
                </div> <!--/footer -->
        
        </div> <!--/main div -->
				
    </body>
    
</html>