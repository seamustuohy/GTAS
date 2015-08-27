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
        <link rel="stylesheet" href="resources/bower_components/angular-ui-grid/ui-grid.css" />

        <link rel="stylesheet" href="resources/css/query-builder.default.css" id="qb-theme" />

        <link rel="stylesheet" href="http://mistic100.github.io/jQuery-QueryBuilder/assets/flags/flags.css" />

        <style type="text/css">

        .full {
        background: url(http://placehold.it/1920x1080) no-repeat center center fixed;
        -webkit-background-size: cover;
        -moz-background-size: cover;
        -o-background-size: cover;
        background-size: cover;
        }

        /*        THIS OVERRIDES ALL NG-TABLES table headers
        please add class to table and scope by additional parent class-name
        you also have this same class name in the styles.css so should consolidate there
        .header{
        background: rgb(247, 250, 250);
        }
        */
        .hiddenRow {
        padding: 0 !important;

        }

        /* .accordian-body {
        margin-left: 50px;
        } */

        #flightTable thead th {
        text-align:center;
        }

        #flightTable tbody td {
        text-align:center;
        }

        #paxTable thead th {
        text-align:center;
        }

        #paxTable tbody td {
        text-align:center;
        }

        /* Exclusive for nested Rule List Display */
        .collapse {
        display: none;
        }
        .collapse.in {
        display: block;
        }
        tr.collapse.in {
        display: table-row;
        }
        tbody.collapse.in {
        display: table-row-group;
        }
        .collapsing {
        position: relative;
        height: 0;
        overflow: hidden;
        -webkit-transition: height .35s ease;
        -o-transition: height .35s ease;
        transition: height .35s ease;
        }

        /*					.glyphicon {
        margin: 1px 0px 5px 1px;
        font-size: 26px;
        color: #222;
        padding: 5px;}

        .glyphicon:hover, .glyphicon:focus {
        color: #222;
        padding: 5px;}
        */

        .glyph {
        text-align: center;
        }


        /* Bootstrap colors

        @brand-success: #5cb85c;
        @brand-danger:  #d9534f;

        */

        /* Exclusive for nested Rule List Display */


        .glyphiconFlightPax {
        margin: 1px 0px 5px 1px;
        font-size: 26px;
        color: #222;
        /*background-color: #F0C425;*/
        padding: 5px;}

        header {
        height: 84px;
        position: relative;
        }
        header::before {
        content: "";
        display: block;
        height: 84px;
        background-color: rgb(43,127,184);
        background-image: url(resources/img/gtas_logo.png);
        background-repeat: no-repeat;
        background-position: top left;
        position: relative;
        top: 0;
        }
        header::after {
        content: "";
        display: block;
        background-color: rgba(167,169,172, 1);
        height: 10px;
        position: relative;
        bottom: 10px;
        margin-left: 1170px;
        }

        .glyphiconFlightPax:hover, .glyphiconFlightPax:focus {
        color: #222;
        /* background-color: #FFFFFF;  #1F2E54 */
        padding: 5px;}


        .glyph {
        text-align: center;
        }

        @media (min-width: 768px) {
        .modal-dialog {
        width: 1000px;
        }
        }
        .ui-grid-pager-control input {
        width: 100px;
        }
        </style>

        </head>

        <body>
        <header></header>

        <div>
        <nav class="navbar navbar-default">
        <div id="navbar">
        <ul class="nav navbar-nav navbar-left" ng-controller="NavCtrl">
        <li ng-class="{active: isActive('')}"><a href="home.action">Dashboard</a></li>
        <sec:authorize access="hasAnyAuthority('VIEW_FLIGHT_PASSENGERS','ADMIN')">
            <li ng-class="{active: isActive('/flights')}"><a href="home.action#/flights">Flights</a></li>
            <li ng-class="{active: isActive('/passengers')}"><a href="home.action#/passengers">Passengers</a></li>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('MANAGE_QUERIES', 'ADMIN')">
            <li ng-class="{active: isActive('/query-builder')}"><a href="home.action#/query-builder">Queries</a></li>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('MANAGE_RULES', 'ADMIN')">
            <li ng-class="{active: isActive('/risk-criteria')}"><a href="home.action#/risk-criteria">Risk Criteria</a></li>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('MANAGE_WATCHLIST','ADMIN')">
            <li ng-class="{active: isActive('/watchlists')}"><a href="home.action#/watchlists">Watchlists</a></li>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('MANAGE_USERS','ADMIN')">
            <li class="dropdown">
            <a href="" class="dropdown-toggle" data-toggle="dropdown" role="button">Admin <span
            class="caret"></span></a>
            <ul class="dropdown-menu">

            <sec:authorize access="hasAnyAuthority('MANAGE_USERS','ADMIN')">
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

        <ul class="nav navbar-nav navbar-right" style="margin:0;">
        <li><a href="logout.action">Logout</a></li>
        </ul>
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
        <script src="resources/bower_components/jquery-extendext/jQuery.extendext.min.js"></script>
        <script src='resources/bower_components/pdfmake/build/pdfmake.min.js'></script>
        <script src='resources/bower_components/pdfmake/build/vfs_fonts.js'></script>

        <!-- <script src="resources/bower_components/datatables/media/js/jquery.dataTables.min.js"></script> -->
        <script src="resources/bower_components/angular-ui-grid/ui-grid.js"></script>

        <script src="resources/js/query-builder.js"></script>
        <script src="app.js"></script>
        <script src="dashboard/DashboardController.js"></script>
        <script src="factory/QueryBuilderFactory.js"></script>
        <script src="factory/jQueryBuilderFactory.js"></script>
        <script src="flights/FlightsIIController.js"></script>
        <script src="flights/FlightsService.js"></script>
        <script src="pax/PaxController.js"></script>
        <script src="pax/PaxService.js"></script>
        <script src="pax/PaxFactory.js"></script>
        <script src="query-builder/QueryBuilderController.js"></script>
        <script src="query-builder/QueryBuilderService.js"></script>
        <script src="query-builder/QueryService.js"></script>
        <script src="risk-criteria/RiskCriteriaController.js"></script>
        <script src="risk-criteria/RiskCriteriaService.js"></script>
        <script src="watchlists/WatchListService.js"></script>
        <script src="watchlists/WatchListController.js"></script>
        </body>
        </html>
