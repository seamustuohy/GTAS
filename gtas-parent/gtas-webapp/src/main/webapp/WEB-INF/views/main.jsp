    <!DOCTYPE html>
        <html lang="en" ng-app="myApp">
        <head>
        <%@ taglib prefix="sec"
                   uri="http://www.springframework.org/security/tags" %>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>GTAS</title>

        <link rel="stylesheet" href="resources/css/style-icomoon.css" />
        <link rel="stylesheet" href="resources/bower_components/bootstrap/dist/css/bootstrap.css" />
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
        <link rel="stylesheet" href="resources/css/query-builder.default.css" />
        <link rel="stylesheet" href="resources/bower_components/angular-material/angular-material.min.css" />
        </head>

        <body>
        <header class="header" title="GLOBAL TRAVEL ASSESSMENT SYSTEM"></header>
        <nav data-ng-controller="NavCtrl">
        <ul class="nav navbar-nav navbar-right" style="margin: 0;">
        <li><a href="logout.action">LOGOUT</a></li>
        </ul>
        <md-tabs md-stretch-tabs md-selected="selectedIndex">
        <sec:authorize access="hasAnyAuthority('View Flight And Passenger','Admin')">
            <md-tab label="Flights"></md-tab>
            <md-tab label="Passengers"></md-tab>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('Manage Queries', 'Admin')">
            <md-tab label="Queries"></md-tab>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('Manage Rules', 'Admin')">
            <md-tab label="Risk Criteria"></md-tab>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('Manage Watch List','Admin')">
            <md-tab label="Watchlist"></md-tab>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('Admin')">
            <md-tab label="Admin"></md-tab>
        </sec:authorize>

        </md-tabs>
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
        <script src='dist/js/bower.components.min.js'></script>
        <script src='resources/js/query-builder.js'></script>

        <script src='app.js'></script>
        <script src='common/filters.js'></script>
        <script src='common/services.js'></script>
        <script src='factory/ModalGridFactory.js'></script>
        <script src='factory/QueryBuilderFactory.js'></script>
        <script src='factory/JqueryQueryBuilderWidget.js'></script>
        <script src='dashboard/DashboardController.js'></script>
        <script src='flights/FlightsController.js'></script>
        <script src='flights/FlightsService.js'></script>
        <script src='nav/NavController.js'></script>
        <script src='pax/PaxController.js'></script>
        <script src='pax/PaxMainController.js'></script>
        <script src='pax/PaxService.js'></script>
        <script src='pax/PaxFactory.js'></script>
        <script src="pax/PassengerDetail.js"></script>
        <script src='query-builder/QueryBuilderController.js'></script>
        <script src='risk-criteria/RiskCriteriaController.js'></script>
        <script src='watchlists/WatchListController.js'></script>
        <script src='admin/AdminController.js'></script>
        <script src='admin/UserController.js'></script>
        </body>
        </html>