var app;
(function () {
    'use strict';
    var pageDefaults = {
            pageNumber: 1,
            pageSize: 10
        },
        appDependencies = [
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
        ],
        initialize = function ($rootScope) {
            $rootScope.$on('$stateChangeStart', function (e, toState) {
                $rootScope.$broadcast('stateChanged', toState.name);
            });
        },
        router = function ($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.otherwise("/flights");
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
                    sticky: true,
                    dsr: true,
                    templateUrl: 'flights/flights.html',
                    controller: 'FlightsController',
                    resolve: {
                        flights: function (flightService, flightsModel) {
                            return flightService.getFlights(flightsModel);
                        }
                    }
                })
                .state('queryFlights', {
                    url: '/query/flights',
                    controller: 'FlightsController',
                    templateUrl: 'flights/query-flights.html',
                    resolve: {
                        flights: function (executeQueryService, $stateParams) {
                            var postData, query = JSON.parse(localStorage['query']);
                            postData = {
                                pageNumber: $stateParams.pageNumber || pageDefaults.pageNumber,
                                pageSize: $stateParams.pageSize || pageDefaults.pageSize,
                                query: query
                            };
                            return executeQueryService.queryFlights(postData);
                        }
                    }
                })
                .state('paxAll', {
                    url: '/passengers',
                    templateUrl: 'pax/pax.table.html',
                    controller: 'PaxController',
                    resolve: {
                        passengers: function (paxService, paxModel) {
                            return paxService.getAllPax(paxModel);
                        }
                    }
                })
                .state('flightpax', {
                    url: '/flightpax/:id/:flightNumber/:origin/:destination/:direction/:eta/:etd',
                    templateUrl: 'pax/pax.table.html',
                    controller: 'PaxController',
                    resolve: {
                        passengers: function (paxService, $stateParams, paxModel) {
                            //because of field/model not standard
                            $stateParams.dest = $stateParams.destination;
                            $stateParams.etaStart = $stateParams.eta;
                            $stateParams.etaEnd = $stateParams.etd;
                            //var model = paxModel;
                            paxModel.reset($stateParams);
                            return paxService.getPax($stateParams.id, paxModel);
                        }
                    }
                })
                .state('queryPassengers', {
                    url: '/query/passengers',
                    templateUrl: 'pax/pax.table.html',
                    controller: 'PaxController',
                    resolve: {
                        passengers: function (executeQueryService, $stateParams) {
                            var postData, query = JSON.parse(localStorage['query']);
                            postData = {
                                pageNumber: $stateParams.pageNumber || pageDefaults.pageNumber,
                                pageSize: $stateParams.pageSize || pageDefaults.pageSize,
                                query: query
                            };
                            return executeQueryService.queryPassengers(postData);
                        }
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
        },
        NavCtrl = function ($scope) {
            var lookup = {
                admin: ['admin', 'admin.users', 'admin.addUser'],
                dashboard: ['dashboard'],
                flights: ['flights'],
                passengers: ['paxAll', 'flightpax'],
                queries: ['query-builder'],
                risks: ['risk-criteria'],
                watchlists: ['watchlists']
            };
            $scope.onRoute = function (stateName) {
                return lookup[stateName].indexOf($scope.stateName) >= 0;
            };
            $scope.showNav = function () {
                return ['queryFlights', 'queryPassengers', 'detail'].indexOf($scope.stateName) === -1;
            };
            $scope.$on('stateChanged', function (e, stateName) {
                $scope.stateName = stateName;
            });
        };

    app = angular
        .module('myApp', appDependencies)
        .config(router)
        .run(initialize)
        .controller('NavCtrl', NavCtrl);
}());
