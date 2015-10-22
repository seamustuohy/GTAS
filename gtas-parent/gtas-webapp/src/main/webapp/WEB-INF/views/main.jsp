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
        <link rel="stylesheet" href="resources/bower_components/bootstrap-select/dist/css/bootstrap-select.min.css" />
        <link rel="stylesheet"
        href="resources/bower_components/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" />
        <link rel="stylesheet"
        href="resources/bower_components/seiyria-bootstrap-slider/dist/css/bootstrap-slider.min.css" />
        <link rel="stylesheet" href="resources/bower_components/selectize/dist/css/selectize.bootstrap3.css" />
        <link rel="stylesheet"
        href="resources/bower_components/bootstrap-datepicker/dist/css/bootstrap-datepicker.min.css" />
        <link rel="stylesheet" href="resources/bower_components/angular-ui-grid/ui-grid.min.css" />
        <link rel="stylesheet" href="resources/css/query-builder.default.css" />
        <link rel="stylesheet" href="resources/bower_components/angular-material/angular-material.min.css" />
        <link rel="stylesheet" href="resources/css/gtas.css" />
        </head>

        <body>
        <header class="header" title="GLOBAL TRAVEL ASSESSMENT SYSTEM"></header>
        <nav data-ng-controller="NavCtrl">
        <div class="nav navbar-nav navbar-right" ng-if="showNav()" style="margin: 0;">
        <md-button class="md-button" href="logout.action">Logout</md-button>
        </div>
        <div class="nav navbar-nav navbar-right" ng-if="showNav() == false" style="margin: 0;">
        <md-button class="md-button" onclick="window.close()">X</md-button>
        </div>
        <span data-ng-if="showNav()">
        <sec:authorize access="hasAnyAuthority('View Flight And Passenger','Admin')">
            <md-button class="md-button" ng-class="{'md-raised md-primary': onRoute('flights')}"
            ng-disabled="onRoute('flights')" href="#/flights"><i class="glyphicon glyphicon-plane"></i>  Flights</md-button>
            <md-button class="md-button" ng-class="{'md-raised md-primary': onRoute('passengers')}"
            ng-disabled="onRoute('passengers')" href="#/passengers"><i class="glyphicon glyphicon-user"></i>  Passengers</md-button>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('Manage Queries', 'Admin')">
            <md-button class="md-button" ng-class="{'md-raised md-primary': onRoute('queries')}"
            ng-disabled="onRoute('queries')" href="#/build/query"><i class="glyphicon glyphicon-filter"></i>  Queries</md-button>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('Manage Rules', 'Admin')">
            <md-button class="md-button" ng-class="{'md-raised md-primary': onRoute('risks')}"
            ng-disabled="onRoute('risks')" href="#/build/rule"><i class="glyphicon glyphicon-flag"></i>  Risk Criteria</md-button>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('Manage Watch List','Admin')">
            <md-button class="md-button" ng-class="{'md-raised md-primary': onRoute('watchlists')}"
            ng-disabled="onRoute('watchlists')" href="#/watchlists"><i class="glyphicon glyphicon-eye-open"></i>  Watchlist</md-button>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('Manage Queries','Manage Rules','Manage Watch List','View Flight And Passenger','Admin')">
            <md-button class="md-button" ng-class="{'md-raised md-primary': onRoute('usersettings')}"
            ng-disabled="onRoute('usersettings')" href="#/user-settings"><i class="glyphicon glyphicon-cog"></i> Settings</md-button>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('Admin')">
            <md-button class="md-button" ng-class="{'md-raised md-primary': onRoute('admin')}"
            ng-disabled="onRoute('admin')" href="#/admin"><i class="glyphicon glyphicon-edit"></i>  Admin</md-button>
        </sec:authorize>


        </span>
        </nav>
        <!-- end of nav -->
        <div ui-view="nav"></div>
        <div ui-view="header"></div>
        <div class="content" ui-view=""></div>
        <div ui-view="footer"></div>

        <script src='resources/bower_components/angular/angular.js'></script>
        <script src='resources/bower_components/angular-ui-router/release/angular-ui-router.min.js'></script>
        <script src='resources/bower_components/moment/min/moment.min.js'></script>
        <script src='resources/bower_components/jquery/dist/jquery.js'></script>
        <script src='resources/bower_components/bootstrap/dist/js/bootstrap.min.js'></script>
        <script src='resources/bower_components/bootstrap-select/dist/js/bootstrap-select.min.js'></script>
        <script src='resources/bower_components/bootbox/bootbox.js'></script>
        <script src='resources/bower_components/seiyria-bootstrap-slider/dist/bootstrap-slider.min.js'></script>
        <script src='resources/bower_components/selectize/dist/js/standalone/selectize.min.js'></script>
        <script src='resources/bower_components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js'></script>
        <script src='resources/bower_components/jquery-extendext/jQuery.extendext.min.js'></script>
        <script src='resources/bower_components/pdfmake/build/pdfmake.min.js'></script>
        <script src='resources/bower_components/pdfmake/build/vfs_fonts.js'></script>
        <script src='resources/bower_components/angular-ui-grid/ui-grid.js'></script>
        <script src='resources/bower_components/angular-material/angular-material.min.js'></script>
        <script src='resources/bower_components/angular-aria/angular-aria.min.js'></script>
        <script src='resources/bower_components/angular-spinners/dist/angular-spinners.min.js'></script>
        <script src='resources/bower_components/angular-animate/angular-animate.min.js'></script>
        <script src='resources/bower_components/angular-messages/angular-messages.min.js'></script>

        <script src='resources/js/query-builder.js'></script>

        <script src='app.js'></script>
        <script src='common/directives.js'></script>
        <script src='common/filters.js'></script>
        <script src='common/services.js'></script>
        <script src='factory/ModalGridFactory.js'></script>
        <script src='factory/JqueryQueryBuilderWidget.js'></script>
        <script src='dashboard/DashboardController.js'></script>
        <script src='flights/FlightsModel.js'></script>
        <script src='flights/FlightsController.js'></script>
        <script src='flights/FlightsService.js'></script>
        <script src='pax/PaxModel.js'></script>
        <script src='pax/PaxController.js'></script>
        <script src='pax/PaxService.js'></script>
        <script src='pax/PaxFactory.js'></script>
        <script src='build/BuildController.js'></script>
        <script src='watchlists/WatchListController.js'></script>
        <script src='admin/AdminController.js'></script>
        <script src='admin/UserController.js'></script>
        <script src='user-settings/UserSettingsController.js'></script>
        </body>
        </html>