    <!DOCTYPE html>
        <html lang="en" ng-app="myApp">
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
        <!-- <link rel="stylesheet" href="resources/css/bootstrap-combined.min.css" /> //-->
        <link rel="stylesheet" href="resources/bower_components/bootstrap/dist/css/bootstrap.css">
        <link rel="stylesheet" href="resources/bower_components/ng-table/dist/ng-table.css" />
        <link rel="stylesheet" href="resources/css/gtas.css" />

        <link rel="stylesheet" href="resources/bower_components/bootstrap-select/dist/css/bootstrap-select.min.css" />
        <link rel="stylesheet"
        href="resources/bower_components/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" />
        <link rel="stylesheet"
        href="resources/bower_components/seiyria-bootstrap-slider/dist/css/bootstrap-slider.min.css" />
        <link rel="stylesheet" href="resources/bower_components/selectize/dist/css/selectize.bootstrap3.css" />
        <link rel="stylesheet"
        href="resources/bower_components/bootstrap-datepicker/dist/css/bootstrap-datepicker.min.css" />
        <link rel="stylesheet"
        href="resources/bower_components/eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.min.css"
        />

        <link rel="stylesheet" href="resources/css/query-builder.default.css" id="qb-theme" />

        <link rel="stylesheet" href="http://mistic100.github.io/jQuery-QueryBuilder/assets/flags/flags.css" />

        <style type="text/css">
        .header{
        background: rgb(247, 250, 250);
        }

        .hiddenRow {
        padding: 0 !important;

        }

        /* .accordian-body {
        margin-left: 50px;
        } */

        #paxTable thead th {
        text-align:center;
        }

        #paxTable tbody td {
        text-align:center;
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
        <!-- <div class="navbar-header">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
        aria-expanded="false" aria-controls="navbar">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        </button>
        </div> -->

        <div id="navbar" class="collapse navbar-collapse">
        <ul class="nav navbar-nav navbar-left">
        <li class="active"><a href="home.action">Home</a></li>

        <sec:authorize access="hasAnyAuthority('VIEW_FLIGHT_PASSENGERS','ADMIN')">

            <li class="dropdown">
            <a href="" class="dropdown-toggle" data-toggle="dropdown" role="button">View <span class="caret"></span></a>
            <ul class="dropdown-menu">

            <li><a href="home.action">Flights</a></li>
            <li><a href="home.action">Passengers</a></li>

            </ul>
            </li>
        </sec:authorize>

        <sec:authorize access="hasAnyAuthority('MANAGE_RULES', 'MANAGE_QUERIES', 'MANAGE_WATCHLIST','ADMIN')">
            <li class="dropdown">
            <a href="" class="dropdown-toggle" data-toggle="dropdown" role="button">Manage <span
            class="caret"></span></a>
            <ul class="dropdown-menu">
            <sec:authorize access="hasAnyAuthority('MANAGE_RULES', 'MANAGE_QUERIES', 'MANAGE_WATCHLIST','ADMIN')">
                <li><a href="home.action?#/query-builder">Queries</a></li>
            </sec:authorize>
            <sec:authorize access="hasAnyAuthority('MANAGE_RULES', 'MANAGE_QUERIES', 'MANAGE_WATCHLIST','ADMIN')">
                <li><a href="home.action?#/risk-criteria">Risk Criteria</a></li>
            </sec:authorize>
            <sec:authorize access="hasAnyAuthority('MANAGE_RULES', 'MANAGE_QUERIES', 'MANAGE_WATCHLIST','ADMIN')">
                <li><a href="">Watchlists</a></li>
            </sec:authorize>
            </ul>
            </li>
        </sec:authorize>


        <sec:authorize access="hasAnyAuthority('MANAGE_USERS','ADMIN')">
            <li class="dropdown">
            <a href="" class="dropdown-toggle" data-toggle="dropdown" role="button">Admin <span
            class="caret"></span></a>
            <ul class="dropdown-menu">

            <sec:authorize access="hasAnyAuthority('MANAGE_RULES','ADMIN')">
                <li><a href="">Users</a></li>
            </sec:authorize>

            <sec:authorize access="hasAnyAuthority('ADMIN')">
                <li><a href="">Properties</a></li>
            </sec:authorize>

            </ul>
            </li>
        </sec:authorize>

        <li class="dropdown">
        <a href="" class="dropdown-toggle" data-toggle="dropdown" role="button">Settings <span class="caret"></span></a>
        <ul class="dropdown-menu">
        <li><a href="">User Settings</a></li>
        </ul>
        </li>

        </ul>

        <ul class="nav navbar-nav navbar-right">
        <li style="position: right;">
        <a href="logout.action">Logout</a>
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
        <script src="resources/bower_components/angular/angular.js"></script>
        <script src="resources/bower_components/angular-ui-router/release/angular-ui-router.js"></script>
        <script src="resources/bower_components/angular-bootstrap/ui-bootstrap.js"></script>
        <script src="resources/bower_components/angular-bootstrap/ui-bootstrap-tpls.js"></script>
        <script src="resources/bower_components/ng-table/dist/ng-table.js"></script>
        <script src="resources/bower_components/spring-security-csrf-token-interceptor/dist/spring-security-csrf-token-interceptor.min.js"></script>
        <script src="resources/bower_components/moment/min/moment.min.js"></script>

        <script src="resources/bower_components/jquery/dist/jquery.js"></script>
        <script src="resources/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="resources/bower_components/bootstrap-select/dist/js/bootstrap-select.min.js"></script>
        <script src="resources/bower_components/bootbox/bootbox.js"></script>
        <script src="resources/bower_components/seiyria-bootstrap-slider/dist/bootstrap-slider.min.js"></script>
        <script src="resources/bower_components/selectize/dist/js/standalone/selectize.min.js"></script>
        <script src="resources/bower_components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
        <script src="resources/bower_components/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js"></script>
        <script src="resources/bower_components/jquery-extendext/jQuery.extendext.min.js"></script>

        <script src="resources/js/query-builder.js"></script>

        <script src="app.js"></script>
        <script src="flights/FlightsController.js"></script>
        <script src="flights/FlightsService.js"></script>
        <script src="pax/PaxController.js"></script>
        <script src="pax/PaxService.js"></script>
        <script src="risk-criteria/RiskCriteriaController.js"></script>
        <script src="risk-criteria/RiskCriteriaService.js"></script>
        <script src="query-builder/QueryBuilderController.js"></script>
        <script src="query-builder/QueryBuilderService.js"></script>
        <script src="query-builder/QueryService.js"></script>
        </body>
        </html>
