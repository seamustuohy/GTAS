var app;
(function () {
    'use strict';
    var initialize = function ($rootScope) {
            //these two are for learning router state
            $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                // LOGIC TO PROMPT LOGIN AUTOMATICALLY
                //if (toState.name !== 'login' && !UsersService.getCurrentUser()) {
                //    event.preventDefault();
                //    $state.go('login');
                //}
            });
            $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
                console.log('toState:' + toState.name);
                console.log(toParams);
                console.log('fromState: ' + fromState.name);
                console.log(fromParams);
            });
        },
        router = function ($stateProvider) {
            $stateProvider
                .state('dashboard', {
                    url: '/dashboard',
                    templateUrl: 'dashboard/dashboard.html',
                    controller: 'DashboardController'
                })
                .state('admin', {
                    url: '/admin',
                    templateUrl: 'admin/admin.header.html',
                    controller: 'AdminCtrl'
                })
                .state('admin.users', {
                    url: '/',
                    sticky: true,
                    dsr: true,
                    views: {
                        "content@admin": {
                            templateUrl: 'admin/admin.html'
                        }
                    }
                })
                .state('admin.addUser', {
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
                })
                .state('flights', {
                    url: '/flights',
                    templateUrl: 'flights/flights.header.html',
                    controller: 'FlightsController'
                })
                .state('flights.all', {
                    sticky: true,
                    dsr: true,
                    views: {/*
                     'header@flights': {
                     templateUrl: 'partials/flights-all-header.html'
                     },*/
                        "content@flights": {
                            templateUrl: 'flights/flights.html'
                        }
                    }
                })
                .state('flights.passengers', {
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
                })
                .state('queryFlights', {
                    url: '/query/flights',
                    params: {
                        parent: 'query'
                    },
                    controller: 'FlightsController',
                    templateUrl: 'flights/flights.html',
                    resolve: {
                        queryResults: function (executeQueryService) {
                            var qbData = JSON.parse(localStorage['qbData']);
                            return executeQueryService.queryFlights(qbData);
                        }
                    }
                })
                .state('queryPassengers', {
                    url: '/query/passengers',
                    params: {
                        parent: 'query'
                    },
                    controller: 'PaxController',
                    templateUrl: 'pax/pax.table.html',
                    resolve: {
                        queryResults: function (executeQueryService) {
                            var qbData = JSON.parse(localStorage['qbData']);
                            return executeQueryService.queryPassengers(qbData);
                        }
                    }
                })
                .state('flights.passengers.detail', {
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
                })
                .state('detail', {
                    url: '/paxdetail/{paxId}/{flightId}',
                    templateUrl: 'pax/pax.detail.html',
                    controller: 'PassengerDetailCtrl',
                    resolve: {
                        passenger: function (paxDetailService, $stateParams) {
                            return paxDetailService.getPaxDetail($stateParams.paxId, $stateParams.flightId);
                        }
                    }
                })
                .state('pax', {
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
                            controller: 'PaxController',
                            templateUrl: 'pax/pax.table.html'
                        }
                    }
                })
                .state('query-builder', {
                    url: '/query-builder',
                    templateUrl: 'query-builder/query.html',
                    controller: 'QueryBuilderController'
                })
                .state('risk-criteria', {
                    url: '/risk-criteria',
                    templateUrl: 'risk-criteria/risk-criteria.html',
                    controller: 'RiskCriteriaController'
                })
                .state('watchlists', {
                    url: '/watchlists',
                    templateUrl: 'watchlists/watchlists.html',
                    controller: 'WatchListController'
                });
        };

    app = angular.module('myApp', [
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
    ])
        .config(router)
        .run(initialize);
}());
