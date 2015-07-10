var app = angular.module('myApp', [
	'ui.router', 
	'ui.bootstrap', 
	'ngTable',
	'spring-security-csrf-token-interceptor'
]);

app.config(function($stateProvider){
$stateProvider
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
        templateUrl: 'query/query.html',
        controller: 'QueryController'
    })
    .state('admin', {
        url: '/admin',
        templateUrl: 'admin/admin.html'
    })
})
