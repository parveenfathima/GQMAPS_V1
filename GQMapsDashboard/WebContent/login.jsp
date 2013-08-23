<!doctype html>
<html>
    <head>
    
         <meta charset="utf-8">
         <title>Login</title>
        
         <link type="text/css" href = "jquery-ui-1.10.2.custom/css/blitzer/jquery-ui-1.10.2.custom.css" rel="stylesheet" />
         <link type="text/css" href = "css/gqmaps.css" rel="stylesheet" />
         
         <script src = "jquery-ui-1.10.2.custom/js/jquery-1.9.1.js" ></script>
         <script src = "jquery-ui-1.10.2.custom/js/jquery-ui-1.10.2.custom.js" ></script>
         <script src = "js/jstorage.js"> </script> 
          
       
         <script src = "js/login.js"> </script>
         <script src = "js/rest_service.js"> </script>  
                   
    </head>
    
    <body class="body" >
        <div class="main-div" id = "divLogin"> <!--main div-->
            <div class="header"><!--header -->
            	<img src="theme/images/gquotient_logo.png"  style = "padding-top:25px; padding-left:25px" width="161" height="53" align="left"/>
            </div><!--/header -->
            
            <div class="content-div"><!--content div-->
            
                <div class="left-div"><!--left div --> 
                    <div class="info-form">
                        <p class="info-font" id="productInfo"></p>
                    </div>
                </div><!--/left div -->
                
                <div class="right-div"><!--right div -->
                    <div class="login-form"> <!--login form div -->
                    
                        <div class="login-form-header"> <!--login form div header -->
                            <p class="login-font">Login</p>
                        </div><!--/login form div header -->
                        
                        <form method = "post">
                            <table>
                                <tr>
                                     <td class="user-id-td">
                                            <label class="label-font" for="txtUserId">User ID</label>
                                     </td>
                                     <td class="user-id-text-box-td">   
                                            <input class="up-text-box-style" type="text" name="txtUserId" id="txtUserId"/>
                                     </td>
                                </tr>
                                <tr>
                                     <td class="password-td">     
                                            <label class="label-font" for="pwdPassword">Password</label>
                                     </td>
                                     <td class="password-text-box-td">   
                                            <input class="up-text-box-style" type="password" name="pwdPassword" id="pwdPassword"/>
                                     </td>
                                </tr>
 							
                            	 <tr></tr> <tr></tr> <tr></tr> <tr></tr> <tr></tr>
                                
                                <tr>
                                     <td colspan="2" align="center">
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <input  type="button" id = "submit" value="LOGIN"/> 

                                     </td>
                               </tr>
                               <tr></tr> <tr></tr> <tr></tr> <tr></tr> <tr></tr>
                               <tr>
                                     <td class="forgot-password-td" colspan="2">     
                                     		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <a class="forgot-password-font" href="#" onClick="checkForValue();">Forgot Password/Reset?</a> &nbsp;&nbsp;&nbsp;
                                            <a class="forgot-password-font" href="#" onClick='alert("Please contact technical support at support@gquotient.com");'>Forgot User ID? </a>
                                     </td>
                               </tr>  
                            </table>  
                        </form>
                        
                    </div><!--/login form div -->
                    
                    <div class="register-form"> <!--register form div -->
                            <p class="register-form-font">If you are a new enterprise, then get registered with us to benefit our services....</p>
                            
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                
                            <input  type="button" value="REGISTER" onClick="window.location.href = 'add_registration.jsp'" />
                    </div><!--/register form div -->
                    
                </div><!--/right div -->
                
            </div><!--/content div -->
            
            <div class = "footer"  style ="margin-top: 395px">
            <br>
             	<p class="Copyrights-font" >© Copyright 2012 Gquotient</p>
            </div>
        
        </div> <!--/main div -->
            
    </body>
    
</html>
