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
                    views: {
                        '@': {
                            controller: 'DashboardController',
                            templateUrl: 'dashboard/dashboard.html'
                        }
                    }
                })
                .state('admin', {
                    url: '/admin',
                    views: {
                        '@': {
                            controller: 'AdminCtrl',
                            templateUrl: 'admin/admin.header.html'
                        }
                    }
                })
                .state('admin.users', {
                    url: '/',
                    sticky: true,
                    dsr: true,
                    views: {
                        "@admin": {
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
                        "@admin": {
                            controller: 'UserCtrl',
                            templateUrl: 'admin/user.html'
                        }
                    }
                })
                .state('flights', {
                    url: '/flights',
                    sticky: true,
                    dsr: true,
                    views: {
                        '@': {
                            controller: 'FlightsController',
                            templateUrl: 'flights/flights.html'
                        }
                    },
                    resolve: {
                        flights: function (flightService) {
                            return flightService.getFlights(flightService.model);
                        }
                    }
                })
                .state('queryFlights', {
                    url: '/query/flights',
                    views: {
                        '@': {
                            controller: 'FlightsController',
                            templateUrl: 'flights/query-flights.html'
                        }
                    },
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
                    views: {
                        '@': {
                            controller: 'PaxController',
                            templateUrl: 'pax/pax.table.html'
                        }
                    },
                    resolve: {
                        passengers: function (paxService) {
                            return paxService.getAllPax(paxService.model);
                        }
                    }
                })
                .state('flightpax', {
                    url: '/flightpax/:id/:flightNumber/:origin/:destination/:direction/:eta/:etd',
                    views: {
                        '@': {
                            controller: 'PaxController',
                            templateUrl: 'pax/pax.table.html'
                        }
                    },
                    resolve: {
                        passengers: function (paxService, $stateParams) {
                            //because of field/model not standard
                            $stateParams.dest = $stateParams.destination;
                            $stateParams.etaStart = $stateParams.eta;
                            $stateParams.etaEnd = $stateParams.etd;
                            return paxService.getPax($stateParams.id, paxService.initialModel($stateParams));
                        }
                    }
                })
                .state('queryPassengers', {
                    url: '/query/passengers',
                    views: {
                        '@': {
                            controller: 'PaxController',
                            templateUrl: 'pax/pax.table.html'
                        }
                    },
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
                    views: {
                        '@': {
                            controller: 'PassengerDetailCtrl',
                            templateUrl: 'pax/pax.detail.html',
                        }
                    },
                    resolve: {
                        passenger: function (paxDetailService, $stateParams) {
                            return paxDetailService.getPaxDetail($stateParams.paxId, $stateParams.flightId);
                        }
                    }
                })
                .state('query-builder', {
                    url: '/query-builder',
                    views: {
                        '@': {
                            controller: 'QueryBuilderController',
                            templateUrl: 'query-builder/query.html'
                        }
                    }
                })
                .state('risk-criteria', {
                    url: '/risk-criteria',
                    views: {
                        '@': {
                            controller: 'RiskCriteriaController',
                            templateUrl: 'risk-criteria/risk-criteria.html'
                        }
                    }
                })
                .state('watchlists', {
                    url: '/watchlists',
                    views: {
                        '@': {
                            controller: 'WatchListController',
                            templateUrl: 'watchlists/watchlists.html'
                        }
                    }
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
