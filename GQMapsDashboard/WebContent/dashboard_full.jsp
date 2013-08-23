<!doctype html>
<html>
    <head>
         <meta charset="utf-8">
         <title>Customer Dashboard</title>
        
         <link type="text/css" href = "jquery-ui-1.10.2.custom/css/blitzer/jquery-ui-1.10.2.custom.css" rel="stylesheet" />
         <link type="text/css" href = "css/gqmaps.css" rel="stylesheet" />
         <link type="text/css" href="css/gqhome.css" rel="stylesheet" />
     
          
        <script type="text/javascript" src = "http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.js" charset="utf-8"></script>
		<script type="text/javascript" src = "http://www.google.com/jsapi" charset="utf-8"></script>

		<script type="text/javascript">
            google.load("visualization", "1", {packages:["piechart", "corechart", "annotatedtimeline", "geomap"]});
        </script>
		   
         <script src = "jquery-ui-1.10.2.custom/js/jquery-1.9.1.js" ></script>
         <script src = "jquery-ui-1.10.2.custom/js/jquery-ui-1.10.2.custom.js" ></script> 
                       
         <script src = "js/jstorage.js"> </script> 
                            
         <script src = "js/rest_service.js"> </script>              
         <script src = "js/dashboard_full.js"> </script>   
           
    </head>
    
    <body class="body">
        <div class="main-div"> <!--main div-->
            <div class="header"><!--header -->
            	<img src="theme/images/gquotient_logo.png"  style = "padding-top:25px; padding-left:25px" width="161" height="53" align="left"/>
                <p id = "entpName" class="hesder-font" style = "padding-top:38px; padding-left:250px;">  </p> 
                <p align="right" style="padding-right:10px; " > <a href="login.html" onClick="logout();"><font style="font-size:15px;"> <b>Logout</b></a></p>
            
            </div><!--/header -->
          
            <div id="dlgAssetSample">
                                
            </div>   
                                
            <div class="content-div"><!--content div-->
				 <div class="plain-div"> <!--plain div -->
      
					<div style = "float:left"> <!--first row dive -->      
                    	   
                            <div class="block-div top-block-div" style = "margin-left:80px"> <!-- chart div -->
                                <div class="inner-head">
                                    CHART
                                </div>
                                <div  class="inner-content top-block-content-div" >
                                        <table>
                                            <tr>
                                                <td>
                                                    <div id="div_topcpuLoad" style="width: 400px; height: 200px; "></div>
                                                </td>
                                                <td>
                                                    <div id="div_topmemoryLoad" style="width: 400px; height: 200px;  padding-left:2px"></div>
                                                </td>
                                            </tr>
                                            <tr><td><a href="#" id = "moreGraphs"><b>Click here for more graphs... </a></td></tr>
                                       </table>

                                </div>
                            </div>    <!-- /chart div -->
                            
                            <div class="block-div right-block-div">  <!-- alert div -->                        
                                <div class="inner-head">
                                    ALERTS
                                    
                                </div>
                                <div class="inner-content right-block-content-div" id = "div_expiryAlert">

                                </div>
                                
                                
                                <div>
                                        <div >
                                            <input type = "button" id = "btnConfAssets" name = "btnConfAssets" value = "Configure Assets" onClick = "goToAssetEdit();" /> <br><br>
                                            <input type = "button" id = "btnThreshold" name = "btnThreshold" value = "SLA & Threshold" /> <br><br>
                                            <label for = "cmbGoals"> Available Goals </label>
                                            <select name="cmbGoals" class = "db-cmb-font" id="cmbGoals" >
                                                <option value = "">Power Usage Effectiveness</option>
                                                <option value = "">Virtualization</option>
                                                <option value = "">Monitoring</option>
                                            </select>
                                                                                         
                                        </div>

                            	</div>                                  
                            </div>   <!-- /alert div -->  
                            
                                            
                    </div> <!--/first row dive -->  
                    
                   
                    <div>  <!-- 2nd row -->
                           <div class="block-div medium-block-div" style = "margin-left:80px">                      
                                <div class="inner-head">
                                    Least Loaded Assets
                                    
                                </div>
                                <div class="inner-content medium-block-content-div">
                                    <div id= "div_leastAssets" style = "height:190px;">
                                                                
                                    </div>

                                    <input type = "button" id = "btnVirtualization" name = "btnVirtualization" value = "Virtualization" onclick = "setGoal(1, 'virtualization');"/>
                                    
                                </div>

                                
                    		</div>
                            
                          <div class="block-div medium-block-div">                      
                                <div class="inner-head">
                                    Top Installed Software
                                </div>
                                <div class="inner-content medium-block-content-div">
                                	<div id = "div_installedSW">
                                    	
                                    </div>
                                    <div>
                                		<input type = "button" id = "btnPDI" name = "btnPDI" value = "Personal Device Impact" /> 
                                    </div> 
                                </div>
                    		</div>
                            
                          <div class="block-div medium-block-div">                      
                                <div class="inner-head">
                                    PUE
                                </div>
                                <div class="inner-content medium-block-content-div">
                                	<div id = "divPUE">
                                    	<font class = "td-font">Current month PUE: <span id = "pue" class = "span-font span-color"> </span></font><br>
                                    </div>
                                    <div>    
                                		<input type = "button" id = "btnPUE" name = "btnPUE" value = "PUE" />                                         
                                    </div>
                                </div>

                    		</div>
                            
                          <div class="block-div medium-block-div">                      
                                <div class="inner-head">
                                    METER
                                    
                                </div>
                                <div class="inner-content medium-block-content-div" id = "div_meterCount>
                                
                                                                           
                                        <font class = "td-font" ><a href="#">Click here to download the meter jar file</a></font>
                                </div>
                    		</div>                                                                                    
                    </div> <!--/2nd row -->
                    

                    <div>  <!-- 3rd row -->
                           <div class="block-div medium-block-div" style = "margin-left:80px">                      
                                <div class="inner-head">
                                    Most Loaded Assets
                                </div>
                                <div class="inner-content medium-block-content-div">
                                    <div style="float:left; margin-left:5px; margin-right:5px; height=100px;" id = "div_mostAssets">
                           
                                    </div>
                                </div>
                    		</div>
                            
                          <div class="block-div medium-block-div">                      
                                <div class="inner-head">
                                    Top Black Listed Software
                                </div>
                                <div class="inner-content medium-block-content-div" id = "div_blacklistedSW">
									
                                </div>
                    		</div>
                            
                          <div class="block-div medium-block-div">                      
                                <div class="inner-head">
                                    Most Connected Server Assets
                                </div>
                                <div class="inner-content medium-block-content-div" id = "div_connectedServers">
									<span class = "span-font span-color"> * </span>To get the result, use developers' machine
                                </div>
                    		</div>
                            
                          <div class="block-div medium-block-div">                      
                                <div class="inner-head">
                                    Externally Connected Websites
                                </div>
                                <div class="inner-content medium-block-content-div" id = "div_connectedWebsites">
									<span class = "span-font span-color"> * </span>To get the result, use developers' machine
                                </div>
                    		</div>                                                                                    
                    </div> <!--/3rd row -->
                    
                        
              </div><!--/plain div -->
           
            </div><!--/content div -->
            
            <div class = "footer" >
             	<font class="Copyrights-font">� Copyright 2012 Gquotient</font>
            </div>
        
        </div> <!--/main div -->     

		       

         

    </body>
    
</html>
