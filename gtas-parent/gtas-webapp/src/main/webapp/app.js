var app = angular.module('myApp', [
    'ui.router',
    'ct.ui.router.extras',
    'ui.grid',
    'ui.grid.resizeColumns',
    'ui.grid.moveColumns',
    'ui.grid.pagination',
    'ui.grid.edit',
    'ui.grid.rowEdit',
    'ui.grid.cellNav',
    'ui.grid.selection',
    'ui.grid.exporter',
    'ui.grid.expandable',
    'ui.bootstrap',
    'ngMaterial',
    'ngMessages',
    'ngAria',
    'ngAnimate',
    'angularSpinners'
]);

app.controller('NavCtrl', function ($scope, $location) {
    'use strict';
    var routes = ['/dashboard', '/flights', '/passengers', '/query-builder', '/risk-criteria', '/watchlists', '/admin'];
    //DEFAULTS to Flights on SPA reload and initial load
    $scope.selectedIndex = 1;
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
            url: '/',
            sticky: true,
            dsr: true,
            views: {
                'header': {
                    template: 'partials/flights-all-header.html'
                    // controller...
                },
                'date-range': {
                    template: 'partials/data-range.html'
                    // controller...
                },
                '': {
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
                    templateUrl: 'pax/pax.table.html',
                    resolve: {
                        passengers: function ($http, $stateParams, paxService) {
                            console.log($stateParams);
                            var id= $stateParams.flight.id;
                            console.log(id);
                            return paxService.getPax(id);
                        }
                    }
                }
            }
        }).state('pax', {
        url: '/passengers',
        controller: 'PaxMainController',
        templateUrl: 'pax/pax.header.html'
        })
     .state('pax.all', {
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
                        return paxService.getPax(1);
                    }
                }
            }
        }
    }).state('pax.detail', {
        url: '/:id/:flightId',
        sticky: true,
        dsr: true,
        views: {
            "content@pax": {
                controller: 'PaxDetailController',
                templateUrl: 'pax/pax.detail.html',
                resolve: {
                    passengers: function ($http, $stateParams, paxDetailService) {
                        return paxDetailService.getPaxDetail($http, $stateParams);
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
