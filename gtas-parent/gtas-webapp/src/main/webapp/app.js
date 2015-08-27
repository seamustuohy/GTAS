var app = angular.module('myApp', [
    'ui.router',
    'ui.bootstrap',
    'ngTable',
    'spring-security-csrf-token-interceptor',
    'ui.grid',
    'ui.grid.pagination',
    'ui.grid.edit',
    'ui.grid.rowEdit',
    'ui.grid.cellNav',
    'ui.grid.selection',
    'ui.grid.exporter'
]);

app.controller('NavCtrl', function ($scope, $location) {
    $scope.isActive = function (route) {
        $scope.path = $location.path();
        return $location.path() === route;
    };
});

app.config(function ($stateProvider) {
    $stateProvider
        .state('admin', {
            url: '/admin',
            templateUrl: 'admin/admin.html'
        })
        .state('dashboard', {
            url: '',
            templateUrl: 'dashboard/dashboard.html',
            controller: 'DashboardController'
        })
        .state('flights', {
            url: '/flights',
            templateUrl: 'flights/flights.html',
            controller: 'FlightsController'
        })
        .state('passengers', {
            url: '/passengers',
            templateUrl: 'pax/pax.html',
            controller: 'PaxController'
        })
        .state('pax', {
            url: '/pax',
            templateUrl: 'pax/pax.html',
            controller: 'PaxController'
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
});
