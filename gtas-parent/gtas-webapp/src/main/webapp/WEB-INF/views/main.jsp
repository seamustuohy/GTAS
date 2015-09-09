    <!DOCTYPE html>
        <html lang="en" ng-app="myApp">
        <head>
        <%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="dist/css/style.min.css" rel="stylesheet">
        <title>GTAS</title>

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

        <script src="dist/js/all.js"></script>
        </body>
        </html>
