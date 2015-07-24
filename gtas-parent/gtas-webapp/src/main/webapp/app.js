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
     .state('travelers', {
        url: '/travelers',
        templateUrl: 'pax/pax.html',
        controller: 'PaxController'
    })
    .state('pax', {
        url: '/pax',
        templateUrl: 'pax/pax.html',
        controller: 'PaxController'
    })
    .state('risk-criteria', {
        url: '/risk-criteria',
        templateUrl: 'risk-criteria/risk-criteria.html',
        controller: 'RiskCriteriaController'
    })
    .state('query-builder', {
        url: '/query-builder',
        templateUrl: 'query-builder/query.html',
        controller: 'QueryBuilderController'
    })
    .state('admin', {
        url: '/admin',
        templateUrl: 'admin/admin.html'
    })
})
