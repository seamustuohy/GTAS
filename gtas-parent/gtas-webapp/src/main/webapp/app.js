var app;
(function () {
    'use strict';
    function PassengerDetailCtrl($scope, passenger) {
        $scope.passenger = passenger.data;
        $scope.paxTableEnabled = false;
        //date modifications ** NOT SURE need to make these date objects no calendar picker will confer with Maneesh reason behind it.
        $scope.passenger.flightETA = new Date($scope.passenger.flightETA);
        $scope.passenger.flightETD = new Date($scope.passenger.flightETD);
        $scope.passenger.dob = new Date($scope.passenger.dob);
    }

    function paxDetailService($http, $q) {
        function getPaxDetail(paxId, flightId) {
            var dfd = $q.defer();
            dfd.resolve($http.get("/gtas/passengers/passenger/" + paxId + "/details?flightId=" + flightId));
            return dfd.promise;
        }
        return ({getPaxDetail: getPaxDetail});
    }
    function initialize($rootScope, $location) {
        $rootScope.$on('$stateChangeStart',
            function (event, toState, toParams, fromState, fromParams) {
                console.log('stateChangeStart');
                console.log(toState.name);
                // LOGIC TO PROMPT LOGIN AUTOMATICALLY
                //if (toState.name !== 'login' && !UsersService.getCurrentUser()) {
                //    event.preventDefault();
                //    $state.go('login');
                //}
            });
    }

    function resolvePassenger(paxDetailService, $stateParams) {
        //var regex = /[?&]([^=#]+)=([^&#]*)/g,
        //    url = window.location.href,
        //    params = {},
        //    match;
        //while (match = regex.exec(url)) {
        //    params[match[1]] = match[2];
        //}
        //return paxDetailService.getPaxDetail(params.paxId, params.flightId);
        console.log('$stateParams');
        console.log($stateParams);
        return paxDetailService.getPaxDetail($stateParams.paxId, $stateParams.flightId);
    }
    function NavCtrl($scope, $location, $state) {
        var routes = ['/flights', '/passengers/', '/query-builder', '/risk-criteria', '/watchlists', '/admin'],
            route = window.location.hash.split('?')[0].replace('#', '');

        if (!route.length) {
            route = '/flights';
        }

        $scope.selectedIndex = routes.indexOf(route) >= 0 ? routes.indexOf(route) : null;

        if (route.indexOf('/paxdetail') >= 0) {
            $state.go('detail');
            route = window.location.hash.replace('#', '');
            $('nav').remove();
        }

        $location.url(route);

        if ($scope.selectedIndex !== null) {
            $scope.$watch('selectedIndex', function (current) {
                $location.url(routes[current]);
            });
        }
    }
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
        .service('paxDetailService', paxDetailService)
        .controller('NavCtrl', NavCtrl)
        .controller('PassengerDetailCtrl', PassengerDetailCtrl)
        .config(function ($stateProvider) {
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
                        passenger: resolvePassenger
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
                .state('query-flights', {
                    url: '/query/flights',
                    templateUrl: 'flights/flights.html',
                    controller: 'ExecuteQueryController'
                })
                .state('test', {
                    url: '/test',
                    templateUrl: 'test.html',
                    controller: 'ExecuteQueryController'
                })
                .state('query-passengers', {
                    url: '/query/passengers',
                    templateUrl: 'pax/pax.table.html',
                    controller: 'ExecuteQueryController'
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
        })
        .run(initialize);
})();
