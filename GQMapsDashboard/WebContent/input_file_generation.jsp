<!doctype html>
<html>
    <head>
         <meta charset="utf-8">
         <title>Input File Generation</title>
        
         <link type="text/css" href = "jquery-ui-1.10.2.custom/css/blitzer/jquery-ui-1.10.2.custom.css" rel="stylesheet" />
         <link type="text/css" href = "css/gqmaps.css" rel="stylesheet" />

         <script src = "jquery-ui-1.10.2.custom/js/jquery-1.9.1.js" ></script>
         <script src = "jquery-ui-1.10.2.custom/js/jquery-ui-1.10.2.custom.js" ></script>           
             
         <script src = "js/jstorage.js"> </script>
         <script src = "js/rest_service.js"> </script>  
		 <script src = "js/input_file_generation.js"> </script>  
         
    </head>
    
    <body class="body" >
        <div class="main-div"> <!--main div-->
            <div class="header"><!--header -->
            	<img src="theme/images/gquotient_logo.png"  style = "padding-top:25px; padding-left:25px" width="161" height="53" align="left"/>
                <p class="hesder-font" style = "padding-top:35px;padding-left:300px ">Enterprise Registration</p> 
                <p align="right" style="padding-right:10px"> <a href="login.jsp" onClick="logout();">Back to Home Page</a></p>
            </div><!--/header -->

            <div class="content-div"><!--content div-->

                    <div class="plain-div"> <!--plain div -->
      
                        <form id = "frmInputFile" class = "plain-form"  method = "POST" action = "createFile.jsp" >
                                   
                            <table align="center" id = "tblInputFile">  
                                <tr>
                                		<td colspan="2" > 

                                            <font size="2" style="color:#000; font-style:italic;"> <font color="#FF0000"> * </font> indicates mandatory fields </font>
                                        </td>
                                </tr>  
                                <tr></tr>
      
                               <tr>
                                     <td>
                                            <label for = "cmbMeters" class = "label-font" >Meter ID <font color="#FF0000"> * </font> </label>
                                     </td>
                                     <td colspan="2">   
                                            <select name = "cmbMeters" id = "cmbMeters" name = "cmbMeters" style="width:100px" >
                                           
                                            </select>
                                     </td>
                                </tr>
                    
                               <tr>
                                     <td>
                                            <label for="txtSwitch" class = "label-font">Switches <font color="#FF0000"> * </font> </label>
                                     </td>
                                     <td colspan="2">   
                                            <input type="text" name="txtSwitch" id="txtSwitch" disabled = "disabled"/> 
                                     </td>
                                </tr>
								<tr></tr><tr></tr><tr></tr><tr></tr>    								<tr></tr><tr></tr><tr></tr><tr></tr>                                                              
                               <tr>
                                        <td>
                                        	<label class = "label-font" style = "width: 20px;">Values</label>
                                        </td>
                                        <td>
                                        	<input type = "checkbox" id = "chkFull" name = "chkFull" > Full </input>
                                        </td>
								</tr>
                                <tr>
                                		<td></td>
                                        <td>
                                            <input type = "checkbox" id = "chkCompSS" name = "chkCompSS" > Computer Snapshot </input>
                                        </td>
                                </tr>
                                <tr>
                                		<td></td>                               

                                        <td>
                                            <input type = "checkbox" id = "chkCD" name = "chkCD" > Connected Devices </input>
                                        </td>
                                </tr>
                                <tr>
                                		<td></td>                                          
                                 
                                        <td>
                                            <input type = "checkbox" id = "chkIS" name = "chkIS" > Installed Software</input>
                                        </td>
                                </tr>
                                <tr>
                                		<td></td>  
                                        <td>                                            
                                            <input type = "checkbox" id = "chkProc" name = "chkProc" > Process </input>
                                        </td>
                                </tr>                                                           
								<tr></tr><tr></tr><tr></tr><tr></tr>								<tr></tr><tr></tr><tr></tr><tr></tr>
                               <tr>
                                     <td > 
                                     		<label for="txtComString" class = "label-font" >Community String</label>
                                     </td>
                                     
                                     <td > 
                                     		<label for="txtIPLB" class = "label-font" >IP Lower Bound</label>
                                     </td>
                                     
                                     <td > 
                                     		<label for="txtIPUB" class = "label-font" >IP Upper Bound</label>
                                     </td>                                                                          
                                </tr>
                                <tr>                                         
                                     <td>   
                                            <input type="text" name="txtComString" id="txtComString" width = "150px" value = "GQ"/>
									 
                                     </td>
                                     <td>   
                                            <input type="text" name="txtIPLB" id="txtIPLB" width = "150px" value = "1.1.1.1" />
                                     
                                     </td>
                                     <td>   
                                            <input type="text" name="txtIPUB" id="txtIPUB" width = "150px" value = "2.2.2.2" />
                                     </td>
                                     <td>   
                                            <input type="button" name="btnAdd" id="btnAdd" value = "Add to Grid"/>
                                     </td>
                                </tr>                                               
                 
                            </table>  
                            
                            <table style = "margin-left:225px" id = "tblCString" border="1">
                            	<tr><th> Community String </th> <th> IP Lower Bound</th> <th> IP Upper Bound </th><tr>
                            </table>
                            <br>
                            <center>
                                <input type="submit" value="Generate Input File" id = "submit"/>
                                <input type="reset" value="Reset"/>
                            </center>
                            <br>
                            
                        </form><!--/plain-form -->
                        
                    </div><!--/plain div -->
           
            </div><!--/content div -->
            
            <div class = "footer">
             	<font class="Copyrights-font">© Copyright 2012 Gquotient</font>
            </div>
        
        </div> <!--/main div -->
        
    </body>
    
</html>
