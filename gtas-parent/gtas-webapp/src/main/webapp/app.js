var app;
(function () {
    'use strict';
    var pageDefaults = {
            pageNumber: 1,
            pageSize: 10
        },
        appDependencies = [
            'ui.router',
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
            'angularSpinners',
            'ngFileUpload'
        ],
        localDateMomentFormat = function ($mdDateLocaleProvider) {
            $mdDateLocaleProvider.formatDate = function (date) {
                return moment(date).format('YYYY-MM-DD');
            };
        },
        initialize = function ($rootScope) {
            $rootScope.$on('$stateChangeStart', function (e, toState, toParams) {
                $rootScope.$broadcast('stateChanged', toState, toParams);
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
                        "@": {
                            controller: 'AdminCtrl',
                            templateUrl: 'admin/admin.html'
                        }
                    }
                })
                .state('modifyUser', {
                    url: '/user/:userId',
                    params: {
                        action: 'modify',
                        user: null
                    },
                    views: {
                        '@': {
                            controller: 'UserCtrl',
                            templateUrl: 'admin/user.html'
                        }
                    }
                })
                .state('addUser', {
                    url: '/user/new',
                    params: {
                        action: 'create',
                        user: null
                    },
                    views: {
                        "@": {
                            controller: 'UserCtrl',
                            templateUrl: 'admin/user.html'
                        }
                    }
                })
                .state('upload', {
                    url: '/upload',
                    views: {
                        '@': {
                            controller: 'UploadCtrl',
                            templateUrl: 'admin/upload.html'
                        }
                    }
                })
                .state('flights', {
                    url: '/flights',
                    views: {
                        '@': {
                            controller: 'FlightsController',
                            templateUrl: 'flights/flights.html'
                        }
                    },
                    resolve: {
                        flights: function (flightService, flightsModel) {
                            return flightService.getFlights(flightsModel);
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
                        passengers: function (paxService, paxModel) {
                            return paxService.getAllPax(paxModel.model);
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
                        paxModel: function ($stateParams, paxModel) {
                            return {
                                model: paxModel.initial($stateParams),
                                reset: function() { this.model.lastName = ''; }
                            };
                        },
                        passengers: function (paxService, $stateParams, paxModel) {
                            //because of field/model not standard
                            $stateParams.dest = $stateParams.destination;
                            $stateParams.etaStart = $stateParams.eta;
                            $stateParams.etaEnd = $stateParams.etd;
                            return paxService.getPax($stateParams.id, paxModel.model);
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
                .state('build', {
                    url: '/build/:mode',
                    views: {
                        '@': {
                            controller: 'BuildController',
                            templateUrl: 'build/build.html'
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
                }).state('user-settings', {
                    url: '/user-settings',
                    views: {
                        '@': {
                            controller: 'UserSettingsController',
                            templateUrl: 'user-settings/user-settings.html'
                        }
                    },
                    resolve: {
                        user: function (userService) {
                            return userService.getUserData();
                        }
                    }
                });
        },
        NavCtrl = function ($scope) {
            var lookup = {
                admin: { name: ['admin', 'addUser', 'modifyUser'] },
                dashboard: { name: ['dashboard'] },
                flights: { name: ['flights'] },
                passengers: { name: ['paxAll', 'flightpax'] },
                queries: { mode: ['query'] },
                risks: { mode: ['rule'] },
                watchlists: { name: ['watchlists'] },
                usersettings: { name: ['user-settings'] },
                upload: { name: ['upload'] }
            };
            $scope.onRoute = function (key) {
                return (lookup[key].name && lookup[key].name.indexOf($scope.stateName) >= 0) || (lookup[key].mode && lookup[key].mode.indexOf($scope.mode) >= 0);
            };
            $scope.showNav = function () {
                return ['queryFlights', 'queryPassengers', 'detail'].indexOf($scope.stateName) === -1;
            };
            $scope.$on('stateChanged', function (e, state, toParams) {
                $scope.stateName = state.name;
                $scope.mode = toParams.mode;
            });
        };

    app = angular
        .module('myApp', appDependencies)
        .config(router)
        .config(localDateMomentFormat)
        .run(initialize)
        .controller('NavCtrl', NavCtrl);
}());
