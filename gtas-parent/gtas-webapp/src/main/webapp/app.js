var app = angular.module('myApp', [
	'ui.router', 
	'ui.bootstrap', 
	'ngTable'
]);

app.config(function($stateProvider){
$stateProvider
/*
    .state('home_old', {
        url: '/home',
        views: {
            'flights': {
            templateUrl: 'flights/flights.html',
            controller: 'FlightsController'
            },
            'pax': {
            templateUrl: 'pax/pax.html',
            controller: 'PaxController'
            }
        }
    })
    */

    .state('flights', {
        url: '',
        templateUrl: 'flights/flights.html',
        controller: 'FlightsController'
    })
    .state('pax', {
        url: '/pax',
        templateUrl: 'pax/pax.html',
        controller: 'PaxController'
    })
    .state('query', {
        url: '/query',
        templateUrl: 'query/query.html'
    })
    .state('admin', {
        url: '/admin',
        templateUrl: 'admin/admin.html'
    })
})

app.controller('TabsCtrl', function ($scope, $window, $state) {
  $scope.gotoState = function(s) {
  	$state.go(s);
  };
});

