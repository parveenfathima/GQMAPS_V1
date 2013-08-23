<!doctype html>
<html>
    <head>
         <meta charset="utf-8">
         <title>Customer Dashboard - Forward</title>
        
         <link type="text/css" href = "jquery-ui-1.10.2.custom/css/blitzer/jquery-ui-1.10.2.custom.css" rel="stylesheet" />
         <link type="text/css" href = "css/gqmaps1.css" rel="stylesheet" />
         <link type="text/css" href="css/gqhome1.css" rel="stylesheet" />

         <script src = "jquery-ui-1.10.2.custom/js/jquery-1.9.1.js" ></script>
         <script src = "jquery-ui-1.10.2.custom/js/jquery-ui-1.10.2.custom.js" ></script> 
                   
         <script src = "js/rest_service.js"> </script>              
         <script src = "js/jstorage.js"> </script>
     	 <script src = "js/dashboard_forward.js"> </script>         
 

    </head>
    
    <body class="body">
        <div class="main-div"> <!--main div-->
            <div class="header"><!--header -->
            	<img src="theme/images/gquotient_logo.png"  style = "padding-top:25px; padding-left:25px" width="161" height="53" align="left"/>
                <p class="hesder-font" style = "padding-top:35px;padding-left:300px ">Customer Dashboard - Forward</p> 
                <p align="right" style="padding-right:10px"> <a href="login.jsp" onClick="logout();">Logout</a></p>
                
            </div><!--/header -->

            <div class="content-div" style="margin-left:5px;"><!--content div-->
				 <div class="plain-div"> <!--plain div -->
      
                        
                        
                        <div class="block-div left-block-div">
                            <div class="inner-head">
                                &nbsp; METER
                            </div>
                            <div class="inner-content">
                                 
                                	No. of Meters: <span id = "mCount" class = "span-font span-color"> </span>
                                
                                <br>
                                
                                
                                	<table id = "mTypes" class = "table-style" border="1px">
                                    	<tr> <th class = "th-font" >Protocol Types</th> </tr>
                                           
                                    </table>
                                
                                </span>
                            </div>
                            
                            <div class="inner-head">
                                &nbsp; ASSETS
                            </div>
                            <div class="inner-content " style="padding-top:0px;">
                            	Total number of assets 
                                	<span id = "totAssets" class = "span-font span-color"> </span> <br>
                                
                             
                                <span id = "saveFwd" > Data sent vs stored </span>          
                            </div>                       
                             
                        </div> <!--/left div -->
                        
                        
                        <div class="block-div center-block-div">
                            <div class="inner-head">
                            	&nbsp; ENTERPRISE
	                               
                            </div>
                            <div class="center-inner-content center-block-content-div" >
      


                            </div> 
    
                          <div class="inner-head">
                                &nbsp; DATE SENT Vs STORED
                            </div>
                            <div class="center-inner-content center-block-content-div">
                                Details
                            </div>                          
                            
                        </div>
                        
                        
                        <div class="block-div right-block-div">
                            <div class="inner-head">
                             	&nbsp; ALERTS
                                
                            </div>
                            <div class="inner-content right-block-content-div">
                                	Meter expiry within
                                	<span id = "mExpDays" class = "span-font span-color"> 
                                
                                	</span>
                                    days
                            </div>
                            
                        </div>  

                        
              </div><!--/plain div -->
           
            </div><!--/content div -->
            
            <div class = "footer">
             	<font class="Copyrights-font">© Copyright 2012 Gquotient</font>
            </div>
        
        </div> <!--/main div -->
        
    </body>
    
</html>
