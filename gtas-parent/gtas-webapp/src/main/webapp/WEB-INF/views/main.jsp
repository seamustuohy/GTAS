
<!DOCTYPE html>
<html lang="en"  ng-app="myApp">
  <head>
  <%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap core CSS -->
    <link href="resources/css/bootstrap.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="resources/css/style.css" rel="stylesheet">
    
    <title>GTAS</title>
        <link rel="stylesheet" href="resources/bower_components/bootstrap/dist/css/bootstrap.css">
        <link rel="stylesheet" href="resources/bower_components/ng-table/dist/ng-table.css" />
    
		    <style type="text/css">
		    .header{
		   		 background: rgb(247, 250, 250); 
					}
		    </style>
    
  </head>

  <body>
    <header>
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <img class="logo" src="resources/img/gtas_logo.png" width="1140px">
          </div>
          <div class="col-md-6">

          </div>
        </div>
      </div>
    </header>

    <div class="container container-main">
     <nav class="navbar navbar-default">
      <div class="container-fluid">
       <!--  <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div> -->
        
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav navbar-left">
            <li class="active"><a href="index.html">Home</a></li>
             
       <sec:authorize access="hasAnyAuthority('VIEW_FLIGHT_PASSENGERS','ADMIN')">
       
             <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button">View <span class="caret"></span></a>
              <ul class="dropdown-menu">
              
                <li><a href="#">Flights</a></li>
                <li><a href="#">Passengers</a></li>
           
              </ul>
            </li>
         </sec:authorize>   
            
           <sec:authorize access="hasAnyAuthority('MANAGE_RULES', 'MANAGE_QUERIES', 'MANAGE_WATCHLIST','ADMIN')">   
             <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button">Manage <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <sec:authorize access="hasAnyAuthority('MANAGE_RULES','ADMIN')">
                	<li><a href="#">Risk Criteria</a></li>
              	</sec:authorize>
              	  <sec:authorize access="hasAnyAuthority('MANAGE_WATCHLIST','ADMIN')">
                	<li><a href="#">Watchlists</a></li>
                </sec:authorize>
                  <sec:authorize access="hasAnyAuthority('MANAGE_QUERIES','ADMIN')">	
                	<li><a href="#">Queries</a></li>
                  </sec:authorize>	
              </ul>
            </li>
          </sec:authorize>
            
            
         <sec:authorize access="hasAnyAuthority('MANAGE_USERS','ADMIN')">   
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button">Admin <span class="caret"></span></a>
              <ul class="dropdown-menu">
               
               <sec:authorize access="hasAnyAuthority('MANAGE_RULES','ADMIN')">
                	<li><a href="#">Users</a></li>
                </sec:authorize>
                
                <sec:authorize access="hasAnyAuthority('ADMIN')">	
                	<li><a href="#">Properties</a></li>
                </sec:authorize>
                	
               </ul>
            </li>
         </sec:authorize>
            
             <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button">Settings <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="#">User Settings</a></li>
              </ul>
            </li>
            
          </ul>
          
          <ul class="nav navbar-nav navbar-right">
          	<li style="position: right;">
            	<a href="/gtas/logout.action">Logout</a>
            </li>
          </ul>
          
        </div><!--/.nav-collapse -->
        
      </div>
      
      <div>
      
      </div>
    </nav> <!-- end of nav -->

				<div class="container">
       				 <div class="row">
	             	   <div class="col-md-12">
	               	     <div ui-view></div>
	                </div>
	             </div>
	        
			</div>


	</div> <!-- main container -->
  
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
        <script src="resources/bower_components/angular/angular.js"></script>
    <script src="resources/bower_components/angular-ui-router/release/angular-ui-router.js"></script>
        
    <script src="resources/bower_components/bootstrap/dist/js/bootstrap.js"></script>
    <script src="resources/login/js/main.js"></script>
          
        
        
<!--
    <script src="resources/bower_components/angular-bootstrap/ui-bootstrap.js"></script>
     
-->	   
	   
        <script src="resources/bower_components/angular-bootstrap/ui-bootstrap-tpls.js"></script>

        <script src="resources/bower_components/ng-table/dist/ng-table.js"></script>
        <script src="resources/bower_components/spring-security-csrf-token-interceptor/dist/spring-security-csrf-token-interceptor.min.js"></script>
        <script src="app.js"></script>
        <script src="flights/FlightsController.js"></script>
        <script src="flights/FlightsService.js"></script>
        <script src="pax/PaxController.js"></script>
        <script src="pax/PaxService.js"></script>
    
  </body>
</html>
