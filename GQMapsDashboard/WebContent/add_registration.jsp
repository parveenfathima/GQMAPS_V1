<!doctype html>
<html>
    <head>
         <meta charset="utf-8">
         <title>Add Registration</title>
        
         <link type="text/css" href = "jquery-ui-1.10.2.custom/css/blitzer/jquery-ui-1.10.2.custom.css" rel="stylesheet" />
         <link type="text/css" href = "css/gqmaps.css" rel="stylesheet" />

         <script src = "jquery-ui-1.10.2.custom/js/jquery-1.9.1.js" ></script>
         <script src = "jquery-ui-1.10.2.custom/js/jquery-ui-1.10.2.custom.js" ></script>           
             
         <script src = "js/jstorage.js"> </script>
          
         <script src = "js/add_registration.js"> </script>
         <script src = "js/rest_service.js"> </script>  
         <script src = "js/general.js"> </script>         
         <script src = "js/check_answer.js"> </script>

    </head>
    
    <body class="body">
        <div class="main-div"> <!--main div-->
            <div class="header"><!--header -->
            	<img src="theme/images/gquotient_logo.png"  style = "padding-top:25px; padding-left:25px" width="161" height="53" align="left"/>
                <p class="hesder-font" style = "padding-top:35px;padding-left:300px ">Enterprise Registration</p> 
                <p align="right" style="padding-right:10px"> <a href="login.jsp" onClick="logout();">Back to Home Page</a></p>
            </div><!--/header -->

            <div class="content-div"><!--content div-->

                    <div class="plain-div"> <!--plain div -->
      
                        <form id = "frmAddRegn" class = "plain-form"  method = "post" >
                                   
                            <table align="center" id = "tblAddRegn">  
                                <tr>
                                		<td colspan="2" > 

                                            <font size="2" style="color:#000; font-style:italic; margin-left:-20px;"> <font color="#FF0000"> * </font> indicates mandatory fields </font>
                                        </td>
                                </tr>  
                                <tr></tr>
                                <tr>
                                     <td>
                                            <label for="txtEName" class = "label-font">Enterprise Name <font color="#FF0000"> * </font></label>
                                     </td>
                                     <td>   
                                            <input type="text" name="txtEName" id="txtEName" style="width:200px" />
                                     </td>
                                </tr>
      
                               <tr>
                                     <td>
                                            <label for = "cmbBusCategory" class = "label-font" >Business Line <font color="#FF0000"> * </font> </label>
                                     </td>
                                     <td>   
                                            <select name = "cmbBusCategory" id = "cmbBusCategory" name = "cmbBusCategory" style="width:205px">
                                           
                                            </select>
                                     </td>
                                </tr>
                    
                               <tr>
                                     <td>
                                            <label for="txtPhone" class = "label-font">Mobile Number <font color="#FF0000"> * </font> </label>
                                     </td>
                                     <td>   
                                            <input type="text" name="txtPhone" id="txtPhone" value = "" style="width:200px"  />
                                     </td>
                                </tr>

                               <tr>
                                     <td>
                                            <label for="txtEmail" class = "label-font" >E-Mail <font color="#FF0000"> * </font> </label>
                                     </td>
                                     <td>   
                                            <input type="email" name="txtEmail" id="txtEmail" value = "" style="width:200px" />
                                     </td>
                                </tr>   
                                
                                <tr></tr><tr></tr><tr></tr>
      								<tr><td> <font size="3px" style= "margin-left:-20px">Facility Details</font> </td></tr>
                   				<tr></tr><tr></tr><tr></tr>
			
                                <tr>
                                     <td>
                                            <label for="cmbEmpCount" class = "label-font">Total Employees</label>
                                     </td>
                                     <td>   
                                            <select name="cmbEmpCount" id="cmbEmpCount" style="width:205px">
                                            	<option value = "1" selected = "selected">0-100</option>
                                                <option value = "2" >100-200</option>
                                                <option value = "3" >200-500</option>
                                                <option value = "4" >>500</option>
                                            </select>                                     
                                     </td>
                                </tr> 
                            
                                 <tr>
                                       <td>
                                              <label for="txtESqft" class = "label-font">Enterprise IT Facility Usable Area</label>
                                       </td>
                                       <td>   
                                              <input type="text" name="txtESqft" id="txtESqft" style="width:200px" />
                                       </td>
                                  </tr>   
                                  
                                 <tr>
                                       <td>
                                              <label for="txtEAsset" class = "label-font">Total no. of IT Assets</label>
                                       </td>
                                       <td>   
                                              <input type="text" name="txtEAsset" id="txtEAsset" style="width:200px" />
                                       </td>
                                       

                                    <td colspan="2" > 
                                        <font size="2" style="color:#F00; font-style:italic;"> (Include Computers, Laptops, Printers, Switches, Routers & Storages) 
                                    </td>                                       
                                  </tr>                                     
                                  
                                  <tr>
                                       <td>
                                              <label for="txtDCSqft" class = "label-font">Datacenter Usable Area</label>
                                       </td>
                                       <td>   
                                              <input type="text" name="txtDCSqft" id="txtDCSqft" style="width:200px" />
                                       </td>
                                  </tr>                                    
                                                      
                                 <tr>
                                       <td>
                                              <label for="txtDCAsset" class = "label-font">Total no. of Datacenter Assets</label>
                                       </td>
                                       <td>   
                                              <input type="text" name="txtDCAsset" id="txtDCAsset" style="width:200px" />
                                       </td>
                                  </tr>

                                 <tr>
                                       <td>
                                              <label for="txtDCUsed" class = "label-font">Datacenter Occupancy Rate</label>
                                       </td>
                                       <td>   
                                              <input type="text" name="txtDCUsed" id="txtDCUsed" style="width:200px" />
                                       </td>
                                		<td colspan="2" > 
                                            <font size="2" style="color:#F00; font-style:italic;">(in %) 
                                        </td>                                       
                                  </tr>    

                                 <tr>
                                       <td>
                                              <label for="txtDCTemp" class = "label-font">Datacenter Temperature</label>
                                       </td>
                                       <td>   
                                              <input type="text" name="txtDCTemp" id="txtDCTemp" style="width:200px" />
                                       </td>

                                		<td colspan="2" > 
                                            <font size="2" style="color:#F00; font-style:italic;">(DC Temperature in celsius) 
                                        </td>
                                </tr>
                                                             
      <!--            
                               <tr>
                                     <td>
                                            <label for="cmbSaveFwd" class = "label-font">Consume / Forward <font color="#FF0000"> * </font></label>
                                     </td>
                                     <td>   
                                            <select name="cmbSaveFwd" id="cmbSaveFwd" style="width:100px">
                                            	<option value = "C">Consume</option>
                                                <option value = "F">Forward</option>
                                            </select>
                                     </td>
                                     <td> 
                                     		<label for="txtUrl" class = "label-font" >Forward SFTPURL</label>
                                     </td>
                                                                         
                                     <td>   
                                            <input type="text" name="txtUrl" id="txtUrl"/>
                                     </td>
                                </tr>      
   -->                  
   
                                <tr></tr><tr></tr><tr></tr>
      								<tr><td> <font size="3px"  style= "margin-left:-20px">Password Reset Enablement </font> </td></tr>
                   				<tr></tr><tr></tr><tr></tr>
                                   
                                <tr>
                                     <td>
                                            <label for="cmbQues1" class = "label-font">Security Question 1 <font color="#FF0000"> * </font> </label>
                                     </td>
                                     <td>   
                                            <select name="cmbQues1" id="cmbQues1" style="width:200px">
                                            	
                                            </select>
                                     </td>
                                </tr>
                                
                                <tr>
                                     <td>
                                            <label for="txtAns1" class = "label-font">Answer 1 <font color="#FF0000" > * </font></label>
                                     </td>
                                     <td>   
                                            <input type="text" name="txtAns1" id="txtAns1" value = "" style="width:200px"  /> 
                                     </td>

                                    <td colspan="2" > 
                                        <font size="2" style="color:#F00; font-style:italic;">(Answer must be in one word & less than 10 characters without special characters) 
                                    </td>
                                </tr>
                     
                                <tr>
                                     <td>
                                            <label for="cmbQues2" class = "label-font">Security Question 2 <font color="#FF0000"> * </font> </label>
                                     </td>
                                     <td>   
                                            <select name="cmbQues2" id="cmbQues2" style="width:205px">
                                            	
                                            </select>
                                     </td>
                                </tr>
                                 
                                <tr>
                                     <td>
                                            <label for="txtAns2" class = "label-font">Answer 2 <font color="#FF0000"> * </font> </label>
                                     </td>
                                     <td>   
                                            <input type="text" name="txtAns2" id="txtAns2" value = "" style="width:200px" />
                                     </td>
                                </tr> 
                                
                                 
                                                                                                                                                                                     
                                  <tr>
                                       <td>
                                              <label for="taComments" class = "label-font">Additional Notes</label>
                                       </td>
                                       <td>   
                                              <textarea name="taComments" id="taComments" rows = "3" style="overflow:auto;resize:none; width:200px" > </textarea>
                                       </td>
                                  </tr>                    
                                                
                 
                            </table>  
                            <br>
                            <center>
                                <input type="button" value="Submit" id = "submit"/>
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
