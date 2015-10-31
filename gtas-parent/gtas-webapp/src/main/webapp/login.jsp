	<!DOCTYPE html>
<head>
	<meta charset="utf-8" />
	<title>GTAS Login</title>
	<meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<meta content="" name="description" />
	<meta content="" name="author" />
	<!-- BEGIN GLOBAL MANDATORY STYLES -->
	<link href="resources/login/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<link href="resources/login/assets/plugins/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>
	<link href="resources/login/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
	<link href="resources/login/assets/css/style-metro.css" rel="stylesheet" type="text/css"/>
	<link href="resources/login/assets/css/style.css" rel="stylesheet" type="text/css"/>
	<link href="resources/login/assets/css/style-responsive.css" rel="stylesheet" type="text/css"/>
	<link href="resources/login/assets/css/themes/default.css" rel="stylesheet" type="text/css" id="style_color"/>
	<!-- <link href="resources/login/assets/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/> -->
	<link rel="stylesheet" type="text/css" href="resources/login/assets/plugins/select2/select2_metro.css" />
	<!-- END GLOBAL MANDATORY STYLES -->
	<!-- BEGIN PAGE LEVEL STYLES -->
	<link href="resources/login/assets/css/pages/login-soft.css" rel="stylesheet" type="text/css"/>
	<!-- END PAGE LEVEL STYLES -->
	<style>
	.backstretch {
		opacity: 0;
	}
	</style>
</head>
<!-- END HEAD -->

<!-- BEGIN BODY -->
<body class="login">
	<!-- BEGIN LOGO -->
	<div class="logo">
		<!-- PUT YOUR LOGO HERE -->
	</div>
	<!-- END LOGO -->
	<!-- BEGIN LOGIN -->
	<div class="content">
		<!-- BEGIN LOGIN FORM -->
		<form class="form-vertical login-form" action="j_spring_security_check" method="post">
			<h3 class="form-title">Login to your account</h3>
			<div class="alert alert-error hide">
				<button class="close" data-dismiss="alert"></button>
				<span>Enter any username and password.</span>
			</div>
			<div class="control-group">
				<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
				<label class="control-label visible-ie8 visible-ie9">Username</label>
				<div class="controls">
					<div class="input-icon left">
						<i class="icon-user"></i>
						<input class="m-wrap placeholder-no-fix" type="text" autocomplete="off" placeholder="Username" name="j_username" id="j_username"/>
					</div>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label visible-ie8 visible-ie9">Password</label>
				<div class="controls">
					<div class="input-icon left">
						<i class="icon-lock"></i>
						<input class="m-wrap placeholder-no-fix" type="password" autocomplete="off" placeholder="Password"  name="j_password" id="j_password"
						/> <!-- onchange="encode(this);" -->
					</div>
				</div>
			</div>
			<div class="form-actions">

				<button type="submit" class="btn blue pull-right">
				Login <i class="m-icon-swapright m-icon-white"></i>
				</button>            
			</div>


		</form>
	</div>
		<!-- END LOGIN FORM -->        
		<!-- BEGIN FORGOT PASSWORD FORM -->

		<!-- END FORGOT PASSWORD FORM -->
		<!-- BEGIN REGISTRATION FORM -->

		<!-- END REGISTRATION FORM -->
	</div>
	<!-- END LOGIN -->
	<!-- BEGIN COPYRIGHT -->
	
	<!-- END COPYRIGHT -->
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	<!-- BEGIN CORE PLUGINS -->   <script src="resources/login/assets/plugins/jquery-1.10.1.min.js" type="text/javascript"></script>
	<script src="resources/login/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
	<!-- IMPORTANT! Load jquery-ui-1.10.1.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
	<script src="resources/login/assets/plugins/jquery-ui/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>      
	<script src="resources/login/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<!-- <script src="resources/login/assets/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js" type="text/javascript" ></script> -->
	<!--[if lt IE 9]>
	<script src="resources/login/assets/plugins/excanvas.min.js"></script>
	<script src="resources/login/assets/plugins/respond.min.js"></script>  
	<![endif]-->   
	<script src="resources/login/assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
	<script src="resources/login/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>  
	<script src="resources/login/assets/plugins/jquery.cookie.min.js" type="text/javascript"></script>
	<script src="resources/login/assets/plugins/jquery-validation/dist/jquery.validate.min.js" type="text/javascript"></script>
	<script src="resources/login/assets/plugins/backstretch/jquery.backstretch.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="resources/login/assets/plugins/select2/select2.min.js"></script>
	<script src="resources/login/assets/scripts/app.js" type="text/javascript"></script>
	<script src="resources/login/assets/scripts/login-soft.js" type="text/javascript"></script>      
	<script>
	
		function encode(val){
			document.getElementById('j_password').value = btoa(val.value);
			};

	
		jQuery(document).ready(function() {     
		  App.init();
		  Login.init();
		});
	</script>
</body>
</html>
