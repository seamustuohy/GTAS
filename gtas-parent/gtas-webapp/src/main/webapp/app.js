var app = angular.module('myApp', [
	'ui.router', 
	'ui.bootstrap', 
	'ngTable'
]);

app.config(function($stateProvider){
$stateProvider
    .state('flights', {
        url: '/flights',
        templateUrl: 'flights/flights.html',
        controller: 'FlightsController'
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

