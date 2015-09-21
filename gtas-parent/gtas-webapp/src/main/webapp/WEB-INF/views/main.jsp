
<!DOCTYPE html>
<html lang="en" ng-app="myApp">
	<head>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>GTAS</title>

	<link rel="stylesheet" href="dist/css/bootstrap.css" />
	<link rel="stylesheet" href="dist/css/gtas.css" />
	<link rel="stylesheet" href="dist/css/bootstrap-select.min.css" />
	<link rel="stylesheet" href="dist/css/awesome-bootstrap-checkbox.css" />
	<link rel="stylesheet" href="dist/css/bootstrap-slider.min.css" />
	<link rel="stylesheet" href="dist/css/selectize.bootstrap3.css" />
	<link rel="stylesheet" href="dist/css/bootstrap-datepicker.min.css" />
	<link rel="stylesheet" href="dist/css/ui-grid.css" />
	<link rel="stylesheet" href="dist/css/query-builder.default.css" id="qb-theme" />
	<link rel="stylesheet" href="dist/css/flags.css" />
	<link rel="stylesheet" href="dist/css/style-icomoon.css" />
	<link rel="stylesheet" href="dist/css/angular-material.min.css" />
	</head>
<body>
	<header></header>

	<div>
		<nav class="navbar navbar-default">
			<div id="navbar">
				<ul class="nav navbar-nav navbar-left" ng-controller="NavCtrl">
					<li ng-class="{active: isActive('')}"><a href="home.action">Dashboard</a></li>
					<sec:authorize
						access="hasAnyAuthority('VIEW_FLIGHT_PASSENGERS','ADMIN')">
						<li ng-class="{active: isActive('/flights')}"><a
							href="home.action#/flights">Flights</a></li>
						<li ng-class="{active: isActive('/passengers')}"><a
							href="home.action#/passengers">Passengers</a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyAuthority('MANAGE_QUERIES', 'ADMIN')">
						<li ng-class="{active: isActive('/query-builder')}"><a
							href="home.action#/query-builder">Queries</a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyAuthority('MANAGE_RULES', 'ADMIN')">
						<li ng-class="{active: isActive('/risk-criteria')}"><a
							href="home.action#/risk-criteria">Risk Criteria</a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyAuthority('MANAGE_WATCHLIST','ADMIN')">
						<li ng-class="{active: isActive('/watchlists')}"><a
							href="home.action#/watchlists">Watchlists</a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyAuthority('MANAGE_USERS','ADMIN')">

						<li ng-class="{active: isActive('/admin')}"><a
							href="home.action#/admin">Admin</a></li>
					</sec:authorize>

					<li class="dropdown"><a href="" class="dropdown-toggle"
						data-toggle="dropdown" role="button">Settings <span
							class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="">User Settings</a></li>
						</ul></li>

				</ul>

				<ul class="nav navbar-nav navbar-right" style="margin: 0;">
					<li><a href="logout.action">Logout</a></li>
				</ul>
			</div>

			<div></div>
		</nav>
		<!-- end of nav -->

		<div class="container">
			<div class="row">
				<div class="col-md-12">
					<div ui-view></div>
				</div>
			</div>

		</div>


	</div>
	<!-- main container -->

	<!-- for Debug purposes -->
	<script src="dist/all.min.js"></script>
<!--
	<script src="dist/js/angular.js"></script>
	<script src="dist/js/angular-ui-router.js"></script>
	<script src="dist/js/ct-ui-router-extras.js"></script>
	<script	src="dist/js/ui-bootstrap.js"></script>
	<script src="dist/js/ui-bootstrap-tpls.js"></script>
	<script src="dist/js/ng-table.js"></script>
	<script src="dist/js/spring-security-csrf-token-interceptor.min.js"></script>
	<script src="dist/js/moment.min.js"></script>
	<script src="dist/js/jquery.js"></script>
	<script src="dist/js/bootstrap.min.js"></script>
	<script src="dist/js/bootstrap-select.min.js"></script>
	<script src="dist/js/bootbox.js"></script>
	<script src="dist/js/bootstrap-slider.min.js"></script>
	<script src="dist/js/selectize.min.js"></script>
	<script src="dist/js/bootstrap-datepicker.min.js"></script>
	<script src="dist/js/jQuery.extendext.min.js"></script>
	<script src="dist/js/pdfmake.min.js"></script>
	<script src="dist/js/vfs_fonts.js"></script>
	<script src="dist/js/angular-material.min.js"></script>
	<script src="dist/js/angular-aria.min.js"></script>
	<script src="resources/bower_components/angular-animate/angular-animate.min.js"></script>
	<script src="resources/bower_components/angular-messages/angular-messages.min.js"></script>
	<script src="dist/js/ui-grid.js"></script>
	<script src="dist/js/query-builder.js"></script>
	<script src="dist/js/app.js"></script>
	<script src="dist/js/GridFactory.js"></script>
	<script src="dist/js/ModalGridFactory.js"></script>
	<script src="dist/js/QueryBuilderFactory.js"></script>
	<script src="dist/js/jQueryBuilderFactory.js"></script>
	<script src="dist/js/DashboardController.js"></script>
	<script src="dist/js/FlightsIIController.js"></script>
	<script src="dist/js/FlightsService.js"></script>
	<script src="dist/js/PaxController.js"></script>
	<script src="dist/js/PaxMainController.js"></script>
	<script src="dist/js/PaxDetailController.js"></script>
	<script src="dist/js/PaxService.js"></script>
	<script src="dist/js/PaxFactory.js"></script>
	<script src="dist/js/QueryBuilderController.js"></script>
	<script src="dist/js/RiskCriteriaController.js"></script>
	<script src="dist/js/RiskCriteriaService.js"></script>
	<script src="dist/js/CrudService.js"></script>
	<script src="dist/js/QueryService.js"></script>
	<script src="dist/js/WatchListService.js"></script>
	<script src="dist/js/WatchListController.js"></script>
	<script src="dist/js/AdminController.js"></script>
	<script src="dist/js/UserController.js"></script>
//-->
	</body>
</html>