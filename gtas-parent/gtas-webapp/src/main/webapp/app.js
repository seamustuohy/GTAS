var app = angular.module('myApp', [
    'ui.router',
    'ct.ui.router.extras',
    'ui.grid',
    'ui.grid.resizeColumns',
    'ui.grid.moveColumns',
    'ui.grid.pagination',
    'ui.grid.autoResize',
    'ui.grid.edit',
    'ui.grid.rowEdit',
    'ui.grid.cellNav',
    'ui.grid.selection',
    'ui.grid.exporter',
    'ui.grid.expandable',
    'ngMaterial',
    'ngMessages',
    'ngAria',
    'ngAnimate',
    'angularSpinners'
]);

app.controller('NavCtrl', function ($scope, $location) {
    'use strict';
    var routes = ['/flights', '/passengers', '/query-builder', '/risk-criteria', '/watchlists', '/admin'];

    //DEFAULTS to first tab on SPA reload and initial load
    $scope.selectedIndex = 0;

    $location.url(routes[$scope.selectedIndex]);
    //IF WE MOVE SECURITY check TO HERE and out of .jsp
    $scope.$watch('selectedIndex', function (current) {
        $location.url(routes[current]);
    });
});

app.config(function ($stateProvider) {
    'use strict';
    $stateProvider.state('dashboard', {
        url: '/dashboard',
        templateUrl: 'dashboard/dashboard.html',
        controller: 'DashboardController'
    }).state('admin', {
        url: '/admin',
        templateUrl: 'admin/admin.header.html',
        controller: 'AdminCtrl'
    }).state('admin.users', {
        url: '/',
        sticky: true,
        dsr: true,
        views: {
            "content@admin": {
                templateUrl: 'admin/admin.html'
            }
        }
    }).state('admin.addUser', {
        url: '/user',
        params: {
            action: null,
            user: null
        },
        sticky: true,
        dsr: true,
        views: {
            "content@admin": {
                controller: 'UserCtrl',
                templateUrl: 'admin/user.html'
            }
        }
    }).state('flights', {
        url: '/flights',
        templateUrl: 'flights/flights.header.html',
        controller: 'FlightsController'
    }).state('flights.all', {
        sticky: true,
        dsr: true,
        views: {
        /*
            'header@flights': {
                templateUrl: 'partials/flights-all-header.html'
            },*/
            "content@flights": {
                templateUrl: 'flights/flights.html'
            }
        }
    }).state('flights.passengers', {
        url: '/passengers',
        params: {
            parent: null,
            flight: null
        },
        sticky: true,
        dsr: true,
        views: {
            "content@flights": {
                controller: 'PaxController',
                templateUrl: 'pax/pax.table.html'
            }
        }
    }).state('flights.passengers.detail', {
        url: '/detail',
        params: {
            parent: null,
            flight: null,
            id: null,
            flightId: null
        },
        "content@flights": {
            templateUrl: 'flights/flights.html'
        }
    }).state('pax', {
        url: '/passengers',
        controller: 'PaxMainController',
        templateUrl: 'pax/pax.header.html'
    }).state('pax.all', {
        url: '/',
        sticky: true,
        dsr: true,
        views: {
            "content@pax": {
                // controller: 'Paginate',
                controller: 'PaxController',
                templateUrl: 'pax/pax.table.html',
                resolve: {
                    passengers: function ($http, $stateParams, paxService) {
                        return null;  //paxService.getPax(1);
                    }
                }
            }
        }
    }).state('query-builder', {
        url: '/query-builder',
        templateUrl: 'query-builder/query.html',
        controller: 'QueryBuilderController'
    }).state('query-flights', {
        url: '/query/flights',
        templateUrl: 'flights/flights.html',
        controller: 'ExecuteQueryController'
    }).state('test', {
        url: '/test',
        templateUrl: 'test.html',
        controller: 'ExecuteQueryController'
    }).state('query-passengers', {
        url: '/query/passengers',
        templateUrl: 'pax/pax.table.html',
        controller: 'ExecuteQueryController'
    }).state('risk-criteria', {
        url: '/risk-criteria',
        templateUrl: 'risk-criteria/risk-criteria.html',
        controller: 'RiskCriteriaController'
    }).state('watchlists', {
        url: '/watchlists',
        templateUrl: 'watchlists/watchlists.html',
        controller: 'WatchListController'
    });
});
