<!doctype html>

<html>
    <head>
         <meta charset="utf-8">
         <title>Change Password</title>
        
         <link type="text/css" href = "jquery-ui-1.10.2.custom/css/blitzer/jquery-ui-1.10.2.custom.css" rel="stylesheet" />
         <link type="text/css" href = "css/gqmaps.css" rel="stylesheet" />
         
         <script src = "jquery-ui-1.10.2.custom/js/jquery-1.9.1.js" ></script>
         <script src = "jquery-ui-1.10.2.custom/js/jquery-ui-1.10.2.custom.js" ></script>  
         <script src = "js/jstorage.js"> </script> 
         <script src = "js/rest_service.js"> </script>         
         <script src = "js/general.js"> </script>                 
         <script src = "js/change_password.js"> </script>
         <script src = "js/check_answer.js"> </script>
        
                
    </head>
    
    <body class="body">
        <div class="main-div" data-role="page" id = "divChangePwd"> <!--main div-->
            <div class="header"><!--header -->
            	<img src="theme/images/gquotient_logo.png"  style = "padding-top:25px; padding-left:25px" width="161" height="53" align="left"/>
                <p class="hesder-font" style = "padding-top:35px;padding-left:300px ">Change Password</p> 
                <p align="right" style="padding-right:10px"> <a href="login.jsp">Back to Login</a></p>
            </div><!--/header -->
            
            <div class="content-div"><!--content div-->

                    <div class="plain-div"> <!--plain div -->
      
                        <form id = "frmChangePwd" class = "plain-form" >
                        
                        	<br>
                                   
                            <table align="center" id = "tblChangePwd">  
                            
                                <tr>
                                		<td colspan="2" > 

                                            <font size="2" style="color:#000; font-style:italic;"> <font color="#FF0000"> * </font> indicates mandatory fields </font>
                                        </td>
                                </tr>  
                                <tr></tr>                                               
                                <tr>
                                     <td>
                                            <label class = "label-font">User ID</label>
                                     </td>
                                     <td id ="tdUserID">   
                                            
                                     </td>
                                </tr>
                                <tr><td>&nbsp; &nbsp;</td><td>&nbsp;&nbsp;</td>  
                                <tr>
                                     <td>
                                            <label for="cmbQues1" class = "label-font">Security Question 1 <font color="#FF0000"> * </font></label>
                                     </td>
                                     <td>   
                                            <select name="cmbQues1" id="cmbQues1" style="width:200px">
                                            	
                                            </select>
                                     </td>
                                </tr>
                                
                                <tr>
                                     <td>
                                            <label for="txtAns1" class = "label-font">Answer 1 <font color="#FF0000"> * </font></label>
                                     </td>
                                     <td>   
                                            <input type="text" name="txtAns1" id="txtAns1"/> 
                                     </td>

                                </tr> 
                                <tr>
                                		<td colspan="2" > 
                                        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <font size="2" style="color:#F00; font-style:italic;">(Answer must be in one word and less than 10 characters) 
                                        </td>
                                </tr>
                     
                                <tr>
                                     <td>
                                            <label for="cmbQues2" class = "label-font">Security Question 2 <font color="#FF0000"> * </font></label>
                                     </td>
                                     <td>   
                                            <select name="cmbQues2" id="cmbQues2" style="width:200px">
                                            	
                                            </select>
                                     </td>
                                </tr>
                                
								<tr>
                                     <td>
                                            <label for="txtAns2" class = "label-font">Answer 2 <font color="#FF0000"> * </font></label>
                                     </td>
                                     <td>   
                                            <input type="text" name="txtAns2" id="txtAns2"/>
                                     </td>
                                </tr>
                                
                                <tr><td>&nbsp; &nbsp;</td><td>&nbsp;&nbsp;</td>  
                                <tr>
                                     <td>
                                            <label for="txtNewPwd" class = "label-font">New Password <font color="#FF0000"> * </font></label>
                                     </td>
                                     <td>   
                                            <input type="password" name="txtNewPwd" id="txtNewPwd"/>
                                     </td>
                                </tr> 
                                
                                <tr>
                                     <td>
                                            <label for="txtConfPwd" class = "label-font">Confirm Password <font color="#FF0000"> * </font></label>
                                     </td>
                                     <td>   
                                            <input type="password" name="txtConfPwd" id="txtConfPwd"/>
                                     </td>
                                </tr>  
                                
                                <tr>
                                		<td colspan="2" > 
                                        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <font size="2" style="color:#F00; font-style:italic;">(6-12 characters with at least one special, numeric,   </font> <br>
                                                                                    	
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            
                                            <font size="2" style="color:#F00; font-style:italic;">  uppercase and lowercase character) </font>
                                        </td>
                                </tr>
                            </table>
                                                  
  							<br><br> 
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                                                        
                            <input type = "checkbox" name="chkChageQA" id="chkChageQA" > <font class = "innercaption-font"> Change Security Questions</font>
                            <br><br><br>	
                            
							<table align="center" id = "tblChangeQA">      
                 
                             	<tr>
                                     <td>
                                            <label for="cmbChangeQues1" class = "label-font">Security Question 1 <font color="#FF0000"> * </font></label>
                                     </td>
                                     <td>   
                                            <select name="cmbChangeQues1" id="cmbChangeQues1" style="width:200px">
                                            	
                                            </select>
                                     </td>
                                </tr>
                                
                                <tr>
                                     <td>
                                            <label for="txtChangeAns1" class = "label-font">Answer 1 <font color="#FF0000"> * </font></label>
                                     </td>
                                     <td>   
                                            <input type="text" name="txtChangeAns1" id="txtChangeAns1"/> 
                                     </td>

                                </tr> 
                                <tr>
                                		<td colspan="2" > 
                                        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <font size="2" style="color:#F00; font-style:italic;">(Answer must be in one word and less than 10 characters) 
                                        </td>
                                </tr>
                     
                                <tr>
                                     <td>
                                            <label for="cmbChangeQues2" class = "label-font">Security Question 2 <font color="#FF0000"> * </font></label>
                                     </td>
                                     <td>   
                                            <select name="cmbChangeQues2" id="cmbChangeQues2" style="width:200px">
                                            	
                                            </select>
                                     </td>
                                </tr>
                                
                                <tr>
                                     <td>
                                            <label for="txtChangeAns2" class = "label-font">Answer 2 <font color="#FF0000"> * </font></label>
                                     </td>
                                     <td>   
                                            <input type="text" name="txtChangeAns2" id="txtChangeAns2"/>
                                     </td>
                                </tr>    
                                             
                            </table>  <!--/tblChangeQA -->
                            <br>
                            <center>
                                <input type="button" id = "submit" value="Submit" class = "button-font"/>
                                <input type="reset" value="Reset" class = "button-font"/>
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
