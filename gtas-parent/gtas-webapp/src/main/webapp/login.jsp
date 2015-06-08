<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
	<title>GTAS</title>
	<meta charset="utf-8">
	<meta name="author" content="drdesign.co.za">
	<meta name="description" content=""/>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0" />
    <!-- Bootstrap Css -->
    
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap-theme.css">
    <link rel="stylesheet" type="text/css" href="resources/css/style.css">
    
    
	<link rel="stylesheet" type="text/css" href="resources/css/reset.css">
	<link rel="stylesheet" type="text/css" href="resources/css/main.css">
	<link rel="stylesheet" type="text/css" href="resources/css/menu.css">
	<link rel="stylesheet" type="text/css" href="resources/css/menu2.css">
	<link href='http://fonts.googleapis.com/css?family=ABeeZee:400,400italic' rel='stylesheet' type='text/css'>
    
    <script type="text/javascript" src="resources/js/jquery.js"></script>
    <script type="text/javascript" src="resources/js/main.js"></script>
	<script type="text/javascript" src="resources/js/script.js"></script>
	<script type="text/javascript" src="resources/js/menu2.js"></script>
    
    <!-- Bootstrap Js -->
    <script type="text/javascript" src="resources/js/bootstrap.js"></script>
</head>
<body>

<!-- Header -->
<header>
   <div class="container header">
    <div class="row">
        <div class="col-sm-12 col-md-12 col-xl-12">
            <div class="col-sm-3 div_margin">
                <!-- Company Logo -->
                <a href="#"><img src="" title="GTAS" alt="GTAS LOGO GOES HERE"/></a>
            </div>
            <div class="col-sm-9  div_margin" style="margin-top: 16px;">
                <!-- Company Title -->
                <h1>
                Global Tracking and Analysis System (GTAS)
                </h1>
                <p class="sub_title">
                    Tag line goes here
                </p>
                <br><br>
            </div>            
        </div>
    </div>
</div>     
</header>

<!-- Main Container Div -->
<div class="container">
    <div class="row">
        <div class="col-sm-12 col-md-12 col-xl-12">
            <div class="col-sm-8">
                <div class="welcome">
                    <div class="col-sm-12">
                        <p class="welcom_p">
                            Welcome to Global Tracking and Analysis System of United States Custom and Border Patrol. 
                        </p>
                    
                        <p class="welcom_p">
                            
                        </p>
                        
                        <p class="welcom_p">
                            If you are authorized USCBP employee please login to the system by providing your credentials. IF you don't have an account please contact the USCBP Administrator at gtasadmin@cbp.gov. 
                        </p>
                    </div>
                    
                </div>
            </div>
            <div class="col-sm-4">
            
            <form name="loginForm" method="GET" action="<c:url value="/login" />">
                <div class="login">
                <div style="margin-bottom: 12px;">
                <center>
                <br><br>
                <h2 style="color: rgb(0, 128, 128);">Login</h2>
                </center>
                </div>
                    <div  class="col-sm-12 login_sub">
                        <div class="col-sm-12 no_padding">
                            <div class="col-sm-4">
                                <p class="login_p">User Id :</p>
                            </div>
                            <div class="col-sm-8">
                                <input class="input_text_box" type="text" name="username" id="username" />
                            </div>
                        </div>
                        
                        <div class="col-sm-12 no_padding">
                            <div class="col-sm-4">
                                <p class="login_p">Password :</p>
                            </div>
                            <div class="col-sm-8">
                                <input class="input_text_box" type="password" name="password" id="password" />
                            </div>
                        </div>
                        
                        <div class="col-sm-12 no_padding">
                            <div class="col-sm-4">
                                
                            </div>
                            <div class="col-sm-8">
                                <input class="buttona" type="submit" name="login" value="Login" />
                                <input class="buttona" type="button" name="clear" value="Clear" />
                            </div>
                        </div>
                        
                        <div class="col-sm-12 no_padding">
                            <div class="col-sm-4">
                                
                            </div>
                            <div class="col-sm-8">
                              <a style="float: right;color: rgb(78, 78, 202);" href="#">Forgot Password</a>
                            </div>
                            
                        </div>
                        <center><div style="line-height: 30px;width: 100%"><font color="red">${message}</font></div></center>
                      
                    </div>
                    </form>  
                </div>
            </div>
        </div>
    </div>
</div>
		
<!-- Footer -->
<footer>
<div class="container footer" >
    <div class="row">
        <div class="col-sm-12 col-md-12 col-xl-12">
            <div>
                <p class="footer_p"> &copy; Copy Right USCBP 2015. All Rights Reserved</p>
            </div>
        </div>
    </div>
</div>
</footer>
	

</body>
</html>